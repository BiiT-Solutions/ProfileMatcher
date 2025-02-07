package com.biit.profile.core.controllers;

import com.biit.profile.core.exceptions.InvalidFormException;
import com.biit.profile.core.metaviewer.Collection;
import com.biit.profile.core.metaviewer.Facet;
import com.biit.profile.core.metaviewer.FacetCategory;
import com.biit.profile.core.metaviewer.Item;
import com.biit.profile.core.metaviewer.types.BooleanType;
import com.biit.profile.core.metaviewer.types.DateTimeType;
import com.biit.profile.core.providers.CadtIndividualProfileProvider;
import com.biit.profile.logger.ProfileLogger;
import com.biit.profile.persistence.entities.cadt.CadtArchetype;
import com.biit.profile.persistence.entities.cadt.CadtCompetence;
import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MetaviewerController {

    public static final String FORM_NAME = "CADT_Score";
    private static final String PIVOTVIEWER_IMAGE_FILE = "./five_colors/five_colors.dzc";

    private static final String PIVOTVIEWER_LINK = "/cadt";

    protected static final String CREATED_AT_FACET = "submittedAt";

    private static final int RED_COLOR_LIMIT = 100;
    private static final int ORANGE_COLOR_LIMIT = 250;
    private static final int YELLOW_COLOR_LIMIT = 350;
    private static final int LIGHT_GREEN_COLOR_LIMIT = 450;

    private static final String RED_COLOR_TAG = "#1";
    private static final String ORANGE_COLOR_TAG = "#2";
    private static final String YELLOW_COLOR_TAG = "#3";
    private static final String LIGHT_GREEN_COLOR_TAG = "#4";
    private static final String DARK_GREEN_COLOR_TAG = "#5";

    private final CadtIndividualProfileProvider cadtIndividualProfileProvider;

    public MetaviewerController(CadtIndividualProfileProvider cadtIndividualProfileProvider) {
        this.cadtIndividualProfileProvider = cadtIndividualProfileProvider;
    }

    public Collection getCollection() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return getCollection(cadtIndividualProfileProvider.getAll());
        } finally {
            stopWatch.stop();
            ProfileLogger.debug(this.getClass(), "Collection created in '" + stopWatch.getTotalTimeMillis() + "' ms");
        }
    }


    public Collection getCollection(List<CadtIndividualProfile> cadtIndividualProfiles) {
        final Collection collection = new Collection(FORM_NAME, PIVOTVIEWER_IMAGE_FILE);
        ProfileLogger.debug(this.getClass(), "Creating a new collection with '{}' elements.", cadtIndividualProfiles.size());
        collection.getFacetCategories().addAll(createCadtFacetsCategories());
        for (CadtIndividualProfile cadtIndividualProfile : cadtIndividualProfiles) {
            final Item item = generateItem(cadtIndividualProfile);
            //If it has data, include it. All has submittedAt facet.
            if (item.getFacets().size() > 1) {
                collection.getItems().getItems().add(item);
            }
        }
        collection.setCreatedAt(LocalDateTime.now());
        return collection;
    }

    protected List<FacetCategory> createCadtFacetsCategories() {
        final List<FacetCategory> facetCategories = new ArrayList<>();
        facetCategories.add(new FacetCategory(CREATED_AT_FACET, DateTimeType.PIVOT_VIEWER_DEFINITION));
        for (CadtArchetype archetype : CadtArchetype.values()) {
            facetCategories.add(new FacetCategory(archetype.getTag(), BooleanType.PIVOT_VIEWER_DEFINITION));
        }
        for (CadtCompetence competence : CadtCompetence.values()) {
            facetCategories.add(new FacetCategory(competence.getTag(), BooleanType.PIVOT_VIEWER_DEFINITION));
        }
        return facetCategories;
    }

    protected Item generateItem(CadtIndividualProfile cadtIndividualProfile) {
        if (cadtIndividualProfile == null) {
            throw new InvalidFormException("Cadt Individual Profile is null.");
        }

        final List<Facet<?>> facets = new ArrayList<>(basicData(cadtIndividualProfile.getCreatedAt()));
        populateFacets(facets, cadtIndividualProfile);
        final Item item = new Item(getColor(cadtIndividualProfile.getScore()), getPivotViewerLink(), cadtIndividualProfile.getCreatedBy());
        item.getFacets().addAll(facets);
        return item;
    }

    protected String getPivotViewerLink() {
        return PIVOTVIEWER_LINK;
    }

    protected void populateFacets(List<Facet<?>> facets, CadtIndividualProfile cadtIndividualProfile) {
        facets.addAll(createCadtValueFacets(cadtIndividualProfile));
    }

    private List<Facet<?>> createCadtValueFacets(CadtIndividualProfile cadtIndividualProfile) {

        cadtIndividualProfile.validate();

        //Score by archetypes
        final List<Facet<?>> facets = new ArrayList<>();

        //Adding archetypes.
        facets.add(new Facet<>(CadtArchetype.RECEPTIVE.getTag(), new BooleanType(
                cadtIndividualProfile.getReceptive().isSelected())));

        facets.add(new Facet<>(CadtArchetype.INNOVATOR.getTag(), new BooleanType(
                cadtIndividualProfile.getInnovator().isSelected())));

        facets.add(new Facet<>(CadtArchetype.STRATEGIST.getTag(), new BooleanType(
                cadtIndividualProfile.getStrategist().isSelected())));

        facets.add(new Facet<>(CadtArchetype.VISIONARY.getTag(), new BooleanType(
                cadtIndividualProfile.getVisionary().isSelected())));

        facets.add(new Facet<>(CadtArchetype.LEADER.getTag(), new BooleanType(
                cadtIndividualProfile.getLeader().isSelected())));

        facets.add(new Facet<>(CadtArchetype.BANKER.getTag(), new BooleanType(
                cadtIndividualProfile.getBanker().isSelected())));

        facets.add(new Facet<>(CadtArchetype.SCIENTIST.getTag(), new BooleanType(
                cadtIndividualProfile.getScientist().isSelected())));

        facets.add(new Facet<>(CadtArchetype.TRADESMAN.getTag(), new BooleanType(
                cadtIndividualProfile.getTradesman().isSelected())));


        //Adding competences
        facets.add(new Facet<>(CadtCompetence.DISCIPLINE.getTag(), new BooleanType(cadtIndividualProfile.isDiscipline())));
        facets.add(new Facet<>(CadtCompetence.CLIENT_ORIENTED.getTag(), new BooleanType(cadtIndividualProfile.isClientOriented())));
        facets.add(new Facet<>(CadtCompetence.ENGAGEMENT.getTag(), new BooleanType(cadtIndividualProfile.isEngagement())));
        facets.add(new Facet<>(CadtCompetence.COOPERATION.getTag(), new BooleanType(cadtIndividualProfile.isCooperation())));
        facets.add(new Facet<>(CadtCompetence.LEADERSHIP.getTag(), new BooleanType(cadtIndividualProfile.isLeadership())));
        facets.add(new Facet<>(CadtCompetence.RELATIONSHIPS.getTag(), new BooleanType(cadtIndividualProfile.isRelationships())));
        facets.add(new Facet<>(CadtCompetence.DIRECTION.getTag(), new BooleanType(cadtIndividualProfile.isDirection())));
        facets.add(new Facet<>(CadtCompetence.MULTICULTURAL_SENSITIVITY.getTag(), new BooleanType(cadtIndividualProfile.isMulticulturalSensitivity())));
        facets.add(new Facet<>(CadtCompetence.JUDGEMENT.getTag(), new BooleanType(cadtIndividualProfile.isJudgement())));
        facets.add(new Facet<>(CadtCompetence.INDEPENDENCE.getTag(), new BooleanType(cadtIndividualProfile.isIndependence())));
        facets.add(new Facet<>(CadtCompetence.INITIATIVE.getTag(), new BooleanType(cadtIndividualProfile.isInitiative())));
        facets.add(new Facet<>(CadtCompetence.GOAL_SETTING.getTag(), new BooleanType(cadtIndividualProfile.isGoalSetting())));
        facets.add(new Facet<>(CadtCompetence.DECISIVENESS.getTag(), new BooleanType(cadtIndividualProfile.isDecisiveness())));
        facets.add(new Facet<>(CadtCompetence.FUTURE.getTag(), new BooleanType(cadtIndividualProfile.isFuture())));
        facets.add(new Facet<>(CadtCompetence.COMMUNICATION_SKILLS.getTag(), new BooleanType(cadtIndividualProfile.isCommunicationSkills())));
        facets.add(new Facet<>(CadtCompetence.BUSINESS_MINDED.getTag(), new BooleanType(cadtIndividualProfile.isBusinessMinded())));
        facets.add(new Facet<>(CadtCompetence.TENACITY.getTag(), new BooleanType(cadtIndividualProfile.isTenacity())));
        facets.add(new Facet<>(CadtCompetence.CONSCIENTIOUSNESS.getTag(), new BooleanType(cadtIndividualProfile.isConscientiousness())));
        facets.add(new Facet<>(CadtCompetence.INTERPERSONAL_SENSITIVITY.getTag(), new BooleanType(cadtIndividualProfile.isInterpersonalSensitivity())));
        facets.add(new Facet<>(CadtCompetence.FLEXIBILITY.getTag(), new BooleanType(cadtIndividualProfile.isFlexibility())));
        facets.add(new Facet<>(CadtCompetence.PERSUASIVENESS.getTag(), new BooleanType(cadtIndividualProfile.isPersuasiveness())));
        facets.add(new Facet<>(CadtCompetence.INNOVATION.getTag(), new BooleanType(cadtIndividualProfile.isInnovation())));
        facets.add(new Facet<>(CadtCompetence.PROBLEM_ANALYSIS.getTag(), new BooleanType(cadtIndividualProfile.isProblemAnalysis())));
        facets.add(new Facet<>(CadtCompetence.PLANIFICATION.getTag(), new BooleanType(cadtIndividualProfile.isPlanning())));

        return facets;
    }


    protected List<Facet<?>> basicData(LocalDateTime submittedTime) {
        final List<Facet<?>> facets = new ArrayList<>();
        if (submittedTime != null) {
            facets.add(new Facet<>(CREATED_AT_FACET, new DateTimeType(submittedTime)));
        }
        return facets;
    }

    protected String getColor(double score) {
        if (score < RED_COLOR_LIMIT) {
            return RED_COLOR_TAG;
        }
        if (score < ORANGE_COLOR_LIMIT) {
            return ORANGE_COLOR_TAG;
        }
        if (score < YELLOW_COLOR_LIMIT) {
            return YELLOW_COLOR_TAG;
        }
        if (score < LIGHT_GREEN_COLOR_LIMIT) {
            return LIGHT_GREEN_COLOR_TAG;
        }
        return DARK_GREEN_COLOR_TAG;
    }
}
