package com.fis.profile.tablelist.dialogs;

import java.util.ArrayList;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

public class JournalCellModifier implements ICellModifier {
   UpdateJournal updateJournal;
   String[] items;
   String colName = "";
   String colDesc = "";
   String fieldName = "";
   ArrayList list = new ArrayList();
   String oldVal = "";

   public JournalCellModifier(UpdateJournal updateJournal, String[] items, String fieldName, ArrayList list) {
      this.updateJournal = updateJournal;
      this.items = items;
      this.fieldName = fieldName;
      this.list = list;
   }

   public boolean canModify(Object element, String property) {
      return property.equals("Map");
   }

   public Object getValue(Object element, String property) {
      this.oldVal = element.toString();
      if (property.equals("Map")) {
         try {
            String value = element.toString().split(",")[4].toString();
            this.colName = element.toString().split(",")[2].toString();
            this.colDesc = element.toString().split(",")[3].toString();
            return value;
         } catch (Exception var4) {
            return "";
         }
      } else {
         return "";
      }
   }

   public void modify(Object element, String property, Object value) {
      int valIndex = 0;
      if (this.list.contains(this.oldVal)) {
         valIndex = this.list.indexOf(this.oldVal);
      }

      TableItem tableItem = (TableItem)element;
      if (property.equals("Map")) {
         try {
            tableItem.setData("Map", value.toString());
         } catch (Exception var7) {
            return;
         }

         this.list.set(valIndex, ",," + this.colName + "," + this.colDesc + "," + tableItem.getData("Map"));

         for(int i = 0; i < this.updateJournal.updateMap.size(); ++i) {
            if (this.updateJournal.updateMap.indexOf(this.colName + ",") != -1) {
               this.updateJournal.updateMap.set(i, this.colName + ", " + this.colDesc + ", " + value.toString());
               break;
            }
         }

         this.updateJournal.updateMap.add(this.colName + "," + this.colDesc + ", " + value.toString());
      }

      this.updateJournal.viewer.setInput(this.list);
      this.updateJournal.viewer.refresh();
   }
}
