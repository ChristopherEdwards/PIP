package com.fnf.gtm.meditor.properties;

import com.fnf.gtm.meditor.MEditorPlugin;
import com.fnf.gtm.meditor.editor.MessageUtil;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

public class MPropertiesPage extends PropertyPage {
   private Text keywordsText;
   private Composite fParent;

   protected Control createContents(Composite parent) {
      this.fParent = parent;
      Composite composite = new Composite(parent, 0);
      composite.setLayout(new GridLayout());
      GridData data = new GridData(1808);
      data.grabExcessHorizontalSpace = true;
      composite.setLayoutData(data);
      this.addSection(composite);
      return composite;
   }

   private void addSection(Composite parent) {
      Group group = new Group(parent, 0);
      group.setText(MessageUtil.getString("M.editor.properties.group.title"));
      GridLayout gridLayout = new GridLayout();
      group.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      group.setLayoutData(gridData);
      Composite panel = new Composite(group, 0);
      GridLayout layout = new GridLayout();
      layout.numColumns = 3;
      panel.setLayout(layout);
      GridData data = new GridData(1808);
      data.grabExcessHorizontalSpace = true;
      panel.setLayoutData(data);
      Label label = new Label(panel, 0);
      label.setText(MessageUtil.getString("M.editor.properties.file.text"));
      this.keywordsText = new Text(panel, 2048);
      this.keywordsText.setLayoutData(new GridData(768));
      this.keywordsText.setText(this.getStoredText(MessageUtil.getString("M_PROJ_SPECIFIC_KWORDS")));
      Button browse = new Button(panel, 8);
      browse.setText(MessageUtil.getString("M.editor.properties.file.browse"));
      gridData = new GridData();
      gridData.widthHint = 60;
      browse.setLayoutData(gridData);
      browse.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            FileDialog fileBrowser = new FileDialog(MPropertiesPage.this.fParent.getShell());
            String dir = fileBrowser.open();
            if (dir != null) {
               MPropertiesPage.this.keywordsText.setText(dir);
            }

         }
      });
   }

   private String getStoredText(String property) {
      String text = null;

      try {
         text = ((IResource)this.getElement()).getPersistentProperty(new QualifiedName(MEditorPlugin.PLUGINID, property));
      } catch (CoreException var4) {
         var4.printStackTrace();
      }

      return text != null ? text : "";
   }

   protected void performDefaults() {
      this.keywordsText.setText("");
   }

   public boolean performOk() {
      this.saveProperties();
      return true;
   }

   private void saveProperties() {
      try {
         ((IResource)this.getElement()).setPersistentProperty(new QualifiedName(MEditorPlugin.PLUGINID, MessageUtil.getString("M_PROJ_SPECIFIC_KWORDS")), this.keywordsText == null ? "" : this.keywordsText.getText());
      } catch (CoreException var2) {
         var2.printStackTrace();
      }

   }
}
