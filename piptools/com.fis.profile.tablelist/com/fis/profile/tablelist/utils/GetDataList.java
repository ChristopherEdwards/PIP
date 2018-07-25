package com.fis.profile.tablelist.utils;

import com.fis.profile.tablelist.TableList;
import com.fnf.profile.ide.utils.ProfileConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GetDataList {
   private String statement;
   private String[] colNames;

   public GetDataList(String statement, String[] colNames) {
      this.statement = statement;
      this.colNames = colNames;
   }

   public ArrayList loadData() {
      String env = TableList.getEnvName();
      String server = TableList.getServerName();
      String port = TableList.getPort();
      String ibsServer = TableList.getIbsServer();
      String userID = TableList.getUserId();
      String pwd = TableList.getPwd();
      ArrayList list = new ArrayList();
      if (env == null) {
         return list;
      } else {
         ProfileConnection hostConnection = new ProfileConnection();
         Connection cn = hostConnection.getConnection(env);
         if (cn == null) {
            hostConnection.setConnection(env, userID, pwd, server, port, ibsServer);
            cn = hostConnection.getConnection(env);
         }

         if (cn != null) {
            try {
               list.clear();
               Statement stmt = cn.createStatement();
               stmt.execute(this.statement);
               ResultSet rs = stmt.getResultSet();

               while(true) {
                  while(rs.next()) {
                     if (this.colNames == null) {
                        list.add(rs.getString(1));
                     } else {
                        String dataType = "";
                        String decimal = "";

                        for(int i = 1; i < this.colNames.length + 1; ++i) {
                           boolean added = false;
                           String value = rs.getString(i);
                           if (this.colNames[i - 1].equals("Data Type")) {
                              dataType = value;
                              value = this.lookForValue(value, TableList.dataType);
                              if (value != null) {
                                 added = true;
                                 list.add(this.colNames[i - 1] + "," + value);
                              }
                           } else if (this.colNames[i - 1].indexOf("Tag Prefix") == -1 && this.colNames[i - 1].indexOf("Tag Suffix") == -1 && !this.colNames[i - 1].equalsIgnoreCase("ASCII Delimiter")) {
                              if (this.colNames[i - 1].equalsIgnoreCase("System Name")) {
                                 value = this.lookForValue(value, TableList.systemName);
                                 if (value != null) {
                                    added = true;
                                    list.add(this.colNames[i - 1] + "," + value);
                                 }
                              } else if (this.colNames[i - 1].equalsIgnoreCase("File Type")) {
                                 value = this.lookForValue(value, TableList.fileType);
                                 if (value != null) {
                                    added = true;
                                    list.add(this.colNames[i - 1] + "," + value);
                                 }
                              } else if (this.colNames[i - 1].equalsIgnoreCase("Record Type")) {
                                 value = this.lookForValue(value, TableList.recordType);
                                 if (value != null) {
                                    added = true;
                                    list.add(this.colNames[i - 1] + "," + value);
                                 }
                              } else if (this.colNames[i - 1].equalsIgnoreCase("Network Location")) {
                                 value = this.lookForValue(value, TableList.networkLoc);
                                 if (value != null) {
                                    added = true;
                                    list.add(this.colNames[i - 1] + "," + value);
                                 }
                              } else if (!this.colNames[i - 1].equalsIgnoreCase("Update Constraint") && !this.colNames[i - 1].equalsIgnoreCase("Delete Constraint")) {
                                 if (this.colNames[i - 1].equalsIgnoreCase("Inheritance File Name")) {
                                    if (value != null && value.length() > 0) {
                                       TableList.superTable = value;
                                    }
                                 } else if (this.colNames[i - 1].equalsIgnoreCase("Decimal Precision")) {
                                    decimal = value;
                                 } else {
                                    int dec;
                                    if (this.colNames[i - 1].equalsIgnoreCase("Java Data Type")) {
                                       if (TableList.javaMap.containsKey(dataType)) {
                                          list.add(this.colNames[i - 1] + "," + TableList.javaMap.get(dataType).toString());
                                       } else if (decimal != null && decimal.length() > 0) {
                                          try {
                                             dec = Integer.parseInt(decimal);
                                             if (dec > 0) {
                                                list.add(this.colNames[i - 1] + ",java.math.BigDecimal");
                                             } else {
                                                list.add(this.colNames[i - 1] + ",String");
                                             }
                                          } catch (Exception var19) {
                                             list.add(this.colNames[i - 1] + ",String");
                                          }
                                       } else {
                                          list.add(this.colNames[i - 1] + ",String");
                                       }

                                       added = true;
                                    } else if (this.colNames[i - 1].equalsIgnoreCase("Profile Data Type")) {
                                       if (TableList.hibernateMap.containsKey(dataType)) {
                                          list.add(this.colNames[i - 1] + "," + TableList.hibernateMap.get(dataType).toString());
                                       } else if (decimal != null && decimal.length() > 0) {
                                          try {
                                             dec = Integer.parseInt(decimal);
                                             if (dec > 0) {
                                                list.add(this.colNames[i - 1] + ",big_decimal");
                                             } else {
                                                list.add(this.colNames[i - 1] + ",string");
                                             }
                                          } catch (Exception var18) {
                                             list.add(this.colNames[i - 1] + ",string");
                                          }
                                       } else {
                                          list.add(this.colNames[i - 1] + ",string");
                                       }

                                       added = true;
                                    }
                                 }
                              } else {
                                 if (value.equals("2")) {
                                    list.add(this.colNames[i - 1] + ",Cascade");
                                 } else {
                                    list.add(this.colNames[i - 1] + ",Restricted");
                                 }

                                 added = true;
                              }
                           } else {
                              value = this.lookForValue(value, TableList.prefixsuffix);
                              if (value != null) {
                                 added = true;
                                 list.add(this.colNames[i - 1] + "," + value);
                              }
                           }

                           if (!added) {
                              list.add(this.colNames[i - 1] + "," + value);
                           }
                        }
                     }
                  }

                  return list;
               }
            } catch (Exception var20) {
               System.out.println("SQL statement got error: " + this.statement);
               var20.printStackTrace();
               return new ArrayList();
            }
         } else {
            list.clear();
            return list;
         }
      }
   }

   private String lookForValue(String value, String[][] list) {
      String returnValue = null;
      if (value != null && !value.equalsIgnoreCase("null")) {
         for(int n = 0; n < list.length; ++n) {
            if (value.equalsIgnoreCase(list[n][1])) {
               return list[n][0];
            }
         }

         return (String)returnValue;
      } else {
         return (String)returnValue;
      }
   }

   public ArrayList loadData_1(int colCount) {
      String env = TableList.getEnvName();
      String server = TableList.getServerName();
      String port = TableList.getPort();
      String ibsServer = TableList.getIbsServer();
      String userID = TableList.getUserId();
      String pwd = TableList.getPwd();
      ArrayList list = new ArrayList();
      if (env == null) {
         return list;
      } else {
         ProfileConnection hostConnection = new ProfileConnection();
         Connection cn = hostConnection.getConnection(env);
         if (cn == null) {
            hostConnection.setConnection(env, userID, pwd, server, port, ibsServer);
            cn = hostConnection.getConnection(env);
         }

         if (cn != null) {
            try {
               Statement stmt = cn.createStatement();
               stmt.execute(this.statement);
               ResultSet rs = stmt.getResultSet();

               while(rs.next()) {
                  String str = "";

                  for(int i = 0; i < colCount; ++i) {
                     String val = "";

                     try {
                        val = rs.getString(i + 1);
                     } catch (Exception var16) {
                        ;
                     }

                     if (val != null) {
                        val = val.replaceAll(",", "aaaaaa");
                     }

                     str = str + val + ",";
                  }

                  str = str.substring(0, str.length() - 1);
                  list.add(str);
               }

               return list;
            } catch (Exception var17) {
               var17.printStackTrace();
               return new ArrayList();
            }
         } else {
            list.clear();
            return list;
         }
      }
   }

   public void updateRecord() {
      String env = TableList.getEnvName();
      String server = TableList.getServerName();
      String port = TableList.getPort();
      String ibsServer = TableList.getIbsServer();
      String userID = TableList.getUserId();
      String pwd = TableList.getPwd();
      ProfileConnection hostConnection = new ProfileConnection();
      Connection cn = hostConnection.getConnection(env);
      if (cn == null) {
         hostConnection.setConnection(env, userID, pwd, server, port, ibsServer);
         cn = hostConnection.getConnection(env);
      }

      if (cn != null) {
         try {
            Statement stmt = cn.createStatement();
            stmt.execute(this.statement);
         } catch (Exception var10) {
            var10.printStackTrace();
            return;
         }
      }

   }
}
