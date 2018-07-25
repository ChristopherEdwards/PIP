package com.fis.profile.tablelist.dialogs;

import com.fis.profile.tablelist.utils.GetDataList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
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

public class UpdateBatch extends Dialog {
   private Text batchName;
   private Text routineName;
   private Text description;
   private Text whereClause;
   private Text distinctClause;
   private Text numThread;
   private Text numMessageBuffer;
   private Text megBufferSize;
   private Text threadContext;
   private Text intervalSchedule;
   private Text intervalThread;
   private Text scheduleTimeout;
   private Text threadTimeout;
   private Button nonRandomAccess;
   private String batchNameVal = "";
   private String routineNameVal = "";
   private String descVal = "";
   private String whereVal = "";
   private String distinctVal = "";
   private String numThreadVal = "";
   private String numMessageBufferVal = "";
   private String msgBufferSizeVal = "";
   private String threadContextVal = "";
   private String nonRandomAccessVal = "";
   private String intervalScheduleVal = "";
   private String intervalThreadVal = "";
   private String scheduleTimeoutVal = "";
   private String threadTimeoutVal = "";

   public UpdateBatch(Shell parentShell, String value) {
      super(parentShell);
      String[] values = value.split(",");
      if (values.length > 13) {
         this.batchNameVal = values[0];
         this.descVal = values[1];
         this.routineNameVal = values[2];
         this.whereVal = values[3];
         this.distinctVal = values[4];
         this.numThreadVal = values[5];
         this.numMessageBufferVal = values[6];
         this.msgBufferSizeVal = values[7];
         this.threadContextVal = values[8];
         this.nonRandomAccessVal = values[9];
         this.intervalScheduleVal = values[10];
         this.intervalThreadVal = values[11];
         this.scheduleTimeoutVal = values[12];
         this.threadTimeoutVal = values[13];
      }

   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText("Update Trigger");
   }

