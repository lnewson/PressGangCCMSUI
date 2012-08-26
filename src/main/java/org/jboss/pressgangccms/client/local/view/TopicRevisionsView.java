package org.jboss.pressgangccms.client.local.view;

import org.jboss.pressgangccms.client.local.constants.CSSConstants;
import org.jboss.pressgangccms.client.local.constants.Constants;
import org.jboss.pressgangccms.client.local.presenter.TopicRevisionsPresenter;
import org.jboss.pressgangccms.client.local.resources.css.TableResources;
import org.jboss.pressgangccms.client.local.resources.images.ImageResources;
import org.jboss.pressgangccms.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgangccms.client.local.view.base.TopicViewBase;
import org.jboss.pressgangccms.rest.v1.entities.RESTTopicV1;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;

/**
 * A MVP view for displaying a topic's revision history
 * 
 * @author Matthew Casperson
 */
public class TopicRevisionsView extends TopicViewBase implements TopicRevisionsPresenter.Display
{
	public static final String HISTORY_TOKEN = "TopicHistoryView";

	private final VerticalPanel searchResultsPanel = new VerticalPanel();

	private final SimplePager pager = new SimplePager();
	private final CellTable<RESTTopicV1> results = new CellTable<RESTTopicV1>(Constants.MAX_SEARCH_RESULTS, (Resources)GWT.create(TableResources.class));
	private AsyncDataProvider<RESTTopicV1> provider;

	private final TextColumn<RESTTopicV1> revisionNumber = new TextColumn<RESTTopicV1>()
	{
		@Override
		public String getValue(final RESTTopicV1 object)
		{
			return object.getRevision().toString();
		}
	};

	private final TextColumn<RESTTopicV1> revisionDate = new TextColumn<RESTTopicV1>()
	{
		@Override
		public String getValue(final RESTTopicV1 object)
		{
			return DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG).format(object.getLastModified());
		}
	};

	@Override
	public AsyncDataProvider<RESTTopicV1> getProvider()
	{
		return provider;
	}

	@Override
	public void setProvider(final AsyncDataProvider<RESTTopicV1> provider)
	{
		this.provider = provider;
		provider.addDataDisplay(results);
	}

	@Override
	public CellTable<RESTTopicV1> getResults()
	{
		return results;
	}

	@Override
	public SimplePager getPager()
	{
		return pager;
	}

	public TopicRevisionsView()
	{
		results.addColumn(revisionNumber, PressGangCCMSUI.INSTANCE.RevisionNumber());
		results.addColumn(revisionDate, PressGangCCMSUI.INSTANCE.RevisionDate());

		searchResultsPanel.addStyleName(CSSConstants.SEARCHRESULTSPANEL);

		searchResultsPanel.add(results);
		searchResultsPanel.add(pager);

		pager.setDisplay(results);

		this.getPanel().add(searchResultsPanel);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SimpleBeanEditorDriver getDriver()
	{
		return null;
	}

	@Override
	public void initialize(final RESTTopicV1 topic)
	{

	}

	@Override
	protected void populateTopActionBar()
	{
		addActionButton(this.getRendered());
		addActionButton(this.getXml());
		addActionButton(this.getXmlErrors());
		addActionButton(this.getFields());
		addActionButton(this.getTags());
		addActionButton(this.getBugs());
		final Image downImage = new Image(ImageResources.INSTANCE.historyDown48());
		downImage.addStyleName(CSSConstants.SPACEDBUTTON);
		addActionButton(downImage);
		addActionButton(this.getSave());

		addRightAlignedActionButtonPaddingPanel();
	}
}
