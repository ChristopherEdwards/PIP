package com.fis.profile.tablelist.dialogs;

import com.fnf.profile.monitor.connprop.util.ProfileConnectionEntry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ConnectionPropLabelProvider extends LabelProvider implements ITableLabelProvider {
   private final String ok_Image = "OK_IMAGE";
   private final String failed_Image = "FAILED_IMAGE";

   public Image getColumnImage(Object element, int columnIndex) {
      return null;
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
