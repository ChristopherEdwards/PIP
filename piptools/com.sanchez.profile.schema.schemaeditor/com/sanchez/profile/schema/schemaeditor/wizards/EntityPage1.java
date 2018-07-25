package com.sanchez.profile.schema.schemaeditor.wizards;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.ui.SchemaInfo;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class EntityPage1 extends WizardPage implements Listener {
   private String errMsg = null;
   private String wizardType;
   private Entity base;
   private SchemaInfo help;
   WizardsUtils wizardsUtils;
   Text descriptionText;
   Combo entityTypeCombo;
   Combo recordTypeCombo;
   Text globalText;
   Text filerText;
   Text keysText;
   Text containerText;
   IContainer folder;
   Button button;
   Text entityNameText;
   Combo networkLocationCombo;
   final String ATTRIBUTE_TYPE = "attribute";
   final String ENTITY_TYPE = "entity";
   protected ArrayList list = new ArrayList();
   protected HashMap map = new HashMap();

   public EntityPage1(String arg0, Entity base, String wizardType) {
      super(arg0);
      this.base = base;
      this.help = new SchemaInfo(base.getHelpFileName());
      this.setDescription(SchemaEditorPlugin.getResourceString("wizard.entity.page1.description"));
      this.wizardType = wizardType;
   }

   public EntityPage1(String arg0, Entity base, String wizardType, ArrayList list) {
      super(arg0);
      this.base = base;
      this.help = new SchemaInfo(base.getHelpFileName());
      this.setDescription(SchemaEditorPlugin.getResourceString("wizard.entity.page1.description"));
      this.wizardType = wizardType;
      this.list = list;

      for(int i = 0; i < list.size(); ++i) {
         String[] tmpString = new String[2];
         String elementString = list.get(i).toString();
         int index = elementString.indexOf(",");
         if (index == -1) {
            tmpString[0] = elementString;
            tmpString[1] = "";
         } else {
            tmpString[0] = elementString.substring(0, index);
            tmpString[1] = elementString.substring(index + 1, elementString.length());
         }

         this.map.put(tmpString[0], tmpString[1]);
      }

   }

   public void onEnterPage() {
      if (this.wizardType.equals("entity")) {
         this.setTitle(SchemaEditorPlugin.getResourceString("wizard.entity.page.title"));
      } else if (this.wizardType.equals("attribute")) {
         this.setTitle(SchemaEditorPlugin.getResourceString("wizard.attribute.page.title"));
      }

      if (this.wizardType.equals("attribute")) {
         this.descriptionText.setEditable(false);
         this.entityTypeCombo.setEnabled(false);
         this.recordTypeCombo.setEnabled(false);
         this.globalText.setEditable(false);
         this.filerText.setEditable(false);
         this.networkLocationCombo.setEnabled(false);
         this.keysText.setEditable(false);
         this.entityNameText.setEditable(false);
      }

      if (!this.list.isEmpty()) {
         try {
            this.entityNameText.setText(this.map.get("File Name").toString());
            this.descriptionText.setText(this.map.get("Description").toString());
            this.keysText.setText(this.map.get("Primary Keys").toString());
            this.entityTypeCombo.setText(this.map.get("File Type").toString());
            this.recordTypeCombo.setText(this.map.get("Record Type").toString());
            this.globalText.setText(this.map.get("Global Name").toString());
            this.filerText.setText(this.map.get("Record Filer Routine").toString());
            this.networkLocationCombo.setText(this.map.get("Network Location").toString());
         } catch (Exception var1) {
            ;
         }
      } else {
         this.descriptionText.setText(this.base.getDescription().getValue());
         this.keysText.setText(this.base.getPrimaryKeys().getValue());
         this.entityTypeCombo.setText(this.wizardsUtils.getComboDisplayValue(this.entityTypeCombo, this.base.getEntityType().getValue()));
         this.recordTypeCombo.setText(this.wizardsUtils.getComboDisplayValue(this.recordTypeCombo, this.base.getRecordType().getValue()));
         this.globalText.setText(this.base.getEntityName().getValue());
         this.filerText.setText(this.base.getFiler().getValue());
         this.networkLocationCombo.setText(this.wizardsUtils.getComboDisplayValue(this.networkLocationCombo, this.base.getNetworkLocation().getValue()));
         this.entityNameText.setText(this.base.getEntityName().getValue());
      }

   }

   public void createControl(Composite parent) {
      Composite composite = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      int ncol = 3;
      gl.numColumns = ncol;
      composite.setLayout(gl);
      this.wizardsUtils = new WizardsUtils();
      TabFolder tabFolder = new TabFolder(composite, 0);
      GridData gd = new GridData(1808);
      gd.widthHint = 450;
      tabFolder.setLayoutData(gd);
      Composite descComposite = new Composite(tabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = ncol;
      descComposite.setLayout(gl);
      if (this.wizardType.equals("attribute")) {
         this.folder = ((NewAttributeWizard)this.getWizard()).folder;
      } else if (this.wizardType.equals("entity")) {
         this.folder = ((NewEntityWizard)this.getWizard()).folder;
      }

      if (this.folder != null) {
         Label label = new Label(descComposite, 131072);
         label.setText("Target folder:");
         this.containerText = new Text(descComposite, 2052);
         this.containerText.setText(this.folder.getFullPath().toString());
         this.containerText.setEditable(false);
         gd = new GridData(768);
         this.containerText.setLayoutData(gd);
         if (this.wizardType.equals("entity")) {
            this.button = new Button(descComposite, 8);
            this.button.setText("Browse...");
            this.button.addListener(13, this);
         } else {
            new Label(descComposite, 131072);
         }
      }

      this.wizardsUtils.createLinkLabel(descComposite, this.base.getEntityName(), this.base);
      this.entityNameText = new Text(descComposite, 2048);
      this.entityNameText.setTextLimit(this.base.getEntityName().getLimit());
      gd = new GridData(768);
      gd.widthHint = 400;
      gd.horizontalAlignment = 16384;
      this.entityNameText.setLayoutData(gd);
      new Label(descComposite, 131072);
      this.wizardsUtils.createLinkLabel(descComposite, this.base.getDescription(), this.base);
      this.descriptionText = new Text(descComposite, 2048);
      this.descriptionText.setTextLimit(this.base.getDescription().getLimit());
      gd = new GridData(768);
      gd.widthHint = 400;
      gd.horizontalAlignment = 16384;
      gd.horizontalSpan = 2;
      this.descriptionText.setLayoutData(gd);
      this.wizardsUtils.createLinkLabel(descComposite, this.base.getPrimaryKeys(), this.base);
      this.keysText = new Text(descComposite, 2048);
      this.keysText.setTextLimit(this.base.getPrimaryKeys().getLimit());
      gd = new GridData(768);
      gd.widthHint = 400;
      gd.horizontalSpan = 2;
      gd.horizontalAlignment = 16384;
      this.keysText.setLayoutData(gd);
      TabItem tabItem1 = new TabItem(tabFolder, 0);
      tabItem1.setText("General");
      tabItem1.setControl(descComposite);
      Composite barComposite = new Composite(tabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = 2;
      barComposite.setLayout(gl);
      this.wizardsUtils.createLinkLabel(barComposite, this.base.getEntityType(), this.base);
      ArrayList lookupTable = this.help.getLookupItems(this.base.getEntityType().getTag());
      this.entityTypeCombo = this.wizardsUtils.createCombo(barComposite, this.base.getEntityType(), lookupTable);
      this.wizardsUtils.createLinkLabel(barComposite, this.base.getRecordType(), this.base);
      lookupTable = this.help.getLookupItems(this.base.getRecordType().getTag());
      this.recordTypeCombo = this.wizardsUtils.createCombo(barComposite, this.base.getRecordType(), lookupTable);
      this.wizardsUtils.createLinkLabel(barComposite, this.base.getGlobal(), this.base);
      this.globalText = new Text(barComposite, 2048);
      this.globalText.setTextLimit(this.base.getGlobal().getLimit());
      gd = new GridData(768);
      gd.widthHint = 400;
      gd.horizontalAlignment = 16384;
      this.globalText.setLayoutData(gd);
      this.wizardsUtils.createLinkLabel(barComposite, this.base.getFiler(), this.base);
      this.filerText = new Text(barComposite, 2048);
      this.filerText.setTextLimit(this.base.getFiler().getLimit());
      gd = new GridData(768);
      gd.widthHint = 400;
      gd.horizontalAlignment = 16384;
      this.filerText.setLayoutData(gd);
      this.wizardsUtils.createLinkLabel(barComposite, this.base.getNetworkLocation(), this.base);
      lookupTable = this.help.getLookupItems(this.base.getNetworkLocation().getTag());
      this.networkLocationCombo = this.wizardsUtils.createCombo(barComposite, this.base.getNetworkLocation(), lookupTable);
      TabItem tabItem2 = new TabItem(tabFolder, 0);
      tabItem2.setText("Advanced");
      tabItem2.setControl(barComposite);
      this.setControl(composite);
      this.addListeners();
      this.onEnterPage();
   }

   private void addListeners() {
      this.keysText.addListener(2, this);
      this.keysText.addFocusListener(new FocusAdapter() {
         public void focusLost(FocusEvent e) {
            String keysValue = EntityPage1.this.keysText.getText().trim();
            if (keysValue.endsWith(",")) {
               keysValue = keysValue.substring(0, keysValue.length() - 1).trim();
            }

            EntityPage1.this.keysText.setText(keysValue.toUpperCase());
         }
      });
      this.descriptionText.addListener(2, this);
      this.entityTypeCombo.addListener(13, this);
      this.recordTypeCombo.addListener(13, this);
      this.globalText.addListener(2, this);
      this.filerText.addListener(2, this);
      this.filerText.addFocusListener(new FocusAdapter() {
         public void focusLost(FocusEvent e) {
            EntityPage1.this.filerText.setText(EntityPage1.this.filerText.getText().toUpperCase());
         }
      });
      this.networkLocationCombo.addListener(13, this);
      if (this.wizardType.equals("entity")) {
         this.entityNameText.addListener(2, this);
         this.entityNameText.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
               EntityPage1.this.entityNameText.setText(EntityPage1.this.entityNameText.getText().toUpperCase());
            }
         });
         this.entityNameText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
               EntityPage1.this.globalText.setText(EntityPage1.this.entityNameText.getText());
               EntityPage1.this.filerText.setText(EntityPage1.this.entityNameText.getText());
            }
         });
      }

   }

   public void handleEvent(Event event) {
      new Status(0, "not_used", 0, "", (Throwable)null);
      this.getWizard().getContainer().updateButtons();
      this.updateStatus((String)null);
      if (event.widget == this.descriptionText) {
         if (!this.wizardsUtils.isTextNonEmpty(this.descriptionText)) {
            this.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.page.reqiredField.message"));
         }
      } else if (event.widget == this.entityTypeCombo) {
         if (this.entityTypeCombo.getText().equals("")) {
            this.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.page.reqiredField.message"));
         }
      } else if (event.widget == this.recordTypeCombo) {
         if (this.recordTypeCombo.getText().equals("")) {
            this.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.page.reqiredField.message"));
         }
      } else if (event.widget == this.globalText) {
         if (!this.wizardsUtils.isTextNonEmpty(this.globalText)) {
            this.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.page.reqiredField.message"));
         }
      } else if (event.widget == this.filerText) {
         if (!this.wizardsUtils.isTextNonEmpty(this.filerText)) {
            this.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.page.reqiredField.message"));
         }
      } else if (event.widget == this.networkLocationCombo) {
         if (this.networkLocationCombo.getText().equals("")) {
            this.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.page.reqiredField.message"));
         }
      } else if (event.widget == this.keysText) {
         if (!this.wizardsUtils.isTextNonEmpty(this.keysText)) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page.reqiredField.message");
            this.updateStatus(this.errMsg);
            this.setPageComplete(false);
         }

         if (!this.isKeyValid(this.keysText.getText())) {
            this.setPageComplete(false);
            this.updateStatus(this.errMsg);
         } else if (this.isDuplicate()) {
            this.setPageComplete(false);
            this.updateStatus(this.errMsg);
         }
      } else if (event.widget == this.entityNameText) {
         if (this.entityNameText.getText().equals("")) {
            this.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.page.reqiredField.message"));
         }

         if (this.isDupTableName(this.entityNameText.getText().toString())) {
            this.setPageComplete(false);
            this.updateStatus(this.errMsg);
         }
      }

   }

   public IWizardPage getNextPage() {
      EntityPage4 page = null;
      if (this.wizardType.equals("entity")) {
         this.saveDataToEntity();
         page = ((NewEntityWizard)this.getWizard()).entityPage4;
         SchemaEditorPlugin.map.put("tableName", this.entityNameText.getText().trim());
         SchemaEditorPlugin.map.put("description", this.descriptionText.getText().trim());
         SchemaEditorPlugin.map.put("primaryKeys", this.keysText.getText().trim());
         SchemaEditorPlugin.map.put("recordType", this.recordTypeCombo.getItem(this.recordTypeCombo.getSelectionIndex()).toString());
         SchemaEditorPlugin.map.put("entityType", this.entityTypeCombo.getItem(this.entityTypeCombo.getSelectionIndex()).toString());
         SchemaEditorPlugin.map.put("global", this.globalText.getText().trim());
         SchemaEditorPlugin.map.put("filer", this.filerText.getText().trim());
         SchemaEditorPlugin.map.put("networkLoc", this.networkLocationCombo.getItem(this.networkLocationCombo.getSelectionIndex()).toString());
      } else if (this.wizardType.equals("attribute")) {
         page = ((NewAttributeWizard)this.getWizard()).entityPage4;
      }

      page.onEnterPage();
      return page;
   }

   private void saveDataToEntity() {
      this.base.getEntityName().setValue(this.entityNameText.getText().trim());
      this.base.getDescription().setValue(this.descriptionText.getText().trim());
      this.base.getEntityType().setValue(this.wizardsUtils.getComboValue(this.entityTypeCombo));
      this.base.getRecordType().setValue(this.wizardsUtils.getComboValue(this.recordTypeCombo));
      this.base.getGlobal().setValue(this.globalText.getText().trim());
      this.base.getFiler().setValue(this.filerText.getText().trim());
      this.base.getNetworkLocation().setValue(this.wizardsUtils.getComboValue(this.networkLocationCombo));
      this.base.getSystemName().setValue("PBS");
   }

   public boolean canFlipToNextPage() {
      if (this.wizardType.equals("attribute")) {
         return true;
      } else {
         return this.isKeyValid(this.keysText.getText()) && !this.isDuplicate() && this.wizardsUtils.isTextNonEmpty(this.descriptionText) && !this.entityTypeCombo.getText().equals("") && !this.recordTypeCombo.getText().equals("") && this.wizardsUtils.isTextNonEmpty(this.globalText) && !this.networkLocationCombo.getText().equals("");
      }
   }

   private boolean isDupTableName(String tableName) {
      for(int i = 0; i < SchemaEditorPlugin.tableList.size(); ++i) {
         String name = SchemaEditorPlugin.tableList.get(i).toString().split(",")[0];
         if (tableName.equalsIgnoreCase(name)) {
            this.errMsg = "This table name already exists.";
            return true;
         }
      }

      return false;
   }

   private boolean isKeyValid(String keyString) {
      StringTokenizer keyTokenizer = new StringTokenizer(keyString, ",");
      int keyCount = keyTokenizer.countTokens();

      for(int i = 0; i < keyCount; ++i) {
         String thisKey = ((String)keyTokenizer.nextElement()).trim();
         if (thisKey.equals("")) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.key.message0");
            return false;
         }

         if (this.wizardsUtils.isSQLKeyword(thisKey)) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.key.message1");
            return false;
         }

         if (thisKey.indexOf("$") != -1) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.key.message2");
            return false;
         }

         if (i == keyCount - 1 && (thisKey.startsWith("\"") && thisKey.endsWith("\"") || this.wizardsUtils.isNumber(thisKey))) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.key.message3");
            return false;
         }

         if (thisKey.indexOf("\"") != -1 && (!thisKey.startsWith("\"") || !thisKey.endsWith("\""))) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.key.message4");
            return false;
         }

         if (thisKey.indexOf("'") != -1) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.key.message5");
            return false;
         }

         if (thisKey.indexOf("_") != -1) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.key.message6");
            return false;
         }
      }

      return true;
   }

   private boolean isDuplicate() {
      EntityPage3 page = null;
      page = ((NewEntityWizard)this.getWizard()).entityPage3;
      String existString = this.keysText.getText();
      if (!page.columnsText.getText().equals("")) {
         existString = existString + "," + page.columnsText.getText();
      }

      if (this.wizardType.equals("attribute") && !page.newColumnsText.getText().equals("")) {
         existString = existString + "," + page.newColumnsText.getText();
      }

      existString = existString.toUpperCase();
      Hashtable ht = new Hashtable();
      StringTokenizer tokenizer = new StringTokenizer(existString, ",");
      int keyCount = tokenizer.countTokens();

      for(int i = 0; i < keyCount; ++i) {
         String thisKey = ((String)tokenizer.nextElement()).trim();
         if (ht.containsKey(thisKey)) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.duplicateItem.message");
            return true;
         }

         ht.put(thisKey, "");
      }

      return false;
   }

   private void updateStatus(String message) {
      this.setMessage(message, 3);
      this.setPageComplete(message == null);
   }
}
