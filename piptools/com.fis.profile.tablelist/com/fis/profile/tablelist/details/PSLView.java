package com.fis.profile.tablelist.details;

import com.fis.profile.tablelist.TableList;
import com.fis.profile.tablelist.dialogs.OpenPSLEditor;
import com.fis.profile.tablelist.dialogs.UpdateBatch;
import com.fis.profile.tablelist.dialogs.UpdateProcedure;
import com.fis.profile.tablelist.dialogs.UpdateTrigger;
import com.fis.profile.tablelist.utils.GetDataList;
import com.fnf.profile.ide.IdePlugin;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class PSLView extends ViewPart {
   private TableViewer procedureViewer = null;
   private ArrayList procedureList = new ArrayList();
   private TableViewer triggerViewer = null;
   private ArrayList triggerList = new ArrayList();
   private TableViewer batchViewer = null;
   private ArrayList batchList = new ArrayList();
   private static String PROCEDURES = "Procedures";
   private static String TRIGGERS = "Triggers";
   private static String BATCH = "Batch Definitions";
   private static String[] PROC_COL = new String[]{"Procedure", "Description", "Program", "User", "Time"};
   private static String[] TRIGGER_COL = new String[]{"Table", "Description", "Trigger Name", "Columns", "Before Insert", "Before Update", "Before Delete", "After Insert", "After update", "After Delete", "If Condition", "Process Mode"};
   private static String[] BATCH_COL = new String[]{"Batch Definition", "Definition", "Rountine", "Where", "Distinct", "Threads", "Message Buffer", "Buffer Size", "Thread Context", "Non-Random", "Interval-Scheduler", "Interval-Threads", "Schedule Timeout", "Thread Timeout"};
   private Text fieldLabelsFilterTxt;
   private TabFolder tabFolder;

   public PSLView() throws CoreException {
      String env = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.environment"));
      String server;
      String port;
      String ibsServer;
      String userID;
      String pwd;
      if (env == null) {
         String connString = IdePlugin.getDefault().getPreferenceStore().getString("com.fnf.profile.environment.preference");
         if (connString.endsWith(";")) {
            connString = connString.split(";")[0];
         }

         String[] connProp = connString.split(",");
         env = connProp[0];
         server = connProp[1];
         port = connProp[2];
         ibsServer = connProp[3];
         userID = connProp[4];
         pwd = connProp[5];
      } else {
         server = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.server"));
         port = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.port"));
         ibsServer = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.ibsserver"));
         userID = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.userID"));
         pwd = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.password"));
      }

      TableList.setNewEnv(env, server, port, userID, pwd, ibsServer);
      this.loadData(PROCEDURES);
      this.loadData(TRIGGERS);
      this.loadData(BATCH);
   }

   public void createPartControl(Composite parent) {
      int groupSpan = 1;
      Composite composite = new Composite(parent, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      composite.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      composite.setLayoutData(gridData);
      GridData data = new GridData();
      data.widthHint = 175;
      data.heightHint = 15;
      data.horizontalSpan = 1;
      this.fieldLabelsFilterTxt = new Text(composite, 2048);
      this.fieldLabelsFilterTxt.setText("type filter text");
      this.fieldLabelsFilterTxt.setForeground(new Color((Device)null, 150, 150, 150));
      this.fieldLabelsFilterTxt.setLayoutData(data);
      Label label = new Label(composite, 0);
      label.setText("Environment: [" + TableList.getEnvName() + "]");
      data = new GridData();
      data.heightHint = 26;
      data.widthHint = 600;
      data.horizontalSpan = 1;
      data.verticalAlignment = 1024;
      label.setLayoutData(data);
      this.tabFolder = new TabFolder(composite, 0);
      GridData gd = new GridData(1808);
      gd.horizontalSpan = 2;
      this.tabFolder.setLayoutData(gd);
      this.createTab(groupSpan, this.procedureList, PROCEDURES, PROC_COL);
      this.createTab(groupSpan, this.triggerList, TRIGGERS, TRIGGER_COL);
      this.createTab(groupSpan, this.batchList, BATCH, BATCH_COL);
      this.fieldLabelsFilterTxt.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e) {
            if (!PSLView.this.fieldLabelsFilterTxt.getText().equals("type filter text")) {
               int index = 0;
               List filteredList = new ArrayList();
               int selIndex = PSLView.this.tabFolder.getSelectionIndex();
               String tabOnFocus = PSLView.this.tabFolder.getItem(selIndex).getText();
               ArrayList list = new ArrayList();
               if (tabOnFocus.equals(PSLView.PROCEDURES)) {
                  list = PSLView.this.procedureList;
               } else if (tabOnFocus.equals(PSLView.TRIGGERS)) {
                  list = PSLView.this.triggerList;
               } else if (tabOnFocus.equals(PSLView.BATCH)) {
                  list = PSLView.this.batchList;
               }

               for(int i = 0; i < list.size(); ++i) {
                  String item = ((String)list.get(i)).toLowerCase();
                  if (item.indexOf(PSLView.this.fieldLabelsFilterTxt.getText().toLowerCase()) != -1) {
                     filteredList.add((String)list.get(i));
                     ++index;
                  }
               }

               if (tabOnFocus.equals(PSLView.PROCEDURES)) {
                  PSLView.this.procedureViewer.setInput(filteredList);
                  PSLView.this.procedureViewer.refresh();
               } else if (tabOnFocus.equals(PSLView.TRIGGERS)) {
                  PSLView.this.triggerViewer.setInput(filteredList);
                  PSLView.this.triggerViewer.refresh();
               } else if (tabOnFocus.equals(PSLView.BATCH)) {
                  PSLView.this.batchViewer.setInput(filteredList);
                  PSLView.this.batchViewer.refresh();
               }

            }
         }
      });
      this.fieldLabelsFilterTxt.addFocusListener(new FocusListener() {
         public void focusLost(FocusEvent e) {
            if (PSLView.this.fieldLabelsFilterTxt.getText().length() == 0) {
               PSLView.this.fieldLabelsFilterTxt.setText("type filter text");
            }
         }

         public void focusGained(FocusEvent e) {
            if (PSLView.this.fieldLabelsFilterTxt.getText().equals("type filter text")) {
               PSLView.this.fieldLabelsFilterTxt.setText("");
            }
         }
      });
   }

   private Composite createTab(int groupSpan, ArrayList list, String title, String[] columns) {
      Composite tab = new Composite(this.tabFolder, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 2;
      tab.setLayout(gl);
      this.createTabContents(tab, groupSpan, list, title, columns);
      TabItem tabItem1 = new TabItem(this.tabFolder, 0);
      tabItem1.setText(title);
      tabItem1.setControl(tab);
      return tab;
   }

   private void createTabContents(final Composite parent, int groupSpan, ArrayList list, String title, String[] columns) {
      final TableViewer viewer = this.createTable(columns, parent, title);
      viewer.setContentProvider(new PSLContentProvider());
      viewer.setLabelProvider(new PSLLabelProvider());
      viewer.setInput(list);
      if (title.equals(PROCEDURES)) {
         this.procedureViewer = viewer;
      } else if (title.equals(TRIGGERS)) {
         this.triggerViewer = viewer;
      } else if (title.equals(BATCH)) {
         this.batchViewer = viewer;
      }

      Composite buttonComposite = this.createButtonComposite(parent);
      final Button updateButton = this.createButton(buttonComposite, "Update");
      final Button editButton = this.createButton(buttonComposite, "Edit PSL");
      updateButton.setEnabled(false);
      editButton.setEnabled(false);
      updateButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            String obj = "";
            int selection = viewer.getTable().getSelectionIndex();
            if (selection != -1) {
               obj = viewer.getElementAt(selection).toString();
            }

            selection = PSLView.this.tabFolder.getSelectionIndex();
            String header = PSLView.this.tabFolder.getItem(selection).getText();
            PSLView.this.loadData(header);
            int returncodex = false;
            int returncode;
            if (header.equals(PSLView.PROCEDURES)) {
               UpdateProcedure dialog = new UpdateProcedure(parent.getShell(), obj);
               returncode = dialog.open();
               if (returncode == 0) {
                  PSLView.this.loadData(header);
                  PSLView.this.procedureViewer.setInput(PSLView.this.procedureList);
                  PSLView.this.procedureViewer.refresh();
                  updateButton.setEnabled(false);
               }
            } else if (header.equals(PSLView.TRIGGERS)) {
               UpdateTrigger dialogx = new UpdateTrigger(parent.getShell(), obj);
               returncode = dialogx.open();
               if (returncode == 0) {
                  PSLView.this.loadData(header);
                  PSLView.this.triggerViewer.setInput(PSLView.this.triggerList);
                  PSLView.this.triggerViewer.refresh();
                  updateButton.setEnabled(false);
               }
            } else if (header.equals(PSLView.BATCH)) {
               UpdateBatch dialogxx = new UpdateBatch(parent.getShell(), obj);
               returncode = dialogxx.open();
               if (returncode == 0) {
                  PSLView.this.loadData(header);
                  PSLView.this.batchViewer.setInput(PSLView.this.batchList);
                  PSLView.this.batchViewer.refresh();
                  updateButton.setEnabled(false);
               }
            }

         }
      });
      editButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            String obj = "";
            int selection = viewer.getTable().getSelectionIndex();
            if (selection != -1) {
               obj = viewer.getElementAt(selection).toString();
            }

            selection = PSLView.this.tabFolder.getSelectionIndex();
            String header = PSLView.this.tabFolder.getItem(selection).getText();
            OpenPSLEditor dialog;
            if (header.equals(PSLView.PROCEDURES)) {
               dialog = new OpenPSLEditor(parent.getShell(), "", obj.split(",")[0], "Procedure");
               dialog.open();
            } else if (header.equals(PSLView.TRIGGERS)) {
               dialog = new OpenPSLEditor(parent.getShell(), obj.split(",")[0], obj.split(",")[2], "Trigger");
               dialog.open();
            } else if (header.equals(PSLView.BATCH)) {
               dialog = new OpenPSLEditor(parent.getShell(), "", obj.split(",")[0], "Batch");
               dialog.open();
            }

         }
      });
      viewer.addSelectionChangedListener(new ISelectionChangedListener() {
         public void selectionChanged(SelectionChangedEvent event) {
            updateButton.setEnabled(true);
            editButton.setEnabled(true);
         }
      });
   }

   private Button createButton(Composite parent, String text) {
      Button button = new Button(parent, 8);
      button.setText(text);
      GridData buttonData = new GridData();
      buttonData.horizontalAlignment = 4;
      buttonData.verticalAlignment = 128;
      buttonData.widthHint = 85;
      button.setLayoutData(buttonData);
      return button;
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

   private TableViewer createTable(String[] columns, Composite parent, String title) {
      GridData data = new GridData(1808);
      Table columnTable = new Table(parent, 68354);
      columnTable.setLayoutData(data);
      this.createColumns(columns, columnTable, title);
      columnTable.setLinesVisible(true);
      columnTable.setHeaderVisible(true);
      columnTable.setVisible(true);
      TableViewer viewer = new TableViewer(columnTable);
      return viewer;
   }

   private void createColumns(String[] columns, Table table, String title) {
      for(int i = 0; i < columns.length; ++i) {
         TableColumn column1 = new TableColumn(table, 0);
         column1.setText(columns[i]);
         if (!columns[i].equals("Description") && !columns[i].equals("Definition")) {
            if (!columns[i].equals("Time") && columns[i].indexOf("Before") == -1 && columns[i].indexOf("After") == -1) {
               column1.setWidth(150);
            } else {
               column1.setWidth(150);
            }
         } else {
            column1.setWidth(250);
         }

         if (title.equals(TRIGGERS)) {
            if (i > 9) {
               column1.setWidth(0);
            }
         } else if (title.equals(BATCH) && i > 2) {
            column1.setWidth(0);
         }
      }

   }

   private void loadData(String header) {
      String statement = "";
      GetDataList dataList;
      if (header.equals(PROCEDURES)) {
         statement = "Select PROCID, DES, PGM, USER, TIME from DBTBL25";
         dataList = new GetDataList(statement, (String[])null);
         this.procedureList = dataList.loadData_1(5);
      } else if (header.equals(TRIGGERS)) {
         statement = "Select TABLE, DES, TRGID, COLUMNS, ACTBI, ACTBU, ACTBD, ACTAI, ACTAU, ACTAD, IFCOND, PROCMODE from DBTBL7";
         dataList = new GetDataList(statement, (String[])null);
         this.triggerList = dataList.loadData_1(12);
      } else if (header.equals(BATCH)) {
         statement = "Select BCHID, DES, PFID, WHERE, DISTINCT, THREADS, MSGBUFS, MAXSIZE, THRLVAR, NONRAND, SCHRCNT, THRRCNT, SCHTIMR, THRTIMR from DBTBL33";
         dataList = new GetDataList(statement, (String[])null);
         this.batchList = dataList.loadData_1(14);
      }

   }

   public void setFocus() {
   }
}
