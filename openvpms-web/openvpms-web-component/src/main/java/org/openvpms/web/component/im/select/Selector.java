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

package org.openvpms.web.component.im.select;

import nextapp.echo2.app.Button;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.layout.RowLayoutData;
import org.apache.commons.lang.ClassUtils;
import org.openvpms.component.business.domain.im.common.IMObject;
import org.openvpms.web.echo.button.ButtonRow;
import org.openvpms.web.echo.factory.ButtonFactory;
import org.openvpms.web.echo.factory.LabelFactory;
import org.openvpms.web.echo.factory.RowFactory;
import org.openvpms.web.echo.factory.TextComponentFactory;
import org.openvpms.web.echo.focus.FocusGroup;
import org.openvpms.web.echo.style.Styles;
import org.openvpms.web.echo.text.TextField;
import org.openvpms.web.resource.i18n.Messages;


/**
 * Component that provides a 'select' button and object summary.
 *
 * @author Tim Anderson
 */
public abstract class Selector<T extends IMObject> {

    /**
     * Determines the layout of the button(s).
     */
    public enum ButtonStyle {

        LEFT, RIGHT, NONE
    }

    /**
     * Determines how the object is displayed.
     */
    public enum Format {

        NAME, DESCRIPTION, SUMMARY
    }

    /**
     * The button identifier.
     */
    private final String buttonId;

    /**
     * The 'select' button.
     */
    private Button select;

    /**
     * Selected object's label. Null if the selector is editable.
     */
    private Label objectLabel;

    /**
     * Selected object's text. Null if the selector is not editable.
     */
    private TextField objectText;

    /**
     * Determines if the selector should expand to the available width.
     */
    private boolean fillWidth;

    /**
     * Determines the layout of the buttons.
     */
    private final ButtonStyle buttonStyle;

    /**
     * Determines if the selector may be edited.
     */
    private final boolean editable;

    /**
     * The presentation format.
     */
    private Format format = Format.SUMMARY;

    /**
     * The component.
     */
    private Component component;

    /**
     * The child component. The deactivated label is added to this if the object is inactive.
     */
    private Component child;

    /**
     * Deactivated label. Null if the object is active.
     */
    private Label deactivated;

    /**
     * The focus group.
     */
    private final FocusGroup focusGroup;


    /**
     * Constructs a {@link Selector}.
     * <p/>
     * Displays button(s) to the left of the object display.
     */
    public Selector() {
        this(ButtonStyle.LEFT, false);
    }

    /**
     * Constructs a {@link Selector}.
     * <p/>
     * Displays button(s) to the left of the object display.
     *
     * @param buttonId the button identifier
     */
    public Selector(String buttonId) {
        this(buttonId, ButtonStyle.LEFT, false);
    }

    /**
     * Constructs a {@link Selector}.
     *
     * @param style    determines the layout of the button(s)
     * @param editable determines if the selector is editable
     */
    public Selector(ButtonStyle style, boolean editable) {
        this("button.select", style, editable);
    }

    /**
     * Constructs a {@link Selector}.
     *
     * @param buttonId the button identifier
     * @param style    determines the layout of the button(s)
     * @param editable determines if the selector is editable
     */
    public Selector(String buttonId, ButtonStyle style, boolean editable) {
        this.buttonId = buttonId;
        buttonStyle = style;
        this.editable = editable;
        focusGroup = new FocusGroup(ClassUtils.getShortClassName(getClass()));
    }


    /**
     * Returns the selector component.
     *
     * @return the selector component
     */
    public Component getComponent() {
        if (component == null) {
            doLayout();
        }
        return component;
    }

    /**
     * Returns the focus group.
     *
     * @return the focus group
     */
    public FocusGroup getFocusGroup() {
        return focusGroup;
    }

    /**
     * Returns the 'select' button.
     *
     * @return the 'select' button
     */
    public Button getSelect() {
        if (select == null) {
            select = createSelectButton(buttonId);
        }
        return select;
    }

    /**
     * Returns the editable text field.
     *
     * @return the editable text field, or {@code null} if this is not an editable selector
     */
    public TextField getTextField() {
        getObjectComponent();
        return objectText;
    }

    /**
     * Returns the text from the editable text field.
     *
     * @return the text, or {@code null} if there is no text, or this is not an editable selector.
     */
    public String getText() {
        TextField field = getTextField();
        return (field != null) ? field.getText() : null;
    }

