package com.fnf.gtm.meditor.preferences;

import com.fnf.gtm.meditor.MEditorPlugin;
import com.fnf.gtm.meditor.editor.MessageUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class MEditorPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, SelectionListener, ModifyListener {
   private Button checkBoxLineNumber;
   private Text textFieldTabWidth;
   private FontFieldEditor fFontEditor;
   int textLimit = 3;

   public MEditorPreferencePage() {
      this.setPreferenceStore(MEditorPlugin.getDefault().getPreferenceStore());
      this.setDescription("M editor settings:");
   }

   private Composite createComposite(Composite parent, int numColumns) {
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

   protected Control createContents(Composite parent) {
      Composite behaviorComposite = this.createComposite(parent, 2);
      String label = "Text &font:";
      this.addTextFontEditor(behaviorComposite, label, "org.eclipse.jface.textfont");
      Composite composite = new Composite(parent, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      composite.setLayout(gridLayout);
      composite.setLayoutData(new GridData());
      this.checkBoxLineNumber = new Button(composite, 32);
      this.checkBoxLineNumber.setText(MessageUtil.getString("M.editor.lineNumber.title"));
      GridData gd = new GridData(32);
      gd.horizontalSpan = 2;
      this.checkBoxLineNumber.setLayoutData(gd);
      this.checkBoxLineNumber.setSelection(true);
      this.checkBoxLineNumber.addSelectionListener(this);
      Label labelControl = new Label(composite, 0);
      labelControl.setText(MessageUtil.getString("M.editor.tabWidth.title"));
      gd = new GridData(32);
      labelControl.setLayoutData(gd);
      this.textFieldTabWidth = new Text(composite, 2052);
      gd = new GridData(32);
      gd.widthHint = this.convertWidthInCharsToPixels(this.textLimit + 1);
      this.textFieldTabWidth.setLayoutData(gd);
      this.textFieldTabWidth.setTextLimit(this.textLimit);
      this.loadPreferenceValues();
      return new Composite(parent, 0);
   }

   private void addTextFontEditor(Composite parent, String label, String key) {
      Composite editorComposite = new Composite(parent, 0);
      GridLayout layout = new GridLayout();
      layout.numColumns = 3;
      editorComposite.setLayout(layout);
      this.fFontEditor = new FontFieldEditor(key, label, editorComposite);
      this.fFontEditor.setChangeButtonText("C&hange...");
      GridData gd = new GridData(768);
      gd.horizontalSpan = 2;
      editorComposite.setLayoutData(gd);
   }

   protected IPreferenceStore doGetPreferenceStore() {
      return MEditorPlugin.getDefault().getPreferenceStore();
   }

   public void init(IWorkbench workbench) {
   }

   private void loadPreferenceValues() {
      this.fFontEditor.setPreferenceStore(this.getPreferenceStore());
      this.fFontEditor.load();
      IPreferenceStore store = this.getPreferenceStore();
      this.checkBoxLineNumber.setSelection(store.getBoolean("com.fnf.gtm.meditor.showLineNumber"));
      this.textFieldTabWidth.setText(store.getString("com.fnf.gtm.meditor.width"));
   }

   private void initializeDefaults() {
      IPreferenceStore store = this.getPreferenceStore();
      this.fFontEditor.loadDefault();
      this.checkBoxLineNumber.setSelection(store.getDefaultBoolean("com.fnf.gtm.meditor.showLineNumber"));
      this.textFieldTabWidth.setText(store.getDefaultString("com.fnf.gtm.meditor.width"));
   }

   protected void performDefaults() {
      super.performDefaults();
      this.initializeDefaults();
   }

   public boolean performOk() {
      if (!this.validate()) {
         return false;
      } else {
         this.fFontEditor.store();
         this.storePreferenceValues();
         MEditorPlugin.getDefault().savePluginPreferences();
         return true;
      }
   }

   private void storePreferenceValues() {
      IPreferenceStore store = this.getPreferenceStore();
      store.setValue("com.fnf.gtm.meditor.showLineNumber", this.checkBoxLineNumber.getSelection());
      store.setValue("com.fnf.gtm.meditor.width", Integer.parseInt(this.textFieldTabWidth.getText()));
   }

   private boolean validate() {
      String message = "";
      String number = this.textFieldTabWidth.getText();
      if (number.length() != 0 && number != null) {
         int value = Integer.parseInt(number);
         if (value < 0) {
            message = "Displayed tab width can not be negative number!";
         }
      } else {
         message = "Displayed tab width can not be empty!";
      }

      if (message.equalsIgnoreCase("")) {
         return true;
      } else {
         MessageDialog.openError(this.getShell(), "Error", message);
         return false;
      }
   }

   public void widgetDefaultSelected(SelectionEvent event) {
   }

   public void widgetSelected(SelectionEvent event) {
   }

   public void modifyText(ModifyEvent event) {
   }

   public static void initDefaults(IPreferenceStore store) {
      store.setDefault("org.eclipse.jface.textfont", "1|Courier|10|0|WINDOWS|1|-13|0|0|0|0|0|0|0|1|0|0|0|0|Courier");
      store.setDefault("com.fnf.gtm.meditor.showLineNumber", true);
      store.setDefault("com.fnf.gtm.meditor.width", 8);
   }
}
