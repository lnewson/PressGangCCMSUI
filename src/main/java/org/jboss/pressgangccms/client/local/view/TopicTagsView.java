package org.jboss.pressgangccms.client.local.view;

import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgangccms.client.local.constants.CSSConstants;
import org.jboss.pressgangccms.client.local.presenter.TopicTagsPresenter;
import org.jboss.pressgangccms.client.local.resources.images.ImageResources;
import org.jboss.pressgangccms.client.local.ui.UIUtilities;
import org.jboss.pressgangccms.client.local.ui.editor.topicview.assignedtags.TopicTagViewProjectsEditor;
import org.jboss.pressgangccms.client.local.ui.search.SearchUICategory;
import org.jboss.pressgangccms.client.local.ui.search.SearchUIProject;
import org.jboss.pressgangccms.client.local.ui.search.SearchUIProjects;
import org.jboss.pressgangccms.client.local.ui.search.SearchUITag;
import org.jboss.pressgangccms.client.local.view.base.TopicViewBase;
import org.jboss.pressgangccms.rest.v1.entities.RESTTopicV1;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class TopicTagsView extends TopicViewBase implements TopicTagsPresenter.Display
{
	public static final String HISTORY_TOKEN = "TopicTagsView";

	final FlexTable layout = new FlexTable();

	/** The GWT Editor Driver */
	private final TopicTagsPresenterDriver driver = GWT.create(TopicTagsPresenterDriver.class);
	private TopicTagViewProjectsEditor editor;
	private final PushButton add = UIUtilities.createPushButton(ImageResources.INSTANCE.plusGreen32(), ImageResources.INSTANCE.plusGreenDown32(), ImageResources.INSTANCE.plusGreenHover32());

	private final ValueListBox<SearchUIProject> projects;
	private final ValueListBox<SearchUICategory> categories;
	private final ValueListBox<SearchUITag> myTags;

	public interface TopicTagsPresenterDriver extends SimpleBeanEditorDriver<SearchUIProjects, TopicTagViewProjectsEditor>
	{
	}

	public ValueListBox<SearchUITag> getMyTags()
	{
		return myTags;
	}

	public ValueListBox<SearchUICategory> getCategories()
	{
		return categories;
	}

	public ValueListBox<SearchUIProject> getProjects()
	{
		return projects;
	}

	@Override
	public TopicTagViewProjectsEditor getEditor()
	{
		return editor;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SimpleBeanEditorDriver getDriver()
	{
		return driver;
	}

	public TopicTagsView()
	{
		addActionButton(this.getRendered());
		addActionButton(this.getXml());
		addActionButton(this.getXmlErrors());
		addActionButton(this.getFields());
		final Image downImage = new Image(ImageResources.INSTANCE.tagDown48());
		downImage.addStyleName(CSSConstants.SPACEDBUTTON);
		addActionButton(downImage);
		addActionButton(this.getSave());

		addRightAlignedActionButtonPaddingPanel();

		projects = new ValueListBox<SearchUIProject>(new ProxyRenderer<SearchUIProject>(null)
		{
			@Override
			public String render(final SearchUIProject object)
			{
				return object == null ? null : object.getName();
			}
		}, new ProvidesKey<SearchUIProject>()
		{
			@Override
			public Object getKey(final SearchUIProject item)
			{
				return item.getId();
			}
		});

		categories = new ValueListBox<SearchUICategory>(new ProxyRenderer<SearchUICategory>(null)
		{
			@Override
			public String render(final SearchUICategory object)
			{
				return object == null ? null : object.getName();
			}
		}, new ProvidesKey<SearchUICategory>()
		{
			@Override
			public Object getKey(final SearchUICategory item)
			{
				return item.getId();
			}
		});

		myTags = new ValueListBox<SearchUITag>(new ProxyRenderer<SearchUITag>(null)
		{
			@Override
			public String render(final SearchUITag object)
			{
				return object == null ? null : object.getName();
			}
		}, new ProvidesKey<SearchUITag>()
		{
			@Override
			public Object getKey(final SearchUITag item)
			{
				return item.getId();
			}
		});

		/* Add the layout to the panel */
		layout.addStyleName(CSSConstants.TOPICTAGVIEWNEWTAGTABLE);
		this.getPanel().setWidget(layout);
		layout.setWidget(0, 0, projects);
		layout.setWidget(0, 1, categories);
		layout.setWidget(0, 2, myTags);
		layout.setWidget(0, 3, add);

		layout.getFlexCellFormatter().setColSpan(1, 0, 4);
	}

	@Override
	public void updateNewTagCategoriesDisplay()
	{
		final List<SearchUICategory> listCategories = projects.getValue().getCategories();

		if (!listCategories.isEmpty())
		{
			final SearchUICategory category = listCategories.get(0);

			categories.setValue(category);

			/*
			 * The final list of acceptable values will be the object that was
			 * set with setValue() combined with the list we supply to the
			 * setAcceptableValues() method. We need to use setValue() to set a
			 * valid value before we call setAcceptableValues(), or otherwise an
			 * old value will appear in the list. We can't pass null to
			 * setValue() either.
			 * 
			 * However, sometimes having the same object set via setValue()
			 * included in the list sent to setAcceptableValues() can results in
			 * the item showing up twice in the drop down box. So we need to
			 * clone the original array and remove the item that was set via
			 * setValue().
			 */
			final List<SearchUICategory> clonedCategories = new ArrayList<SearchUICategory>(listCategories);
			clonedCategories.remove(category);

			categories.setAcceptableValues(clonedCategories);

			final List<SearchUITag> listTags = category.getMyTags();

			if (!listTags.isEmpty())
			{
				final SearchUITag tag = listTags.get(0);

				myTags.setValue(tag);

				final List<SearchUITag> cloneTags = new ArrayList<SearchUITag>(listTags);
				clonedCategories.remove(tag);

				myTags.setAcceptableValues(cloneTags);
			}
		}
	}

	@Override
	public void updateNewTagTagDisplay()
	{
		final List<SearchUITag> listTags = categories.getValue().getMyTags();

		if (!listTags.isEmpty())
		{
			final SearchUITag tag = listTags.get(0);

			myTags.setValue(tag);

			final List<SearchUITag> cloneTags = new ArrayList<SearchUITag>(listTags);
			clonedCategories.remove(tag);

			myTags.setAcceptableValues(cloneTags);
		}
	}

	public void initializeNewTags(final SearchUIProjects tags)
	{
		if (!tags.getProjects().isEmpty())
		{
			projects.setValue(tags.getProjects().get(0));

			projects.setAcceptableValues(tags.getProjects());

			if (!projects.getValue().getCategories().isEmpty())
			{
				categories.setValue(projects.getValue().getCategories().get(0));

				categories.setAcceptableValues(projects.getValue().getCategories());

				if (!categories.getValue().getMyTags().isEmpty())
				{
					myTags.setValue(categories.getValue().getMyTags().get(0));

					myTags.setAcceptableValues(categories.getValue().getMyTags());
				}
			}
		}
	}

	@Override
	public void initialize(final RESTTopicV1 topic)
	{
		/* Build up a hierarchy of tags assigned to the topic */
		final SearchUIProjects projects = new SearchUIProjects(topic.getTags());
		/* SearchUIProjectsEditor is a simple panel */
		editor = new TopicTagViewProjectsEditor();
		/* Initialize the driver with the top-level editor */
		driver.initialize(editor);
		/* Copy the data in the object into the UI */
		driver.edit(projects);
		/* Add the projects */
		layout.setWidget(1, 0, editor);
	}

	public PushButton getAdd()
	{
		return add;
	}
}