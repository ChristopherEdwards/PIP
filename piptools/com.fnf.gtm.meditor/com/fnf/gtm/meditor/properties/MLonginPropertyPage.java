package com.fnf.gtm.meditor.properties;

import com.fnf.gtm.meditor.editor.MessageUtil;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

public class MLonginPropertyPage extends PropertyPage {
   private static final int TEXT_FIELD_WIDTH = 50;
   private String msg = "";
   private Label lblSpace;
   private Text txtFtpHost;
   private Text txtFtpUserID;
   private Text txtFtpPassword;
   private Text txtFtpDirectory;

   private boolean validate() {
      if (this.txtFtpDirectory.getText().trim().compareTo("") != 0) {
         if (this.txtFtpDirectory.getText().trim().endsWith("/")) {
            return true;
         } else if (this.txtFtpDirectory.getText().trim().endsWith(":")) {
            return true;
         } else {
            MessageDialog.openInformation(this.getShell(), "Alert", "Host directory must end with a slash (/) or a colon (:)");
            return false;
         }
      } else {
         return true;
      }
   }

   public Control createContents(Composite parent) {
      int column = 3;
      Composite panel = this.createComposite(parent, column);
      String msg = MessageUtil.getString("M.editor.property.description");
      this.lblSpace = this.createDescLabel(panel, msg, column);
      this.lblSpace = this.createLabel(panel, "User ID:");
      this.txtFtpUserID = this.addTextSection(panel, "User ID:", "FTPUSERID", "");
      this.lblSpace = this.createLabel(panel, "");
      this.lblSpace = this.createLabel(panel, "Password:");
      this.txtFtpPassword = this.addTextSection(panel, "Password:", "FTPPASSWORD", "");
      this.txtFtpPassword.setEchoChar('*');
      this.lblSpace = this.createLabel(panel, "");
      this.lblSpace = this.createLabel(panel, "Host:");
      this.txtFtpHost = this.addTextSection(panel, "Host:", "FTPHOST", "");
      this.lblSpace = this.createLabel(panel, "");
      this.lblSpace = this.createLabel(panel, "Host Directory:");
      this.txtFtpDirectory = this.addTextSection(panel, "Host Directory:", "FTPDIRECTORY", "");
      this.lblSpace = this.createLabel(panel, "");
      return new Canvas(panel, 0);
   }

   private Text addTextSection(Composite parent, String title, String property, String defaultProperty) {
      Text text = new Text(parent, 2052);
      GridData gd = new GridData();
      gd.grabExcessVerticalSpace = true;
      gd.widthHint = this.convertWidthInCharsToPixels(50);
      gd.horizontalAlignment = 4;
      text.setLayoutData(gd);

      try {
         String str = ((IResource)this.getElement()).getPersistentProperty(new QualifiedName("", property));
         text.setText(str != null ? str : defaultProperty);
      } catch (CoreException var8) {
         text.setText(defaultProperty);
      }

      return text;
   }

   protected Composite createComposite(Composite parent, int numColumns) {
      Composite composite = new Composite(parent, 0);
      GridLayout layout = new GridLayout();
      layout.numColumns = numColumns;
      composite.setLayout(layout);
      GridData data = new GridData();
      data.verticalAlignment = 4;
      data.horizontalAlignment = 4;
      composite.setLayoutData(data);
      return composite;
   }

   protected Label createLabel(Composite parent, String str) {
      Label label = new Label(parent, 131072);
      label.setText(str);
      GridData data = new GridData();
      label.setLayoutData(data);
      return label;
   }

   protected Label createDescLabel(Composite parent, String str, int cols) {
      Label label = new Label(parent, 131072);
      label.setText(str);
      GridData data = new GridData();
      data.horizontalSpan = cols;
      label.setLayoutData(data);
      return label;
   }

   public boolean performOk() {
      if (!this.validate()) {
         return false;
      } else {
         this.saveSettings();
         return true;
      }
   }

   private void saveSettings() {
      try {
         ((IResource)this.getElement()).setPersistentProperty(new QualifiedName("", "FTPUSERID"), this.txtFtpUserID.getText());
         ((IResource)this.getElement()).setPersistentProperty(new QualifiedName("", "FTPPASSWORD"), this.txtFtpPassword.getText());
         ((IResource)this.getElement()).setPersistentProperty(new QualifiedName("", "FTPHOST"), this.txtFtpHost.getText());
         ((IResource)this.getElement()).setPersistentProperty(new QualifiedName("", "FTPDIRECTORY"), this.txtFtpDirectory.getText());
      } catch (CoreException var2) {
         System.out.println(var2.getMessage());
      }

   }

   public void performDefaults() {
      this.txtFtpUserID.setText("");
      this.txtFtpPassword.setText("");
      this.txtFtpHost.setText("");
      this.txtFtpDirectory.setText("");
   }

   public void performApply() {
      this.saveSettings();
   }
}
