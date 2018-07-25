package com.sanchez.profile.schema.schemaeditor.wizards;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Entity;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class EntityPage3 extends WizardPage implements Listener {
   private String errMsg = null;
   private Entity base;
   private WizardsUtils wizardsUtils;
   Text columnsText;
   Text newColumnsText;
   final String ATTRIBUTE_TYPE = "attribute";
   final String ENTITY_TYPE = "entity";
   private String wizardType;

   public EntityPage3(String arg0, Entity base, String wizardType) {
      super(arg0);
      this.base = base;
      this.setDescription(SchemaEditorPlugin.getResourceString("wizard.entity.page3.description"));
      this.wizardType = wizardType;
      this.setPageComplete(false);
   }

   public void onEnterPage() {
      if (this.wizardType.equals("entity")) {
         this.setTitle(SchemaEditorPlugin.getResourceString("wizard.entity.page.title") + " - " + this.base.getEntityName().getValue() + ".TBL");
      }

      if (this.wizardType.equals("attribute")) {
         this.setTitle(SchemaEditorPlugin.getResourceString("wizard.attribute.page.title"));
      }

      this.setDescription(SchemaEditorPlugin.getResourceString("wizard.entity.page3.description"));
   }

   public void createControl(Composite parent) {
      this.wizardsUtils = new WizardsUtils();
      Composite composite = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      int ncol = 2;
      gl.numColumns = ncol;
      composite.setLayout(gl);
      Label columnLabel = new Label(composite, 0);
      columnLabel.setText("Columns:");
      GridData gd = new GridData(2);
      columnLabel.setLayoutData(gd);
      gd = new GridData(768);
      if (this.wizardType.equals("attribute")) {
         this.columnsText = new Text(composite, 2882);
         gd.widthHint = 600;
         gd.heightHint = 200;
      } else if (this.wizardType.equals("entity")) {
         this.columnsText = new Text(composite, 2048);
      }

      this.columnsText.setLayoutData(gd);
      if (this.wizardType.equals("attribute")) {
         (new Label(composite, 0)).setText("New Columns:");
         this.newColumnsText = new Text(composite, 2048);
         this.newColumnsText.setText("");
         gd = new GridData(768);
         this.newColumnsText.setLayoutData(gd);
         String columnsString = this.getColumns();
         this.columnsText.setText(columnsString);
         this.columnsText.setEditable(false);
      }

      this.setControl(composite);
      this.addListeners();
   }

   private String getColumns() {
      String columnsString = "";
      Hashtable attributeTable = ((NewAttributeWizard)this.getWizard()).getAttributeBaseTable();
      Enumeration attributes = attributeTable.keys();

      while(attributes.hasMoreElements()) {
         String column = (String)attributes.nextElement();
         if (!this.wizardsUtils.isInString(this.base.getPrimaryKeys().getValue(), column)) {
            columnsString = columnsString + column + ",";
         }
      }

      if (columnsString.endsWith(",")) {
         columnsString = columnsString.substring(0, columnsString.lastIndexOf(","));
      }

      return columnsString;
   }

   private void addListeners() {
      this.columnsText.addListener(2, this);
      this.columnsText.addFocusListener(new FocusAdapter() {
         public void focusLost(FocusEvent e) {
            String columnsValue = EntityPage3.this.columnsText.getText().trim();
            if (columnsValue.endsWith(",")) {
               columnsValue = columnsValue.substring(0, columnsValue.length() - 1).trim();
            }

            EntityPage3.this.columnsText.setText(columnsValue.toUpperCase());
         }
      });
      if (this.wizardType.equals("attribute")) {
         this.newColumnsText.addListener(2, this);
         this.newColumnsText.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
               String columnsValue = EntityPage3.this.newColumnsText.getText().trim();
               if (columnsValue.endsWith(",")) {
                  columnsValue = columnsValue.substring(0, columnsValue.length() - 1).trim();
               }

               EntityPage3.this.newColumnsText.setText(columnsValue.toUpperCase());
            }
         });
      }

   }

   public boolean isColumnValid(String columnsValue) {
      StringTokenizer keyTokenizer = new StringTokenizer(columnsValue, ",");
      int keyCount = keyTokenizer.countTokens();

      for(int i = 0; i < keyCount; ++i) {
         String thisKey = ((String)keyTokenizer.nextElement()).trim();
         if (thisKey.equals("")) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.column.message0");
            return false;
         }

         if (this.wizardsUtils.isNumber(thisKey)) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.column.message1");
            return false;
         }

         if (this.wizardsUtils.isSQLKeyword(thisKey)) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.column.message2");
            return false;
         }

         if (thisKey.indexOf("$") != -1) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.column.message3");
            return false;
         }

         if (Character.isDigit(thisKey.charAt(0))) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.column.message4");
            return false;
         }

         if (thisKey.indexOf("'") != -1 || thisKey.indexOf("\"") != -1) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.column.message5");
            return false;
         }

         if (thisKey.indexOf("_") != -1) {
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page3.column.message6");
            return false;
         }
      }

      return true;
   }

   public boolean canFlipToNextPage() {
      return this.isColumnValid(this.columnsText.getText()) && !this.isDuplicate();
   }

   public void handleEvent(Event e) {
      new Status(0, "not_used", 0, "", (Throwable)null);
      this.getWizard().getContainer().updateButtons();
      this.updateStatus((String)null);
      this.errMsg = "";
      if (e.widget == this.columnsText) {
         if (!this.isColumnValid(this.columnsText.getText())) {
            this.setPageComplete(false);
            this.updateStatus(this.errMsg);
         } else if (this.isDuplicate()) {
            this.setPageComplete(false);
            this.updateStatus(this.errMsg);
         }
      }

      if (this.wizardType.equals("attribute") && e.widget == this.newColumnsText) {
         if (!this.wizardsUtils.isTextNonEmpty(this.newColumnsText)) {
            this.setPageComplete(false);
            this.errMsg = SchemaEditorPlugin.getResourceString("wizard.entity.page.reqiredField.message");
            this.updateStatus(this.errMsg);
         }

         if (!this.isColumnValid(this.newColumnsText.getText())) {
            this.updateStatus(this.errMsg);
         } else if (this.isDuplicate()) {
            this.setPageComplete(false);
            this.updateStatus(this.errMsg);
         }
      }

   }

   private boolean isDuplicate() {
      EntityPage1 page = null;
      page = ((NewEntityWizard)this.getWizard()).entityPage1;
      String existString = page.keysText.getText();
      if (!this.columnsText.getText().equals("")) {
         existString = existString + "," + this.columnsText.getText();
      }

      if (this.wizardType.equals("attribute") && !this.newColumnsText.getText().equals("")) {
         existString = existString + "," + this.newColumnsText.getText();
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

   public IWizardPage getNextPage() {
      EntityPage1 page1 = null;
      if (this.wizardType.equals("attribute")) {
         page1 = ((NewAttributeWizard)this.getWizard()).entityPage1;
      } else {
         page1 = ((NewEntityWizard)this.getWizard()).entityPage1;
      }

      EntityPage4 page = null;
      if (this.wizardType.equals("entity")) {
         this.base.getPrimaryKeys().setValue(page1.keysText.getText().trim());
         page = ((NewEntityWizard)this.getWizard()).entityPage4;
      } else if (this.wizardType.equals("attribute")) {
         page = ((NewAttributeWizard)this.getWizard()).entityPage4;
      }

      page.onEnterPage();
      return page;
   }

   private void updateStatus(String message) {
      this.setMessage(message, 3);
      this.setPageComplete(message == null);
   }
}
