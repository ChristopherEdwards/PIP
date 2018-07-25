package com.fis.profile.tablelist.dialogs;

import com.fis.profile.tablelist.utils.GetDataList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class UpdateTrigger extends Dialog {
   private Text tableName;
   private Text triggerName;
   private Text description;
   private Text columns;
   private Text condition;
   private Button beforeInsert;
   private Button beforeDelete;
   private Button beforeUpdate;
   private Button afterInsert;
   private Button afterDelete;
   private Button afterUpdate;
   private Button finProcess;
   private Button onlineProcess;
   private Button batchProcess;
   private String tableNameVal = "";
   private String descVal = "";
   private String triggerNameVal = "";
   private String bInsert = "";
   private String bUpdate = "";
   private String bDelete = "";
   private String aInsert = "";
   private String aUpdate = "";
   private String aDelete = "";
   private String columnsVal = "";
   private String conditionVal = "";
   private String processModeVal = "";

   public UpdateTrigger(Shell parentShell, String value) {
      super(parentShell);
      String[] values = value.split(",");
      if (values.length > 11) {
         this.tableNameVal = values[0];
         this.descVal = values[1];
         this.triggerNameVal = values[2];
         this.columnsVal = values[3].replaceAll("aaaaaa", ",");
         this.bInsert = values[4];
         this.bUpdate = values[5];
         this.bDelete = values[6];
         this.aInsert = values[7];
         this.aUpdate = values[8];
         this.aDelete = values[9];
         this.conditionVal = values[10].replaceAll("aaaaaa", ",");
         this.processModeVal = values[11];
      }

   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText("Update Trigger");
   }

   protected Control createDialogArea(Composite composite) {
      Composite parent = new Composite(composite, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      parent.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      gridData.heightHint = 400;
      gridData.widthHint = 600;
      composite.setLayoutData(gridData);
      TabFolder tabFolder = new TabFolder(parent, 0);
      GridData gd = new GridData(768);
      gd.widthHint = 570;
      tabFolder.setLayoutData(gd);
      Composite mainTab = this.buildMainTab(tabFolder);
      TabItem tabItem1 = new TabItem(tabFolder, 0);
      tabItem1.setText("Main");
      tabItem1.setControl(mainTab);
      return parent;
   }

   private Composite buildMainTab(TabFolder parent) {
      Composite mainTab = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 2;
      mainTab.setLayout(gl);
      GridData gd = new GridData(768);
      mainTab.setLayoutData(gd);
      this.tableName = this.createText(mainTab, "Table Name", 100);
      this.tableName.setEnabled(false);
      this.tableName.setText(this.tableNameVal);
      this.triggerName = this.createText(mainTab, "Trigger Name", 55);
      this.triggerName.setEnabled(false);
      this.triggerName.setText(this.triggerNameVal);
      this.description = this.createText(mainTab, "Description", 40);
      this.description.setText(this.descVal);
      Group actionGroup = new Group(mainTab, 0);
      actionGroup.setText("Trigger Actions");
      gl = new GridLayout();
      gl.numColumns = 6;
      actionGroup.setLayout(gl);
      GridData gridData = new GridData(768);
      gridData.horizontalSpan = 2;
      actionGroup.setLayoutData(gridData);
      this.beforeInsert = this.createCheckBox(actionGroup, "BEFORE INSERT");
      this.beforeInsert.setSelection(this.getBooleanValue(this.bInsert));
      this.beforeUpdate = this.createCheckBox(actionGroup, "BEFORE UPDATE");
      this.beforeUpdate.setSelection(this.getBooleanValue(this.bUpdate));
      this.beforeDelete = this.createCheckBox(actionGroup, "BEFORE DELETE");
      this.beforeDelete.setSelection(this.getBooleanValue(this.bDelete));
      this.afterInsert = this.createCheckBox(actionGroup, "AFTER INSERT");
      this.afterInsert.setSelection(this.getBooleanValue(this.aInsert));
      this.afterUpdate = this.createCheckBox(actionGroup, "AFTER UPDATE");
      this.afterUpdate.setSelection(this.getBooleanValue(this.aUpdate));
      this.afterDelete = this.createCheckBox(actionGroup, "AFTER DELETE");
      this.afterDelete.setSelection(this.getBooleanValue(this.aDelete));
      Label label = new Label(mainTab, 0);
      label.setText("A list of column names associated with the UPDATE action:");
      gridData = new GridData(768);
      gridData.horizontalSpan = 2;
      label.setLayoutData(gridData);
      this.columns = this.createText(mainTab, "", 255);
      if (this.columnsVal != null && !this.columnsVal.equalsIgnoreCase("null")) {
         this.columns.setText(this.columnsVal);
      } else {
         this.columns.setText("");
      }

      this.columns.setEnabled(false);
      gridData = new GridData(768);
      gridData.horizontalSpan = 2;
      this.columns.setLayoutData(gridData);
      this.condition = this.createText(mainTab, "Execute #IF Condition: ", 255);
      if (this.conditionVal != null && !this.conditionVal.equalsIgnoreCase("null")) {
         this.condition.setText(this.conditionVal);
      } else {
         this.condition.setText("");
      }

      Group processGroup = new Group(mainTab, 0);
      processGroup.setText("Processing Mode");
      gl = new GridLayout();
      gl.numColumns = 6;
      processGroup.setLayout(gl);
      gridData = new GridData(768);
      gridData.horizontalSpan = 2;
      processGroup.setLayoutData(gridData);
      this.finProcess = this.createCheckBox(processGroup, "Financial Processes");
      if (this.processModeVal.indexOf("F") != -1) {
         this.finProcess.setSelection(true);
      } else {
         this.finProcess.setSelection(false);
      }

      this.onlineProcess = this.createCheckBox(processGroup, "On-line Processes");
      if (this.processModeVal.indexOf("O") != -1) {
         this.onlineProcess.setSelection(true);
      } else {
         this.onlineProcess.setSelection(false);
      }

      this.batchProcess = this.createCheckBox(processGroup, "Batch Processes");
      if (this.processModeVal.indexOf("B") != -1) {
         this.batchProcess.setSelection(true);
      } else {
         this.batchProcess.setSelection(false);
      }

      return mainTab;
   }

   private boolean getBooleanValue(String val) {
      return val.equalsIgnoreCase("true");
   }

   public Text createText(Composite parent, String labelString, int length) {
      GridData gd;
      if (labelString.length() > 0) {
         Label label = new Label(parent, 0);
         gd = new GridData();
         label.setLayoutData(gd);
         label.setText(labelString + ":");
      }

      Text text = new Text(parent, 2048);
      gd = new GridData(768);
      gd.heightHint = 15;
      text.setLayoutData(gd);
      text.setTextLimit(length);
      return text;
   }

   public Button createCheckBox(Composite parent, String labelString) {
      Button button = new Button(parent, 32);
      button.setText(labelString);
      GridData gd = new GridData(768);
      gd.horizontalSpan = 2;
      button.setLayoutData(gd);
      return button;
   }

   private boolean validate() {
      String returnMsg = "";
      if (this.description.getText().length() == 0) {
         returnMsg = "Description is required.";
      }

      String message = "This table can not be updated due to below problems: \n";
      message = message + returnMsg;
      message = message + "\n Please go back and modify your changes.";
      boolean isValid;
      if (returnMsg.length() > 0) {
         MessageDialog.openInformation((Shell)null, "Validate Error", message);
         isValid = false;
      } else {
         boolean ok = MessageDialog.openConfirm((Shell)null, "Are you sure to update?", "Changes will be updated to Profile DB. This dialog will be closed and the PSL View will be refreshed with new information.");
         if (!ok) {
            isValid = false;
         } else {
            isValid = true;
         }
      }

      return isValid;
   }

   protected void okPressed() {
      if (this.validate()) {
         this.doUpdate();
         super.okPressed();
      }
   }

   private void doUpdate() {
      String statement = "UPDATE DBTBL7 SET DES = '" + this.description.getText().replaceAll("'", "") + "', ";
      if (this.beforeInsert.getSelection()) {
         statement = statement + "ACTBI = '1', ";
      } else {
         statement = statement + "ACTBI = '0', ";
      }

      if (this.beforeUpdate.getSelection()) {
         statement = statement + "ACTBU = '1', ";
      } else {
         statement = statement + "ACTBU = '0', ";
      }

      if (this.beforeDelete.getSelection()) {
         statement = statement + "ACTBD = '1', ";
      } else {
         statement = statement + "ACTBD = '0', ";
      }

      if (this.afterInsert.getSelection()) {
         statement = statement + "ACTAI = '1', ";
      } else {
         statement = statement + "ACTAI = '0', ";
      }

      if (this.afterUpdate.getSelection()) {
         statement = statement + "ACTAU = '1', ";
      } else {
         statement = statement + "ACTAU = '0', ";
      }

      if (this.afterDelete.getSelection()) {
         statement = statement + "ACTAD = '1', ";
      } else {
         statement = statement + "ACTAD = '0', ";
      }

      String modeString = "";
      if (this.finProcess.getSelection()) {
         modeString = "F,";
      }

      if (this.onlineProcess.getSelection()) {
         modeString = modeString + "O,";
      }

      if (this.batchProcess.getSelection()) {
         modeString = modeString + "B";
      }

      if (modeString.endsWith(",")) {
         modeString = modeString.substring(0, modeString.length() - 1);
      }

      statement = statement + "PROCMODE = '" + modeString + "', ";
      if (this.condition.getText().length() == 0) {
         statement = statement + "IFCOND = 'null' ";
      } else {
         statement = statement + "IFCOND = '" + this.condition.getText().replaceAll("'", "") + "' ";
      }

      statement = statement + " WHERE TABLE = '" + this.tableNameVal + "' AND TRGID = '" + this.triggerNameVal + "'";
      GetDataList doUpdate = new GetDataList(statement, (String[])null);
      doUpdate.updateRecord();
   }
}
