/*******************************************************************************
 * Copyright (c) 2019 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/

package org.polarsys.capella.test.platform.ju.testcases;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EReference;

public class PlatformFilesHelper {
  private PlatformFilesHelper() {
  }

  /**
   * 
   * @param testPluginFolder
   * @return the root folder (which contains .git folder)
   */
  public static File findRootFolder(File testPluginFolder) {
    if (testPluginFolder.listFiles(new FileFilter() {
      @Override
      public boolean accept(File subFolder) {
        return subFolder.isDirectory() && subFolder.getName().equals(".git");
      }
    }).length == 1) {
      return testPluginFolder;
    }
    return findRootFolder(testPluginFolder.getParentFile());
  }

  /**
   * 
   * @param eReference
   * @return whether the reference is ignored during the check of surrogate query
   */
  public static boolean isIgnored(EReference eReference) {
    // A reference is ignored if it has the viatra.variant=unimplemented annotation
    Optional<EAnnotation> eAnnotation = eReference.getEAnnotations().stream().filter(new Predicate<EAnnotation>() {
      @Override
      public boolean test(EAnnotation eAnnotation) {
        return eAnnotation.getSource().equals("http://www.polarsys.org/capella/derived");
      }
    }).findFirst();
    return eAnnotation.get().getDetails().stream().anyMatch(new Predicate<Map.Entry<String, String>>() {
      @Override
      public boolean test(Map.Entry<String, String> entry) {
        return entry.getKey().equals("viatra.variant") && entry.getValue().equals("unimplemented");
      }
    });
  }

  /**
   * 
   * @param folder
   * @param folderName
   * @return the subfolder with the given name
   */
  public static File getSubFolder(File folder, String folderName) {
    File[] listFiles = folder.listFiles(new FileFilter() {
      @Override
      public boolean accept(File subFolder) {
        return subFolder.isDirectory() && subFolder.getName().equals(folderName);
      }
    });
    if (listFiles.length > 0)
      return listFiles[0];
    return null;
  }

  /**
   * 
   * @param pluginsFolder
   * @return the list of plugin.xml files from the plugins folder
   */
  public static List<File> getPluginXmlFiles(File pluginsFolder) {
    return getPluginFilesByNameEnding(pluginsFolder, "plugin.xml");
  }

  /**
   * 
   * @param pluginsFolder
   * @param name
   *          (filename or extension)
   * @return the list of plugin.xml files from the plugins folder which matches the given name
   */
  public static List<File> getPluginFilesByNameEnding(File pluginsFolder, String name) {
    List<File> pluginFiles = new ArrayList<>();
    for (File file : pluginsFolder.listFiles()) {
      if (file.isFile() && file.getName().endsWith(name))
        pluginFiles.add(file);
      else if (file.isDirectory()) {
        pluginFiles.addAll(getPluginFilesByNameEnding(file, name));
      }
    }
    return pluginFiles;
  }

  public static String findInFile(File file, String pattern) {
    String firstMatch = null;
    try {
      Pattern p = Pattern.compile(pattern);

      Optional<String> message = Files.lines(file.toPath()).filter(p.asPredicate()).findFirst();
      if (message.isPresent()) {
        firstMatch = message.get();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    return firstMatch;
  }
}
