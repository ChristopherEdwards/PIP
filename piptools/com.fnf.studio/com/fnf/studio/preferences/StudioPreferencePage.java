package com.fnf.studio.preferences;

import com.fnf.studio.StudioPlugin;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class StudioPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
   public static final String P_PATH = "pathPreference";
   public static final String P_BOOLEAN = "booleanPreference";
   public static final String P_CHOICE = "choicePreference";
   public static final String P_STRING = "stringPreference";

   public StudioPreferencePage() {
      super(1);
      this.setPreferenceStore(StudioPlugin.getDefault().getPreferenceStore());
      this.setDescription("General Settings For Fidelity Application Studio Development");
   }

   private void initializeDefaults() {
      IPreferenceStore store = this.getPreferenceStore();
      store.setDefault("booleanPreference", true);
      store.setDefault("choicePreference", "choice2");
      store.setDefault("stringPreference", "Default value");
   }

   public void createFieldEditors() {
   }

   public void init(IWorkbench workbench) {
   }
}
