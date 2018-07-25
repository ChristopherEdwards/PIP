package com.sanchez.profile.schema.schemaeditor.wizards;

import com.sanchez.profile.schema.schemaeditor.ui.SchemaInfo;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Attribute;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Entity;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

public class EntityAttributeList {
   private SchemaInfo help;
   private Vector attributes = new Vector();
   private Set changeListeners = new HashSet();
   private String lastKey;
   private Attribute base = new Attribute();
   private WizardsUtils wizardsUtils = new WizardsUtils();

   public EntityAttributeList() {
      this.help = new SchemaInfo(this.base.getHelpFileName());
   }

   public void initData(Entity entityBase, String keys, String columns) {
      this.lastKey = keys;
      if (keys.lastIndexOf(",") != -1) {
         this.lastKey = keys.substring(keys.lastIndexOf(",") + 1).trim();
      }

      StringTokenizer keyTokenizer = new StringTokenizer(keys, ",");
      StringTokenizer columnTokenizer = new StringTokenizer(columns, ",");
      int keyCount = keyTokenizer.countTokens();
      int columnCount = columnTokenizer.countTokens();
      this.attributes = new Vector(keyCount + columnCount);

      int i;
      String thisCol;
      for(i = 0; i < keyCount; ++i) {
         thisCol = ((String)keyTokenizer.nextElement()).trim();
         if (!this.wizardsUtils.isNumber(thisCol) && !thisCol.startsWith("\"")) {
            this.addAttribute(thisCol, (new Integer(i + 1)).toString() + "*", "", "", true, "", "", "", "");
         } else {
            this.addAttribute(thisCol, (new Integer(i + 1)).toString() + "*", "", "Numeric", true, "12", "", "", thisCol);
         }
      }

      for(i = 0; i < columnCount; ++i) {
         thisCol = ((String)columnTokenizer.nextElement()).trim();
         String subScriptLiteral = this.lastKey;
         String fieldPosition = (new Integer(i + 1)).toString();
         if (entityBase.getRecordType().getValue().equals("10")) {
            subScriptLiteral = "";
            fieldPosition = "";
         }

         this.addAttribute(thisCol, subScriptLiteral, fieldPosition, "", false, "", "", "", "");
      }

   }

   public void initData(Entity entityBase, Hashtable attributeBaseTable, String keys, String columns, String newColumns) {
      this.lastKey = keys;
      if (keys.lastIndexOf(",") != -1) {
         this.lastKey = keys.substring(keys.lastIndexOf(",") + 1).trim();
      }

      String oldColumns = keys;
      if (!columns.equals("")) {
         oldColumns = keys + "," + columns;
      }

      StringTokenizer oldColumnsTokenizer = new StringTokenizer(oldColumns, ",");
      StringTokenizer newColumnsTokenizer = new StringTokenizer(newColumns, ",");
      StringTokenizer columnsTokenizer = new StringTokenizer(columns, ",");
      int columnsCount = columnsTokenizer.countTokens();
      int oldCount = oldColumnsTokenizer.countTokens();
      int newCount = newColumnsTokenizer.countTokens();
      this.attributes = new Vector(oldCount + newCount);

      int i;
      String thisCol;
      for(i = 0; i < oldCount; ++i) {
         thisCol = ((String)oldColumnsTokenizer.nextElement()).trim();
         Attribute base = (Attribute)attributeBaseTable.get(thisCol);
         if (base != null) {
            this.addAttribute(thisCol, base.getSubScriptLiteral().getValue(), base.getFieldPosition().getValue(), this.wizardsUtils.getCellComboDisplayValue(this.help, base.getDataType().getTag(), base.getDataType().getValue()), base.getRequired().getValue().equals("1"), base.getLength().getValue(), base.getDecimalPrecision().getValue(), base.getComputedExpression().getValue(), base.getDescription().getValue());
         }
      }

      for(i = 0; i < newCount; ++i) {
         thisCol = ((String)newColumnsTokenizer.nextElement()).trim();
         String subScriptLiteral = this.lastKey;
         String fieldPosition = (new Integer(i + columnsCount + 1)).toString();
         if (entityBase.getRecordType().getValue().equals("10")) {
            subScriptLiteral = "";
            fieldPosition = "";
         }

         this.addAttribute(thisCol, subScriptLiteral, fieldPosition, "", false, "", "", "", "");
      }

   }

   public Vector getAttributes() {
      return this.attributes;
   }

   public void addAttribute(String attributeName, String SubScriptLiteral, String FieldPosition, String DataType, boolean Required, String Length, String DecimalPrecision, String ComputedExpression, String Description) {
      EntityTableEntry entityTableEntry = new EntityTableEntry(attributeName, SubScriptLiteral, FieldPosition, DataType, Required, Length, DecimalPrecision, ComputedExpression, Description);
      this.attributes.add(this.attributes.size(), entityTableEntry);
      Iterator iterator = this.changeListeners.iterator();

      while(iterator.hasNext()) {
         ((IEntityListViewer)iterator.next()).addAttribute(entityTableEntry);
      }

   }

   public void removeAttribute(EntityTableEntry entityTableEntry) {
      this.attributes.remove(entityTableEntry);
      Iterator iterator = this.changeListeners.iterator();

      while(iterator.hasNext()) {
         ((IEntityListViewer)iterator.next()).removeAttribute(entityTableEntry);
      }

   }

   public void removeAllAttributes() {
      if (this.attributes != null) {
         Iterator iter = this.attributes.iterator();

         while(iter.hasNext()) {
            EntityTableEntry element = (EntityTableEntry)iter.next();
            this.attributes.remove(element);
         }

      }
   }

   public void attributeChanged(EntityTableEntry entityTableEntry) {
      Iterator iterator = this.changeListeners.iterator();

      while(iterator.hasNext()) {
         ((IEntityListViewer)iterator.next()).updateAttribute(entityTableEntry);
      }

   }

   public void removeChangeListener(IEntityListViewer viewer) {
      this.changeListeners.remove(viewer);
   }

   public void addChangeListener(IEntityListViewer viewer) {
      this.changeListeners.add(viewer);
   }
}
