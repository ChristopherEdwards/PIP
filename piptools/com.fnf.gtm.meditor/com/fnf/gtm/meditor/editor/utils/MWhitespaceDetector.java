package com.fnf.gtm.meditor.editor.utils;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class MWhitespaceDetector implements IWhitespaceDetector {
   public boolean isWhitespace(char c) {
      return c == ' ' || c == '\t' || c == '\n' || c == '\r';
   }
}
