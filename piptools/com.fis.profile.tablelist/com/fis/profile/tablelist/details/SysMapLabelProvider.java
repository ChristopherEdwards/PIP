package com.fis.profile.tablelist.details;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class SysMapLabelProvider extends LabelProvider implements ITableLabelProvider {
   public Image getColumnImage(Object element, int columnIndex) {
      return null;
   }

   public String getColumnText(Object element, int columnIndex) {
      String returnValue = "";
      String elementString = (String)element;
      String[] tmpString = elementString.split(",");

      try {
         returnValue = tmpString[columnIndex];
      } catch (Exception var6) {
         returnValue = "";
      }

      return returnValue;
   }
}
