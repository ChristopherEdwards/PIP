package com.sanchez.profile.schema.schemaeditor.ui;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Attribute;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.EditablePage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AttributeRules {
   public static boolean returnCode;
   public static String returnMessage;

   public static void validate(EditablePage page, Attribute base, EntryBase entry, String value) {
      returnMessage = "";
      returnCode = true;
      if (entry.getTag().equalsIgnoreCase("DataType")) {
         rule4(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("DataType")) {
         rule5Sup(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("Length")) {
         rule5(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("Description")) {
         rule6(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("LookupEntity")) {
         rule7(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("mPatternMatch")) {
         rule8(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("Required")) {
         rule12(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("DataType")) {
         rule12Sup(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("DisplaySize")) {
         rule13(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("Length")) {
         rule13Sup(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("ComputedExpression")) {
         rule14(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("SubScriptLiteral")) {
         rule14Sup(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("FieldPosition")) {
         rule14Sup(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("Required")) {
         rule15Sup(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("SubScriptLiteral")) {
         rule15(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("IsSerialValue")) {
         rule16(page, base, entry, value);
      }

      if (entry.getTag().equalsIgnoreCase("DataType")) {
         rule16Sup(page, base, entry, value);
      }

   }

   private static void rule16Sup(EditablePage page, Attribute base, EntryBase entry, String dataTypeValue) {
      if (!dataTypeValue.equalsIgnoreCase("D")) {
         EntryBase isSerialValue = base.getIsSerialValue();
         Control[] children = page.getGroup2().getContainer().getChildren();
         int indexOfRequiredControl = getIndex(children, isSerialValue.getDisplayLabel()) - 2;
         if (indexOfRequiredControl != 0 && children[indexOfRequiredControl] instanceof Button) {
            boolean selection = ((Button)children[indexOfRequiredControl]).getSelection();
            if (selection) {
               ((Button)children[indexOfRequiredControl]).setSelection(false);
               isSerialValue.setValue("0");
               MessageDialog.openInformation((Shell)null, "Information", SchemaEditorPlugin.getResourceString("attribute.rule16Sup.information1"));
               returnCode = true;
            }
         }
      }

   }

   private static void rule16(EditablePage page, Attribute base, EntryBase entry, String isSerialValue) {
      EntryBase dataType = base.getDataType();
      Control[] children = page.getGroup3().getContainer().getChildren();
      int indexOfDataTypeControl = getIndex(children, dataType.getDisplayLabel());
      if (indexOfDataTypeControl != 0 && children[indexOfDataTypeControl] instanceof Combo) {
         String comboValue = ((Combo)children[indexOfDataTypeControl]).getText();
         if (!comboValue.equalsIgnoreCase("Date") && isSerialValue.equalsIgnoreCase("1")) {
            returnMessage = SchemaEditorPlugin.getResourceString("attribute.rule16.message1");
            returnCode = false;
         }
      }

   }

   private static void rule15Sup(EditablePage page, Attribute base, EntryBase entry, String requiredValue) {
      if (base.getSubScriptLiteral().getValue().endsWith("*") && requiredValue.equals("0")) {
         returnMessage = SchemaEditorPlugin.getResourceString("attribute.rule15Sup.message1");
         returnCode = false;
      }

   }

   private static void rule15(EditablePage page, Attribute base, EntryBase entry, String subScriptLiteralValue) {
      EntryBase required = base.getRequired();
      if (subScriptLiteralValue.endsWith("*") && required.getValue().equals("0")) {
         required.setValue("1");
         MessageDialog.openInformation((Shell)null, "Information", SchemaEditorPlugin.getResourceString("attribute.rule15.information1"));
         returnCode = true;
      }

   }

   private static void rule14Sup(EditablePage page, Attribute base, EntryBase entry, String value) {
      EntryBase computedExpression = base.getComputedExpression();
      if (!value.equals("") && !computedExpression.getValue().equals("")) {
         returnMessage = entry.getDisplayLabel() + " " + SchemaEditorPlugin.getResourceString("attribute.rule14Sup.message1");
         returnCode = false;
      }

   }

   private static void rule14(EditablePage page, Attribute base, EntryBase entry, String computedExpressionValue) {
      EntryBase subScriptLiteral = base.getSubScriptLiteral();
      EntryBase fieldPosition = base.getFieldPosition();
      if ((!subScriptLiteral.getValue().equals("") || !fieldPosition.getValue().equals("")) && !computedExpressionValue.equals("")) {
         MessageDialog.openWarning((Shell)null, "Warning", SchemaEditorPlugin.getResourceString("attribute.rule14.warning1"));
         returnCode = true;
      }

   }

   private static void rule13Sup(EditablePage page, Attribute base, EntryBase entry, String lengthValue) {
      EntryBase displaySize = base.getDisplaySize();
      if (!displaySize.getValue().trim().equals("")) {
         if (lengthValue.equals("")) {
            lengthValue = "0";
         }

         int displaySizeValue = 0;

         try {
            displaySizeValue = Integer.parseInt(displaySize.getValue());
         } catch (Exception var6) {
            ;
         }

         if (Integer.parseInt(lengthValue) <= displaySizeValue) {
            returnMessage = SchemaEditorPlugin.getResourceString("attribute.rule13Sup.message1");
            returnCode = false;
         }

      }
   }

   private static void rule13(EditablePage page, Attribute base, EntryBase entry, String displaySizeValue) {
      if (!displaySizeValue.trim().equals("")) {
         EntryBase length = base.getLength();
         String lengthValue = length.getValue();
         if (lengthValue.equals("")) {
            lengthValue = "0";
         }

         int len = Integer.parseInt(lengthValue);
         if (Integer.parseInt(displaySizeValue) > len) {
            returnMessage = SchemaEditorPlugin.getResourceString("attribute.rule13.message1");
            returnCode = false;
         }

      }
   }

   private static void rule12Sup(EditablePage page, Attribute base, EntryBase entry, String dataTypeValue) {
      if (dataTypeValue.equalsIgnoreCase("L")) {
         EntryBase required = base.getRequired();
         Control[] children = page.getGroup1().getContainer().getChildren();
         int indexOfRequiredControl = getIndex(children, required.getDisplayLabel()) - 2;
         if (indexOfRequiredControl != 0 && children[indexOfRequiredControl] instanceof Button) {
            boolean selection = ((Button)children[indexOfRequiredControl]).getSelection();
            if (selection) {
               ((Button)children[indexOfRequiredControl]).setSelection(false);
               required.setValue("0");
               MessageDialog.openInformation((Shell)null, "Information", SchemaEditorPlugin.getResourceString("attribute.rule12Sup.information1"));
               returnCode = true;
            }
         }
      }

   }

   private static void rule12(EditablePage page, Attribute base, EntryBase entry, String requiredValue) {
      EntryBase dataType = base.getDataType();
      Control[] children = page.getGroup3().getContainer().getChildren();
      int indexOfDataTypeControl = getIndex(children, dataType.getDisplayLabel());
      if (indexOfDataTypeControl != 0 && children[indexOfDataTypeControl] instanceof Combo) {
         String comboValue = ((Combo)children[indexOfDataTypeControl]).getText();
         if (comboValue.equalsIgnoreCase("Logical") && requiredValue.equalsIgnoreCase("1")) {
            returnMessage = SchemaEditorPlugin.getResourceString("attribute.rule12.message1");
            returnCode = false;
         }
      }

   }

   private static void rule9(EditablePage page, Attribute base, EntryBase entry, String value) {
      int len = 0;
      EntryBase length = base.getLength();
      Control[] children = page.getGroup2().getContainer().getChildren();
      int index = getIndex(children, length.getDisplayLabel());
      if (children[index] instanceof Text) {
         len = Integer.parseInt(((Text)children[index]).getText());
      }

      if (len != 0) {
         if (value.length() > len) {
            returnMessage = entry.getDisplayLabel() + " " + SchemaEditorPlugin.getResourceString("attribute.rule9.message1");
            returnCode = false;
         }

      }
   }

   private static void rule8(EditablePage page, Attribute base, EntryBase entry, String mPatternMatchValue) {
      EntryBase mPatternMatch = base.getPatternMatch();
      Control[] children = page.getGroup3().getContainer().getChildren();
      if (mPatternMatchValue.indexOf("X?") == -1) {
         int indexOfMPatternMatchControl = getIndex(children, mPatternMatch.getDisplayLabel());
         if (children[indexOfMPatternMatchControl] instanceof Text) {
            ((Text)children[indexOfMPatternMatchControl]).setText("X?" + mPatternMatchValue);
            mPatternMatch.setValue("X?" + mPatternMatchValue);
         }

         returnMessage = SchemaEditorPlugin.getResourceString("attribute.rule8.message1");
         returnCode = false;
      }

   }

   private static void rule7(EditablePage page, Attribute base, EntryBase entry, String lookupEntityValue) {
      if (lookupEntityValue.startsWith("^")) {
         returnMessage = SchemaEditorPlugin.getResourceString("attribute.rule7.message1");
         returnCode = false;
      }

   }

   private static void rule6(EditablePage page, Attribute base, EntryBase entry, String descriptionValue) {
      EntryBase description = base.getDescription();
      EntryBase reportHeader = base.getReportHeader();
      String oldValue = description.getValue();
      if (!descriptionValue.trim().equalsIgnoreCase(oldValue)) {
         reportHeader.setValue(descriptionValue);
         MessageDialog.openInformation((Shell)null, "Information", SchemaEditorPlugin.getResourceString("attribute.rule6.information1"));
         returnCode = true;
      }

   }

   private static void rule6Sup(EditablePage page, Attribute base, EntryBase entry, String value) {
   }

   private static int getIndex(Control[] children, String string) {
      int index = 0;
      System.out.println("children " + children.length);

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

   private static void rule5Sup(EditablePage page, Attribute base, EntryBase entry, String dataTypeValue) {
      EntryBase length = base.getLength();
      Control[] children = page.getGroup2().getContainer().getChildren();
      int indexOfLengthControl = getIndex(children, length.getDisplayLabel());
      if (dataTypeValue.equalsIgnoreCase("D")) {
         if (indexOfLengthControl != 0 && children[indexOfLengthControl] instanceof Text) {
            if (((Text)children[indexOfLengthControl]).getText().equals("10")) {
               return;
            }

            ((Text)children[indexOfLengthControl]).setText("10");
            length.setValue("10");
         }

         MessageDialog.openInformation((Shell)null, "Information", SchemaEditorPlugin.getResourceString("attribute.rule5Sup.information1"));
         returnCode = true;
      }

   }

   private static void rule5(EditablePage page, Attribute base, EntryBase entry, String lenghValue) {
      EntryBase dataType = base.getDataType();
      Control[] children = page.getGroup3().getContainer().getChildren();
      int indexOfDataTypeControl = getIndex(children, dataType.getDisplayLabel());
      if (indexOfDataTypeControl != 0 && children[indexOfDataTypeControl] instanceof Combo) {
         String comboValue = ((Combo)children[indexOfDataTypeControl]).getText();
         if (comboValue.equalsIgnoreCase("Date") && !lenghValue.trim().equalsIgnoreCase("10")) {
            returnMessage = SchemaEditorPlugin.getResourceString("attribute.rule5.message1");
            returnCode = false;
         }
      }

   }

   private static void rule4(EditablePage page, Attribute base, EntryBase entry, String dataTypeValue) {
      if (dataTypeValue.equalsIgnoreCase("M") || dataTypeValue.equalsIgnoreCase("B")) {
         MessageDialog.openInformation((Shell)null, "Information", SchemaEditorPlugin.getResourceString("attribute.rule4.information1"));
         returnCode = true;
      }

   }
}
