package com.sanchez.profile.schema.schemaeditor.ui;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.EditablePage;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Entity;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class EntityRules {
   public static boolean returnCode = true;
   public static String returnMessage = "";

   public static void validate(EditablePage page, Entity base, EntryBase entry, String value) {
      returnMessage = "";
      returnCode = true;
      if (entry.getTag().equalsIgnoreCase("LocalArrayName")) {
         rule2(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("EntityType")) {
         rule3(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("Filer")) {
         rule4(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("PrimaryKeys")) {
         rule5(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("NetworkLocation")) {
         rule6(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("EnableLogging")) {
         rule7(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("RecordType")) {
         rule8(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("")) {
         rule9(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("Query")) {
         rule10(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("CreateDate")) {
         rule11(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("CreateTime")) {
         rule12(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("Global")) {
         rule8Sup(base, value);
      }

      if (entry.getTag().equalsIgnoreCase("AttributeProtectionRoutine")) {
         rule3Sup(base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("Filer")) {
         rule3Sup(base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("Global")) {
         rule3Sup2(base, entry, value);
      }

   }

   private static void rule12(EditablePage page, Entity base, EntryBase entry, String value) {
   }

   private static void rule11(EditablePage page, Entity base, EntryBase entry, String value) {
   }

   private static void rule10(EditablePage page, Entity base, EntryBase entry, String QueryValue) {
      if (QueryValue.indexOf("*") != -1 || QueryValue.indexOf("<<") != -1 || QueryValue.indexOf(">>") != -1) {
         returnMessage = SchemaEditorPlugin.getResourceString("entity.rule10.message1");
         returnCode = false;
      }

   }

   private static String rule10(Entity base, String QueryValue) {
      return QueryValue.indexOf("*") == -1 && QueryValue.indexOf("<<") == -1 && QueryValue.indexOf(">>") == -1 ? "" : SchemaEditorPlugin.getResourceString("entity.rule10.message1");
   }

   private static void rule9(EditablePage page, Entity base, EntryBase entry, String value) {
   }

   private static void rule8(EditablePage page, Entity base, EntryBase entry, String recordTypeValue) {
      EntryBase global = base.getGlobal();
      if (recordTypeValue.equals("0") && !global.getValue().equals("")) {
         returnMessage = SchemaEditorPlugin.getResourceString("entity.rule8.message1");
         returnCode = false;
      }

   }

   private static void rule8Sup(Entity base, String value) {
      if (base.getRecordType().getValue().equals("0") && !value.trim().equals("")) {
         returnMessage = SchemaEditorPlugin.getResourceString("entity.rule8Sup.message1");
         returnCode = false;
      }

      if (!base.getEntityType().getValue().equals("5") && value.equals("")) {
         returnMessage = SchemaEditorPlugin.getResourceString("entity.rule8Sup.message2");
         returnCode = false;
      }

   }

   private static String rule8(Entity base, String recordTypeValue) {
      EntryBase global = base.getGlobal();
      return recordTypeValue.equals("0") && !global.getValue().equals("") ? SchemaEditorPlugin.getResourceString("entity.rule8.message1") : "";
   }

   private static void rule7(EditablePage page, Entity base, EntryBase entry, String enableLoggingValue) {
      EntryBase global = base.getGlobal();
      EntryBase netLoc = base.getNetworkLocation();
      if ((global.getValue().equals("CTBL") || global.getValue().equals("UTBL") || global.getValue().equals("DBTBL") || global.getValue().equals("TRN") || global.getValue().equals("DBCTL") || global.getValue().equals("STBL") || global.getValue().equals("SCATBL")) && netLoc.getValue().equals("2") && !enableLoggingValue.equals("1")) {
         returnMessage = SchemaEditorPlugin.getResourceString("entity.rule7.message1");
         returnCode = false;
      }

   }

   private static String rule7(Entity base, String enableLoggingValue) {
      EntryBase global = base.getGlobal();
      EntryBase netLoc = base.getNetworkLocation();
      return (global.getValue().equals("CTBL") || global.getValue().equals("UTBL") || global.getValue().equals("DBTBL") || global.getValue().equals("TRN") || global.getValue().equals("DBCTL") || global.getValue().equals("STBL") || global.getValue().equals("SCATBL")) && netLoc.getValue().equals("2") && !enableLoggingValue.equals("1") ? SchemaEditorPlugin.getResourceString("entity.rule7.message1") : "";
   }

   private static void rule6(EditablePage page, Entity base, EntryBase entry, String networkLocationValue) {
      EntryBase fileType = base.getEntityType();
      EntryBase global = base.getGlobal();
      if ((global.getValue().equals("CTBL") || global.getValue().equals("UTBL") || global.getValue().equals("DBTBL1") || global.getValue().equals("DBTBL1D") || global.getValue().equals("TRN") || global.getValue().equals("DBCTL") || global.getValue().equals("STBL") || global.getValue().equals("SCATBL")) && !fileType.getValue().equals("5") && !networkLocationValue.equals("2")) {
         returnMessage = SchemaEditorPlugin.getResourceString("entity.rule6.message1");
         returnCode = false;
      }

   }

   private static String rule6(Entity base, String networkLocationValue) {
      EntryBase fileType = base.getEntityType();
      EntryBase global = base.getGlobal();
      return (global.getValue().equals("CTBL") || global.getValue().equals("UTBL") || global.getValue().equals("DBTBL1") || global.getValue().equals("DBTBL1D") || global.getValue().equals("TRN") || global.getValue().equals("DBCTL") || global.getValue().equals("STBL") || global.getValue().equals("SCATBL")) && !fileType.getValue().equals("5") && !networkLocationValue.equals("2") ? SchemaEditorPlugin.getResourceString("entity.rule6.message1") : "";
   }

   private static void rule5(EditablePage page, Entity base, EntryBase entry, String PrimaryKeysValue) {
      if (PrimaryKeysValue.indexOf("$J") != -1) {
         returnMessage = SchemaEditorPlugin.getResourceString("entity.rule5.message1");
         returnCode = false;
      } else if (PrimaryKeysValue.endsWith("\"")) {
         returnMessage = SchemaEditorPlugin.getResourceString("entity.rule5.message2");
         returnCode = false;
      }

   }

   private static String rule5(Entity base, String PrimaryKeysValue) {
      if (PrimaryKeysValue.indexOf("$J") != -1) {
         return SchemaEditorPlugin.getResourceString("entity.rule5.message1");
      } else {
         return PrimaryKeysValue.endsWith("\"") ? SchemaEditorPlugin.getResourceString("entity.rule5.message2") : "";
      }
   }

   private static void rule4(EditablePage page, Entity base, EntryBase entry, String filerValue) {
      EntryBase filer = base.getFiler();
      String oldFilerValue = filer.getValue();
      if (filerValue.trim().length() != 0 && !filerValue.equalsIgnoreCase(oldFilerValue)) {
         returnMessage = validateName("filer", filerValue);
         if (!returnMessage.equals("")) {
            returnCode = false;
         }

         if (searchRoutineName("filer", filerValue)) {
            returnMessage = SchemaEditorPlugin.getResourceString("entity.rule4.message2");
            returnCode = false;
         }
      }

   }

   private static String rule4(Entity base, String filerValue) {
      EntryBase filer = base.getFiler();
      String oldFilerValue = filer.getValue();
      if (filerValue.trim().length() != 0 && !filerValue.equalsIgnoreCase(oldFilerValue)) {
         returnMessage = validateName("filer", filerValue);
         if (!returnMessage.equals("")) {
            return SchemaEditorPlugin.getResourceString("entity.rule4.message1");
         }

         if (searchRoutineName("filer", filerValue)) {
            return SchemaEditorPlugin.getResourceString("entity.rule4.message2");
         }
      }

      return "";
   }

   private static boolean searchRoutineName(String field, String value) {
      return false;
   }

   private static String validateName(String field, String value) {
      if (field.equalsIgnoreCase("filer")) {
         if (value.length() == 0) {
            return "";
         }

         if (!Character.isLetter(value.charAt(0))) {
            return SchemaEditorPlugin.getResourceString("entity.validateName.message1");
         }

         for(int i = 1; i < value.length(); ++i) {
            if (!Character.isLetter(value.charAt(i)) && !Character.isDigit(value.charAt(i))) {
               return SchemaEditorPlugin.getResourceString("entity.validateName.message2");
            }
         }
      }

      return "";
   }

   private static void rule3(EditablePage page, Entity base, EntryBase entry, String fileTypeValue) {
      EntryBase udacc = base.getAttributeProtectionRoutine();
      EntryBase udfile = base.getFiler();
      EntryBase global = base.getGlobal();
      EntryBase netLoc = base.getNetworkLocation();
      EntryBase log = base.getEnableLogging();
      EntryBase fsn = base.getLocalArrayName();
      if (fileTypeValue.equals("5")) {
         if (!udacc.getValue().equals("") || !udfile.getValue().equals("")) {
            MessageDialog.openInformation((Shell)null, "Information", SchemaEditorPlugin.getResourceString("entity.rule3.information1"));
            udacc.setValue("");
            udfile.setValue("");
         }

         returnCode = true;
      } else if (global.getValue().equals("")) {
         returnMessage = SchemaEditorPlugin.getResourceString("entity.rule3.message1");
         returnCode = false;
      } else if (fsn.getValue().equals("")) {
         returnMessage = SchemaEditorPlugin.getResourceString("entity.rule3.message2");
         returnCode = false;
      } else if (global.getValue().equals("CTBL") || global.getValue().equals("UTBL") || global.getValue().equals("DBTBL") || global.getValue().equals("TRN") || global.getValue().equals("DBCTL") || global.getValue().equals("STBL") || global.getValue().equals("SCATBL")) {
         MessageDialog.openInformation((Shell)null, "Information", SchemaEditorPlugin.getResourceString("entity.rule3.information2"));
         netLoc.setValue("2");
         log.setValue("1");
         returnCode = true;
      }

   }

   private static void rule3Sup2(Entity base, EntryBase entry, String value) {
      if (!base.getEntityType().getValue().equals("5") && value.trim().equals("")) {
         String entryName = entry.getDisplayLabel();
         entryName = entryName.substring(0, entryName.length() - 1);
         returnMessage = entryName + " " + SchemaEditorPlugin.getResourceString("entity.rule3Sup2.message1");
         returnCode = false;
      }

   }

   private static void rule3Sup(Entity base, EntryBase entry, String value) {
      if (base.getEntityType().getValue().equals("5") && !value.trim().equals("")) {
         String entryName = entry.getDisplayLabel();
         entryName = entryName.substring(0, entryName.length() - 1);
         returnMessage = entryName + " " + SchemaEditorPlugin.getResourceString("entity.rule3Sup.message1");
         returnCode = false;
      }

   }

   private static String rule3(Entity base, String fileTypeValue) {
      EntryBase udacc = base.getAttributeProtectionRoutine();
      EntryBase udfile = base.getFiler();
      EntryBase global = base.getGlobal();
      EntryBase netLoc = base.getNetworkLocation();
      EntryBase log = base.getEnableLogging();
      EntryBase fsn = base.getLocalArrayName();
      if (fileTypeValue.equals("5")) {
         MessageDialog.openInformation((Shell)null, "Information", SchemaEditorPlugin.getResourceString("entity.rule3.information1"));
         udacc.setValue("");
         udfile.setValue("");
      } else {
         if (global.getValue().equals("")) {
            return SchemaEditorPlugin.getResourceString("entity.rule3.message1");
         }

         if (fsn.getValue().equals("")) {
            return SchemaEditorPlugin.getResourceString("entity.rule3.message2");
         }

         if (global.getValue().equals("CTBL") || global.getValue().equals("UTBL") || global.getValue().equals("DBTBL") || global.getValue().equals("TRN") || global.getValue().equals("DBCTL") || global.getValue().equals("STBL") || global.getValue().equals("SCATBL")) {
            MessageDialog.openInformation((Shell)null, "Information", SchemaEditorPlugin.getResourceString("entity.rule3.information2"));
            netLoc.setValue("2");
            log.setValue("1");
         }
      }

      return "";
   }

   private static int getIndex(Control[] children, String string) {
      int index = 0;

      for(int i = 0; i < children.length; ++i) {
         if (children[i] instanceof Label) {
            Label label = (Label)children[i];
            if (label.getText().equalsIgnoreCase(string)) {
               index = i + 1;
               break;
            }
         }
      }

      return index;
   }

   private static void rule2(EditablePage page, Entity base, EntryBase entry, String arrayNameValue) {
      if (arrayNameValue.trim().equals("")) {
         returnMessage = SchemaEditorPlugin.getResourceString("entity.rule2.message1");
         returnCode = false;
      }

   }

   private static String rule2(Entity base, String arrayNameValue) {
      if (arrayNameValue.trim().equals("")) {
         return SchemaEditorPlugin.getResourceString("entity.rule2.message1");
      } else {
         return arrayNameValue.length() > 8 ? SchemaEditorPlugin.getResourceString("entity.rule2.message2") : "";
      }
   }

   private static void rule1(EditablePage page, Entity base, EntryBase entry, String fileNameValue) {
      EntryBase fileType = base.getEntityType();
      EntryBase global = base.getGlobal();
      EntryBase netLoc = base.getNetworkLocation();
      EntryBase log = base.getEnableLogging();
      Control[] children = page.getGroup2().getContainer().getChildren();
      int indexOfFileTypeControl = getIndex(children, fileType.getDisplayLabel());
      if (fileNameValue.indexOf("_") != -1 && fileType.getValue().trim().equals("")) {
         if (indexOfFileTypeControl != 0 && children[indexOfFileTypeControl] instanceof Combo) {
            ((Combo)children[indexOfFileTypeControl]).setText("7");
            fileType.setValue("7");
         }
      } else if (global.getValue().trim().equals("")) {
         String name;
         if (fileNameValue.toUpperCase().startsWith("Z")) {
            name = fileNameValue.substring(1);
         } else {
            name = fileNameValue;
         }

         if (name.length() >= 4) {
            String first4 = name.substring(0, 4);
            if (first4.equalsIgnoreCase("UTBL") || first4.equalsIgnoreCase("STBL") || first4.equalsIgnoreCase("CTBL")) {
               global.setValue(first4);
               ((Combo)children[indexOfFileTypeControl]).setText("3");
               fileType.setValue("3");
               netLoc.setValue("2");
               log.setValue("1");
            }
         }
      }

   }
}
