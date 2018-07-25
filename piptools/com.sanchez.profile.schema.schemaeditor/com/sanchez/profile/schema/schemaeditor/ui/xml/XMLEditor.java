package com.sanchez.profile.schema.schemaeditor.ui.xml;

import com.fnf.profile.ide.actions.ActionSendToProfile;
import com.sanchez.profile.schema.schemaeditor.actions.ActionSaveFile;
import org.apache.xerces.dom.DocumentImpl;
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
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

public class XMLEditor extends TextEditor {
   private ColorManager colorManager = new ColorManager();
   private DocumentImpl document;
   boolean editable = false;
   LineNumberRulerColumn fLineNumberRulerColumn;

   public XMLEditor(boolean editable) {
      this.setSourceViewerConfiguration(new XMLConfiguration(this.colorManager));
      this.setDocumentProvider(new XMLDocumentProvider());
      this.editable = editable;
   }

   public XMLEditor() {
      this.setSourceViewerConfiguration(new XMLConfiguration(this.colorManager));
      this.setDocumentProvider(new XMLDocumentProvider());
      this.editable = true;
   }

   public void dispose() {
      this.colorManager.dispose();
      super.dispose();
   }

   protected void editorContextMenuAboutToShow(IMenuManager parentMenu) {
      parentMenu.add(new Separator("group.undo"));
      parentMenu.add(new Separator("group.copy"));
      parentMenu.add(new Separator("group.print"));
      parentMenu.add(new Separator("group.edit"));
      parentMenu.add(new Separator("group.find"));
      parentMenu.add(new Separator("group.add"));
      parentMenu.add(new Separator("group.rest"));
      parentMenu.add(new Separator("additions"));
      parentMenu.add(new Separator("group.save"));
      this.addAction(parentMenu, "group.undo", ITextEditorActionConstants.REVERT_TO_SAVED);
      this.addAction(parentMenu, "group.copy", ITextEditorActionConstants.SELECT_ALL);
      this.addAction(parentMenu, "group.copy", ITextEditorActionConstants.COPY);
      this.addAction(parentMenu, "group.copy", ITextEditorActionConstants.PASTE);
      this.addAction(parentMenu, "group.print", ITextEditorActionConstants.PRINT);
      ActionSaveFile saveAction = new ActionSaveFile(this, "Save");
      saveAction.setEnabled(false);
      parentMenu.appendToGroup("group.save", saveAction);
      if (this.isDirty()) {
         saveAction.setEnabled(true);
      }

      ActionSendToProfile sendToProfileAction = new ActionSendToProfile(this, "Send to Profile");
      sendToProfileAction.setEnabled(true);
      parentMenu.appendToGroup("group.save", sendToProfileAction);
      if (this.isDirty()) {
         sendToProfileAction.setEnabled(false);
      }

   }

   public void doSave(IProgressMonitor monitor) {
      super.doSave(monitor);
   }

   public void doSaveAs() {
      super.doSaveAs();
   }

   public void createPartControl(Composite parent) {
      super.createPartControl(parent);
      this.getSourceViewer().setEditable(this.editable);
   }

   public void doSetInput(IEditorInput input) throws CoreException {
      super.doSetInput(input);
   }

   protected IVerticalRulerColumn createLineNumberRulerColumn() {
      this.fLineNumberRulerColumn = new LineNumberRulerColumn();
      this.initializeLineNumberRulerColumn(this.fLineNumberRulerColumn);
      return this.fLineNumberRulerColumn;
   }

   protected void initializeLineNumberRulerColumn(LineNumberRulerColumn rulerColumn) {
      rulerColumn.setForeground(this.colorManager.getColor(new RGB(50, 50, 50)));
      rulerColumn.setBackground(this.colorManager.getColor(new RGB(238, 238, 238)));
   }

   protected IVerticalRuler createVerticalRuler() {
      CompositeRuler ruler = new CompositeRuler();
      ruler.addDecorator(0, new AnnotationRulerColumn(12));
      ruler.addDecorator(1, this.createLineNumberRulerColumn());
      return ruler;
   }
}
