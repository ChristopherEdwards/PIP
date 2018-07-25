package com.sanchez.profile.schema.schemaeditor.ui.formpages;

import com.fnf.profile.ide.utils.ProfileConnection;
import com.fnf.profile.ide.utils.ProfileConnectionProp;
import com.sanchez.profile.schema.schemaeditor.editors.EntityEditor;
import com.sanchez.profile.schema.schemaeditor.ui.PageModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class TableKeysTree extends PageModel {
   private EntityEditor eEditor = null;
   private Composite container;
   private TableViewer TblViewer = null;
   private TableViewer Detail1Viewer = null;
   private String environmentName = new String("Unknown");
   private String returnMessage = new String();
   private ArrayList object = new ArrayList();
   private ArrayList objectList = new ArrayList();
   private Composite parent;
   private String tableName;
   private String groupName;
   private String headerName;
   private String[] columnProps;
   private ProfileConnectionProp connProp;
   private ProfileConnection hostConnection;

   public TableKeysTree(Object obj, Composite parent, int groupSpan, String tableName, String groupName, String headerName, String[] columnProps, String helpFile) {
      super(helpFile);
      this.tableName = tableName;
      this.eEditor = (EntityEditor)obj;
      this.parent = parent;
      this.groupName = groupName;
      this.headerName = headerName;
      this.columnProps = columnProps;
      this.hostConnection = new ProfileConnection();
      this.connProp = new ProfileConnectionProp(this.eEditor.iFile);
      this.loadData(parent);
      if (this.object != null) {
         this.createList(parent, groupSpan);
      }
   }

   private void createList(Composite parent, int groupSpan) {
      this.container = this.createContainer(groupSpan, this.groupName);
      Table indexListTable = this.createTable(this.headerName);
      this.TblViewer = new TableViewer(indexListTable);
      this.TblViewer.setContentProvider(new DetailTableContentProvider());
      this.TblViewer.setLabelProvider(new DetailTableLabelProvider());
      this.TblViewer.setInput(this.object);
      this.Detail1Viewer = this.createColumnDetailGroup();
      this.TblViewer.addSelectionChangedListener(new ISelectionChangedListener() {
         public void selectionChanged(SelectionChangedEvent event) {
            String selectedFieldLabel = "";
            String statement = "";
            int selection = TableKeysTree.this.TblViewer.getTable().getSelectionIndex();
            if (selection != -1) {
               Object obj = TableKeysTree.this.TblViewer.getElementAt(selection);
               if (obj != null) {
                  selectedFieldLabel = obj.toString().split(",")[0];
                  if (TableKeysTree.this.headerName.equals("Index Name")) {
                     statement = "Select IDXDESC, NULLFLG, ORDERBY, PARFID, QRY1, QRY2, SAVFLG, UPCASE, GLOBAL  from DBTBL8 WHERE FID = '" + TableKeysTree.this.tableName.toUpperCase() + "' " + "AND INDEXNM = '" + selectedFieldLabel + "'";
                  } else if (TableKeysTree.this.headerName.equals("Foreign Keys")) {
                     statement = "Select PKEYS, RCFRMAX, RCFRMIN, RCTOMAX, RCTOMIN, TBLREF, UPD, DEL  from DBTBL1F WHERE FID = '" + TableKeysTree.this.tableName.toUpperCase() + "' " + "AND FKEYS = '" + selectedFieldLabel + "'";
                  } else if (TableKeysTree.this.headerName.equals("Journal Name")) {
                     statement = "Select DES, SUBTABLE, EFD, TRANTYPE, MODE, EXCOLUMN, INCOLUMN, SEQ, QUERY1, QUERY2, PARFID  from DBTBL9 WHERE PRITABLE = '" + TableKeysTree.this.tableName.toUpperCase() + "' " + "AND JRNID = '" + selectedFieldLabel + "'";
                  }
               }
            }

            if (selectedFieldLabel != "") {
               TableKeysTree.this.loadColumnDetail(TableKeysTree.this.object, statement, TableKeysTree.this.columnProps);
               if (TableKeysTree.this.object.size() > 0) {
                  TableKeysTree.this.Detail1Viewer.setInput(TableKeysTree.this.object);
                  TableKeysTree.this.Detail1Viewer.refresh();
               }
            }

         }
      });
   }

   private Table createTable(String columnName) {
      GridData data = new GridData(768);
      data.widthHint = 200;
      data.heightHint = 250;
      Table table = new Table(this.container, 68354);
      table.setLayoutData(data);
      TableColumn column1 = new TableColumn(table, 0);
      column1.setText(columnName);
      column1.setWidth(350);
      table.setLinesVisible(true);
      table.setHeaderVisible(true);
      table.setVisible(true);
      return table;
   }

   private Composite createContainer(int groupSpan, String title) {
      Composite composite = new Composite(this.parent, 0);
      GridLayout gridLayout = new GridLayout();
      composite.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      gridData.verticalAlignment = 1024;
      gridData.horizontalSpan = groupSpan;
      composite.setLayoutData(gridData);
      Group group = new Group(composite, 0);
      group.setText(title);
      group.setFont(this.factory.getSchemaLabelFont());
      gridLayout = new GridLayout();
      group.setLayout(gridLayout);
      gridData = new GridData(768);
      gridData.horizontalSpan = groupSpan;
      gridData.widthHint = 200;
      gridData.heightHint = 320;
      group.setLayoutData(gridData);
      this.container = new Composite(group, 0);
      gridLayout = new GridLayout();
      gridLayout.numColumns = groupSpan * 2;
      this.container.setLayout(gridLayout);
      gridData = new GridData(768);
      this.container.setLayoutData(gridData);
      return this.container;
   }

   private TableViewer createColumnDetailGroup() {
      GridData data = new GridData(768);
      data.widthHint = 500;
      data.heightHint = 250;
      Table columnTable = new Table(this.container, 68354);
      columnTable.setLayoutData(data);
      TableColumn column1 = new TableColumn(columnTable, 0);
      column1.setText("Property");
      column1.setWidth(200);
      TableColumn column2 = new TableColumn(columnTable, 0);
      column2.setText("value");
      column2.setWidth(250);
      columnTable.setLinesVisible(true);
      columnTable.setHeaderVisible(true);
      columnTable.setVisible(true);
      TableViewer viewer = new TableViewer(columnTable);
      viewer.setContentProvider(new ColumnDetailsContentProvider());
      viewer.setLabelProvider(new ColumnDetailsLabelProvider());
      return viewer;
   }

   private void loadData(Composite parent) {
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
         this.getList(cn);
      } else {
         MessageDialog.openWarning(parent.getShell(), "Server cannot be connected!", "Please check the setting in the project's properties page!");
      }

   }

   private void loadColumnDetail(ArrayList list, String statement, String[] columnProp) {
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
         try {
            list.clear();
            Statement stmt = cn.createStatement();
            stmt.execute(statement);
            ResultSet rs = stmt.getResultSet();

            while(rs.next()) {
               for(int i = 1; i < columnProp.length - 1; ++i) {
                  list.add(columnProp[i - 1] + "," + rs.getString(i));
               }
            }
         } catch (Exception var14) {
            this.returnMessage = var14.getMessage();
            var14.printStackTrace();
         }
      } else {
         MessageDialog.openWarning(this.parent.getShell(), "Server cannot be connected!", "Please check the setting in the project's properties page!");
      }

   }

   public boolean getList(Connection con) {
      try {
         this.object.clear();
         String selectStatement = new String("");
         if (this.tableName.equals("")) {
            this.returnMessage = "Table required";
            return false;
         } else {
            if (this.headerName.equals("Index Name")) {
               selectStatement = "select INDEXNM from DBTBL8 where FID = '" + this.tableName.toUpperCase() + "'";
            } else if (this.headerName.equals("Foreign Keys")) {
               selectStatement = "select FKEYS from DBTBL1F where FID = '" + this.tableName.toUpperCase() + "'";
            } else if (this.headerName.equals("Journal Name")) {
               selectStatement = "select JRNID from DBTBL9 where PRITABLE = '" + this.tableName.toUpperCase() + "'";
            }

            Statement stmt = con.createStatement();
            stmt.execute(selectStatement);
            ResultSet rs = stmt.getResultSet();

            while(rs.next()) {
               this.object.add(rs.getString(1));
            }

            Statement stmt3 = con.createStatement();
            stmt3.execute("SELECT CONAM FROM CUVAR");
            ResultSet rs3 = stmt3.getResultSet();
            rs3.next();
            this.environmentName = rs3.getString(1);
            this.returnMessage = "";
            return true;
         }
      } catch (Exception var7) {
         this.returnMessage = var7.getMessage();
         var7.printStackTrace();
         return false;
      }
   }
}
