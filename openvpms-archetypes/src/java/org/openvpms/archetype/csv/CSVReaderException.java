/*
 * Version: 1.0
 *
 * The contents of this file are subject to the OpenVPMS License Version
 * 1.0 (the 'License'); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.openvpms.org/license/
 *
 * Software distributed under the License is distributed on an 'AS IS' basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Copyright 2015 (C) OpenVPMS Ltd. All Rights Reserved.
 */

package org.openvpms.archetype.csv;

import org.apache.commons.resources.Messages;
import org.openvpms.component.system.common.exception.OpenVPMSException;

/**
 * An exception for product I/O errors.
 *
 * @author Tim Anderson
 */
public class CSVReaderException extends CSVException {

    /**
     * An enumeration of error codes.
     */
    public enum ErrorCode {
        RequiredValue,
        InvalidValue,
        InvalidLine,
        UnrecognisedDocument,
        InvalidColumn,
        ReadError
    }

    /**
     * The error code.
     */
    private final ErrorCode errorCode;

    /**
     * The error messages.
     */
    private static Messages MESSAGES = Messages.getMessages("org.openvpms.archetype.csv."
                                                            + OpenVPMSException.ERRMESSAGES_FILE);

    /**
     * Constructs an {@link CSVReaderException}.
     *
     * @param code the error code
     * @param line the line the error occurred on
     * @param args the arguments to format the error message
     */
    public CSVReaderException(ErrorCode code, int line, Object... args) {
        super(MESSAGES.getMessage(code.toString(), args), line);
        this.errorCode = code;
    }

    /**
     * Constructs an {@link CSVReaderException}.
     *
     * @param code  the error code
     * @param line  the line the error occurred on
     * @param cause the cause
     */
    public CSVReaderException(ErrorCode code, int line, Throwable cause) {
        super(cause, line);
        this.errorCode = code;
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
