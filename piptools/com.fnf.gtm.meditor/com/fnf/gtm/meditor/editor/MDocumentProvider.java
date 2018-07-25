package com.fnf.gtm.meditor.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class MDocumentProvider extends FileDocumentProvider {
   IDocument document;

   protected IDocument createDocument(Object element) throws CoreException {
      this.document = super.createDocument(element);
      if (this.document != null) {
         IDocumentPartitioner partitioner = new FastPartitioner(new MPartitionScanner(), new String[]{"__gtm_comment"});
         partitioner.connect(this.document);
         this.document.setDocumentPartitioner(partitioner);
      }

      return this.document;
   }
}
