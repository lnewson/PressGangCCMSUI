package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.ServiceConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.base.ComponentBase;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.TemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;

import com.google.gwt.user.client.ui.HasWidgets;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.BaseRestCallback;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls;

import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.clearContainerAndAddTopLevelPanel;

@Dependent
public class WelcomePresenter extends ComponentBase implements TemplatePresenter {

    public static final String HISTORY_TOKEN = "WelcomeView";

    public interface Display extends BaseTemplateViewInterface {
        void initialize(final RESTTopicV1 topic);
    }

    @Inject
    private Display display;

    @Override
    public void go(final HasWidgets container) {
        display.setViewShown(true);
        clearContainerAndAddTopLevelPanel(container, display);
        process(ServiceConstants.DEFAULT_HELP_TOPIC, HISTORY_TOKEN);
    }

    public void process(final int topicId, final String pageId)
    {
        super.bind(topicId, pageId, display);

        final RESTCalls.RESTCallback<RESTTopicV1> callback = new BaseRestCallback<RESTTopicV1, Display>(
                display,
                new BaseRestCallback.SuccessAction<RESTTopicV1, WelcomePresenter.Display>() {

                    @Override
                    public void doSuccessAction(final RESTTopicV1 retValue, final Display display) {
                        display.initialize(retValue);
                    }

                });
        RESTCalls.getTopic(callback, ServiceConstants.WELCOME_VIEW_CONTENT_TOPIC);
    }


    @Override
    public void parseToken(final String historyToken) {

    }
}
