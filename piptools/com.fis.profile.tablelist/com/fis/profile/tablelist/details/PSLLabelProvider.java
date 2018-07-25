package com.fis.profile.tablelist.details;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class PSLLabelProvider extends LabelProvider implements ITableLabelProvider {
   public Image getColumnImage(Object element, int columnIndex) {
      return null;
   }

   public String getColumnText(Object element, int columnIndex) {
      String[] tmpString = element.toString().split(",");
      return tmpString[columnIndex].replaceAll("aaaaaa", ",");
   }
}
