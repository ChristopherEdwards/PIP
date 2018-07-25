package com.sanchez.profile.schema.schemaeditor.wizards;

import com.fnf.studio.views.FidelityConsoleView;
import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.ui.SchemaInfo;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Attribute;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Entity;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Vector;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.xml.sax.SAXException;

public class NewEntityWizard extends Wizard implements INewWizard {
   private static String crlf = System.getProperties().getProperty("line.separator");
   private String consoleMessage = "";
   private FidelityConsoleView console;
   public EntityAttributeList attributeEntry = new EntityAttributeList();
   private boolean entityOverwriteFlag = true;
   private boolean attributeOverwriteFlag = true;
   private IWorkbenchPage page;
   public EntityPage0 entityPage0;
   public EntityPage1 entityPage1;
   public EntityPage2 entityPage2;
   public EntityPage3 entityPage3;
   public EntityPage4 entityPage4;
   public Entity entityBase = new Entity();
   public IContainer folder;
   private WizardsUtils wizardsUtils = new WizardsUtils();
   protected IStructuredSelection selection;
   protected IWorkbench workbench;

   public void addPages() {
      this.entityPage1 = new EntityPage1("Page1", this.entityBase, "entity");
      this.addPage(this.entityPage1);
      this.entityPage3 = new EntityPage3("Page3", this.entityBase, "entity");
      this.addPage(this.entityPage3);
      this.entityPage4 = new EntityPage4("Page4", "entity");
      this.addPage(this.entityPage4);
   }

   public void init(IWorkbench workbench, IStructuredSelection selection) {
      this.workbench = workbench;
      this.selection = selection;
      if (selection != null && !selection.isEmpty()) {
         Object obj = selection.getFirstElement();
         if (obj instanceof IResource) {
            if (obj instanceof IContainer) {
               this.folder = (IContainer)obj;
            } else {
               this.folder = ((IResource)obj).getParent();
            }
         }
      }

   }

   public boolean canFinish() {
      return this.getContainer().getCurrentPage() == this.entityPage4;
   }

