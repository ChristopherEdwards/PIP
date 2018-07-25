package com.fis.profile.tablelist.dialogs;

import com.fis.profile.tablelist.TableList;
import com.fis.profile.tablelist.utils.GetDataList;
import com.fis.profile.tablelist.utils.SchemaInfo;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

public class UpdateColumn extends Dialog {
   private HashMap map = new HashMap();
   private Text displaySize = null;
   private Text reportHeader = null;
   private Text subScriptLit = null;
   private Text fieldPosition = null;
   private Combo prefix = null;
   private Combo suffix = null;
   private Text subFieldPosition = null;
   private Text subFieldTag = null;
   private Text pattern = null;
   private Text desc = null;
   private Text masterAttr = null;
   private Combo domain = null;
   private Button required = null;
   private Text maxValue = null;
   private Text minValue = null;
   private Text len = null;
   private Text decimalPrecision = null;
   private Button isSerial = null;
   private Combo dataType = null;
   private Text defaultValue = null;
   private Text computerExpr = null;
   private Text lookupEntity = null;
   private String[] items = new String[]{"", "#", ",", "/", ":", "<CR>", "<FF>", "<LF>", "<TAB>", "\\", "^", "CTRL/A", "SCA delimiter", "~"};
   private String[] items1 = new String[]{"Binary", "Clock Time", "Currency", "Date", "Frequency", "Logical", "Memo", "Numeric", "Text", "Upper Case"};
   private SchemaInfo help = new SchemaInfo("AttributeInfo.xml");

   public UpdateColumn(Shell parentShell, ArrayList list) {
      super(parentShell);

      for(int i = 0; i < list.size(); ++i) {
         String value = list.get(i).toString();
         int index = value.indexOf(",");
         String propName = value.substring(0, index);
         String propValue = value.substring(index + 1, value.length());
         this.map.put(propName, propValue);
      }

   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText("Update Column");
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
      Composite characterScreenTab = this.buildCharacterScreenTab(tabFolder);
      TabItem tabItem3 = new TabItem(tabFolder, 0);
      tabItem3.setText("User Interface");
      tabItem3.setControl(characterScreenTab);
      return parent;
   }

   private Composite buildCharacterScreenTab(TabFolder parent) {
      Composite characterScreenTab = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 2;
      characterScreenTab.setLayout(gl);
      GridData gd = new GridData(768);
      characterScreenTab.setLayoutData(gd);
      Group group = this.createGroup(characterScreenTab, "Field Based Processing", 60, 2);
      this.displaySize = this.createText(group, "Field Display Size", 2, "DisplaySize");
      this.reportHeader = this.createText(group, "Report Header", 40, "ReportHeader");
      return characterScreenTab;
   }

