package org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterV1;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.search.SearchPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.WaitingDialog;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.ui.UIUtilities;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.search.SearchUIProjectsEditor;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.tag.SearchUIProjects;
import org.jetbrains.annotations.NotNull;

public class SearchView extends BaseTemplateView implements SearchPresenter.Display {

    private final PushButton searchTopics = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Search());
    private final Label tags = UIUtilities.createTopTabDownLabel(PressGangCCMSUI.INSTANCE.Tags());
    private final PushButton fields = UIUtilities.createTopTabPushButton(PressGangCCMSUI.INSTANCE.Fields());
    private final PushButton filters = UIUtilities.createTopTabPushButton(PressGangCCMSUI.INSTANCE.Filters());
    private final PushButton locales = UIUtilities.createTopTabPushButton(PressGangCCMSUI.INSTANCE.Locales());

    /**
     * The GWT Editor Driver
     */
    private final SearchPresenterDriver driver = GWT.create(SearchPresenterDriver.class);
    /**
     * The UI hierarchy
     */
    private final SearchUIProjects searchUIProjects = new SearchUIProjects();

    /**
     * The dialog that is presented when the view is unavailable.
     */
    private final WaitingDialog waiting = new WaitingDialog();

    @NotNull
    @Override
    public SearchUIProjects getSearchUIProjects() {
        return searchUIProjects;
    }

    @Override
    public SearchPresenterDriver getDriver() {
        return driver;
    }

    @Override
    public PushButton getSearchTopics() {
        return searchTopics;
    }

    @Override
    public PushButton getFields() {
        return fields;
    }

    @Override
    public PushButton getFilters() {
        return filters;
    }

    @Override
    public PushButton getLocales() {
        return locales;
    }

    public SearchView() {
        super(PressGangCCMSUI.INSTANCE.PressGangCCMS(), PressGangCCMSUI.INSTANCE.Search());

        /* Build the action bar icons */
        addActionButton(searchTopics);
        addActionButton(tags);
        addActionButton(fields);
        //addActionButton(locales);
        addActionButton(filters);
    }

    @Override
    public void display(final RESTTagCollectionV1 tagCollection, final boolean readOnly) {

        throw new UnsupportedOperationException("display() is not supported. Use displayExtended() instead.");
    }

    public final void displayExtended(@NotNull final RESTTagCollectionV1 tagCollection, final RESTFilterV1 filter, final boolean readOnly) {

        /* Construct a hierarchy of tags from the tag collection */
        searchUIProjects.initialize(tagCollection, filter);

        /* SearchUIProjectsEditor is a grid */
        @NotNull final SearchUIProjectsEditor editor = new SearchUIProjectsEditor(driver, searchUIProjects);
        /* Initialize the driver with the top-level editor */
        driver.initialize(editor);
        /* Copy the data in the object into the UI */
        driver.edit(searchUIProjects);
        /* Add the projects */
        this.getPanel().setWidget(editor);
    }

    @Override
    protected void showWaiting() {
        waiting.center();
        waiting.show();
    }

    @Override
    protected void hideWaiting() {
        waiting.hide();
    }
}
