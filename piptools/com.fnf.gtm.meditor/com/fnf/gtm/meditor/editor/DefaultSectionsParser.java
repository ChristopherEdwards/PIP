package com.fnf.gtm.meditor.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.text.IDocument;

public class DefaultSectionsParser implements IMFileParser {
   protected IAdaptable getParent(Hashtable toc, String number) {
      int lastDot = number.lastIndexOf(46);
      if (lastDot < 0) {
         return null;
      } else {
         String parentNumber = number.substring(0, lastDot);
         return (IAdaptable)toc.get(parentNumber);
      }
   }

   protected IAdaptable getParent1(Hashtable toc, String number) {
      int lastDot = number.lastIndexOf(46);
      if (lastDot < 0) {
         return null;
      } else {
         String parentNumber = number.substring(0, lastDot);
         return (IAdaptable)toc.get(parentNumber);
      }
   }

   protected String getText(IDocument document) {
      try {
         InputStream in = new ByteArrayInputStream(document.get().getBytes());
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         byte[] buf = new byte[1024];

         for(int read = in.read(buf); read > 0; read = in.read(buf)) {
            out.write(buf, 0, read);
         }

         return out.toString();
      } catch (IOException var6) {
         return "";
      }
   }

   public MarkElement[] parse(IDocument document) {
      Hashtable markTable = new Hashtable(40);
      Vector topLevel = new Vector();
      String s = this.getText(document);
      int start = 0;
      int end = -1;
      int keyEnd = false;
      int lineno = 0;
      int lastlineno = 0;
      MarkElement lastme = null;
      int ix = s.indexOf(10, start);

      while(ix != -1) {
         int start = end + 1;
         end = ix = s.indexOf(10, start);
         ++lineno;
         if (ix != -1) {
            ++lineno;
            if (s.charAt(start) != ' ' && s.charAt(start) != '\r' && s.charAt(start) != '\n' && s.charAt(start) != '\t' && s.charAt(start) != '/' && s.charAt(start) != '-') {
               if (lastme != null) {
                  lastme.setNumberOfLines(lineno - lastlineno - 1);
               }

               lastlineno = lineno;
               String markName = this.parseHeading(s, start, end);
               String tmp = new String(markName.toUpperCase());
               if (tmp.indexOf(";") != -1) {
                  markName = markName.substring(0, tmp.indexOf(";"));
               }

               if (markName.indexOf(" ") != -1) {
                  markName = markName.substring(0, markName.indexOf(" "));
               }

               if (markName.indexOf("\t") != -1) {
                  markName = markName.substring(0, markName.indexOf("\t"));
               }

               if (markName.indexOf("(") != -1) {
                  markName = markName.substring(0, markName.indexOf("("));
               }

               markName = markName.trim();
               String markNumber = (new Integer(start)).toString();
               IAdaptable parent = this.getParent(markTable, markNumber);
               MarkElement me = new MarkElement(parent, markName, start, end - start);
               lastme = me;
               markTable.put(markNumber, me);
               topLevel.add(me);
            }
         }
      }

      if (lastme != null) {
         lastme.setNumberOfLines(lineno - lastlineno - 1);
      }

      MarkElement[] results = new MarkElement[topLevel.size()];
      topLevel.copyInto(results);
      return results;
   }

   private String parseHeading(String buffer, int start, int end) {
      while(Character.isWhitespace(buffer.charAt(end - 1)) && end > start) {
         --end;
      }

      return buffer.substring(start, end);
   }

   protected String parseNumber(String heading) {
      int start = 0;
      int end = heading.length();

      char c;
      do {
         c = heading.charAt(start++);
      } while((c == '.' || Character.isDigit(c)) && start < end);

      while(heading.charAt(start - 1) == '.' && start > 0) {
         --start;
      }

      return heading.substring(0, start);
   }
}
