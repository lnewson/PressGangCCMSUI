package org.jboss.pressgang.ccms.ui.client.local.ui.editor.topicview;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class RESTTranslatedTopicV1BasicDetailsEditor extends Grid implements Editor<RESTTranslatedTopicV1> {

    private static final int ROWS = 7;
    private static final int COLS = 2;

    private final SimpleIntegerBox id = new SimpleIntegerBox();
    private final SimpleIntegerBox revision = new SimpleIntegerBox();
    private final ValueListBox<String> locale = new ValueListBox<String>(new Renderer<String>() {

        @Override
        public String render(final String object) {
            return object;
        }

        @Override
        public void render(final String object, final Appendable appendable) throws IOException {
        }
    });
    private final TextBox title = new TextBox();
    private final TextArea description = new TextArea();
    private final DateBox created = new DateBox();

    public DateBox createdEditor() {
        return created;
    }

    public TextArea descriptionEditor() {
        return description;
    }

    public TextBox titleEditor() {
        return title;
    }

    public ValueListBox<String> localeEditor() {
        return locale;
    }

    public SimpleIntegerBox revisionEditor() {
        return revision;
    }

    public SimpleIntegerBox idEditor() {
        return id;
    }

    public RESTTranslatedTopicV1BasicDetailsEditor(final boolean readOnly, final List<String> locales) {
        super(ROWS, COLS);

        this.addStyleName(CSSConstants.TOPIC_VIEW_PANEL);

        title.setReadOnly(readOnly);
        /* http://code.google.com/p/google-web-toolkit/issues/detail?id=6112 */
        DOM.setElementPropertyBoolean(locale.getElement(), "disabled", readOnly);
        /* http://stackoverflow.com/a/11176707/157605 */
        locale.setValue("");
        locale.setAcceptableValues(locales == null ? new ArrayList<String>() : locales);
        description.setReadOnly(readOnly);

        id.setReadOnly(true);
        revision.setReadOnly(true);
        created.setEnabled(false);

        id.addStyleName(CSSConstants.TOPIC_VIEW_ID_FIELD);
        revision.addStyleName(CSSConstants.TOPIC_VIEW_REVISION_NUMBER_FIELD);
        title.addStyleName(CSSConstants.TOPIC_VIEW_TITLE_FIELD);
        locale.addStyleName(CSSConstants.TOPIC_VIEW_LOCALE_FIELD);
        description.addStyleName(CSSConstants.TOPIC_VIEW_DESCRIPTION_FIELD);

        int row = 0;        
        this.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.TopicID()));
        this.setWidget(row, 1, id);
        
        ++row;
        this.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.TopicRevision()));
        this.setWidget(row, 1, revision);
        
        ++row;
        this.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.TopicCreated()));
        this.setWidget(row, 1, created);

        ++row;
        this.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.TopicLocale()));
        this.setWidget(row, 1, locale);

        ++row;
        this.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.TopicTitle()));
        this.setWidget(row, 1, title);

        ++row;
        this.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.TopicDescription()));        
        this.setWidget(row, 1, description);

        for (int i = 0; i < ROWS; ++i) {
            this.getCellFormatter().addStyleName(i, 0, CSSConstants.TOPIC_VIEW_LABEL);
        }

        for (int i = 0; i < ROWS - 1; ++i) {
            this.getCellFormatter().addStyleName(i, 1, CSSConstants.TOPIC_VIEW_DETAIL);
        }
        this.getCellFormatter().addStyleName(ROWS - 1, 1, CSSConstants.TOPIC_VIEW_DESCRIPTION_DETAIL);
    }
}