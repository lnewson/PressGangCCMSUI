package org.jboss.pressgang.ccms.ui.client.local.sort.propertytag;

import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTPropertyTagCollectionItemV1;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * Sorts RESTTagCollectionItemV1 objects based on their name.
 */
public class RESTPropertyTagCollectionItemNameSort implements Comparator<RESTPropertyTagCollectionItemV1> {
    final boolean ascending;

    /**
     *
     * @param ascending true if the items should be sorted in ascending order, false otherwise
     */
    public RESTPropertyTagCollectionItemNameSort(final boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public int compare(@Nullable final RESTPropertyTagCollectionItemV1 arg0, @Nullable final RESTPropertyTagCollectionItemV1 arg1) {
        final int ascendingMultiplier = ascending ? 1 : -1;

        /*
            First deal with null objects
        */
        if (arg0 == null && arg1 == null) {
            return 0;
        }

        if (arg0 == arg1) {
            return 0;
        }

        if (arg0 == null) {
            return -1 * ascendingMultiplier;
        }

        if (arg1 == null) {
            return 1 * ascendingMultiplier;
        }

        /*
            Fall back to comparing by name
         */
        if (arg0.getItem().getName() == null && arg1.getItem().getName() == null) {
            return 0;
        }

        if (arg0.getItem().getName() == null) {
            return -1 * ascendingMultiplier;
        }

        if (arg1.getItem().getName() == null) {
            return 1 * ascendingMultiplier;
        }

        final int nameSort = arg0.getItem().getName().compareTo(arg1.getItem().getName()) * ascendingMultiplier;

        if (nameSort == 0) {
            return new RESTPropertyTagCollectionItemIDSort(ascending).compare(arg0, arg1);
        }

        return nameSort;
    }
}
