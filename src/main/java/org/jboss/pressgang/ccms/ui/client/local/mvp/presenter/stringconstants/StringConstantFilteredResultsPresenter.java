package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.stringconstants;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.HasData;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTStringConstantCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTProjectCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTStringConstantCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTStringConstantV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.constants.ServiceConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.filteredresults.BaseFilteredResultsComponent;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.filteredresults.BaseFilteredResultsView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.filteredresults.BaseFilteredResultsViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.searchandedit.BaseSearchAndEditViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.BaseRestCallback;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls;
import org.jboss.pressgang.ccms.ui.client.local.utilities.EnhancedAsyncDataProvider;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.clearContainerAndAddTopLevelPanel;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.removeHistoryToken;

/**
    The presenter used to display the list of string constants
 */
@Dependent
public class StringConstantFilteredResultsPresenter extends BaseFilteredResultsComponent<RESTStringConstantCollectionItemV1> {

    public static final String HISTORY_TOKEN = "StringConstantFilteredResultsView";

    @Inject
    private Display display;
    private String queryString;

    @Override
    protected void displayQueryElements(@NotNull final String queryString) {
        final String[] queryStringElements = queryString.replace(Constants.QUERY_PATH_SEGMENT_PREFIX, "").split(";");
        for (final String queryStringElement : queryStringElements) {
            final String[] queryElements = queryStringElement.split("=");

            if (queryElements.length == 2) {
                if (queryElements[0].equals(CommonFilterConstants.STRING_CONSTANT_IDS_FILTER_VAR)) {
                    this.display.getIdFilter().setText(URL.decodeQueryString(queryElements[1]));
                } else if (queryElements[0].equals(CommonFilterConstants.STRING_CONSTANT_NAME_FILTER_VAR)) {
                    this.display.getNameFilter().setText(URL.decodeQueryString(queryElements[1]));
                } else if (queryElements[0].equals(CommonFilterConstants.STRING_CONSTANT_VALUE_FILTER_VAR)) {
                    this.display.getValueFilter().setText(URL.decodeQueryString(queryElements[1]));
                }
            }
        }
    }

    @Override
    protected EnhancedAsyncDataProvider<RESTStringConstantCollectionItemV1> generateListProvider(@NotNull final String queryString, @NotNull final BaseTemplateViewInterface waitDisplay) {
        final EnhancedAsyncDataProvider<RESTStringConstantCollectionItemV1> provider = new EnhancedAsyncDataProvider<RESTStringConstantCollectionItemV1>() {
            @Override
            protected void onRangeChanged(final HasData<RESTStringConstantCollectionItemV1> list) {

                final BaseRestCallback<RESTStringConstantCollectionV1, Display> callback =new BaseRestCallback<RESTStringConstantCollectionV1, Display>(display, new BaseRestCallback.SuccessAction<RESTStringConstantCollectionV1, Display>() {
                    @Override
                    public void doSuccessAction(final RESTStringConstantCollectionV1 retValue, final Display display) {
                        getProviderData().setItems(retValue.getItems());
                        getProviderData().setSize(retValue.getSize());
                        relinkSelectedItem();
                        displayAsynchronousList(getProviderData().getItems(), getProviderData().getSize(), getProviderData().getStartRow());
                    }
                });

                getProviderData().setStartRow(list.getVisibleRange().getStart());
                final int length = list.getVisibleRange().getLength();
                final int end = getProviderData().getStartRow() + length;

                RESTCalls.getStringConstantsFromQuery(callback, queryString, getProviderData().getStartRow(), end);
            }
        };
        return provider;
    }

    @Override
    public String getQuery() {
        final StringBuilder retValue = new StringBuilder();
        if (!display.getIdFilter().getText().isEmpty()) {
            retValue.append(";").append(CommonFilterConstants.STRING_CONSTANT_IDS_FILTER_VAR).append("=").append((Constants.ENCODE_QUERY_OPTIONS ? URL.encodeQueryString(display.getIdFilter().getText()) : display.getIdFilter().getText()));
        }
        if (!display.getValueFilter().getText().isEmpty()) {
            retValue.append(";").append(CommonFilterConstants.STRING_CONSTANT_VALUE_FILTER_VAR).append("=").append((Constants.ENCODE_QUERY_OPTIONS ? URL.encodeQueryString(display.getValueFilter().getText()) : display.getValueFilter().getText()));
        }
        if (!display.getNameFilter().getText().isEmpty()) {
            retValue.append(";").append(CommonFilterConstants.STRING_CONSTANT_NAME_FILTER_VAR).append("=").append((Constants.ENCODE_QUERY_OPTIONS ? URL.encodeQueryString(display.getNameFilter().getText()) : display.getNameFilter().getText()));
        }

        return retValue.toString().isEmpty() ? Constants.QUERY_PATH_SEGMENT_PREFIX
                : Constants.QUERY_PATH_SEGMENT_PREFIX_WO_SEMICOLON + retValue.toString();
    }

    @Override
    @NotNull
    public Display getDisplay() {
        return display;
    }

    @Override
    public void bindExtendedFilteredResults(final int topicId, @NotNull final String pageId, final String queryString) {
        super.bindFilteredResults(topicId, pageId, queryString, display);
        display.setProvider(generateListProvider(queryString, display));
    }

    @Override
    public void parseToken(@NotNull final String historyToken) {
        queryString = removeHistoryToken(historyToken, HISTORY_TOKEN);
    }

    @Override
    public void go(HasWidgets container) {
        clearContainerAndAddTopLevelPanel(container, display);
        bindExtendedFilteredResults(ServiceConstants.DEFAULT_HELP_TOPIC, HISTORY_TOKEN, queryString);
    }

    public interface Display extends BaseFilteredResultsViewInterface<RESTStringConstantCollectionItemV1> {

        TextBox getIdFilter();

        TextBox getNameFilter();

        TextBox getValueFilter();
    }
}