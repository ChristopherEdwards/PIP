package com.sanchez.profile.schema.schemaeditor.ui.formpages;

import com.fnf.profile.ide.utils.DQObjectItem;
import com.fnf.profile.ide.utils.ProfileConnection;
import com.fnf.profile.ide.utils.ProfileConnectionProp;
import com.fnf.profile.ide.utils.TableContentProvider;
import com.fnf.profile.ide.utils.TableEntry;
import com.fnf.profile.ide.utils.TableLabelProvider;
import com.sanchez.profile.schema.schemaeditor.editors.EntityEditor;
import com.sanchez.profile.schema.schemaeditor.ui.PageModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class MasterDetailedTree extends PageModel implements Listener {
   private EntityEditor eEditor = null;
   private Composite container;
   private ArrayList tableList = new ArrayList();
   private ArrayList detailList = new ArrayList();
   private TableViewer tableListTblViewer = null;
   public TableViewer detailListTblViewer = null;
   private List filteredList = new ArrayList();
   private String[] columnNames = new String[]{"Property", "Value"};
   private String[] columnProps = new String[]{"File Name", "Data Item Name", "Description", "Default Value", "Alias Name", "Data Type", "Maximum Field Length", "Maximum Value", "Minimum Value", "Required Indicator", "Computed Expression", "Null is Zero", "Decimal Precision", "Look-Up Table Name", "Pattern Match", "Internal Data Type", "Field Display Size", "UI Control", "UI Group", "Hidden Indicator", "Subscript Key", "Field Position", "Report Header", "Sub Field Definition", "Sub-Field Delimiter (Tag Prefix)", "Sub-Field Delimiter (Tag Suffix)", "Sub-Field Position", "Master Dictionary Reference", "Master Dictionary File Name", "User ID", "Last Updated"};
   public String environmentName = new String("Unknown");
   public String returnMessage = new String();
   public ArrayList objectNames = new ArrayList();
   public ArrayList objectDescriptions = new ArrayList();
   public ArrayList objectTableEntries = new ArrayList();
   public ArrayList objectDetails = new ArrayList();
   public Composite parent;
   public String tableName;
   public ProfileConnectionProp connProp;
   public ProfileConnection hostConnection;
   public String selectedFieldLabel = "";

   public MasterDetailedTree(Object obj, Composite parent, int groupSpan, String tableName, String helpFile) {
      super(helpFile);
      this.tableName = tableName;
      this.eEditor = (EntityEditor)obj;
      this.parent = parent;
      this.hostConnection = new ProfileConnection();
      this.connProp = new ProfileConnectionProp(this.eEditor.iFile);
      this.createMasterList(parent, groupSpan);
      this.createDetailList(parent, groupSpan);
   }

   private void createMasterList(final Composite parent, int groupSpan) {
      Composite composite = new Composite(parent, 0);
      GridLayout gridLayout = new GridLayout();
      composite.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      gridData.horizontalSpan = groupSpan;
      composite.setLayoutData(gridData);
      Group group = new Group(composite, 0);
      group.setText("Column Name");
      group.setFont(this.factory.getSchemaLabelFont());
      gridLayout = new GridLayout();
      group.setLayout(gridLayout);
      gridData = new GridData(768);
      gridData.horizontalSpan = groupSpan;
      gridData.widthHint = 320;
      group.setLayoutData(gridData);
      this.container = new Composite(group, 0);
      gridLayout = new GridLayout();
      gridLayout.numColumns = groupSpan * 2;
      this.container.setLayout(gridLayout);
      gridData = new GridData(768);
      this.container.setLayoutData(gridData);
      if (this.tableList == null || this.tableList.size() == 0) {
         this.tableList = this.loadData(parent, this.tableName);
      }

      if (this.tableList != null) {
         GridData data = new GridData(768);
         data.widthHint = 320;
         data.heightHint = 250;
         Table tableListTable = new Table(this.container, 68354);
         tableListTable.setLayoutData(data);
         TableColumn column1 = new TableColumn(tableListTable, 0);
         column1.setText("Name");
         column1.setWidth(100);
         TableColumn column2 = new TableColumn(tableListTable, 0);
         column2.setText("Description");
         column2.setWidth(250);
         this.tableListTblViewer = new TableViewer(tableListTable);
         this.tableListTblViewer.setContentProvider(new TableContentProvider(this.tableList));
         this.tableListTblViewer.setLabelProvider(new TableLabelProvider());
         this.tableListTblViewer.setInput((TableEntry[])this.tableList.toArray(new TableEntry[this.tableList.size()]));
         this.tableListTblViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
               MasterDetailedTree.this.detailList.clear();
               MasterDetailedTree.this.detailList = MasterDetailedTree.this.loadDetailData(parent, MasterDetailedTree.this.tableName);
               MasterDetailedTree.this.detailListTblViewer.setInput(MasterDetailedTree.this.detailList);
               MasterDetailedTree.this.detailListTblViewer.refresh();
            }
         });
      }
   }

   private void createDetailList(Composite parent, int groupSpan) {
      Composite composite = new Composite(parent, 0);
      GridLayout gridLayout = new GridLayout();
      composite.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      gridData.horizontalSpan = groupSpan;
      composite.setLayoutData(gridData);
      Group group = new Group(composite, 0);
      group.setText("Column Details");
      group.setFont(this.factory.getSchemaLabelFont());
      gridLayout = new GridLayout();
      group.setLayout(gridLayout);
      gridData = new GridData(768);
      gridData.horizontalSpan = groupSpan;
      group.setLayoutData(gridData);
      this.container = new Composite(group, 0);
      gridLayout = new GridLayout();
      gridLayout.numColumns = groupSpan * 2;
      this.container.setLayout(gridLayout);
      gridData = new GridData(768);
      this.container.setLayoutData(gridData);
      GridData data = new GridData();
      data.widthHint = 450;
      data.heightHint = 250;
      Table detailListTable = new Table(this.container, 68356);
      detailListTable.setLayoutData(data);
      detailListTable.setLinesVisible(true);
      detailListTable.setHeaderVisible(true);
      TableColumn column1 = new TableColumn(detailListTable, 16384, 0);
      column1.setText("Property");
      column1.setWidth(200);
      TableColumn column2 = new TableColumn(detailListTable, 16384, 1);
      column2.setText("Value");
      column2.setWidth(250);
      this.detailListTblViewer = new TableViewer(detailListTable);
      this.detailListTblViewer.setUseHashlookup(true);
      this.detailListTblViewer.setColumnProperties(this.columnNames);
      CellEditor[] editors = new CellEditor[this.columnNames.length];
      TextCellEditor textEditor = new TextCellEditor(detailListTable);
      editors[0] = textEditor;
      textEditor = new TextCellEditor(detailListTable);
      editors[1] = textEditor;
      this.detailListTblViewer.setContentProvider(new DetailTableContentProvider());
      this.detailListTblViewer.setLabelProvider(new DetailTableLabelProvider());
      this.detailListTblViewer.setInput(this.detailList);
      this.detailListTblViewer.setCellEditors(editors);
      this.detailListTblViewer.setCellModifier(new ColumnPropCellModifier(this));
   }

   private ArrayList loadData(Composite parent, String tableName) {
      String env = this.connProp.getEnv();
      String userID = this.connProp.getUserID();
      String pwd = this.connProp.getPwd();
      String server = this.connProp.getServer();
      String port = this.connProp.getPort();
      String ibsServer = this.connProp.getIbsServer();
      Connection cn = this.hostConnection.getConnection(env);
      if (cn == null) {
         this.hostConnection.setConnection(env, userID, pwd, server, port, ibsServer);
         cn = this.hostConnection.getConnection(env);
      }

      if (cn != null) {
         this.getList(cn, "Table", tableName);
      } else {
         MessageDialog.openWarning(parent.getShell(), "Server cannot be connected!", "Please check the setting in the project's properties page!");
      }

      return this.objectTableEntries;
   }

   private ArrayList loadDetailData(Composite parent, String tableName) {
      String env = this.connProp.getEnv();
      String userID = this.connProp.getUserID();
      String pwd = this.connProp.getPwd();
      String server = this.connProp.getServer();
      String port = this.connProp.getPort();
      String ibsServer = this.connProp.getIbsServer();
      Connection cn = this.hostConnection.getConnection(env);
      if (cn == null) {
         this.hostConnection.setConnection(env, userID, pwd, server, port, ibsServer);
         cn = this.hostConnection.getConnection(env);
      }

      if (cn != null) {
         this.getDetailList(cn, tableName);
      } else {
         MessageDialog.openWarning(parent.getShell(), "Server cannot be connected!", "Please check the setting in the project's properties page!");
      }

      return this.objectDetails;
   }

   public boolean getList(Connection con, String objectType, String tableName) {
      DQObjectItem item = new DQObjectItem(objectType);

      try {
         this.objectNames.clear();
         this.objectDescriptions.clear();
         this.objectTableEntries.clear();
         new String("");
         if (!item.itemFound) {
            this.returnMessage = objectType + " Object not  found";
            return false;
         } else if (tableName.equals("")) {
            this.returnMessage = "Table required";
            return false;
         } else {
            String selectStatement = "Select DI, DES from DBTBL1D WHERE FID = '" + tableName.toUpperCase() + "'";
            Statement stmt = con.createStatement();
            stmt.execute(selectStatement);
            ResultSet rs = stmt.getResultSet();
            boolean foundOne = false;

            while(rs.next()) {
               foundOne = true;
               this.objectNames.add(rs.getString(1));
               this.objectDescriptions.add(rs.getString(1) + " - " + rs.getString(2));
               this.objectTableEntries.add(new TableEntry(rs.getString(1), rs.getString(2)));
            }

            if (!foundOne) {
               this.returnMessage = "No match found";
               return false;
            } else {
               Statement stmt2 = con.createStatement();
               stmt2.execute("SELECT CONAM FROM CUVAR");
               ResultSet rs2 = stmt2.getResultSet();
               rs2.next();
               this.environmentName = rs2.getString(1);
               this.returnMessage = "";
               return true;
            }
         }
      } catch (Exception var11) {
         this.returnMessage = var11.getMessage();
         var11.printStackTrace();
         return false;
      }
   }

   private boolean getDetailList(Connection con, String tableName) {
      new String("");

      try {
         if (tableName.equals("")) {
            this.returnMessage = "Table required";
            return false;
         } else {
            int selection = this.tableListTblViewer.getTable().getSelectionIndex();
            if (selection != -1) {
               TableEntry entry = (TableEntry)this.tableListTblViewer.getElementAt(selection);
               this.selectedFieldLabel = entry.getName();
            }

            String selectStatement = "Select FID, DI, DES, DFT, ALIAS, TYP, LEN, MAX, MIN, REQ, CMP, NULLIND, DEC, TBL, PTN, ITP, SIZ, UICONTROL, UIGROUP, HIDDEN, NOD, POS, RHD, SFD, SFD1, SFD2, SFP, MDD, MDDFID, USER, LTD  from DBTBL1D WHERE FID = '" + tableName.toUpperCase() + "' " + "AND DI = '" + this.selectedFieldLabel + "'";
            Statement stmt = con.createStatement();
            stmt.execute(selectStatement);
            ResultSet rs = stmt.getResultSet();
            boolean foundOne = false;

            while(rs.next()) {
               foundOne = true;

               for(int i = 1; i < this.columnProps.length - 1; ++i) {
                  this.objectDetails.add(this.columnProps[i - 1] + "," + rs.getString(i));
               }
            }

            if (!foundOne) {
               this.returnMessage = "No match found";
               return false;
            } else {
               Statement stmt2 = con.createStatement();
               stmt2.execute("SELECT CONAM FROM CUVAR");
               ResultSet rs2 = stmt2.getResultSet();
               rs2.next();
               this.environmentName = rs2.getString(1);
               this.returnMessage = "";
               return true;
            }
         }
      } catch (Exception var11) {
         this.returnMessage = var11.getMessage();
         var11.printStackTrace();
         return false;
      }
   }

   public void handleEvent(Event event) {
   }
}
