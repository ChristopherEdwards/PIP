package com.sanchez.profile.schema.schemaeditor.ui;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMSerializer {
   private String indent = "    ";
   private String lineSeparator = System.getProperties().getProperty("line.separator");

   public void setIndent(String indent) {
      this.indent = indent;
   }

   public void setLineSeparator(String lineSeparator) {
      this.lineSeparator = lineSeparator;
   }

   public void serialize(Document doc, StringBuffer sb) {
      this.serializeNode(doc, sb, "");
   }

   private void serializeNode(Node node, StringBuffer sb, String indentLevel) {
      switch(node.getNodeType()) {
      case 1:
         String name = node.getNodeName();
         sb.append(indentLevel + "<" + name);
         NamedNodeMap attributes = node.getAttributes();

         for(int i = 0; i < attributes.getLength(); ++i) {
            Node current = attributes.item(i);
            sb.append(" " + current.getNodeName() + "=\"" + current.getNodeValue() + "\"");
         }

         sb.append(">");
         NodeList children = node.getChildNodes();
         if (children != null) {
            if (children.item(0) != null && children.item(0).getNodeType() == 1) {
               sb.append(this.lineSeparator);
            }

            for(int i = 0; i < children.getLength(); ++i) {
               this.serializeNode(children.item(i), sb, indentLevel + this.indent);
            }

            if (children.item(0) != null && children.item(children.getLength() - 1).getNodeType() == 1) {
               sb.append(indentLevel);
            }
         }

         sb.append("</" + name + ">");
         sb.append(this.lineSeparator);
      case 2:
      case 6:
      default:
         break;
      case 3:
         String value = XMLUtil.encodeXmlString(node.getNodeValue());
         value = value.replaceAll("\n", this.lineSeparator);
         sb.append(value);
         break;
      case 4:
         sb.append("<![CDATA[" + node.getNodeValue() + "]]>");
         break;
      case 5:
         sb.append("&" + node.getNodeName() + ";");
         break;
      case 7:
         sb.append("<?" + node.getNodeName() + " " + node.getNodeValue() + "?>");
         sb.append(this.lineSeparator);
         break;
      case 8:
         sb.append(indentLevel + "<!-- " + node.getNodeValue() + " -->");
         sb.append(this.lineSeparator);
         break;
      case 9:
         sb.append("<?xml version=\"1.0\"?>");
         sb.append(this.lineSeparator);
         NodeList nodes = node.getChildNodes();
         if (nodes != null) {
            for(int i = 0; i < nodes.getLength(); ++i) {
               this.serializeNode(nodes.item(i), sb, "");
            }
         }
         break;
      case 10:
         DocumentType docType = (DocumentType)node;
         sb.append("<!DOCTYPE " + docType.getName());
         if (docType.getPublicId() != null) {
            System.out.print(" PUBLIC \"" + docType.getPublicId() + "\" ");
         } else {
            sb.append(" SYSTEM ");
         }

         sb.append("\"" + docType.getSystemId() + "\">");
         sb.append(this.lineSeparator);
      }

   }
}
