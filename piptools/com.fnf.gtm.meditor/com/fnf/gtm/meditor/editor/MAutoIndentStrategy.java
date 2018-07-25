package com.fnf.gtm.meditor.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;

public class MAutoIndentStrategy extends DefaultIndentLineAutoEditStrategy {
   public void customizeDocumentCommand(IDocument d, DocumentCommand c) {
      if (c.length == 0 && c.text != null && this.endsWithDelimiter(d, c.text)) {
         this.smartIndentAfterNewLine(d, c);
      } else if (")".equals(c.text)) {
         this.smartInsertAfterBracket(d, c);
      }

   }

   private boolean endsWithDelimiter(IDocument d, String txt) {
      String[] delimiters = d.getLegalLineDelimiters();

      for(int i = 0; i < delimiters.length; ++i) {
         if (txt.endsWith(delimiters[i])) {
            return true;
         }
      }

      return false;
   }

   protected int findMatchingOpenBracket(IDocument document, int line, int end, int closingBracketIncrease) throws BadLocationException {
      int start = document.getLineOffset(line);

      for(int brackcount = this.getBracketCount(document, start, end, false) - closingBracketIncrease; brackcount < 0; brackcount += this.getBracketCount(document, start, end, false)) {
         --line;
         if (line < 0) {
            return -1;
         }

         start = document.getLineOffset(line);
         end = start + document.getLineLength(line) - 1;
      }

      return line;
   }

   private int getBracketCount(IDocument document, int start, int end, boolean ignoreCloseBrackets) throws BadLocationException {
      int begin = start;
      int bracketcount = 0;

      while(begin < end) {
         char curr = document.getChar(begin);
         ++begin;
         switch(curr) {
         case '"':
         case '\'':
            begin = this.getStringEnd(document, begin, end, curr);
            break;
         case '#':
            ++begin;
            if (document.getChar(begin) == '/') {
               begin = this.getCommentEnd(document, begin, end);
            }
            break;
         case '(':
            ++bracketcount;
            ignoreCloseBrackets = false;
            break;
         case ')':
            if (!ignoreCloseBrackets) {
               --bracketcount;
            }
            break;
         case ';':
            begin = end;
         }
      }

      return bracketcount;
   }

   private int getCommentEnd(IDocument document, int position, int end) throws BadLocationException {
      int currentPosition = position;

      char curr;
      do {
         if (currentPosition >= end) {
            return end;
         }

         curr = document.getChar(currentPosition);
         ++currentPosition;
      } while(curr != '/' || currentPosition >= end || document.getChar(currentPosition) != '#');

      return currentPosition + 1;
   }

   protected String getIndentOfLine(IDocument document, int line) throws BadLocationException {
      if (line > -1) {
         int start = document.getLineOffset(line);
         int end = start + document.getLineLength(line) - 1;
         int whiteend = this.findEndOfWhiteSpace(document, start, end);
         return document.get(start, whiteend - start);
      } else {
         return "";
      }
   }

   private int getStringEnd(IDocument document, int position, int end, char character) throws BadLocationException {
      int currentPosition = position;

      while(currentPosition < end) {
         char currentCharacter = document.getChar(currentPosition);
         ++currentPosition;
         if (currentCharacter == '\\') {
            ++currentPosition;
         } else if (currentCharacter == character) {
            return currentPosition;
         }
      }

      return end;
   }

   protected void smartIndentAfterNewLine(IDocument document, DocumentCommand command) {
      int docLength = document.getLength();
      if (command.offset != -1 && docLength != 0) {
         try {
            int p = command.offset == docLength ? command.offset - 1 : command.offset;
            int line = document.getLineOfOffset(p);
            StringBuffer buf = new StringBuffer(command.text);
            int start;
            if (command.offset < docLength && document.getChar(command.offset) == ')') {
               start = this.findMatchingOpenBracket(document, line, command.offset, 0);
               if (start == -1) {
                  start = line;
               }

               buf.append(this.getIndentOfLine(document, start));
            } else {
               start = document.getLineOffset(line);
               int whiteend = this.findEndOfWhiteSpace(document, start, command.offset);
               int bracketCount = this.getBracketCount(document, start, command.offset, true);
               if (bracketCount >= 0) {
                  buf.append(document.get(start, whiteend - start));
                  if (this.getBracketCount(document, start, command.offset, true) > 0) {
                     buf.append('\t');
                  }
               }
            }

            command.text = buf.toString();
         } catch (BadLocationException var10) {
            ;
         }

      }
   }

   private String getOneIndentLevel() {
      return String.valueOf('\t');
   }

   protected void smartInsertAfterBracket(IDocument document, DocumentCommand command) {
      if (command.offset != -1 && document.getLength() != 0) {
         try {
            int p = command.offset == document.getLength() ? command.offset - 1 : command.offset;
            int line = document.getLineOfOffset(p);
            int start = document.getLineOffset(line);
            int whiteend = this.findEndOfWhiteSpace(document, start, command.offset);
            if (whiteend == command.offset) {
               int indLine = this.findMatchingOpenBracket(document, line, command.offset, 1);
               if (indLine != -1 && indLine != line) {
                  StringBuffer replaceText = new StringBuffer(this.getIndentOfLine(document, indLine));
                  replaceText.append(document.get(whiteend, command.offset - whiteend));
                  replaceText.append(command.text);
                  command.length = command.offset - start;
                  command.offset = start;
                  command.text = replaceText.toString();
               }
            }
         } catch (BadLocationException var9) {
            ;
         }

      }
   }
}
