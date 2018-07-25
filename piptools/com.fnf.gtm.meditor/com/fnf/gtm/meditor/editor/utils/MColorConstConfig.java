package com.fnf.gtm.meditor.editor.utils;

import com.fnf.gtm.meditor.MEditorPlugin;
import com.fnf.gtm.meditor.editor.MEditor;
import com.fnf.gtm.meditor.editor.MessageUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.osgi.framework.Bundle;

public class MColorConstConfig {
   public String[] fgKeywords = new String[]{""};
   public String[] fgOperators = new String[]{""};
   public String[] fgFunctions = new String[]{""};
   public String[] fgVariables = new String[]{""};
   public String[] fgBadComments = new String[]{"go", "$$^SQL", "$$SELECT^SQL"};
   private String cKeywords = "[KEYWORDS]";
   private String cOperators = "[OPERATORS]";
   private String cFunctions = "[FUNCTIONS]";
   private String cVariables = "[VARIABLES]";
   private String cBadComments = "[BadComments]";
   private List keywordsList = new ArrayList();
   private List operatorsList = new ArrayList();
   private List functionsList = new ArrayList();
   private List variablesList = new ArrayList();
   private List badCommentsList = new ArrayList();
   private MEditor editor;

   private boolean isTag(String str) {
      if (str.equalsIgnoreCase("[KEYWORDS]")) {
         return true;
      } else if (str.equalsIgnoreCase("[OPERATORS]")) {
         return true;
      } else if (str.equalsIgnoreCase("[FUNCTIONS]")) {
         return true;
      } else if (str.equalsIgnoreCase("[VARIABLES]")) {
         return true;
      } else {
         return str.equalsIgnoreCase("[BadComments]");
      }
   }

   public MColorConstConfig(MEditor editor) {
      this.editor = editor;

      try {
         Map override = null;
         Bundle plugin = Platform.getBundle("com.fnf.gtm.meditor");
         URL fileURL = FileLocator.find(plugin, Path.fromOSString("config/gtmKeywords.ini"), (Map)override);
         InputStream input = fileURL.openStream();
         BufferedReader br = new BufferedReader(new InputStreamReader(input));
         String line = br.readLine();

         while(true) {
            while(line != null) {
               if (line.trim().equals(this.cKeywords)) {
                  line = br.readLine();
                  line = line.trim();

                  while(!this.isTag(line.trim())) {
                     if (!line.trim().equals("")) {
                        this.keywordsList.add(line.trim());
                     }

                     line = br.readLine();
                     if (line == null) {
                        break;
                     }
                  }
               } else if (line.trim().equals(this.cOperators)) {
                  line = br.readLine();

                  while(!this.isTag(line.trim())) {
                     if (!line.trim().equals("")) {
                        this.operatorsList.add(line.trim());
                     }

                     line = br.readLine();
                     if (line == null) {
                        break;
                     }
                  }
               } else if (line.trim().equals(this.cFunctions)) {
                  line = br.readLine();
                  line = line.trim();

                  while(!this.isTag(line.trim())) {
                     if (!line.trim().equals("")) {
                        this.functionsList.add(line.trim());
                     }

                     line = br.readLine();
                     if (line == null) {
                        break;
                     }
                  }
               } else if (line.trim().equals(this.cVariables)) {
                  line = br.readLine();

                  while(!this.isTag(line.trim())) {
                     if (!line.trim().equals("")) {
                        this.variablesList.add(line.trim());
                     }

                     line = br.readLine();
                     if (line == null) {
                        break;
                     }
                  }
               } else if (line.trim().equals(this.cBadComments)) {
                  line = br.readLine();

                  while(!this.isTag(line.trim())) {
                     if (!line.trim().equals("")) {
                        this.badCommentsList.add(line.trim());
                     }

                     line = br.readLine();
                     if (line == null) {
                        break;
                     }
                  }
               } else {
                  line = br.readLine();
               }
            }

            br.close();
            String filepath = this.findMKeyWordsFileFromProjProp(MessageUtil.getString("M_PROJ_SPECIFIC_KWORDS"));
            if (!filepath.equals("")) {
               File file = new File(filepath);
               br = new BufferedReader(new FileReader(file));
               line = br.readLine();

               while(true) {
                  while(line != null) {
                     if (line.trim().equals(this.cKeywords)) {
                        line = br.readLine();
                        line = line.trim();

                        while(!this.isTag(line.trim())) {
                           if (!line.trim().equals("")) {
                              this.keywordsList.add(line.trim());
                           }

                           line = br.readLine();
                           if (line == null) {
                              break;
                           }
                        }
                     } else if (line.trim().equals(this.cOperators)) {
                        line = br.readLine();

                        while(!this.isTag(line.trim())) {
                           if (!line.trim().equals("")) {
                              this.operatorsList.add(line.trim());
                           }

                           line = br.readLine();
                           if (line == null) {
                              break;
                           }
                        }
                     } else if (line.trim().equals(this.cFunctions)) {
                        line = br.readLine();
                        line = line.trim();

                        while(!this.isTag(line.trim())) {
                           if (!line.trim().equals("")) {
                              this.functionsList.add(line.trim());
                           }

                           line = br.readLine();
                           if (line == null) {
                              break;
                           }
                        }
                     } else if (line.trim().equals(this.cVariables)) {
                        line = br.readLine();

                        while(!this.isTag(line.trim())) {
                           if (!line.trim().equals("")) {
                              this.variablesList.add(line.trim());
                           }

                           line = br.readLine();
                           if (line == null) {
                              break;
                           }
                        }
                     } else if (line.trim().equals(this.cBadComments)) {
                        line = br.readLine();

                        while(!this.isTag(line.trim())) {
                           if (!line.trim().equals("")) {
                              this.badCommentsList.add(line.trim());
                           }

                           line = br.readLine();
                           if (line == null) {
                              break;
                           }
                        }
                     } else {
                        line = br.readLine();
                     }
                  }

                  br.close();
                  break;
               }
            }

            this.fgKeywords = new String[this.keywordsList.size()];
            this.fgKeywords = this.convertArray(this.keywordsList);
            this.fgOperators = new String[this.operatorsList.size()];
            this.fgOperators = this.convertArray(this.operatorsList);
            this.fgFunctions = new String[this.functionsList.size()];
            this.fgFunctions = this.convertArray(this.functionsList);
            this.fgVariables = new String[this.variablesList.size()];
            this.fgVariables = this.convertArray(this.variablesList);
            this.fgBadComments = new String[this.badCommentsList.size()];
            this.fgBadComments = this.convertArray(this.badCommentsList);
            break;
         }
      } catch (IOException var10) {
         var10.printStackTrace();
      }

   }

   private String findMKeyWordsFileFromProjProp(String property) {
      try {
         Object obj = this.editor.getEditorInput().getAdapter(IFile.class);
         IFile ifile = (IFile)obj;
         IProject prj = ifile.getProject();
         String str = prj.getPersistentProperty(new QualifiedName(MEditorPlugin.PLUGINID, property));
         if (str != null) {
            return str;
         }
      } catch (CoreException var6) {
         var6.printStackTrace();
      }

      return "";
   }

   private String[] convertArray(List objList) {
      String[] tmpStr = new String[objList.size()];

      for(int i = 0; i < objList.size(); ++i) {
         tmpStr[i] = (String)objList.get(i);
      }

      return tmpStr;
   }
}
