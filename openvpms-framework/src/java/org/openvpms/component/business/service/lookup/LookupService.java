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
 *  Copyright 2007 (C) OpenVPMS Ltd. All Rights Reserved.
 *
 *  $Id$
 */

package org.openvpms.component.business.service.lookup;

import org.openvpms.component.business.dao.im.common.IMObjectDAO;
import org.openvpms.component.business.service.archetype.IArchetypeService;


/**
 * Default implementation of the {@link ILookupService}.
 *
 * @author <a href="mailto:support@openvpms.org">OpenVPMS Team</a>
 * @version $LastChangedDate: 2006-05-02 05:16:31Z $
 */
public class LookupService extends AbstractLookupService {

    /**
     * Constructs a new <tt>LookupService</tt>.
     *
     * @param service the archetype service
     * @param dao     the data access object
     */
    public LookupService(IArchetypeService service, IMObjectDAO dao) {
        super(service, dao);
    }

}
