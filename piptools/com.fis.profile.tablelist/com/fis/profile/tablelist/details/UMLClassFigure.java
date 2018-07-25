package com.fis.profile.tablelist.details;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;

public class UMLClassFigure extends Figure {
   public static Color classColor = new Color((Device)null, 255, 255, 206);
   private CompartmentFigure attributeFigure = new CompartmentFigure();
   private CompartmentFigure methodFigure = new CompartmentFigure();

   public UMLClassFigure(Label name, Label desc) {
      ToolbarLayout layout = new ToolbarLayout();
      this.setLayoutManager(layout);
      this.setBorder(new LineBorder(ColorConstants.black, 2));
      this.setBackgroundColor(classColor);
      this.setOpaque(true);
      this.add(name);
      this.add(this.attributeFigure);
      this.add(this.methodFigure);
      if (desc.getText().length() > 0) {
         TooltipFigure toolTip = new TooltipFigure(desc);
         toolTip.setPreferredSize(220, 15);
         toolTip.repaint();
         this.setToolTip(toolTip);
      }

   }

   public CompartmentFigure getAttributesCompartment() {
      return this.attributeFigure;
   }

   public CompartmentFigure getMethodsCompartment() {
      return this.methodFigure;
   }
}
