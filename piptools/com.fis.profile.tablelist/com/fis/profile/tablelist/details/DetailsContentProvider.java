package com.fis.profile.tablelist.details;

import java.util.ArrayList;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class DetailsContentProvider implements IStructuredContentProvider {
   public Object[] getElements(Object input) {
      return input instanceof ArrayList ? ((ArrayList)input).toArray() : (Object[])input;
   }

   public void dispose() {
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
   }
}
