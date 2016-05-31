/*******************************************************************************
 * Copyright (c) 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.core.sirius.analysis;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DDiagramElementContainer;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.polarsys.capella.common.data.modellingcore.IState;
import org.polarsys.capella.common.helpers.EcoreUtil2;
import org.polarsys.capella.core.data.capellacommon.EntryPointPseudoState;
import org.polarsys.capella.core.data.capellacommon.ExitPointPseudoState;
import org.polarsys.capella.core.data.capellacommon.FinalState;
import org.polarsys.capella.core.data.capellacommon.ForkPseudoState;
import org.polarsys.capella.core.data.capellacommon.InitialPseudoState;
import org.polarsys.capella.core.data.capellacommon.JoinPseudoState;
import org.polarsys.capella.core.data.capellacommon.Region;
import org.polarsys.capella.core.data.capellacommon.State;
import org.polarsys.capella.core.data.capellacommon.TerminatePseudoState;

/**
 * Services for Mode State machine diagram.
 */
public class ModeStateMachineServices {

  /** A shared instance. */
  private static StateMachineServices _service;

  /**
   * returns a shared instance of this services.
   * 
   * @return a shared instance of this services.
   */
  public static StateMachineServices getService() {
    if (_service == null) {
      _service = new StateMachineServices();
    }
    return _service;
  }

  public EObject moveRegionMSM(EObject context, Region newRegion, Region selectedRegion) {

    EObject container = newRegion.eContainer();
    State state = (State) container;
    state.getOwnedRegions().remove(newRegion);
    int index = 0;
    if (selectedRegion != null) {
      index = state.getOwnedRegions().indexOf(selectedRegion) + 1;
    }
    state.getOwnedRegions().add(index, newRegion);

    return context;
  }

  public Region getRegionForTransitionMSM(EObject context, DDiagramElement delement) {

    EObject target = delement.getTarget();

    // if select a region of a mode/state
    if (target instanceof Region) {
      return (Region) target.eContainer().eContainer();
    }

    // if select a mode/state
    if (target instanceof IState) {
      EObject container = target.eContainer();
      if (container instanceof Region) {
        return (Region) container;
      }
    }

    return null;
  }

  public boolean canCreateTransitionMSM(EObject context, EObject sourceElement, EObject targetElement) {

    IState source = null;
    IState target = null;

    if (sourceElement instanceof IState) {
      source = (IState) sourceElement;
    } else if (sourceElement instanceof Region) {
      source = (IState) sourceElement.eContainer();
    } else {
      return false;
    }

    if (targetElement instanceof IState) {
      target = (IState) targetElement;
    } else if (targetElement instanceof Region) {
      target = (IState) targetElement.eContainer();
    } else {
      return false;
    }

    if ((target instanceof InitialPseudoState) || (source instanceof TerminatePseudoState)
        || (source instanceof FinalState)) {
      return false;
    }

    if ((source instanceof InitialPseudoState)
        && ((target instanceof TerminatePseudoState) || (target instanceof FinalState))) {
      return false;
    }

    if (((target instanceof ExitPointPseudoState) && !(StateMachineServices.getService().isInSameOrSubRegion(target,
        source)))
        || ((source instanceof EntryPointPseudoState) && !StateMachineServices.getService().isInSameOrSubRegion(source,
            target))
        || ((source instanceof JoinPseudoState) && (StateMachineServices.getService().getSourcingTransition(source)
            .size() != 0))
        || ((target instanceof ForkPseudoState) && (StateMachineServices.getService().getTargettingTransition(target)
            .size() != 0))) {
      return false;
    }

    if (EcoreUtil.isAncestor(source, target) || EcoreUtil.isAncestor(target, source)) {
      return false;
    }

    // cannot create a transition between states located on different regions of the same parent state
    if (EcoreUtil2.getCommonAncestor(source, target) instanceof IState) {
      return false;
    }

    return true;
  }
  
  public Region getRegionForInsertionMSM(EObject context, EObject delement) {
    Region region = null;

    if (delement instanceof DDiagram) {
      region = (Region) ((DSemanticDiagram) delement).getTarget();
    } else if (delement instanceof DDiagramElementContainer) {
      EObject target = ((DNodeContainer) delement).getTarget();
      if (target instanceof Region) {
        return (Region) target;
      }
    }
    return region;
  }
}
