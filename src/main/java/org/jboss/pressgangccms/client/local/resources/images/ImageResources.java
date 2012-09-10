package org.jboss.pressgangccms.client.local.resources.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Embeds image resources into the GWT app.
 * 
 * @author Matthew Casperson
 */
public interface ImageResources extends ClientBundle {
    /**
     * A singleton instance of this class.
     */
    ImageResources INSTANCE = GWT.create(ImageResources.class);

    /**
     * @return An animated GIF used to indicate that the page is loading
     */
    @Source("spinner.gif")
    ImageResource spinner();

    /**
     * @return The application banner
     */
    @Source("headingBanner.png")
    ImageResource headingBanner();

    /**
     * @return Used to indicate tristate checkbox in a nonselected state
     */
    @Source("round32.png")
    ImageResource round32();

    /**
     * @return Used to indicate a selected tristate checkbox
     */
    @Source("plus32.png")
    ImageResource plus32();

    /**
     * @return Used to indicate an unselected tristate checkbox
     */
    @Source("minus32.png")
    ImageResource minus32();

}
