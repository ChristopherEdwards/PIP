package com.sanchez.profile.schema.schemaeditor.ui.formpages;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class DetailTableLabelProvider extends LabelProvider implements ITableLabelProvider {
   public Image getColumnImage(Object element, int columnIndex) {
      return null;
   }

   public String getColumnText(Object element, int columnIndex) {
      String[] tmpString = ((String)element).split(",");
      return columnIndex > tmpString.length - 1 ? "" : tmpString[columnIndex];
   }
}
