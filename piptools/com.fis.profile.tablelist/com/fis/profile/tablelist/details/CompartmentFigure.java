package com.fis.profile.tablelist.details;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;

public class CompartmentFigure extends Figure {
   public CompartmentFigure() {
      ToolbarLayout layout = new ToolbarLayout();
      layout.setMinorAlignment(1);
      layout.setStretchMinorAxis(false);
      layout.setSpacing(2);
      this.setLayoutManager(layout);
      this.setBorder(new CompartmentFigure.CompartmentFigureBorder());
   }

   public class CompartmentFigureBorder extends AbstractBorder {
      public Insets getInsets(IFigure figure) {
         return new Insets(1, 0, 0, 0);
      }

      public void paint(IFigure figure, Graphics graphics, Insets insets) {
      }
   }
}
