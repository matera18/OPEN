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
 * Copyright 2014 (C) OpenVPMS Ltd. All Rights Reserved.
 */

package org.openvpms.web.workspace.patient.mr;

import nextapp.echo2.app.table.TableColumn;
import nextapp.echo2.app.table.TableColumnModel;
import org.openvpms.archetype.rules.patient.reminder.ReminderArchetypes;
import org.openvpms.component.business.domain.im.archetype.descriptor.ArchetypeDescriptor;
import org.openvpms.component.business.service.archetype.helper.DescriptorHelper;
import org.openvpms.component.system.common.query.SortConstraint;
import org.openvpms.web.component.im.layout.LayoutContext;

/**
 * Table model for <em>act.patientReminder</em> and <em>act.patientAlert</em> acts.
 *
 * @author Tim Anderson
 */
public class ReminderActTableModel extends PatientRecordActTableModel {

    /**
     * The reminder type model index.
     */
    private int reminderTypeIndex;

    /**
     * The product model index.
     */
    private int productIndex;


    /**
     * Constructs a {@code ReminderActTableModel}.
     *
     * @param shortNames the act archetype short names
     * @param context    the layout context
     */
    public ReminderActTableModel(String[] shortNames, LayoutContext context) {
        super(shortNames, context);
    }

    /**
     * Returns the sort criteria.
     *
     * @param column    the primary sort column
     * @param ascending if {@code true} sort in ascending order; otherwise sort in {@code descending} order
     * @return the sort criteria, or {@code null} if the column isn't sortable
     */
    @Override
    public SortConstraint[] getSortConstraints(int column, boolean ascending) {
        TableColumn col = getColumn(column);
        int index = col.getModelIndex();
        if (index == reminderTypeIndex || index == productIndex) {
            // can't sort on these as they aren't applicable to patient alerts
            return null;
        }
        return super.getSortConstraints(column, ascending);
    }

    /**
     * Returns a list of descriptor names to include in the table.
     *
     * @return the list of descriptor names to include in the table
     */
    @Override
    protected String[] getNodeNames() {
        return new String[]{"startTime", "endTime", "status", "description"};
    }

    /**
     * Returns the index to insert the archetype column.
     *
     * @param showId determines if the Id column is being displayed
     * @return the index to insert the archetype column
     */
    @Override
    protected int getArchetypeColumnIndex(boolean showId) {
        return showId ? 2 : 3;
    }

    /**
     * Creates a column model for a set of archetypes.
     * This implementation adds the act.patientReminder reminderType and product nodes.
     *
     * @param shortNames the archetype short names
     * @param context    the layout context
     * @return a new column model
     */
    @Override
    protected TableColumnModel createColumnModel(String[] shortNames,
                                                 LayoutContext context) {
        TableColumnModel model = super.createColumnModel(shortNames, context);
        String shortName = ReminderArchetypes.REMINDER;
        ArchetypeDescriptor archetype = DescriptorHelper.getArchetypeDescriptor(
                shortName);
        if (archetype != null) {
            TableColumn reminderType = addColumn(archetype, "reminderType", model);
            reminderTypeIndex = reminderType.getModelIndex();

            TableColumn product = addColumn(archetype, "product", model);
            productIndex = product.getModelIndex();
        }
        return model;
    }
}
