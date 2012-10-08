package org.jboss.pressgang.ccms.ui.client.local.ui.searchfield;

import java.util.Date;

import com.google.gwt.user.client.ui.TriStateSelectionState;

/**
 * The backing object for the search fields view. Instance of this class will be manipulated by a GWT Editor
 * 
 * @author Matthew Casperson
 * 
 */
public class SearchUIFields {
    private Date createdAfter;
    private Date createdBefore;
    private Date editedAfter;
    private Date editedBefore;
    private Integer editedInLastXDays;
    private Integer notEditedInLastXDays;
    private String ids;
    private String notIds;
    private String title;
    private String contents;
    private String notContents;
    private String notTitle;
    private String description;
    private String notDescription;
    private String includedInContentSpecs;
    private String notIncludedInContentSpecs;
    private String freeTextSearch;
    private TriStateSelectionState hasBugzillaBugs = TriStateSelectionState.NONE;
    private TriStateSelectionState hasOpenBugzillaBugs = TriStateSelectionState.NONE;
    private boolean matchAll = true;

    public Date getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(Date createdAfter) {
        this.createdAfter = createdAfter;
    }

    public Date getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(Date createdBefore) {
        this.createdBefore = createdBefore;
    }

    public Date getEditedAfter() {
        return editedAfter;
    }

    public void setEditedAfter(Date editedAfter) {
        this.editedAfter = editedAfter;
    }

    public Date getEditedBefore() {
        return editedBefore;
    }

    public void setEditedBefore(Date editedBefore) {
        this.editedBefore = editedBefore;
    }

    public Integer getEditedInLastXDays() {
        return editedInLastXDays;
    }

    public void setEditedInLastXDays(Integer editedInLastXDays) {
        this.editedInLastXDays = editedInLastXDays;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getNotIds() {
        return notIds;
    }

    public void setNotIds(String notIds) {
        this.notIds = notIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotTitle() {
        return notTitle;
    }

    public void setNotTitle(String notTitle) {
        this.notTitle = notTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotDescription() {
        return notDescription;
    }

    public void setNotDescription(String notDescription) {
        this.notDescription = notDescription;
    }

    public String getIncludedInContentSpecs() {
        return includedInContentSpecs;
    }

    public void setIncludedInContentSpecs(String includedInContentSpecs) {
        this.includedInContentSpecs = includedInContentSpecs;
    }

    public String getNotIncludedInContentSpecs() {
        return notIncludedInContentSpecs;
    }

    public void setNotIncludedInContentSpecs(String notIncludedInContentSpecs) {
        this.notIncludedInContentSpecs = notIncludedInContentSpecs;
    }

    public String getFreeTextSearch() {
        return freeTextSearch;
    }

    public void setFreeTextSearch(String freeTextSearch) {
        this.freeTextSearch = freeTextSearch;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Integer getNotEditedInLastXDays() {
        return notEditedInLastXDays;
    }

    public void setNotEditedInLastXDays(Integer notEditedInLastXDays) {
        this.notEditedInLastXDays = notEditedInLastXDays;
    }

    public String getNotContents() {
        return notContents;
    }

    public void setNotContents(String notContents) {
        this.notContents = notContents;
    }

    public TriStateSelectionState getHasBugzillaBugs() {
        return hasBugzillaBugs;
    }

    public void setHasBugzillaBugs(TriStateSelectionState hasBugzillaBugs) {
        this.hasBugzillaBugs = hasBugzillaBugs;
    }

    public TriStateSelectionState getHasOpenBugzillaBugs() {
        return hasOpenBugzillaBugs;
    }

    public void setHasOpenBugzillaBugs(TriStateSelectionState hasOpenBugzillaBugs) {
        this.hasOpenBugzillaBugs = hasOpenBugzillaBugs;
    }

    public boolean isMatchAll() {
        return matchAll;
    }

    public void setMatchAll(boolean matchAll) {
        this.matchAll = matchAll;
    }
}
