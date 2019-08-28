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
package org.polarsys.capella.test.diagram.tools.ju.csa;

import org.eclipse.sirius.business.api.session.Session;
import org.polarsys.capella.test.diagram.common.ju.context.CSADiagram;
import org.polarsys.capella.test.diagram.tools.ju.model.EmptyProject;
import org.polarsys.capella.test.framework.context.SessionContext;

public class DragAndDropTest extends EmptyProject {

  private final static String CONSTRAINT_ID = "CONSTRAINT_ID";

  @Override
  public void test() throws Exception {
    Session session = getSession(getRequiredTestModel());
    SessionContext context = new SessionContext(session);
    CSADiagram csa = CSADiagram.createDiagram(context, SA__SYSTEM);
    String diagramId = csa.getDiagramId();

    // create constraint
    String constraint_id = csa.createConstraint(CONSTRAINT_ID);

    // hide constraint
    csa.removeConstraints(constraint_id);

    // drag and drop from explorer
    csa.dragAndDropConstraintsFromExplorer(constraint_id, diagramId);

  }

}
