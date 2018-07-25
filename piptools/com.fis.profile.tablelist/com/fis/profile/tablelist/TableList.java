package com.fis.profile.tablelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class TableList extends AbstractUIPlugin {
   public static final String PLUGIN_ID = "com.fis.profile.tablelist";
   private static TableList plugin;
   private ResourceBundle resourceBundle;
   public static String env_name;
   public static String server_name;
   public static String port_number;
   public static String user_id;
   public static String password;
   public static String ibs_server;
   private static String selectedTableName = "";
   private static String selectedColumnName = "";
   private static String loadedTableName = "";
   private static String loadedColumnName = "";
   private static String loadedIndex = "";
   private static String loadedForeignKey = "";
   private static String loadedJournal = "";
   private static String loadedTableDoc = "";
   private static String loadedSysMap = "";
   private static String loadedColumnDoc = "";
   private static ArrayList columnProp = new ArrayList();
   private static ArrayList objectTableDetails = new ArrayList();
   public static String[][] prefixsuffix = new String[13][2];
   public static String[][] dataType = new String[10][2];
   public static String[][] recordType = new String[4][2];
   public static String[][] networkLoc = new String[3][2];
   public static String[][] systemName = new String[5][2];
   public static String[][] fileType = new String[7][2];
   public static HashMap javaMap = new HashMap();
   public static HashMap hibernateMap = new HashMap();
   public static String superTable = "";
   public static String[] columnProps = new String[]{"File Name", "Data Item Name", "Description", "Required Indicator", "Maximum Field Length", "Decimal Precision", "Data Type", "Maximum Value", "Minimum Value", "Default Value", "Computed Expression", "Is Serial Value", "Look-Up Table Name", "Null is Zero", "Master Dictionary Reference", "Pattern Match", "Field Position", "Subscript Key", "Sub Field Definition", "Sub-Field Delimiter (Tag Prefix)", "Sub-Field Delimiter (Tag Suffix)", "Sub-Field Position", "Sub-Field Tag", "Field Display Size", "Report Header", "Internal Data Type", "Java Data Type", "Profile Data Type", "Master Dictionary File Name", "User ID", "Last Updated"};
   public static String[] tableColumnProps = new String[]{"File Name", "Description", "Primary Keys", "Database", "ASCII Delimiter", "Deletion Restriction Flag", "Look-up Table List", "Look-up Table List (Line 2)", "Default Heading", "Descending Order", "Record Existed Indicator (node number)", "Extended File and Column Names Allowed", "Documentation File Name", "File Type", "Data Item Protection Filename", "File Short Name", "Global Name", "MUMPS Global Reference", "Default Data Item List", "Required Data Item List", "Enable Automatic Logging", "Last Updated", "Implicit Data Dictionary Reference", "Network Location", "Inheritance File Name", "Data Entry Pre-Processor", "Data Item@Name (Time Created)", "Data Item Name (Time Last Updated)", "Data Item Name (Date Created)", "Data Item Name (Date Last Updated)", "Data Item Name (User Created)", "Date Item Name (User Last Updated)", "Query", "Record Type", "File Maintenance Restriction Flag", "File Maintenance Data Entry Screen IDs", "System Name", "Primary Keys Access Routine", "Record Filer Routine", "User Defined File Post Processor", "User Defined Authorization Routine", "User ID"};

   public TableList() {
      this.buildPrefixSuffix();
      this.buildDataType();
      this.buildFileType();
      this.buildSystemName();
      this.buildRecordType();
      this.buildNetworkLoc();

      try {
         this.resourceBundle = ResourceBundle.getBundle("com.fis.profile.tablelist.resources");
      } catch (MissingResourceException var1) {
         this.resourceBundle = null;
      }

   }

   public void start(BundleContext context) throws Exception {
      super.start(context);
      plugin = this;
   }

   public void stop(BundleContext context) throws Exception {
      plugin = null;
      super.stop(context);
   }

   public static TableList getDefault() {
      return plugin;
   }

   public static ImageDescriptor getImageDescriptor(String path) {
      return imageDescriptorFromPlugin("com.fis.profile.tablelist", path);
   }

   public static String getResourceString(String key) {
      ResourceBundle bundle = getDefault().getResourceBundle();

      try {
         return bundle.getString(key);
      } catch (MissingResourceException var2) {
         return null;
      }
   }

   public ResourceBundle getResourceBundle() {
      return this.resourceBundle;
   }

   public static void setNewEnv(String env, String server, String port, String userid, String pwd, String ibsserver) {
      env_name = env;
      server_name = server;
      port_number = port;
      user_id = userid;
      password = pwd;
      ibs_server = ibsserver;
   }

   public static String getEnvName() {
      return env_name;
   }

   public static String getServerName() {
      return server_name;
   }

   public static String getPort() {
      return port_number;
   }

   public static String getIbsServer() {
      return ibs_server;
   }

   public static String getUserId() {
      return user_id;
   }

   public static String getPwd() {
      return password;
   }

   public static void setSelectedTableName(String tableName) {
      selectedTableName = tableName;
   }

   public static void setSelectedColumnName(String columnName) {
      selectedColumnName = columnName;
   }

   public static String getSelectedTableName() {
      return selectedTableName;
   }

   public static String getSelectedColumnName() {
      return selectedColumnName;
   }

   public static String getLoadedTableName() {
      return loadedTableName;
   }

   public static void setLoadedTableName(String tableName) {
      loadedTableName = tableName;
   }

   public static String getLoadedColumnName() {
      return loadedColumnName;
   }

   public static void setLoadedColumnName(String colName) {
      loadedColumnName = colName;
   }

   public static String getLoadedIndex() {
      return loadedIndex;
   }

   public static void setLoadedIndex(String tableName) {
      loadedIndex = tableName;
   }

   public static String getLoadedForeignKey() {
      return loadedForeignKey;
   }

   public static void setLoadedForeignKey(String tableName) {
      loadedForeignKey = tableName;
   }

   public static String getLoadedJournal() {
      return loadedJournal;
   }

   public static void setLoadedJournal(String tableName) {
      loadedJournal = tableName;
   }

   public static String getLoadedTableDoc() {
      return loadedTableDoc;
   }

   public static void setLoadedTableDoc(String tableName) {
      loadedTableDoc = tableName;
   }

   public static String getLoadedColumnDoc() {
      return loadedColumnDoc;
   }

   public static void setLoadedColumnDoc(String columnName) {
      loadedColumnDoc = columnName;
   }

   public static String getLoadedSysMap() {
      return loadedSysMap;
   }

   public static void setLoadedSysMap(String columnName) {
      loadedSysMap = columnName;
   }

   public static void setColumnProp(ArrayList prop) {
      columnProp = prop;
   }

   public static ArrayList getColumnProp() {
      return columnProp;
   }

   public static void setTableDetails(ArrayList details) {
      objectTableDetails = details;
   }

   public static ArrayList getTableDetails() {
      return objectTableDetails;
   }

   private void buildPrefixSuffix() {
      prefixsuffix[0][0] = "CTRL/A";
      prefixsuffix[0][1] = "1";
      prefixsuffix[1][0] = "<TAB>";
      prefixsuffix[1][1] = "9";
      prefixsuffix[2][0] = "<LF>";
      prefixsuffix[2][1] = "10";
      prefixsuffix[3][0] = "<FF>";
      prefixsuffix[3][1] = "12";
      prefixsuffix[4][0] = "<CR>";
      prefixsuffix[4][1] = "13";
      prefixsuffix[5][0] = "#";
      prefixsuffix[5][1] = "35";
      prefixsuffix[6][0] = ",";
      prefixsuffix[6][1] = "44";
      prefixsuffix[7][0] = "/";
      prefixsuffix[7][1] = "47";
      prefixsuffix[8][0] = ":";
      prefixsuffix[8][1] = "48";
      prefixsuffix[9][0] = "\\";
      prefixsuffix[9][1] = "92";
      prefixsuffix[10][0] = "^";
      prefixsuffix[10][1] = "94";
      prefixsuffix[11][0] = "SCA Delimiter";
      prefixsuffix[11][1] = "124";
      prefixsuffix[12][0] = "~";
      prefixsuffix[12][1] = "126";
   }

   private void buildDataType() {
      dataType[0][0] = "Currency";
      dataType[0][1] = "$";
      javaMap.put("$", "java.math.BigDecimal");
      hibernateMap.put("$", "big_decimal");
      dataType[1][0] = "Binary";
      dataType[1][1] = "B";
      javaMap.put("B", "String");
      hibernateMap.put("B", "string");
      dataType[2][0] = "Clock Time";
      dataType[2][1] = "C";
      javaMap.put("C", "java.util.Date");
      hibernateMap.put("C", "date");
      dataType[3][0] = "Date";
      dataType[3][1] = "D";
      javaMap.put("D", "java.util.Date");
      hibernateMap.put("D", "date");
      dataType[4][0] = "Frequency";
      dataType[4][1] = "F";
      javaMap.put("F", "String");
      hibernateMap.put("F", "string");
      dataType[5][0] = "Logical";
      dataType[5][1] = "L";
      javaMap.put("L", "Boolean");
      hibernateMap.put("L", "boolean");
      dataType[6][0] = "Memo";
      dataType[6][1] = "M";
      javaMap.put("M", "String");
      hibernateMap.put("M", "string");
      dataType[7][0] = "Numeric";
      dataType[7][1] = "N";
      javaMap.put("N", "java.math.BigInteger");
      hibernateMap.put("N", "big_integer");
      dataType[8][0] = "Text";
      dataType[8][1] = "T";
      javaMap.put("T", "String");
      hibernateMap.put("T", "string");
      dataType[9][0] = "Upper Case";
      dataType[9][1] = "U";
      javaMap.put("U", "String");
      hibernateMap.put("U", "string");
   }

   private void buildRecordType() {
      recordType[0][0] = "None";
      recordType[0][1] = "0";
      recordType[1][0] = "Unsegmented";
      recordType[1][1] = "1";
      recordType[2][0] = "Mixed Type";
      recordType[2][1] = "11";
      recordType[3][0] = "Node [Segmented]";
      recordType[3][1] = "10";
   }

   private void buildNetworkLoc() {
      networkLoc[0][0] = "Both";
      networkLoc[0][1] = "2";
      networkLoc[1][0] = "Client Only";
      networkLoc[1][1] = "1";
      networkLoc[2][0] = "Server Only";
      networkLoc[2][1] = "0";
   }

   private void buildSystemName() {
      systemName[0][0] = "ACCOUNTS PAYABLE SYSTEM";
      systemName[0][1] = "APS";
      systemName[1][0] = "DATA QWIK";
      systemName[1][1] = "DBS";
      systemName[2][0] = "GENERAL LEDGER SYSTEM";
      systemName[2][1] = "GLS";
      systemName[3][0] = "PROFILE";
      systemName[3][1] = "PBS";
      systemName[4][0] = "SCA UTILITIES";
      systemName[4][1] = "SCA";
   }

   private void buildFileType() {
      fileType[0][0] = "Entity File";
      fileType[0][1] = "1";
      fileType[1][0] = "Domain File";
      fileType[1][1] = "3";
      fileType[2][0] = "Relationship File";
      fileType[2][1] = "2";
      fileType[3][0] = "Index File";
      fileType[3][1] = "4";
      fileType[4][0] = "Dummy File";
      fileType[4][1] = "5";
      fileType[5][0] = "Journal File";
      fileType[5][1] = "6";
      fileType[6][0] = "GUI Client";
      fileType[6][1] = "7";
   }
}
