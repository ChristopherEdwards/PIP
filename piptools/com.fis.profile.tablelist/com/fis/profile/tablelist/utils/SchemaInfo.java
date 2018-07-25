package com.fis.profile.tablelist.utils;

import com.fis.profile.tablelist.TableList;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import org.eclipse.core.runtime.IPath;

public class SchemaInfo {
   private static final String ATTRIBITE_HELP = "AttributeInfo.xml";
   private static final String ENTITY_HELP = "EntityInfo.xml";
   private Hashtable helpTable = new Hashtable();
   private Hashtable lookupTable = new Hashtable();

   public SchemaInfo(String filename) {
      InputStream in = getHelpAsStream(filename);
      this.helpTable = XMLUtil.helpFromStream(in);
      in = getHelpAsStream(filename);
      this.lookupTable = XMLUtil.lookupFromStream(in);
   }

   public String getHelp(String tag) {
      return this.helpTable.containsKey(tag) ? (String)this.helpTable.get(tag) : "";
   }

   public ArrayList getLookupItems(String tag) {
      return this.lookupTable.containsKey(tag) ? (ArrayList)this.lookupTable.get(tag) : null;
   }

   private static InputStream getHelpAsStream(String filename) {
      return SchemaInfo.class.getResourceAsStream(filename);
   }

   private static File getSchemaFile(int key) {
      IPath path = null;
      path = TableList.getDefault().getStateLocation();
      path = path.append("EntityInfo.xml");
      return path.toFile();
   }
}
