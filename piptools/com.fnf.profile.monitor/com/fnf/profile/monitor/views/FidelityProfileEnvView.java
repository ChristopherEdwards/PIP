package com.fnf.profile.monitor.views;

import com.fnf.profile.ide.IdePlugin;
import com.fnf.profile.ide.dialogs.ProfileEnvDialog;
import com.fnf.profile.ide.utils.ProfileEnvEntry;
import com.fnf.profile.monitor.connprop.util.IConnectionListViewer;
import com.fnf.profile.monitor.connprop.util.ProfileConnectionEntry;
import com.fnf.profile.monitor.connprop.util.ProfileConnectionEntryList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IProgressService;

public class FidelityProfileEnvView extends ViewPart {
   private Table table = null;
   private TableViewer tableViewer = null;
   private Button addButton;
   private Button deleteButton;
   private Button updateButton;
   private Button refreshButton;
   private Button importButton;
   private Button exportButton;
   private String[] columnNames = new String[]{"Environment", "Server Name", "IBS Server Name", "Port", "Ping Status", "MTM Status", "IBS Server Status", "Posting Date", "Error Count"};
   private ProfileConnectionEntryList connList = new ProfileConnectionEntryList();
   private int selection = -1;
   Hashtable ht;

   public void createPartControl(Composite parent) {
      Composite composite = new Composite(parent, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      gridLayout.marginWidth = 4;
      gridLayout.horizontalSpacing = 5;
      composite.setLayout(gridLayout);
      this.createTable(composite);
      this.createTableViewer();
      this.tableViewer.setContentProvider(new FidelityProfileEnvView.ConnectionPropContentProvider());
      this.tableViewer.setLabelProvider(new ConnectionPropLabelProvider());
      this.tableViewer.setSorter(new ViewerSorter() {
         public int compare(Viewer lViewer, Object e1, Object e2) {
            ProfileConnectionEntry lhs = (ProfileConnectionEntry)e1;
            ProfileConnectionEntry rhs = (ProfileConnectionEntry)e2;
            String comp1 = lhs.getHost_name();
            String comp2 = rhs.getHost_name();
            int res = comp1.toLowerCase().compareTo(comp2.toLowerCase());
            return res;
         }
      });
      this.connList.initData();
      this.tableViewer.setInput(this.connList);
      this.createButton(composite);
   }

   private void createTable(Composite parent) {
      int style = 68356;
      this.table = new Table(parent, style);
      GridData gridData = new GridData(1808);
      gridData.grabExcessVerticalSpace = true;
      this.table.setLayoutData(gridData);
      this.table.setLinesVisible(true);
      this.table.setHeaderVisible(true);
      this.table.setVisible(true);

      for(int i = 0; i < this.columnNames.length; ++i) {
         this.createColumn(i);
      }

      this.table.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent e) {
            FidelityProfileEnvView.this.selection = FidelityProfileEnvView.this.table.getSelectionIndex();
         }
      });
   }

   private void createColumn(int i) {
      TableColumn column = new TableColumn(this.table, 16384, i);
      column.setAlignment(16384);
      column.setText(this.columnNames[i]);
      column.setWidth(100);
   }

   private void createTableViewer() {
      this.tableViewer = new TableViewer(this.table);
      this.tableViewer.setUseHashlookup(true);
      this.tableViewer.setColumnProperties(this.columnNames);
   }

   private void createButton(Composite composite) {
      final Composite buttonComposite = new Composite(composite, 0);
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
      buttonData.widthHint = 100;
      this.refreshButton = new Button(buttonComposite, 16777224);
      this.refreshButton.setText("Refresh");
      this.refreshButton.setLayoutData(buttonData);
      this.refreshButton.setToolTipText("Refresh status");
      this.refreshButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            FidelityProfileEnvView.this.connList.updateList();
            FidelityProfileEnvView.this.tableViewer.refresh();
            FidelityProfileEnvView.this.refreshConnectionStatus();
            FidelityProfileEnvView.this.updateRefreshLabel();
         }
      });
      this.exportButton = new Button(buttonComposite, 16777224);
      this.exportButton.setText("Export");
      this.exportButton.setLayoutData(buttonData);
      this.exportButton.setToolTipText("Export connection setting");
      this.exportButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            FidelityProfileEnvView.this.exportConnections();
         }
      });
      this.importButton = new Button(buttonComposite, 16777224);
      this.importButton.setText("Import");
      this.importButton.setLayoutData(buttonData);
      this.importButton.setToolTipText("Import Connection setting from a text file");
      this.importButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            FidelityProfileEnvView.this.importConnections();
         }
      });
      this.addButton = new Button(buttonComposite, 16777224);
      this.addButton.setText("Add Connection");
      this.addButton.setLayoutData(buttonData);
      this.addButton.setToolTipText("Add New Connection");
      this.addButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            FidelityProfileEnvView.this.addEnvToList();
            buttonComposite.setCursor(new Cursor(buttonComposite.getDisplay(), 0));
         }
      });
      this.deleteButton = new Button(buttonComposite, 16777224);
      this.deleteButton.setText("Delete Connection");
      this.deleteButton.setLayoutData(buttonData);
      this.deleteButton.setToolTipText("Delete Connection");
      this.deleteButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            TableItem item = FidelityProfileEnvView.this.tableViewer.getTable().getItem(FidelityProfileEnvView.this.selection);
            String host_name = item.getText(0);
            boolean status = MessageDialog.openConfirm(new Shell(), "Confirmation", "You are going to delete the connection settings for " + host_name + " Click OK to continue, Cancel to cancel");
            if (status) {
               IdePlugin.getDefault().removeEnvEntry(host_name);
               FidelityProfileEnvView.this.saveConnections();
               ProfileConnectionEntry entry = FidelityProfileEnvView.this.connList.getEntry(host_name);
               FidelityProfileEnvView.this.connList.removeConnection(entry);
            }

         }
      });
      this.updateButton = new Button(buttonComposite, 16777224);
      this.updateButton.setText("Update Connection");
      this.updateButton.setLayoutData(buttonData);
      this.updateButton.setToolTipText("Update Connection");
      this.updateButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            FidelityProfileEnvView.this.updateEnvEntry();
            buttonComposite.setCursor(new Cursor(buttonComposite.getDisplay(), 0));
         }
      });
   }

   public void setFocus() {
   }

   private void addEnvToList() {
      ProfileEnvDialog dialog = new ProfileEnvDialog(new Shell(), (ProfileEnvEntry)null);
      if (dialog.open() == 0) {
         ProfileEnvEntry env = dialog.getEnv();
         IdePlugin.getDefault().addEnvList(env);
         this.saveConnections();
         ProfileConnectionEntry entry = new ProfileConnectionEntry(env);
         this.connList.addConnection(entry);
      }

   }

   private void updateEnvEntry() {
      TableItem item = this.tableViewer.getTable().getItem(this.selection);
      String host_name = item.getText(0);
      ProfileEnvEntry oldEnv = IdePlugin.getDefault().getEnvironment(host_name);
      ProfileEnvDialog dialog = new ProfileEnvDialog(new Shell(), oldEnv);
      if (dialog.open() == 0) {
         IdePlugin.getDefault().removeEnvEntry(host_name);
         ProfileEnvEntry env = dialog.getEnv();
         IdePlugin.getDefault().addEnvList(env);
         this.saveConnections();
         ProfileConnectionEntry entry = this.connList.getEntry(env.getName());
         entry.setServer_name(env.getServer());
         entry.setPort_number(env.getPort());
         entry.setIbs_server_name(env.getIbsServer());
         entry.setUser_id(env.getUserName());
         entry.setPassword(env.getPassword());
         this.connList.connectionChanged(entry);
      }

   }

   private void saveConnections() {
      HashSet set = IdePlugin.getDefault().getEnvSet();
      int num = set.size();
      String[] envString = new String[num];
      int i = 0;

      for(Iterator itr = set.iterator(); itr.hasNext(); ++i) {
         ProfileEnvEntry envEntry = (ProfileEnvEntry)itr.next();
         envString[i] = envEntry.getData();
      }

      IdePlugin.getDefault().setEnvPreference(envString);
   }

   public void refreshConnectionStatus() {
      this.ht = this.initHashtable();
      CheckConnectionJob checkJob = new CheckConnectionJob("Check Connection", this.connList, this.ht);
      IJobManager manager = Platform.getJobManager();
      manager.addJobChangeListener(new JobChangeAdapter() {
         public void done(IJobChangeEvent event) {
            if (event.getJob().getName().equals("Check Connection")) {
               Runnable r = new Runnable() {
                  public void run() {
                     FidelityProfileEnvView.this.tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
                        public void run() {
                           FidelityProfileEnvView.this.updateTable(FidelityProfileEnvView.this.ht);
                        }
                     });
                  }
               };
               if (FidelityProfileEnvView.this.tableViewer.getTable().getDisplay() != null) {
                  r.run();
               } else {
                  FidelityProfileEnvView.this.tableViewer.getTable().getDisplay().syncExec(r);
               }
            }

         }
      });
      checkJob.schedule();
   }

   private void updateTable(Hashtable ht) {
      Iterator itr = this.connList.getConnections().iterator();

      while(itr.hasNext()) {
         ProfileConnectionEntry entry = (ProfileConnectionEntry)itr.next();
         String host = entry.getHost_name();
         Hashtable conn = (Hashtable)ht.get(host);
         entry.setPing_status((String)conn.get("ping_status"));
         entry.setMtm_status((String)conn.get("mtm_status"));
         entry.setIbs_server_count((String)conn.get("ibs_server_count"));
         entry.setSystem_date((String)conn.get("system_date"));
         entry.setError_count((String)conn.get("error_count"));
         this.connList.connectionChanged(entry);
      }

   }

   private Hashtable initHashtable() {
      Hashtable htHost = new Hashtable();
      Iterator itr = this.connList.getConnections().iterator();

      while(itr.hasNext()) {
         ProfileConnectionEntry entry = (ProfileConnectionEntry)itr.next();
         String host = entry.getHost_name();
         Hashtable htConn = new Hashtable();
         htConn.put("ping_status", "Failed");
         htConn.put("mtm_status", "Failed");
         htConn.put("ibs_server_count", "-1");
         htConn.put("system_date", "-1");
         htConn.put("error_count", "-1");
         htHost.put(host, htConn);
      }

      return htHost;
   }

   private void exportConnections() {
      FileDialog dialog = new FileDialog(new Shell(), 8192);
      final String path = dialog.open();
      if (path != null) {
         final StringBuffer buffer = new StringBuffer();
         Iterator itr = this.connList.getConnections().iterator();

         while(itr.hasNext()) {
            ProfileConnectionEntry entry = (ProfileConnectionEntry)itr.next();
            buffer.append(entry.toString());
            buffer.append(";");
         }

         try {
            IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
            progressService.busyCursorWhile(new IRunnableWithProgress() {
               public void run(IProgressMonitor monitor) {
                  FidelityProfileEnvView.this.tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
                     public void run() {
                        try {
                           FileOutputStream stream = new FileOutputStream(path, false);
                           stream.write(buffer.toString().getBytes());
                           stream.close();
                        } catch (FileNotFoundException var2) {
                           var2.printStackTrace();
                        } catch (IOException var3) {
                           var3.printStackTrace();
                        }

                     }
                  });
               }
            });
         } catch (InvocationTargetException var6) {
            var6.printStackTrace();
         } catch (InterruptedException var7) {
            var7.printStackTrace();
         }
      }

   }

   public void importConnections() {
      FileDialog dialog = new FileDialog(new Shell(), 4096);
      String filename = dialog.open();
      int importCount = 0;
      StringBuffer buffer = new StringBuffer();
      if (filename != null || filename.length() > 0) {
         try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line = null;

            while((line = in.readLine()) != null) {
               buffer.append(line);
            }

            if (buffer.toString().length() > 0) {
               String[] connStr = buffer.toString().split(";");

               for(int i = 0; i < connStr.length; ++i) {
                  if (connStr[i].length() != 0 && connStr[i] != null) {
                     ProfileEnvEntry env = new ProfileEnvEntry(connStr[i]);
                     if (IdePlugin.getDefault().getEnvironment(env.getName()) != null) {
                        boolean status = MessageDialog.openConfirm(new Shell(), "Confirmation", env.getName() + " already exists. Do you want to overwrite it? Click OK will reset the username/password " + "to NULL");
                        if (status) {
                           IdePlugin.getDefault().removeEnvEntry(env.getName());
                           IdePlugin.getDefault().addEnvList(env);
                           this.saveConnections();
                           ProfileConnectionEntry entry = this.connList.getEntry(env.getName());
                           entry.setServer_name(env.getServer());
                           entry.setPort_number(env.getPort());
                           entry.setIbs_server_name(env.getIbsServer());
                           entry.setUser_id(env.getUserName());
                           entry.setPassword(env.getPassword());
                           this.connList.connectionChanged(entry);
                           ++importCount;
                        }
                     } else {
                        IdePlugin.getDefault().addEnvList(env);
                        this.saveConnections();
                        ProfileConnectionEntry entry = new ProfileConnectionEntry(env);
                        this.connList.addConnection(entry);
                        ++importCount;
                     }
                  }
               }

               if (importCount > 0) {
                  MessageDialog.openInformation(new Shell(), "Information", "Please enter the user name and password for those connections that have been imported before running the Refresh.");
               }
            }
         } catch (FileNotFoundException var12) {
            var12.printStackTrace();
         } catch (IOException var13) {
            var13.printStackTrace();
         }
      }

   }

   private void updateRefreshLabel() {
      DateFormat df = DateFormat.getDateTimeInstance(1, 1);
      Date today = new Date();
      this.getViewSite().getActionBars().getStatusLineManager().setMessage("Last refreshed time : " + df.format(today));
   }

   class ConnectionPropContentProvider implements IStructuredContentProvider, IConnectionListViewer {
      public Object[] getElements(Object inputElement) {
         return FidelityProfileEnvView.this.connList.getConnections().toArray();
      }

      public void dispose() {
         FidelityProfileEnvView.this.connList.removeChangeListener(this);
      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
         if (newInput != null) {
            ((ProfileConnectionEntryList)newInput).addChangeListener(this);
         }

         if (oldInput != null) {
            ((ProfileConnectionEntryList)oldInput).removeChangeListener(this);
         }

      }

      public void addConnection(ProfileConnectionEntry connectionEntry) {
         FidelityProfileEnvView.this.tableViewer.add(connectionEntry);
      }

      public void removeConnection(ProfileConnectionEntry connectionEntry) {
         FidelityProfileEnvView.this.tableViewer.remove(connectionEntry);
      }

      public void updateConnection(ProfileConnectionEntry connectionEntry) {
         FidelityProfileEnvView.this.tableViewer.update(connectionEntry, (String[])null);
      }
   }
}
