package com.fnf.gtm.meditor.editor;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class MarkElementProperties implements IPropertySource {
   protected MarkElement element;
   protected static final String PROPERTY_LINECOUNT = "lineno";
   protected static final String PROPERTY_START = "start";
   protected static final String PROPERTY_LENGTH = "length";

   public MarkElementProperties(MarkElement element) {
      this.element = element;
   }

   public Object getEditableValue() {
      return this;
   }

   public IPropertyDescriptor[] getPropertyDescriptors() {
      IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[3];
      PropertyDescriptor descriptor = new PropertyDescriptor("lineno", "Line_count");
      propertyDescriptors[0] = descriptor;
      descriptor = new PropertyDescriptor("start", "Title_start");
      propertyDescriptors[1] = descriptor;
      descriptor = new PropertyDescriptor("length", "Title_length");
      propertyDescriptors[2] = descriptor;
      return propertyDescriptors;
   }

   public Object getPropertyValue(Object name) {
      if (name.equals("lineno")) {
         return new Integer(this.element.getNumberOfLines());
      } else if (name.equals("start")) {
         return new Integer(this.element.getStart());
      } else {
         return name.equals("length") ? new Integer(this.element.getLength()) : null;
      }
   }

   public boolean isPropertySet(Object property) {
      return false;
   }

   public void resetPropertyValue(Object property) {
   }

   public void setPropertyValue(Object name, Object value) {
   }
}
