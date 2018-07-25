package com.sanchez.profile.schema.schemaeditor.ui;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

public class FieldFactory {
   public static final String DEFAULT_HEADER_COLOR = "__schema__header__";
   public static final String COLOR_BORDER = "__schema__border";
   public static final String COLOR_COMPOSITE_SEPARATOR = "__schema__separate__";
   public static final String COLOR_FORM = "__schema__form";
   private Hashtable colorRegistry;
   private Color backgroundColor;
   private Color foregroundColor;
   private Color widgetBackgroundColor;
   private Color borderColor;
   private Display display;
   private FieldFactory.BorderPainter borderPainter;
   private Font schemaLabelFont;
   private String lineSpace;
   public static final int BORDER_STYLE = 2048;

   public void paintBordersFor(Composite parent) {
   }

   public FieldFactory() {
      this(Display.getCurrent());
   }

   public FieldFactory(Display display) {
      this.colorRegistry = new Hashtable();
      this.lineSpace = "";
      this.display = display;
      this.initialize();
   }

   public Composite createComposite(Composite parent) {
      return this.createComposite(parent, 0);
   }

   public Composite createComposite(Composite parent, int style) {
      Composite composite = new Composite(parent, style);
      composite.setBackground(this.backgroundColor);
      composite.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
            ((Control)e.widget).setFocus();
         }
      });
      composite.setMenu(parent.getMenu());
      return composite;
   }

   public Composite createCompositeSeparator(Composite parent) {
      Composite composite = new Composite(parent, 0);
      composite.setBackground(this.getColor("__schema__separate__"));
      return composite;
   }

   public Label createHeadingLabel(Composite parent, String text) {
      return this.createHeadingLabel(parent, text, (Color)null, 0);
   }

   public Label createHeadingLabel(Composite parent, String text, int style) {
      return this.createHeadingLabel(parent, text, (Color)null, style);
   }

   public Label createHeadingLabel(Composite parent, String text, Color bg) {
      return this.createHeadingLabel(parent, text, bg, 0);
   }

   public Label createHeadingLabel(Composite parent, String text, Color bg, int style) {
      Label label = new Label(parent, style);
      if (text != null) {
         label.setText(text);
      }

      label.setBackground(this.getColor("__schema__header__"));
      label.setForeground(Display.getCurrent().getSystemColor(1));
      label.setFont(this.getHeadingFont());
      return label;
   }

   private Font getHeadingFont() {
      FontData fd = new FontData();
      fd.setStyle(1);
      fd.setHeight(12);
      return new Font(Display.getCurrent(), fd);
   }

   public Color getHeadingColor() {
      return this.getColor("__schema__header__");
   }

   public Label createLabel(Composite parent, String text) {
      return this.createLabel(parent, text, 0);
   }

   public Label createLabel(Composite parent, String text, int style) {
      Label label = new Label(parent, style);
      if (text != null) {
         label.setText(text);
      }

      label.setForeground(this.foregroundColor);
      Font font = label.getFont();
      FontData[] fontData = font.getFontData();
      fontData[0].setStyle(fontData[0].getStyle());
      fontData[0].setHeight(9);
      label.setFont(new Font(Display.getCurrent(), fontData[0]));
      return label;
   }

   public Label createSeparator(Composite parent, int style) {
      Label label = new Label(parent, 2 | style);
      label.setBackground(new Color(this.display, 222, 0, 0));
      return label;
   }

   public Button createButton(Composite parent, String text, int style) {
      Button button = new Button(parent, style);
      button.setBackground(this.widgetBackgroundColor);
      button.setForeground(this.foregroundColor);
      Font font = button.getFont();
      FontData[] fontData = font.getFontData();
      fontData[0].setStyle(fontData[0].getStyle());
      fontData[0].setHeight(9);
      button.setFont(new Font(Display.getCurrent(), fontData[0]));
      return button;
   }

   public Text createText(Composite parent, String value) {
      return this.createText(parent, value, 2052);
   }

   public Text createText(Composite parent, String value, int style) {
      Text text = new Text(parent, style);
      if (value != null) {
         text.setText(value);
      }

      text.setBackground(this.backgroundColor);
      text.setForeground(this.foregroundColor);
      return text;
   }

   public Combo createCombo(Composite parent, String[] array, String value) {
      Combo combo = new Combo(parent, 2048);
      if (array != null) {
         combo.setItems(array);
      }

      combo.setText(value);
      combo.setBackground(this.backgroundColor);
      combo.setForeground(this.foregroundColor);
      GridData gd = new GridData();
      gd.verticalAlignment = 1;
      combo.setLayoutData(gd);
      return combo;
   }

   public Combo createCombo(Composite parent, ArrayList lookupTable, String value) {
      Combo combo = new Combo(parent, 8);
      combo.setBackground(this.backgroundColor);
      combo.setForeground(this.foregroundColor);
      GridData gd = new GridData();
      gd.verticalAlignment = 1;
      combo.setLayoutData(gd);
      if (lookupTable == null) {
         combo.setText(value);
         return combo;
      } else {
         int index = -1;

         for(int i = 0; i < lookupTable.size(); ++i) {
            combo.add(((LookupItem)lookupTable.get(i)).getDisplayValue());
            if (((LookupItem)lookupTable.get(i)).getValue().equals(value)) {
               index = i;
            }
         }

         combo.select(index);
         if (index == -1) {
            combo.setText(value);
         }

         combo.setData(lookupTable);
         return combo;
      }
   }

   public void dispose() {
      Enumeration colors = this.colorRegistry.elements();

      while(colors.hasMoreElements()) {
         Color c = (Color)colors.nextElement();
         c.dispose();
      }

      this.colorRegistry = null;
   }

   public Color getBackgroundColor() {
      return this.backgroundColor;
   }

   public Color getBorderColor() {
      return this.borderColor;
   }

   public Color getColor(String key) {
      return (Color)this.colorRegistry.get(key);
   }

   public Color getForegroundColor() {
      return this.foregroundColor;
   }

   private void initialize() {
      FontData fd = new FontData();
      fd.setStyle(1);
      fd.setHeight(10);
      this.schemaLabelFont = new Font(this.display, fd);
      this.widgetBackgroundColor = this.display.getSystemColor(22);
      this.backgroundColor = this.display.getSystemColor(25);
      this.registerColor("__schema__form", 211, 231, 255);
      this.registerColor("__schema__border", 195, 191, 179);
      this.registerColor("__schema__separate__", 152, 170, 203);
      this.registerColor("__schema__header__", 0, 0, 102);
      if (this.isWhiteBackground()) {
         this.borderColor = this.getColor("__schema__border");
      } else {
         this.borderColor = this.display.getSystemColor(22);
      }

      this.foregroundColor = this.display.getSystemColor(24);
   }

   public boolean isWhiteBackground() {
      return this.backgroundColor.getRed() == 255 && this.backgroundColor.getGreen() == 255 && this.backgroundColor.getBlue() == 255;
   }

   public Color registerColor(String key, int r, int g, int b) {
      Color c = new Color(this.display, r, g, b);
      this.colorRegistry.put(key, c);
      return c;
   }

   public void setBackgroundColor(Color color) {
      this.backgroundColor = color;
   }

   public void setForegroundColor(Color color) {
      this.foregroundColor = color;
   }

   public Color getWidgetBackgroundColor() {
      return this.widgetBackgroundColor;
   }

   public void setWidgetBackgroundColor(Color color) {
      this.widgetBackgroundColor = color;
   }

   public Font getSchemaLabelFont() {
      return this.schemaLabelFont;
   }

   class BorderPainter implements PaintListener {
      public void paintControl(PaintEvent event) {
         Composite composite = (Composite)event.widget;
         Control[] children = composite.getChildren();

         for(int i = 0; i < children.length; ++i) {
            Control c = children[i];
            if (c.getEnabled() || c instanceof CCombo) {
               Rectangle b;
               GC gc;
               if (!(c instanceof Text) && !(c instanceof Canvas) && !(c instanceof CCombo)) {
                  if (c instanceof Table || c instanceof Tree || c instanceof TableTree) {
                     b = c.getBounds();
                     gc = event.gc;
                     gc.setForeground(FieldFactory.this.borderColor);
                     gc.drawRectangle(b.x - 1, b.y - 1, b.width + 2, b.height + 2);
                  }
               } else {
                  b = c.getBounds();
                  gc = event.gc;
                  gc.setForeground(c.getBackground());
                  gc.drawRectangle(b.x - 1, b.y - 1, b.width + 1, b.height + 1);
                  gc.setForeground(FieldFactory.this.foregroundColor);
                  if (c instanceof CCombo) {
                     gc.drawRectangle(b.x - 1, b.y - 1, b.width + 1, b.height + 1);
                  } else {
                     gc.drawRectangle(b.x - 1, b.y - 2, b.width + 1, b.height + 3);
                  }
               }
            }
         }

      }
   }
}
