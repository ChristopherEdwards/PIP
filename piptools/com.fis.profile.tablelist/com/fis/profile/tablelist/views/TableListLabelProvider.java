package com.fis.profile.tablelist.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class TableListLabelProvider extends LabelProvider implements ITableLabelProvider {
   public Image getColumnImage(Object element, int columnIndex) {
      return null;
   }

   public String getColumnText(Object element, int columnIndex) {
      String[] tmpString = ((String)element).split(",");
      if (columnIndex > tmpString.length - 1) {
         return "";
      } else {
         String returnVal = tmpString[columnIndex];
         if (returnVal.indexOf("aaaaaa") != -1) {
            returnVal = returnVal.replaceAll("aaaaaa", ",");
         }

         return returnVal;
      }
   }
}
