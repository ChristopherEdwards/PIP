package com.fnf.gtm.meditor.editor.utils;

import org.eclipse.jface.text.rules.IWordDetector;

public class MWordDetector implements IWordDetector {
   public boolean isWordPart(char character) {
      return Character.isJavaIdentifierPart(character);
   }

   public boolean isWordStart(char character) {
      return Character.isJavaIdentifierStart(character);
   }
}
