package com.sanchez.profile.schema.schemaeditor.ui;

import java.util.ArrayList;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public abstract class PageModel {
   protected final FieldFactory factory = new FieldFactory();
   private String[] emptyArray = new String[]{""};
   private String helpFile;
   private SchemaInfo help;

   public PageModel(String helpFile) {
      this.helpFile = helpFile;
      this.help = new SchemaInfo(helpFile);
   }

   protected Control createEntry(Composite parent, EntryBase entry, FieldFactory factory) {
      String displayLabel = entry.getDisplayLabel();
      String tag = entry.getTag();
      String value = entry.getValue();
      boolean readonly = entry.isReadonly();
      boolean lookup = entry.getLookupTable();
      ArrayList lookupTable = null;
      entry.getPath();
      int span = entry.getSpan();
      int controlType = entry.getControlType();
      int dataType = entry.getDataType();
      int limit = entry.getLimit();
      if (lookup) {
         lookupTable = this.help.getLookupItems(tag);
      }

      Text text = null;
      Combo combo = null;
      Label label = null;
      Button button = null;
      if (span != 1) {
         span = 2;
      }

      if (controlType != 11 && controlType != 15) {
         if (controlType == 12) {
            combo = this.createCombo(entry, parent, displayLabel, value, readonly, lookupTable, 1 + 2 * (span - 1), factory, limit);
            return combo;
         } else if (controlType == 14) {
            if (value.equals("")) {
               value = "0";
            }

            button = this.createButton(entry, parent, displayLabel, value, readonly, factory);
            return button;
         } else if (value == null) {
            label = this.createLabel(parent, displayLabel, span * 2);
            return label;
         } else {
            label = this.createLabel(parent, "", span * 2);
            return label;
         }
      } else {
         int style = 2052;
         if (dataType == 6) {
            style = 770;
         }

         text = this.createText(entry, parent, style, displayLabel, value, readonly, 1 + 2 * (span - 1), factory, limit);
         return text;
      }
   }

   protected Button createButton(EntryBase entry, Composite parent, String tag, String value, boolean readonly, FieldFactory factory) {
      Button button = factory.createButton(parent, tag, 32);
      if (value.equals("1")) {
         button.setSelection(true);
      } else {
         button.setSelection(false);
      }

      this.createLinkLabel(parent, entry);
      GridData gd = new GridData(128);
      button.setLayoutData(gd);
      return button;
   }

   protected Label createLabel(Composite parent, String tag, int span) {
      Label label = this.factory.createLabel(parent, tag);
      GridData gd = new GridData(772);
      gd.horizontalSpan = span;
      label.setLayoutData(gd);
      return label;
   }

   protected Text createText(EntryBase entry, Composite parent, int style, String label, String value, boolean readonly, int span, FieldFactory factory, int limit) {
      Label l = this.createLinkLabel(parent, entry);
      GridData gd = new GridData();
      gd.horizontalIndent = 12;
      l.setLayoutData(gd);
      Text text = factory.createText(parent, value, style);
      if (readonly) {
         text.setEditable(false);
      }

      boolean fillHorizontal = false;
      if (entry.getSpan() == 1) {
         if (limit >= 40) {
            fillHorizontal = true;
         }
      } else if (limit >= 80) {
         fillHorizontal = true;
      }

      if (!entry.getTag().equals("ComputedExpression") && !entry.getTag().equals("LookupEntity") && !fillHorizontal) {
         gd = new GridData(516);
      } else {
         gd = new GridData(772);
      }

      gd.horizontalSpan = span;
      if (!entry.getTag().equals("ComputedExpression") && !entry.getTag().equals("LookupEntity") && !fillHorizontal) {
         gd.widthHint = limit * 7;
      }

      text.setLayoutData(gd);
      if (entry.getDataType() == 6) {
         gd.heightHint = 300;
      }

      text.pack();
      return text;
   }

   protected Label createLinkLabel(final Composite parent, final EntryBase entry) {
      final Label label = this.factory.createLabel(parent, entry.getDisplayLabel());
      Font f = label.getFont();
      FontData fd = f.getFontData()[0];
      label.setFont(new Font(label.getDisplay(), fd));
      label.addMouseListener(new MouseAdapter() {
         public void mouseDown(MouseEvent e) {
            PageModel.this.handleLinkLabel(parent, entry);
         }
      });
      final Cursor cursor = new Cursor(parent.getDisplay(), 21);
      label.addListener(32, new Listener() {
         public void handleEvent(Event e) {
            label.setCursor(cursor);
         }
      });
      return label;
   }

   protected void handleLinkLabel(Composite parent, EntryBase entry) {
      String title = entry.getDisplayLabel();
      HelpWindow win = new HelpWindow(parent.getShell(), this.help.getHelp(entry.getTag()), title);
      win.open();
   }

   protected Combo createCombo(EntryBase entry, Composite parent, String label, String value, boolean readonly, ArrayList lookupTable, int span, FieldFactory factory, int limit) {
      Label l = this.createLinkLabel(parent, entry);
      GridData gd = new GridData();
      gd.horizontalIndent = 12;
      l.setLayoutData(gd);
      Combo combo = factory.createCombo(parent, lookupTable, value);
      gd = new GridData(516);
      gd.verticalAlignment = 1;
      gd.horizontalSpan = span;
      gd.widthHint = (limit + 20) * 5 * span;
      combo.setLayoutData(gd);
      combo.pack();
      return combo;
   }

   protected Label createSpace(Composite parent) {
      return this.createLabel(parent, "", 2);
   }
}
