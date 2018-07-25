package com.fnf.gtm.meditor.editor;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

public class MTreeViewer extends TreeViewer {
   private Item fReusedExpandedItem;

   public MTreeViewer(Tree tree) {
      super(tree);
      this.setAutoExpandLevel(2);
   }

   public void reconcile(final MElementList delta) {
      if (this.getSorter() == null) {
         Display d = this.getTree().getDisplay();
         d.asyncExec(new Runnable() {
            public void run() {
               Widget w = MTreeViewer.this.findItem(delta);
               if (w != null) {
                  MTreeViewer.this.update(w, delta);
               }

            }
         });
      } else {
         this.refresh();
      }

   }

   protected void reuseTreeItem(Item item, Object element) {
      Item[] c = this.getChildren(item);
      if (c != null && c.length > 0) {
         if (this.getExpanded(item)) {
            this.fReusedExpandedItem = item;
         }

         for(int k = 0; k < c.length; ++k) {
            if (c[k].getData() != null) {
               this.disassociate(c[k]);
            }

            c[k].dispose();
         }
      }

      this.setExpanded(item, this.getExpanded(item));
      this.updateItem(item, element);
      this.updatePlus(item, element);
      this.internalExpandToLevel(item, 0);
      this.fReusedExpandedItem = null;
   }

   protected void createTreeItem(Widget parent, Object element, int ix) {
      try {
         Item[] children = this.getChildren(parent);
         boolean expand = parent instanceof Item && (children == null || children.length == 0);
         Item item;
         if (ix == -1) {
            item = this.newItem(parent, 0, children.length);
         } else {
            item = this.newItem(parent, 0, ix);
         }

         this.updateItem(item, element);
         this.updatePlus(item, element);
         if (expand) {
            this.setExpanded((Item)parent, true);
         }

         this.internalExpandToLevel(item, 0);
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   protected boolean filtered(MElementList parent, MElementList child) {
      Object[] result = new Object[]{child};
      ViewerFilter[] filters = this.getFilters();

      for(int i = 0; i < filters.length; ++i) {
         result = filters[i].filter(this, parent, result);
         if (result.length == 0) {
            return true;
         }
      }

      return false;
   }

   protected void update(Widget w, MElementList delta) {
      try {
         if (w == null) {
            return;
         }

         MElementList[] affected = delta.getChildren();
         Item[] children = this.getChildren(w);

         int i;
         for(i = 0; i < children.length; ++i) {
            Item item = children[i];
            if (i < affected.length) {
               MElementList affectedElement = affected[i];
               if (item != null) {
                  if (item.getData() != null) {
                     if (affectedElement.toString().equals(((MElementList)item.getData()).toString())) {
                        item.setData(affectedElement);
                     } else {
                        this.reuseTreeItem(item, affectedElement);
                     }

                     if (this.getExpanded(item)) {
                        this.update(item, affectedElement);
                     } else {
                        Item[] c = this.getChildren(item);
                        if (c.length > 0 && c.length != affectedElement.getChildren().length) {
                           this.reuseTreeItem(item, affectedElement);
                        }
                     }

                     this.updatePlus(item, affectedElement);
                  } else {
                     this.reuseTreeItem(item, affectedElement);
                     this.updatePlus(item, affectedElement);
                  }
               }
            } else {
               this.disassociate(item);
               item.dispose();
            }
         }

         for(i = children.length; i < affected.length; ++i) {
            this.createTreeItem(w, affected[i], -1);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }
}
