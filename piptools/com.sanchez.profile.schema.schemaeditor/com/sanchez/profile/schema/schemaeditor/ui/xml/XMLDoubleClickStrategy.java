package com.sanchez.profile.schema.schemaeditor.ui.xml;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

public class XMLDoubleClickStrategy implements ITextDoubleClickStrategy {
   protected ITextViewer fText;

   public void doubleClicked(ITextViewer part) {
      int pos = part.getSelectedRange().x;
      if (pos >= 0) {
         this.fText = part;
         if (!this.selectComment(pos)) {
            this.selectWord(pos);
         }

      }
   }

   protected boolean selectComment(int caretPos) {
      IDocument doc = this.fText.getDocument();

      try {
         int pos = caretPos;
         char c = ' ';

         while(pos >= 0) {
            c = doc.getChar(pos);
            if (c == '\\') {
               pos -= 2;
            } else {
               if (c == '\r' || c == '"') {
                  break;
               }

               --pos;
            }
         }

         if (c != '"') {
            return false;
         } else {
            int startPos = pos;
            pos = caretPos;
            int length = doc.getLength();

            for(c = ' '; pos < length; ++pos) {
               c = doc.getChar(pos);
               if (c == '\r' || c == '"') {
                  break;
               }
            }

            if (c != '"') {
               return false;
            } else {
               int offset = startPos + 1;
               int len = pos - offset;
               this.fText.setSelectedRange(offset, len);
               return true;
            }
         }
      } catch (BadLocationException var10) {
         return false;
      }
   }

   protected boolean selectWord(int caretPos) {
      IDocument doc = this.fText.getDocument();

      try {
         int pos;
         char c;
         for(pos = caretPos; pos >= 0; --pos) {
            c = doc.getChar(pos);
            if (!Character.isJavaIdentifierPart(c)) {
               break;
            }
         }

         int startPos = pos;
         pos = caretPos;

         for(int length = doc.getLength(); pos < length; ++pos) {
            c = doc.getChar(pos);
            if (!Character.isJavaIdentifierPart(c)) {
               break;
            }
         }

         this.selectRange(startPos, pos);
         return true;
      } catch (BadLocationException var8) {
         return false;
      }
   }

   private void selectRange(int startPos, int stopPos) {
      int offset = startPos + 1;
      int length = stopPos - offset;
      this.fText.setSelectedRange(offset, length);
   }
}
