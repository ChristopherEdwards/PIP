package com.fis.profile.tablelist.dialogs;

import com.fis.profile.tablelist.TableList;
import com.fnf.profile.ide.utils.DQObjectGet;
import com.fnf.profile.ide.utils.DQObjectItem;
import com.fnf.profile.ide.utils.DQObjectList;
import com.fnf.profile.ide.utils.ProfileConnection;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class OpenPSLEditor extends Dialog {
   private Text tableName;
   private Text typeName;
   private Text resourceName;
   public Text targetFolder;
   private String tableNameVal = "";
   private String typeNameVal = "";
   private String resourceNameVal = "";
   private String objectName = "";
   private DQObjectList dqList = new DQObjectList();
   private DQObjectItem objectItem = new DQObjectItem();
   private DQObjectGet dqObject;
   private boolean fileExisted = false;
   private IFile file;
   private boolean setContents = true;
   private boolean openfile = false;
   private String content;
   private InputStream stream;

   public OpenPSLEditor(Shell parentShell, String tableName, String resourceName, String typeName) {
      super(parentShell);
      this.tableNameVal = tableName;
      this.typeNameVal = typeName;
      this.resourceNameVal = resourceName;
      this.objectName = resourceName;
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText("Open in PSL Editor");
   }

   protected Control createDialogArea(Composite composite) {
      Composite parent = new Composite(composite, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      parent.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      gridData.heightHint = 300;
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
      return parent;
   }

   private Composite buildMainTab(TabFolder parent) {
      Composite mainTab = new Composite(parent, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 3;
      mainTab.setLayout(gl);
      GridData gd = new GridData(768);
      mainTab.setLayoutData(gd);
      this.targetFolder = this.createText(mainTab, "Target Folder", 255);
      Button button = new Button(mainTab, 8);
      button.setText("Browse...");
      button.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent e) {
            OpenPSLEditor.this.handleBrowse();
         }
      });
      this.typeName = this.createText(mainTab, "Type", 55);
      this.typeName.setEnabled(false);
      this.typeName.setText(this.typeNameVal);
      gd = new GridData(768);
      gd.horizontalSpan = 2;
      this.typeName.setLayoutData(gd);
      this.tableName = this.createText(mainTab, "Table", 100);
      if (this.tableNameVal.length() > 0) {
         this.tableName.setEnabled(true);
      } else {
         this.tableName.setEnabled(false);
      }

      this.tableName.setText(this.tableNameVal);
      gd = new GridData(768);
      gd.horizontalSpan = 2;
      this.tableName.setLayoutData(gd);
      this.resourceName = this.createText(mainTab, this.typeNameVal, 150);
      if (this.typeNameVal.equals("Procedure")) {
         this.resourceNameVal = this.resourceNameVal + ".PROC";
      } else if (this.typeNameVal.equals("Trigger")) {
         this.resourceNameVal = this.resourceNameVal + ".TRIG";
      } else if (this.typeNameVal.equals("Batch")) {
         this.resourceNameVal = this.resourceNameVal + ".BATCH";
      }

      this.resourceName.setText(this.resourceNameVal);
      this.resourceName.setEnabled(false);
      gd = new GridData(768);
      gd.horizontalSpan = 2;
      this.resourceName.setLayoutData(gd);
      return mainTab;
   }

   private void handleBrowse() {
      SelectProjectFolder dialog = new SelectProjectFolder(this.getShell(), this);
      dialog.open();
   }

   public Text createText(Composite parent, String labelString, int length) {
      GridData gd;
      if (labelString.length() > 0) {
         Label label = new Label(parent, 0);
         gd = new GridData();
         label.setLayoutData(gd);
         label.setText(labelString + ":");
      }

      Text text = new Text(parent, 2048);
      gd = new GridData(768);
      gd.heightHint = 15;
      text.setLayoutData(gd);
      text.setTextLimit(length);
      return text;
   }

   public Button createCheckBox(Composite parent, String labelString) {
      Button button = new Button(parent, 32);
      button.setText(labelString);
      GridData gd = new GridData(768);
      gd.horizontalSpan = 2;
      button.setLayoutData(gd);
      return button;
   }

   private boolean validate() {
      String returnMsg = "";
      if (this.targetFolder.getText().length() == 0) {
         returnMsg = "Target Folder is required.\n";
      }

      boolean isValid;
      if (returnMsg.length() > 0) {
         MessageDialog.openInformation((Shell)null, "Validate Error", returnMsg);
         isValid = false;
      } else {
         boolean ok = MessageDialog.openConfirm((Shell)null, "Open Resource in PSL Editor", this.resourceNameVal + " will be saved under target folder and it will be open for editing.");
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
         this.openEditor();
         super.okPressed();
      }
   }

   public String[] doGetFromProfile() {
      String[] results = new String[0];
      String env = TableList.getEnvName();
      String userID = TableList.getUserId();
      String pwd = TableList.getPwd();
      String server = TableList.getServerName();
      String port = TableList.getPort();
      String ibsServer = TableList.getIbsServer();
      ProfileConnection hostConnection = new ProfileConnection();
      Connection cn = hostConnection.getConnection(env);
      if (cn == null) {
         hostConnection.setConnection(env, userID, pwd, server, port, ibsServer);
         cn = hostConnection.getConnection(env);
      }

      if (cn != null) {
         this.dqObject = new DQObjectGet();
         this.dqObject.dqObjectType = this.typeNameVal;
         if (this.typeNameVal.equalsIgnoreCase("Trigger")) {
            this.dqObject.dqObjectName = this.tableNameVal + "-" + this.objectName;
         } else {
            this.dqObject.dqObjectName = this.objectName;
         }

         String ret = this.dqObject.getCode(cn);
         if (this.dqObject.getCodeOK) {
            results = new String[]{this.dqObject.sourceCode};
            return results;
         }

         MessageDialog.openWarning(this.getShell(), "Warning!", "Get " + this.dqObject.dqObjectType + "-" + this.dqObject.dqObjectName + " failed - " + ret);
      } else {
         MessageDialog.openWarning(this.getShell(), "Connection Failed", "Check settings in the preference page");
      }

      return results;
   }

   private boolean openEditor() {
      String[] tmp = this.doGetFromProfile();
      String containerName = this.targetFolder.getText();
      String fileName = this.resourceName.getText();
      if (this.typeNameVal.equalsIgnoreCase("Trigger")) {
         fileName = this.tableName.getText() + "-" + fileName;
      }

      if (fileName == null) {
         return false;
      } else {
         IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
         IResource resource = root.findMember(new Path(containerName));
         if (resource == null) {
            String msg = "Target folder \"" + containerName + "\" does not exist.";
            MessageDialog.openError(this.getShell(), "Error", msg);
            return false;
         } else {
            if (!resource.exists() || !(resource instanceof IContainer)) {
               try {
                  this.throwCoreException("Container \"" + containerName + "\" does not exist.");
               } catch (CoreException var9) {
                  ;
               }
            }

            IContainer container = (IContainer)resource;

            try {
               this.file = container.getFile(new Path(fileName));
               if (this.file.exists()) {
                  this.fileExisted = true;
                  if (!MessageDialog.openQuestion(this.getShell(), "File already exists", "Do you want to overwrite the exist file?")) {
                     this.setContents = false;
                  }
               }

               if (this.setContents) {
                  this.content = tmp[0];
                  this.stream = this.openContentStream(this.content);
                  this.stream.close();
                  if (this.content.equals("")) {
                     return false;
                  }

                  if (this.file.exists()) {
                     this.file.setContents(this.stream, true, true, (IProgressMonitor)null);
                  } else {
                     this.file.create(this.stream, true, (IProgressMonitor)null);
                  }

                  IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                  IDE.openEditor(page, this.file);
               }

               return true;
            } catch (Exception var8) {
               MessageDialog.openError(this.getShell(), "Error", var8.getMessage());
               return false;
            }
         }
      }
   }

   private InputStream openContentStream(String content) {
      return new ByteArrayInputStream(content.getBytes());
   }

   private void throwCoreException(String message) throws CoreException {
      IStatus status = new Status(4, "com.fnf.profile.ide", 0, message, (Throwable)null);
      throw new CoreException(status);
   }
}
