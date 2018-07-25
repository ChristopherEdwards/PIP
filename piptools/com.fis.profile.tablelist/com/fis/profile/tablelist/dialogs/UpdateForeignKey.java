package com.fis.profile.tablelist.dialogs;

import com.fis.profile.tablelist.TableList;
import com.fis.profile.tablelist.details.TableDetailsView;
import com.fis.profile.tablelist.utils.GetDataList;
import com.fis.profile.tablelist.utils.SchemaInfo;
import com.fis.profile.tablelist.views.TableListView;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class UpdateForeignKey extends Dialog {
   private SchemaInfo help = new SchemaInfo("ForeignKeyInfo.xml");
   private Text foreignAttrList;
   private Text foreignEntity;
   private Text maxInRef;
   private Text minInRef;
   private Text maxInForeign;
   private Text minInForeign;
   private Combo updateConstraint;
   private Combo deleteContraint;
   private String fieldName = "";
   private HashMap map = new HashMap();

   public UpdateForeignKey(Shell parentShell) {
      super(parentShell);
   }

   public UpdateForeignKey(Shell parentShell, ArrayList list, String fieldName) {
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
      shell.setText("Update Foreign Key");
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
      this.foreignAttrList = this.createText(mainTab, "Foreign Key Reference", 60, "");
      this.foreignAttrList.setText(this.fieldName);
      if (this.fieldName.equals("")) {
         this.foreignAttrList.setEditable(true);
      } else {
         this.foreignAttrList.setEditable(false);
      }

      this.foreignEntity = this.createText(mainTab, "Foreign File Name", 256, "ForeignFileName");
      if (this.fieldName.equals("")) {
         this.foreignEntity.setEditable(true);
      } else {
         this.foreignEntity.setEditable(false);
      }

      this.maxInRef = this.createText(mainTab, "Maximum Occurrences in Referencing Table", 2, "");
      this.minInRef = this.createText(mainTab, "Minimum Occurrences in Referencing Table", 2, "");
      this.maxInForeign = this.createText(mainTab, "Maximum Occurrences in Foreign Table", 2, "");
      this.minInForeign = this.createText(mainTab, "Minimum Occurrences in Foreign Table", 2, "");
      String[] items = new String[]{"Restricted", "Cascade"};
      this.updateConstraint = this.createCombo(mainTab, items, "Update Constraint", "UpdateConstraint");
      this.deleteContraint = this.createCombo(mainTab, items, "Delete Constraint", "DeleteConstraint");
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

   public Combo createCombo(Composite parent, String[] items, String labelString, String tag) {
      Label label = new Label(parent, 0);
      GridData gd = new GridData();
      label.setLayoutData(gd);
      label.setText(labelString + ":");
      label.setToolTipText(this.help.getHelp(tag));
      Combo combo = new Combo(parent, 0);

      for(int i = 0; i < items.length; ++i) {
         combo.add(items[i]);
      }

      if (!this.map.containsKey(labelString)) {
         return combo;
      } else {
         String value = this.map.get(labelString).toString();
         if (value == null || value.equalsIgnoreCase("null")) {
            value = "0";
         }

         int index = 0;

         for(int i = 0; i < items.length; ++i) {
            if (value.equals(items[i])) {
               index = i;
               break;
            }
         }

         combo.select(index);
         return combo;
      }
   }

   private boolean testNumber(String text) {
      if (text == null || text.equals("") || text.equalsIgnoreCase("null")) {
         text = "0";
      }

      boolean isNumber;
      try {
         Integer.parseInt(text);
         isNumber = true;
      } catch (NumberFormatException var3) {
         isNumber = false;
      }

      return isNumber;
   }

   private String validateNumber() {
      String returnMsg = "";
      if (!this.testNumber(this.maxInRef.getText())) {
         returnMsg = returnMsg + "\nMaximum Occurrences in Referencing Table needs to be a number.\n";
      }

      if (!this.testNumber(this.minInRef.getText())) {
         returnMsg = returnMsg + "Minimum Occurrences in Referencing Table needs to be a number.\n";
      }

      if (!this.testNumber(this.maxInForeign.getText())) {
         returnMsg = returnMsg + "Maximum Occurrences in Foreign Table needs to be a number.\n";
      }

      if (!this.testNumber(this.minInForeign.getText())) {
         returnMsg = returnMsg + "Minimum Occurrences in Foreign Table needs to be a number.\n";
      }

      return returnMsg;
   }

   private String validateRef() {
      String returnMsg = "";
      TableListView view = (TableListView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.views.TableListView");
      TableDetailsView tableDetailView = (TableDetailsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.details.TableDetailsView");
      ArrayList list = view.getTableList();
      if (!list.contains(this.foreignEntity.getText().trim().toUpperCase())) {
         returnMsg = returnMsg + "Foreign File Name is not valid. This should be a valid table name.\n";
      } else {
         String statement = "Select DI from DBTBL1D WHERE FID = '" + this.foreignEntity.getText().trim().replaceAll("'", "''").toUpperCase() + "'";
         GetDataList dataList = new GetDataList(statement, (String[])null);
         ArrayList colList = dataList.loadData_1(1);
         if (!colList.contains(this.foreignAttrList.getText().trim().toUpperCase())) {
            returnMsg = returnMsg + "Foreign Key Reference is not valid. This should be a valid column name of table " + this.foreignEntity.getText().trim().toUpperCase() + "\n";
         } else if (this.fieldName.equals("")) {
            ArrayList foreignKeyList = tableDetailView.getForeignKeyList();
            if (foreignKeyList.contains(this.foreignAttrList.getText().trim().toUpperCase())) {
               returnMsg = returnMsg + "Foreign Key Reference already exists.\n";
            }
         }
      }

      return returnMsg;
   }

   private boolean validate() {
      boolean isValid = true;
      String message = "";
      message = message + this.validateRef();
      message = message + this.validateNumber();
      if (message.length() > 0) {
         MessageDialog.openInformation((Shell)null, "Validate Error", message);
         isValid = false;
      } else {
         boolean ok = MessageDialog.openConfirm((Shell)null, "Are you sure to update?", "Changes will be updated to Profile DB. This dialog will be closed and the Foreign Key View will be refreshed with new information.");
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
         if (this.fieldName.equals("")) {
            this.doAdd();
         } else {
            this.doUpdate();
         }

         super.okPressed();
      }
   }

   private void doAdd() {
      String statement = "INSERT INTO DBTBL1F (FID, FKEYS, %LIBS, TBLREF, RCFRMAX, RCFRMIN, RCTOMAX, RCTOMIN,  UPD, DEL) VALUES ('" + TableList.getSelectedTableName() + "', '" + this.foreignAttrList.getText().replaceAll("'", "''").toUpperCase() + "', 'SYSDEV', '" + this.foreignEntity.getText().replaceAll("'", "''").toUpperCase() + "', ";
      if (this.maxInRef.getText().equals("")) {
         statement = statement + "null, ";
      } else {
         statement = statement + "'" + this.maxInRef.getText() + "', ";
      }

      if (this.minInRef.getText().equals("")) {
         statement = statement + "null, ";
      } else {
         statement = statement + "'" + this.minInRef.getText() + "', ";
      }

      if (this.maxInForeign.getText().equals("")) {
         statement = statement + "null, ";
      } else {
         statement = statement + "'" + this.maxInForeign.getText() + "', ";
      }

      if (this.minInForeign.getText().equals("")) {
         statement = statement + "null, ";
      } else {
         statement = statement + "'" + this.minInForeign.getText() + "', ";
      }

      if (this.updateConstraint.getSelectionIndex() == 0) {
         statement = statement + "'0', ";
      } else if (this.updateConstraint.getSelectionIndex() == 1) {
         statement = statement + "'2', ";
      } else {
         statement = statement + "null, ";
      }

      if (this.deleteContraint.getSelectionIndex() == 0) {
         statement = statement + "'0' )";
      } else if (this.deleteContraint.getSelectionIndex() == 1) {
         statement = statement + "'2' )";
      } else {
         statement = statement + "null )";
      }

      GetDataList doUpdate = new GetDataList(statement, (String[])null);
      doUpdate.updateRecord();
   }

   private void doUpdate() {
      String statement = "UPDATE DBTBL1F SET RCFRMAX = '" + this.maxInRef.getText().replaceAll("'", "''") + "', RCFRMIN = '" + this.minInRef.getText().replaceAll("'", "''") + "', RCTOMAX = '" + this.maxInForeign.getText().replaceAll("'", "''") + "', RCTOMIN = '" + this.minInForeign.getText().replaceAll("'", "''") + "', TBLREF = '" + this.foreignEntity.getText().replaceAll("'", "''") + "', ";
      if (this.updateConstraint.getSelectionIndex() == 0) {
         statement = statement + "UPD = '0', ";
      } else if (this.updateConstraint.getSelectionIndex() == 1) {
         statement = statement + "UPD = '2', ";
      } else {
         statement = statement + "UPD = null, ";
      }

      if (this.deleteContraint.getSelectionIndex() == 0) {
         statement = statement + "DEL = '0' ";
      } else if (this.deleteContraint.getSelectionIndex() == 1) {
         statement = statement + "DEL = '2' ";
      } else {
         statement = statement + "DEL = null ";
      }

      statement = statement + "WHERE FID = '" + TableList.getSelectedTableName() + "' " + "AND FKEYS = '" + this.fieldName + "'";
      GetDataList doUpdate = new GetDataList(statement, (String[])null);
      doUpdate.updateRecord();
   }
}
