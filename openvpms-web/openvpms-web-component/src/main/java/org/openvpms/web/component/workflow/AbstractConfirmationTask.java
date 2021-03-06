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

package org.openvpms.web.component.workflow;

import nextapp.echo2.app.event.WindowPaneEvent;
import org.openvpms.web.echo.dialog.ConfirmationDialog;
import org.openvpms.web.echo.event.WindowPaneListener;
import org.openvpms.web.echo.help.HelpContext;

/**
 * An {@link EvalTask} task that pops up a Yes/No/Cancel or OK/Cancel
 * confirmation dialog.
 * <p/>
 * It evaluates {@code true} if Yes/OK is selected, or {@code false}
 * if No is selected. Selecting Cancel cancels the task.
 *
 * @author Tim Anderson
 */
public abstract class AbstractConfirmationTask extends EvalTask<Boolean> {

    /**
     * The help context.
     */
    private final HelpContext help;

    /**
     * The confirmation dialog.
     */
    private ConfirmationDialog dialog;

    /**
     * Constructs an {@link AbstractConfirmationTask}.
     *
     * @param help the help context
     */
    public AbstractConfirmationTask(HelpContext help) {
        this.help = help;
    }

    /**
     * Returns the dialog.
     *
     * @return the dialog, or {@code null} if the task isn't started
     */
    public ConfirmationDialog getConfirmationDialog() {
        return dialog;
    }

    /**
     * Starts the task.
     * <p/>
     * The registered {@link TaskListener} will be notified on completion or failure.
     *
     * @param context the task context
     */
    public void start(TaskContext context) {
        dialog = createConfirmationDialog(context, help);
        dialog.addWindowPaneListener(new WindowPaneListener() {
            public void onClose(WindowPaneEvent event) {
                String action = dialog.getAction();
                dialog = null;
                if (ConfirmationDialog.YES_ID.equals(action) || ConfirmationDialog.OK_ID.equals(action)) {
                    setValue(true);
                } else if (ConfirmationDialog.NO_ID.equals(action)) {
                    setValue(false);
                } else {
                    notifyCancelled();
                }
            }
        });
        dialog.show();
    }

    /**
     * Creates a new confirmation dialog.
     *
     * @param context the task context
     * @param help    the help context
     * @return a new confirmation dialog
     */
    protected abstract ConfirmationDialog createConfirmationDialog(TaskContext context, HelpContext help);

}
