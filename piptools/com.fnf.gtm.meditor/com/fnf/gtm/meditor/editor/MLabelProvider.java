package com.fnf.gtm.meditor.editor;

import com.fnf.gtm.meditor.MEditorPlugin;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class MLabelProvider extends LabelProvider {
   private Map imageCache = new HashMap(11);

   public Image getImage(Object element) {
      ImageDescriptor descriptor = null;
      if (element instanceof MarkElement) {
         descriptor = MEditorPlugin.getImageDescriptor("gtmTag.gif");
         Image image = (Image)this.imageCache.get(descriptor);
         if (image == null) {
            image = descriptor.createImage();
            this.imageCache.put(descriptor, image);
         }

         return image;
      } else {
         throw this.unknownElement(element);
      }
   }

   public String getText(Object element) {
      if (element instanceof MarkElement) {
         return ((MarkElement)element).getName() == null ? "GTM" : ((MarkElement)element).getName();
      } else {
         throw this.unknownElement(element);
      }
   }

   public void dispose() {
      Iterator i = this.imageCache.values().iterator();

      while(i.hasNext()) {
         ((Image)i.next()).dispose();
      }

      this.imageCache.clear();
   }

   protected RuntimeException unknownElement(Object element) {
      return new RuntimeException("Unknown type of element in tree of type " + element.getClass().getName());
   }
}
