package com.sanchez.profile.schema.schemaeditor.wizards;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

public class EntityCellModifier implements ICellModifier {
   EntityPage4 entityPage4;
   EntityAttributeList attributeList;
   WizardsUtils wizardsUtils = new WizardsUtils();

   public EntityCellModifier(EntityPage4 entityPage4) {
      this.entityPage4 = entityPage4;
      this.attributeList = entityPage4.getAttributeEntry();
   }

   public boolean canModify(Object element, String property) {
      EntityTableEntry tableEntry = (EntityTableEntry)element;
      if (!this.wizardsUtils.isNumber(tableEntry.getAttributeName()) && !tableEntry.getAttributeName().startsWith("\"")) {
         if (!this.wizardsUtils.isInString(this.entityPage4.keys, tableEntry.getAttributeName()) || !property.equals(this.entityPage4.getColumnNames().get(1)) && !property.equals(this.entityPage4.getColumnNames().get(2)) && !property.equals(this.entityPage4.getColumnNames().get(4))) {
            this.entityPage4.updateStatus((String)null);
            return true;
         } else {
            this.entityPage4.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.key.readonly.message"));
            return false;
         }
      } else {
         this.entityPage4.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.literalKey.message"));
         return false;
      }
   }

   public Object getValue(Object element, String property) {
      int columnIndex = this.entityPage4.getColumnNames().indexOf(property);
      Object result = null;
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
         String stringValue = tableEntry.getDataType();
         String[] choices = this.entityPage4.getChoices(property);

         int i;
         for(i = choices.length - 1; !stringValue.equals(choices[i]) && i > 0; --i) {
            ;
         }

         result = new Integer(i);
         break;
      case 4:
         result = new Boolean(tableEntry.isRequired());
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
         break;
      default:
         result = "";
      }

      return result;
   }

   public void modify(Object element, String property, Object value) {
      this.entityPage4.updateStatus((String)null);
      int columnIndex = this.entityPage4.getColumnNames().indexOf(property);
      TableItem item = (TableItem)element;
      EntityTableEntry tableEntry = (EntityTableEntry)item.getData();
      String valueString;
      switch(columnIndex) {
      case 0:
         valueString = ((String)value).trim();
         tableEntry.setAttributeName(valueString.toUpperCase());
         break;
      case 1:
         if (!this.wizardsUtils.isInString(this.entityPage4.keys, tableEntry.getAttributeName())) {
            valueString = ((String)value).trim();
            tableEntry.setSubScriptLiteral(valueString);
         }
         break;
      case 2:
         if (!this.wizardsUtils.isInString(this.entityPage4.keys, tableEntry.getAttributeName())) {
            valueString = ((String)value).trim();
            tableEntry.setFieldPosition(valueString);
         }
         break;
      case 3:
         try {
            int index = (Integer)value;
            if (index < 0) {
               return;
            }
         } catch (Exception var11) {
            return;
         }

         valueString = this.entityPage4.getChoices(property)[(Integer)value].trim();
         if (!tableEntry.getDataType().equals(valueString)) {
            tableEntry.setDataType(valueString);
         }

         String tag = property;
         if (this.entityPage4.map.containsKey(property)) {
            tag = this.entityPage4.map.get(property).toString();
         }

         WizardsUtils wizardsUtils = new WizardsUtils();
         String defaultLength = wizardsUtils.getCellComboDefaultLength(this.entityPage4.help, tag, valueString);
         tableEntry.setLength(defaultLength);
         break;
      case 4:
         if (!this.wizardsUtils.isInString(this.entityPage4.keys, tableEntry.getAttributeName())) {
            tableEntry.setRequired((Boolean)value);
         }
         break;
      case 5:
         valueString = ((String)value).trim();
         tableEntry.setLength(valueString);
         break;
      case 6:
         valueString = ((String)value).trim();
         tableEntry.setDecimalPrecision(valueString);
         break;
      case 7:
         valueString = ((String)value).trim();
         if (tableEntry.getAttributeName().indexOf("_") == -1 && tableEntry.getAttributeName().length() <= 8) {
            tableEntry.setComputedExpression(valueString);
         } else if (tableEntry.getAttributeName().indexOf("_") != -1) {
            this.entityPage4.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.columnName.message1"));
         } else if (tableEntry.getAttributeName().length() > 8) {
            this.entityPage4.updateStatus(SchemaEditorPlugin.getResourceString("wizard.entity.columnName.message2"));
         }
         break;
      case 8:
         valueString = ((String)value).trim();
         tableEntry.setDescription(valueString);
      }

      this.entityPage4.getAttributeEntry().attributeChanged(tableEntry);
      this.entityPage4.tableViewer.refresh();
   }
}
