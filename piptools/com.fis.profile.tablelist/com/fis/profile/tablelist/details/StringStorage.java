package com.fis.profile.tablelist.details;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.PlatformObject;

class StringStorage extends PlatformObject implements IStorage {
   private String string;
   private String name;

   StringStorage(String input, String name) {
      this.string = input;
      this.name = name;
   }

   public InputStream getContents() throws CoreException {
      return new ByteArrayInputStream(this.string.getBytes());
   }

   public IPath getFullPath() {
      return null;
   }

   public String getName() {
      return this.name + ".PROC";
   }

   public boolean isReadOnly() {
      return true;
   }
}
