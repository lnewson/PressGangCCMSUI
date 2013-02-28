package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTImageCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTLanguageImageCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTImageCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTLanguageImageCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.components.ComponentImageV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTImageV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLanguageImageV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTStringConstantV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.constants.ServiceConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.events.viewevents.ImagesFilteredResultsAndImageViewEvent;
import org.jboss.pressgang.ccms.ui.client.local.mvp.events.viewevents.SearchResultsAndTopicViewEvent;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenterInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.searchandedit.BaseSearchAndEditPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.searchandedit.DisplayNewEntityCallback;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.searchandedit.GetNewEntityCallback;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.searchandedit.BaseSearchAndEditViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.preferences.Preferences;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.BaseRestCallback;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls.RESTCallback;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.image.RESTImageV1Editor;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.image.RESTLanguageImageV1Editor;
import org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vectomatic.file.File;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.events.ErrorHandler;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.clearContainerAndAddTopLevelPanel;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.removeHistoryToken;

/**
 * The presenter used to add logic to the image search and edit view.
 *
 * Images are a little different to other entities in that one of the properties, imageBase64, is generated by the server from
 * the binary image data. This property is used by the GWT application to display the image, which means that when editing
 * uploading a new image we actually have to save to the server instead of applying all changes locally and then saving them all
 * in one hit.
 *
 * It also means that when we create a new image we actually create and save a new image, instead of creating an in memory image
 * that is edited and then saved.
 *
 * @author Matthew Casperson
 */
