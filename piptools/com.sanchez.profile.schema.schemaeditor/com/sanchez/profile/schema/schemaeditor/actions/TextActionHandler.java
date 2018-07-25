package com.sanchez.profile.schema.schemaeditor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Event;

public class TextActionHandler {
   public SourceViewer viewer;
   public TextActionHandler.CutActionHandler textCutAction = new TextActionHandler.CutActionHandler();
   public TextActionHandler.CopyActionHandler textCopyAction = new TextActionHandler.CopyActionHandler();
   public TextActionHandler.PasteActionHandler textPasteAction = new TextActionHandler.PasteActionHandler();
   public TextActionHandler.SelectAllActionHandler textSelectAllAction = new TextActionHandler.SelectAllActionHandler();

   public TextActionHandler(SourceViewer viewer) {
      this.viewer = viewer;
   }

   private class CopyActionHandler extends Action {
      protected CopyActionHandler() {
         this.setId("TextCopyActionHandler");
      }

      public void runWithEvent(Event event) {
         TextActionHandler.this.viewer.getTextWidget().copy();
      }
   }

   private class CutActionHandler extends Action {
      protected CutActionHandler() {
         this.setId("TextCutActionHandler");
      }

      public void runWithEvent(Event event) {
         TextActionHandler.this.viewer.getTextWidget().cut();
      }
   }

   private class PasteActionHandler extends Action {
      protected PasteActionHandler() {
         this.setId("TextPasteActionHandler");
      }

      public void runWithEvent(Event event) {
         TextActionHandler.this.viewer.getTextWidget().paste();
      }
   }

   private class SelectAllActionHandler extends Action {
      protected SelectAllActionHandler() {
         this.setId("TextSelectAllActionHandler");
      }

      public void runWithEvent(Event event) {
         TextActionHandler.this.viewer.getTextWidget().selectAll();
      }
   }
}
