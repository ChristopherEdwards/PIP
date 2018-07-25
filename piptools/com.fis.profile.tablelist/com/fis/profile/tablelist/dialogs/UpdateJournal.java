package com.fis.profile.tablelist.dialogs;

import com.fis.profile.tablelist.TableList;
import com.fis.profile.tablelist.details.TableDetailsView;
import com.fis.profile.tablelist.utils.GetDataList;
import com.fis.profile.tablelist.utils.SchemaInfo;
import com.fis.profile.tablelist.views.TableListContentProvider;
import com.fis.profile.tablelist.views.TableListLabelProvider;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class UpdateJournal extends Dialog {
   private String fieldName = "";
   private SchemaInfo help = new SchemaInfo("JournalInfo.xml");
   private HashMap map = new HashMap();
   private Text journalName;
   private Text journalName1;
   private Text journalTableName;
   private Text description;
   private Text excludeCol;
   private Text includeCol;
   private Text condition1;
   private Text condition2;
   private Text primaryTable;
   private Button efd;
   private Button nonEFD;
   private Button insert;
   private Button update;
   private Button delete;
   private Button financial;
   private Button online;
   private Button batch;
   private Combo journalNameCombo;
   public TableViewer viewer;
   public ArrayList updateMap = new ArrayList();
   private boolean isEdit;
   private String journalNameSelection = "";

   public UpdateJournal(Shell parentShell) {
      super(parentShell);
   }

   public UpdateJournal(Shell parentShell, ArrayList list, String fieldName) {
      super(parentShell);
      this.fieldName = fieldName;
      if (fieldName.equals("")) {
         this.isEdit = false;
      } else {
         this.isEdit = true;
      }

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

      this.updateMap.clear();
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText("Update Journal");
   }

   protected Control createDialogArea(Composite composite) {
      Composite parent = new Composite(composite, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      parent.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      gridData.heightHint = 450;
      gridData.widthHint = 600;
      composite.setLayoutData(gridData);
      TabFolder tabFolder = new TabFolder(parent, 0);
      GridData gd = new GridData(768);
      gd.widthHint = 570;
      tabFolder.setLayoutData(gd);
      Composite mainTab = this.buildHeaderTab(tabFolder);
      TabItem tabItem1 = new TabItem(tabFolder, 0);
      tabItem1.setText("Journal Header");
      tabItem1.setControl(mainTab);
      Composite colTab = this.buildColumnTab(tabFolder);
      TabItem tabItem2 = new TabItem(tabFolder, 0);
      tabItem2.setText("Journal Column Details");
      tabItem2.setControl(colTab);
      return parent;
   }

   private Composite buildColumnTab(TabFolder parent) {
      Composite colTab = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 2;
      colTab.setLayout(gl);
      GridData gd = new GridData(768);
      colTab.setLayoutData(gd);
      this.viewer = this.createTableViewer(colTab);
      return colTab;
   }

   private TableViewer createTableViewer(Composite parent) {
      String pryTable = "";
      this.primaryTable = this.createText(parent, "Primary Table", 12, "PrimaryTable");
      this.primaryTable.setText(pryTable);
      this.primaryTable.setEditable(false);
      this.journalName1 = this.createText(parent, "Journal Name", 14, "JournalName");
      this.journalName1.setText(this.fieldName);
      this.journalName1.setEditable(false);
      GridData data = new GridData(768);
      data.heightHint = 250;
      data.horizontalSpan = 2;
      Table columnTable = new Table(parent, 68354);
      columnTable.setLayoutData(data);
      columnTable.setLinesVisible(true);
      columnTable.setHeaderVisible(true);
      columnTable.setVisible(true);
      String[] colName = new String[]{"", "", "Column Name", "Column Description", "Map"};
      String[] colTag = new String[]{"", "", "ColName", "ColDesc", "Map"};

      for(int i = 0; i < colName.length; ++i) {
         TableColumn column = new TableColumn(columnTable, 16384, i);
         column.setAlignment(16384);
         column.setText(colName[i]);
         column.setToolTipText(this.help.getHelp(colTag[i]));
         if (i != 0 && i != 1) {
            if (i == 2) {
               column.setWidth(100);
            } else if (i == 3) {
               column.setWidth(150);
            } else if (i == 4) {
               column.setWidth(250);
            }
         } else {
            column.setWidth(0);
         }
      }

      TableViewer viewer = new TableViewer(columnTable);
      viewer.setUseHashlookup(true);
      viewer.setColumnProperties(colName);
      ArrayList list = this.getMappingList();
      if (list.size() == 0) {
         list.add(",,,,null");
      }

      this.primaryTable.setText(TableList.getSelectedTableName());
      this.journalName.setText(this.fieldName);
      CellEditor[] editors = new CellEditor[5];
      TextCellEditor textEditor = new TextCellEditor(columnTable);
      editors[0] = textEditor;
      textEditor = new TextCellEditor(columnTable);
      editors[1] = textEditor;
      textEditor = new TextCellEditor(columnTable);
      editors[2] = textEditor;
      textEditor = new TextCellEditor(columnTable);
      editors[3] = textEditor;
      String[] items = new String[]{"null", "table.column", "table.NEXTVAL", "OLD.COLUMN", "NEW.COLUMN", "nnn or nnn.nnn", "$$tag^pgm", "EffectiveDate", "SystemDate", "CurrentDate", "CurrentTime", "TellerLocC", "UserId", "string"};
      textEditor = new TextCellEditor(columnTable);
      editors[4] = textEditor;
      viewer.setContentProvider(new TableListContentProvider());
      viewer.setLabelProvider(new TableListLabelProvider());
      viewer.setInput(list);
      viewer.setCellEditors(editors);
      viewer.setCellModifier(new JournalCellModifier(this, items, this.fieldName, list));

      for(int i = 0; i < viewer.getTable().getItemCount(); ++i) {
         TableItem item = viewer.getTable().getItem(i);
         item.setBackground(new Color((Device)null, 255, 255, 206));
      }

      return viewer;
   }

   private ArrayList getMappingList() {
      String statement = "select PRITABLE, JRNID, COLNAM, COLDES, MAP from DBTBL9D WHERE PRITABLE = '" + TableList.getSelectedTableName() + "' " + "AND JRNID = '" + this.fieldName + "'";
      GetDataList dataList = new GetDataList(statement, (String[])null);
      return dataList.loadData_1(5);
   }

   private void getMappingListWithSelection() {
      String statement = "Select '', '', DI, DES, '' from DBTBL1D WHERE FID = '" + this.journalNameSelection + "' ";
      GetDataList dataList = new GetDataList(statement, (String[])null);
      ArrayList list = dataList.loadData_1(5);
      this.viewer.setInput(list);
      this.viewer.setCellModifier(new JournalCellModifier(this, (String[])null, this.fieldName, list));
      this.viewer.refresh();
   }

   private Composite buildHeaderTab(TabFolder parent) {
      Composite mainTab = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 2;
      mainTab.setLayout(gl);
      GridData gd = new GridData(768);
      mainTab.setLayoutData(gd);
      this.journalName = this.createText(mainTab, "Journal Name", 14, "JournalName");
      this.journalName.setText(this.fieldName);
      if (!this.isEdit) {
         this.journalName.setEditable(true);
      } else {
         this.journalName.setEditable(false);
      }

      this.journalName.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e) {
            UpdateJournal.this.journalName1.setText(UpdateJournal.this.journalName.getText().toString().toUpperCase());
         }
      });
      String label;
      String[] options;
      int i;
      if (!this.isEdit) {
         label = "SELECT FID FROM DBTBL1 WHERE FILETYP = '6'";
         GetDataList dataList = new GetDataList(label, (String[])null);
         ArrayList list = dataList.loadData_1(1);
         options = new String[list.size()];

         for(i = 0; i < list.size(); ++i) {
            options[i] = list.get(i).toString();
         }

         this.journalNameCombo = this.createCombo(mainTab, options, "Journal Table Name", "JournalTableName");
         this.journalNameCombo.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
               int index = UpdateJournal.this.journalNameCombo.getSelectionIndex();
               if (index >= 0) {
                  UpdateJournal.this.journalNameSelection = UpdateJournal.this.journalNameCombo.getItem(index).toString().toUpperCase();
                  UpdateJournal.this.getMappingListWithSelection();
               }
            }
         });
      } else {
         this.journalTableName = this.createText(mainTab, "Journal Table Name", 12, "JournalTableName");
         this.journalTableName.setEditable(false);
      }

      this.description = this.createText(mainTab, "Description", 40, "Description");
      this.excludeCol = this.createText(mainTab, "Exclude Columns", 250, "ExcludeColumns");
      this.includeCol = this.createText(mainTab, "Include Columns", 250, "IncludeColumns");
      this.condition1 = this.createText(mainTab, "Query Condition 1", 100, "Query1");
      this.condition2 = this.createText(mainTab, "Query Condition 2", 100, "Query2");
      label = "Effective-Dated Option";
      Group group = this.createGroup(mainTab, label, 40, 2);
      this.efd = this.createCheckBox(group, "EFD", "EFD");
      this.nonEFD = this.createCheckBox(group, "Non-EFD", "EFD");
      if (this.map.containsKey(label)) {
         String value = this.map.get(label).toString();
         options = value.split(",");

         for(i = 0; i < options.length; ++i) {
            if (options[i].equalsIgnoreCase("E")) {
               this.efd.setSelection(true);
            } else if (options[i].equalsIgnoreCase("N")) {
               this.nonEFD.setSelection(true);
            }
         }
      }

      label = "Transaction Type";
      Group group1 = this.createGroup(mainTab, label, 40, 2);
      this.financial = this.createCheckBox(group1, "Financial", "TransactionType");
      this.online = this.createCheckBox(group1, "Online", "TransactionType");
      this.batch = this.createCheckBox(group1, "Batch", "TransactionType");
      if (this.map.containsKey(label)) {
         String value = this.map.get(label).toString();
         String[] options = value.split(",");

         for(int i = 0; i < options.length; ++i) {
            if (options[i].equalsIgnoreCase("F")) {
               this.financial.setSelection(true);
            } else if (options[i].equalsIgnoreCase("B")) {
               this.batch.setSelection(true);
            } else if (options[i].equalsIgnoreCase("O")) {
               this.online.setSelection(true);
            }
         }
      }

      label = "Processing Mode";
      Group group2 = this.createGroup(mainTab, label, 40, 2);
      this.insert = this.createCheckBox(group2, "Insert", "ProcessingMode");
      this.update = this.createCheckBox(group2, "Update", "ProcessingMode");
      this.delete = this.createCheckBox(group2, "Delete", "ProcessingMode");
      if (this.map.containsKey(label)) {
         String value = this.map.get(label).toString();
         String[] options = value.split(",");

         for(int i = 0; i < options.length; ++i) {
            if (options[i].equalsIgnoreCase("I")) {
               this.insert.setSelection(true);
            } else if (options[i].equalsIgnoreCase("U")) {
               this.update.setSelection(true);
            } else if (options[i].equalsIgnoreCase("D")) {
               this.delete.setSelection(true);
            }
         }
      }

      return mainTab;
   }

   public Group createGroup(Composite parent, String title, int height, int groupSpan) {
      Group group = new Group(parent, 0);
      group.setText(title);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 6;
      group.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      gridData.heightHint = height;
      gridData.horizontalSpan = groupSpan;
      group.setLayoutData(gridData);
      return group;
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
      return button;
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

      return combo;
   }

   private boolean validate() {
      boolean isValid = true;
      String returnMsg = "";
      if (this.journalName.getText().equals("")) {
         returnMsg = returnMsg + "Journal Name is required field. \n";
         isValid = false;
      } else if (!this.isEdit) {
         TableDetailsView tableDetailView = (TableDetailsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.details.TableDetailsView");
         ArrayList journalList = tableDetailView.getJournalList();
         this.journalName.getText().trim().toUpperCase();
         if (journalList.contains(this.journalName.getText().trim().toUpperCase())) {
            returnMsg = returnMsg + "Journal Name already exists.\n";
            isValid = false;
         }
      }

      if (!this.isEdit && this.journalNameSelection.equals("")) {
         returnMsg = returnMsg + "Journal Table Name is required field. \n";
         isValid = false;
      }

      if (this.description.getText().equals("")) {
         returnMsg = returnMsg + "Description is required field. \n";
         isValid = false;
      }

      if (!isValid) {
         MessageDialog.openInformation((Shell)null, "Validate Error", returnMsg);
         isValid = false;
      } else {
         boolean ok = MessageDialog.openConfirm((Shell)null, "Are you sure to update?", "Changes will be updated to Profile DB. This dialog will be closed and the Journal View will be refreshed with new information.");
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
         if (!this.isEdit) {
            this.doAdd();
         } else {
            this.doUpdate();
         }

         super.okPressed();
      }
   }

   private void doAdd() {
      try {
         String statement = "INSERT INTO DBTBL9 (PRITABLE, SUBTABLE, JRNID, %LIBS, DES, EXCOLUMN, INCOLUMN, QUERY1, QUERY2,EFD, TRANTYPE, MODE) Values ('" + TableList.getSelectedTableName() + "', '" + this.journalNameSelection + "', '" + this.journalName.getText().replaceAll("'", "''").toUpperCase() + "', 'SYSDEV', '" + this.description.getText().replaceAll("'", "''") + "', '" + this.excludeCol.getText().replaceAll("'", "''") + "', '" + this.includeCol.getText().replaceAll("'", "''") + "', '" + this.condition1.getText().replaceAll("'", "''") + "', '" + this.condition2.getText().replaceAll("'", "''") + "', ";
         String efdString = "";
         if (this.efd.getSelection()) {
            efdString = "E,";
         }

         if (this.nonEFD.getSelection()) {
            efdString = efdString + "N";
         }

         statement = statement + "'" + efdString + "', ";
         String tranTypeString = "";
         if (this.financial.getSelection()) {
            tranTypeString = "F,";
         }

         if (this.batch.getSelection()) {
            tranTypeString = tranTypeString + "B,";
         }

         if (this.online.getSelection()) {
            tranTypeString = tranTypeString + "O";
         }

         if (tranTypeString.endsWith(",")) {
            tranTypeString = tranTypeString.substring(0, tranTypeString.length() - 1);
         }

         statement = statement + "'" + tranTypeString + "', ";
         String procModeString = "";
         if (this.insert.getSelection()) {
            procModeString = "I,";
         }

         if (this.update.getSelection()) {
            procModeString = procModeString + "U,";
         }

         if (this.delete.getSelection()) {
            procModeString = procModeString + "D";
         }

         if (procModeString.endsWith(",")) {
            procModeString = procModeString.substring(0, procModeString.length() - 1);
         }

         statement = statement + "'" + procModeString + "')";
         GetDataList doUpdate = new GetDataList(statement, (String[])null);
         doUpdate.updateRecord();

         for(int i = 0; i < this.updateMap.size(); ++i) {
            String colName = this.updateMap.get(i).toString().split(",")[0];
            String mapVal = this.updateMap.get(i).toString().split(",")[2];
            String colDesc = this.updateMap.get(i).toString().split(",")[1];
            if (colName.length() > 0 && colDesc.length() > 0) {
               statement = "INSERT INTO DBTBL9D (PRITABLE, JRNID, MAP, COLNAM, %LIBS) VALUES ('" + TableList.getSelectedTableName() + "', '" + this.journalName.getText().toString().replaceAll("'", "''").toUpperCase() + "', '" + mapVal.trim() + "', '" + colName.trim() + "', 'SYSDEV')";
               doUpdate = new GetDataList(statement, (String[])null);
               doUpdate.updateRecord();
            }
         }
      } catch (Exception var10) {
         MessageDialog.openWarning((Shell)null, "Information", "There is an error while creating a new Journal. Please review error log for reference.");
      }

   }

   private void doUpdate() {
      try {
         String statement = "UPDATE DBTBL9 SET DES = '" + this.description.getText().replaceAll("'", "''") + "', EXCOLUMN = '" + this.excludeCol.getText().replaceAll("'", "''") + "', INCOLUMN = '" + this.includeCol.getText().replaceAll("'", "''") + "', QUERY1 = '" + this.condition1.getText().replaceAll("'", "''") + "', QUERY2 = '" + this.condition2.getText().replaceAll("'", "''") + "', ";
         String efdString = "";
         if (this.efd.getSelection()) {
            efdString = "E,";
         }

         if (this.nonEFD.getSelection()) {
            efdString = efdString + "N";
         }

         String tranTypeString = "";
         if (this.financial.getSelection()) {
            tranTypeString = "F,";
         }

         if (this.batch.getSelection()) {
            tranTypeString = tranTypeString + "B,";
         }

         if (this.online.getSelection()) {
            tranTypeString = tranTypeString + "O";
         }

         if (tranTypeString.endsWith(",")) {
            tranTypeString = tranTypeString.substring(0, tranTypeString.length() - 1);
         }

         String procModeString = "";
         if (this.insert.getSelection()) {
            procModeString = "I,";
         }

         if (this.update.getSelection()) {
            procModeString = procModeString + "U,";
         }

         if (this.delete.getSelection()) {
            procModeString = procModeString + "D";
         }

         if (procModeString.endsWith(",")) {
            procModeString = procModeString.substring(0, procModeString.length() - 1);
         }

         statement = statement + "EFD = '" + efdString + "', TRANTYPE = '" + tranTypeString + "', MODE = '" + procModeString + "' WHERE PRITABLE = '" + TableList.getSelectedTableName() + "' " + "AND JRNID = '" + this.fieldName + "'";
         GetDataList doUpdate = new GetDataList(statement, (String[])null);
         doUpdate.updateRecord();

         for(int i = 0; i < this.updateMap.size(); ++i) {
            String colName = this.updateMap.get(i).toString().split(",")[0];
            String mapVal = this.updateMap.get(i).toString().split(",")[2];
            statement = "UPDATE DBTBL9D SET MAP = '" + mapVal.trim() + "' WHERE COLNAM = '" + colName.trim() + "' AND PRITABLE = '" + TableList.getSelectedTableName() + "' " + "AND JRNID = '" + this.fieldName + "'";
            doUpdate = new GetDataList(statement, (String[])null);
            doUpdate.updateRecord();
         }
      } catch (Exception var9) {
         MessageDialog.openWarning((Shell)null, "Information", "There is an error while updating a Journal. Please review error log for reference.");
      }

   }
}
