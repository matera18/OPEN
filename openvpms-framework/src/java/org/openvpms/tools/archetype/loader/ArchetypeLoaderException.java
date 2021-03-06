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
 *  Copyright 2005 (C) OpenVPMS Ltd. All Rights Reserved.
 *
 *  $Id: ArchetypeLoaderException.java 2308 2007-08-24 06:54:12Z tanderson $
 */


package org.openvpms.tools.archetype.loader;

import org.apache.commons.resources.Messages;
import org.openvpms.component.system.common.exception.OpenVPMSException;


/**
 * This is the base exception thrown by the archetype loader.
 *
 * @author <a href="mailto:support@openvpms.org">OpenVPMS Team</a>
 * @version $LastChangedDate: 2007-08-24 03:54:12 -0300 (Fri, 24 Aug 2007) $
 */
public class ArchetypeLoaderException extends OpenVPMSException {

    /**
     * Serialisation version identifier.
     */
    private static final long serialVersionUID = 1L;


    /**
     * An enumeration of error codes.
     */
    public enum ErrorCode {
        FileNotFound,
        DirNotFound,
        ValidationError
    }

    /**
     * The error code.
     */
    private ErrorCode errorCode;

    /**
     * The messages.
     */
    private static Messages messages = Messages
            .getMessages("org.openvpms.tools.archetype.loader."
                    + OpenVPMSException.ERRMESSAGES_FILE);

    /**
     * Instantiate an exception given an error code. The error code corresponds
     * to a message that does not require any parameters to rendered.
     *
     * @param errorCode the error code
     */
    public ArchetypeLoaderException(ErrorCode errorCode) {
        super(messages.getMessage(errorCode.toString()));
        this.errorCode = errorCode;
    }

    /**
     * Instantiate an exception given an error code and a set of associated
     * object parameters. The params are required to render the message.
     *
     * @param errorCode the error code
     * @param params    the parameters used to render the message associated
     *                  with the error code
     */
    public ArchetypeLoaderException(ErrorCode errorCode, Object ... params) {
        super(messages.getMessage(errorCode.toString(), params));
        this.errorCode = errorCode;
    }

    /**
     * Create an exception with the following error code and the root exception.
     * The error code is used to render a local specific message.
     *
     * @param errorCode the error code
     * @param cause     the root exception
     */
    public ArchetypeLoaderException(ErrorCode errorCode, Throwable cause,
                                    Object ... params) {
        super(messages.getMessage(errorCode.toString(), params), cause);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code.
     *
     * @return the error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
