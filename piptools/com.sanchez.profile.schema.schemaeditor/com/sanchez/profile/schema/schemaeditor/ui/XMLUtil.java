package com.sanchez.profile.schema.schemaeditor.ui;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class XMLUtil {
   private static String prefix_Attribute = "com.sanchez.profile.schema.schemaeditor.ui.formpages.";

   public static HashMap getAllFields(Class aClass) {
      HashMap allFields = new HashMap();
      Class myClass = aClass;

      do {
         Field[] field4ThisClass = myClass.getDeclaredFields();

         for(int ii = 0; ii < field4ThisClass.length; ++ii) {
            String fieldName = field4ThisClass[ii].getName();
            Field alreadyExisted = (Field)allFields.get(fieldName);
            if (alreadyExisted == null) {
               allFields.put(fieldName, field4ThisClass[ii]);
            }
         }

         myClass = myClass.getSuperclass();
      } while(!myClass.equals(Object.class));

      return allFields;
   }

   public static Element getFirstElement(Element element, String name) {
      NodeList nl = element.getElementsByTagName(name);
      if (nl.getLength() < 1) {
         throw new RuntimeException("Element: " + element + " does not contain: " + name);
      } else {
         return (Element)nl.item(0);
      }
   }

   public static String getSimpleElementText(Element node, String name) {
      Element namedElement = getFirstElement(node, name);
      return getSimpleElementText(namedElement);
   }

   public static String getSimpleElementText(Element node) {
      StringBuffer sb = new StringBuffer();
      NodeList children = node.getChildNodes();

      for(int i = 0; i < children.getLength(); ++i) {
         Node child = children.item(i);
         if (child instanceof Text) {
            sb.append(child.getNodeValue());
         }
      }

      return sb.toString();
   }

   public static boolean hasChildElementNode(Element node) {
      NodeList children = node.getChildNodes();

      for(int i = 0; i < children.getLength(); ++i) {
         Node child = children.item(i);
         if (child.getNodeType() == 1) {
            return true;
         }
      }

      return false;
   }

   public static String formatedName(String tagName) {
      String str = tagName.toLowerCase().substring(0, 1) + tagName.substring(1);
      int index = str.indexOf(45);
      if (index == -1) {
         return str;
      } else {
         str = str.substring(0, index) + "_" + str.substring(index + 1);
         return str;
      }
   }

   public static synchronized String encodeXmlString(String s) {
      if (s == null) {
         return "";
      } else {
         String encoded_str = "";

         for(int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '&') {
               encoded_str = encoded_str + "&amp;";
            } else if (s.charAt(i) == '\'') {
               encoded_str = encoded_str + "&apos;";
            } else if (s.charAt(i) == '"') {
               encoded_str = encoded_str + "&quot;";
            } else if (s.charAt(i) == '>') {
               encoded_str = encoded_str + "&gt;";
            } else if (s.charAt(i) == '<') {
               encoded_str = encoded_str + "&lt;";
            } else {
               encoded_str = encoded_str + s.charAt(i);
            }
         }

         return encoded_str;
      }
   }

   public static Hashtable helpFromStream(InputStream input) {
      try {
         DOMParser parser = new DOMParser();
         parser.parse(new InputSource(input));
         Document document = parser.getDocument();
         Element root = document.getDocumentElement();
         root.normalize();
         NodeList helpNL = root.getChildNodes();
         Hashtable helpTable = new Hashtable();

         for(int ii = 0; ii < helpNL.getLength(); ++ii) {
            Node helpNode = helpNL.item(ii);
            if (helpNode.getNodeType() == 1) {
               Object key = null;
               String info = "";
               Element helpElement = (Element)helpNode;
               if (helpElement.getNodeName().equalsIgnoreCase("info")) {
                  String tag = helpElement.getAttribute("tag");
                  StringBuffer sb = new StringBuffer();
                  if (tag != null) {
                     sb.append(tag);
                  }

                  key = sb.toString();
                  NodeList resultsNL = helpElement.getChildNodes();

                  for(int jj = 0; jj < resultsNL.getLength(); ++jj) {
                     Node resultNode = resultsNL.item(jj);
                     if (resultNode.getNodeType() == 1) {
                        Element resultElement = (Element)resultNode;
                        if (resultElement.getNodeName().equalsIgnoreCase("help")) {
                           info = getSimpleElementText(resultElement);
                           helpTable.put(key, info);
                        }
                     }
                  }
               }
            }
         }

         return helpTable;
      } catch (Exception var17) {
         var17.printStackTrace();
         return null;
      }
   }

   public static Hashtable lookupFromStream(InputStream input) {
      try {
         DOMParser parser = new DOMParser();
         parser.parse(new InputSource(input));
         Document document = parser.getDocument();
         Element root = document.getDocumentElement();
         root.normalize();
         NodeList helpNL = root.getChildNodes();
         Hashtable lookupTable = new Hashtable();

         for(int ii = 0; ii < helpNL.getLength(); ++ii) {
            Node helpNode = helpNL.item(ii);
            if (helpNode.getNodeType() == 1) {
               Object key = null;
               ArrayList lookup = null;
               Element helpElement = (Element)helpNode;
               if (helpElement.getNodeName().equalsIgnoreCase("info")) {
                  String tag = helpElement.getAttribute("tag");
                  StringBuffer sb = new StringBuffer();
                  if (tag != null) {
                     sb.append(tag);
                  }

                  key = sb.toString();
                  NodeList resultsNL = helpElement.getChildNodes();

                  for(int jj = 0; jj < resultsNL.getLength(); ++jj) {
                     Node resultNode = resultsNL.item(jj);
                     if (resultNode.getNodeType() == 1) {
                        Element resultElement = (Element)resultNode;
                        if (resultElement.getNodeName().equalsIgnoreCase("lookup")) {
                           lookup = getLookupArray(resultElement, "item");
                           lookupTable.put(key, lookup);
                        }
                     }
                  }
               }
            }
         }

         return lookupTable;
      } catch (Exception var17) {
         var17.printStackTrace();
         return null;
      }
   }

   public static ArrayList getLookupArray(Element node, String name) {
      ArrayList list = new ArrayList();
      NodeList children = node.getElementsByTagName(name);

      for(int i = 0; i < children.getLength(); ++i) {
         Node child = children.item(i);
         Element childElement = (Element)child;
         NodeList lookupValueNL = childElement.getElementsByTagName("value");
         String tableKey = getSimpleElementText((Element)lookupValueNL.item(0));
         NodeList lookupDisplayNL = childElement.getElementsByTagName("display");
         String tableValue = getSimpleElementText((Element)lookupDisplayNL.item(0));
         NodeList lookupLength = childElement.getElementsByTagName("defaultLength");
         if (lookupLength.getLength() != 0) {
            String tableLength = getSimpleElementText((Element)lookupLength.item(0));
            list.add(new LookupItem(tableKey, tableValue, tableLength));
         } else {
            list.add(new LookupItem(tableKey, tableValue));
         }
      }

      Comparator comparator = new Comparator() {
         public int compare(Object a, Object b) {
            return ((LookupItem)a).getDisplayValue().compareToIgnoreCase(((LookupItem)b).getDisplayValue());
         }
      };
      Collections.sort(list, comparator);
      return list;
   }
}
