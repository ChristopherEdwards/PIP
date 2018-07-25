package com.fnf.gtm.meditor;

import com.fnf.gtm.meditor.preferences.MEditorPreferencePage;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class MEditorPlugin extends AbstractUIPlugin {
   public static String PLUGINID = "com.fnf.gtm.meditor";
   private static MEditorPlugin plugin;
   private ResourceBundle resourceBundle;

   public static ImageDescriptor getImageDescriptor(String name) {
      String iconPath = "icons/";
      return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGINID, iconPath + name);
   }

   public MEditorPlugin() {
      plugin = this;

      try {
         this.resourceBundle = ResourceBundle.getBundle("com.fng.gtm.meditor.MEditorPluginResources");
      } catch (MissingResourceException var1) {
         this.resourceBundle = null;
      }

   }

   public static MEditorPlugin getDefault() {
      return plugin;
   }

   public static IWorkspace getWorkspace() {
      return ResourcesPlugin.getWorkspace();
   }

   public static String getResourceString(String key) {
      ResourceBundle bundle = getDefault().getResourceBundle();

      try {
         return bundle.getString(key);
      } catch (MissingResourceException var2) {
         return key;
      }
   }

   public ResourceBundle getResourceBundle() {
      return this.resourceBundle;
   }

   protected void initializeDefaultPreferences(IPreferenceStore store) {
      MEditorPreferencePage.initDefaults(store);
   }
}