   public boolean performFinish() {
      if (!this.entityPage4.isPageComplete()) {
         return false;
      } else if (this.folder == null) {
         return true;
      } else {
         this.page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
         this.console = (FidelityConsoleView)this.page.findView("com.fnf.studio.views.FidelityConsoleView");
         if (!this.entityBase.getEntityName().getValue().equals(this.folder.getName().toUpperCase())) {
            IFolder tableFolder = (IFolder)this.folder.findMember(this.entityBase.getEntityName().getValue().toLowerCase());
            if (tableFolder == null) {
               try {
                  IWorkspaceRoot root = this.folder.getWorkspace().getRoot();
                  IPath path = this.folder.getFullPath().append(this.entityBase.getEntityName().getValue().toLowerCase());
                  tableFolder = root.getFolder(path);
                  tableFolder.create(false, true, (IProgressMonitor)null);
                  this.consoleMessage = MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.finish.folder.message"), tableFolder.getFullPath().toString()) + crlf;
               } catch (CoreException var17) {
                  if (this.console != null) {
                     this.console.setOutputText(this.consoleMessage + MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.finish.folder.error"), tableFolder.getName(), crlf + var17.getMessage() + crlf));
                  }

                  var17.printStackTrace();
                  return true;
               }
            }

            this.folder = tableFolder;
         }

         String entityFileName = this.entityBase.getEntityName().getValue().toUpperCase() + ".TBL";
         String entityXml = this.entityBase.getXMLString();
         IFile entityFile = this.folder.getFile(new Path(entityFileName));
         this.deleteOldFiles(entityFile);
         IFile newEntityFile = this.createFile(entityFile, entityXml);
         if (newEntityFile == null) {
            this.consoleMessage = this.consoleMessage + MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.finish.file.error"), entityFileName, crlf);
            return true;
         } else {
            this.consoleMessage = this.consoleMessage + MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.finish.file.message"), entityFileName) + crlf;
            if (this.entityPage4.checkBox.getSelection()) {
               try {
                  IEditorInput input = new FileEditorInput(newEntityFile);
                  this.page.openEditor(input, "com.sanchez.profile.schema.schemaeditor.editors.EntityEditor", true);
               } catch (PartInitException var16) {
                  var16.printStackTrace();
               }
            }

            Vector attributeVector = this.entityPage4.attributeEntry.getAttributes();
            Iterator iter = attributeVector.iterator();

            while(iter.hasNext()) {
               EntityTableEntry element = (EntityTableEntry)iter.next();
               if (!element.getAttributeName().startsWith("\"") && !this.wizardsUtils.isNumber(element.getAttributeName()) && element.getAttributeName().length() != 0) {
                  String AttributefileName = this.entityBase.getEntityName().getValue() + "-" + element.getAttributeName() + ".COL";
                  String attributeXml = this.createAttributeXml(element);
                  IFile attributeFile = this.folder.getFile(new Path(AttributefileName));
                  IFile newAttributeFile = this.createFile(attributeFile, attributeXml);
                  if (newAttributeFile == null) {
                     this.consoleMessage = this.consoleMessage + MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.finish.file.error"), AttributefileName, crlf);
                     return true;
                  }

                  this.consoleMessage = this.consoleMessage + MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.finish.file.message"), AttributefileName) + crlf;
                  if (this.entityPage4.checkBox.getSelection()) {
                     try {
                        IEditorInput input = new FileEditorInput(newEntityFile);
                        this.page.openEditor(input, "com.sanchez.profile.ide.editors.PSLEditor", true);
                     } catch (PartInitException var15) {
                        var15.printStackTrace();
                     }
                  }
               }
            }

            if (this.entityPage4.checkBox.getSelection()) {
               try {
                  IEditorInput input = new FileEditorInput(newEntityFile);
                  this.page.openEditor(input, "com.sanchez.profile.ide.editors.PSLEditor");
               } catch (PartInitException var14) {
                  var14.printStackTrace();
               }
            }

            this.consoleMessage = this.consoleMessage + MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.finish.message"), entityFileName);
            if (this.console != null) {
               this.console.setOutputText(this.consoleMessage);
            } else {
               try {
                  this.page.showView("com.fnf.studio.views.FidelityConsoleView");
               } catch (PartInitException var13) {
                  var13.printStackTrace();
               }

               this.console = (FidelityConsoleView)this.page.findView("com.fnf.studio.views.FidelityConsoleView");
               this.console.setOutputText(this.consoleMessage);
            }

            return true;
         }
      }
   }

   private String createAttributeXml(EntityTableEntry element) {
      Attribute attributeBase = new Attribute();
      SchemaInfo help = new SchemaInfo(attributeBase.getHelpFileName());
      attributeBase.getEntityName().setValue(this.entityBase.getEntityName().getValue().trim());
      attributeBase.getAttributeName().setValue(element.getAttributeName());
      attributeBase.getSubScriptLiteral().setValue(element.getSubScriptLiteral());
      attributeBase.getFieldPosition().setValue(element.getFieldPosition());
      String dataTypeValue = this.wizardsUtils.getCellComboValue(help, attributeBase.getDataType().getTag(), element.getDataType());
      attributeBase.getDataType().setValue(dataTypeValue);
      String isRequried = element.isRequired() ? "1" : "0";
      attributeBase.getRequired().setValue(isRequried);
      attributeBase.getLength().setValue(element.getLength());
      attributeBase.getDecimalPrecision().setValue(element.getDecimalPrecision());
      attributeBase.getComputedExpression().setValue(element.getComputedExpression());
      attributeBase.getDescription().setValue(element.getDescription());
      attributeBase.getDisplaySize().setValue(element.getLength());
      attributeBase.getReportHeader().setValue(element.getDescription());
      String xmlString = attributeBase.getXMLString();
      return xmlString;
   }

   private IFile createFile(IFile file, String xmlString) {
      try {
         xmlString = this.wizardsUtils.formatXml(xmlString);
      } catch (SAXException var4) {
         var4.printStackTrace();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      try {
         file.create(new ByteArrayInputStream(xmlString.getBytes()), false, (IProgressMonitor)null);
         return file;
      } catch (CoreException var6) {
         if (this.console != null) {
            this.console.setOutputText(this.consoleMessage + MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.finish.file.error"), file.getName(), crlf + var6.getMessage() + crlf));
         }

         var6.printStackTrace();
         return null;
      }
   }

   private void deleteOldFiles(IFile file) {
      try {
         IEditorReference[] editorRef = this.page.getEditorReferences();

         for(int i = 0; i < editorRef.length; ++i) {
            if (editorRef[i].getName().equals(file.getName())) {
               this.page.closeEditor(editorRef[i].getEditor(false), false);
            } else if (editorRef[i].getName().startsWith(this.entityBase.getEntityName().getValue() + "-") && editorRef[i].getName().endsWith(".COL")) {
               this.page.closeEditor(editorRef[i].getEditor(false), false);
            }
         }

         IResource[] resource = this.folder.members();

         for(int j = 0; j < resource.length; ++j) {
            if (resource[j].getName().equals(file.getName())) {
               resource[j].delete(true, (IProgressMonitor)null);
            }

            if (resource[j].getName().startsWith(this.entityBase.getEntityName().getValue() + "-") && resource[j].getName().endsWith(".COL")) {
               resource[j].delete(true, (IProgressMonitor)null);
            }
         }
      } catch (CoreException var5) {
         var5.printStackTrace();
      }

   }
}
