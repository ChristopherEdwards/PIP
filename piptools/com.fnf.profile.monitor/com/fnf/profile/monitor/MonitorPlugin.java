package com.fnf.profile.monitor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class MonitorPlugin extends AbstractUIPlugin {
   public static final String PLUGIN_ID = "com.fnf.profile.monitor";
   private static MonitorPlugin plugin;

   public MonitorPlugin() {
      plugin = this;
   }

   public void start(BundleContext context) throws Exception {
      super.start(context);
   }

   public void stop(BundleContext context) throws Exception {
      plugin = null;
      super.stop(context);
   }

   public static MonitorPlugin getDefault() {
      return plugin;
   }

   public static ImageDescriptor getImageDescriptor(String path) {
      return imageDescriptorFromPlugin("com.fnf.profile.monitor", path);
   }
}
