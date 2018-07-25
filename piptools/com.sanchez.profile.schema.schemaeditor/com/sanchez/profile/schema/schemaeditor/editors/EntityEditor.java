package com.sanchez.profile.schema.schemaeditor.editors;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.ui.DOMSerializer;
import com.sanchez.profile.schema.schemaeditor.ui.EntityRules;
import com.sanchez.profile.schema.schemaeditor.ui.XMLUtil;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.ColumnDetails;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.EditablePage;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Entity;
import com.sanchez.profile.schema.schemaeditor.ui.xml.XMLEditor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import org.apache.xerces.parsers.DOMParser;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class EntityEditor extends MultiPageEditorPart {
   private boolean convertOK = true;
   private boolean parseXMLsuccessed = true;
   private EntityRules rules = new EntityRules();
   private Entity base = new Entity();
   private XMLEditor sourcePage;
   private ColumnDetails columnDetails;
   private EditablePage main;
   private EditablePage characterScreen;
   private EditablePage profileDB;
   private EditablePage documentation;
   private EditablePage audit;
   private Vector tagElement = new Vector();
   private Vector pages = new Vector();
   private String rootName;
   private int oldIndex = 0;
   private String errorMessage = "";
   private String tableName = "";
   private String tableDesc = "";
   public IFile iFile;

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
            this.createColumnDetailsPage();
            this.createMainPage();
            this.createProfileDBPage();
            this.createAuditPage();
            this.createCharacterScreenPage();
            this.createDocumentationPage();
            this.createSourcePage();
         } else {
            this.createXMLPage();
            this.parseXMLsuccessed = false;
         }

         if (!this.errorMessage.equals("")) {
            this.handleException("SAX Parser Error", this.errorMessage);
         }

      }
   }

   private void createXMLPage() {
      XMLEditor xmlEditor = new XMLEditor(true);

      try {
         int index = this.addPage(xmlEditor, this.getEditorInput());
         this.pages.add(index, xmlEditor);
         this.setPageText(index, xmlEditor.getTitle());
      } catch (PartInitException var3) {
         this.handleException("Create Source Page Error", var3.getMessage());
      }

   }

   void createSourcePage() {
      try {
         int index = this.addPage(this.sourcePage, this.getEditorInput());
         this.pages.add(index, this.sourcePage);
         this.setPageText(index, this.sourcePage.getTitle());
      } catch (PartInitException var2) {
         this.handleException("Create Source Page Error", var2.getMessage());
      }

   }

   private void createColumnDetailsPage() {
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
      this.columnDetails = new ColumnDetails(this, composite, this.base.getVMainG1(), this.base.getHelpFileName());
      int index = this.addPage(composite);
      this.pages.add(index, this.columnDetails);
      this.setPageText(index, SchemaEditorPlugin.getResourceString("entity.tab.columnDetail.title"));
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
      String header = SchemaEditorPlugin.getResourceString("entity.tab.main.title");
      String desc = this.base.getDescProfileDBPage();
      this.main = new EditablePage(this, composite, header, desc, this.base.getVMainG1(), this.base.getTitleMainG1(), this.base.getDescMainG1(), 23, this.base.getVMainG2(), this.base.getTitleMainG2(), this.base.getDescMainG2(), 23, (Vector)null, "", "", 23, this.base.getHelpFileName());
      int index = this.addPage(composite);
      this.pages.add(index, this.main);
      this.setPageText(index, SchemaEditorPlugin.getResourceString("entity.tab.main.title"));
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
      String header = SchemaEditorPlugin.getResourceString("entity.tab.characterScreen.title");
      String desc = this.base.getDescCharacterScreenPage();
      this.characterScreen = new EditablePage(this, composite, header, desc, this.base.getVCharacterScreenG1(), this.base.getTitleCharacterScreenG1(), this.base.getDescCharacterScreenG1(), 23, this.base.getVCharacterScreenG2(), this.base.getTitleCharacterScreenG2(), this.base.getDescCharacterScreenG2(), 23, (Vector)null, "", "", 20, this.base.getHelpFileName());
      int index = this.addPage(composite);
      this.pages.add(index, this.characterScreen);
      this.setPageText(index, SchemaEditorPlugin.getResourceString("entity.tab.characterScreen.title"));
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
      String header = SchemaEditorPlugin.getResourceString("entity.tab.profileDB.title");
      String desc = this.base.getDescProfileDBPage();
      this.profileDB = new EditablePage(this, composite, header, desc, this.base.getVProfileDBG1(), this.base.getTitleProfileDBG1(), this.base.getDescProfileDBG1(), 23, this.base.getVProfileDBG2(), this.base.getTitleProfileDBG2(), this.base.getDescProfileDBG2(), 23, (Vector)null, "", "", 23, this.base.getHelpFileName());
      int index = this.addPage(composite);
      this.pages.add(index, this.profileDB);
      this.setPageText(index, SchemaEditorPlugin.getResourceString("entity.tab.profileDB.title"));
   }

   private void createAuditPage() {
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
      String header = SchemaEditorPlugin.getResourceString("entity.tab.audit.title");
      String desc = this.base.getDescAuditPage();
      this.audit = new EditablePage(this, composite, header, desc, this.base.getVAudit(), this.base.getTitleAudit(), this.base.getDescAudit(), 22, (Vector)null, "", "", 20, (Vector)null, "", "", 20, this.base.getHelpFileName());
      int index = this.addPage(composite);
      this.pages.add(index, this.audit);
      this.setPageText(index, SchemaEditorPlugin.getResourceString("entity.tab.audit.title"));
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
      String header = SchemaEditorPlugin.getResourceString("entity.tab.documentation.title");
      String desc = this.base.getDescProfileDBPage();
      this.documentation = new EditablePage(this, composite, header, desc, this.base.getVDocumentation(), this.base.getTitleDocumentation(), this.base.getDescDocumentation(), 22, (Vector)null, "", "", 20, (Vector)null, "", "", 20, this.base.getHelpFileName());
      int index = this.addPage(composite);
      this.pages.add(index, this.documentation);
      this.setPageText(index, SchemaEditorPlugin.getResourceString("entity.tab.documentation.title"));
   }

   public void initializeBase(Document document) {
      this.updateBase(document);
   }

   private void updateBase(Document doc) {
      HashMap allFields = XMLUtil.getAllFields(this.base.getClass());

      try {
         Element root = doc.getDocumentElement();
         this.rootName = root.getNodeName();
         this.walk(root);
         if (!this.tagElement.isEmpty()) {
            for(int ii = 0; ii < this.tagElement.size(); ++ii) {
               Element child = (Element)this.tagElement.elementAt(ii);
               if (!XMLUtil.hasChildElementNode(child)) {
                  String tagName = child.getTagName();
                  if (tagName.equalsIgnoreCase("Package")) {
                     tagName = "E_package";
                  }

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

   protected void pageChange(int index) {
      if (this.getPageText(index).equalsIgnoreCase(this.sourcePage.getTitle()) && this.isDirty()) {
         String xml = this.base.getXMLString();
         this.updateXMLEditor(xml);
      }

      if (this.getPageText(this.oldIndex).equalsIgnoreCase(this.sourcePage.getTitle())) {
         this.updateModelBySource();
      }

      if (this.getPageText(index).equalsIgnoreCase(SchemaEditorPlugin.getResourceString("entity.tab.main.title"))) {
         if (!this.isValidData(this.oldIndex)) {
            this.setActivePage(this.oldIndex);
            return;
         }

         this.main.updateClient(this, this.base.getVMainG1(), 23, this.base.getVMainG2(), 23, (Vector)null, 23);
         this.main.setDirty(false);
      }

      if (this.getPageText(index).equalsIgnoreCase(SchemaEditorPlugin.getResourceString("entity.tab.audit.title"))) {
         if (!this.isValidData(this.oldIndex)) {
            this.setActivePage(this.oldIndex);
            return;
         }

         this.audit.updateClient(this, this.base.getVAudit(), 22, (Vector)null, 20, (Vector)null, 20);
         this.audit.setDirty(false);
      }

      if (this.getPageText(index).equalsIgnoreCase(SchemaEditorPlugin.getResourceString("entity.tab.profileDB.title"))) {
         if (!this.isValidData(this.oldIndex)) {
            this.setActivePage(this.oldIndex);
            return;
         }

         this.profileDB.updateClient(this, this.base.getVProfileDBG1(), 23, this.base.getVProfileDBG2(), 23, (Vector)null, 23);
         this.profileDB.setDirty(false);
      }

      if (this.getPageText(index).equalsIgnoreCase(SchemaEditorPlugin.getResourceString("entity.tab.characterScreen.title"))) {
         if (!this.isValidData(this.oldIndex)) {
            this.setActivePage(this.oldIndex);
            return;
         }

         this.characterScreen.updateClient(this, this.base.getVCharacterScreenG1(), 23, this.base.getVCharacterScreenG2(), 23, (Vector)null, 20);
         this.characterScreen.setDirty(false);
      }

      if (this.getPageText(index).equalsIgnoreCase(SchemaEditorPlugin.getResourceString("entity.tab.documentation.title"))) {
         if (!this.isValidData(this.oldIndex)) {
            this.setActivePage(this.oldIndex);
            return;
         }

         this.documentation.updateClient(this, this.base.getVDocumentation(), 22, (Vector)null, 20, (Vector)null, 20);
         this.documentation.setDirty(false);
      }

      this.oldIndex = index;
      super.pageChange(index);
   }

   private boolean isValidData(int index) {
      if (this.oldIndex == 6) {
         return true;
      } else if (index == 0) {
         return true;
      } else {
         EditablePage thePage = (EditablePage)this.pages.get(index);
         boolean ok = thePage.isValid();
         thePage.setValid(true);
         return ok;
      }
   }

   private boolean isValid(Object object) {
      if (object instanceof EditablePage) {
         EditablePage p = (EditablePage)object;
         return p.isValid();
      } else {
         return true;
      }
   }

   private void updateModelBySource() {
      Document newDoc = this.getXMLDocumentFormSource();
      this.updateBase(newDoc);
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
         this.handleException("DOM Parser Error", var5.getMessage());
         return null;
      }
   }

   public void doSave(IProgressMonitor monitor) {
      IEditorPart editor = null;
      if (this.parseXMLsuccessed) {
         editor = this.getEditor(6);
         this.saveChanges();
      } else {
         editor = this.getEditor(1);
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
      } else {
         editor = this.getEditor(0);
      }

      editor.doSaveAs();
   }

   public void dispose() {
      super.dispose();
   }

   private void saveChanges() {
      this.commitChanges();
      String xml = this.base.getXMLString();
      this.updateXMLEditor(xml);
      this.updateModelBySource();
   }

   private void commitChanges() {
      this.main.commitChanges(this.isDirty());
      this.characterScreen.commitChanges(this.isDirty());
      this.profileDB.commitChanges(this.isDirty());
      this.documentation.commitChanges(this.isDirty());
      this.audit.commitChanges(this.isDirty());
   }

   public void gotoMarker(IMarker marker) {
      this.setActivePage(5);
      IDE.gotoMarker(this.getEditor(5), marker);
   }

   public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
      if (!(editorInput instanceof IFileEditorInput)) {
         throw new PartInitException("Invalid Input: Must be IFileEditorInput");
      } else {
         try {
            InputStream in = ((IFileEditorInput)editorInput).getFile().getContents();
            this.iFile = ((IFileEditorInput)editorInput).getFile();

            try {
               if (!this.isXMLDocument(in)) {
                  this.convertOK = MessageDialog.openConfirm((Shell)null, "Question", "This non-xml format file will be converted to a xml format.\n Are you sure?");
                  if (this.convertOK) {
                     this.setNonXMLToBase(((IFileEditorInput)editorInput).getFile().getContents());
                     this.setFilenameToBase(((IFileEditorInput)editorInput).getFile().getName().toString());
                     String xml = this.base.getXMLString();
                     InputStream newInput = new ByteArrayInputStream(xml.getBytes());
                     ((IFileEditorInput)editorInput).getFile().setContents(this.serializeInput(newInput), true, true, (IProgressMonitor)null);
                     newInput.close();
                  }
               }
            } catch (IOException var6) {
               var6.printStackTrace();
            }
         } catch (CoreException var7) {
            var7.printStackTrace();
         }

         super.init(site, editorInput);
         this.setTitle(editorInput.getName());
      }
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

   private void setFilenameToBase(String filename) {
      String entityName = "";
      int dot = filename.indexOf(".");
      if (dot != -1) {
         entityName = filename.substring(0, dot);
      }

      this.base.getEntityName().setValue(entityName);
   }

   private void setNonXMLToBase(InputStream in) {
      Properties file = new Properties();
      Hashtable table = new Hashtable();

      try {
         file.load(in);
      } catch (IOException var15) {
         var15.printStackTrace();
      }

      String docValue = "";
      String[] docValueArray = new String[file.size()];
      boolean docExist = false;
      Enumeration keys = file.propertyNames();

      while(keys.hasMoreElements()) {
         String element = (String)keys.nextElement();
         String elementValue = file.getProperty(element);
         if (elementValue.length() > 2) {
            elementValue = elementValue.substring(1, elementValue.length() - 1);
            elementValue = elementValue.replaceAll("\"\"", "\"");
            if (element.indexOf(")") != -1) {
               element = element.substring(0, element.lastIndexOf(")"));
            }

            String[] str = element.split(",");
            int len = str.length;
            switch(len) {
            case 3:
               table.put("FID", elementValue.split("[|]"));
               break;
            case 4:
               table.put(str[3], elementValue.split("[|]"));
               break;
            case 5:
               docExist = true;
               if (str[3].equals("0")) {
                  try {
                     docValueArray[Integer.parseInt(str[4])] = elementValue;
                  } catch (NumberFormatException var14) {
                     ;
                  }
               }
            }
         }
      }

      if (docExist) {
         for(int i = 0; i < docValueArray.length; ++i) {
            if (docValueArray[i] != null && !docValueArray[i].equals("")) {
               if (!docValue.equals("")) {
                  docValue = docValue + System.getProperties().getProperty("line.separator");
               }

               docValue = docValue + docValueArray[i];
            }
         }
      }

      table.put("DOC", new String[]{docValue});
      HashMap allFields = XMLUtil.getAllFields(this.base.getClass());
      Iterator iter = allFields.keySet().iterator();

      while(iter.hasNext()) {
         String tag = (String)iter.next();
         String first = tag.toUpperCase().substring(0, 1);
         tag = first + tag.substring(1);
         String position = SchemaEditorPlugin.getResourceString(tag);
         if (position != null) {
            String tagValue = this.getTagValueFromTable(table, position);
            this.setValueToBase(this.base, tag, tagValue);
         }
      }

   }

   private String getTagValueFromTable(Hashtable table, String position) {
      String tagValue = "";
      if (position.indexOf(",") != -1) {
         String key = position.substring(0, position.indexOf(","));
         int index = Integer.parseInt(position.substring(position.indexOf(",") + 1));
         String[] values = (String[])table.get(key);
         if (values == null) {
            return tagValue;
         }

         if (index <= values.length && index > 0) {
            tagValue = values[index - 1];
         }
      }

      return tagValue;
   }

   private void setValueToBase(Entity base, String tag, String value) {
      Class fieldType = String.class;
      String setterMethodName = "set" + tag + "Value";
      Class[] setterParams = new Class[]{fieldType};
      Method method = null;

      try {
         method = base.getClass().getMethod(setterMethodName, setterParams);
      } catch (Exception var13) {
         String msg = "Exception caught when creating method " + setterMethodName + " for class " + base.getClass().getName();
         System.out.println(msg);
      }

      if (method != null) {
         Object[] setterParamObjects = new Object[1];
         if (value == null) {
            value = "";
         }

         setterParamObjects[0] = value;
         if (!fieldType.isPrimitive()) {
            try {
               method.invoke(base, setterParamObjects);
            } catch (IllegalArgumentException var10) {
               var10.printStackTrace();
            } catch (IllegalAccessException var11) {
               var11.printStackTrace();
            } catch (InvocationTargetException var12) {
               var12.printStackTrace();
            }
         }
      }

   }

   private boolean isXMLDocument(InputStream is) {
      boolean ret = true;
      BufferedReader dis = new BufferedReader(new InputStreamReader(is));

      try {
         String line = dis.readLine();
         if (line != null && line.startsWith("^DBTBL")) {
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

   public boolean isSaveAsAllowed() {
      return true;
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
         this.sourcePage = (XMLEditor)this.getEditor(6);
         IDocument document = this.sourcePage.getDocumentProvider().getDocument(this.getEditorInput());
         if (xmlInput.endsWith(System.getProperties().getProperty("line.separator")) && xmlInput.length() > 0) {
            xmlInput = xmlInput.substring(0, xmlInput.lastIndexOf(System.getProperties().getProperty("line.separator")));
         }

         document.set(xmlInput);
      } catch (Exception var9) {
         this.handleException("DOM Parser Error", var9.getMessage());
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
         this.handleException("DOM Parser Error", var11.getMessage());
      }

   }

   private void handleException(String title, String message) {
      (new ErrorDialog(this.getEditorSite().getShell(), title, "Message: Unable to open Entity Editor", new Status(4, "org.eclipse.core.resources", 0, message, (Throwable)null), 4)).open();
   }

   public Entity getBase() {
      return this.base;
   }
}