   private Composite buildProfileDBTab(TabFolder parent) {
      Composite profileDBTab = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 2;
      profileDBTab.setLayout(gl);
      GridData gd = new GridData(768);
      profileDBTab.setLayoutData(gd);
      Group group = this.createGroup(profileDBTab, "Storage", 60, 2);
      this.subScriptLit = this.createText(group, "Subscript Key", 26, "SubScriptLiteral");
      this.subScriptLit.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e) {
            if (UpdateColumn.this.subScriptLit.getText().endsWith("*") && !UpdateColumn.this.required.getSelection()) {
               UpdateColumn.this.required.setSelection(true);
               MessageDialog.openWarning((Shell)null, "Information", TableList.getResourceString("attribute.rule15.information1"));
            }

            if (UpdateColumn.this.computerExpr.getText().length() > 0 && UpdateColumn.this.subScriptLit.getText().length() > 0) {
               UpdateColumn.this.subScriptLit.setText("");
               MessageDialog.openWarning((Shell)null, "Information", TableList.getResourceString("attribute.rule14Sup.message1"));
            }

         }
      });
      this.fieldPosition = this.createText(group, "Field Position", 2, "FieldPosition");
      this.fieldPosition.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e) {
            if (UpdateColumn.this.computerExpr.getText().length() > 0 && UpdateColumn.this.fieldPosition.getText().length() > 0) {
               UpdateColumn.this.fieldPosition.setText("");
               MessageDialog.openWarning((Shell)null, "Information", TableList.getResourceString("attribute.rule14Sup.message1"));
            }

         }
      });
      Group group2 = this.createGroup(profileDBTab, "Sub Field", 120, 2);
      this.prefix = this.createCombo(group2, this.items, "Sub-Field Delimiter (Tag Prefix)", TableList.prefixsuffix, "SubFieldPrefix");
      this.suffix = this.createCombo(group2, this.items, "Sub-Field Delimiter (Tag Suffix)", TableList.prefixsuffix, "SubFieldSuffix");
      this.subFieldPosition = this.createText(group2, "Sub-Field Position", 2, "SubFieldPosition");
      this.subFieldTag = this.createText(group2, "Sub-Field Tag", 2, "SubFieldTag");
      Group group3 = this.createGroup(profileDBTab, "Properties", 60, 2);
      this.pattern = this.createText(group3, "Pattern Match", 60, "MPatternMatch");
      this.pattern.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e) {
            String value = UpdateColumn.this.pattern.getText();
            if (value.indexOf("X?") == -1) {
               UpdateColumn.this.pattern.setText("X?" + value);
               MessageDialog.openWarning((Shell)null, "Information", TableList.getResourceString("attribute.rule8.message1"));
            }

         }
      });
      return profileDBTab;
   }

   private Composite buildMainTab(TabFolder parent) {
      Composite mainTab = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 2;
      mainTab.setLayout(gl);
      GridData gd = new GridData(768);
      mainTab.setLayoutData(gd);
      Group group = this.createGroup(mainTab, "Identity", 150, 1);
      this.desc = this.createText(group, "Description", 40, "Description");
      this.desc.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e) {
            UpdateColumn.this.reportHeader.setText(UpdateColumn.this.desc.getText());
         }
      });
      this.masterAttr = this.createText(group, "Master Dictionary Reference", 12, "MasterAttribute");
      this.required = this.createCheckBox(group, "Required Indicator", "Required");
      this.required.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            int selection = UpdateColumn.this.dataType.getSelectionIndex();
            String dataTypeValue = UpdateColumn.this.dataType.getItem(selection);
            if (UpdateColumn.this.required.getSelection()) {
               if (dataTypeValue.equalsIgnoreCase("Logical")) {
                  UpdateColumn.this.required.setSelection(false);
                  MessageDialog.openWarning((Shell)null, "Information", TableList.getResourceString("attribute.rule12.message1"));
               }
            } else if (UpdateColumn.this.subScriptLit.getText().endsWith("*")) {
               UpdateColumn.this.required.setSelection(true);
               MessageDialog.openWarning((Shell)null, "Information", TableList.getResourceString("attribute.rule12.message1"));
            }

         }
      });
      Group group2 = this.createGroup(mainTab, "Constrains", 150, 1);
      this.maxValue = this.createText(group2, "Maximum Value", 25, "MaximumValue");
      this.minValue = this.createText(group2, "Minimum Value", 25, "MinimumValue");
      this.len = this.createText(group2, "Maximum Field Length", 5, "Length");
      this.decimalPrecision = this.createText(group2, "Decimal Precision", 2, "DecimalPrecision");
      this.isSerial = this.createCheckBox(group2, "Is Serial Value", "isSerialValue");
      Group group3 = this.createGroup(mainTab, "Properties", 200, 2);
      this.dataType = this.createCombo(group3, this.items1, "Data Type", TableList.dataType, "DataType");
      this.defaultValue = this.createText(group3, "Default Value", 58, "DefaultValue");
      this.computerExpr = this.createText(group3, "Computed Expression", 255, "ComputedExpression");
      this.computerExpr.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e) {
            if ((UpdateColumn.this.subScriptLit.getText().length() > 0 || UpdateColumn.this.fieldPosition.getText().length() > 0) && UpdateColumn.this.computerExpr.getText().length() > 0) {
               UpdateColumn.this.computerExpr.setText("");
               MessageDialog.openWarning((Shell)null, "Information", TableList.getResourceString("attribute.rule14.warning1"));
            }

         }
      });
      this.lookupEntity = this.createText(group3, "Look-Up Table Name", 255, "LookupEntity");
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

   public Button createCheckBox(Composite parent, String labelString, String tag) {
      Button button = new Button(parent, 32);
      button.setText(labelString);
      button.setToolTipText(this.help.getHelp(tag));
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
      boolean isValid = true;
      String returnMsg = "";
      returnMsg = returnMsg + this.validateDataType();
      returnMsg = returnMsg + this.validateLength();
      returnMsg = returnMsg + this.validateLookUpTable();
      returnMsg = returnMsg + this.validateDisplaySize();
      returnMsg = returnMsg + this.validateIsSerialValue();
      String message = "This column can not be updated due to below problems: \n";
      message = message + returnMsg;
      message = message + "\n Please go back and modify your changes.";
      if (returnMsg.length() > 0) {
         MessageDialog.openInformation((Shell)null, "Validate Error", message);
         isValid = false;
      } else {
         boolean ok = MessageDialog.openConfirm((Shell)null, "Are you sure to update?", "Changes will be updated to Profile DB. This dialog will be closed and the Column Details View will be refreshed with new information.");
         if (!ok) {
            isValid = false;
         } else {
            isValid = true;
         }
      }

      return isValid;
   }

   private String validateDataType() {
      String returnMsg = "";
      int selection = this.dataType.getSelectionIndex();
      String value = this.dataType.getItem(selection);
      if (value.equalsIgnoreCase("Logical") && this.required.getSelection()) {
         return TableList.getResourceString("attribute.rule12.message1") + "\n";
      } else {
         if (value.equalsIgnoreCase("Date")) {
            this.len.setText("10");
         }

         return returnMsg;
      }
   }

   private String validateLength() {
      String returnMsg = "";
      String value = this.len.getText();
      if (value == null) {
         value = "";
      }

      int selection = this.dataType.getSelectionIndex();
      String dataTypeValue = this.dataType.getItem(selection);
      if (dataTypeValue.equalsIgnoreCase("Date") && !value.equals("10")) {
         returnMsg = returnMsg + TableList.getResourceString("attribute.rule5.message1") + "\n";
      }

      String displaySizeText = this.displaySize.getText();
      if (displaySizeText == null || displaySizeText.equalsIgnoreCase("null")) {
         displaySizeText = "0";
      }

      int displaySize = false;
      boolean var7 = false;

      int displaySize;
      try {
         displaySize = Integer.parseInt(displaySizeText);
      } catch (NumberFormatException var9) {
         displaySize = 0;
      }

      int intValue;
      try {
         intValue = Integer.parseInt(value);
      } catch (NumberFormatException var8) {
         intValue = 0;
      }

      if (displaySize >= intValue) {
         returnMsg = returnMsg + TableList.getResourceString("attribute.rule13Sup.message1") + "\n";
      }

      return returnMsg;
   }

   private String validateLookUpTable() {
      String returnMsg = "";
      String value = this.lookupEntity.getText();
      if (value == null) {
         value = "";
      }

      if (value.length() > 0 && value.substring(0, 1).equals("^")) {
         returnMsg = returnMsg + TableList.getResourceString("attribute.rule7.message1") + "\n";
      }

      return returnMsg;
   }

   private String validateDisplaySize() {
      String returnMsg = "";
      String value = this.displaySize.getText();
      if (value == null || value.equalsIgnoreCase("null")) {
         value = "0";
      }

      int intLen = false;
      boolean var4 = false;

      int intLen;
      try {
         intLen = Integer.parseInt(this.len.getText());
      } catch (NumberFormatException var6) {
         intLen = 0;
      }

      int intDisplaySize;
      try {
         intDisplaySize = Integer.parseInt(value);
      } catch (NumberFormatException var5) {
         intDisplaySize = 0;
      }

      if (intLen <= intDisplaySize) {
         returnMsg = TableList.getResourceString("attribute.rule13.message1") + "\n";
      }

      return returnMsg;
   }

   private String validateIsSerialValue() {
      String returnMsg = "";
      boolean value = this.isSerial.getSelection();
      int selection = this.dataType.getSelectionIndex();
      String dataTypeValue = this.dataType.getItem(selection);
      if (value && !dataTypeValue.equalsIgnoreCase("Date")) {
         returnMsg = TableList.getResourceString("attribute.rule16.message1") + "\n";
      }

      return returnMsg;
   }

   protected void okPressed() {
      if (this.validate()) {
         String req;
         if (this.required.getSelection()) {
            req = "1";
         } else {
            req = "0";
         }

         String srl;
         if (this.isSerial.getSelection()) {
            srl = "1";
         } else {
            srl = "0";
         }

         String statement = "UPDATE DBTBL1D SET DES = '" + this.desc.getText() + "', " + "REQ = '" + req + "', " + "LEN = '" + this.len.getText() + "', " + "DEC = '" + this.decimalPrecision.getText() + "', ";
         int i;
         if (this.dataType.getSelectionIndex() >= 0) {
            for(i = 0; i < TableList.dataType.length; ++i) {
               if (this.dataType.getItem(this.dataType.getSelectionIndex()).toString().equalsIgnoreCase(TableList.dataType[i][0])) {
                  statement = statement + "TYP = '" + TableList.dataType[i][1] + "', ";
                  break;
               }
            }
         }

         statement = statement + "MAX = '" + this.maxValue.getText() + "', " + "MIN = '" + this.minValue.getText() + "', " + "DFT = '" + this.defaultValue.getText() + "', " + "CMP = '" + this.computerExpr.getText() + "', " + "SRL = '" + srl + "', " + "TBL = '" + this.lookupEntity.getText() + "', " + "MDD = '" + this.masterAttr.getText() + "', " + "PTN = '" + this.pattern.getText() + "', " + "POS = '" + this.fieldPosition.getText() + "', " + "NOD = '" + this.subScriptLit.getText() + "', ";
         if (this.prefix.getSelectionIndex() > 0) {
            for(i = 0; i < TableList.prefixsuffix.length; ++i) {
               if (this.prefix.getItem(this.prefix.getSelectionIndex()).toString().equalsIgnoreCase(TableList.prefixsuffix[i][0])) {
                  statement = statement + "SFD1 = '" + TableList.prefixsuffix[i][1] + "', ";
                  break;
               }
            }
         }

         if (this.suffix.getSelectionIndex() > 0) {
            for(i = 0; i < TableList.prefixsuffix.length; ++i) {
               if (this.suffix.getItem(this.suffix.getSelectionIndex()).toString().equalsIgnoreCase(TableList.prefixsuffix[i][0])) {
                  statement = statement + "SFD2 = '" + TableList.prefixsuffix[i][1] + "', ";
                  break;
               }
            }
         }

         statement = statement + "SFP = '" + this.subFieldPosition.getText() + "', " + "SFT = '" + this.subFieldTag.getText() + "', " + "SIZ = '" + this.displaySize.getText() + "', " + "RHD = '" + this.reportHeader.getText() + "' " + "WHERE FID = '" + TableList.getSelectedTableName() + "' " + "AND DI = '" + TableList.getSelectedColumnName() + "'";
         GetDataList doUpdate = new GetDataList(statement, (String[])null);
         doUpdate.updateRecord();
         super.okPressed();
      }
   }
}
