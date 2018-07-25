package com.fis.profile.tablelist.views;

import com.fis.profile.tablelist.TableList;
import com.fis.profile.tablelist.details.ColumnDetailsView;
import com.fis.profile.tablelist.details.TableDetailsView;
import com.fis.profile.tablelist.dialogs.ProfileEnvDialog;
import com.fis.profile.tablelist.utils.GetDataList;
import com.fnf.profile.ide.IdePlugin;
import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.wizards.NewAttributeWizard;
import com.sanchez.profile.schema.schemaeditor.wizards.NewEntityWizard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class TableListView extends ViewPart implements Listener, IWorkbenchWindowActionDelegate {
   private TableViewer viewer;
   private TableViewer detailViewer;
   private Table tableListTable = null;
   private Table tableDetailTable = null;
   private String[] columnNames = new String[]{"Table Name", "Description"};
   private String[] detailColumnNames = new String[]{"Column Name", "Description"};
   private ArrayList objectColumnDetails = new ArrayList();
   private Text fieldLabelsFilterTxt;
   private Label label;
   private ArrayList objectTableEntries = new ArrayList();
   private ArrayList objectColumnEntries = new ArrayList();
   private Button changeEnvButton;
   private Button openViewButton;
   private Button openTableViewButton;
   private Button newTable;
   private Button newColumn;
   private String tableName = "";
   private String columnName = "";
   private String envString = "";
   private ArrayList objectTableDetails = new ArrayList();

   public void createPartControl(Composite parent) {
      Composite composite = new Composite(parent, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 3;
      gridLayout.marginWidth = 4;
      gridLayout.horizontalSpacing = 5;
      composite.setLayout(gridLayout);
      GridData data = new GridData();
      data.widthHint = 175;
      data.heightHint = 15;
      data.horizontalSpan = 1;
      this.fieldLabelsFilterTxt = new Text(composite, 2048);
      this.fieldLabelsFilterTxt.setText("type filter text");
      this.fieldLabelsFilterTxt.setForeground(new Color((Device)null, 150, 150, 150));
      this.fieldLabelsFilterTxt.setLayoutData(data);
      this.fieldLabelsFilterTxt.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e) {
            if (!TableListView.this.fieldLabelsFilterTxt.getText().equals("type filter text")) {
               int index = 0;
               List filteredList = new ArrayList();

               for(int i = 0; i < TableListView.this.objectTableEntries.size(); ++i) {
                  String item = ((String)TableListView.this.objectTableEntries.get(i)).toLowerCase();
                  if (item.indexOf(TableListView.this.fieldLabelsFilterTxt.getText().toLowerCase()) != -1) {
                     filteredList.add((String)TableListView.this.objectTableEntries.get(i));
                     ++index;
                  }
               }

               TableListView.this.viewer.setInput(filteredList);
               TableListView.this.viewer.refresh();
            }
         }
      });
      this.fieldLabelsFilterTxt.addFocusListener(new FocusListener() {
         public void focusLost(FocusEvent e) {
            if (TableListView.this.fieldLabelsFilterTxt.getText().length() == 0) {
               TableListView.this.fieldLabelsFilterTxt.setText("type filter text");
            }
         }

         public void focusGained(FocusEvent e) {
            if (TableListView.this.fieldLabelsFilterTxt.getText().equals("type filter text")) {
               TableListView.this.fieldLabelsFilterTxt.setText("");
            }
         }
      });
      this.label = new Label(composite, 0);
      this.label.setText("");
      data = new GridData();
      data.heightHint = 26;
      data.widthHint = 600;
      data.horizontalSpan = 2;
      data.verticalAlignment = 1024;
      this.label.setLayoutData(data);

      try {
         this.loadData(composite);
      } catch (CoreException var6) {
         var6.printStackTrace();
      }

      this.tableListTable = this.createTable(composite);
      this.createColumn(this.columnNames, this.tableListTable);
      this.viewer = new TableViewer(this.tableListTable);
      this.viewer.setUseHashlookup(true);
      this.viewer.setColumnProperties(this.columnNames);
      this.viewer.setContentProvider(new TableListContentProvider());
      this.viewer.setLabelProvider(new TableListLabelProvider());
      this.viewer.setInput(this.objectTableEntries);
      this.tableDetailTable = this.createTable(composite);
      this.createColumn(this.detailColumnNames, this.tableDetailTable);
      this.detailViewer = new TableViewer(this.tableDetailTable);
      this.detailViewer.setUseHashlookup(true);
      this.detailViewer.setColumnProperties(this.detailColumnNames);
      this.detailViewer.setContentProvider(new TableListContentProvider());
      this.detailViewer.setLabelProvider(new TableListLabelProvider());
      this.detailViewer.setInput(this.objectColumnEntries);
      this.detailViewer.addSelectionChangedListener(new ISelectionChangedListener() {
         public void selectionChanged(SelectionChangedEvent event) {
            int selection = TableListView.this.detailViewer.getTable().getSelectionIndex();
            if (selection != -1) {
               String obj = TableListView.this.detailViewer.getElementAt(selection).toString();
               TableListView.this.columnName = obj.split(",")[0];
               TableList.setSelectedColumnName(TableListView.this.columnName);
               TableListView.this.label.setText(TableListView.this.envString + "  Table: [" + TableListView.this.tableName + "]" + "  Column: [" + TableListView.this.columnName + "]");
               TableListView.this.openViewButton.setEnabled(true);
               TableDetailsView tView = (TableDetailsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.details.TableDetailsView");
               ColumnDetailsView view = (ColumnDetailsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.details.columnDetailsView");
               if (tView != null) {
                  tView.refresh();
               }

               if (view != null) {
                  view.refresh();
               }
            }

         }
      });
      this.viewer.addSelectionChangedListener(new ISelectionChangedListener() {
         public void selectionChanged(SelectionChangedEvent event) {
            int selection = TableListView.this.viewer.getTable().getSelectionIndex();
            if (selection != -1) {
               String obj = TableListView.this.viewer.getElementAt(selection).toString();
               TableListView.this.tableName = obj.split(",")[0];
               TableList.setSelectedTableName(TableListView.this.tableName);
               TableList.setSelectedColumnName("");
               TableListView.this.label.setText(TableListView.this.envString + "  Table: [" + TableListView.this.tableName + "]");
               if (TableListView.this.objectColumnEntries == null) {
                  TableListView.this.objectColumnEntries = new ArrayList();
               }

               TableListView.this.objectColumnEntries.clear();
               TableListView.this.objectColumnEntries = TableListView.this.loadDetailData();
               TableListView.this.openTableViewButton.setEnabled(true);
               TableDetailsView tView = (TableDetailsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.details.TableDetailsView");
               ColumnDetailsView view = (ColumnDetailsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.details.columnDetailsView");
               if (tView != null) {
                  tView.refresh();
               }

               if (view != null) {
                  view.refresh();
               }

               TableListView.this.newColumn.setEnabled(true);
            }

         }
      });
      this.createButtons(composite);
   }

   private void createButtons(final Composite composite) {
      Composite buttonComposite = new Composite(composite, 0);
      GridLayout glayout = new GridLayout();
      glayout.marginHeight = 5;
      glayout.marginWidth = 5;
      glayout.numColumns = 1;
      GridData gdata = new GridData(2);
      buttonComposite.setLayout(glayout);
      buttonComposite.setLayoutData(gdata);
      buttonComposite.setFont(composite.getFont());
      GridData buttonData = new GridData();
      buttonData.horizontalAlignment = 4;
      buttonData.widthHint = 85;
      this.changeEnvButton = new Button(buttonComposite, 16777224);
      this.changeEnvButton.setText("Connection");
      this.changeEnvButton.setLayoutData(buttonData);
      this.changeEnvButton.setToolTipText("Change Connection");
      this.changeEnvButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            ProfileEnvDialog dialog = new ProfileEnvDialog(new Shell());
            int returncode = dialog.open();
            if (returncode == 0) {
               TableListView.this.reloadList();
               TableListView.this.viewer.setContentProvider(new TableListContentProvider());
               TableListView.this.viewer.setLabelProvider(new TableListLabelProvider());
               TableListView.this.viewer.setInput(TableListView.this.objectTableEntries);
               TableListView.this.objectColumnEntries.clear();
               TableListView.this.detailViewer.setContentProvider(new TableListContentProvider());
               TableListView.this.detailViewer.setLabelProvider(new TableListLabelProvider());
               TableListView.this.detailViewer.setInput(TableListView.this.objectColumnEntries);
               TableListView.this.detailViewer.setSelection((ISelection)null);
               TableList.setSelectedTableName("");
               TableList.setSelectedColumnName("");
               TableDetailsView tView = (TableDetailsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.details.TableDetailsView");
               ColumnDetailsView view = (ColumnDetailsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.details.columnDetailsView");
               if (tView != null) {
                  tView.refresh();
               }

               if (view != null) {
                  view.refresh();
               }

               TableListView.this.newColumn.setEnabled(false);
            }

         }
      });
      this.newTable = new Button(buttonComposite, 16777224);
      this.newTable.setText("New Table");
      this.newTable.setLayoutData(buttonData);
      this.newTable.setToolTipText("Create a new table");
      this.newTable.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            NewEntityWizard wizard = new NewEntityWizard();
            wizard.init(PlatformUI.getWorkbench(), (IStructuredSelection)null);
            WizardDialog dialog = new WizardDialog(composite.getShell(), wizard);
            int result = dialog.open();
            if (result == 0) {
               HashMap map = SchemaEditorPlugin.map;
               String newTableName = map.get("tableName").toString().replaceAll("'", "''").toUpperCase();
               String[] keys = map.get("primaryKeys").toString().split(",");
               String statement = "INSERT INTO DBTBL1 (FID, FDOC, DES, ACCKEYS, %LIBS, FILETYP, RECTYP,GLOBAL, UDFILE, NETLOC) VALUES ('" + newTableName + "', '" + newTableName + "', '" + map.get("description").toString().replaceAll("'", "''") + "', '" + map.get("primaryKeys").toString().replaceAll("'", "''") + "', 'SYSDEV' , ";

               int index;
               for(index = 0; index < TableList.fileType.length; ++index) {
                  if (map.get("entityType").toString().equalsIgnoreCase(TableList.fileType[index][0])) {
                     statement = statement + "'" + TableList.fileType[index][1] + "', ";
                     break;
                  }
               }

               for(index = 0; index < TableList.recordType.length; ++index) {
                  if (map.get("recordType").toString().equalsIgnoreCase(TableList.recordType[index][0])) {
                     statement = statement + "'" + TableList.recordType[index][1] + "', ";
                     break;
                  }
               }

               statement = statement + "'" + map.get("global").toString().replaceAll("'", "''") + "', '" + map.get("filer").toString().replaceAll("'", "''") + "', ";

               for(index = 0; index < TableList.networkLoc.length; ++index) {
                  if (map.get("networkLoc").toString().equalsIgnoreCase(TableList.networkLoc[index][0])) {
                     statement = statement + "'" + TableList.networkLoc[index][1] + "')";
                     break;
                  }
               }

               GetDataList doUpdate = new GetDataList(statement, (String[])null);
               doUpdate.updateRecord();
               TableList.setSelectedTableName(newTableName);
               ArrayList list = SchemaEditorPlugin.list;

               try {
                  int indexx;
                  String position;
                  for(indexx = 0; indexx < list.size(); ++indexx) {
                     String name = list.get(indexx).toString().split(",")[0];
                     position = list.get(indexx).toString().split(",")[1];
                     String type = list.get(indexx).toString().split(",")[2];
                     String required = list.get(indexx).toString().split(",")[3];
                     String len = list.get(indexx).toString().split(",")[4];
                     String decimal = list.get(indexx).toString().split(",")[5];
                     String desc = list.get(indexx).toString().split(",")[6];
                     String newColumnName = name.trim().replaceAll("'", "''").toUpperCase();
                     String updateDataType = "";
                     statement = "INSERT INTO DBTBL1D (FID, DI, DES, TYP, REQ, LEN, DEC, POS, %LIBS, NOD) VALUES ('" + map.get("tableName").toString().replaceAll("'", "''").toUpperCase() + "', '" + newColumnName + "', '" + desc.replaceAll("'", "''") + "', ";

                     for(int typeIndex = 0; typeIndex < TableList.dataType.length; ++typeIndex) {
                        if (type.equalsIgnoreCase(TableList.dataType[typeIndex][0])) {
                           statement = statement + "'" + TableList.dataType[typeIndex][1] + "', ";
                           updateDataType = TableList.dataType[typeIndex][1];
                           break;
                        }
                     }

                     if (required.equalsIgnoreCase("true")) {
                        statement = statement + "'1', ";
                     } else {
                        statement = statement + "'0', ";
                     }

                     statement = statement + "'" + len + "', '" + decimal + "', '" + position + "', 'SYSDEV', '" + keys[keys.length - 1] + "')";
                     boolean isPrim = false;

                     for(int primayIndex = 0; primayIndex < keys.length; ++primayIndex) {
                        if (keys[primayIndex].trim().equalsIgnoreCase(name.trim().replaceAll("'", "''"))) {
                           isPrim = true;
                           break;
                        }
                     }

                     if (!isPrim) {
                        doUpdate = new GetDataList(statement, (String[])null);
                        doUpdate.updateRecord();
                     } else {
                        statement = "UPDATE DBTBL1D SET DES = '" + desc.replaceAll("'", "''") + "', " + "TYP = '" + updateDataType + "', LEN = '" + len + "', DEC = '" + decimal + "' WHERE FID = '" + newTableName + "' AND DI = '" + newColumnName + "'";
                        doUpdate = new GetDataList(statement, (String[])null);
                        doUpdate.updateRecord();
                     }
                  }

                  TableListView.this.reloadList();
                  TableListView.this.viewer.setInput(TableListView.this.objectTableEntries);
                  indexx = 0;

                  for(int i = 0; i < TableListView.this.objectTableEntries.size(); ++i) {
                     position = TableListView.this.objectTableEntries.get(i).toString().split(",")[0];
                     if (position.equalsIgnoreCase(map.get("tableName").toString().replaceAll("'", "''"))) {
                        indexx = i;
                        break;
                     }
                  }

                  TableListView.this.viewer.getTable().setSelection(indexx);
                  TableListView.this.viewer.refresh();
                  TableListView.this.tableName = map.get("tableName").toString().replaceAll("'", "''").toUpperCase();
                  TableListView.this.objectColumnEntries.clear();
                  TableListView.this.objectColumnEntries = TableListView.this.loadDetailData();
                  TableListView.this.detailViewer.getTable().setSelection(0);
               } catch (Exception var23) {
                  MessageDialog.openWarning((Shell)null, "Information", "There is an error while creating a new table. Please review error log for reference.");
               }
            }

         }
      });
      this.newTable.setEnabled(true);
      this.newColumn = new Button(buttonComposite, 16777224);
      this.newColumn.setText("New Column");
      this.newColumn.setLayoutData(buttonData);
      this.newColumn.setToolTipText("Create a new column");
      this.newColumn.setEnabled(false);
      this.newColumn.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            SchemaEditorPlugin.list.clear();
            NewAttributeWizard wizard = new NewAttributeWizard(TableList.getTableDetails());
            wizard.init(PlatformUI.getWorkbench(), (IStructuredSelection)null);
            WizardDialog dialog = new WizardDialog(composite.getShell(), wizard);
            int result = dialog.open();
            if (result == 0) {
               String name = "";
               ArrayList list = SchemaEditorPlugin.list;

               try {
                  int index;
                  String val;
                  for(index = 0; index < list.size(); ++index) {
                     name = list.get(index).toString().split(",")[0];
                     String position = list.get(index).toString().split(",")[1];
                     val = list.get(index).toString().split(",")[2];
                     String required = list.get(index).toString().split(",")[3];
                     String len = list.get(index).toString().split(",")[4];
                     String decimal = list.get(index).toString().split(",")[5];
                     String desc = list.get(index).toString().split(",")[6];
                     String statement = "INSERT INTO DBTBL1D (FID, DI, DES, TYP, REQ, LEN, DEC, POS, %LIBS) VALUES ('" + TableList.getSelectedTableName() + "', '" + name.replaceAll("'", "''").toUpperCase() + "', '" + desc.replaceAll("'", "''") + "', ";

                     for(int typeIndex = 0; typeIndex < TableList.dataType.length; ++typeIndex) {
                        if (val.equalsIgnoreCase(TableList.dataType[typeIndex][0])) {
                           statement = statement + "'" + TableList.dataType[typeIndex][1] + "', ";
                           break;
                        }
                     }

                     if (required.equalsIgnoreCase("true")) {
                        statement = statement + "'1', ";
                     } else {
                        statement = statement + "'0', ";
                     }

                     statement = statement + "'" + len + "', '" + decimal + "', '" + position + "', 'SYSDEV')";
                     GetDataList doUpdate = new GetDataList(statement, (String[])null);
                     doUpdate.updateRecord();
                  }

                  TableListView.this.objectColumnEntries.clear();
                  TableListView.this.objectColumnEntries = TableListView.this.loadDetailData();
                  index = 0;

                  for(int i = 0; i < TableListView.this.objectColumnEntries.size(); ++i) {
                     val = TableListView.this.objectColumnEntries.get(i).toString().split(",")[0];
                     if (val.equalsIgnoreCase(name.replaceAll("'", "''"))) {
                        index = i;
                        break;
                     }
                  }

                  TableListView.this.detailViewer.getTable().setSelection(index);
                  TableListView.this.detailViewer.refresh();
               } catch (Exception var16) {
                  MessageDialog.openWarning((Shell)null, "Information", "There is an error while creating a new column. Please review error log for reference.");
               }
            }

         }
      });
      this.openTableViewButton = new Button(buttonComposite, 16777224);
      this.openTableViewButton.setText("Table Details");
      this.openTableViewButton.setLayoutData(buttonData);
      this.openTableViewButton.setToolTipText("Open Table Details View");
      this.openTableViewButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            if (TableListView.this.objectTableDetails == null) {
               TableListView.this.objectTableDetails = new ArrayList();
            }

            TableListView.this.objectTableDetails.clear();
            TableListView.this.objectTableDetails = TableListView.this.loadTableDetailsData();
            TableList.setTableDetails(TableListView.this.objectTableDetails);
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

            try {
               page.showView("com.fis.profile.tablelist.details.TableDetailsView");
            } catch (PartInitException var4) {
               var4.printStackTrace();
            }

         }
      });
      this.openTableViewButton.setEnabled(false);
      this.openViewButton = new Button(buttonComposite, 16777224);
      this.openViewButton.setText("Column Details");
      this.openViewButton.setLayoutData(buttonData);
      this.openViewButton.setToolTipText("Open Column Details View");
      this.openViewButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            if (TableListView.this.objectColumnDetails == null) {
               TableListView.this.objectColumnDetails = new ArrayList();
            }

            TableListView.this.objectColumnDetails.clear();
            TableListView.this.objectColumnDetails = TableListView.this.loadColumnDetailsData();
            TableList.setColumnProp(TableListView.this.objectColumnDetails);
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

            try {
               page.showView("com.fis.profile.tablelist.details.columnDetailsView");
            } catch (PartInitException var4) {
               var4.printStackTrace();
            }

         }
      });
      this.openViewButton.setEnabled(false);
   }

   private Table createTable(Composite parent) {
      int style = 68356;
      Table table = new Table(parent, style);
      GridData gridData = new GridData(1808);
      gridData.grabExcessVerticalSpace = true;
      table.setLayoutData(gridData);
      table.setLinesVisible(true);
      table.setHeaderVisible(true);
      table.setVisible(true);
      return table;
   }

   public ArrayList loadDetailData() {
      ArrayList list = new ArrayList();
      if (this.tableName == "") {
         return list;
      } else {
         String statement = "Select DI, DES from DBTBL1D WHERE FID = '" + this.tableName.toUpperCase() + "'";
         GetDataList dataList = new GetDataList(statement, (String[])null);
         list = dataList.loadData_1(2);
         this.detailViewer.setInput(list);
         this.detailViewer.refresh();
         this.detailViewer.getTable().select(0);
         SchemaEditorPlugin.colList = list;
         return list;
      }
   }

   public void refresh(String refreshTable) {
      int selIndex = this.viewer.getTable().getSelectionIndex();
      String selVal = "";
      if (this.objectTableEntries.get(selIndex) != null) {
         selVal = this.objectTableEntries.get(selIndex).toString().split(",")[0];
      }

      if (!refreshTable.equalsIgnoreCase(selVal.trim())) {
         this.reloadList();
         this.viewer.setContentProvider(new TableListContentProvider());
         this.viewer.setLabelProvider(new TableListLabelProvider());
         this.viewer.setInput(this.objectTableEntries);
         int index = -1;

         for(int i = 0; i < this.objectTableEntries.size(); ++i) {
            if (this.objectTableEntries.get(i).toString().indexOf(refreshTable + ",") != -1) {
               index = i;
               break;
            }
         }

         this.viewer.getTable().setSelection(index);
         this.tableName = refreshTable;
         this.loadDetailData();
      }
   }

   private void createColumn(String[] colName, Table table) {
      for(int i = 0; i < colName.length; ++i) {
         TableColumn column = new TableColumn(table, 16384, i);
         column.setAlignment(16384);
         column.setText(colName[i]);
         if (i == 0) {
            column.setWidth(125);
         } else {
            column.setWidth(300);
         }
      }

   }

   private void loadData(Composite composite) throws CoreException {
      String server = null;
      String port = null;
      String ibsServer = null;
      String userID = null;
      String pwd = null;
      String env = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.environment"));
      if (env == null) {
         String connString = IdePlugin.getDefault().getPreferenceStore().getString("com.fnf.profile.environment.preference");
         if (connString.endsWith(";")) {
            connString = connString.split(";")[0];
         }

         String[] connProp = connString.split(",");
         if (connProp.length > 5) {
            env = connProp[0];
            server = connProp[1];
            port = connProp[2];
            ibsServer = connProp[3];
            userID = connProp[4];
            pwd = connProp[5];
         }
      } else {
         server = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.server"));
         port = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.port"));
         ibsServer = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.ibsserver"));
         userID = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.userID"));
         pwd = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.password"));
      }

      this.envString = "Environment: [" + env + "]";
      this.label.setText(this.envString);
      TableList.setNewEnv(env, server, port, userID, pwd, ibsServer);
      if (env == null) {
         MessageDialog.openWarning(composite.getShell(), "Server cannot be connected!", "Please set up and select your connection preference on Preference page under Window menu!");
      }

      this.reloadList();
   }

   public void reloadList() {
      if (this.objectTableEntries == null) {
         this.objectTableEntries = new ArrayList();
      }

      this.objectTableEntries.clear();
      SchemaEditorPlugin.tableList.clear();
      String statement = "Select FID, DES from DBTBL1";
      GetDataList dataList = new GetDataList(statement, (String[])null);
      this.objectTableEntries = dataList.loadData_1(2);
      SchemaEditorPlugin.tableList = this.objectTableEntries;
      this.fieldLabelsFilterTxt.setText("type filter text");
      this.envString = "Environment: [" + TableList.env_name + "]";
      this.label.setText(this.envString);
   }

   private ArrayList loadColumnDetailsData() {
      ArrayList list = new ArrayList();
      if (this.tableName != "" && this.columnName != "") {
         String statement = "Select FID, DI, DES, REQ, LEN, DEC, TYP, MAX, MIN, DFT, CMP, SRL, TBL, NULLIND, MDD, PTN, POS, NOD, SFD, SFD1, SFD2, SFP, SFT, SIZ, RHD, ITP, ALIAS, MDDFID, USER, LTD, '', ''  from DBTBL1D WHERE FID = '" + this.tableName.toUpperCase() + "' " + "AND DI = '" + this.columnName + "'";
         GetDataList dataList = new GetDataList(statement, TableList.columnProps);
         list = dataList.loadData();
         return list;
      } else {
         return list;
      }
   }

   private ArrayList loadTableDetailsData() {
      ArrayList list = new ArrayList();
      if (this.tableName == "") {
         return list;
      } else {
         String statement = "select FID, DES, ACCKEYS, DBASE, DEL, DFLAG, DFTDES, DFTDES1, DFTHDR, DFTORD, EXIST, EXTENDLENGTH, FDOC, FILETYP, FPN, FSN, GLOBAL, GLREF, LISTDFT, LISTREQ, LOG, LTD, MPLCTDD, NETLOC, PARFID, PREDAEN, PTRTIM, PTRTIMU, PTRTLD, PTRTLDU, PTRUSER, PTRUSERU, QID1, RECTYP, RFLAG, SCREEN, SYSSN, UDACC, UDFILE, UDPOST, UDPRE, USER from DBTBL1 where FID = '" + this.tableName.toUpperCase() + "'";
         GetDataList dataList = new GetDataList(statement, TableList.tableColumnProps);
         list = dataList.loadData();
         return list;
      }
   }

   public ArrayList getTableList() {
      ArrayList list = new ArrayList();

      for(int i = 0; i < this.objectTableEntries.size(); ++i) {
         list.add(this.objectTableEntries.get(i).toString().split(",")[0]);
      }

      return list;
   }

   public void setFocus() {
      this.viewer.getControl().setFocus();
   }

   public void handleEvent(Event event) {
   }

   public void init(IWorkbenchWindow window) {
   }

   public void run(IAction action) {
   }

   public void selectionChanged(IAction action, ISelection selection) {
   }
}
