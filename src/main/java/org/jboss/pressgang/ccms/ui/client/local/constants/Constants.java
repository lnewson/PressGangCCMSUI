package org.jboss.pressgang.ccms.ui.client.local.constants;

/**
 * This class contains a number of constants used throughout the application.
 * 
 * @author Matthew Casperson
 */
public final class Constants {
    
    /**
     * And ID that can not appear in the database.
     */
    public static final int NULL_ID = -1;
    
    /**
     * How long to wait before refreshing the rendered view (in milliseconds).
     */
    public static final int REFRESH_RATE = 1000;
    
    
    /**
     * The name of a javascript click event.
     */
    public static final String JAVASCRIPT_CLICK_EVENT = "click";

    /**
     * All path segments that define a query must start with this string.
     */
    public static final String QUERY_PATH_SEGMENT_PREFIX_WO_SEMICOLON = "query";

    /**
     * All path segments that define a query must start with this string.
     */
    public static final String QUERY_PATH_SEGMENT_PREFIX = QUERY_PATH_SEGMENT_PREFIX_WO_SEMICOLON + ";";

    /**
     * The size of the split panels.
     */
    public static final int SPLIT_PANEL_SIZE = 300;

    /**
     * The height of the header banner in the template.
     */
    public static final int HEADING_BANNER_HEIGHT = 110;

    /**
     * The size of the split panel dividers.
     */
    public static final int SPLIT_PANEL_DIVIDER_SIZE = 5;
    
    /**
     * The size of the page title bar.
     */
    public static final int PAGE_TITLE_BAR_HEIGHT = 3;

    /**
     * The height of the action bars.
     */
    public static final int ACTION_BAR_HEIGHT = 96;

    /**
     * The width of the shortcut bar.
     */
    public static final int SHORTCUT_BAR_WIDTH = 110;
    
    /**
     * The height of the footer
     */
    public static final int FOOTER_HEIGHT = 16;

    /**
     * The maximum number of results to return in a search result.
     */
    public static final int MAX_SEARCH_RESULTS = 15;
    /**
     * The REST server.
     */
    //public static final String BASE_URL = "http://localhost:8080/TopicIndex/";
    public static final String BASE_URL = "http://skynet-dev.usersys.redhat.com:8080/TopicIndex/";
    /**
     * The REST URL.
     */
    public static final String REST_SERVER = BASE_URL + "seam/resource/rest";

    /**
     * The base URL to Bugzilla.
     */
    public static final String BUGZILLA_BASE_URL = "https://bugzilla.redhat.com";

    /**
     * The Bugzilla link for this product.
     */
    public static final String BUGZILLA_URL = BUGZILLA_BASE_URL
            + "/enter_bug.cgi?product=PressGang CCMS&component=Web-UI&version=1.1";

    /**
     * View Bugzilla bug URL.
     */
    public static final String BUGZILLA_VIEW_BUG_URL = BUGZILLA_BASE_URL + "/show_bug.cgi?id=";
    
    /**
     * A link to the survey
     */
    public static final String KEY_SURVEY_LINK = "http://www.keysurvey.com/votingmodule/s180/f/457744/1909/?LQID=1&source=";
    
    private Constants() {

    }
}
