package com.sanchez.profile.schema.schemaeditor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class SchemaEditorPlugin extends AbstractUIPlugin {
   private static SchemaEditorPlugin plugin;
   private ResourceBundle resourceBundle;
   public static ArrayList list = new ArrayList();
   public static HashMap map = new HashMap();
   public static ArrayList colList = new ArrayList();
   public static ArrayList tableList = new ArrayList();

   public SchemaEditorPlugin(IPluginDescriptor descriptor) {
      super(descriptor);
      plugin = this;

      try {
         this.resourceBundle = ResourceBundle.getBundle("com.sanchez.profile.schema.schemaeditor.resources");
      } catch (MissingResourceException var2) {
         this.resourceBundle = null;
      }

   }

   public static SchemaEditorPlugin getDefault() {
      return plugin;
   }

   public static String getResourceString(String key) {
      ResourceBundle bundle = getDefault().getResourceBundle();

      try {
         return bundle.getString(key);
      } catch (MissingResourceException var2) {
         return null;
      }
   }

   public ResourceBundle getResourceBundle() {
      return this.resourceBundle;
   }

   public static ImageDescriptor getImageDescriptor(String name) {
      String iconPath = "icons/";

      try {
         URL installURL = getDefault().getDescriptor().getInstallURL();
         URL url = new URL(installURL, iconPath + name);
         return ImageDescriptor.createFromURL(url);
      } catch (MalformedURLException var4) {
         return ImageDescriptor.getMissingImageDescriptor();
      }
   }
}
