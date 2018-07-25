package com.fis.profile.tablelist.dialogs;

import com.fis.profile.tablelist.TableList;
import com.fnf.profile.ide.IdePlugin;
import com.fnf.profile.monitor.connprop.util.IConnectionListViewer;
import com.fnf.profile.monitor.connprop.util.ProfileConnectionEntry;
import com.fnf.profile.monitor.connprop.util.ProfileConnectionEntryList;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ProfileEnvDialog extends Dialog {
   private Table table;
   private TableViewer tableViewer;
   private String[] columnNames = new String[]{"Environment", "Server Name", "IBS Server Name", "Port"};
   private ProfileConnectionEntryList connList = new ProfileConnectionEntryList();
   private int selection = -1;
   private TableList plugin;
   private TableItem selectedItem;

   public ProfileEnvDialog(Shell parentShell) {
      super(parentShell);
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText("Select a connection");
   }

   protected Control createDialogArea(Composite composite) {
      Composite container = new Composite(composite, 0);
      GridLayout layout = new GridLayout();
      layout.marginLeft = 5;
      layout.marginBottom = 5;
      layout.marginTop = 5;
      layout.marginRight = 5;
      layout.numColumns = 1;
      container.setLayout(layout);
      container.setLayoutData(new GridData(1808));
      Label label = new Label(container, 0);
      label.setText("Select a connection from below list:");
      GridData gridData = new GridData(1808);
      gridData.horizontalSpan = 1;
      gridData.heightHint = 20;
      label.setLayoutData(gridData);
      this.createTable(container);
      this.createTableViewer();
      this.tableViewer.setContentProvider(new ProfileEnvDialog.ConnectionPropContentProvider());
      this.tableViewer.setLabelProvider(new ConnectionPropLabelProvider());
      this.connList.initData();
      this.tableViewer.setInput(this.connList);
      String envName = TableList.getEnvName();

      for(int i = 0; i < this.tableViewer.getTable().getItemCount(); ++i) {
         TableItem item = this.tableViewer.getTable().getItem(i);
         if (item.getText(0).equals(envName)) {
            this.tableViewer.getTable().setSelection(i);
            break;
         }
      }

      return container;
   }

   private void createTableViewer() {
      this.tableViewer = new TableViewer(this.table);
      this.tableViewer.setUseHashlookup(true);
      this.tableViewer.setColumnProperties(this.columnNames);
   }

   private void createTable(Composite parent) {
      int style = 68356;
      this.table = new Table(parent, style);
      GridData gridData = new GridData(1808);
      gridData.grabExcessVerticalSpace = true;
      gridData.heightHint = 250;
      this.table.setLayoutData(gridData);
      this.table.setLinesVisible(true);
      this.table.setHeaderVisible(true);
      this.table.setVisible(true);

      for(int i = 0; i < this.columnNames.length; ++i) {
         this.createColumn(i);
      }

      this.table.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent e) {
            ProfileEnvDialog.this.selection = ProfileEnvDialog.this.table.getSelectionIndex();
            if (ProfileEnvDialog.this.selection != -1) {
               ProfileEnvDialog.this.selectedItem = ProfileEnvDialog.this.table.getItem(ProfileEnvDialog.this.selection);
            }

         }
      });
   }

   private void createColumn(int i) {
      TableColumn column = new TableColumn(this.table, 16384, i);
      column.setAlignment(16384);
      column.setText(this.columnNames[i]);
      column.setWidth(100);
   }

   protected void okPressed() {
      if (this.table.getItems().length == 0) {
         super.okPressed();
      } else {
         if (this.selectedItem == null) {
            MessageDialog.openWarning(this.getShell(), "Select a connection.", "Please select a connection before saving.!");
         } else {
            ProfileConnectionEntry entry = this.connList.getEntry(this.selectedItem.getText(0));

            try {
               TableList.setNewEnv(this.selectedItem.getText(0), this.selectedItem.getText(1), this.selectedItem.getText(3), entry.getUser_id(), entry.getPassword(), this.selectedItem.getText(2));
               ResourcesPlugin.getWorkspace().getRoot().setPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.environment"), this.selectedItem.getText(0));
               ResourcesPlugin.getWorkspace().getRoot().setPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.server"), this.selectedItem.getText(1));
               ResourcesPlugin.getWorkspace().getRoot().setPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.port"), this.selectedItem.getText(3));
               ResourcesPlugin.getWorkspace().getRoot().setPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.ibsserver"), this.selectedItem.getText(2));
               ResourcesPlugin.getWorkspace().getRoot().setPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.userID"), entry.getUser_id());
               ResourcesPlugin.getWorkspace().getRoot().setPersistentProperty(new QualifiedName(IdePlugin.PLUGINID, "com.fnf.profile.connection.password"), entry.getPassword());
               super.okPressed();
            } catch (CoreException var3) {
               var3.printStackTrace();
            }
         }

      }
   }

   class ConnectionPropContentProvider implements IStructuredContentProvider, IConnectionListViewer {
      public Object[] getElements(Object inputElement) {
         return ProfileEnvDialog.this.connList.getConnections().toArray();
      }

      public void dispose() {
         ProfileEnvDialog.this.connList.removeChangeListener(this);
      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      }

      public void addConnection(ProfileConnectionEntry connectionEntry) {
      }

      public void removeConnection(ProfileConnectionEntry connectionEntry) {
      }

      public void updateConnection(ProfileConnectionEntry connectionEntry) {
      }
   }
}
