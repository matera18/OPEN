/*
 *  Version: 1.0
 *
 *  The contents of this file are subject to the OpenVPMS License Version
 *  1.0 (the 'License'); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *  http://www.openvpms.org/license/
 *
 *  Software distributed under the License is distributed on an 'AS IS' basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *  Copyright 2008 (C) OpenVPMS Ltd. All Rights Reserved.
 *
 *  $Id$
 */

package org.openvpms.web.component.im.util;

import org.junit.Test;
import org.openvpms.component.business.domain.im.act.Act;
import org.openvpms.component.business.domain.im.act.FinancialAct;
import org.openvpms.component.business.domain.im.common.IMObject;
import org.openvpms.web.test.AbstractAppTest;

import static org.junit.Assert.assertEquals;


/**
 * Tests the {@link IMObjectHelper} class.
 *
 * @author <a href="mailto:support@openvpms.org">OpenVPMS Team</a>
 * @version $LastChangedDate: 2006-05-02 05:16:31Z $
 */
public class IMObjectHelperTestCase extends AbstractAppTest {

    /**
     * Tests the {@link IMObjectHelper#getType} method.
     */
    @Test
    public void testGetType() {
        checkGetType(FinancialAct.class, "act.customerAccountChargesInvoice");
        checkGetType(Act.class, "act.customerAccountChargesInvoice",
                     "act.customerEstimation");
        checkGetType(IMObject.class, "act.customerAccountChargesInvoice",
                     "party.customerperson");
    }

    /**
     * Checks the {@link IMObjectHelper#getType} method for a set of short names
     *
     * @param expected   the expected result
     * @param shortNames the archetype short names
     */
    private void checkGetType(Class expected, String... shortNames) {
        assertEquals(expected, IMObjectHelper.getType(shortNames));
    }
}
