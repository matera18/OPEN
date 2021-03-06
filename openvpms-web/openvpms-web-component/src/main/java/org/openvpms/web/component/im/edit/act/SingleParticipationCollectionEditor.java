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
 * Copyright 2013 (C) OpenVPMS Ltd. All Rights Reserved.
 */

package org.openvpms.web.component.im.edit.act;

import org.openvpms.component.business.domain.im.common.IMObject;
import org.openvpms.component.business.domain.im.common.Participation;
import org.openvpms.web.component.im.edit.IMObjectEditor;
import org.openvpms.web.component.im.edit.SingleIMObjectCollectionEditor;
import org.openvpms.web.component.im.layout.LayoutContext;
import org.openvpms.web.component.property.CollectionProperty;


/**
 * Editor for collections of {@link Participation}s with 0..1 or 1..1
 * cardinality.
 *
 * @author Tim Anderson
 */
public class SingleParticipationCollectionEditor
        extends SingleIMObjectCollectionEditor {

    /**
     * Constructs a {@link SingleParticipationCollectionEditor}.
     *
     * @param property the collection property
     * @param object   the parent object
     * @param context  the layout context
     */
    public SingleParticipationCollectionEditor(CollectionProperty property, IMObject object, LayoutContext context) {
        super(property, object, context);
    }

    /**
     * Returns the participation editor, or {@code null} if there is
     * no current participation editor
     *
     * @return the participation editor. May be {@code null}
     */
    protected ParticipationEditor getParticipationEditor() {
        IMObjectEditor editor = getCurrentEditor();
        return (editor instanceof ParticipationEditor) ?
               (ParticipationEditor) editor : null;
    }

    /**
     * Determines if the participation is empty. This returns true if
     * the participation entity is null and the participation is optional.
     *
     * @return {@code true} if the participation is null
     */
    protected boolean isEmpty() {
        ParticipationEditor editor = getParticipationEditor();
        return editor != null && getCollection().getMinCardinality() == 0 && editor.isNull();
    }

}
