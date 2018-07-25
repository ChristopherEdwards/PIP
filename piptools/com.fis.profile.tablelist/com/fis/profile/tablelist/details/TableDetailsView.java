package com.fis.profile.tablelist.details;

import com.fis.profile.tablelist.TableList;
import com.fis.profile.tablelist.dialogs.UpdateForeignKey;
import com.fis.profile.tablelist.dialogs.UpdateIndex;
import com.fis.profile.tablelist.dialogs.UpdateJournal;
import com.fis.profile.tablelist.dialogs.UpdateTable;
import com.fis.profile.tablelist.utils.GetDataList;
import com.fis.profile.tablelist.views.TableListView;
import java.util.ArrayList;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class TableDetailsView extends ViewPart implements Listener {
   private Composite container;
   private TableViewer indexListTblViewer = null;
   private TableViewer foreignListTblViewer = null;
   private TableViewer journalListTblViewer = null;
   private String[] tableColumnProps = new String[]{"File Name", "Description", "Primary Keys", "ASCII Delimiter", "Deletion Restriction Flag", "Look-up Table List", "Look-up Table List (Line 2)", "Default Heading", "Descending Order", "Record Existed Indicator (node number)", "Extended File and Column Names Allowed", "Documentation File Name", "File Type", "Data Item Protection Filename", "File Short Name", "Global Name", "MUMPS Global Reference", "Default Data Item List", "Required Data Item List", "Enable Automatic Logging", "Last Updated", "Implicit Data Dictionary Reference", "Network Location", "Inheritance File Name", "Data Entry Pre-Processor", "Data Item Name (Time Created)", "Data Item Name (Time Last Updated)", "Data Item Name (Date Created)", "Data Item Name (Date Last Updated)", "Data Item Name (User Created)", "Date Item Name (User Last Updated)", "Query", "Record Type", "File Maintenance Restriction Flag", "File Maintenance Data Entry Screen IDs", "System Name", "Primary Keys Access Routine", "Record Filer Routine", "User Defined File Post Processor", "User Defined Authorization Routine", "User ID"};
   private String[] indexColumnProps = new String[]{"Index Description", "Allow NULL Value", "Order By", "Supertype File Name", "Query Definition #1", "Query Definition #2", "Save Old DAYEND Index Entry", "Convert to Uppercase", "Global Name"};
   private String[] foreignColumnProps = new String[]{"Primary Keys", "Maximum Occurrences in Referencing Table", "Minimum Occurrences in Referencing Table", "Maximum Occurrences in Foreign Table", "Minimum Occurrences in Foreign Table", "Foreign File Name", "Update Constraint", "Delete Constraint"};
   private String[] journalColumnProps = new String[]{"Description", "Journal Table Name", "Effective-Dated Option", "Transaction Type", "Processing Mode", "Exclude Columns", "Include Columns", "Priority (processing order)", "Query Condition 1", "Query Condition 2", "Supertype File Name"};
   private ArrayList objectIndex = new ArrayList();
   private ArrayList objectForeign = new ArrayList();
   private ArrayList objectJournal = new ArrayList();
   private ArrayList objectList0 = new ArrayList();
   private ArrayList objectList1 = new ArrayList();
   private ArrayList objectList2 = new ArrayList();
   private ArrayList objectList3 = new ArrayList();
   private ArrayList diagramExportList = new ArrayList();
   private ArrayList diagramImportList = new ArrayList();
   private TableViewer columnDetail0Viewer = null;
   private TableViewer columnDetail1Viewer = null;
   private TableViewer columnDetail2Viewer = null;
   private TableViewer columnDetail3Viewer = null;
   private TabFolder tabFolder;
   private TabFolder subTabFolder;
   private Text tableDoc;
   private String strDoc = "";
   private String tableName = "";
   private String selectedFieldLabel = "";
   private Button indexAddButton;
   private Button indexEditButton;
   private Button foreignKeyAddButton;
   private Button foreignKeyEditButton;
   private Button journalAddButton;
   private Button journalEditButton;
   private Button indexDeleteButton;
   private Button foreignKeyDeleteButton;
   private Button journalDeleteButton;
   private Button radIndex;
   private Button radForeignKey;
   private Button radJournal;
   private LightweightSystem lwsImport;
   private LightweightSystem lwsExport;
   private Canvas importCanvas;
   private Canvas exportCanvas;
   private Figure importContents;
   private Figure exportContents;
   private ScrollPane importPane;
   private ScrollPane exportPane;
   private static String PROPERTY = "Table Properties";
   private static String FOREIGNKEY = "Foreign Key";
   private static String DOCUMENT = "Table Documentation";
   private static String JOURNAL = "Journal";
   private static String REFERENCES = "References";
   private static String INDEX = "Index";
   private static String IMPORT = "Import";
   private static String EXPORT = "Export";

   public TableDetailsView() {
      this.tableName = TableList.getSelectedTableName();
      this.objectList0 = TableList.getTableDetails();
      PlatformUI.getWorkbench().addWindowListener(new IWindowListener() {
         public void windowActivated(IWorkbenchWindow window) {
            TableDetailsView.this.rePaintGraph();
         }

         public void windowClosed(IWorkbenchWindow window) {
         }

         public void windowDeactivated(IWorkbenchWindow window) {
         }

         public void windowOpened(IWorkbenchWindow window) {
         }
      });
   }

   private void rePaintGraph() {
      if (this.tabFolder != null && this.tabFolder.getSelectionIndex() == 5) {
         if (this.subTabFolder != null && this.subTabFolder.getSelectionIndex() == 0) {
            this.lwsImport.setContents(this.importPane);
         }

         if (this.subTabFolder != null && this.subTabFolder.getSelectionIndex() == 1) {
            this.lwsExport.setContents(this.exportPane);
         }

      }
   }

   public void createPartControl(Composite parent) {
      int groupSpan = 1;
      Composite composite = new Composite(parent, 0);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      composite.setLayout(gridLayout);
      GridData gridData = new GridData(768);
      composite.setLayoutData(gridData);
      this.tabFolder = new TabFolder(composite, 0);
      GridData gd = new GridData(1808);
      this.tabFolder.setLayoutData(gd);
      Composite detailsTab = new Composite(this.tabFolder, 0);
      GridLayout gl = new GridLayout();
      gl.numColumns = 1;
      detailsTab.setLayout(gl);
      this.createTableDetails(detailsTab, groupSpan);
      TabItem tabItem1 = new TabItem(this.tabFolder, 0);
      tabItem1.setText(PROPERTY);
      tabItem1.setControl(detailsTab);
      Composite indexTab = new Composite(this.tabFolder, 0);
      gl.numColumns = 1;
      indexTab.setLayout(gl);
      this.createIndexList(indexTab, 2);
      TabItem tabItem2 = new TabItem(this.tabFolder, 0);
      tabItem2.setText(INDEX);
      tabItem2.setControl(indexTab);
      Composite keysTab = new Composite(this.tabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = 1;
      keysTab.setLayout(gl);
      this.createForeignKeyList(keysTab, 2);
      TabItem tabItem3 = new TabItem(this.tabFolder, 0);
      tabItem3.setText(FOREIGNKEY);
      tabItem3.setControl(keysTab);
      Composite journalTab = new Composite(this.tabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = 1;
      journalTab.setLayout(gl);
      this.createJournalList(journalTab, 2);
      TabItem tabItem4 = new TabItem(this.tabFolder, 0);
      tabItem4.setText(JOURNAL);
      tabItem4.setControl(journalTab);
      Composite docTab = new Composite(this.tabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = 2;
      docTab.setLayout(gl);
      this.tableDoc = new Text(docTab, 2562);
      gd = new GridData(1808);
      this.tableDoc.setLayoutData(gd);
      this.tableDoc.setText(this.strDoc);
      this.tableDoc.setTextLimit(8000);
      this.tableDoc.setEditable(true);
      Button updateButton = new Button(docTab, 8);
      updateButton.setText("Update");
      GridData buttonData = new GridData();
      buttonData.horizontalAlignment = 4;
      buttonData.verticalAlignment = 128;
      buttonData.widthHint = 85;
      updateButton.setLayoutData(buttonData);
      updateButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            Shell shell = new Shell();
            boolean ok = MessageDialog.openConfirm(shell, "Update Documentation", "Are you sure to update this documentation for table " + TableList.getSelectedTableName() + "?");
            if (ok) {
               String delStatement = "Delete From DBTBL1TBLDOC where DBTBL11D.FID = '" + TableDetailsView.this.tableName.toUpperCase() + "'";
               GetDataList dataList = new GetDataList(delStatement, (String[])null);
               dataList.updateRecord();
               char[] charlist = TableDetailsView.this.tableDoc.getText().toCharArray();
               int SEQ = 1;
               boolean nextLine = false;
               boolean hasEmptyLine = false;

               for(int i = 0; i < charlist.length; ++i) {
                  String value = "";
                  int charCount = 0;

                  for(boolean var13 = false; charCount < 90 && i < charlist.length; ++i) {
                     int asciiValue = charlist[i];
                     if ((asciiValue == '\n' || asciiValue == '\r') && nextLine) {
                        hasEmptyLine = true;
                     }

                     if (asciiValue == '\n' || asciiValue == '\r') {
                        nextLine = true;
                        ++i;
                        break;
                     }

                     value = value + charlist[i];
                     ++charCount;
                     nextLine = false;
                  }

                  String statement;
                  GetDataList dataList2;
                  if (hasEmptyLine) {
                     statement = "INSERT INTO DBTBL1TBLDOC (DES, FID, %LIBS, SEQ) VALUES (' ', '" + TableDetailsView.this.tableName + "', 'SYSDEV', '" + SEQ + "')";
                     dataList2 = new GetDataList(statement, (String[])null);
                     dataList2.updateRecord();
                     nextLine = false;
                     hasEmptyLine = false;
                  } else {
                     value = value.replace("'", "''");
                     statement = "INSERT INTO DBTBL1TBLDOC (DES, FID, %LIBS, SEQ) VALUES ('" + value + "', '" + TableDetailsView.this.tableName + "', 'SYSDEV', '" + SEQ + "')";
                     dataList2 = new GetDataList(statement, (String[])null);
                     dataList2.updateRecord();
                  }

                  ++SEQ;
                  boolean var16 = false;
               }

            }
         }
      });
      TabItem tabItem5 = new TabItem(this.tabFolder, 0);
      tabItem5.setText(DOCUMENT);
      tabItem5.setControl(docTab);
      Composite diagramTab = new Composite(this.tabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = 1;
      diagramTab.setLayout(gl);
      Composite radComposite = new Composite(diagramTab, 0);
      gl = new GridLayout();
      gl.numColumns = 3;
      radComposite.setLayout(gl);
      this.radForeignKey = new Button(radComposite, 16);
      this.radForeignKey.setText(FOREIGNKEY);
      this.radForeignKey.setSelection(true);
      this.radForeignKey.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            TableDetailsView.this.ReloadData(TableDetailsView.REFERENCES, false);
         }
      });
      this.radIndex = new Button(radComposite, 16);
      this.radIndex.setText(INDEX);
      this.radIndex.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            TableDetailsView.this.ReloadData(TableDetailsView.REFERENCES, false);
         }
      });
      this.radJournal = new Button(radComposite, 16);
      this.radJournal.setText(JOURNAL);
      this.radJournal.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            TableDetailsView.this.ReloadData(TableDetailsView.REFERENCES, false);
         }
      });
      this.subTabFolder = new TabFolder(diagramTab, 0);
      gd = new GridData(1808);
      this.subTabFolder.setLayoutData(gd);
      Composite importRefTab = new Composite(this.subTabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = 1;
      importRefTab.setLayout(gl);
      this.createImportDiagram(importRefTab);
      TabItem tabItem7 = new TabItem(this.subTabFolder, 0);
      tabItem7.setText(IMPORT);
      tabItem7.setControl(importRefTab);
      Composite exportRefTab = new Composite(this.subTabFolder, 0);
      gl = new GridLayout();
      gl.numColumns = 1;
      exportRefTab.setLayout(gl);
      this.createExportDiagram(exportRefTab);
      TabItem tabItem8 = new TabItem(this.subTabFolder, 0);
      tabItem8.setText(EXPORT);
      tabItem8.setControl(exportRefTab);
      this.subTabFolder.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            int selection = TableDetailsView.this.subTabFolder.getSelectionIndex();
            String header = TableDetailsView.this.subTabFolder.getItem(selection).getText();
            TableDetailsView.this.ReloadData(header, false);
         }
      });
      TabItem tabItem6 = new TabItem(this.tabFolder, 0);
      tabItem6.setText(REFERENCES);
      tabItem6.setControl(diagramTab);
      this.tabFolder.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            int selection = TableDetailsView.this.tabFolder.getSelectionIndex();
            String header = TableDetailsView.this.tabFolder.getItem(selection).getText();
            TableDetailsView.this.ReloadData(header, false);
         }
      });
   }

   private void createImportDiagram(Composite parent) {
      try {
         this.importCanvas = new Canvas(parent, 264192);
         this.importCanvas.setLayoutData(new GridData(4, 4, true, true));
         this.importCanvas.setBackgroundMode(0);
         this.lwsImport = new LightweightSystem(this.importCanvas);
         this.drawImportDiagram();
         this.lwsImport.setContents(this.importPane);
      } catch (Exception var2) {
         ;
      }

   }

   private void createExportDiagram(Composite parent) {
      try {
         this.exportCanvas = new Canvas(parent, 264192);
         this.exportCanvas.setLayoutData(new GridData(4, 4, true, true));
         this.exportCanvas.setBackground(this.tabFolder.getBackground());
         this.lwsExport = new LightweightSystem(this.exportCanvas);
         this.drawExportDiagram();
         this.lwsExport.setContents(this.exportPane);
      } catch (Exception var2) {
         ;
      }

   }

   private void drawExportDiagram() {
      final TableListView tableView = (TableListView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.views.TableListView");

      try {
         if (this.diagramExportList.size() == 0) {
            this.loadExportDiagramData();
         }

         if (this.diagramExportList.size() > 0) {
            this.diagramExportList.size();
         }

         this.exportPane = new ScrollPane();
         this.exportPane.setBounds(new Rectangle(0, 0, 200, 100));
         this.exportPane.setScrollBarVisibility(2);
         Label classLabel1 = new Label(TableList.getSelectedTableName());
         Label tooltipLabel = new Label(this.getTableDesc(TableList.getSelectedTableName()));
         UMLClassFigure classFigure = new UMLClassFigure(classLabel1, tooltipLabel);
         classFigure.addMouseListener(new MouseListener() {
            public void mouseDoubleClicked(MouseEvent me) {
               tableView.refresh(TableList.getSelectedTableName());
            }

            public void mousePressed(MouseEvent me) {
            }

            public void mouseReleased(MouseEvent me) {
            }
         });
         XYLayout contentsLayout = new XYLayout();
         this.exportContents = new Figure();
         this.exportContents.setLayoutManager(contentsLayout);
         if (TableList.getSelectedTableName().length() > 0) {
            this.exportContents.add(classFigure);
         }

         int leftLength = 0;

         for(int i = 0; i < this.diagramExportList.size(); ++i) {
            final String tableLabel = this.diagramExportList.get(i).toString();
            Label classLabel2 = new Label(tableLabel);
            tooltipLabel = new Label(this.getTableDesc(tableLabel));
            UMLClassFigure classFigure2 = new UMLClassFigure(classLabel2, tooltipLabel);
            classFigure2.addMouseListener(new MouseListener() {
               public void mouseDoubleClicked(MouseEvent me) {
                  tableView.refresh(tableLabel);
                  TableList.setSelectedTableName(tableLabel);
                  TableDetailsView.this.tableName = tableLabel;
                  TableDetailsView.this.ReloadData(TableDetailsView.EXPORT, true);
               }

               public void mousePressed(MouseEvent me) {
               }

               public void mouseReleased(MouseEvent me) {
               }
            });
            contentsLayout.setConstraint(classFigure2, new Rectangle(10, 40 * (i + 1), 100, 30));
            leftLength += 40;
            this.exportContents.add(classFigure2);
            PolylineConnection c = new PolylineConnection();
            ChopboxAnchor sourceAnchor = new ChopboxAnchor(classFigure);
            ChopboxAnchor targetAnchor = new ChopboxAnchor(classFigure2);
            c.setSourceAnchor(targetAnchor);
            c.setTargetAnchor(sourceAnchor);
            PolygonDecoration decoration = new PolygonDecoration();
            PointList decorationPointList = new PointList();
            decorationPointList.addPoint(0, 0);
            decorationPointList.addPoint(-1, 1);
            decorationPointList.addPoint(-1, -1);
            decoration.setTemplate(decorationPointList);
            c.setTargetDecoration(decoration);
            this.exportContents.add(c);
         }

         contentsLayout.setConstraint(classFigure, new Rectangle(350, leftLength / 2, 100, 30));
         this.exportPane.setContents(this.exportContents);
         this.lwsExport.setContents(this.exportPane);
      } catch (Exception var16) {
         var16.printStackTrace();
      }

   }

   private void drawImportDiagram() {
      final TableListView tableView = (TableListView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.fis.profile.tablelist.views.TableListView");

      try {
         if (this.diagramImportList.size() == 0) {
            this.loadImportDiagramData();
         }

         if (this.diagramImportList.size() > 0) {
            this.diagramImportList.size();
         }

         this.importPane = new ScrollPane();
         this.importPane.setBounds(new Rectangle(0, 0, 200, 100));
         this.importPane.setScrollBarVisibility(2);
         Label classLabel1 = new Label(TableList.getSelectedTableName());
         Label tooltipLabel = new Label(this.getTableDesc(TableList.getSelectedTableName()));
         UMLClassFigure classFigure = new UMLClassFigure(classLabel1, tooltipLabel);
         classFigure.addMouseListener(new MouseListener() {
            public void mouseDoubleClicked(MouseEvent me) {
               tableView.refresh(TableList.getSelectedTableName());
            }

            public void mousePressed(MouseEvent me) {
            }

            public void mouseReleased(MouseEvent me) {
            }
         });
         XYLayout contentsLayout = new XYLayout();
         this.importContents = new Figure();
         this.importContents.setLayoutManager(contentsLayout);
         if (TableList.getSelectedTableName().length() > 0) {
            this.importContents.add(classFigure);
         }

         int rightLength = 0;

         for(int i = 0; i < this.diagramImportList.size(); ++i) {
            final String tableLabel = this.diagramImportList.get(i).toString();
            Label classLabel2 = new Label(tableLabel);
            tooltipLabel = new Label(this.getTableDesc(tableLabel));
            UMLClassFigure classFigure2 = new UMLClassFigure(classLabel2, tooltipLabel);
            classFigure2.addMouseListener(new MouseListener() {
               public void mouseDoubleClicked(MouseEvent me) {
                  tableView.refresh(tableLabel);
                  TableList.setSelectedTableName(tableLabel);
                  TableDetailsView.this.tableName = tableLabel;
                  TableDetailsView.this.ReloadData(TableDetailsView.IMPORT, true);
               }

               public void mousePressed(MouseEvent me) {
               }

               public void mouseReleased(MouseEvent me) {
               }
            });
            contentsLayout.setConstraint(classFigure2, new Rectangle(350, 40 * (i + 1), 100, 30));
            rightLength += 40;
            this.importContents.add(classFigure2);
            PolylineConnection c = new PolylineConnection();
            ChopboxAnchor sourceAnchor = new ChopboxAnchor(classFigure);
            ChopboxAnchor targetAnchor = new ChopboxAnchor(classFigure2);
            c.setSourceAnchor(sourceAnchor);
            c.setTargetAnchor(targetAnchor);
            PolygonDecoration decoration = new PolygonDecoration();
            PointList decorationPointList = new PointList();
            decorationPointList.addPoint(0, 0);
            decorationPointList.addPoint(-1, 1);
            decorationPointList.addPoint(-1, -1);
            decoration.setTemplate(decorationPointList);
            c.setTargetDecoration(decoration);
            this.importContents.add(c);
         }

         contentsLayout.setConstraint(classFigure, new Rectangle(10, rightLength / 2, 100, 30));
         this.importPane.setContents(this.importContents);
      } catch (Exception var16) {
         var16.printStackTrace();
      }

   }

   private void ReloadData(String header, boolean isRefresh) {
      String statement = "";
      if (header.equals(REFERENCES)) {
         int selection = this.subTabFolder.getSelectionIndex();
         header = this.subTabFolder.getItem(selection).getText();
      }

      if (header.equals(PROPERTY)) {
         statement = "select FID, DES, ACCKEYS, DEL, DFLAG, DFTDES, DFTDES1, DFTHDR, DFTORD, EXIST, EXTENDLENGTH, FDOC, FILETYP, FPN, FSN, GLOBAL, GLREF, LISTDFT, LISTREQ, LOG, LTD, MPLCTDD, NETLOC, PARFID, PREDAEN, PTRTIM, PTRTIMU, PTRTLD, PTRTLDU, PTRUSER, PTRUSERU, QID1, RECTYP, RFLAG, SCREEN, SYSSN, UDACC, UDFILE, UDPOST, UDPRE, USER from DBTBL1 where FID = '" + this.tableName.toUpperCase() + "'";
         if (!this.tableName.equals(TableList.getLoadedTableName()) || this.objectList0 == null || this.objectList0.size() == 0 || isRefresh) {
            TableList.superTable = "";
            TableList.setLoadedTableName(this.tableName);
            this.objectList0 = this.loadData(statement, this.tableColumnProps);
            this.columnDetail0Viewer.setContentProvider(new DetailsContentProvider());
            this.columnDetail0Viewer.setLabelProvider(new DetailsLabelProvider());
            this.columnDetail0Viewer.setInput(this.objectList0);
            this.columnDetail0Viewer.refresh();
            TableList.setTableDetails(this.objectList0);
         }
      } else if (header.equals(INDEX)) {
         statement = "select INDEXNM from DBTBL8 where FID = '" + this.tableName.toUpperCase() + "'";
         if (!this.tableName.equals(TableList.getLoadedIndex()) || isRefresh) {
            TableList.setLoadedIndex(this.tableName);
            this.objectIndex = this.loadData(statement, (String[])null);
            this.objectList1.clear();
            this.indexListTblViewer.setInput(this.objectIndex);
            this.indexListTblViewer.refresh();
            if (this.objectIndex.size() == 0) {
               this.indexDeleteButton.setEnabled(false);
               this.indexEditButton.setEnabled(false);
            } else {
               this.indexDeleteButton.setEnabled(true);
               this.indexEditButton.setEnabled(true);
            }

            this.columnDetail1Viewer.setInput(this.objectList1);
            this.columnDetail1Viewer.refresh();
         }
      } else if (header.equals(FOREIGNKEY)) {
         statement = "select FKEYS from DBTBL1F where FID = '" + this.tableName.toUpperCase() + "'";
         if (!this.tableName.equals(TableList.getLoadedForeignKey()) || isRefresh) {
            TableList.setLoadedForeignKey(this.tableName);
            this.objectForeign = this.loadData(statement, (String[])null);
            this.objectList2.clear();
            this.foreignListTblViewer.setInput(this.objectForeign);
            this.foreignListTblViewer.refresh();
            if (this.objectForeign.size() == 0) {
               this.foreignKeyDeleteButton.setEnabled(false);
               this.foreignKeyEditButton.setEnabled(false);
            } else {
               this.foreignKeyDeleteButton.setEnabled(true);
               this.foreignKeyEditButton.setEnabled(true);
            }

            this.columnDetail2Viewer.setInput(this.objectList2);
            this.columnDetail2Viewer.refresh();
         }
      } else if (header.equals(JOURNAL)) {
         statement = "select JRNID from DBTBL9 where PRITABLE = '" + this.tableName.toUpperCase() + "'";
         if (!this.tableName.equals(TableList.getLoadedJournal()) || isRefresh) {
            TableList.setLoadedJournal(this.tableName);
            this.objectJournal = this.loadData(statement, (String[])null);
            this.objectList3.clear();
            this.journalListTblViewer.setInput(this.objectJournal);
            this.journalListTblViewer.refresh();
            if (this.objectJournal.size() == 0) {
               this.journalDeleteButton.setEnabled(false);
               this.journalEditButton.setEnabled(false);
            } else {
               this.journalDeleteButton.setEnabled(true);
               this.journalEditButton.setEnabled(true);
            }

            this.columnDetail3Viewer.setInput(this.objectList3);
            this.columnDetail3Viewer.refresh();
         }
      } else if (header.equals(DOCUMENT)) {
         statement = "select DES from DBTBL1TBLDOC where FID = '" + this.tableName + "'";
         if (!this.tableName.equals(TableList.getLoadedTableDoc())) {
            TableList.setLoadedTableDoc(this.tableName);
            ArrayList list = this.loadData(statement, (String[])null);
            this.strDoc = "";

            for(int i = 0; i < list.size(); ++i) {
               if (list.get(i) == null) {
                  this.strDoc = this.strDoc + "\n";
               } else {
                  this.strDoc = this.strDoc + list.get(i).toString() + "\n";
               }
            }

            this.tableDoc.setText(this.strDoc);
         }
      } else if (header.equals(IMPORT)) {
         if (this.importContents != null) {
            this.importContents.removeAll();
         }

         this.diagramImportList.clear();
         this.drawImportDiagram();
         this.lwsImport.setContents(this.importPane);
      } else if (header.equals(EXPORT)) {
         if (this.exportContents != null) {
            this.exportContents.removeAll();
         }

         this.diagramExportList.clear();
         this.drawExportDiagram();
         this.lwsExport.setContents(this.exportPane);
      }

   }

   private ArrayList loadData(String statement, String[] colNames) {
      ArrayList list = new ArrayList();
      if (this.tableName == "") {
         return list;
      } else {
         GetDataList dataList = new GetDataList(statement, colNames);
         list = dataList.loadData();
         return list;
      }
   }

   private Button createButton(Composite parent, String text) {
      Button button = new Button(parent, 8);
      button.setText(text);
      GridData buttonData = new GridData();
      buttonData.horizontalAlignment = 4;
      buttonData.verticalAlignment = 128;
      buttonData.widthHint = 85;
      button.setLayoutData(buttonData);
      return button;
   }

   private void createTableDetails(final Composite parent, int groupSpan) {
      this.container = this.createContainer(parent, groupSpan, PROPERTY);
      this.columnDetail0Viewer = this.createColumnDetailGroup();
      this.columnDetail0Viewer.setContentProvider(new DetailsContentProvider());
      this.columnDetail0Viewer.setLabelProvider(new DetailsLabelProvider());
      this.columnDetail0Viewer.setInput(this.objectList0);
      Button editButton = this.createButton(this.container, "Edit");
      editButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            UpdateTable dialog = new UpdateTable(parent.getShell(), TableDetailsView.this.objectList0);
            int returncode = dialog.open();
            if (returncode == 0) {
               TableDetailsView.this.ReloadData(TableDetailsView.PROPERTY, true);
            }

         }
      });
   }

   private void loadIndexDetails() {
      String statement = "Select IDXDESC, NULLFLG, ORDERBY, PARFID, QRY1, QRY2, SAVFLG, UPCASE, GLOBAL from DBTBL8 WHERE FID = '" + this.tableName.toUpperCase() + "' " + "AND INDEXNM = '" + this.selectedFieldLabel + "'";
      if (this.selectedFieldLabel != "") {
         this.objectList1 = this.loadColumnDetail(statement, this.indexColumnProps);
         if (this.objectList1.size() > 0) {
            this.columnDetail1Viewer.setInput(this.objectList1);
            this.columnDetail1Viewer.refresh();
         }
      }

   }

   private void loadForeignKeyDetails() {
      String statement = "Select PKEYS, RCFRMAX, RCFRMIN, RCTOMAX, RCTOMIN, TBLREF, UPD, DEL from DBTBL1F WHERE FID = '" + this.tableName.toUpperCase() + "' " + "AND FKEYS = '" + this.selectedFieldLabel + "'";
      if (this.selectedFieldLabel != "") {
         this.objectList2 = this.loadColumnDetail(statement, this.foreignColumnProps);
         if (this.objectList2.size() > 0) {
            this.columnDetail2Viewer.setInput(this.objectList2);
            this.columnDetail2Viewer.refresh();
         }
      }

   }

   private void loadImportDiagramData() {
      this.diagramImportList.clear();
      String statement = "";
      if (this.radForeignKey.getSelection()) {
         statement = "Select TBLREF from DBTBL1F Where FID = '" + this.tableName.toUpperCase() + "'";
      } else if (this.radIndex.getSelection()) {
         statement = "select INDEXNM from DBTBL8 where FID = '" + this.tableName.toUpperCase() + "'";
      } else if (this.radJournal.getSelection()) {
         statement = "select JRNID from DBTBL9 where PRITABLE = '" + this.tableName.toUpperCase() + "'";
      }

      GetDataList dataList = new GetDataList(statement, (String[])null);
      this.diagramImportList = dataList.loadData_1(1);
   }

   private void loadExportDiagramData() {
      this.diagramExportList.clear();
      String statement = "";
      if (this.radForeignKey.getSelection()) {
         statement = "Select FID from DBTBL1F Where TBLREF = '" + this.tableName.toUpperCase() + "'";
      } else if (this.radIndex.getSelection()) {
         statement = "select FID from DBTBL8 where INDEXNM = '" + this.tableName.toUpperCase() + "'";
      } else if (this.radJournal.getSelection()) {
         statement = "select PRITABLE from DBTBL9 where JRNID = '" + this.tableName.toUpperCase() + "'";
      }

      GetDataList dataList = new GetDataList(statement, (String[])null);
      this.diagramExportList = dataList.loadData_1(1);
   }

   private String getTableDesc(String tableName) {
      try {
         new ArrayList();
         String statement = "Select DES from DBTBL1 Where FID = '" + tableName + "'";
         GetDataList dataList = new GetDataList(statement, (String[])null);
         ArrayList list = dataList.loadData_1(1);
         return list.get(0).toString().replaceAll("aaaaaa", ",");
      } catch (Exception var5) {
         return "";
      }
   }

   private void loadJournalDetails() {
      String statement = "Select DES, SUBTABLE, EFD, TRANTYPE, MODE, EXCOLUMN, INCOLUMN, SEQ, QUERY1, QUERY2, PARFID  from DBTBL9 WHERE PRITABLE = '" + this.tableName.toUpperCase() + "' " + "AND JRNID = '" + this.selectedFieldLabel + "'";
      if (this.selectedFieldLabel != "") {
         this.objectList3 = this.loadColumnDetail(statement, this.journalColumnProps);
         if (this.objectList3.size() > 0) {
            this.columnDetail3Viewer.setInput(this.objectList3);
            this.columnDetail3Viewer.refresh();
         }
      }

   }

   private Composite createButtonComposite(Composite parent) {
      Composite buttonComposite = new Composite(parent, 0);
      GridLayout glayout = new GridLayout();
      glayout.marginHeight = 5;
      glayout.marginWidth = 5;
      glayout.numColumns = 1;
      GridData gdata = new GridData(2);
      buttonComposite.setLayout(glayout);
      buttonComposite.setLayoutData(gdata);
      buttonComposite.setFont(parent.getFont());
      return buttonComposite;
   }

   private void createIndexList(final Composite parent, int groupSpan) {
      this.container = this.createContainer(parent, groupSpan, "Table Index");
      Table indexListTable = this.createTable("Index Name");
      this.indexListTblViewer = new TableViewer(indexListTable);
      this.indexListTblViewer.setContentProvider(new TableContentProvider());
      this.indexListTblViewer.setLabelProvider(new TableLabelProvider());
      this.indexListTblViewer.setInput(this.objectIndex);
      this.columnDetail1Viewer = this.createColumnDetailGroup();
      this.indexListTblViewer.addSelectionChangedListener(new ISelectionChangedListener() {
         public void selectionChanged(SelectionChangedEvent event) {
            int selection = TableDetailsView.this.indexListTblViewer.getTable().getSelectionIndex();
            if (selection != -1) {
               Object obj = TableDetailsView.this.indexListTblViewer.getElementAt(selection);
               if (obj != null) {
                  TableDetailsView.this.selectedFieldLabel = obj.toString();
                  TableDetailsView.this.loadIndexDetails();
                  TableDetailsView.this.indexEditButton.setEnabled(true);
                  TableDetailsView.this.indexDeleteButton.setEnabled(true);
               }
            }

         }
      });
      Composite buttonComposite = this.createButtonComposite(this.container);
      this.indexAddButton = this.createButton(buttonComposite, "Add");
      this.indexAddButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            UpdateIndex dialog = new UpdateIndex(parent.getShell());
            int returncode = dialog.open();
            if (returncode == 0) {
               TableDetailsView.this.ReloadData(TableDetailsView.INDEX, true);
            }

         }
      });
      this.indexEditButton = this.createButton(buttonComposite, "Edit");
      this.indexEditButton.setEnabled(false);
      this.indexEditButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            if (TableDetailsView.this.indexListTblViewer.getTable().getSelectionIndex() < 0) {
               MessageDialog.openWarning((Shell)null, "Information", "Please select a index first.");
            } else {
               UpdateIndex dialog = new UpdateIndex(parent.getShell(), TableDetailsView.this.objectList1, TableDetailsView.this.selectedFieldLabel);
               int returncode = dialog.open();
               if (returncode == 0) {
                  TableDetailsView.this.loadIndexDetails();
               }

            }
         }
      });
      this.indexDeleteButton = this.createButton(buttonComposite, "Delete");
      this.indexDeleteButton.setEnabled(false);
      this.indexDeleteButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            if (TableDetailsView.this.indexListTblViewer.getTable().getSelectionIndex() < 0) {
               MessageDialog.openWarning((Shell)null, "Information", "Please select a index first.");
            } else {
               boolean ok = MessageDialog.openConfirm((Shell)null, "Are you sure to delete index?", "You are going to delete index " + TableDetailsView.this.selectedFieldLabel + ". Are you sure to continue?");
               if (ok) {
                  String statement = "DELETE DBTBL8 WHERE FID = '" + TableList.getSelectedTableName() + "' AND INDEXNM = '" + TableDetailsView.this.selectedFieldLabel + "'";
                  GetDataList doUpdate = new GetDataList(statement, (String[])null);
                  doUpdate.updateRecord();
                  TableDetailsView.this.ReloadData(TableDetailsView.INDEX, true);
               }

            }
         }
      });
   }

   private void createForeignKeyList(final Composite parent, int groupSpan) {
      this.container = this.createContainer(parent, groupSpan, FOREIGNKEY);
      Table foreignListTable = this.createTable(FOREIGNKEY);
      this.foreignListTblViewer = new TableViewer(foreignListTable);
      this.foreignListTblViewer.setContentProvider(new TableContentProvider());
      this.foreignListTblViewer.setLabelProvider(new TableLabelProvider());
      this.foreignListTblViewer.setInput(this.objectForeign);
      this.columnDetail2Viewer = this.createColumnDetailGroup();
      this.foreignListTblViewer.addSelectionChangedListener(new ISelectionChangedListener() {
         public void selectionChanged(SelectionChangedEvent event) {
            int selection = TableDetailsView.this.foreignListTblViewer.getTable().getSelectionIndex();
            if (selection != -1) {
               Object obj = TableDetailsView.this.foreignListTblViewer.getElementAt(selection);
               if (obj != null) {
                  TableDetailsView.this.selectedFieldLabel = obj.toString();
                  TableDetailsView.this.loadForeignKeyDetails();
                  TableDetailsView.this.foreignKeyEditButton.setEnabled(true);
                  TableDetailsView.this.foreignKeyDeleteButton.setEnabled(true);
               }
            }

         }
      });
      Composite buttonComposite = this.createButtonComposite(this.container);
      this.foreignKeyAddButton = this.createButton(buttonComposite, "Add");
      this.foreignKeyAddButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            UpdateForeignKey dialog = new UpdateForeignKey(parent.getShell());
            int returncode = dialog.open();
            if (returncode == 0) {
               TableDetailsView.this.ReloadData(TableDetailsView.FOREIGNKEY, true);
            }

         }
      });
      this.foreignKeyEditButton = this.createButton(buttonComposite, "Edit");
      this.foreignKeyEditButton.setEnabled(false);
      this.foreignKeyEditButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            if (TableDetailsView.this.foreignListTblViewer.getTable().getSelectionIndex() < 0) {
               MessageDialog.openWarning((Shell)null, "Information", "Please select a foreign key first.");
            } else {
               UpdateForeignKey dialog = new UpdateForeignKey(parent.getShell(), TableDetailsView.this.objectList2, TableDetailsView.this.selectedFieldLabel);
               int returncode = dialog.open();
               if (returncode == 0) {
                  TableDetailsView.this.loadForeignKeyDetails();
               }

            }
         }
      });
      this.foreignKeyDeleteButton = this.createButton(buttonComposite, "Delete");
      this.foreignKeyDeleteButton.setEnabled(false);
      this.foreignKeyDeleteButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            if (TableDetailsView.this.foreignListTblViewer.getTable().getSelectionIndex() < 0) {
               MessageDialog.openWarning((Shell)null, "Information", "Please select a foreign key first.");
            } else {
               boolean ok = MessageDialog.openConfirm((Shell)null, "Are you sure to delete Foreign Key?", "You are going to delete Foreign Key " + TableDetailsView.this.selectedFieldLabel + ". Are you sure to continue?");
               if (ok) {
                  String statement = "DELETE DBTBL1F WHERE FID = '" + TableList.getSelectedTableName() + "' AND FKEYS = '" + TableDetailsView.this.selectedFieldLabel + "'";
                  GetDataList doUpdate = new GetDataList(statement, (String[])null);
                  doUpdate.updateRecord();
                  TableDetailsView.this.ReloadData(TableDetailsView.FOREIGNKEY, true);
               }

            }
         }
      });
   }

   private void createJournalList(final Composite parent, int groupSpan) {
      this.container = this.createContainer(parent, groupSpan, "Table Journal");
      Table journalListTable = this.createTable("Journal Name");
      this.journalListTblViewer = new TableViewer(journalListTable);
      this.journalListTblViewer.setContentProvider(new TableContentProvider());
      this.journalListTblViewer.setLabelProvider(new TableLabelProvider());
      this.journalListTblViewer.setInput(this.objectJournal);
      this.columnDetail3Viewer = this.createColumnDetailGroup();
      this.journalListTblViewer.addSelectionChangedListener(new ISelectionChangedListener() {
         public void selectionChanged(SelectionChangedEvent event) {
            int selection = TableDetailsView.this.journalListTblViewer.getTable().getSelectionIndex();
            if (selection != -1) {
               Object obj = TableDetailsView.this.journalListTblViewer.getElementAt(selection);
               if (obj != null) {
                  TableDetailsView.this.selectedFieldLabel = obj.toString();
                  TableDetailsView.this.loadJournalDetails();
                  TableDetailsView.this.journalEditButton.setEnabled(true);
                  TableDetailsView.this.journalDeleteButton.setEnabled(true);
               }
            }

         }
      });
      Composite buttonComposite = this.createButtonComposite(this.container);
      this.journalAddButton = this.createButton(buttonComposite, "Add");
      this.journalAddButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            UpdateJournal dialog = new UpdateJournal(parent.getShell());
            int returncode = dialog.open();
            if (returncode == 0) {
               TableDetailsView.this.ReloadData(TableDetailsView.JOURNAL, true);
            }

         }
      });
      this.journalEditButton = this.createButton(buttonComposite, "Edit");
      this.journalEditButton.setEnabled(false);
      this.journalEditButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            if (TableDetailsView.this.journalListTblViewer.getTable().getSelectionIndex() < 0) {
               MessageDialog.openWarning((Shell)null, "Information", "Please select a journal first.");
            } else {
               UpdateJournal dialog = new UpdateJournal(parent.getShell(), TableDetailsView.this.objectList3, TableDetailsView.this.selectedFieldLabel);
               int returncode = dialog.open();
               if (returncode == 0) {
                  TableDetailsView.this.loadJournalDetails();
               }

            }
         }
      });
      this.journalDeleteButton = this.createButton(buttonComposite, "Delete");
      this.journalDeleteButton.setEnabled(false);
      this.journalDeleteButton.addSelectionListener(new SelectionListener() {
         public void widgetDefaultSelected(SelectionEvent e) {
         }

         public void widgetSelected(SelectionEvent e) {
            if (TableDetailsView.this.journalListTblViewer.getTable().getSelectionIndex() < 0) {
               MessageDialog.openWarning((Shell)null, "Information", "Please select a journal first.");
            } else {
               boolean ok = MessageDialog.openConfirm((Shell)null, "Are you sure to delete Journal?", "You are going to delete Journal " + TableDetailsView.this.selectedFieldLabel + ". Are you sure to continue?");
               if (ok) {
                  String statement = "DELETE DBTBL9D WHERE PRITABLE = '" + TableList.getSelectedTableName() + "' AND JRNID = '" + TableDetailsView.this.selectedFieldLabel + "'";
                  GetDataList doUpdate = new GetDataList(statement, (String[])null);
                  doUpdate.updateRecord();
                  TableDetailsView.this.ReloadData(TableDetailsView.JOURNAL, true);
               }

            }
         }
      });
   }

   private Table createTable(String columnName) {
      GridData data = new GridData(1808);
      Table table = new Table(this.container, 68354);
      table.setLayoutData(data);
      if (columnName.length() > 0) {
         TableColumn column1 = new TableColumn(table, 0);
         column1.setText(columnName);
         column1.setWidth(350);
      }

      table.setLinesVisible(true);
      table.setHeaderVisible(true);
      table.setVisible(true);
      return table;
   }

   private Composite createContainer(Composite parent, int groupSpan, String title) {
      Group group = new Group(parent, 0);
      group.setText(title);
      GridLayout gridLayout = new GridLayout();
      group.setLayout(gridLayout);
      GridData gridData = new GridData(1808);
      gridData.horizontalSpan = groupSpan;
      group.setLayoutData(gridData);
      this.container = new Composite(group, 0);
      gridLayout = new GridLayout();
      gridLayout.numColumns = groupSpan * 2;
      this.container.setLayout(gridLayout);
      gridData = new GridData(1808);
      this.container.setLayoutData(gridData);
      return this.container;
   }

   private TableViewer createColumnDetailGroup() {
      GridData data = new GridData(1808);
      Table columnTable = new Table(this.container, 68354);
      columnTable.setLayoutData(data);
      TableColumn column1 = new TableColumn(columnTable, 0);
      column1.setText("Property");
      column1.setWidth(200);
      TableColumn column2 = new TableColumn(columnTable, 0);
      column2.setText("value");
      column2.setWidth(250);
      columnTable.setLinesVisible(true);
      columnTable.setHeaderVisible(true);
      columnTable.setVisible(true);
      TableViewer viewer = new TableViewer(columnTable);
      viewer.setContentProvider(new DetailsContentProvider());
      viewer.setLabelProvider(new DetailsLabelProvider());
      return viewer;
   }

   private ArrayList loadColumnDetail(String statement, String[] columnProp) {
      new ArrayList();
      GetDataList dataList = new GetDataList(statement, columnProp);
      ArrayList list = dataList.loadData();
      return list;
   }

   public void refresh() {
      this.tableName = TableList.getSelectedTableName();
      int selection = this.tabFolder.getSelectionIndex();
      String header = this.tabFolder.getItem(selection).getText();
      this.ReloadData(header, false);
   }

   public ArrayList getForeignKeyList() {
      return this.objectForeign;
   }

   public ArrayList getJournalList() {
      return this.objectJournal;
   }

   public ArrayList getIndexList() {
      return this.objectIndex;
   }

   public void setFocus() {
   }

   public void handleEvent(Event event) {
   }
}
