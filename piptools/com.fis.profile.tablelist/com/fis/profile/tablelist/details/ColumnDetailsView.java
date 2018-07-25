package com.fis.profile.tablelist.details;

import com.fis.profile.tablelist.TableList;
import com.fis.profile.tablelist.dialogs.UpdateColumn;
import com.fis.profile.tablelist.utils.GetDataList;
import com.fis.profile.tablelist.views.TableListView;
import com.fnf.profile.ide.utils.DQObjectGet;
import com.fnf.profile.ide.utils.ProfileConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.eclipse.core.resources.IStorage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class ColumnDetailsView extends ViewPart implements Listener {
   private TableViewer propListTblViewer;
   private String[] columnNames1 = new String[]{"Property", "Value"};
   private ProfileConnection hostConnection = new ProfileConnection();
   private ArrayList objectProps = new ArrayList();
   private String tableName = "";
   private String selectedFieldLabel;
   private String strDoc = "";
   private Text colDoc;
   private DQObjectGet dqGetter = new DQObjectGet();
   private Composite container;
   private TableViewer sysMapTblViewer = null;
   private String[] sysMapColumnProps = new String[]{"Target Name", "Label Name", "Number of Times Read", "Number of Times Set"};
   private String[] detailColumnProps = new String[]{"Property", "Value"};
   private TabFolder tabFolder;
   private ArrayList objectSysMap = new ArrayList();
   private static String COLUMN_PROP = "Column Properties";
   private static String SYSMAP = "System Map";
   private static String COLUMN_DOC = "Column Documentation";
   IDoubleClickListener sourceListener = new IDoubleClickListener() {
      public void doubleClick(DoubleClickEvent event) {
         ISelection sysmapRecord = event.getSelection();
         if (!sysmapRecord.isEmpty()) {
            ColumnDetailsView.this.tableName = TableList.getSelectedTableName();
            String env = TableList.getEnvName();
            Connection cn = ColumnDetailsView.this.hostConnection.getConnection(env);
            String[] str = sysmapRecord.toString().substring(1, sysmapRecord.toString().length()).split(",");
            String type = new String();
            String name = new String();
            Shell shell = new Shell();

            String source;
            try {
               source = "select PROCID from DBTBL25 where PGM = '" + str[0] + "'";
               Statement stmt = cn.createStatement();
               stmt.execute(source);
               ResultSet rs = stmt.getResultSet();
               if (rs.next()) {
                  name = rs.getString(1);
                  type = "Procedure";
               } else {
                  source = "select BCHID from DBTBL33 where PGM = '" + str[0] + "'";
                  stmt = cn.createStatement();
                  stmt.execute(source);
                  rs = stmt.getResultSet();
                  if (rs.next()) {
                     name = rs.getString(1);
                     type = "Batch";
                  }
               }

               if (name.equals("")) {
                  source = "select FID from DBTBL1 where FID = 'DBLABELMAP'";
                  stmt = cn.createStatement();
                  stmt.execute(source);
                  ResultSet rs3 = stmt.getResultSet();
                  if (rs3.next()) {
                     System.out.println("Found DBLABELMAP");
                     source = "select ELEMTYPE,ELEMNAME from DBLABELMAP where TARGET = '" + str[0] + "' and LABEL = '" + str[1] + "'";
                     stmt = cn.createStatement();
                     stmt.execute(source);
                     ResultSet rs4 = stmt.getResultSet();
                     if (rs4.next()) {
                        type = rs4.getString(1);
                        name = rs4.getString(2);
                     }
                  }
               }
            } catch (SQLException var15) {
               var15.printStackTrace();
               MessageDialog.openInformation(shell, "Get Source Code", "There is no source code for your selection.");
               return;
            }

            if (name != null && name.length() > 0) {
               boolean ok = MessageDialog.openConfirm(shell, "Get Source Code", "Do you want to fetch the source code for " + type + " " + name);
               if (ok) {
                  ColumnDetailsView.this.dqGetter.setDqObjectName(name);
                  ColumnDetailsView.this.dqGetter.setDqObjectType(type);
                  source = ColumnDetailsView.this.dqGetter.getCode(cn);
                  IStorage storage = new StringStorage(source, name);
                  IStorageEditorInput input = new StringInput(storage);
                  IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                  if (page != null) {
                     try {
                        page.openEditor(input, "com.fnf.profile.ide.editors.PSLEditor");
                     } catch (PartInitException var14) {
                        var14.printStackTrace();
                     }
                  }

               }
            } else {
               MessageDialog.openInformation(shell, "Get Source Code", "There is no source code for your selection.");
            }
         }
      }
   };

   public ColumnDetailsView() {
      this.tableName = TableList.getSelectedTableName();
      this.selectedFieldLabel = TableList.getSelectedColumnName();
      this.objectProps = TableList.getColumnProp();
   }

   public void createPartControl(Composite parent) {
      int groupSpan = 1;
      Composite composite = new Composite(parent, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 1;
      composite.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      composite.setLayoutData(gridData);
      this.tabFolder = new TabFolder(composite, 0);
      GridData gd = new GridData(1808);
      this.tabFolder.setLayoutData(gd);
      Composite colTab = new Composite(this.tabFolder, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 1;
      colTab.setLayout(gl);
      this.propListTblViewer = this.createTab(colTab, groupSpan, COLUMN_PROP, this.detailColumnProps);
      this.propListTblViewer.setInput(this.objectProps);
      TabItem tabItem1 = new TabItem(this.tabFolder, 0);
      tabItem1.setText(COLUMN_PROP);
      tabItem1.setControl(colTab);
      Composite sysMapTab = new Composite(this.tabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = 1;
      sysMapTab.setLayout(gl);
      this.sysMapTblViewer = this.createTab(sysMapTab, groupSpan, SYSMAP, this.sysMapColumnProps);
      TabItem tabItem2 = new TabItem(this.tabFolder, 0);
      tabItem2.setText(SYSMAP);
      tabItem2.setControl(sysMapTab);
      Composite docTab = new Composite(this.tabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = 2;
      docTab.setLayout(gl);
      this.colDoc = new Text(docTab, 2562);
      gd = new GridData(1808);
      this.colDoc.setLayoutData(gd);
      this.colDoc.setText(this.strDoc);
      this.colDoc.setTextLimit(8000);
      this.colDoc.setEditable(true);
      Button updateButton = new Button(docTab, 8);
      updateButton.setText("Update");
      GridData buttonData = new GridData();
      buttonData.horizontalAlignment = 4;
      buttonData.verticalAlignment = 128;
      buttonData.widthHint = 85;
      updateButton.setLayoutData(buttonData);
      updateButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            Shell shell = new Shell();
            boolean ok = MessageDialog.openConfirm(shell, "Update Documentation", "Are you sure to update this documentation in Profile DB?");
            if (ok) {
               String delStatement = "Delete From DBTBL11D where DBTBL11D.DI = '" + ColumnDetailsView.this.selectedFieldLabel + "' AND DBTBL11D.FID = '" + ColumnDetailsView.this.tableName.toUpperCase() + "'";
               GetDataList dataList = new GetDataList(delStatement, (String[])null);
               dataList.updateRecord();
               char[] charlist = ColumnDetailsView.this.colDoc.getText().toCharArray();
               int SEQ = 1;
               boolean nextLine = false;
               boolean hasEmptyLine = false;

               for(int i = 0; i < charlist.length; ++i) {
                  String value = "";
                  int charCount = 0;

                  for(boolean var13 = false; charCount < 90 && i < charlist.length; ++i) {
                     int asciiValue = charlist[i];
                     if ((asciiValue == '\n' || asciiValue == '\r') && nextLine) {
                        hasEmptyLine = true;
                     }

                     if (asciiValue == '\n' || asciiValue == '\r') {
                        nextLine = true;
                        ++i;
                        break;
                     }

                     value = value + charlist[i];
                     ++charCount;
                     nextLine = false;
                  }

                  String statement;
                  GetDataList dataList2;
                  if (hasEmptyLine) {
                     ++SEQ;
                     statement = "INSERT INTO DBTBL11D (DOC, DI, FID, %LIBS, SEQ) VALUES (' ', '" + ColumnDetailsView.this.selectedFieldLabel + "', '" + ColumnDetailsView.this.tableName + "', 'SYSDEV', '" + SEQ + "')";
                     dataList2 = new GetDataList(statement, (String[])null);
                     dataList2.updateRecord();
                     nextLine = false;
                     hasEmptyLine = false;
                  } else {
                     value = value.replace("'", "''");
                     statement = "INSERT INTO DBTBL11D (DOC, DI, FID, %LIBS, SEQ) VALUES ('" + value + "', '" + ColumnDetailsView.this.selectedFieldLabel + "', '" + ColumnDetailsView.this.tableName + "', 'SYSDEV', '" + SEQ + "')";
                     dataList2 = new GetDataList(statement, (String[])null);
                     dataList2.updateRecord();
                  }

                  ++SEQ;
                  boolean var16 = false;
               }

            }
         }
      });
      TabItem tabItem3 = new TabItem(this.tabFolder, 0);
      tabItem3.setText("Column Documentation");
      tabItem3.setControl(docTab);
      this.tabFolder.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            int selection = ColumnDetailsView.this.tabFolder.getSelectionIndex();
            String header = ColumnDetailsView.this.tabFolder.getItem(selection).getText();
            ColumnDetailsView.this.ReloadData(header, false);
         }
      });
   }

   public void ReloadData(String header, boolean isRefresh) {
      String statement = "";
      if (this.tableName != "" && this.selectedFieldLabel != "") {
         GetDataList dataList;
         if (header.equals(COLUMN_PROP)) {
            statement = "Select FID, DI, DES, REQ, LEN, DEC, TYP, MAX, MIN, DFT, CMP, SRL, TBL, NULLIND, MDD, PTN, POS, NOD, SFD, SFD1, SFD2, SFP, SFT, SIZ, RHD, ITP, MDDFID, USER, LTD, '', '' from DBTBL1D WHERE FID = '" + this.tableName.toUpperCase() + "' " + "AND DI = '" + this.selectedFieldLabel + "'";
            if (this.selectedFieldLabel.equalsIgnoreCase(TableList.getLoadedColumnName()) && !isRefresh) {
               return;
            }

            TableList.setLoadedColumnName(this.selectedFieldLabel);
            dataList = new GetDataList(statement, TableList.columnProps);
            if (this.objectProps == null) {
               this.objectProps = new ArrayList();
            }

            this.objectProps.clear();
            this.objectProps = dataList.loadData();
            TableList.setColumnProp(this.objectProps);
            this.propListTblViewer.setContentProvider(new DetailsContentProvider());
            this.propListTblViewer.setLabelProvider(new DetailsLabelProvider());
            this.propListTblViewer.setInput(this.objectProps);
            this.propListTblViewer.refresh();
         } else if (header.equals(SYSMAP)) {
            if (this.selectedFieldLabel.equalsIgnoreCase(TableList.getLoadedSysMap()) && !isRefresh) {
               return;
            }

            TableList.setLoadedSysMap(this.selectedFieldLabel);
            statement = "select TARGET, LABEL, COUNTREAD, COUNTSET from SYSMAPPROPDATA  where TABLE = '" + this.tableName.toUpperCase() + "' and  COLUMN = '" + this.selectedFieldLabel + "'";
            dataList = new GetDataList(statement, (String[])null);
            if (this.objectSysMap == null) {
               this.objectSysMap = new ArrayList();
            }

            this.objectSysMap.clear();
            this.objectSysMap = dataList.loadData_1(4);
            this.sysMapTblViewer.setContentProvider(new SysMapContentProvider());
            this.sysMapTblViewer.setLabelProvider(new SysMapLabelProvider());
            this.sysMapTblViewer.setInput(this.objectSysMap);
            this.sysMapTblViewer.refresh();
         } else if (header.equals(COLUMN_DOC)) {
            if (this.selectedFieldLabel.equalsIgnoreCase(TableList.getLoadedColumnDoc()) && !isRefresh) {
               return;
            }

            TableList.setLoadedColumnDoc(this.selectedFieldLabel);
            statement = "select DOC from DBTBL11D where DBTBL11D.DI = '" + this.selectedFieldLabel + "' AND DBTBL11D.FID = '" + this.tableName.toUpperCase() + "'";
            dataList = new GetDataList(statement, (String[])null);
            new ArrayList();
            ArrayList list = dataList.loadData();
            this.strDoc = "";

            for(int i = 0; i < list.size(); ++i) {
               if (list.get(i) != null) {
                  this.strDoc = this.strDoc + list.get(i).toString() + "\n";
               }
            }

            this.colDoc.setText(this.strDoc);
         }

      } else {
         this.objectProps.clear();
         this.propListTblViewer.setContentProvider(new DetailsContentProvider());
         this.propListTblViewer.setLabelProvider(new DetailsLabelProvider());
         this.propListTblViewer.setInput(this.objectProps);
         this.propListTblViewer.refresh();
         this.objectSysMap.clear();
         this.sysMapTblViewer.setContentProvider(new SysMapContentProvider());
         this.sysMapTblViewer.setLabelProvider(new SysMapLabelProvider());
         this.sysMapTblViewer.setInput(this.objectSysMap);
         this.sysMapTblViewer.refresh();
         this.colDoc.setText("");
      }
   }

   private Composite createButtonComposite(Composite parent) {
      Composite buttonComposite = new Composite(parent, 0);
      GridLayout glayout = new GridLayout();
      glayout.marginHeight = 5;
      glayout.marginWidth = 5;
      glayout.numColumns = 1;
      GridData gdata = new GridData(2);
      buttonComposite.setLayout(glayout);
      buttonComposite.setLayoutData(gdata);
      buttonComposite.setFont(parent.getFont());
      return buttonComposite;
   }

   private TableViewer createTab(Composite parent, int groupSpan, String headerName, String[] columnNames) {
      this.container = this.createContainer(parent, groupSpan, headerName);
      Table table = this.createTable("");

      for(int i = 0; i < columnNames.length; ++i) {
         TableColumn column = new TableColumn(table, 0);
         column.setText(columnNames[i]);
         column.setWidth(200);
      }

      TableViewer viewer = new TableViewer(table);
      viewer.setContentProvider(new DetailsContentProvider());
      viewer.setLabelProvider(new DetailsLabelProvider());
      viewer.addDoubleClickListener(this.sourceListener);
      if (headerName.equals(COLUMN_PROP)) {
         Composite buttonComposite = this.createButtonComposite(this.container);
         Button editButton = new Button(buttonComposite, 8);
         editButton.setText("Edit");
         GridData buttonData = new GridData();
         buttonData.horizontalAlignment = 4;
         buttonData.verticalAlignment = 128;
         buttonData.widthHint = 85;
         editButton.setLayoutData(buttonData);
         editButton.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
               if (TableList.getSelectedColumnName().equals("")) {
                  MessageDialog.openWarning((Shell)null, "Information", "Please select a column first.");
               } else if (TableList.getSelectedColumnName().equals("")) {
                  MessageDialog.openWarning((Shell)null, "Information", "Please select a column first.");
               } else {
                  if (!ColumnDetailsView.this.canUpdateColumn()) {
                     MessageDialog.openInformation((Shell)null, "Can not edit column", "This column can not be edited or deleted. You must update this column in " + TableList.superTable);
                  } else {
                     UpdateColumn dialog = new UpdateColumn(new Shell(), ColumnDetailsView.this.objectProps);
                     int returncode = dialog.open();
                     if (returncode == 0) {
                        ColumnDetailsView.this.ReloadData(ColumnDetailsView.COLUMN_PROP, true);
                     }
                  }

               }
            }
         });
         Button deleteButton = new Button(buttonComposite, 8);
         deleteButton.setText("Delete");
         buttonData = new GridData();
         buttonData.horizontalAlignment = 4;
         buttonData.verticalAlignment = 128;
         buttonData.widthHint = 85;
         deleteButton.setLayoutData(buttonData);
         deleteButton.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
               if (TableList.getSelectedColumnName().equals("")) {
                  MessageDialog.openWarning((Shell)null, "Information", "Please select a column first.");
               } else if (TableList.getSelectedColumnName().equals("")) {
                  MessageDialog.openWarning((Shell)null, "Information", "Please select a column first.");
               } else {
                  if (!ColumnDetailsView.this.canUpdateColumn()) {
                     MessageDialog.openInformation((Shell)null, "Can not delete column", "This column can not be edited or deleted. You must update this column in " + TableList.superTable);
                  } else {
                     boolean ok = MessageDialog.openConfirm((Shell)null, "Are you sure to delete column?", "You are going to delete column " + ColumnDetailsView.this.selectedFieldLabel + ". Are you sure to continue?");
                     if (ok) {
                        String statement = "DELETE DBTBL1D WHERE FID = '" + TableList.getSelectedTableName() + "' AND DI = '" + ColumnDetailsView.this.selectedFieldLabel + "'";
                        GetDataList doUpdate = new GetDataList(statement, (String[])null);
                        doUpdate.updateRecord();
                        ColumnDetailsView.this.ReloadData(ColumnDetailsView.COLUMN_PROP, true);
                        TableListView tableView = (TableListView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.views.TableListView");
                        tableView.loadDetailData();
                     }
                  }

               }
            }
         });
      }

      return viewer;
   }

   private Table createTable(String columnName) {
      GridData data = new GridData(1808);
      Table table = new Table(this.container, 68354);
      table.setLayoutData(data);
      if (columnName.length() > 0) {
         TableColumn column1 = new TableColumn(table, 0);
         column1.setText(columnName);
         column1.setWidth(350);
      }

      table.setLinesVisible(true);
      table.setHeaderVisible(true);
      table.setVisible(true);
      return table;
   }

   private Composite createContainer(Composite parent, int groupSpan, String title) {
      Group group = new Group(parent, 0);
      group.setText(title);
      GridLayout gridLayout = new GridLayout();
      group.setLayout(gridLayout);
      GridData gridData = new GridData(1808);
      gridData.horizontalSpan = groupSpan;
      group.setLayoutData(gridData);
      this.container = new Composite(group, 0);
      gridLayout = new GridLayout();
      gridLayout.numColumns = groupSpan * 2;
      this.container.setLayout(gridLayout);
      gridData = new GridData(1808);
      this.container.setLayoutData(gridData);
      return this.container;
   }

   private boolean canUpdateColumn() {
      boolean canUpdate = true;
      if (TableList.superTable.length() > 0) {
         String statement = "SELECT DI from DBTBL1D where FID = '" + TableList.superTable + "' and DI= '" + TableList.getSelectedColumnName() + "'";
         GetDataList getData = new GetDataList(statement, (String[])null);
         ArrayList list = getData.loadData_1(1);
         return list.size() == 0;
      } else {
         return canUpdate;
      }
   }

   public void refresh() {
      try {
         this.tableName = TableList.getSelectedTableName();
         this.selectedFieldLabel = TableList.getSelectedColumnName();
         int selection = this.tabFolder.getSelectionIndex();
         String header = this.tabFolder.getItem(selection).getText();
         this.ReloadData(header, false);
      } catch (Exception var3) {
         if (this.tableName != "") {
            this.ReloadData(COLUMN_PROP, false);
         }
      }

   }

   public void setFocus() {
   }

   public void handleEvent(Event event) {
   }
}
