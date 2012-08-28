package org.jboss.pressgangccms.client.local.view;

import org.jboss.pressgangccms.client.local.constants.CSSConstants;
import org.jboss.pressgangccms.client.local.presenter.TopicXMLPresenter;
import org.jboss.pressgangccms.client.local.presenter.TopicXMLPresenter.TopicXMLPresenterDriver;
import org.jboss.pressgangccms.client.local.resources.images.ImageResources;
import org.jboss.pressgangccms.client.local.ui.SplitType;
import org.jboss.pressgangccms.client.local.ui.UIUtilities;
import org.jboss.pressgangccms.client.local.ui.editor.topicview.RESTTopicV1XMLEditor;
import org.jboss.pressgangccms.client.local.view.base.TopicViewBase;
import org.jboss.pressgangccms.rest.v1.entities.RESTTopicV1;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;

public class TopicXMLView extends TopicViewBase implements TopicXMLPresenter.Display
{
	public static final String HISTORY_TOKEN = "TopicXMLView";

	/** The GWT Editor Driver */
	private final TopicXMLPresenterDriver driver = GWT.create(TopicXMLPresenterDriver.class);

	private RESTTopicV1XMLEditor editor;

	private final ToggleButton lineWrap = UIUtilities.createToggleButton(ImageResources.INSTANCE.lineWrap48(), ImageResources.INSTANCE.lineWrapDown48(), ImageResources.INSTANCE.lineWrapHover48(), CSSConstants.SPACEDBUTTON);
	private final ToggleButton showInvisibles = UIUtilities.createToggleButton(ImageResources.INSTANCE.hiddenCharacters48(), ImageResources.INSTANCE.hiddenCharactersDown48(), ImageResources.INSTANCE.hiddenCharactersHover48(), CSSConstants.SPACEDBUTTON);

	@Override
	public ToggleButton getShowInvisibles()
	{
		return showInvisibles;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SimpleBeanEditorDriver getDriver()
	{
		return driver;
	}

	@Override
	public ToggleButton getLineWrap()
	{
		return lineWrap;
	}

	@Override
	public AceEditor getEditor()
	{
		if (editor != null)
			return editor.xml;
		return null;
	}

	public TopicXMLView()
	{

	}
	
	@Override
	protected void populateTopActionBar()
	{
		addActionButton(this.getRenderedSplit());
		addActionButton(this.getRendered());
		final Image downImage = new Image(ImageResources.INSTANCE.xmlDown48());
		downImage.addStyleName(CSSConstants.SPACEDBUTTON);
		addActionButton(downImage);
		addActionButton(this.getXmlErrors());
		addActionButton(this.getFields());
		addActionButton(this.getTags());
		addActionButton(this.getBugs());
		addActionButton(this.getHistory());
		addActionButton(this.getSave());
		
		fixReadOnlyButtons();

		addRightAlignedActionButtonPaddingPanel();
		addActionButton(lineWrap);
		addActionButton(showInvisibles);
	}

	@Override
	public void initialize(final RESTTopicV1 topic, final boolean readOnly, final SplitType splitType)
	{
		this.getPanel().addStyleName(CSSConstants.TOPICXMLVIEWPANEL);
		
		this.readOnly = readOnly;
		fixReadOnlyButtons();
		buildSplitViewButtons(splitType);
		
		/* SearchUIProjectsEditor is a grid */
		editor = new RESTTopicV1XMLEditor(readOnly);
		/* Initialize the driver with the top-level editor */
		driver.initialize(editor);
		/* Copy the data in the object into the UI */
		driver.edit(topic);
		/* Add the projects */
		this.getPanel().setWidget(editor);
	}
}
