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

package org.openvpms.web.component.property;

import nextapp.echo2.app.event.WindowPaneListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openvpms.component.business.domain.im.common.IMObject;
import org.openvpms.component.business.service.archetype.IArchetypeService;
import org.openvpms.component.business.service.archetype.ValidationError;
import org.openvpms.component.business.service.archetype.ValidationException;
import org.openvpms.component.system.common.exception.OpenVPMSException;
import org.openvpms.web.echo.error.ErrorHandler;
import org.openvpms.web.resource.i18n.Messages;
import org.openvpms.web.system.ServiceHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Validation helper.
 *
 * @author Tim Anderson
 */
public class ValidationHelper {

    /**
     * The logger.
     */
    private static final Log log = LogFactory.getLog(ValidationHelper.class);


    /**
     * Validates an object.
     *
     * @param object the object to validate
     * @return {@code true} if the object is valid; otherwise {@code false}
     */
    public static boolean isValid(IMObject object) {
        IArchetypeService service = ServiceHelper.getArchetypeService();
        return isValid(object, service);
    }

    /**
     * Validates an object.
     *
     * @param object  the object to validate
     * @param service the archetype service
     * @return {@code true} if the object is valid; otherwise {@code false}
     */
    public static boolean isValid(IMObject object, IArchetypeService service) {
        List<ValidatorError> errors = validate(object, service);
        return (errors == null);
    }

    /**
     * Validates an object.
     *
     * @param object  the object to validate
     * @param service the archetype service
     * @return a list of validation errors, or {@code null} if the object is valid
     */
    public static List<ValidatorError> validate(IMObject object, IArchetypeService service) {
        List<ValidatorError> result = null;
        try {
            service.validateObject(object);
        } catch (ValidationException exception) {
            log.debug(exception, exception);
            List<ValidationError> errors = exception.getErrors();
            result = new ArrayList<ValidatorError>();
            if (errors.isEmpty()) {
                result.add(new ValidatorError(exception.getMessage()));
            } else {
                for (ValidationError error : errors) {
                    result.add(new ValidatorError(error));
                }
            }
        } catch (OpenVPMSException exception) {
            log.debug(exception, exception);
            result = new ArrayList<ValidatorError>();
            result.add(new ValidatorError(exception.getMessage()));
        }
        return result;
    }

    /**
     * Displays the first error from a validator.
     * <p/>
     * The error will be formatted.
     *
     * @param validator the validator
     */
    public static void showError(Validator validator) {
        showError(null, validator, null);
    }

    /**
     * Displays the first error from a validator.
     * <p/>
     * The error will be formatted.
     *
     * @param title     the dialog title. May  be {@code null}
     * @param validator the validator
     * @param listener  the listener to notify when the dialog closes. May be {@code null}
     */
    public static void showError(String title, Validator validator, WindowPaneListener listener) {
        showError(title, validator, null, true, listener);
    }

    /**
     * Display the first error from a validator.
     *
     * @param title     the dialog title. May  be {@code null}
     * @param validator the validator
     * @param key       resource bundle key, if the error should be included in text. May be {@code null}
     * @param formatted if {@code true} format the message, otherwise display as is
     */
    public static void showError(String title, Validator validator, String key, boolean formatted) {
        showError(title, validator, key, formatted, null);
    }

    /**
     * Display the first error from a validator.
     *
     * @param title     the dialog title. May  be {@code null}
     * @param validator the validator
     * @param key       resource bundle key, if the error should be included in text. May be {@code null}
     * @param formatted if {@code true} format the message, otherwise display as is
     * @param listener  the listener to notify when the dialog closes. May be {@code null}
     */
    public static void showError(String title, Validator validator, String key, boolean formatted,
                                 WindowPaneListener listener) {
        Collection<Modifiable> invalid = validator.getInvalid();
        if (!invalid.isEmpty()) {
            Modifiable modifiable = invalid.iterator().next();
            List<ValidatorError> errors = validator.getErrors(modifiable);
            if (!errors.isEmpty()) {
                ValidatorError error = errors.get(0);
                String message = (formatted) ? error.toString() : error.getMessage();
                if (key != null) {
                    message = Messages.format(key, message);
                }
                ErrorHandler.getInstance().error(title, message, null, listener);
            }
        }
    }

}
