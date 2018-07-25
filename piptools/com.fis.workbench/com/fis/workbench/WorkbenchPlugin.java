package com.fis.workbench;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class WorkbenchPlugin extends AbstractUIPlugin {
   private static WorkbenchPlugin plugin;
   private ResourceBundle resourceBundle;

   public WorkbenchPlugin() {
      plugin = this;
   }

   public void start(BundleContext context) throws Exception {
      super.start(context);
   }

   public void stop(BundleContext context) throws Exception {
      super.stop(context);
      plugin = null;
   }

   public static WorkbenchPlugin getDefault() {
      return plugin;
   }

   public static String getResourceString(String key) {
      ResourceBundle bundle = getDefault().getResourceBundle();

      try {
         return bundle != null ? bundle.getString(key) : key;
      } catch (MissingResourceException var2) {
         return key;
      }
   }

   public ResourceBundle getResourceBundle() {
      try {
         if (this.resourceBundle == null) {
            this.resourceBundle = ResourceBundle.getBundle("com.fis.workbench.WorkbenchPluginResources");
         }
      } catch (MissingResourceException var1) {
         this.resourceBundle = null;
      }

      return this.resourceBundle;
   }

   public static ImageDescriptor getImageDescriptor(String path) {
      return AbstractUIPlugin.imageDescriptorFromPlugin("com.fis.workbench", path);
   }

   public static String getPluginId() {
      return getDefault().getBundle().getSymbolicName();
   }

   public static void log(Throwable e) {
      log((IStatus)(new Status(4, getPluginId(), 4, "Error", e)));
   }

   public static void log(IStatus status) {
      getDefault().getLog().log(status);
   }
}
