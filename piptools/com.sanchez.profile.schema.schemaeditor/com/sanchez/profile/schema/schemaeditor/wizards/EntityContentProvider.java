package com.sanchez.profile.schema.schemaeditor.wizards;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

public class EntityContentProvider implements IStructuredContentProvider, IEntityListViewer {
   private EntityAttributeList attributeList;
   private TableViewer tableViewer;

   public EntityContentProvider(EntityAttributeList attributeList, TableViewer tableViewer) {
      this.attributeList = attributeList;
      this.tableViewer = tableViewer;
   }

   public EntityContentProvider() {
   }

   public Object[] getElements(Object input) {
      return this.attributeList.getAttributes().size() > 0 ? this.attributeList.getAttributes().toArray() : null;
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      if (newInput != null) {
         ((EntityAttributeList)newInput).addChangeListener(this);
      }

      if (oldInput != null) {
         ((EntityAttributeList)oldInput).removeChangeListener(this);
      }

   }

   public void dispose() {
   }

   public void addAttribute(EntityTableEntry entityTableEntry) {
      this.tableViewer.add(entityTableEntry);
   }

   public void removeAttribute(EntityTableEntry entityTableEntry) {
      this.tableViewer.remove(entityTableEntry);
   }

   public void updateAttribute(EntityTableEntry entityTableEntry) {
      this.tableViewer.update(entityTableEntry, (String[])null);
   }
}
