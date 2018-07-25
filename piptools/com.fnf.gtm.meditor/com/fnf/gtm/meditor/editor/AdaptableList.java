package com.fnf.gtm.meditor.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class AdaptableList implements IWorkbenchAdapter, IAdaptable {
   protected List children = new ArrayList();

   public AdaptableList() {
   }

   public AdaptableList(IAdaptable[] newChildren) {
      for(int i = 0; i < newChildren.length; ++i) {
         this.children.add(newChildren[i]);
      }

   }

   public AdaptableList add(Iterator iterator) {
      while(iterator.hasNext()) {
         this.add((IAdaptable)iterator.next());
      }

      return this;
   }

   public AdaptableList add(IAdaptable adaptable) {
      this.children.add(adaptable);
      return this;
   }

   public Object getAdapter(Class adapter) {
      return adapter == IWorkbenchAdapter.class ? this : null;
   }

   public Object[] getChildren() {
      return this.children.toArray();
   }

   public Object[] getChildren(Object o) {
      return this.children.toArray();
   }

   public ImageDescriptor getImageDescriptor(Object object) {
      return null;
   }

   public String getLabel(Object object) {
      return object == null ? "" : object.toString();
   }

   public Object getParent(Object object) {
      return null;
   }

   public void remove(IAdaptable adaptable) {
      this.children.remove(adaptable);
   }

   public int size() {
      return this.children.size();
   }
}
