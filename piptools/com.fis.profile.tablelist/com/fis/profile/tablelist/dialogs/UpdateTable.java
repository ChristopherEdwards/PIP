package com.fis.profile.tablelist.dialogs;

import com.fis.profile.tablelist.TableList;
import com.fis.profile.tablelist.utils.GetDataList;
import com.fis.profile.tablelist.utils.SchemaInfo;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class UpdateTable extends Dialog {
   private HashMap map = new HashMap();
   private Text desc;
   private Text superTypeFileName;
   private Text primaryKeys;
   private Text globalName;
   private Text recordExists;
   private Text query;
   private Text filer;
   private Text protectionFileName;
   private Text createDate;
   private Text createTime;
   private Text createUser;
   private Text modifyDate;
   private Text modifyTime;
   private Text modifyUser;
   private Combo systemName;
   private Combo fileType;
   private Combo asciiDelimiter;
   private Combo recordType;
   private Combo networkLocation;
   private Button enableLogging;
   private Text preProcessor;
   private Text fileShortName;
   private Text lookupLine;
   private Text lookupLine2;
   private Text defaultHeading;
   private Text screen;
   private Button delFlag;
   private Button fMaintenanceFlag;
   private Button descendingOrder;
   private SchemaInfo help = new SchemaInfo("EntityInfo.xml");

   public UpdateTable(Shell parentShell, ArrayList list) {
      super(parentShell);

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
      shell.setText("Update Table");
   }

   protected Control createDialogArea(Composite composite) {
      Composite parent = new Composite(composite, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 1;
      parent.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      gridData.heightHint = 500;
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
      Composite profileDBTab = this.buildProfileDBTab(tabFolder);
      TabItem tabItem2 = new TabItem(tabFolder, 0);
      tabItem2.setText("Profile DB");
      tabItem2.setControl(profileDBTab);
      Composite auditTab = this.buildAuditTab(tabFolder);
      TabItem tabItem3 = new TabItem(tabFolder, 0);
      tabItem3.setText("Audit");
      tabItem3.setControl(auditTab);
      Composite characterScreenTab = this.buildCharacterScreenTab(tabFolder);
      TabItem tabItem4 = new TabItem(tabFolder, 0);
      tabItem4.setText("User Interface");
      tabItem4.setControl(characterScreenTab);
      return parent;
   }

   private Composite buildAuditTab(TabFolder parent) {
      Composite auditTab = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 2;
      auditTab.setLayout(gl);
      GridData gd = new GridData(768);
      auditTab.setLayoutData(gd);
      Group group = this.createGroup(auditTab, "Audit", 200, 1);
      this.createDate = this.createText(group, "Data Item Name (Date Created)", 12, "CreateDate");
      this.createTime = this.createText(group, "Data Item Name (Time Created)", 12, "CreateTime");
      this.createUser = this.createText(group, "Data Item Name (User Created)", 12, "CreateUser");
      this.modifyDate = this.createText(group, "Data Item Name (Date Last Updated)", 12, "ModifyDate");
      this.modifyTime = this.createText(group, "Data Item Name (Time Last Updated)", 12, "ModifyTime");
      this.modifyUser = this.createText(group, "Date Item Name (User Last Updated)", 12, "ModifyUser");
      this.enableLogging = this.createCheckBox(group, "Enable Automatic Logging", "EnableLogging");
      return auditTab;
   }

   private Composite buildCharacterScreenTab(TabFolder parent) {
      Composite characterScreenTab = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 1;
      characterScreenTab.setLayout(gl);
      GridData gd = new GridData(768);
      characterScreenTab.setLayoutData(gd);
      Group group = this.createGroup(characterScreenTab, "Screen Logic", 100, 2);
      this.preProcessor = this.createText(group, "Data Entry Pre-Processor", 255, "DataEntryPreProcessor");
      this.delFlag = this.createCheckBox(group, "Deletion Restriction Flag", "DeletionRestricted");
      this.fMaintenanceFlag = this.createCheckBox(group, "File Maintenance Restriction Flag", "RestrictEntityMaintenance");
      this.descendingOrder = this.createCheckBox(group, "Descending Order", "DescendingOrder");
      Group group2 = this.createGroup(characterScreenTab, "Processing", 150, 2);
      this.fileShortName = this.createText(group2, "File Short Name", 40, "FileShortName");
      this.lookupLine = this.createText(group2, "Look-up Table List", 200, "LookupLineOne");
      this.lookupLine2 = this.createText(group2, "Look-up Table List (Line 2)", 200, "LookupLineTwo");
      this.defaultHeading = this.createText(group2, "Default Heading", 78, "DefaultHeading");
      this.screen = this.createText(group2, "File Maintenance Data Entry Screen IDs", 12, "DataEntryScreen");
      return characterScreenTab;
   }

   private Composite buildProfileDBTab(TabFolder parent) {
      Composite profileDBTab = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 1;
      profileDBTab.setLayout(gl);
      GridData gd = new GridData(768);
      profileDBTab.setLayoutData(gd);
      Group group = this.createGroup(profileDBTab, "Storage", 150, 2);
      this.globalName = this.createText(group, "Global Name", 40, "GlobalName");
      String[] items = new String[]{"", "#", ",", "/", ":", "<CR>", "<FF>", "<LF>", "<TAB>", "\\", "^", "CTRL/A", "SCA Delimiter", "~"};
      this.asciiDelimiter = this.createCombo(group, items, "ASCII Delimiter", TableList.prefixsuffix, "ASCIIDelimiter");
      this.recordExists = this.createText(group, "Record Existed Indicator (node number)", 6, "RecordExistsNode");
      String[] items1 = new String[]{"", "Mixed Type", "Node [Segmented]", "None", "Unsegmented"};
      this.recordType = this.createCombo(group, items1, "Record Type", TableList.recordType, "RecordType");
      String[] items2 = new String[]{"", "Both", "Client Only", "Server Only"};
      this.networkLocation = this.createCombo(group, items2, "Network Location", TableList.networkLoc, "NetworkLocation");
      Group group2 = this.createGroup(profileDBTab, "Processing", 120, 2);
      this.query = this.createText(group2, "Query", 100, "Query");
      this.filer = this.createText(group2, "Record Filer Routine", 8, "Filer");
      this.protectionFileName = this.createText(group2, "Data Item Protection Filename", 4, "AttributeProtectionRoutine");
      return profileDBTab;
   }

   private Composite buildMainTab(TabFolder parent) {
      Composite mainTab = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 1;
      mainTab.setLayout(gl);
      GridData gd = new GridData(768);
      mainTab.setLayoutData(gd);
      Group group = this.createGroup(mainTab, "Identity", 150, 1);
      this.desc = this.createText(group, "Description", 40, "Description");
      String[] items = new String[]{"", "ACCOUNTS PAYABLE SYSTEM", "DATA QWIK", "GENERAL LEDGER SYSTEM", "PROFILE", "SCA UTILITIES"};
      this.systemName = this.createCombo(group, items, "System Name", TableList.systemName, "SystemName");
      this.superTypeFileName = this.createText(group, "Supertype File Name", 12, "SuperEntity");
      this.superTypeFileName.setEditable(false);
      Group group2 = this.createGroup(mainTab, "Properties", 150, 1);
      String[] items1 = new String[]{"Domain File", "Dummy File", "Entity File", "GUI Client", "Index File", "Journal File", "Relationship File"};
      this.fileType = this.createCombo(group2, items1, "File Type", TableList.fileType, "FileType");
      this.primaryKeys = this.createText(group2, "Primary Keys", 100, "PrimaryKeys");
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

   public Group createGroup(Composite parent, String title, int height, int groupSpan) {
      Group group = new Group(parent, 0);
      group.setText(title);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      group.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      gridData.heightHint = height;
      gridData.horizontalSpan = groupSpan;
      group.setLayoutData(gridData);
      return group;
   }

   public Combo createCombo(Composite parent, String[] items, String labelString, String[][] list, String tag) {
      Label label = new Label(parent, 0);
      GridData gd = new GridData();
      label.setLayoutData(gd);
      label.setText(labelString + ":");
      label.setToolTipText(this.help.getHelp(tag));
      Combo combo = new Combo(parent, 0);

      for(int i = 0; i < items.length; ++i) {
         combo.add(items[i]);
      }

      String value = this.map.get(labelString).toString();
      if (value == null || value.equalsIgnoreCase("null")) {
         value = "0";
      }

      int index = 0;

      for(int i = 0; i < items.length; ++i) {
         if (value.equalsIgnoreCase(items[i])) {
            index = i;
            break;
         }
      }

      combo.select(index);
      return combo;
   }

   public Button createCheckBox(Composite parent, String labelString, String tag) {
      Button button = new Button(parent, 32);
      button.setText(labelString);
      button.setToolTipText(this.help.getHelp(tag));
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

   private boolean validate() {
      boolean isValid = true;
      String returnMsg = "";
      returnMsg = returnMsg + this.validateFileShortName();
      returnMsg = returnMsg + this.validateFileType();
      returnMsg = returnMsg + this.validateGlobalName();
      returnMsg = returnMsg + this.validatePrimaryKey();
      returnMsg = returnMsg + this.validateNetworkLoc();
      returnMsg = returnMsg + this.validateRecordType();
      returnMsg = returnMsg + this.validateQuery();
      returnMsg = returnMsg + this.validateProtectionFileName();
      returnMsg = returnMsg + this.validateFiler();
      returnMsg = returnMsg + this.validateEnableLogging();
      String message = "This table can not be updated due to below problems: \n";
      message = message + returnMsg;
      message = message + "\n Please go back and modify your changes.";
      if (returnMsg.length() > 0) {
         MessageDialog.openInformation((Shell)null, "Validate Error", message);
         isValid = false;
      } else {
         boolean ok = MessageDialog.openConfirm((Shell)null, "Are you sure to update?", "Changes will be updated to Profile DB. This dialog will be closed and the Table Details View will be refreshed with new information.");
         if (!ok) {
            isValid = false;
         } else {
            isValid = true;
         }
      }

      return isValid;
   }

   private String validateFileShortName() {
      String returnMsg = "";
      if (this.fileShortName.getText().length() == 0) {
         returnMsg = TableList.getResourceString("entity.rule2.message1") + "\n";
      }

      return returnMsg;
   }

   private String validateFileType() {
      String returnMsg = "";
      int index = this.fileType.getSelectionIndex();
      if (this.fileType.getItem(index).toString().equalsIgnoreCase("Dummy File")) {
         if (this.filer.getText().length() > 0 || this.protectionFileName.getText().length() > 0) {
            returnMsg = TableList.getResourceString("entity.rule3.information1") + "\n";
         }
      } else if (this.globalName.getText().length() == 0) {
         returnMsg = TableList.getResourceString("entity.rule3.message1") + "\n";
      }

      return returnMsg;
   }

   private String validateGlobalName() {
      String returnMsg = "";
      return returnMsg;
   }

   private String validatePrimaryKey() {
      String returnMsg = "";
      if (this.primaryKeys.getText().indexOf("$J") != -1) {
         returnMsg = TableList.getResourceString("entity.rule5.message1") + "\n";
      } else if (this.primaryKeys.getText().endsWith("\"")) {
         returnMsg = returnMsg + TableList.getResourceString("entity.rule5.message2") + "\n";
      }

      return returnMsg;
   }

   private String validateNetworkLoc() {
      String returnMsg = "";
      int index = this.fileType.getSelectionIndex();
      if ((this.globalName.getText().equals("CTBL") || this.globalName.getText().equals("UTBL") || this.globalName.getText().equals("DBTBL1") || this.globalName.getText().equals("DBTBL1D") || this.globalName.getText().equals("TRN") || this.globalName.getText().equals("DBCTL") || this.globalName.getText().equals("STBL") || this.globalName.getText().equals("SCATBL")) && !this.fileType.getItem(index).toString().equalsIgnoreCase("Dummy File") && this.networkLocation.getSelectionIndex() != 1) {
         returnMsg = TableList.getResourceString("entity.rule6.message1") + "\n";
      }

      return returnMsg;
   }

   private String validateRecordType() {
      String returnMsg = "";
      int index = this.recordType.getSelectionIndex();
      if (this.globalName.getText().length() > 0 && this.recordType.getItem(index).equalsIgnoreCase("None")) {
         returnMsg = TableList.getResourceString("entity.rule8.message1") + "\n";
      }

      return returnMsg;
   }

   private String validateQuery() {
      String returnMsg = "";
      if (this.query.getText().indexOf("*") != -1 || this.query.getText().indexOf("<<") != -1 || this.query.getText().indexOf(">>") != -1) {
         returnMsg = TableList.getResourceString("entity.rule10.message1") + "\n";
      }

      return returnMsg;
   }

   private String validateProtectionFileName() {
      String returnMsg = "";
      int index = this.fileType.getSelectionIndex();
      if (this.fileType.getItem(index).toString().equalsIgnoreCase("Dummy File") && this.protectionFileName.getText().length() > 0) {
         returnMsg = "Data Item Protection Filename " + TableList.getResourceString("entity.rule10.message1") + "\n";
      }

      return returnMsg;
   }

   private String validateFiler() {
      String returnMsg = "";
      int index = this.fileType.getSelectionIndex();
      if (this.fileType.getItem(index).toString().equalsIgnoreCase("Dummy File") && this.filer.getText().length() > 0) {
         returnMsg = "Record Filer Routine " + TableList.getResourceString("entity.rule10.message1") + "\n";
      }

      return returnMsg;
   }

   private String validateEnableLogging() {
      String returnMsg = "";
      if ((this.globalName.getText().equals("CTBL") || this.globalName.getText().equals("UTBL") || this.globalName.getText().equals("DBTBL1") || this.globalName.getText().equals("DBTBL1D") || this.globalName.getText().equals("TRN") || this.globalName.getText().equals("DBCTL") || this.globalName.getText().equals("STBL") || this.globalName.getText().equals("SCATBL")) && this.networkLocation.getSelectionIndex() != 1 && !this.enableLogging.getSelection()) {
         returnMsg = TableList.getResourceString("entity.rule6.message1");
      }

      return returnMsg;
   }

   protected void okPressed() {
      if (this.validate()) {
         String statement = "UPDATE DBTBL1 SET DES = '" + this.desc.getText() + "', " + "ACCKEYS = '" + this.primaryKeys.getText() + "', ";
         int i;
         if (this.asciiDelimiter.getSelectionIndex() > 0) {
            for(i = 0; i < TableList.prefixsuffix.length; ++i) {
               if (this.asciiDelimiter.getItem(this.asciiDelimiter.getSelectionIndex()).toString().equalsIgnoreCase(TableList.prefixsuffix[i][0])) {
                  statement = statement + "DEL = '" + TableList.prefixsuffix[i][1] + "', ";
                  break;
               }
            }
         }

         if (this.delFlag.getSelection()) {
            statement = statement + "DFLAG = '1', ";
         } else {
            statement = statement + "DFLAG = '0', ";
         }

         statement = statement + "DFTDES = '" + this.lookupLine.getText() + "', " + "DFTDES1 = '" + this.lookupLine2.getText() + "', " + "DFTHDR = '" + this.defaultHeading.getText() + "', ";
         if (this.descendingOrder.getSelection()) {
            statement = statement + "DFTORD = '1', ";
         } else {
            statement = statement + "DFTORD = '0', ";
         }

         statement = statement + "EXIST = '" + this.recordExists.getText() + "', ";
         if (this.fileType.getSelectionIndex() > 0) {
            for(i = 0; i < TableList.fileType.length; ++i) {
               if (this.fileType.getItem(this.fileType.getSelectionIndex()).toString().equalsIgnoreCase(TableList.fileType[i][0])) {
                  statement = statement + "FILETYP = '" + TableList.fileType[i][1] + "', ";
                  break;
               }
            }
         }

         statement = statement + "FPN = '" + this.protectionFileName.getText() + "', " + "FSN = '" + this.fileShortName.getText() + "', " + "GLOBAL = '" + this.globalName.getText() + "', ";
         if (this.enableLogging.getSelection()) {
            statement = statement + "LOG = '1', ";
         } else {
            statement = statement + "LOG = '0', ";
         }

         if (this.networkLocation.getSelectionIndex() > 0) {
            for(i = 0; i < TableList.networkLoc.length; ++i) {
               if (this.networkLocation.getItem(this.networkLocation.getSelectionIndex()).toString().equalsIgnoreCase(TableList.networkLoc[i][0])) {
                  statement = statement + "NETLOC = '" + TableList.networkLoc[i][1] + "', ";
                  break;
               }
            }
         }

         statement = statement + "PREDAEN = '" + this.preProcessor.getText() + "', " + "PTRTIM = '" + this.createTime.getText() + "', " + "PTRTIMU = '" + this.modifyTime.getText() + "', " + "PTRTLD = '" + this.createDate.getText() + "', " + "PTRTLDU = '" + this.modifyDate.getText() + "', " + "PTRUSER = '" + this.createUser.getText() + "', " + "PTRUSERU = '" + this.modifyUser.getText() + "', " + "QID1 = '" + this.query.getText() + "', ";
         if (this.recordType.getSelectionIndex() > 0) {
            for(i = 0; i < TableList.recordType.length; ++i) {
               if (this.recordType.getItem(this.recordType.getSelectionIndex()).toString().equalsIgnoreCase(TableList.recordType[i][0])) {
                  statement = statement + "RECTYP = '" + TableList.recordType[i][1] + "', ";
                  break;
               }
            }
         }

         if (this.fMaintenanceFlag.getSelection()) {
            statement = statement + "RFLAG = '1', ";
         } else {
            statement = statement + "RFLAG = '0', ";
         }

         statement = statement + "SCREEN = '" + this.screen.getText() + "', ";
         if (this.systemName.getSelectionIndex() > 0) {
            for(i = 0; i < TableList.systemName.length; ++i) {
               if (this.systemName.getItem(this.systemName.getSelectionIndex()).toString().equalsIgnoreCase(TableList.systemName[i][0])) {
                  statement = statement + "SYSSN = '" + TableList.systemName[i][1] + "', ";
                  break;
               }
            }
         }

         statement = statement + "UDFILE = '" + this.filer.getText() + "' " + "WHERE FID = '" + TableList.getSelectedTableName() + "'";
         GetDataList doUpdate = new GetDataList(statement, (String[])null);
         doUpdate.updateRecord();
         super.okPressed();
      }
   }
}
