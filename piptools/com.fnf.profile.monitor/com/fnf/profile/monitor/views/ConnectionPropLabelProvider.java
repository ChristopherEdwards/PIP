package com.fnf.profile.monitor.views;

import com.fnf.profile.monitor.MonitorPlugin;
import com.fnf.profile.monitor.connprop.util.ProfileConnectionEntry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ConnectionPropLabelProvider extends LabelProvider implements ITableLabelProvider {
   private final String ok_Image = "OK_IMAGE";
   private final String failed_Image = "FAILED_IMAGE";

   public Image getColumnImage(Object element, int columnIndex) {
      ImageRegistry imageRegistry = MonitorPlugin.getDefault().getImageRegistry();
      ImageDescriptor descriptorOK = null;
      descriptorOK = MonitorPlugin.getImageDescriptor("icons/ok.gif");
      ImageDescriptor descriptorFailed = null;
      descriptorFailed = MonitorPlugin.getImageDescriptor("icons/failed.gif");
      Image image = null;
      ProfileConnectionEntry env = (ProfileConnectionEntry)element;
      String result = "";
      switch(columnIndex) {
      case 4:
         result = env.getPing_status();
         if (result != null && result != "") {
            if (result.equals("OK")) {
               image = imageRegistry.get("OK_IMAGE");
               if (image == null) {
                  image = descriptorOK.createImage();
                  imageRegistry.put("OK_IMAGE", image);
               }
            } else {
               image = imageRegistry.get("FAILED_IMAGE");
               if (image == null) {
                  image = descriptorFailed.createImage();
                  imageRegistry.put("FAILED_IMAGE", image);
               }
            }
         }
         break;
      case 5:
         result = env.getMtm_status();
         if (result != null && result != "") {
            if (result.equals("OK")) {
               image = imageRegistry.get("OK_IMAGE");
               if (image == null) {
                  image = descriptorOK.createImage();
                  imageRegistry.put("OK_IMAGE", image);
               }
            } else {
               image = imageRegistry.get("FAILED_IMAGE");
               if (image == null) {
                  image = descriptorFailed.createImage();
                  imageRegistry.put("FAILED_IMAGE", image);
               }
            }
         }
         break;
      case 6:
         result = env.getIbs_server_count();
         if (result != null && result != "") {
            if (result.equals("-1")) {
               image = imageRegistry.get("FAILED_IMAGE");
               if (image == null) {
                  image = descriptorFailed.createImage();
                  imageRegistry.put("FAILED_IMAGE", image);
               }
            } else {
               image = imageRegistry.get("OK_IMAGE");
               if (image == null) {
                  image = descriptorOK.createImage();
                  imageRegistry.put("OK_IMAGE", image);
               }
            }
         }
         break;
      case 7:
         result = env.getSystem_date();
         if (result != null && result != "" && result.equals("-1")) {
            image = imageRegistry.get("FAILED_IMAGE");
            if (image == null) {
               image = descriptorFailed.createImage();
               imageRegistry.put("FAILED_IMAGE", image);
            }
         }
         break;
      case 8:
         result = env.getError_count();
         if (result != null && result != "" && result.equals("-1")) {
            image = imageRegistry.get("FAILED_IMAGE");
            if (image == null) {
               image = descriptorFailed.createImage();
               imageRegistry.put("FAILED_IMAGE", image);
            }
         }
      }

      return image;
   }

   public String getColumnText(Object element, int columnIndex) {
      String result = "";
      ProfileConnectionEntry env = (ProfileConnectionEntry)element;
      switch(columnIndex) {
      case 0:
         result = env.getHost_name();
         break;
      case 1:
         result = env.getServer_name();
         break;
      case 2:
         result = env.getIbs_server_name();
         break;
      case 3:
         result = env.getPort_number();
      case 4:
      case 5:
      case 6:
      default:
         break;
      case 7:
         result = env.getSystem_date();
         if (result.equals("-1")) {
            result = "";
         }
         break;
      case 8:
         result = env.getError_count();
         if (result.equals("-1")) {
            result = "";
         }
      }

      return result;
   }
}
