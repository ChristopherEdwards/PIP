package com.fnf.gtm.meditor.editor;

import org.eclipse.jface.viewers.ViewerSorter;

public class MElementSorter extends ViewerSorter {
   public boolean isSorterProperty(Object element, Object property) {
      return true;
   }
}
