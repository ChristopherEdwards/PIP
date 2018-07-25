package com.sanchez.profile.schema.schemaeditor.wizards;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class EntityLabelProvider extends LabelProvider implements ITableLabelProvider {
   private Image uncheckedImage;
   private Image checkedImage;
   public static final String CHECKED_IMAGE = "checked";
   public static final String UNCHECKED_IMAGE = "unchecked";
   private static ImageRegistry imageRegistry = new ImageRegistry();

   static {
      imageRegistry.put("checked", SchemaEditorPlugin.getImageDescriptor("checked.gif"));
      imageRegistry.put("unchecked", SchemaEditorPlugin.getImageDescriptor("unchecked.gif"));
   }

   private Image getImage(boolean isSelected) {
      String key = isSelected ? "checked" : "unchecked";
      return imageRegistry.get(key);
   }

   public String getColumnText(Object element, int columnIndex) {
      String result = "";
      EntityTableEntry tableEntry = (EntityTableEntry)element;
      switch(columnIndex) {
      case 0:
         result = tableEntry.getAttributeName();
         break;
      case 1:
         result = tableEntry.getSubScriptLiteral();
         break;
      case 2:
         result = tableEntry.getFieldPosition();
         break;
      case 3:
         result = tableEntry.getDataType();
      case 4:
      default:
         break;
      case 5:
         result = tableEntry.getLength();
         break;
      case 6:
         result = tableEntry.getDecimalPrecision();
         break;
      case 7:
         result = tableEntry.getComputedExpression();
         break;
      case 8:
         result = tableEntry.getDescription();
      }

      return result;
   }

   public Image getColumnImage(Object element, int columnIndex) {
      return columnIndex == 4 ? this.getImage(((EntityTableEntry)element).isRequired()) : null;
   }
}
