package com.fnf.ui.editor.simplexml;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class SimplexmlPlugin extends AbstractUIPlugin {
   public static final String PLUGIN_ID = "com.fnf.ui.editor.simplexml";
   private static SimplexmlPlugin plugin;

   public SimplexmlPlugin() {
      plugin = this;
   }

   public void start(BundleContext context) throws Exception {
      super.start(context);
   }

   public void stop(BundleContext context) throws Exception {
      plugin = null;
      super.stop(context);
   }

   public static SimplexmlPlugin getDefault() {
      return plugin;
   }

   public static ImageDescriptor getImageDescriptor(String path) {
      return imageDescriptorFromPlugin("com.fnf.ui.editor.simplexml", path);
   }
}
