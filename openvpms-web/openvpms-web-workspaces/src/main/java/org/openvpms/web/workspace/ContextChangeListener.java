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
 *  Copyright 2006 (C) OpenVPMS Ltd. All Rights Reserved.
 *
 *  $Id: ContextChangeListener.java 5106 2013-05-13 03:01:33Z tanderson $
 */

package org.openvpms.web.workspace;

import org.openvpms.component.business.domain.im.common.IMObject;

import java.util.EventListener;


/**
 * Listener for context changes.
 *
 * @author <a href="mailto:support@openvpms.org">OpenVPMS Team</a>
 * @version $LastChangedDate: 2013-05-13 00:01:33 -0300 (Mon, 13 May 2013) $
 */
public interface ContextChangeListener extends EventListener {

    /**
     * Change the context.
     *
     * @param context the context to change to
     */
    void changeContext(IMObject context);

    /**
     * Change the context.
     *
     * @param shortName the archetype short name of the context to change to
     */
    void changeContext(String shortName);
}
