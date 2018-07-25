package com.fnf.ui.editor.simplexml;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.source.AnnotationRulerColumn;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.IVerticalRulerColumn;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

public class SimpleXMLEditor extends TextEditor implements IEditorPart {
   private ColorManager colorManager;
   private boolean editable;

   public SimpleXMLEditor() {
      this.colorManager = new ColorManager();
      this.setSourceViewerConfiguration(new XMLConfiguration(this.colorManager));
      this.setDocumentProvider(new XMLDocumentProvider());
   }

   public SimpleXMLEditor(boolean editable) {
      this.editable = editable;
      this.colorManager = new ColorManager();
      this.setSourceViewerConfiguration(new XMLConfiguration(this.colorManager));
      this.setDocumentProvider(new XMLDocumentProvider());
   }

   protected IVerticalRulerColumn createLineNumberRulerColumn() {
      this.fLineNumberRulerColumn = new LineNumberRulerColumn();
      this.initializeLineNumberRulerColumn(this.fLineNumberRulerColumn);
      return this.fLineNumberRulerColumn;
   }

   public void createPartControl(Composite parent) {
      super.createPartControl(parent);
      this.getSourceViewer().setEditable(this.editable);
   }

   protected IVerticalRuler createVerticalRuler() {
      CompositeRuler ruler = new CompositeRuler();
      ruler.addDecorator(0, new AnnotationRulerColumn(12));
      ruler.addDecorator(1, this.createLineNumberRulerColumn());
      return ruler;
   }

   public void dispose() {
      this.colorManager.dispose();
      super.dispose();
   }

   public void doSave(IProgressMonitor monitor) {
      super.doSave(monitor);
   }

   public void doSaveAs() {
      super.doSaveAs();
   }

   public void doSetInput(IEditorInput input) throws CoreException {
      super.doSetInput(input);
   }

   protected void editorContextMenuAboutToShow(IMenuManager parentMenu) {
      parentMenu.add(new Separator("group.copy"));
      parentMenu.add(new Separator("group.print"));
      parentMenu.add(new Separator("additions"));
      parentMenu.add(new Separator("group.save"));
      this.addAction(parentMenu, "group.copy", ITextEditorActionConstants.SELECT_ALL);
      this.addAction(parentMenu, "group.copy", ITextEditorActionConstants.COPY);
      this.addAction(parentMenu, "group.copy", ITextEditorActionConstants.PASTE);
      this.addAction(parentMenu, "group.print", ITextEditorActionConstants.PRINT);
   }

   protected void initializeLineNumberRulerColumn(LineNumberRulerColumn rulerColumn) {
      rulerColumn.setForeground(this.colorManager.getColor(new RGB(50, 50, 50)));
      rulerColumn.setBackground(this.colorManager.getColor(new RGB(238, 238, 238)));
   }
}
