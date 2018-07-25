package com.fis.profile.tablelist.dialogs;

import com.fis.profile.tablelist.TableList;
import com.fis.profile.tablelist.details.TableDetailsView;
import com.fis.profile.tablelist.utils.GetDataList;
import com.fis.profile.tablelist.utils.SchemaInfo;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class UpdateIndex extends Dialog {
   private SchemaInfo help = new SchemaInfo("IndexInfo.xml");
   private Text indexName;
   private Text description;
   private Text globalName;
   private Text orderBy;
   private Text query1;
   private Text query2;
   private Text superType;
   private Button nullFlag;
   private Button saveFlag;
   private Button upCase;
   private String fieldName = "";
   private HashMap map = new HashMap();

   public UpdateIndex(Shell parentShell) {
      super(parentShell);
   }

   public UpdateIndex(Shell parentShell, ArrayList list, String fieldName) {
      super(parentShell);
      this.fieldName = fieldName;

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

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText("Update Index");
   }

   protected Control createDialogArea(Composite composite) {
      Composite parent = new Composite(composite, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      parent.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      gridData.heightHint = 350;
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
      this.indexName = this.createText(mainTab, "Index Name", 16, "INDEXNM");
      this.indexName.setText(this.fieldName);
      if (this.fieldName.equals("")) {
         this.indexName.setEditable(true);
      } else {
         this.indexName.setEditable(false);
      }

      this.description = this.createText(mainTab, "Index Description", 29, "IDXDESC");
      this.globalName = this.createText(mainTab, "Global Name", 40, "GLOBAL");
      this.orderBy = this.createText(mainTab, "Order By", 40, "ORDERBY");
      this.query1 = this.createText(mainTab, "Query Definition #1", 60, "QRY1");
      this.query2 = this.createText(mainTab, "Query Definition #2", 60, "QRY2");
      this.superType = this.createText(mainTab, "Supertype File Name", 8, "PARFID");
      this.nullFlag = this.createCheckBox(mainTab, "Allow NULL Value", "NULLFLG");
      this.saveFlag = this.createCheckBox(mainTab, "Save Old DAYEND Index Entry", "SAVFLG");
      this.upCase = this.createCheckBox(mainTab, "Convert to Uppercase", "UPCASE");
      return mainTab;
   }

   public Text createText(Composite parent, String labelString, int length, String tag) {
      Label label = new Label(parent, 0);
      GridData gd = new GridData();
      label.setLayoutData(gd);
      label.setText(labelString + ":");
      label.setToolTipText(this.help.getHelp(tag));
      Text text = new Text(parent, 2048);
      gd = new GridData(768);
      gd.heightHint = 15;
      text.setLayoutData(gd);
      text.setTextLimit(length);
      if (!this.map.containsKey(labelString)) {
         return text;
      } else {
         String value = this.map.get(labelString).toString();
         if (value == null || value.equalsIgnoreCase("null")) {
            value = "";
         }

         text.setText(value);
         return text;
      }
   }

   public Button createCheckBox(Composite parent, String labelString, String tag) {
      Button button = new Button(parent, 32);
      button.setText(labelString);
      button.setToolTipText(this.help.getHelp(tag));
      GridData gd = new GridData(768);
      gd.horizontalSpan = 2;
      button.setLayoutData(gd);
      if (!this.map.containsKey(labelString)) {
         return button;
      } else {
         String value = this.map.get(labelString).toString();
         if (value == null) {
            value = "false";
         }

         if (value.equals("true")) {
            button.setSelection(true);
         } else {
            button.setSelection(false);
         }

         return button;
      }
   }

   private boolean validate() {
      String returnMsg = "";
      returnMsg = returnMsg + this.validateIndexName();
      returnMsg = returnMsg + this.validateIndexDesc();
      returnMsg = returnMsg + this.validateQuery();
      String message = "This table can not be updated due to below problems: \n";
      message = message + returnMsg;
      message = message + "\n Please go back and modify your changes.";
      boolean isValid;
      if (returnMsg.length() > 0) {
         MessageDialog.openInformation((Shell)null, "Validate Error", message);
         isValid = false;
      } else {
         boolean ok = MessageDialog.openConfirm((Shell)null, "Are you sure to update?", "Changes will be updated to Profile DB. This dialog will be closed and the Index View will be refreshed with new information.");
         if (!ok) {
            isValid = false;
         } else {
            isValid = true;
         }
      }

      return isValid;
   }

   private String validateIndexDesc() {
      String returnMsg = "";
      if (this.description.getText().length() == 0) {
         returnMsg = "Index Description is required and can not be empty.\n";
      }

      return returnMsg;
   }

   private String validateIndexName() {
      String returnMsg = "";
      if (this.indexName.getText().length() == 0) {
         returnMsg = "Index Name is required and can not be empty.\n";
      } else if (this.fieldName.equals("")) {
         TableDetailsView tableDetailView = (TableDetailsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.details.TableDetailsView");
         ArrayList list = tableDetailView.getIndexList();
         if (list.contains(this.indexName.getText().trim().toUpperCase())) {
            returnMsg = returnMsg + "Index Name already exists.\n";
         } else if (this.indexName.getText().indexOf("_") != -1) {
            returnMsg = returnMsg + "Index Name cannot contain '_'\n";
         }
      }

      return returnMsg;
   }

   private String validateQuery() {
      String returnMsg = "";
      if (this.query2.getText().length() > 0 && this.query1.getText().length() == 0) {
         returnMsg = "Query Definition #1 is empty.\n";
      }

      return returnMsg;
   }

   protected void okPressed() {
      if (this.validate()) {
         if (this.fieldName.equals("")) {
            this.doAdd();
         } else {
            this.doUpdate();
         }

         super.okPressed();
      }
   }

   private void doAdd() {
      String statement = "INSERT INTO DBTBL8 (FID, INDEXNM, %LIBS, IDXDESC, GLOBAL, ORDERBY, QRY1, QRY2, PARFID, NULLFLG, SAVFLG, UPCASE) VALUES ('" + TableList.getSelectedTableName() + "', '" + this.indexName.getText().replaceAll("'", "''").toUpperCase() + "', 'SYSDEV', '" + this.description.getText().replaceAll("'", "''") + "', '" + this.globalName.getText().replaceAll("'", "''") + "', '" + this.orderBy.getText().replaceAll("'", "''") + "', '" + this.query1.getText().replaceAll("'", "''") + "', '" + this.query2.getText().replaceAll("'", "''") + "', '" + this.superType.getText().replaceAll("'", "''") + "', ";
      if (this.nullFlag.getSelection()) {
         statement = statement + "'1', ";
      } else {
         statement = statement + "'0', ";
      }

      if (this.saveFlag.getSelection()) {
         statement = statement + "'1', ";
      } else {
         statement = statement + "'0', ";
      }

      if (this.upCase.getSelection()) {
         statement = statement + "'1' )";
      } else {
         statement = statement + "'0' )";
      }

      GetDataList doUpdate = new GetDataList(statement, (String[])null);
      doUpdate.updateRecord();
   }

   private void doUpdate() {
      String statement = "UPDATE DBTBL8 SET IDXDESC = '" + this.description.getText().replaceAll("'", "''") + "', GLOBAL = '" + this.globalName.getText().replaceAll("'", "''") + "', ORDERBY = '" + this.orderBy.getText().replaceAll("'", "''") + "', QRY1 = '" + this.query1.getText().replaceAll("'", "''") + "', QRY2 = '" + this.query2.getText().replaceAll("'", "''") + "', PARFID = '" + this.superType.getText().replaceAll("'", "''") + "', ";
      if (this.nullFlag.getSelection()) {
         statement = statement + "NULLFLG = '1', ";
      } else {
         statement = statement + "NULLFLG = '0', ";
      }

      if (this.saveFlag.getSelection()) {
         statement = statement + "SAVFLG = '1', ";
      } else {
         statement = statement + "SAVFLG = '0', ";
      }

      if (this.upCase.getSelection()) {
         statement = statement + "UPCASE = '1' ";
      } else {
         statement = statement + "UPCASE = '0' ";
      }

      statement = statement + "WHERE FID = '" + TableList.getSelectedTableName() + "' " + "AND INDEXNM = '" + this.fieldName + "'";
      GetDataList doUpdate = new GetDataList(statement, (String[])null);
      doUpdate.updateRecord();
   }
}
