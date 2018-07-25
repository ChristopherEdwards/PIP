package com.fnf.studio;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class StudioPlugin extends AbstractUIPlugin {
   private static StudioPlugin plugin;
   private ResourceBundle resourceBundle;
   static String PLUGINID = "com.fnf.studio.StudioPlugin";

   public StudioPlugin() {
      plugin = this;
   }

   public static StudioPlugin getDefault() {
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
      try {
         this.resourceBundle = ResourceBundle.getBundle("com.fnf.studio.StudioPluginResources");
      } catch (MissingResourceException var1) {
         this.resourceBundle = null;
      }

      return this.resourceBundle;
   }

   public static ImageDescriptor getImageDescriptor(String name) {
      String iconPath = "icons/";
      return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGINID, iconPath + name);
   }
}
