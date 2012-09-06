package org.jboss.pressgangccms.client.local.mvp.presenter.tag;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.pressgangccms.client.local.mvp.presenter.base.TemplatePresenter;
import org.jboss.pressgangccms.client.local.mvp.view.tag.TagProjectsView;
import org.jboss.pressgangccms.client.local.mvp.view.tag.TagViewInterface;
import org.jboss.pressgangccms.client.local.restcalls.RESTCalls;
import org.jboss.pressgangccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTProjectV1;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

@Dependent
public class TagProjectsPresenter extends TemplatePresenter
{
	public interface Display extends TagViewInterface
	{
		AsyncDataProvider<RESTProjectV1> getProvider();

		void setProvider(final AsyncDataProvider<RESTProjectV1> provider);

		CellTable<RESTProjectV1> getResults();

		SimplePager getPager();
		
		Column<RESTProjectV1, String> getButtonColumn();
	}

	@Inject
	private Display display;
	
	private String queryString;

	@Override
	public void parseToken(final String searchToken)
	{
		queryString = searchToken.replace(TagProjectsView.HISTORY_TOKEN + ";", "");
	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.getTopLevelPanel());

		bind();
	}

	private void bind()
	{
		super.bind(display);

		final AsyncDataProvider<RESTProjectV1> provider = new AsyncDataProvider<RESTProjectV1>()
		{
			@Override
			protected void onRangeChanged(final HasData<RESTProjectV1> display)
			{
				final int start = display.getVisibleRange().getStart();
				final int length = display.getVisibleRange().getLength();
				final int end = start + length;

				final RESTCalls.RESTCallback<RESTProjectCollectionV1> callback = new RESTCalls.RESTCallback<RESTProjectCollectionV1>()
				{
					@Override
					public void begin()
					{
						startProcessing();
					}

					@Override
					public void generalException(final Exception ex)
					{
						stopProcessing();
					}

					@Override
					public void success(final RESTProjectCollectionV1 retValue)
					{
						try
						{
							updateRowData(start, retValue.getItems());
							updateRowCount(retValue.getSize(), true);
						}
						finally
						{
							stopProcessing();
						}
					}	

					@Override
					public void failed()
					{
						stopProcessing();
					}
				};
				
				RESTCalls.getProjectsFromQuery(callback, queryString, start, end);
			}
		};

		display.setProvider(provider);
	}

	private void stopProcessing()
	{
		display.setSpinnerVisible(false);
	}

	private void startProcessing()
	{
		display.setSpinnerVisible(true);
	}
}