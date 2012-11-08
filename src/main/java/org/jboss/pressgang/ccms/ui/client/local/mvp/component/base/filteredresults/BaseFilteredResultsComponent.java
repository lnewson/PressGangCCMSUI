package org.jboss.pressgang.ccms.ui.client.local.mvp.component.base.filteredresults;

import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.ui.client.local.mvp.component.base.ComponentBase;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.filteredresults.BaseFilteredResultsViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.ui.ProviderUpdateData;
import org.jboss.pressgang.ccms.ui.client.local.utilities.EnhancedAsyncDataProvider;

/**
 * This is the base class that is used for components adding logic to views that list the results of a query
 * @author Matthew Casperson
 *
 * @param <S> The filtered results view type
 * @param <T> The entity type
 * @param <U> The collection type for entity T
 * @param <V> The collection item type for entity T
 */
abstract public class BaseFilteredResultsComponent<S extends BaseFilteredResultsViewInterface<T, U, V>, 
        T extends RESTBaseEntityV1<T, U, V>, U extends RESTBaseCollectionV1<T, U, V>, V extends RESTBaseCollectionItemV1<T, U, V>>
    extends ComponentBase<S> implements BaseFilteredResultsComponentInterface<S, T, U, V> {

    /** Holds the data required to populate and refresh the tags list */
    protected ProviderUpdateData<V> providerData = new ProviderUpdateData<V>();

    @Override
    public ProviderUpdateData<V> getProviderData() {
        return providerData;
    }

    @Override
    public void setTagProviderData(final ProviderUpdateData<V> providerData) {
        this.providerData = providerData;
    }
    
    @Override
    public void bind(final String queryString, final S display, final BaseTemplateViewInterface waitDisplay)
    {
        super.bind(display, waitDisplay);
        displayQueryElements(queryString);
    }

    /**
     * DIsplay the current filter options
     * 
     * @param queryString The string that contains the filter options
     */
    abstract protected void displayQueryElements(final String queryString);
    
    abstract protected EnhancedAsyncDataProvider<V> generateListProvider(final String queryString,
            final S display, final BaseTemplateViewInterface waitDisplay);

}
