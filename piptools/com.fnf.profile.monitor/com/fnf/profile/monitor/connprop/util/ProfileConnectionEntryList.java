package com.fnf.profile.monitor.connprop.util;

import com.fnf.profile.ide.IdePlugin;
import com.fnf.profile.ide.utils.ProfileEnvEntry;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class ProfileConnectionEntryList {
   private Set changeListeners = new HashSet();
   private Vector connections = new Vector();

   public void addConnection(ProfileConnectionEntry connectionEntry) {
      this.connections.add(this.connections.size(), connectionEntry);
      Iterator iterator = this.changeListeners.iterator();

      while(iterator.hasNext()) {
         ((IConnectionListViewer)iterator.next()).addConnection(connectionEntry);
      }

   }

   public void removeConnection(ProfileConnectionEntry connectionEntry) {
      this.connections.remove(connectionEntry);
      Iterator iterator = this.changeListeners.iterator();

      while(iterator.hasNext()) {
         ((IConnectionListViewer)iterator.next()).removeConnection(connectionEntry);
      }

   }

   public void connectionChanged(ProfileConnectionEntry connectionEntry) {
      Iterator iterator = this.changeListeners.iterator();

      while(iterator.hasNext()) {
         ((IConnectionListViewer)iterator.next()).updateConnection(connectionEntry);
      }

   }

   public void removeChangeListener(IConnectionListViewer viewer) {
      this.changeListeners.remove(viewer);
   }

   public void addChangeListener(IConnectionListViewer viewer) {
      this.changeListeners.add(viewer);
   }

   public void initData() {
      HashSet envList = IdePlugin.getDefault().getEnvSet();
      Iterator itr = envList.iterator();

      while(itr.hasNext()) {
         ProfileEnvEntry entry = (ProfileEnvEntry)itr.next();
         ProfileConnectionEntry connEntry = new ProfileConnectionEntry(entry);
         this.addConnection(connEntry);
      }

   }

   public Vector getConnections() {
      return this.connections;
   }

   public ProfileConnectionEntry getEntry(String host_name) {
      Iterator itr = this.connections.iterator();

      while(itr.hasNext()) {
         ProfileConnectionEntry entry = (ProfileConnectionEntry)itr.next();
         if (entry.getHost_name().equalsIgnoreCase(host_name)) {
            return entry;
         }
      }

      return null;
   }

   public void updateList() {
      HashSet envList = IdePlugin.getDefault().getEnvSet();
      this.connections.clear();
      Iterator itr = envList.iterator();

      while(itr.hasNext()) {
         ProfileEnvEntry entry = (ProfileEnvEntry)itr.next();
         ProfileConnectionEntry connEntry = new ProfileConnectionEntry(entry);
         this.addConnection(connEntry);
      }

   }
}
