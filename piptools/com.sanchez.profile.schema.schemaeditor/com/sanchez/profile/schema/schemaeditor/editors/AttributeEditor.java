package com.sanchez.profile.schema.schemaeditor.editors;

import com.fnf.profile.ide.editors.psleditor.PSLEditor;
import com.fnf.profile.ide.editors.psleditor.PSLSourceViewerConfiguration;
import com.fnf.profile.ide.editors.psleditor.utils.ColorManager;
import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.actions.TextActionHandler;
import com.sanchez.profile.schema.schemaeditor.ui.DOMSerializer;
import com.sanchez.profile.schema.schemaeditor.ui.XMLUtil;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Attribute;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.EditablePage;
import com.sanchez.profile.schema.schemaeditor.ui.xml.XMLEditor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import org.apache.xerces.parsers.DOMParser;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class AttributeEditor extends MultiPageEditorPart {
   private IAction actionCopy;
   private IAction actionCut;
   private IAction actionPaste;
   private IAction actionSelectAll;
   private Attribute base = new Attribute();
   private EditablePage characterScreen;
   private EditablePage documentation;
   private String errorMessage = "";
   private EditablePage main;
   private MenuManager menuMgr;
   private int oldIndex = 0;
   private Vector pages = new Vector();
   private boolean convertOK = true;
   private boolean parseXMLsuccessed = true;
   private EditablePage profileDB;
   private IDocument pslDocument;
   private String rootName;
   private XMLEditor sourcePage;
   private Vector tagElement = new Vector();
   private ITextListener textListener;
   private SourceViewer viewer;
   private boolean viewerDirty;
   private XMLEditor xmlEditor;

   private void commitChanges() {
      this.main.commitChanges(this.isDirty());
      this.characterScreen.commitChanges(this.isDirty());
      this.profileDB.commitChanges(this.isDirty());
      this.documentation.commitChanges(this.isDirty());
   }

   private void createCharacterScreenPage() {
      final Composite composite = new Composite(this.getContainer(), 512);
      final GridLayout gridLayout = new GridLayout();
      composite.setLayout(gridLayout);
      composite.getVerticalBar().addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent selectionevent) {
         }

         public void widgetSelected(SelectionEvent e) {
            ScrollBar sb = (ScrollBar)e.getSource();
            gridLayout.marginHeight = 5 - sb.getSelection() * 20;
            composite.layout();
         }
      });
      String header = SchemaEditorPlugin.getResourceString("attribute.tab.characterScreen.title");
      String desc = this.base.getDescCharacterPage();
      this.characterScreen = new EditablePage(this, composite, header, desc, this.base.getVCharacterScreen(), this.base.getTitleCharacterScreen(), this.base.getDescCharacterScreen(), 22, (Vector)null, "", "", 20, (Vector)null, "", "", 20, this.base.getHelpFileName());
      int index = this.addPage(composite);
      this.pages.add(index, this.characterScreen);
      this.setPageText(index, SchemaEditorPlugin.getResourceString("attribute.tab.characterScreen.title"));
   }

   private void createDocumentationPage() {
      final Composite composite = new Composite(this.getContainer(), 512);
      final GridLayout gridLayout = new GridLayout();
      composite.setLayout(gridLayout);
      composite.getVerticalBar().addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent selectionevent) {
         }

         public void widgetSelected(SelectionEvent e) {
            ScrollBar sb = (ScrollBar)e.getSource();
            gridLayout.marginHeight = 5 - sb.getSelection() * 20;
            composite.layout();
         }
      });
      String header = SchemaEditorPlugin.getResourceString("attribute.tab.documentation.title");
      String desc = this.base.getDescProfilePage();
      this.documentation = new EditablePage(this, composite, header, desc, this.base.getVDocumentation(), this.base.getTitleDocumentation(), this.base.getDescDocumentation(), 22, (Vector)null, "", "", 20, (Vector)null, "", "", 20, this.base.getHelpFileName());
      int index = this.addPage(composite);
      this.pages.add(index, this.documentation);
      this.setPageText(index, SchemaEditorPlugin.getResourceString("attribute.tab.documentation.title"));
   }

   private void createMainPage() {
      final Composite composite = new Composite(this.getContainer(), 512);
      final GridLayout gridLayout = new GridLayout();
      composite.setLayout(gridLayout);
      composite.getVerticalBar().addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent selectionevent) {
         }

         public void widgetSelected(SelectionEvent e) {
            ScrollBar sb = (ScrollBar)e.getSource();
            gridLayout.marginHeight = 5 - sb.getSelection() * 20;
            composite.layout();
         }
      });
      String header = "";
      String desc = "";
      header = SchemaEditorPlugin.getResourceString("attribute.tab.main.title");
      desc = this.base.getDescMainPage();
      this.main = new EditablePage(this, composite, header, desc, this.base.getVMainG1(), this.base.getTitleMainG1(), this.base.getDescMainG1(), 21, this.base.getVMainG2(), this.base.getTitleMainG2(), this.base.getDescMainG2(), 21, this.base.getVMainG3(), this.base.getTitleMainG3(), this.base.getDescMainG3(), 22, this.base.getHelpFileName());
      int index = this.addPage(composite);
      this.pages.add(index, this.main);
      this.setPageText(index, SchemaEditorPlugin.getResourceString("attribute.tab.main.title"));
   }

   protected void createPages() {
      if (!this.convertOK) {
         this.createXMLPage();
         this.parseXMLsuccessed = false;
      } else {
         this.sourcePage = new XMLEditor(false);

         try {
            DOMParser parser = new DOMParser();
            InputStream iss = ((IFileEditorInput)this.getEditorInput()).getFile().getContents();
            parser.parse(new InputSource(iss));
            Document document = parser.getDocument();
            this.initializeBase(document);
         } catch (SAXParseException var4) {
            this.errorMessage = "Line " + var4.getLineNumber() + ", Position " + var4.getColumnNumber() + ": " + var4.getMessage();
            this.parseXMLsuccessed = false;
         } catch (Exception var5) {
            this.errorMessage = var5.getMessage();
            this.parseXMLsuccessed = false;
         }

         if (this.parseXMLsuccessed) {
            this.createMainPage();
            this.createProfileDBPage();
            this.createCharacterScreenPage();
            this.createDocumentationPage();
            this.createPSLCodePage();
            this.createSourcePage();
         } else {
            this.parseXMLsuccessed = false;
            this.createXMLPage();
         }

         if (!this.errorMessage.equals("")) {
            this.handleException("SAX Parser Error", this.errorMessage);
         }

      }
   }

   private void createProfileDBPage() {
      final Composite composite = new Composite(this.getContainer(), 512);
      final GridLayout gridLayout = new GridLayout();
      composite.setLayout(gridLayout);
      composite.getVerticalBar().addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent selectionevent) {
         }

         public void widgetSelected(SelectionEvent e) {
            ScrollBar sb = (ScrollBar)e.getSource();
            gridLayout.marginHeight = 5 - sb.getSelection() * 20;
            composite.layout();
         }
      });
      String header = SchemaEditorPlugin.getResourceString("attribute.tab.profileDB.title");
      String desc = this.base.getDescProfilePage();
      this.profileDB = new EditablePage(this, composite, header, desc, this.base.getVProfileDBG1(), this.base.getTitleProfileDBG1(), this.base.getDescProfileDBG1(), 23, this.base.getVProfileDBG2(), this.base.getTitleProfileDBG2(), this.base.getDescProfileDBG2(), 23, this.base.getVProfileDBG3(), this.base.getTitleProfileDBG3(), this.base.getDescProfileDBG3(), 23, this.base.getHelpFileName());
      int index = this.addPage(composite);
      this.pages.add(index, this.profileDB);
      this.setPageText(index, SchemaEditorPlugin.getResourceString("attribute.tab.profileDB.title"));
   }

   private void createPSLCodePage() {
      Composite composite = new Composite(this.getContainer(), 0);
      GridLayout gridLayout = new GridLayout();
      composite.setLayout(gridLayout);
      String psl = this.base.getPSLCode().getValue();
      this.viewer = new SourceViewer(composite, (IVerticalRuler)null, 768);
      Control control = this.viewer.getControl();
      GridData data = new GridData(1808);
      data.grabExcessHorizontalSpace = true;
      data.grabExcessVerticalSpace = true;
      control.setLayoutData(data);
      this.pslDocument = new org.eclipse.jface.text.Document();
      this.pslDocument.set(psl);
      this.viewer.configure(new PSLSourceViewerConfiguration(new ColorManager(), (PSLEditor)null));
      this.viewer.setEditable(true);
      this.viewer.setDocument(this.pslDocument);
      this.textListener = new ITextListener() {
         public void textChanged(TextEvent event) {
            AttributeEditor.this.base.getPSLCode().setValue(AttributeEditor.this.pslDocument.get());
            if (!AttributeEditor.this.isDirty()) {
               String xml = AttributeEditor.this.base.getXMLString();
               AttributeEditor.this.updateXMLEditor(xml);
            }

            AttributeEditor.this.viewerDirty = true;
         }
      };
      this.viewer.addTextListener(this.textListener);
      Font font = JFaceResources.getFontRegistry().get("org.eclipse.jface.textfont");
      this.viewer.getTextWidget().setFont(font);
      this.menuMgr = new MenuManager("PSLCode Menu");
      TextActionHandler txtActionHandler = new TextActionHandler(this.viewer);
      this.actionCut = txtActionHandler.textCutAction;
      this.actionPaste = txtActionHandler.textPasteAction;
      this.actionCopy = txtActionHandler.textCopyAction;
      this.actionSelectAll = txtActionHandler.textSelectAllAction;
      this.actionSelectAll.setEnabled(true);
      this.menuMgr.setRemoveAllWhenShown(true);
      this.menuMgr.addMenuListener(new IMenuListener() {
         public void menuAboutToShow(IMenuManager menuManager) {
            if (AttributeEditor.this.viewer.getTextWidget().getSelectionCount() != 0) {
               AttributeEditor.this.actionCut.setEnabled(true);
               AttributeEditor.this.actionCopy.setEnabled(true);
               AttributeEditor.this.actionPaste.setEnabled(true);
            } else {
               AttributeEditor.this.actionCut.setEnabled(false);
               AttributeEditor.this.actionCopy.setEnabled(false);
            }

            menuManager.add(AttributeEditor.this.actionCut);
            menuManager.add(AttributeEditor.this.actionPaste);
            menuManager.add(AttributeEditor.this.actionCopy);
            menuManager.add(AttributeEditor.this.actionSelectAll);
         }
      });
      Menu menu = this.menuMgr.createContextMenu(control);
      control.setMenu(menu);
      int index = this.addPage(composite);
      this.pages.add(index, psl);
      this.setPageText(index, SchemaEditorPlugin.getResourceString("attribute.tab.pslCode.title"));
   }

   private void createSourcePage() {
      try {
         int index = this.addPage(this.sourcePage, this.getEditorInput());
         this.pages.add(index, this.sourcePage);
         this.setPageText(index, this.sourcePage.getTitle());
      } catch (PartInitException var2) {
         this.handleException("Create Source Page Error", var2.getMessage());
      }

   }

   private void createXMLPage() {
      this.xmlEditor = new XMLEditor(true);

      try {
         int index = this.addPage(this.xmlEditor, this.getEditorInput());
         this.pages.add(index, this.xmlEditor);
         this.setPageText(index, this.xmlEditor.getTitle());
      } catch (PartInitException var2) {
         this.handleException("Create Source Page Error", var2.getMessage());
      }

   }

   public void dispose() {
      super.dispose();
   }

   public void doSave(IProgressMonitor monitor) {
      IEditorPart editor = null;
      if (this.parseXMLsuccessed) {
         editor = this.getEditor(5);
         this.saveChanges();
         this.viewerDirty = false;
      } else {
         editor = this.getEditor(0);
      }

      editor.doSave(monitor);
   }

   public void doSaveAs() {
      IEditorPart editor = null;
      if (this.parseXMLsuccessed) {
         this.saveChanges();
         editor = this.getEditor(5);
         this.setPageText(0, editor.getTitle());
         this.setInput(editor.getEditorInput());
         this.viewerDirty = false;
      } else {
         editor = this.getEditor(0);
      }

      editor.doSaveAs();
   }

   public Attribute getBase() {
      return this.base;
   }

   private Document getXMLDocumentFormSource() {
      try {
         String editorText = this.sourcePage.getDocumentProvider().getDocument(this.sourcePage.getEditorInput()).get();
         InputStream input = new ByteArrayInputStream(editorText.getBytes());
         DOMParser parser = new DOMParser();
         parser.parse(new InputSource(input));
         Document document = parser.getDocument();
         return document;
      } catch (Exception var5) {
         this.handleException("SAX Parser Error", var5.getMessage());
         return null;
      }
   }

   private void handleException(String title, String message) {
      (new ErrorDialog(this.getEditorSite().getShell(), title, "Message: Unable to open Attribute Editor", new Status(4, "org.eclipse.core.resources", 0, message, (Throwable)null), 4)).open();
   }

   public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
      boolean var10000 = editorInput instanceof IFileEditorInput;
      if (editorInput instanceof IFileEditorInput) {
         try {
            InputStream in = ((IFileEditorInput)editorInput).getFile().getContents();

            try {
               if (!this.isXMLDocument(in)) {
                  this.convertOK = MessageDialog.openConfirm((Shell)null, "Question", "This non-xml format file will be converted to a xml format.\n Are you sure?");
                  if (this.convertOK) {
                     this.SetDocumentToBase(((IFileEditorInput)editorInput).getFile().getContents());
                     this.setNonXMLToBase(((IFileEditorInput)editorInput).getFile().getContents());
                     this.setFilenameToBase(((IFileEditorInput)editorInput).getFile().getName().toString());
                     String xml = this.base.getXMLString();
                     InputStream newInput = new ByteArrayInputStream(xml.getBytes());
                     ((IFileEditorInput)editorInput).getFile().setContents(this.serializeInput(newInput), true, true, (IProgressMonitor)null);
                     newInput.close();
                  }
               }
            } catch (IOException var7) {
               var7.printStackTrace();
            }
         } catch (CoreException var8) {
            var8.printStackTrace();
         }
      } else if (editorInput instanceof IStorageEditorInput) {
         try {
            ((IStorageEditorInput)editorInput).getStorage().getContents();
         } catch (CoreException var6) {
            var6.printStackTrace();
         }
      }

      super.init(site, editorInput);
      this.setTitle(editorInput.getName());
   }

   private void SetDocumentToBase(InputStream is) {
      String docValue = "";
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      try {
         String line = br.readLine();

         for(int index = 0; line != null; ++index) {
            if (index >= 31) {
               if (!docValue.equals("")) {
                  docValue = docValue + System.getProperties().getProperty("line.separator");
               }

               docValue = docValue + line;
            }

            line = br.readLine();
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      this.base.getDocumentation().setValue(docValue);
   }

   private void setFilenameToBase(String filename) {
      String entityName = "";
      String attributeName = "";
      int dash = filename.indexOf("-");
      if (dash != -1) {
         entityName = filename.substring(0, dash);
      }

      int dot = filename.indexOf(".");
      if (dot != -1 && dash != -1) {
         attributeName = filename.substring(dash + 1, dot);
      }

      this.base.getEntityName().setValue(entityName);
      this.base.getAttributeName().setValue(attributeName);
   }

   private InputStream serializeInput(InputStream input) {
      DOMParser parser = new DOMParser();

      try {
         parser.parse(new InputSource(input));
      } catch (SAXException var7) {
         var7.printStackTrace();
      } catch (IOException var8) {
         var8.printStackTrace();
      }

      Document doc = parser.getDocument();
      DOMSerializer serializer = new DOMSerializer();
      StringBuffer sb = new StringBuffer();
      serializer.serialize(doc, sb);
      String xmlInput = sb.toString();
      if (xmlInput.endsWith(System.getProperties().getProperty("line.separator")) && xmlInput.length() > 0) {
         xmlInput = xmlInput.substring(0, xmlInput.lastIndexOf(System.getProperties().getProperty("line.separator")));
      }

      return new ByteArrayInputStream(xmlInput.getBytes());
   }

   public void initializeBase(Document document) {
      this.updateBase(document);
   }

   public boolean isSaveAsAllowed() {
      return true;
   }

   private boolean isValid(Object object) {
      if (object instanceof EditablePage) {
         EditablePage p = (EditablePage)object;
         return p.isValid();
      } else {
         return true;
      }
   }

   private boolean isValidData(int index) {
      int count = this.pages.size();
      if (count == 6) {
         if (this.oldIndex == 4 || this.oldIndex == 5) {
            return true;
         }
      } else if (this.oldIndex == 3) {
         return true;
      }

      EditablePage thePage = (EditablePage)this.pages.get(index);
      boolean ok = thePage.isValid();
      thePage.setValid(true);
      return ok;
   }

   private boolean isXMLDocument(InputStream is) {
      boolean ret = true;
      BufferedReader dis = new BufferedReader(new InputStreamReader(is));

      try {
         String line = dis.readLine();
         if (line != null && line.startsWith("NOD")) {
            ret = false;
         }

         dis.close();
      } catch (IOException var7) {
         ret = false;
         System.out.println("eee" + var7.getMessage());
         var7.printStackTrace();
      }

      return ret;
   }

   protected void pageChange(int index) {
      String psl;
      if (this.getPageText(index).equalsIgnoreCase(this.sourcePage.getTitle()) && this.isDirty()) {
         psl = this.base.getXMLString();
         this.updateXMLEditor(psl);
      }

      if (this.getPageText(this.oldIndex).equalsIgnoreCase(this.sourcePage.getTitle())) {
         this.updateModelBySource();
      }

      if (this.getPageText(index).equalsIgnoreCase(SchemaEditorPlugin.getResourceString("attribute.tab.main.title"))) {
         if (!this.isValidData(this.oldIndex)) {
            this.setActivePage(this.oldIndex);
            return;
         }

         this.main.updateClient(this, this.base.getVMainG1(), 21, this.base.getVMainG2(), 21, this.base.getVMainG3(), 22);
         this.main.setDirty(false);
      }

      if (this.getPageText(index).equalsIgnoreCase(SchemaEditorPlugin.getResourceString("attribute.tab.profileDB.title"))) {
         if (!this.isValidData(this.oldIndex)) {
            this.setActivePage(this.oldIndex);
            return;
         }

         this.profileDB.updateClient(this, this.base.getVProfileDBG1(), 23, this.base.getVProfileDBG2(), 23, this.base.getVProfileDBG3(), 23);
         this.profileDB.setDirty(false);
      }

      if (this.getPageText(index).equalsIgnoreCase(SchemaEditorPlugin.getResourceString("attribute.tab.characterScreen.title"))) {
         if (!this.isValidData(this.oldIndex)) {
            this.setActivePage(this.oldIndex);
            return;
         }

         this.characterScreen.updateClient(this, this.base.getVCharacterScreen(), 23, (Vector)null, 20, (Vector)null, 20);
         this.characterScreen.setDirty(false);
      }

      if (this.getPageText(index).equalsIgnoreCase(SchemaEditorPlugin.getResourceString("attribute.tab.documentation.title"))) {
         if (!this.isValidData(this.oldIndex)) {
            this.setActivePage(this.oldIndex);
            return;
         }

         this.documentation.updateClient(this, this.base.getVDocumentation(), 22, (Vector)null, 20, (Vector)null, 20);
         this.documentation.setDirty(false);
      }

      if (this.getPageText(index).equalsIgnoreCase(SchemaEditorPlugin.getResourceString("attribute.tab.pslCode.title")) && this.viewerDirty) {
         if (!this.isValidData(this.oldIndex)) {
            this.setActivePage(this.oldIndex);
            return;
         }

         psl = this.base.getPSLCode().getValue();
         this.viewer.removeTextListener(this.textListener);
         this.pslDocument.set(psl);
         this.viewer.setDocument(this.pslDocument);
         this.viewer.addTextListener(this.textListener);
         this.viewerDirty = false;
      }

      this.oldIndex = index;
      super.pageChange(index);
   }

   private void saveChanges() {
      this.commitChanges();
      String xml = this.base.getXMLString();
      this.updateXMLEditor(xml);
      this.updateModelBySource();
   }

   private void setNonXMLToBase(InputStream in) {
      Properties file = new Properties();
      String key = null;
      HashMap allFields = XMLUtil.getAllFields(this.base.getClass());

      try {
         file.load(in);
         Enumeration keys = file.propertyNames();

         while(keys.hasMoreElements()) {
            key = (String)keys.nextElement();
            String fieldName = SchemaEditorPlugin.getResourceString(key);
            System.out.println(key + "  -- " + fieldName);
            if (fieldName != null) {
               Field var10000 = (Field)allFields.get(fieldName);
               String first = fieldName.toUpperCase().substring(0, 1);
               String setterMethodName = "set" + first + fieldName.substring(1) + "Value";
               Class fieldType = String.class;
               Class[] setterParams = new Class[]{fieldType};
               Method method = null;

               try {
                  method = this.base.getClass().getMethod(setterMethodName, setterParams);
               } catch (Exception var14) {
                  String msg = "Exception caught when creating method " + setterMethodName + " for class " + this.base.getClass().getName();
                  System.out.println(msg);
               }

               if (method != null) {
                  Object[] setterParamObjects = new Object[1];
                  String value = file.getProperty(key);
                  if (value == null) {
                     value = "";
                  }

                  setterParamObjects[0] = value;
                  if (!fieldType.isPrimitive()) {
                     method.invoke(this.base, setterParamObjects);
                  }
               }
            }
         }

         System.out.println(this.base.getXMLString());
      } catch (Exception var15) {
         var15.printStackTrace();
      }

   }

   private void updateBase(Document doc) {
      HashMap allFields = XMLUtil.getAllFields(this.base.getClass());

      try {
         Element root = doc.getDocumentElement();
         if (root == null) {
            return;
         }

         this.rootName = root.getNodeName();
         this.walk(root);
         if (!this.tagElement.isEmpty()) {
            for(int ii = 0; ii < this.tagElement.size(); ++ii) {
               Element child = (Element)this.tagElement.elementAt(ii);
               if (!XMLUtil.hasChildElementNode(child)) {
                  String tagName = child.getTagName();
                  String fieldName = XMLUtil.formatedName(tagName);
                  Field field = (Field)allFields.get(fieldName);
                  if (field != null) {
                     String first = fieldName.toUpperCase().substring(0, 1);
                     String setterMethodName = "set" + first + fieldName.substring(1) + "Value";
                     Class fieldType = String.class;
                     Class[] setterParams = new Class[]{fieldType};
                     Method method = null;

                     String value;
                     try {
                        method = this.base.getClass().getMethod(setterMethodName, setterParams);
                     } catch (Exception var16) {
                        value = "Exception caught when creating method " + setterMethodName + "for class " + this.base.getClass().getName();
                        this.handleException(value, var16.getMessage());
                     }

                     if (method != null) {
                        Object[] setterParamObjects = new Object[1];
                        value = XMLUtil.getSimpleElementText(child);
                        setterParamObjects[0] = value;
                        if (!fieldType.isPrimitive()) {
                           method.invoke(this.base, setterParamObjects);
                        }
                     }
                  }
               }
            }
         }
      } catch (Exception var17) {
         this.handleException("Update Base Error", var17.getMessage());
      }

   }

   private void updateFile(String xml) {
      try {
         InputStream is = new ByteArrayInputStream(xml.getBytes());
         DOMParser parser = new DOMParser();
         parser.parse(new InputSource(is));
         Document doc = parser.getDocument();
         DOMSerializer serializer = new DOMSerializer();
         StringBuffer sb = new StringBuffer();
         serializer.serialize(doc, sb);
         String xmlInput = sb.toString();
         InputStream in = new ByteArrayInputStream(xmlInput.getBytes());
         FileEditorInput input = (FileEditorInput)this.getEditorInput();
         IFile file = input.getFile();
         file.setContents(in, true, true, (IProgressMonitor)null);
      } catch (Exception var11) {
         this.handleException("SAX Parser Error", var11.getMessage());
      }

   }

   private void updateModelBySource() {
      Document newDoc = this.getXMLDocumentFormSource();
      this.updateBase(newDoc);
   }

   public void updateXMLEditor(String xml) {
      try {
         InputStream in = new ByteArrayInputStream(xml.getBytes());
         DOMParser parser = new DOMParser();
         parser.parse(new InputSource(in));
         Document doc = parser.getDocument();
         DOMSerializer serializer = new DOMSerializer();
         StringBuffer sb = new StringBuffer();
         serializer.serialize(doc, sb);
         String xmlInput = sb.toString();
         this.sourcePage = (XMLEditor)this.getEditor(5);
         IDocument document = this.sourcePage.getDocumentProvider().getDocument(this.getEditorInput());
         if (xmlInput.endsWith(System.getProperties().getProperty("line.separator")) && xmlInput.length() > 0) {
            xmlInput = xmlInput.substring(0, xmlInput.lastIndexOf(System.getProperties().getProperty("line.separator")));
         }

         document.set(xmlInput);
      } catch (Exception var9) {
         this.handleException("SAX Parser Error", var9.getMessage());
      }

   }

   private void walk(Node node) {
      int type = node.getNodeType();
      switch(type) {
      case 1:
         if (!this.rootName.equalsIgnoreCase(node.getNodeName())) {
            this.tagElement.add(node);
         }
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      default:
         for(Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            this.walk(child);
         }

      }
   }
}
