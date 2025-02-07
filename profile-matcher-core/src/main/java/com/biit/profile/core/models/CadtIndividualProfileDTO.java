package com.biit.profile.core.models;

import com.biit.profile.persistence.entities.cadt.CardSelection;
import com.biit.server.controllers.models.ElementDTO;

import java.io.Serial;
import java.util.UUID;


public class CadtIndividualProfileDTO extends ElementDTO<Long> {

    @Serial
    private static final long serialVersionUID = -2465157070391914318L;


    private Long id;

    private CardSelection receptive;

    private CardSelection innovator;

    private CardSelection strategist;

    private CardSelection visionary;

    private CardSelection leader;

    private CardSelection banker;

    private CardSelection scientist;

    private CardSelection tradesman;

    private boolean discipline = false;

    private boolean clientOriented = false;

    private boolean engagement = false;

    private boolean cooperation = false;

    private boolean leadership = false;

    private boolean relationships = false;

    private boolean direction = false;

    private boolean multiculturalSensitivity = false;

    private boolean judgement = false;

    private boolean independence = false;

    private boolean initiative = false;

    private boolean goalSetting = false;

    private boolean decisiveness = false;

    private boolean future = false;

    private boolean communicationSkills = false;

    private boolean businessMinded = false;

    private boolean tenacity = false;

    private boolean conscientiousness = false;

    private boolean interpersonalSensitivity = false;

    private boolean flexibility = false;

    private boolean persuasiveness = false;

    private boolean innovation = false;

    private boolean problemAnalysis = false;

    private boolean planning = false;

    private String droolsId;

    private int formVersion;

    private UUID session;

    private Double score;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public CardSelection getReceptive() {
        return receptive;
    }

    public void setReceptive(CardSelection receptive) {
        this.receptive = receptive;
    }

    public CardSelection getInnovator() {
        return innovator;
    }

    public void setInnovator(CardSelection innovator) {
        this.innovator = innovator;
    }

    public CardSelection getStrategist() {
        return strategist;
    }

    public void setStrategist(CardSelection strategist) {
        this.strategist = strategist;
    }

    public CardSelection getVisionary() {
        return visionary;
    }

    public void setVisionary(CardSelection visionary) {
        this.visionary = visionary;
    }

    public CardSelection getLeader() {
        return leader;
    }

    public void setLeader(CardSelection leader) {
        this.leader = leader;
    }

    public CardSelection getBanker() {
        return banker;
    }

    public void setBanker(CardSelection banker) {
        this.banker = banker;
    }

    public CardSelection getScientist() {
        return scientist;
    }

    public void setScientist(CardSelection scientist) {
        this.scientist = scientist;
    }

    public CardSelection getTradesman() {
        return tradesman;
    }

    public void setTradesman(CardSelection tradesman) {
        this.tradesman = tradesman;
    }

    public boolean isDiscipline() {
        return discipline;
    }

    public void setDiscipline(boolean discipline) {
        this.discipline = discipline;
    }

    public boolean isClientOriented() {
        return clientOriented;
    }

    public void setClientOriented(boolean clientOriented) {
        this.clientOriented = clientOriented;
    }

    public boolean isEngagement() {
        return engagement;
    }

    public void setEngagement(boolean engagement) {
        this.engagement = engagement;
    }

    public boolean isCooperation() {
        return cooperation;
    }

    public void setCooperation(boolean cooperation) {
        this.cooperation = cooperation;
    }

    public boolean isLeadership() {
        return leadership;
    }

    public void setLeadership(boolean leadership) {
        this.leadership = leadership;
    }

    public boolean isRelationships() {
        return relationships;
    }

    public void setRelationships(boolean relationships) {
        this.relationships = relationships;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public boolean isMulticulturalSensitivity() {
        return multiculturalSensitivity;
    }

    public void setMulticulturalSensitivity(boolean multiculturalSensitivity) {
        this.multiculturalSensitivity = multiculturalSensitivity;
    }

    public boolean isJudgement() {
        return judgement;
    }

    public void setJudgement(boolean judgement) {
        this.judgement = judgement;
    }

    public boolean isIndependence() {
        return independence;
    }

    public void setIndependence(boolean independence) {
        this.independence = independence;
    }

    public boolean isInitiative() {
        return initiative;
    }

    public void setInitiative(boolean initiative) {
        this.initiative = initiative;
    }

    public boolean isGoalSetting() {
        return goalSetting;
    }

    public void setGoalSetting(boolean goalSetting) {
        this.goalSetting = goalSetting;
    }

    public boolean isDecisiveness() {
        return decisiveness;
    }

    public void setDecisiveness(boolean decisiveness) {
        this.decisiveness = decisiveness;
    }

    public boolean isFuture() {
        return future;
    }

    public void setFuture(boolean future) {
        this.future = future;
    }

    public boolean isCommunicationSkills() {
        return communicationSkills;
    }

    public void setCommunicationSkills(boolean communicationSkills) {
        this.communicationSkills = communicationSkills;
    }

    public boolean isBusinessMinded() {
        return businessMinded;
    }

    public void setBusinessMinded(boolean businessMinded) {
        this.businessMinded = businessMinded;
    }

    public boolean isTenacity() {
        return tenacity;
    }

    public void setTenacity(boolean tenacity) {
        this.tenacity = tenacity;
    }

    public boolean isConscientiousness() {
        return conscientiousness;
    }

    public void setConscientiousness(boolean conscientiousness) {
        this.conscientiousness = conscientiousness;
    }

    public boolean isInterpersonalSensitivity() {
        return interpersonalSensitivity;
    }

    public void setInterpersonalSensitivity(boolean interpersonalSensitivity) {
        this.interpersonalSensitivity = interpersonalSensitivity;
    }

    public boolean isFlexibility() {
        return flexibility;
    }

    public void setFlexibility(boolean flexibility) {
        this.flexibility = flexibility;
    }

    public boolean isPersuasiveness() {
        return persuasiveness;
    }

    public void setPersuasiveness(boolean persuasiveness) {
        this.persuasiveness = persuasiveness;
    }

    public boolean isInnovation() {
        return innovation;
    }

    public void setInnovation(boolean innovation) {
        this.innovation = innovation;
    }

    public boolean isProblemAnalysis() {
        return problemAnalysis;
    }

    public void setProblemAnalysis(boolean problemAnalysis) {
        this.problemAnalysis = problemAnalysis;
    }

    public boolean isPlanning() {
        return planning;
    }

    public void setPlanning(boolean planning) {
        this.planning = planning;
    }

    public String getDroolsId() {
        return droolsId;
    }

    public void setDroolsId(String droolsId) {
        this.droolsId = droolsId;
    }

    public int getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(int formVersion) {
        this.formVersion = formVersion;
    }

    public UUID getSession() {
        return session;
    }

    public void setSession(UUID session) {
        this.session = session;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
