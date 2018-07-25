package com.sanchez.profile.schema.schemaeditor.ui;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.editors.AttributeEditor;
import com.sanchez.profile.schema.schemaeditor.editors.EntityEditor;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.EditablePage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FormEntry {
   private Text textControl;
   private Combo combo;
   private Button button;
   private String value;
   private boolean dirty;
   private Vector listeners = new Vector();
   private EntryBase entry;
   private Composite parent;
   private EditablePage page;
   private String msgTitle = SchemaEditorPlugin.getResourceString("form.msgTitle");
   private AttributeEditor aEditor = null;
   private EntityEditor eEditor = null;
   private EntityRules rules;
   private Object base;

   public FormEntry(Object obj, EditablePage page, Composite parent, Control control, EntryBase entry) {
      this.parent = parent;
      this.page = page;
      this.base = obj;
      if (control instanceof Text) {
         this.textControl = (Text)control;
         this.combo = null;
         this.button = null;
         this.entry = entry;
         this.value = entry.getValue();
         this.addListeners();
      }

      if (control instanceof Combo) {
         this.textControl = null;
         this.combo = (Combo)control;
         this.button = null;
         this.entry = entry;
         this.value = entry.getValue();
         this.addListeners();
      }

      if (control instanceof Button) {
         this.textControl = null;
         this.combo = null;
         this.button = (Button)control;
         this.entry = entry;
         this.value = entry.getValue();
         this.addListeners();
      }

      if (control instanceof Label) {
         this.dirty = false;
         this.value = entry.getValue();
      }

   }

   public void addFormTextListener(IFormTextListener listener) {
      this.listeners.addElement(listener);
   }

   private void addListeners() {
      if (this.textControl != null) {
         this.textControl.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
               FormEntry.this.keyReleaseOccured(e);
            }
         });
         this.textControl.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
               FormEntry.this.editOccured(e);
            }
         });
         final EntryBase lEntry = this.entry;
         this.textControl.addListener(25, new Listener() {
            public void handleEvent(Event e) {
               if (FormEntry.this.textControl.getSelectionCount() == 0 && FormEntry.this.textControl.getText().length() >= lEntry.getLimit() && e.character != '\b' && e.character != 127 && FormEntry.this.textControl.isFocusControl() && !e.text.equals(lEntry.getValue())) {
                  e.doit = false;
                  FormEntry.this.textControl.getShell().getDisplay().beep();
               }

               if (FormEntry.this.textControl.isFocusControl() && !e.text.equals(lEntry.getValue()) && e.character != '\b' && e.character != 127 && lEntry.getDataType() == 2 && !Character.isDigit(e.character)) {
                  e.doit = false;
                  FormEntry.this.textControl.getShell().getDisplay().beep();
               }

            }
         });
         this.textControl.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
               if (FormEntry.this.dirty) {
                  FormEntry.this.commit();
               }

            }
         });
      }

      if (this.combo != null) {
         this.combo.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
               FormEntry.this.keyReleaseOccured(e);
            }
         });
         this.combo.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
               FormEntry.this.editOccured(e);
            }
         });
         this.combo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
               FormEntry.this.selectOccured(e);
            }
         });
         this.combo.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
               if (FormEntry.this.dirty) {
                  FormEntry.this.commit();
               }

            }
         });
      }

      if (this.button != null) {
         this.button.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
               FormEntry.this.keyReleaseOccured(e);
            }
         });
         this.button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
               FormEntry.this.selectOccured(e);
            }
         });
         this.button.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
               if (FormEntry.this.dirty) {
                  FormEntry.this.commit();
               }

            }
         });
      }

   }

   protected void keyReleaseOccured(KeyEvent e) {
      if (e.character == '\r') {
         if (this.dirty && this.entry.getDataType() != 6) {
            this.commit();
         }
      } else if (e.character == 27) {
         if (this.textControl != null) {
            this.textControl.setText(this.value != null ? this.value : "");
         } else if (this.button != null) {
            if (this.entry.getDataType() == 5) {
               this.button.setSelection(this.value == "1");
            }
         } else if (this.combo != null) {
            System.out.println("combo key");
         }

         this.dirty = false;
      }

   }

   protected void editOccured(ModifyEvent e) {
      this.dirty = true;
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         ((IFormTextListener)iter.next()).textDirty(this);
      }

   }

   protected void selectOccured(SelectionEvent e) {
      this.dirty = true;
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         ((IFormTextListener)iter.next()).textDirty(this);
      }

   }

   public void commit() {
      if (this.dirty) {
         if (this.textControl != null) {
            this.value = this.validEntry(this.textControl, this.entry, this.textControl.getText());
            this.textControl.setText(this.value);
            if (this.base instanceof EntityEditor) {
               EntityEditor eEditor = (EntityEditor)this.base;
               EntityRules.validate(this.page, eEditor.getBase(), this.entry, this.textControl.getText());
               if (!EntityRules.returnCode) {
                  this.showMessage(EntityRules.returnMessage);
                  this.value = this.entry.getValue();
                  this.textControl.setFocus();
               }
            }

            if (this.base instanceof AttributeEditor) {
               AttributeEditor aEditor = (AttributeEditor)this.base;
               AttributeRules.validate(this.page, aEditor.getBase(), this.entry, this.textControl.getText());
               if (!AttributeRules.returnCode) {
                  MessageDialog.openWarning((Shell)null, this.msgTitle, AttributeRules.returnMessage);
                  this.value = this.entry.getValue();
                  this.textControl.setFocus();
               }
            }

            this.textControl.setText(this.value);
         } else {
            EntityEditor eEditor;
            AttributeEditor aEditor;
            if (this.combo != null) {
               String comboValue = this.getComboValue();
               this.value = this.validEntry(this.combo, this.entry, comboValue);
               this.combo.setText(this.getComboDisplayValue(this.value));
               if (this.base instanceof EntityEditor) {
                  eEditor = (EntityEditor)this.base;
                  EntityRules.validate(this.page, eEditor.getBase(), this.entry, comboValue);
                  if (!EntityRules.returnCode) {
                     this.showMessage(EntityRules.returnMessage);
                     this.value = this.entry.getValue();
                     this.combo.setFocus();
                  }
               }

               if (this.base instanceof AttributeEditor) {
                  aEditor = (AttributeEditor)this.base;
                  AttributeRules.validate(this.page, aEditor.getBase(), this.entry, comboValue);
                  if (!AttributeRules.returnCode) {
                     this.showMessage(AttributeRules.returnMessage);
                     this.value = this.entry.getValue();
                     this.combo.setFocus();
                  }
               }

               this.combo.setText(this.getComboDisplayValue(this.value));
            } else if (this.button != null) {
               this.value = this.button.getSelection() ? "1" : "0";
               boolean selection = this.value.equals("1");
               this.button.setSelection(selection);
               if (this.base instanceof EntityEditor) {
                  eEditor = (EntityEditor)this.base;
                  EntityRules.validate(this.page, eEditor.getBase(), this.entry, this.value);
                  if (!EntityRules.returnCode) {
                     this.showMessage(EntityRules.returnMessage);
                     this.value = this.entry.getValue();
                     this.button.setFocus();
                  }
               }

               if (this.base instanceof AttributeEditor) {
                  aEditor = (AttributeEditor)this.base;
                  AttributeRules.validate(this.page, aEditor.getBase(), this.entry, this.value);
                  if (!AttributeRules.returnCode) {
                     this.showMessage(AttributeRules.returnMessage);
                     this.value = this.entry.getValue();
                     this.button.setFocus();
                  }
               }

               selection = this.value.equals("1");
               this.button.setSelection(selection);
            } else {
               this.value = "";
            }
         }

         Iterator iter = this.listeners.iterator();

         while(iter.hasNext()) {
            ((IFormTextListener)iter.next()).textValueChanged(this);
         }
      }

      this.dirty = false;
   }

   private String getComboValue() {
      ArrayList items = (ArrayList)this.combo.getData();
      String displayValue = this.combo.getText();

      for(int i = 0; i < items.size(); ++i) {
         LookupItem item = (LookupItem)items.get(i);
         if (item.getDisplayValue().equalsIgnoreCase(displayValue)) {
            return item.getValue();
         }
      }

      return "";
   }

   private String getComboDisplayValue(String value) {
      ArrayList items = (ArrayList)this.combo.getData();
      this.combo.getText();

      for(int i = 0; i < items.size(); ++i) {
         LookupItem item = (LookupItem)items.get(i);
         if (item.getValue().equalsIgnoreCase(value)) {
            return item.getDisplayValue();
         }
      }

      return "";
   }

   private boolean validCharacter(String value) {
      int key = value.indexOf("&");
      if (key != -1) {
         return false;
      } else {
         key = value.indexOf("\"");
         if (key != -1) {
            return false;
         } else {
            key = value.indexOf("`");
            if (key != -1) {
               return false;
            } else {
               key = value.indexOf("<");
               if (key != -1) {
                  return false;
               } else {
                  key = value.indexOf(">");
                  return key == -1;
               }
            }
         }
      }
   }

   private String validEntry(Control control, EntryBase entry, String currentInput) {
      String tempValue = "";
      if (currentInput.equals("")) {
         if (entry.isRequired()) {
            String msg = entry.getDisplayLabel() + " is requied field";
            this.showMessage(msg);
            control.setFocus();
            return entry.getValue();
         } else {
            return currentInput;
         }
      } else {
         int limit = entry.getLimit();
         String msg;
         switch(entry.getDataType()) {
         case 1:
            tempValue = currentInput.toUpperCase();
            if (tempValue.length() > limit) {
               tempValue = tempValue.substring(0, limit);
               String msg = "The length of " + entry.getDisplayLabel() + " can not be more than " + limit;
               this.showMessage(msg);
               control.setFocus();
            }
            break;
         case 2:
            boolean isNum = this.isNumber(currentInput);
            tempValue = isNum ? currentInput : entry.getValue();
            if (!isNum) {
               msg = entry.getDisplayLabel() + "  must be a number!";
               this.showMessage(msg);
               control.setFocus();
            } else if (tempValue.length() > limit) {
               tempValue = tempValue.substring(0, limit);
               msg = "The length of " + entry.getDisplayLabel() + " can not be more than " + limit;
               this.showMessage(msg);
               control.setFocus();
            }
         case 3:
         case 5:
         default:
            break;
         case 4:
            tempValue = currentInput;
            if (currentInput.length() > limit) {
               tempValue = currentInput.substring(0, limit);
               msg = "The length of " + entry.getDisplayLabel() + " can not be more than " + limit;
               this.showMessage(msg);
               control.setFocus();
            }
            break;
         case 6:
            tempValue = currentInput;
            if (currentInput.length() > limit) {
               tempValue = currentInput.substring(0, limit);
               msg = "The length of " + entry.getDisplayLabel() + " can not be more than " + limit;
               this.showMessage(msg);
               control.setFocus();
            }
         }

         return tempValue;
      }
   }

   private void commitCombo() {
      if (this.dirty) {
         this.value = this.getComboValue();
         Iterator iter = this.listeners.iterator();

         while(iter.hasNext()) {
            ((IFormTextListener)iter.next()).textValueChanged(this);
         }
      }

      this.dirty = false;
   }

   private boolean isNumber(String number) {
      if (number != null && !number.equals("") && !number.trim().equals("")) {
         char[] data = number.toCharArray();
         char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
         boolean isNumber = true;
         int dotCounter = 0;

         for(int i = 0; i < data.length; ++i) {
            int matchCounter = digits.length;

            for(int j = 0; j < digits.length; ++j) {
               if (data[i] != digits[j]) {
                  --matchCounter;
               }
            }

            if (data[i] == '.') {
               ++dotCounter;
            }

            if (matchCounter == 0 || matchCounter >= 0 && dotCounter > 1) {
               isNumber = false;
               break;
            }
         }

         if (dotCounter == 1 && number.length() == 1) {
            isNumber = false;
         }

         return isNumber;
      } else {
         return false;
      }
   }

   protected void keyReleaseOccuredOnText(KeyEvent e) {
      Iterator iter;
      if (this.entry.getDataType() == 2) {
         if (Character.isDigit(e.character)) {
            this.dirty = true;
            iter = this.listeners.iterator();

            while(iter.hasNext()) {
               ((IFormTextListener)iter.next()).textDirty(this);
            }
         } else {
            this.textControl.setText(this.value != null ? this.value : "");
            this.dirty = false;
         }
      } else if (this.entry.getDataType() == 1) {
         if (Character.isLowerCase(e.character)) {
            this.textControl.setText(this.textControl.getText().toUpperCase());
         }

         this.dirty = true;
         iter = this.listeners.iterator();

         while(iter.hasNext()) {
            ((IFormTextListener)iter.next()).textDirty(this);
         }
      }

   }

   private void showMessage(String msg) {
      MessageDialog.openWarning(this.parent.getShell(), this.msgTitle, msg);
      this.page.setValid(false);
   }

   public String getValue() {
      return this.value;
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public void removeFormTextListener(IFormTextListener listener) {
      this.listeners.removeElement(listener);
   }

   public void setDirty(boolean newDirty) {
      this.dirty = newDirty;
   }

   public void setValue(String value) {
      if (this.textControl != null) {
         this.textControl.setText(value != null ? value : "");
      }

      this.value = value;
   }
}
