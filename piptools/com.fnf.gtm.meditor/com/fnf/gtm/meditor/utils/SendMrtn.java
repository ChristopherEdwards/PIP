package com.fnf.gtm.meditor.utils;

import com.oroinc.net.ftp.FTPClient;
import com.oroinc.net.ftp.FTPReply;
import java.io.FileInputStream;
import java.io.InputStream;

public class SendMrtn {
   public static String crlf = System.getProperties().getProperty("line.separator");

   public static String send(String server, String username, String password, String remote, String local, String fileName) {
      FTPClient ftp = new FTPClient();
      String fileNameAndPath = local + "/" + fileName;

      try {
         ftp.connect(server);
         int reply = ftp.getReplyCode();
         if (!FTPReply.isPositiveCompletion(reply)) {
            return "Failed " + fileNameAndPath + " " + ftp.getReplyString();
         }

         if (!ftp.login(username, password)) {
            return "Failed " + fileNameAndPath + " invalid User/Password ";
         }

         InputStream input = new FileInputStream(fileNameAndPath);
         ftp.storeFile(remote + fileName, input);
         reply = ftp.getReplyCode();
         if (!FTPReply.isPositiveCompletion(reply)) {
            return "Failed " + fileNameAndPath + " " + ftp.getReplyString();
         }

         ftp.logout();
         ftp.disconnect();
      } catch (Exception var10) {
         return "Failed " + fileNameAndPath + " " + var10.getMessage();
      }

      String returnMessage = "OK " + fileNameAndPath + " " + crlf + " sent to " + server + ":" + remote + fileName;
      return returnMessage;
   }
}
