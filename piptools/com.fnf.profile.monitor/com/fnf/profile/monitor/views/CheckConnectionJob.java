package com.fnf.profile.monitor.views;

import com.fnf.profile.ide.utils.ProfileConnection;
import com.fnf.profile.monitor.connprop.util.ProfileConnectionEntry;
import com.fnf.profile.monitor.connprop.util.ProfileConnectionEntryList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class CheckConnectionJob extends Job {
   private ProfileConnectionEntryList connectionList;
   private Connection cn;
   private Hashtable htHost = new Hashtable();
   private Hashtable connectionStatus = new Hashtable();

   public CheckConnectionJob(String name, ProfileConnectionEntryList connList, Hashtable ht) {
      super(name);
      this.connectionList = connList;
      this.htHost = ht;
   }

   protected IStatus run(IProgressMonitor monitor) {
      monitor.beginTask("Checking connection status", this.connectionList.getConnections().size());
      Iterator itr = this.connectionList.getConnections().iterator();

      while(itr.hasNext()) {
         ProfileConnectionEntry entry = (ProfileConnectionEntry)itr.next();
         String host = entry.getHost_name();
         String userID = entry.getUser_id();
         String pwd = entry.getPassword();
         String server = entry.getServer_name();
         String port = entry.getPort_number();
         String ibsServer = entry.getIbs_server_name();
         if (host.length() > 0 && userID.length() > 0 && pwd.length() > 0 && server.length() > 0 && server.length() > 0 && port.length() > 0 && ibsServer.length() > 0) {
            monitor.subTask("Checking " + host);
            this.connectionStatus = (Hashtable)this.htHost.get(host);
            if (!this.getPingStatus(server)) {
               monitor.worked(1);
            } else {
               this.connectionStatus.remove("ping_status");
               this.connectionStatus.put("ping_status", "OK");
               if (!this.getMTMStatus(host, userID, pwd, server, port, ibsServer)) {
                  monitor.worked(1);
               } else {
                  this.connectionStatus.remove("mtm_status");
                  this.connectionStatus.put("mtm_status", "OK");
                  this.connectionStatus.remove("ibs_server_count");
                  this.connectionStatus.put("ibs_server_count", Integer.toString(1));
                  this.connectionStatus.remove("system_date");
                  this.connectionStatus.put("system_date", this.getSystem_Date(this.cn));
                  this.connectionStatus.remove("error_count");
                  this.connectionStatus.put("error_count", Integer.toString(this.getError_Count(this.cn)));
                  monitor.worked(1);
               }
            }
         }
      }

      return Status.OK_STATUS;
   }

   private boolean getPingStatus(String host_name) {
      try {
         Process p = Runtime.getRuntime().exec("ping -n 1 " + host_name);
         int status = p.waitFor();
         return status == 0;
      } catch (InterruptedException var4) {
         var4.printStackTrace();
         return false;
      } catch (IOException var5) {
         var5.printStackTrace();
         return false;
      }
   }

   private boolean getMTMStatus(String host, String userID, String pwd, String server, String port, String ibsServer) {
      ProfileConnection hostConnection = new ProfileConnection();
      this.cn = hostConnection.getConnection(host);
      boolean status;
      if (this.cn == null) {
         status = hostConnection.setConnection(host, userID, pwd, server, port, ibsServer);
         this.cn = hostConnection.getConnection(host);
         return status;
      } else {
         try {
            this.cn.close();
            status = hostConnection.setConnection(host, userID, pwd, server, port, ibsServer);
            this.cn = hostConnection.getConnection(host);
            return status;
         } catch (SQLException var9) {
            var9.printStackTrace();
            return false;
         }
      }
   }

   private String getSystem_Date(Connection connection) {
      String command = "select TJD from CUVAR";

      String system_date;
      try {
         Statement stmt = connection.createStatement(1004, 1007);
         ResultSet result = stmt.executeQuery(command);
         result.first();
         system_date = result.getString(1);
         stmt.close();
      } catch (SQLException var6) {
         var6.printStackTrace();
         system_date = "";
      }

      return system_date;
   }

   private int getError_Count(Connection connection) {
      boolean var2 = true;

      int err_cnt;
      try {
         PreparedStatement stmt = connection.prepareStatement("select count(SEQ) from ERROR where DATE = ?");
         stmt.setDate(1, new Date(Calendar.getInstance().getTimeInMillis()));
         ResultSet result = stmt.executeQuery();
         result.first();
         err_cnt = result.getInt(1);
         stmt.close();
      } catch (SQLException var5) {
         var5.printStackTrace();
         err_cnt = -1;
      }

      return err_cnt;
   }
}
