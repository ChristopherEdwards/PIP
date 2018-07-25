package com.sanchez.profile.schema.schemaeditor.wizards;

import com.fnf.studio.views.FidelityConsoleView;
import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.editors.AttributeEditor;
import com.sanchez.profile.schema.schemaeditor.editors.EntityEditor;
import com.sanchez.profile.schema.schemaeditor.ui.SchemaInfo;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Attribute;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Entity;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.apache.xerces.parsers.DOMParser;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class NewAttributeWizard extends Wizard implements INewWizard {
   private static String crlf = System.getProperties().getProperty("line.separator");
   private String consoleMessage = "";
   private FidelityConsoleView console;
   Hashtable attributeBaseTable = new Hashtable();
   private IWorkbenchPage page;
   public EntityPage0 entityPage0;
   public EntityPage1 entityPage1;
   public EntityPage2 entityPage2;
   public EntityPage3 entityPage3;
   public EntityPage4 entityPage4;
   public Entity entityBase = new Entity();
   public IContainer folder;
   public IFile entityFile;
   private WizardsUtils wizardsUtils = new WizardsUtils();
   protected IStructuredSelection selection;
   protected IWorkbench workbench;
   protected ArrayList list = new ArrayList();

   public NewAttributeWizard() {
   }

   public NewAttributeWizard(ArrayList list) {
      this.list = list;
   }

   public void addPages() {
      this.entityPage1 = new EntityPage1("Page1", this.entityBase, "attribute", this.list);
      this.addPage(this.entityPage1);
      this.entityPage3 = new EntityPage3("Page3", this.entityBase, "attribute");
      this.addPage(this.entityPage3);
      this.entityPage4 = new EntityPage4("Page4", "attribute");
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

         this.entityFile = this.getEntityFile();
         if (this.entityFile == null) {
            return;
         }

         EntityEditor eEditor = new EntityEditor();
         InputStream is = null;

         try {
            is = this.entityFile.getContents();
         } catch (CoreException var10) {
            System.out.println("Init - Create Stream failed");
            var10.printStackTrace();
         }

         DOMParser parser = new DOMParser();

         try {
            parser.parse(new InputSource(is));
         } catch (SAXException var8) {
            System.out.println("Init - SAXException occured");
            var8.printStackTrace();
         } catch (IOException var9) {
            System.out.println("Init - IOException occured");
            var9.printStackTrace();
         }

         Document document = parser.getDocument();
         eEditor.initializeBase(document);
         this.entityBase = eEditor.getBase();
         this.getAttributeBases();
      }

   }

   private IFile getEntityFile() {
      IResource[] resource = (IResource[])null;

      try {
         resource = this.folder.members();
      } catch (CoreException var3) {
         System.out.println("getEntityFile - CoreException occured");
         var3.printStackTrace();
      }

      for(int j = 0; j < resource.length; ++j) {
         if (resource[j].getName().equals(this.folder.getName().toUpperCase() + ".TBL")) {
            return (IFile)resource[j];
         }
      }

      return null;
   }

   private void getAttributeBases() {
      IResource[] resource = (IResource[])null;

      try {
         resource = this.folder.members();

         for(int j = 0; j < resource.length; ++j) {
            if (resource[j].getName().startsWith(this.entityBase.getEntityName().getValue() + "-") && resource[j].getName().endsWith(".COL")) {
               new Attribute();
               IFile attributeFile = (IFile)resource[j];
               AttributeEditor aEditor = new AttributeEditor();
               InputStream is = null;
               is = attributeFile.getContents();
               DOMParser parser = new DOMParser();

               try {
                  parser.parse(new InputSource(is));
               } catch (SAXException var10) {
                  System.out.println("getAttributeBases - SAXException occured");
                  var10.printStackTrace();
               } catch (IOException var11) {
                  System.out.println("getAttributeBases - IOException occured");
                  var11.printStackTrace();
               }

               Document document = parser.getDocument();
               aEditor.initializeBase(document);
               Attribute attributeBase = aEditor.getBase();
               String attributeName = attributeBase.getAttributeName().getValue();
               this.attributeBaseTable.put(attributeName, attributeBase);
            }
         }
      } catch (CoreException var12) {
         System.out.println("getAttributeBases - CoreException occured");
         var12.printStackTrace();
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
         String entityFileName = this.entityBase.getEntityName().getValue().toUpperCase() + ".TBL";
         this.entityBase.getXMLString();
         this.folder.getFile(new Path(entityFileName));
         this.page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
         this.console = (FidelityConsoleView)this.page.findView("com.fnf.studio.views.FidelityConsoleView");
         Vector attributeVector = this.entityPage4.attributeEntry.getAttributes();
         Iterator iter = attributeVector.iterator();

         while(iter.hasNext()) {
            EntityTableEntry element = (EntityTableEntry)iter.next();
            String AttributefileName = this.entityBase.getEntityName().getValue() + "-" + element.getAttributeName() + ".COL";
            String attributeXml = this.createAttributeXml(element);
            IFile newAttributeFile = null;
            newAttributeFile = this.createFile(AttributefileName, attributeXml);
            if (newAttributeFile == null) {
               this.consoleMessage = this.consoleMessage + MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.finish.file.error"), AttributefileName) + crlf;
               return true;
            }

            this.consoleMessage = this.consoleMessage + MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.finish.file.message"), AttributefileName) + crlf;
            if (this.entityPage4.checkBox.getSelection()) {
               try {
                  IEditorInput input = new FileEditorInput(newAttributeFile);
                  this.page.openEditor(input, "com.sanchez.profile.schema.schemaeditor.editors.AttributeEditor", true);
               } catch (PartInitException var10) {
                  var10.printStackTrace();
               }
            }
         }

         this.consoleMessage = this.consoleMessage + SchemaEditorPlugin.getResourceString("wizard.attribute.page.success.message");
         if (this.console != null) {
            this.console.setOutputText(this.consoleMessage);
         } else {
            try {
               this.page.showView("com.fnf.studio.views.FidelityConsoleView");
            } catch (PartInitException var9) {
               var9.printStackTrace();
            }

            this.console = (FidelityConsoleView)this.page.findView("com.fnf.studio.views.FidelityConsoleView");
            this.console.setOutputText(this.consoleMessage);
         }

         return true;
      }
   }

   private IFile createFile(String AttributefileName, String xmlString) {
      IFile file = this.folder.getFile(new Path(AttributefileName));

      try {
         xmlString = this.wizardsUtils.formatXml(xmlString);
      } catch (SAXException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      try {
         file.create(new ByteArrayInputStream(xmlString.getBytes()), false, (IProgressMonitor)null);
         return file;
      } catch (CoreException var7) {
         if (this.console != null) {
            this.console.setOutputText(this.consoleMessage + MessageFormat.format(SchemaEditorPlugin.getResourceString("wizard.entity.finish.file.error"), AttributefileName, crlf + var7.getMessage() + crlf));
         }

         var7.printStackTrace();
         return null;
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

   public Hashtable getAttributeBaseTable() {
      return this.attributeBaseTable;
   }
}
