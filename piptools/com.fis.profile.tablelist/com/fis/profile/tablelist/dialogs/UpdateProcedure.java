package com.fis.profile.tablelist.dialogs;

import com.fis.profile.tablelist.utils.GetDataList;
import com.fis.profile.tablelist.utils.SchemaInfo;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
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

public class UpdateProcedure extends Dialog {
   private SchemaInfo help = new SchemaInfo("IndexInfo.xml");
   private Text procName;
   private Text description;
   private Text routine;
   private String name = "";
   private String desc = "";
   private String routineName = "";

   public UpdateProcedure(Shell parentShell, String value) {
      super(parentShell);
      String[] values = value.split(",");
      if (values.length > 3) {
         this.name = values[0];
         this.desc = values[1];
         this.routineName = values[2];
      }

   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText("Update Produce");
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
      gl.numColumns = 2;
      mainTab.setLayout(gl);
      GridData gd = new GridData(768);
      mainTab.setLayoutData(gd);
      this.procName = this.createText(mainTab, "Procedure Name", 100);
      this.procName.setEditable(false);
      this.procName.setText(this.name);
      this.routine = this.createText(mainTab, "Runtime Routine", 8);
      if (this.routineName != null && !this.routineName.equalsIgnoreCase("null")) {
         this.routine.setText(this.routineName);
      } else {
         this.routine.setText("");
      }

      this.description = this.createText(mainTab, "Description", 30);
      this.description.setText(this.desc);
      return mainTab;
   }

   public Text createText(Composite parent, String labelString, int length) {
      Label label = new Label(parent, 0);
      GridData gd = new GridData();
      label.setLayoutData(gd);
      label.setText(labelString + ":");
      Text text = new Text(parent, 2048);
      gd = new GridData(768);
      gd.heightHint = 15;
      text.setLayoutData(gd);
      text.setTextLimit(length);
      return text;
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

   private boolean validate() {
      String returnMsg = "";
      if (this.description.getText().length() == 0) {
         returnMsg = "Description is required.";
      }

      String message = "This table can not be updated due to below problems: \n";
      message = message + returnMsg;
      message = message + "\n Please go back and modify your changes.";
      boolean isValid;
      if (returnMsg.length() > 0) {
         MessageDialog.openInformation((Shell)null, "Validate Error", message);
         isValid = false;
      } else {
         boolean ok = MessageDialog.openConfirm((Shell)null, "Are you sure to update?", "Changes will be updated to Profile DB. This dialog will be closed and the PSL View will be refreshed with new information.");
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
         this.doUpdate();
         super.okPressed();
      }
   }

   private void doUpdate() {
      String routineVal = this.routine.getText();
      if (routineVal.length() == 0) {
         routineVal = "null";
      } else {
         routineVal = this.routine.getText().replaceAll("'", "");
      }

      String statement = "UPDATE DBTBL25 SET DES = '" + this.description.getText().replaceAll("'", "") + "', " + "PGM = '" + routineVal + "' " + " WHERE PROCID = '" + this.name + "'";
      GetDataList doUpdate = new GetDataList(statement, (String[])null);
      doUpdate.updateRecord();
   }
}
