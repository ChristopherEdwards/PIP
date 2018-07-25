package com.fnf.gtm.meditor.editor;

import com.fnf.gtm.meditor.editor.utils.ColorManager;
import org.eclipse.jface.text.rules.RuleBasedScanner;

public class MEditorEnvironment {
   private static ColorManager fgColorProvider;
   private static MScanner fgCodeScanner;
   private static int fgRefCount = 0;

   public static void connect(Object client) {
      if (++fgRefCount == 1) {
         fgColorProvider = new ColorManager();
         fgCodeScanner = new MScanner(fgColorProvider, (MEditor)null);
      }

   }

   public static void disconnect(Object client) {
      if (--fgRefCount == 0) {
         fgCodeScanner = null;
         fgColorProvider.dispose();
         fgColorProvider = null;
      }

   }

   public static RuleBasedScanner getJavaCodeScanner() {
      return fgCodeScanner;
   }

   public static ColorManager getGtmColorProvider() {
      return fgColorProvider;
   }

   public static RuleBasedScanner getGtmScanner() {
      return fgCodeScanner;
   }
}
