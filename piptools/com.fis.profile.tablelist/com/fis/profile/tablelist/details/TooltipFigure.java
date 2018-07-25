package com.fis.profile.tablelist.details;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;

public class TooltipFigure extends Figure {
   public TooltipFigure(Label desc) {
      ToolbarLayout layout = new ToolbarLayout();
      this.setLayoutManager(layout);
      this.add(desc);
   }
}
