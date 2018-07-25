package com.sanchez.profile.schema.schemaeditor.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class HelpWindow extends Dialog {
   private String title;
   private String input;

   public HelpWindow(Shell parentShell, String input, String title) {
      super(parentShell);
      this.input = input;
      this.title = title;
   }

   protected Control createContents(Composite parent) {
      final Shell shell = this.getShell();
      this.getShell().setText("Help Information for " + this.title);
      parent.setBackground(this.getShell().getDisplay().getSystemColor(9));
      Composite container = new Composite(parent, 16384);
      container.addKeyListener(new KeyAdapter() {
         public void keyReleased(KeyEvent e) {
            if (e.character == 27) {
               shell.dispose();
            }

         }
      });
      GridLayout layout = new GridLayout();
      layout.marginWidth = 0;
      layout.marginHeight = 0;
      container.setLayout(layout);
      container.setLayoutData(new GridData(1808));
      GridData data = new GridData(1808);
      data.heightHint = 450;
      data.widthHint = 650;
      container.setLayoutData(data);
      Text text = new Text(container, 770);
      new FontData("Courier", 8, 0);
      text.setFont(new Font((Device)null, "Courier", 8, 0));
      text.setText(this.input);
      text.setBackground(new Color((Device)null, new RGB(243, 240, 229)));
      data = new GridData(1808);
      text.setLayoutData(data);
      return container;
   }
}