@Dependent
public class ImagesFilteredResultsAndDetailsPresenter
        extends
        BaseSearchAndEditPresenter<
                        RESTImageV1,
                        RESTImageCollectionV1,
                        RESTImageCollectionItemV1,
                        RESTImageV1Editor>
        implements BaseTemplatePresenterInterface {


    public interface Display extends BaseSearchAndEditViewInterface<RESTImageV1, RESTImageCollectionV1, RESTImageCollectionItemV1> {

    }

    /**
     * History token
     */
    public static final String HISTORY_TOKEN = "ImageFilteredResultsAndImageView";

    /**
     * A reference to the StringConstants that holds the locales.
     */
    private String[] locales;

    @Inject
    private HandlerManager eventBus;

    @Inject
    private Display display;

    @Inject
    private ImageFilteredResultsPresenter imageFilteredResultsComponent;

    @Inject
    private ImagePresenter imageComponent;

    private String queryString;

    @Override
    public void go(@NotNull final HasWidgets container) {
        clearContainerAndAddTopLevelPanel(container, display);
        bindSearchAndEditExtended(ServiceConstants.IMAGES_TOPIC, HISTORY_TOKEN, queryString);
    }

    @Override
    public void bindSearchAndEditExtended(final int topicId, @NotNull final String pageId, @NotNull final String queryString) {
        display.setFeedbackLink(Constants.KEY_SURVEY_LINK + HISTORY_TOKEN);

        imageComponent.bindExtended(ServiceConstants.IMAGE_HELP_TOPIC, pageId);
        imageFilteredResultsComponent.bindExtendedFilteredResults(ServiceConstants.SEARCH_VIEW_HELP_TOPIC, pageId, queryString);

        /* A call back used to get a fresh copy of the entity that was selected */
        @NotNull final GetNewEntityCallback<RESTImageV1> getNewEntityCallback = new GetNewEntityCallback<RESTImageV1>() {

            @Override
            public void getNewEntity(@NotNull final RESTImageV1 selectedEntity, @NotNull final DisplayNewEntityCallback<RESTImageV1> displayCallback) {
                @NotNull final RESTCallback<RESTImageV1> callback = new BaseRestCallback<RESTImageV1, BaseTemplateViewInterface>(display,
                        new BaseRestCallback.SuccessAction<RESTImageV1, BaseTemplateViewInterface>() {
                            @Override
                            public void doSuccessAction(@NotNull final RESTImageV1 retValue, @NotNull final BaseTemplateViewInterface display) {
                                displayCallback.displayNewEntity(retValue);
                            }
                        });
                RESTCalls.getImage(callback, selectedEntity.getId());
            }
        };

        super.bindSearchAndEdit(topicId, pageId, Preferences.IMAGE_VIEW_MAIN_SPLIT_WIDTH, imageComponent.getDisplay(), imageComponent.getDisplay(), imageFilteredResultsComponent.getDisplay(),
                imageFilteredResultsComponent, display, display, getNewEntityCallback);

        populateLocales();
    }

    @Override
    public void parseToken(@NotNull final String historyToken) {
        queryString = removeHistoryToken(historyToken, HISTORY_TOKEN);
        if (!queryString.startsWith(Constants.QUERY_PATH_SEGMENT_PREFIX)) {
            queryString = Constants.QUERY_PATH_SEGMENT_PREFIX;
        }
    }

    /**
     * Here we load the actual language images associated with the image
     */
    @Override
    protected void loadAdditionalDisplayedItemData() {

        @NotNull final RESTCallback<RESTImageV1> callback = new BaseRestCallback<RESTImageV1, ImagesFilteredResultsAndDetailsPresenter.Display>(
                display, new BaseRestCallback.SuccessAction<RESTImageV1, ImagesFilteredResultsAndDetailsPresenter.Display>() {
            @Override
            public void doSuccessAction(@NotNull final RESTImageV1 retValue, @NotNull final ImagesFilteredResultsAndDetailsPresenter.Display display) {
                checkArgument(retValue.getLanguageImages_OTM() != null, "The image should have the language image children populated.");

                /*
                 * Do a shallow copy here, because Chrome has issues with System.arraycopy - see
                 * http://code.google.com/p/chromium/issues/detail?id=56588
                 */
                retValue.cloneInto(imageFilteredResultsComponent.getProviderData().getDisplayedItem().getItem(), false);

                finishLoading();
            }
        });

        RESTCalls.getImage(callback, imageFilteredResultsComponent.getProviderData().getSelectedItem().getItem().getId());
    }

    @NotNull
    private BaseRestCallback.SuccessAction<RESTImageV1, BaseTemplateViewInterface> getDefaultImageRestCallback() {
        return new BaseRestCallback.SuccessAction<RESTImageV1, BaseTemplateViewInterface>() {
            @Override
            public void doSuccessAction(@NotNull final RESTImageV1 retValue, @NotNull final BaseTemplateViewInterface display) {
                retValue.cloneInto(imageFilteredResultsComponent.getProviderData().getSelectedItem().getItem(), false);
                retValue.cloneInto(imageFilteredResultsComponent.getProviderData().getDisplayedItem().getItem(), false);
                initializeViews();
                updateDisplayAfterSave(false);
            }
        };
    }

    @NotNull
    private BaseRestCallback.FailureAction<BaseTemplateViewInterface> getDefaultImageRestFailureCallback() {
        return new BaseRestCallback.FailureAction<BaseTemplateViewInterface>() {
            @Override
            public void doFailureAction(final BaseTemplateViewInterface display) {
                Window.alert(PressGangCCMSUI.INSTANCE.ImageUploadFailure());
            }
        };
    }

    @NotNull
    private List<String> getUnassignedLocales() {
        @NotNull final List<String> newLocales = new ArrayList<String>(Arrays.asList(locales));

        /* Make it so you can't add a locale if it already exists */
        if (imageFilteredResultsComponent.getProviderData().getDisplayedItem().getItem().getLanguageImages_OTM() != null) {
            for (@NotNull final RESTLanguageImageCollectionItemV1 langImage : imageFilteredResultsComponent.getProviderData()
                    .getDisplayedItem().getItem().getLanguageImages_OTM().returnExistingAndAddedCollectionItems()) {
                newLocales.remove(langImage.getItem().getLocale());
            }
        }

        return newLocales;
    }

    private void populateLocales() {
        @NotNull final RESTCalls.RESTCallback<RESTStringConstantV1> callback = new BaseRestCallback<RESTStringConstantV1, BaseTemplateViewInterface>(
                display, new BaseRestCallback.SuccessAction<RESTStringConstantV1, BaseTemplateViewInterface>() {
            @Override
            public void doSuccessAction(@NotNull final RESTStringConstantV1 retValue, final BaseTemplateViewInterface display) {
                        /* Get the list of locales from the StringConstant */
                locales = retValue.getValue().replaceAll("\\r\\n", "").replaceAll("\\n", "").replaceAll(" ", "")
                        .split(",");

                finishLoading();
            }
        });

        RESTCalls.getStringConstant(callback, ServiceConstants.LOCALE_STRING_CONSTANT);
    }

    /**
     * Each Language Image has an upload button that needs to be bound to some behaviour.
     */
    private void bindImageUploadButtons() {

        checkState(imageComponent.getDisplay().getEditor() != null, "display.getEditor() cannot be null");

        for (@NotNull final RESTLanguageImageV1Editor editor : imageComponent.getDisplay().getEditor().languageImages_OTMEditor().itemsEditor().getEditors()) {
            editor.getUploadButton().addClickHandler(new ClickHandler() {
                @Override
                public void onClick(final ClickEvent event) {

                    /*
                     * There should only be one file, but use a loop to accommodate any changes that might implement multiple
                     * files
                     */
                    for (@NotNull final File file : editor.getUpload().getFiles()) {
                        display.addWaitOperation();

                        @NotNull final FileReader reader = new FileReader();

                        reader.addErrorHandler(new ErrorHandler() {
                            @Override
                            public void onError(@NotNull final org.vectomatic.file.events.ErrorEvent event) {
                                imageComponent.getDisplay().removeWaitOperation();
                            }
                        });

                        reader.addLoadEndHandler(new LoadEndHandler() {
                            @Override
                            public void onLoadEnd(@NotNull final LoadEndEvent event) {
                                try {
                                    final String result = reader.getStringResult();
                                    @NotNull final byte[] buffer = GWTUtilities.getByteArray(result, 1);

                                    /* Flush any changes */
                                    imageComponent.getDisplay().getDriver().flush();
                                    
                                    /*
                                     * Create the image to be modified. This is so we don't send off unnecessary data.
                                     */
                                    @NotNull final RESTImageV1 updateImage = new RESTImageV1();
                                    updateImage.setId(imageFilteredResultsComponent.getProviderData().getDisplayedItem().getItem()
                                            .getId());
                                    updateImage.explicitSetDescription(imageFilteredResultsComponent.getProviderData().getDisplayedItem().getItem().getDescription());

                                    /* Create the language image */
                                    @NotNull final RESTLanguageImageV1 updatedLanguageImage = new RESTLanguageImageV1();
                                    updatedLanguageImage.setId(editor.self.getItem().getId());
                                    updatedLanguageImage.explicitSetImageData(buffer);
                                    updatedLanguageImage.explicitSetFilename(file.getName());

                                    /* Add the language image */
                                    updateImage.explicitSetLanguageImages_OTM(new RESTLanguageImageCollectionV1());
                                    updateImage.getLanguageImages_OTM().addUpdateItem(updatedLanguageImage);

                                    @NotNull final RESTCalls.RESTCallback<RESTImageV1> callback = new BaseRestCallback<RESTImageV1, BaseTemplateViewInterface>(
                                            display, getDefaultImageRestCallback(), getDefaultImageRestFailureCallback());

                                    RESTCalls.updateImage(callback, updateImage);
                                } finally {
                                    display.removeWaitOperation();
                                }
                            }
                        });

                        reader.readAsBinaryString(file);

                        /* we only upload one file */
                        break;
                    }
                }
            });
        }
    }

    @Override
    public boolean hasUnsavedChanges() {
        if (imageFilteredResultsComponent.getProviderData().getDisplayedItem() != null) {

            imageComponent.getDisplay().getDriver().flush();

            return !GWTUtilities.stringEqualsEquatingNullWithEmptyString(imageFilteredResultsComponent.getProviderData()
                    .getSelectedItem().getItem().getDescription(), imageFilteredResultsComponent.getProviderData()
                    .getDisplayedItem().getItem().getDescription());
        }
        return false;
    }

    /**
     * Potentially two REST calls have to finish before we can display the page. This function will be called as each REST call
     * finishes, and when all the information has been gathered, the page will be displayed.
     */
    private void finishLoading() {
        if (locales != null && imageFilteredResultsComponent.getProviderData().getDisplayedItem() != null) {
            initializeViews();
        }
    }

    @Override
    protected void bindActionButtons() {
        imageComponent.getDisplay().getSave().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (hasUnsavedChanges()) {

                    /*
                     * Create the image to be modified. This is so we don't send off unnessessary data.
                     */
                    @NotNull final RESTImageV1 updateImage = new RESTImageV1();
                    updateImage.setId(imageFilteredResultsComponent.getProviderData().getDisplayedItem().getItem().getId());
                    updateImage.explicitSetDescription(imageFilteredResultsComponent.getProviderData().getDisplayedItem().getItem()
                            .getDescription());

                    @NotNull final RESTCalls.RESTCallback<RESTImageV1> callback = new BaseRestCallback<RESTImageV1, BaseTemplateViewInterface>(
                            display, getDefaultImageRestCallback());

                    RESTCalls.updateImage(callback, updateImage);
                } else {
                    Window.alert(PressGangCCMSUI.INSTANCE.NoUnsavedChanges());
                }

            }
        });

        imageComponent.getDisplay().getAddLocale().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                imageComponent.getDisplay().getAddLocaleDialog().getDialogBox().center();
                imageComponent.getDisplay().getAddLocaleDialog().getDialogBox().show();
            }
        });

        imageComponent.getDisplay().getRemoveLocale().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (Window.confirm(PressGangCCMSUI.INSTANCE.ConfirmDelete())) {

                    final int selectedTab = imageComponent.getDisplay().getEditor().languageImages_OTMEditor().getSelectedIndex();
                    if (selectedTab != -1) {
                        final RESTLanguageImageCollectionItemV1 selectedImage = imageComponent.getDisplay().getEditor()
                                .languageImages_OTMEditor().itemsEditor().getList().get(selectedTab);

                        /* Adding or removing a locale will save changes to the description */
                        imageComponent.getDisplay().getDriver().flush();

                        /*
                         * Create the image to be modified. This is so we don't send off unnessessary data.
                         */
                        @NotNull final RESTImageV1 updateImage = new RESTImageV1();
                        updateImage.setId(imageFilteredResultsComponent.getProviderData().getDisplayedItem().getItem().getId());
                        updateImage.explicitSetDescription(imageFilteredResultsComponent.getProviderData().getDisplayedItem()
                                .getItem().getDescription());

                        /* Create the language image */
                        @NotNull final RESTLanguageImageV1 languageImage = new RESTLanguageImageV1();
                        languageImage.setId(selectedImage.getItem().getId());

                        /* Add the langauge image */
                        updateImage.explicitSetLanguageImages_OTM(new RESTLanguageImageCollectionV1());
                        updateImage.getLanguageImages_OTM().addRemoveItem(languageImage);

                        @NotNull final RESTCalls.RESTCallback<RESTImageV1> callback = new BaseRestCallback<RESTImageV1, BaseTemplateViewInterface>(
                                display, getDefaultImageRestCallback());

                        RESTCalls.updateImage(callback, updateImage);
                    }
                }
            }
        });

        imageComponent.getDisplay().getAddLocaleDialog().getOk().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                imageComponent.getDisplay().getAddLocaleDialog().getDialogBox().hide();

                final String selectedLocale = imageComponent.getDisplay().getAddLocaleDialog().getLocales()
                        .getItemText(imageComponent.getDisplay().getAddLocaleDialog().getLocales().getSelectedIndex());

                /* Don't add locales twice */
                if (imageFilteredResultsComponent.getProviderData().getDisplayedItem().getItem().getLanguageImages_OTM() != null) {
                    for (@NotNull final RESTLanguageImageCollectionItemV1 langImage : imageFilteredResultsComponent.getProviderData()
                            .getDisplayedItem().getItem().getLanguageImages_OTM().returnExistingAndAddedCollectionItems()) {
                        if (langImage.getItem().getLocale().equals(selectedLocale)) {
                            return;
                        }
                    }
                }

                /* Adding or removing a locate will also save any changes to the description */
                imageComponent.getDisplay().getDriver().flush();

                /*
                 * Create the image to be modified. This is so we don't send off unnessessary data.
                 */
                @NotNull final RESTImageV1 updateImage = new RESTImageV1();
                updateImage.setId(imageFilteredResultsComponent.getProviderData().getDisplayedItem().getItem().getId());
                updateImage.explicitSetDescription(imageFilteredResultsComponent.getProviderData().getDisplayedItem().getItem()
                        .getDescription());

                /* Create the language image */
                @NotNull final RESTLanguageImageV1 languageImage = new RESTLanguageImageV1();
                languageImage.explicitSetLocale(selectedLocale);

                /* Add the langauge image */
                updateImage.explicitSetLanguageImages_OTM(new RESTLanguageImageCollectionV1());
                updateImage.getLanguageImages_OTM().addNewItem(languageImage);

                @NotNull final RESTCalls.RESTCallback<RESTImageV1> callback = new BaseRestCallback<RESTImageV1, BaseTemplateViewInterface>(
                        display, getDefaultImageRestCallback());

                RESTCalls.updateImage(callback, updateImage);
            }
        });

        imageComponent.getDisplay().getAddLocaleDialog().getCancel().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                imageComponent.getDisplay().getAddLocaleDialog().getDialogBox().hide();
            }
        });

        imageComponent.getDisplay().getViewImage().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {

                final int selectedTab = imageComponent.getDisplay().getEditor().languageImages_OTMEditor().getSelectedIndex();
                if (selectedTab != -1) {
                    final RESTLanguageImageCollectionItemV1 selectedImage = imageComponent.getDisplay().getEditor()
                            .languageImages_OTMEditor().itemsEditor().getList().get(selectedTab);

                    /* This may be null if no image was uploaded */
                    if (selectedImage.getItem().getImageDataBase64() != null) {
                        displayImageInPopup(GWTUtilities.getStringUTF8(selectedImage.getItem().getImageDataBase64()));
                    }
                }
            }

            ;
        });

        imageComponent.getDisplay().getFindTopics().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(@NotNull final ClickEvent event) {

                final String docbookFileName = ComponentImageV1.getDocbookFileName(imageFilteredResultsComponent.getProviderData()
                        .getDisplayedItem().getItem());

                if (docbookFileName != null && !docbookFileName.isEmpty() && isOKToProceed()) {

                    @NotNull final String searchQuery = "images/" + docbookFileName;

                    eventBus.fireEvent(new SearchResultsAndTopicViewEvent(Constants.QUERY_PATH_SEGMENT_PREFIX
                            + org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants.TOPIC_XML_FILTER_VAR + "=" + (Constants.ENCODE_QUERY_OPTIONS ? URL.encodeQueryString(searchQuery) : searchQuery), event.getNativeEvent()
                            .getKeyCode() == KeyCodes.KEY_CTRL));
                }

            }
        });
    }

    /**
     * Open a popup window that displays the image defined in the base64 parameter
     *
     * @param base64 The BASE64 representation of the image to be displayed
     */
    native private void displayImageInPopup(@NotNull final String base64) /*-{
        var win = $wnd.open("data:image/jpeg;base64," + base64, "_blank",
            "width=" + (screen.width - 200) + ", height="
                + (screen.height - 200) + ", left=100, top=100"); // a window object
    }-*/;

    @Override
    protected void bindFilteredResultsButtons() {
        imageFilteredResultsComponent.getDisplay().getEntitySearch().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(@NotNull final ClickEvent event) {
                if (isOKToProceed()) {
                    eventBus.fireEvent(new ImagesFilteredResultsAndImageViewEvent(imageFilteredResultsComponent.getQuery(), event.getNativeEvent().getKeyCode() == KeyCodes.KEY_CTRL));
                }
            }
        });

        imageFilteredResultsComponent.getDisplay().getCreate().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (isOKToProceed()) {

                    /* Start by getting the default locale */
                    @NotNull final RESTCallback<RESTStringConstantV1> callback = new BaseRestCallback<RESTStringConstantV1, ImagesFilteredResultsAndDetailsPresenter.Display>(
                            display,
                            new BaseRestCallback.SuccessAction<RESTStringConstantV1, ImagesFilteredResultsAndDetailsPresenter.Display>() {
                                @Override
                                public void doSuccessAction(@NotNull final RESTStringConstantV1 retValue,
                                                            final ImagesFilteredResultsAndDetailsPresenter.Display display) {

                                    /* When we have the default locale, create a new image */
                                    @NotNull final RESTLanguageImageV1 langImage = new RESTLanguageImageV1();
                                    langImage.explicitSetLocale(retValue.getValue());

                                    @NotNull final RESTImageV1 newImage = new RESTImageV1();
                                    newImage.explicitSetLanguageImages_OTM(new RESTLanguageImageCollectionV1());
                                    newImage.getLanguageImages_OTM().addNewItem(langImage);

                                    @NotNull final RESTCallback<RESTImageV1> imageCallback = new BaseRestCallback<RESTImageV1, ImagesFilteredResultsAndDetailsPresenter.Display>(
                                            display,
                                            new BaseRestCallback.SuccessAction<RESTImageV1, ImagesFilteredResultsAndDetailsPresenter.Display>() {
                                                @Override
                                                public void doSuccessAction(@NotNull final RESTImageV1 retValue,
                                                                            final ImagesFilteredResultsAndDetailsPresenter.Display display) {

                                                    @NotNull final RESTImageCollectionItemV1 selectedImageCollectionItem = new RESTImageCollectionItemV1();
                                                    selectedImageCollectionItem.setItem(retValue.clone(false));
                                                    imageFilteredResultsComponent.getProviderData().setSelectedItem(
                                                            selectedImageCollectionItem);

                                                    @NotNull final RESTImageCollectionItemV1 displayedImageCollectionItem = new RESTImageCollectionItemV1();
                                                    displayedImageCollectionItem.setItem(retValue.clone(false));
                                                    imageFilteredResultsComponent.getProviderData().setDisplayedItem(
                                                            displayedImageCollectionItem);

                                                    initializeViews();

                                                    /* Display the entities property view */
                                                    switchView(imageComponent.getDisplay());

                                                    /* Reload the filtered results view */
                                                    updateDisplayAfterSave(true);
                                                }
                                            });

                                    RESTCalls.createImage(imageCallback, newImage);
                                }
                            });

                    RESTCalls.getStringConstant(callback, ServiceConstants.DEFAULT_LOCALE_ID);

                }
            }
        });

    }

    @Override
    protected void initializeViews(@Nullable final List<BaseTemplateViewInterface> filter) {

        if (viewIsInFilter(filter, imageComponent.getDisplay())) {
            imageComponent.getDisplay().displayExtended(imageFilteredResultsComponent.getProviderData().getDisplayedItem().getItem(), false,
                    getUnassignedLocales().toArray(new String[0]));
        }

        bindImageUploadButtons();
    }
}
