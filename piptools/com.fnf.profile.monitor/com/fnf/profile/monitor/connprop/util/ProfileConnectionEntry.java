package com.fnf.profile.monitor.connprop.util;

import com.fnf.profile.ide.utils.ProfileEnvEntry;

public class ProfileConnectionEntry {
   private String host_name = "";
   private String server_name = "";
   private String ibs_server_name = "";
   private String port_number = "";
   private String ping_status = "";
   private String mtm_status = "";
   private String ibs_server_count = "";
   private String system_date = "";
   private String error_count = "";
   private String user_id = "";
   private String password = "";

   public ProfileConnectionEntry(ProfileEnvEntry env) {
      this.host_name = env.getName();
      this.server_name = env.getServer();
      this.port_number = env.getPort();
      this.user_id = env.getUserName();
      this.password = env.getPassword();
      this.ibs_server_name = env.getIbsServer();
   }

   public String getError_count() {
      return this.error_count;
   }

   public void setError_count(String error_count) {
      this.error_count = error_count;
   }

   public String getHost_name() {
      return this.host_name;
   }

   public void setHost_name(String host_name) {
      this.host_name = host_name;
   }

   public String getIbs_server_count() {
      return this.ibs_server_count;
   }

   public void setIbs_server_count(String ibs_server_count) {
      this.ibs_server_count = ibs_server_count;
   }

   public String getIbs_server_name() {
      return this.ibs_server_name;
   }

   public void setIbs_server_name(String ibs_server_name) {
      this.ibs_server_name = ibs_server_name;
   }

   public String getMtm_status() {
      return this.mtm_status;
   }

   public void setMtm_status(String mtm_status) {
      this.mtm_status = mtm_status;
   }

   public String getPing_status() {
      return this.ping_status;
   }

   public void setPing_status(String ping_status) {
      this.ping_status = ping_status;
   }

   public String getPort_number() {
      return this.port_number;
   }

   public void setPort_number(String port_number) {
      this.port_number = port_number;
   }

   public String getServer_name() {
      return this.server_name;
   }

   public void setServer_name(String server_name) {
      this.server_name = server_name;
   }

   public String getSystem_date() {
      return this.system_date;
   }

   public void setSystem_date(String system_date) {
      this.system_date = system_date;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getUser_id() {
      return this.user_id;
   }

   public void setUser_id(String user_id) {
      this.user_id = user_id;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append(this.host_name);
      buffer.append(",");
      buffer.append(this.server_name);
      buffer.append(",");
      buffer.append(this.port_number);
      buffer.append(",");
      buffer.append(this.ibs_server_name);
      return buffer.toString();
   }
}
