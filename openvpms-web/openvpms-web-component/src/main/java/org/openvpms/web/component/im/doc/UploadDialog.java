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
 */

package org.openvpms.web.component.im.doc;

import nextapp.echo2.app.filetransfer.UploadEvent;
import nextapp.echo2.app.filetransfer.UploadListener;
import nextapp.echo2.app.filetransfer.UploadSelect;
import org.openvpms.web.echo.dialog.PopupDialog;
import org.openvpms.web.echo.help.HelpContext;
import org.openvpms.web.resource.i18n.Messages;

import java.util.TooManyListenersException;


/**
 * File upload dialog.
 *
 * @author Tim Anderson
 */
public class UploadDialog extends PopupDialog {

    /**
     * Constructs a {@code UploadDialog}.
     *
     * @param help the help context
     */
    public UploadDialog(final UploadListener listener, HelpContext help) {
        super(Messages.get("file.upload.title"), CANCEL, help);
        setModal(true);
        UploadSelect select = new UploadSelect();

        UploadListener delegate = new UploadListener() {
            public void fileUpload(UploadEvent event) {
                close();
                listener.fileUpload(event);
            }

            public void invalidFileUpload(UploadEvent event) {
                close();
                listener.invalidFileUpload(event);
            }
        };

        try {
            select.addUploadListener(delegate);
        } catch (TooManyListenersException exception) {
            throw new RuntimeException(exception);
        }
        getLayout().add(select);
    }

}
