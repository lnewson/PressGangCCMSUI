package org.jboss.pressgangccms.client.local.mvp.view.tag;

import org.jboss.pressgangccms.client.local.mvp.presenter.tag.TagPresenter;
import org.jboss.pressgangccms.client.local.mvp.presenter.tag.TagPresenter.TagPresenterDriver;
import org.jboss.pressgangccms.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgangccms.client.local.ui.editor.tagview.RESTTagV1BasicDetailsEditor;
import org.jboss.pressgangccms.rest.v1.entities.RESTTagV1;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;

public class TagView extends TagViewBase implements TagPresenter.Display
{
	public static final String HISTORY_TOKEN = "TagView";
	
	/** The GWT Editor Driver */
	private final TagPresenterDriver driver = GWT.create(TagPresenterDriver.class);
	
	private boolean readOnly = false;

	@SuppressWarnings("rawtypes")
	@Override
	public SimpleBeanEditorDriver getDriver()
	{
		return driver;
	}
	
	public TagView()
	{
		super(PressGangCCMSUI.INSTANCE.PressGangCCMS(), PressGangCCMSUI.INSTANCE.Tags());
	}

	@Override
	protected void populateTopActionBar()
	{
		this.addActionButton(this.getTagDetails());
		this.addActionButton(this.getTagProjects());
		this.addActionButton(this.getTagCategories());
		this.addActionButton(this.getSave());
		addRightAlignedActionButtonPaddingPanel();
	}

	@Override
	public void initialize(final RESTTagV1 tag, final boolean readOnly)
	{
		this.readOnly = readOnly;
		
		/* SearchUIProjectsEditor is a grid */
		final RESTTagV1BasicDetailsEditor editor = new RESTTagV1BasicDetailsEditor(this.readOnly);
	    /* Initialize the driver with the top-level editor */
	    driver.initialize(editor);
	    /* Copy the data in the object into the UI */
	    driver.edit(tag);
	    /* Add the projects */
	    this.getPanel().setWidget(editor);
	} 
}