   protected Control createDialogArea(Composite composite) {
      Composite parent = new Composite(composite, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      parent.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      gridData.heightHint = 550;
      gridData.widthHint = 600;
      composite.setLayoutData(gridData);
      TabFolder tabFolder = new TabFolder(parent, 512);
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
      this.batchName = this.createText(mainTab, "Batch Name", 55);
      this.batchName.setEnabled(false);
      this.batchName.setText(this.batchNameVal);
      this.routineName = this.createText(mainTab, "Routine Name", 8);
      if (this.routineNameVal != null && !this.routineNameVal.equalsIgnoreCase("null")) {
         this.routineName.setText(this.routineNameVal);
      } else {
         this.routineName.setText("");
      }

      this.description = this.createText(mainTab, "Description", 40);
      this.description.setText(this.descVal);
      this.whereClause = this.createText(mainTab, "SQL Where Clause", 100);
      if (this.whereVal != null && !this.whereVal.equalsIgnoreCase("null")) {
         this.whereClause.setText(this.whereVal);
      } else {
         this.whereClause.setText("");
      }

      new Label(mainTab, 131072);
      Label label = new Label(mainTab, 16384);
      label.setText("( e.g. DEP.STAT > '4' )");
      label.setForeground(new Color((Device)null, 100, 100, 100));
      this.distinctClause = this.createText(mainTab, "SQL Distinct Clause", 100);
      if (this.distinctVal != null && !this.distinctVal.equalsIgnoreCase("null")) {
         this.distinctClause.setText(this.distinctVal);
      } else {
         this.distinctClause.setText("");
      }

      new Label(mainTab, 131072);
      label = new Label(mainTab, 16384);
      label.setText("( e.g. PROCDT,OSECID,CASEQ,SEQ )");
      label.setForeground(new Color((Device)null, 100, 100, 100));
      this.numThread = this.createText(mainTab, "Number of Threads", 2);
      if (this.numThreadVal != null && !this.numThreadVal.equalsIgnoreCase("null")) {
         this.numThread.setText(this.numThreadVal);
      } else {
         this.numThread.setText("");
      }

      this.numMessageBuffer = this.createText(mainTab, "Number of Message Buffer", 4);
      if (this.numMessageBufferVal != null && !this.numMessageBufferVal.equalsIgnoreCase("null")) {
         this.numMessageBuffer.setText(this.numMessageBufferVal);
      } else {
         this.numMessageBuffer.setText("");
      }

      this.megBufferSize = this.createText(mainTab, "Message Buffer Size", 5);
      if (this.msgBufferSizeVal != null && !this.msgBufferSizeVal.equalsIgnoreCase("null")) {
         this.megBufferSize.setText(this.msgBufferSizeVal);
      } else {
         this.megBufferSize.setText("");
      }

      this.threadContext = this.createText(mainTab, "Thread Context", 80);
      if (this.threadContextVal != null && !this.threadContextVal.equalsIgnoreCase("null")) {
         this.threadContext.setText(this.threadContextVal);
      } else {
         this.threadContext.setText("");
      }

      new Label(mainTab, 0);
      Composite subCom = new Composite(mainTab, 0);
      gl = new GridLayout();
      gl.numColumns = 2;
      subCom.setLayout(gl);
      gd = new GridData(768);
      subCom.setLayoutData(gd);
      this.nonRandomAccess = this.createCheckBox(subCom, "Non-Random Message Access");
      this.nonRandomAccess.setText("Non-Random Message Access");
      this.nonRandomAccess.setSelection(this.getBooleanValue(this.nonRandomAccessVal));
      this.intervalSchedule = this.createText(mainTab, "Job Monitor Update Interval - Scheduler", 12);
      if (this.intervalScheduleVal != null && !this.intervalScheduleVal.equalsIgnoreCase("null")) {
         this.intervalSchedule.setText(this.intervalScheduleVal);
      } else {
         this.intervalSchedule.setText("");
      }

      this.intervalThread = this.createText(mainTab, "Job Monitor Update Interval - Threads", 12);
      if (this.intervalThreadVal != null && !this.intervalThreadVal.equalsIgnoreCase("null")) {
         this.intervalThread.setText(this.intervalThreadVal);
      } else {
         this.intervalThread.setText("");
      }

      this.scheduleTimeout = this.createText(mainTab, "Scheduler Timeout (seconds)", 2);
      if (this.scheduleTimeoutVal != null && !this.scheduleTimeoutVal.equalsIgnoreCase("null")) {
         this.scheduleTimeout.setText(this.scheduleTimeoutVal);
      } else {
         this.scheduleTimeout.setText("");
      }

      this.threadTimeout = this.createText(mainTab, "Thread Timeout", 2);
      if (this.threadTimeoutVal != null && !this.threadTimeoutVal.equalsIgnoreCase("null")) {
         this.threadTimeout.setText(this.threadTimeoutVal);
      } else {
         this.threadTimeout.setText("");
      }

      return mainTab;
   }

   private boolean getBooleanValue(String val) {
      return val.equalsIgnoreCase("true");
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
      if (this.description.getText().length() == 0) {
         returnMsg = "\nDescription is required.\n";
      }

      if (!this.testNumber(this.numThread.getText())) {
         returnMsg = returnMsg + "Number of Thread needs to be a number.\n";
      }

      if (!this.testNumber(this.numMessageBuffer.getText())) {
         returnMsg = returnMsg + "Number of Message Buffer needs to be a number.\n";
      }

      if (!this.testNumber(this.megBufferSize.getText())) {
         returnMsg = returnMsg + "Message Buffer Size needs to be a number.\n";
      }

      if (!this.testNumber(this.intervalSchedule.getText())) {
         returnMsg = returnMsg + "Job Monitor Update Interval - Scheduler needs to be a number.\n";
      }

      if (!this.testNumber(this.intervalThread.getText())) {
         returnMsg = returnMsg + "Job Monitor Update Interval - Threads needs to be a number.\n";
      }

      if (!this.testNumber(this.scheduleTimeout.getText())) {
         returnMsg = returnMsg + "Schedule Timeout needs to be a number.\n";
      }

      if (!this.testNumber(this.threadTimeout.getText())) {
         returnMsg = returnMsg + "Thread Timeout needs to be a number.\n";
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

   private boolean testNumber(String text) {
      if (text == null || text.equals("") || text.equalsIgnoreCase("null")) {
         text = "0";
      }

      boolean isNumber;
      try {
         Integer.parseInt(text);
         isNumber = true;
      } catch (NumberFormatException var3) {
         isNumber = false;
      }

      return isNumber;
   }

   protected void okPressed() {
      if (this.validate()) {
         this.doUpdate();
         super.okPressed();
      }
   }

   private void doUpdate() {
      String statement = "UPDATE DBTBL33 SET DES = '" + this.description.getText().replaceAll("'", "") + "', ";
      if (this.routineName.getText().length() == 0) {
         statement = statement + "PFID = 'null', ";
      } else {
         statement = statement + "PFID = '" + this.routineName.getText().replaceAll("'", "") + "', ";
      }

      if (this.whereClause.getText().length() == 0) {
         statement = statement + "WHERE = 'null', ";
      } else {
         statement = statement + "WHERE = '" + this.whereClause.getText().replaceAll("'", "") + "', ";
      }

      if (this.distinctClause.getText().length() == 0) {
         statement = statement + "DISTINCT = 'null', ";
      } else {
         statement = statement + "DISTINCT = '" + this.distinctClause.getText().replaceAll("'", "") + "', ";
      }

      if (this.numThread.getText().length() == 0) {
         statement = statement + "THREADS = '', ";
      } else {
         statement = statement + "THREADS = '" + this.numThread.getText() + "', ";
      }

      if (this.numMessageBuffer.getText().length() == 0) {
         statement = statement + "MSGBUFS = '', ";
      } else {
         statement = statement + "MSGBUFS = '" + this.numMessageBuffer.getText() + "', ";
      }

      if (this.megBufferSize.getText().length() == 0) {
         statement = statement + "MAXSIZE = '', ";
      } else {
         statement = statement + "MAXSIZE = '" + this.megBufferSize.getText() + "', ";
      }

      if (this.threadContext.getText().length() == 0) {
         statement = statement + "THRLVAR = 'null', ";
      } else {
         statement = statement + "THRLVAR = '" + this.threadContext.getText().replaceAll("'", "") + "', ";
      }

      if (this.intervalSchedule.getText().length() == 0) {
         statement = statement + "SCHRCNT = '', ";
      } else {
         statement = statement + "SCHRCNT = '" + this.intervalSchedule.getText() + "', ";
      }

      if (this.intervalThread.getText().length() == 0) {
         statement = statement + "THRRCNT = '', ";
      } else {
         statement = statement + "THRRCNT = '" + this.intervalThread.getText() + "', ";
      }

      if (this.scheduleTimeout.getText().length() == 0) {
         statement = statement + "SCHTIMR = '', ";
      } else {
         statement = statement + "SCHTIMR = '" + this.scheduleTimeout.getText() + "', ";
      }

      if (this.threadTimeout.getText().length() == 0) {
         statement = statement + "THRTIMR = '', ";
      } else {
         statement = statement + "THRTIMR = '" + this.threadTimeout.getText() + "', ";
      }

      if (this.nonRandomAccess.getSelection()) {
         statement = statement + "NONRAND = '1' ";
      } else {
         statement = statement + "NONRAND = '0' ";
      }

      statement = statement + "WHERE BCHID = '" + this.batchNameVal + "'";
      GetDataList doUpdate = new GetDataList(statement, (String[])null);
      doUpdate.updateRecord();
   }
}
