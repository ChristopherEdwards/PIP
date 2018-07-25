package com.fis.workbench.console;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceBoundle {
   private static final String BUNDLE_NAME = "com.fis.workbench.console.messages";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("com.fis.workbench.console.messages");

   public static String getString(String key) {
      try {
         return RESOURCE_BUNDLE.getString(key);
      } catch (MissingResourceException var1) {
         return '!' + key + '!';
      }
   }

   public static String format(String key, Object[] args) {
      return MessageFormat.format(getString(key), args);
   }
}
