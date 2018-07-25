package com.sanchez.profile.schema.schemaeditor.wizards;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class EntryTableSorter extends ViewerSorter {
   private int criteria;

   public EntryTableSorter(int criteria) {
      this.criteria = criteria;
   }

   public int compare(Viewer viewer, Object o1, Object o2) {
      EntityTableEntry entry1 = (EntityTableEntry)o1;
      EntityTableEntry entry2 = (EntityTableEntry)o2;
      String string1;
      String string2;
      int result;
      switch(this.criteria) {
      case 0:
         return this.collator.compare(entry1.getAttributeName(), entry2.getAttributeName());
      case 1:
         return this.collator.compare(entry1.getSubScriptLiteral(), entry2.getSubScriptLiteral());
      case 2:
         string1 = entry1.getFieldPosition();
         string2 = entry2.getFieldPosition();
         if (string1.equals("")) {
            string1 = "0";
         }

         if (string2.equals("")) {
            string2 = "0";
         }

         result = Integer.parseInt(string1) - Integer.parseInt(string2);
         return result < 0 ? -1 : (result > 0 ? 1 : 0);
      case 3:
         return this.collator.compare(entry1.getDataType(), entry2.getDataType());
      case 4:
         return this.collator.compare((new Boolean(entry1.isRequired())).toString(), (new Boolean(entry2.isRequired())).toString());
      case 5:
         string1 = entry1.getLength();
         string2 = entry2.getLength();
         if (string1.equals("")) {
            string1 = "0";
         }

         if (string2.equals("")) {
            string2 = "0";
         }

         result = Integer.parseInt(string1) - Integer.parseInt(string2);
         result = result < 0 ? -1 : (result > 0 ? 1 : 0);
         return result;
      case 6:
         string1 = entry1.getDecimalPrecision();
         string2 = entry2.getDecimalPrecision();
         if (string1.equals("")) {
            string1 = "0";
         }

         if (string2.equals("")) {
            string2 = "0";
         }

         result = Integer.parseInt(string1) - Integer.parseInt(string2);
         result = result < 0 ? -1 : (result > 0 ? 1 : 0);
         return result;
      case 7:
         return this.collator.compare(entry1.getComputedExpression(), entry2.getComputedExpression());
      case 8:
         return this.collator.compare(entry1.getDescription(), entry2.getDescription());
      default:
         return 0;
      }
   }
}
