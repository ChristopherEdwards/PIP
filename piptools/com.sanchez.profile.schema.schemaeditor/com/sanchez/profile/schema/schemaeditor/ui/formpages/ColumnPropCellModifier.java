package com.sanchez.profile.schema.schemaeditor.ui.formpages;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellModifier;

public class ColumnPropCellModifier implements ICellModifier {
   MasterDetailedTree tree;
   private HashMap map = new HashMap();

   public ColumnPropCellModifier(MasterDetailedTree tree) {
      this.tree = tree;
      this.map.put("Description", "DES");
      this.map.put("Default Label", "RHD");
      this.map.put("Type", "TYP");
      this.map.put("Required", "REQ");
      this.map.put("Length", "LEN");
      this.map.put("Display Size", "SIZ");
      this.map.put("Decimal Precision", "DEC");
      this.map.put("Null is Zero", "NULLIND");
      this.map.put("Table Lookup", "TBL");
      this.map.put("Mask", "PTN");
      this.map.put("Subscript", "NOD");
      this.map.put("Position", "POS");
   }

   public boolean canModify(Object element, String property) {
      return true;
   }

   public void modify(Object element, String property, Object value) {
   }

   public Object getValue(Object element, String property) {
      try {
         String value = element.toString().split(",")[1].toString();
         return value;
      } catch (Exception var4) {
         return "";
      }
   }

   private void updateValue(String field, String newValue) {
      String env = this.tree.connProp.getEnv();
      String userID = this.tree.connProp.getUserID();
      String pwd = this.tree.connProp.getPwd();
      String server = this.tree.connProp.getServer();
      String port = this.tree.connProp.getPort();
      String ibsServer = this.tree.connProp.getIbsServer();
      Connection cn = this.tree.hostConnection.getConnection(env);
      if (cn == null) {
         this.tree.hostConnection.setConnection(env, userID, pwd, server, port, ibsServer);
         cn = this.tree.hostConnection.getConnection(env);
      }

      if (cn != null) {
         this.runUpdate(cn, field, newValue);
      } else {
         MessageDialog.openWarning(this.tree.parent.getShell(), "Server cannot be connected!", "Please check the setting in the project's properties page!");
      }

   }

   private boolean runUpdate(Connection con, String field, String newValue) {
      new String("");

      try {
         if (this.tree.tableName.equals("")) {
            return false;
         } else {
            String selectStatement = "Update DBTBL1D SET " + this.map.get(field) + "='" + newValue + "' WHERE FID = '" + this.tree.tableName.toUpperCase() + "' " + "AND DI = '" + this.tree.selectedFieldLabel + "'";
            Statement stmt = con.createStatement();
            stmt.execute(selectStatement);
            return true;
         }
      } catch (Exception var6) {
         var6.printStackTrace();
         return false;
      }
   }
}
