package org.jboss.pressgangccms.client.local.mvp.presenter.topic;

import javax.enterprise.context.Dependent;

import org.jboss.pressgangccms.client.local.mvp.presenter.base.TemplatePresenter;
import org.jboss.pressgangccms.client.local.mvp.view.topic.TopicViewInterface;
import com.google.gwt.user.client.ui.HasWidgets;

@Dependent
public class TopicRenderedPresenter extends TemplatePresenter
{
	private String topicId;

	public interface Display extends TopicViewInterface
	{

	}

	@Override
	public void go(final HasWidgets container)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parseToken(final String historyToken)
	{
		// TODO Auto-generated method stub		
	}
}
