package com.fnf.gtm.meditor.editor;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageUtil {
   private static final String RESOURCE_BUNDLE = "com.fnf.gtm.meditor.editor.messages";
   private static ResourceBundle fgResourceBundle = ResourceBundle.getBundle("com.fnf.gtm.meditor.editor.messages");

   public static String format(String key, Object[] args) {
      return MessageFormat.format(getString(key), args);
   }

   public static String getString(String key) {
      try {
         return fgResourceBundle.getString(key);
      } catch (MissingResourceException var1) {
         return "!" + key + "!";
      }
   }
}
