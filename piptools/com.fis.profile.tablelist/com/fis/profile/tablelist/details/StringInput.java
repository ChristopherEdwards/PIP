package com.fis.profile.tablelist.details;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

class StringInput extends PlatformObject implements IStorageEditorInput {
   private IStorage storage;

   StringInput(IStorage storage) {
      this.storage = storage;
   }

   public boolean exists() {
      return true;
   }

   public ImageDescriptor getImageDescriptor() {
      return null;
   }

   public String getName() {
      return this.storage.getName();
   }

   public IPersistableElement getPersistable() {
      return null;
   }

   public IStorage getStorage() {
      return this.storage;
   }

   public String getToolTipText() {
      return "String-based file: " + this.storage.getName();
   }
}
