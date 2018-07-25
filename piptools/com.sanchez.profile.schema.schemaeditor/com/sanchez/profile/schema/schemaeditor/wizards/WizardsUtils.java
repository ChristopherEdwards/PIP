package com.sanchez.profile.schema.schemaeditor.wizards;

import com.sanchez.profile.schema.schemaeditor.SchemaEditorPlugin;
import com.sanchez.profile.schema.schemaeditor.ui.DOMSerializer;
import com.sanchez.profile.schema.schemaeditor.ui.EntryBase;
import com.sanchez.profile.schema.schemaeditor.ui.HelpWindow;
import com.sanchez.profile.schema.schemaeditor.ui.LookupItem;
import com.sanchez.profile.schema.schemaeditor.ui.SchemaInfo;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Attribute;
import com.sanchez.profile.schema.schemaeditor.ui.formpages.Entity;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.xerces.parsers.DOMParser;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WizardsUtils {
   private SchemaInfo help;

   public Combo createCombo(Composite parent, EntryBase entry, ArrayList lookupTable) {
      Combo combo = new Combo(parent, 2056);

      for(int i = 0; i < lookupTable.size(); ++i) {
         combo.add(((LookupItem)lookupTable.get(i)).getDisplayValue());
      }

      combo.setData(lookupTable);
      combo.pack();
      return combo;
   }

   public void createLinkLabel(final Composite parent, final EntryBase entry, final Entity base) {
      final Label label = new Label(parent, 0);
      label.setText(entry.getDisplayLabel());
      Font f = label.getFont();
      FontData fd = f.getFontData()[0];
      label.setFont(new Font(label.getDisplay(), fd));
      label.addMouseListener(new MouseAdapter() {
         public void mouseDown(MouseEvent e) {
            WizardsUtils.this.handleLinkLabel(parent, entry, base);
         }
      });
      final Cursor cursor = new Cursor(parent.getDisplay(), 21);
      label.addListener(32, new Listener() {
         public void handleEvent(Event e) {
            label.setCursor(cursor);
         }
      });
   }

   public void handleLinkLabel(Composite parent, EntryBase entry, Entity base) {
      String title = entry.getDisplayLabel();
      this.help = new SchemaInfo(base.getHelpFileName());
      HelpWindow win = new HelpWindow(parent.getShell(), this.help.getHelp(entry.getTag()), title);
      win.open();
   }

   public boolean isTextNonEmpty(Text t) {
      String s = t.getText();
      return s != null && s.trim().length() > 0;
   }

   public String getComboValue(Combo combo) {
      ArrayList items = (ArrayList)combo.getData();
      String displayValue = combo.getText();

      for(int i = 0; i < items.size(); ++i) {
         LookupItem item = (LookupItem)items.get(i);
         if (item.getDisplayValue().equalsIgnoreCase(displayValue)) {
            return item.getValue();
         }
      }

      return "";
   }

   public String getComboDisplayValue(Combo combo, String value) {
      ArrayList items = (ArrayList)combo.getData();
      combo.getText();

      for(int i = 0; i < items.size(); ++i) {
         LookupItem item = (LookupItem)items.get(i);
         if (item.getValue().equalsIgnoreCase(value)) {
            return item.getDisplayValue();
         }
      }

      return "";
   }

   public String getCellComboDefaultLength(SchemaInfo help, String tag, String displayValue) {
      ArrayList items = help.getLookupItems(tag);

      for(int i = 0; i < items.size(); ++i) {
         LookupItem item = (LookupItem)items.get(i);
         if (item.getDisplayValue().equalsIgnoreCase(displayValue)) {
            return item.getDefaultLength();
         }
      }

      return "";
   }

   public String getCellComboValue(SchemaInfo help, String tag, String displayValue) {
      ArrayList items = help.getLookupItems(tag);

      for(int i = 0; i < items.size(); ++i) {
         LookupItem item = (LookupItem)items.get(i);
         if (item.getDisplayValue().equalsIgnoreCase(displayValue)) {
            return item.getValue();
         }
      }

      return "";
   }

   public String getCellComboDisplayValue(SchemaInfo help, String tag, String value) {
      ArrayList items = help.getLookupItems(tag);

      for(int i = 0; i < items.size(); ++i) {
         LookupItem item = (LookupItem)items.get(i);
         if (item.getValue().equalsIgnoreCase(value)) {
            return item.getDisplayValue();
         }
      }

      return "";
   }

   public String formatXml(String xml) throws SAXException, IOException {
      InputStream in = new ByteArrayInputStream(xml.getBytes());
      DOMParser parser = new DOMParser();
      parser.parse(new InputSource(in));
      Document doc = parser.getDocument();
      DOMSerializer serializer = new DOMSerializer();
      StringBuffer sb = new StringBuffer();
      serializer.serialize(doc, sb);
      String xmlInput = sb.toString();
      return xmlInput;
   }

   public boolean isNumber(String number) {
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

   public boolean isInString(String keys, String key) {
      if (keys != null && key != null) {
         if (keys.indexOf(",") == -1) {
            return keys.equals(key);
         } else {
            StringTokenizer keyTokenizer = new StringTokenizer(keys, ",");
            int keyCount = keyTokenizer.countTokens();

            for(int i = 0; i < keyCount; ++i) {
               String thisKey = ((String)keyTokenizer.nextElement()).trim();
               if (thisKey.equals(key)) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public boolean isSQLKeyword(String inputString) {
      URL base = SchemaEditorPlugin.getDefault().getDescriptor().getInstallURL();

      try {
         URL fileURL = new URL(base, "config/SQLKeywords.ini");
         InputStream input = fileURL.openStream();
         BufferedReader br = new BufferedReader(new InputStreamReader(input));

         String thisLine;
         while((thisLine = br.readLine()) != null) {
            if (thisLine.equalsIgnoreCase(inputString)) {
               return true;
            }
         }
      } catch (MalformedURLException var7) {
         System.out.println("isSQLKeyword MalformedURLException occured");
         var7.printStackTrace();
      } catch (IOException var8) {
         System.out.println("isSQLKeyword IOException occured");
         var8.printStackTrace();
      }

      return false;
   }

   public String[] getDataTypes() {
      String[] dataTypeArray = (String[])null;
      Attribute base = new Attribute();
      this.help = new SchemaInfo(base.getHelpFileName());
      ArrayList arrayList = this.help.getLookupItems(base.getDataType().getTag());
      dataTypeArray = new String[arrayList.size()];

      for(int i = 0; i < arrayList.size(); ++i) {
         dataTypeArray[i] = ((LookupItem)arrayList.get(i)).getDisplayValue();
      }

      return dataTypeArray;
   }
}