    /**
     * Sets the current object.
     *
     * @param object the object. May be {@code null}
     */
    public void setObject(T object) {
        if (object != null) {
            setObject(object.getName(), object.getDescription(), object.isActive());
        } else {
            setObject(null, null, true);
        }
    }

    /**
     * Sets the presentation format.
     *
     * @param format the presentation format
     */
    public void setFormat(Format format) {
        this.format = format;
    }

    /**
     * Determines if the selector should expand to the parent container's width.
     * <p/>
     * Default is {@code false}.
     *
     * @param fillWidth if {@code true} expand to the parent container's width
     */
    public void setFillWidth(boolean fillWidth) {
        this.fillWidth = fillWidth;
        if (objectText != null) {
            objectText.setWidth(new Extent(100, Extent.PERCENT));
        }

        if (component != null) {
            component.removeAll();
            doLayout(component);
        }
    }

    /**
     * Sets the current object details.
     *
     * @param name        the object name. May be {@code null}
     * @param description the object description. May be {@code null}
     * @param active      determines if the object is active
     */
    protected void setObject(String name, String description, boolean active) {
        String text = null;
        if (name != null || description != null) {
            if (format == Format.NAME || description == null) {
                text = Messages.format("imobject.name", name);
            } else if (format == Format.DESCRIPTION || name == null) {
                text = Messages.format("imobject.description", description);
            } else if (format == Format.SUMMARY) {
                text = Messages.format("imobject.summary", name, description);
            }
        }
        getComponent();
        if (objectText != null) {
            objectText.setText(text);
            if (objectText.isEnabled()) {
                int length = (text != null) ? text.length() : 0;
                if (objectText.getCursorPosition() < length) {
                    objectText.setCursorPosition(length);
                }
            }
        } else {
            objectLabel.setText(text);
        }

        if (child != null) {
            if (!active) {
                if (deactivated == null) {
                    deactivated = LabelFactory.create("imobject.deactivated", "Selector.Deactivated");
                    child.add(deactivated);
                }
            } else if (deactivated != null) {
                child.remove(deactivated);
                deactivated = null;
            }
        }
    }

    /**
     * Lays out the component
     */
    protected void doLayout() {
        component = RowFactory.create(Styles.CELL_SPACING);
        doLayout(component);
    }

    /**
     * Lays out the component.
     *
     * @param container the container
     */
    private void doLayout(Component container) {
        child = null;
        Component objectComponent = getObjectComponent();
        if (buttonStyle == ButtonStyle.RIGHT && fillWidth) {
            // set the objectComponent to fill the available width to force the buttons to the right
            RowLayoutData layout = new RowLayoutData();
            layout.setWidth(Styles.FULL_WIDTH);
            container.add(objectComponent);
            objectComponent.setLayoutData(layout);
            child = RowFactory.create(getButtons(component));
            container.add(objectComponent);
        } else if (buttonStyle == ButtonStyle.RIGHT) {
            child = RowFactory.create(Styles.CELL_SPACING, RowFactory.create(objectComponent, getButtons(component)));
        } else if (buttonStyle == ButtonStyle.LEFT) {
            // display button(s) on the left
            child = RowFactory.create(Styles.CELL_SPACING, objectComponent);
            child.add(getButtons(child), 0);
        } else {
            // no buttons
            container.add(objectComponent);
        }
        if (objectText != null) {
            if (buttonStyle == ButtonStyle.RIGHT) {
                focusGroup.add(0, objectText);
            } else {
                focusGroup.add(objectText);
            }
        }
        if (child != null) {
            container.add(child);
        }
    }

    /**
     * Returns the object component, creating it if needed.
     *
     * @return the object component
     */
    protected Component getObjectComponent() {
        Component component;
        if (editable) {
            if (objectText == null) {
                objectText = TextComponentFactory.create();
                objectText.setStyleName("Selector");
                if (fillWidth) {
                    objectText.setWidth(Styles.FULL_WIDTH);
                }
            }
            component = objectText;
        } else {
            if (objectLabel == null) {
                objectLabel = LabelFactory.create();
            }
            component = objectLabel;
        }
        return component;
    }

    /**
     * Returns the button component.
     *
     * @param container the parent container
     * @return the button(s)
     */
    protected ButtonRow getButtons(Component container) {
        ButtonRow result = new ButtonRow(container, getFocusGroup());
        result.addButton(getSelect());
        return result;
    }

    /**
     * Creates the select button.
     *
     * @param buttonId the button identifier
     * @return the select button
     */
    protected Button createSelectButton(String buttonId) {
        return ButtonFactory.create(buttonId);
    }

}
