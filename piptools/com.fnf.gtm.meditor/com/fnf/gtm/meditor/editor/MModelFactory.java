package com.fnf.gtm.meditor.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

public class MModelFactory {
   private static MModelFactory instance = new MModelFactory();
   private boolean registryLoaded = false;
   IMFileParser parser = null;

   protected void addSections(AdaptableList list, MarkElement element) {
      list.add((IAdaptable)element);
      Object[] children = element.getChildren(element);

      for(int i = 0; i < children.length; ++i) {
         this.addSections(list, (MarkElement)children[i]);
      }

   }

   public AdaptableList getContentOutline(IDocument document) {
      return new AdaptableList(this.getToc(document));
   }

   public static MModelFactory getInstance() {
      return instance;
   }

   public AdaptableList getSections(IDocument document) {
      MarkElement[] topLevel = this.getToc(document);
      AdaptableList list = new AdaptableList();

      for(int i = 0; i < topLevel.length; ++i) {
         this.addSections(list, topLevel[i]);
      }

      return list;
   }

   public AdaptableList getSections(ISelection sel) {
      if (!(sel instanceof IStructuredSelection)) {
         return null;
      } else {
         IStructuredSelection structured = (IStructuredSelection)sel;
         Object object = structured.getFirstElement();
         if (object instanceof IDocument) {
            IDocument document = (IDocument)object;
            return this.getSections(document);
         } else {
            return null;
         }
      }
   }

   private MarkElement[] getToc(IDocument document) {
      if (!this.registryLoaded) {
         this.loadParser();
      }

      return this.parser.parse(document);
   }

   private void loadParser() {
      IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
      IExtensionPoint point = pluginRegistry.getExtensionPoint("com.fnf.gtm.meditor", "sectionParser");
      if (point != null) {
         IExtension[] extensions = point.getExtensions();

         for(int i = 0; i < extensions.length; ++i) {
            IExtension currentExtension = extensions[i];
            if (i == extensions.length - 1) {
               IConfigurationElement[] configElements = currentExtension.getConfigurationElements();

               for(int j = 0; j < configElements.length; ++j) {
                  IConfigurationElement config = configElements[i];
                  if (config.getName().equals("parser")) {
                     this.processParserElement(config);
                     break;
                  }
               }
            }
         }
      }

      if (this.parser == null) {
         this.parser = new DefaultSectionsParser();
      }

      this.registryLoaded = true;
   }

   private void processParserElement(IConfigurationElement element) {
      try {
         this.parser = (IMFileParser)element.createExecutableExtension("class");
      } catch (CoreException var2) {
         this.parser = null;
      }

   }
}
