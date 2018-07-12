/*******************************************************************************
 * Copyright (c) 2006, 2018 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/

package org.polarsys.capella.core.data.helpers.epbs.delegates;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.polarsys.capella.core.data.epbs.PhysicalArchitectureRealization;
import org.polarsys.capella.core.data.helpers.cs.delegates.ArchitectureAllocationHelper;

public class PhysicalArchitectureRealizationHelper {
  private static PhysicalArchitectureRealizationHelper instance;

  private PhysicalArchitectureRealizationHelper() {
    // do nothing
  }

  public static PhysicalArchitectureRealizationHelper getInstance() {
    if (instance == null) {
    	instance = new PhysicalArchitectureRealizationHelper();
    }
    return instance;
  }

  public Object doSwitch(PhysicalArchitectureRealization element, EStructuralFeature feature) {
    // no helper found... searching in super classes...
	  return ArchitectureAllocationHelper.getInstance().doSwitch(element, feature);
  }
}
