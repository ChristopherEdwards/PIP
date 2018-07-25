package com.fnf.gtm.meditor.editor;

import java.util.Vector;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.views.properties.IPropertySource;

public class MarkElement implements IWorkbenchAdapter, IAdaptable {
   private String headingName;
   private IAdaptable parent;
   private int offset;
   private int numberOfLines;
   private int length;
   private Vector children;

   public MarkElement(IAdaptable parent, String heading, int offset, int length) {
      this.parent = parent;
      if (parent instanceof MarkElement) {
         ((MarkElement)parent).addChild(this);
      }

      this.headingName = heading;
      this.offset = offset;
      this.length = length;
   }

   private void addChild(MarkElement child) {
      if (this.children == null) {
         this.children = new Vector();
      }

      this.children.add(child);
   }

   public Object getAdapter(Class adapter) {
      if (adapter == IWorkbenchAdapter.class) {
         return this;
      } else {
         return adapter == IPropertySource.class ? new MarkElementProperties(this) : null;
      }
   }

   public Object[] getChildren(Object object) {
      return this.children != null ? this.children.toArray() : new Object[0];
   }

   public ImageDescriptor getImageDescriptor(Object object) {
      IWorkbenchAdapter parentElement = (IWorkbenchAdapter)this.parent.getAdapter(IWorkbenchAdapter.class);
      return parentElement != null ? parentElement.getImageDescriptor(object) : null;
   }

   public String getLabel(Object o) {
      return this.headingName;
   }

   public String getName() {
      return this.headingName;
   }

   public int getLength() {
      return this.length;
   }

   public int getNumberOfLines() {
      return this.numberOfLines;
   }

   public Object getParent(Object o) {
      return null;
   }

   public int getStart() {
      return this.offset;
   }

   public void setNumberOfLines(int newNumberOfLines) {
      this.numberOfLines = newNumberOfLines;
   }
}
