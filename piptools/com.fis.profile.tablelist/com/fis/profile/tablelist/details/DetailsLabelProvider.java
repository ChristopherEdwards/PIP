package com.fis.profile.tablelist.details;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class DetailsLabelProvider extends LabelProvider implements ITableLabelProvider {
   public Image getColumnImage(Object element, int columnIndex) {
      return null;
   }

   public String getColumnText(Object element, int columnIndex) {
      String[] tmpString = new String[2];
      String elementString = (String)element;
      int index = elementString.indexOf(",");
      if (index == -1) {
         tmpString[0] = elementString;
         tmpString[1] = "";
      } else {
         tmpString[0] = elementString.substring(0, index);
         tmpString[1] = elementString.substring(index + 1, elementString.length());
      }

      return tmpString[columnIndex].replaceAll("aaaaaa", ",");
   }
}
