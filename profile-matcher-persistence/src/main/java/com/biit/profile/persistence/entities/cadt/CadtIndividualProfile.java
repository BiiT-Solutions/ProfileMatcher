package com.biit.profile.persistence.entities.cadt;

import com.biit.profile.persistence.entities.exceptions.InvalidProfileValueException;
import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.UUID;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "cadt_individual_profiles", indexes = {
        @Index(name = "ind_receptive", columnList = "receptive"),
        @Index(name = "ind_innovator", columnList = "innovator"),
        @Index(name = "ind_strategist", columnList = "strategist"),
        @Index(name = "ind_visionary", columnList = "visionary"),
        @Index(name = "ind_leader", columnList = "leader"),
        @Index(name = "ind_banker", columnList = "banker"),
        @Index(name = "ind_scientist", columnList = "scientist"),
        @Index(name = "ind_tradesman", columnList = "tradesman"),
        @Index(name = "ind_created_by", columnList = "created_by"),
})
public class CadtIndividualProfile extends Element<Long> {

    @Serial
    private static final long serialVersionUID = -2465157070391914318L;

    private static final int SELECTED_COMPETENCES = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receptive", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardSelection receptive;

    @Column(name = "innovator", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardSelection innovator;

    @Column(name = "strategist", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardSelection strategist;

    @Column(name = "visionary", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardSelection visionary;

    @Column(name = "leader", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardSelection leader;

    @Column(name = "banker", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardSelection banker;

    @Column(name = "scientist", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardSelection scientist;

    @Column(name = "tradesman", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardSelection tradesman;

    @Column(name = "discipline", nullable = false)
    private boolean discipline = false;

    @Column(name = "client_oriented", nullable = false)
    private boolean clientOriented = false;

    @Column(name = "engagement", nullable = false)
    private boolean engagement = false;

    @Column(name = "cooperation", nullable = false)
    private boolean cooperation = false;

    @Column(name = "leadership", nullable = false)
    private boolean leadership = false;

    @Column(name = "relationships", nullable = false)
    private boolean relationships = false;

    @Column(name = "direction", nullable = false)
    private boolean direction = false;

    @Column(name = "multicultural_sensitivity", nullable = false)
    private boolean multiculturalSensitivity = false;

    @Column(name = "judgement", nullable = false)
    private boolean judgement = false;

    @Column(name = "independence", nullable = false)
    private boolean independence = false;

    @Column(name = "initiative", nullable = false)
    private boolean initiative = false;

    @Column(name = "goal_setting", nullable = false)
    private boolean goalSetting = false;

    @Column(name = "decisiveness", nullable = false)
    private boolean decisiveness = false;

    @Column(name = "future", nullable = false)
    private boolean future = false;

    @Column(name = "communication_skills", nullable = false)
    private boolean communicationSkills = false;

    @Column(name = "business-minded", nullable = false)
    private boolean businessMinded = false;

    @Column(name = "tenacity", nullable = false)
    private boolean tenacity = false;

    @Column(name = "conscientiousness", nullable = false)
    private boolean conscientiousness = false;

    @Column(name = "interpersonal_sensitivity", nullable = false)
    private boolean interpersonalSensitivity = false;

    @Column(name = "flexibility", nullable = false)
    private boolean flexibility = false;

    @Column(name = "persuasiveness", nullable = false)
    private boolean persuasiveness = false;

    @Column(name = "innovation", nullable = false)
    private boolean innovation = false;

    @Column(name = "problem_analysis", nullable = false)
    private boolean problemAnalysis = false;

    @Column(name = "planning", nullable = false)
    private boolean planning = false;

    @Column(name = "drools_id", nullable = false)
    private String droolsId;

    @Column(name = "form_version", nullable = false)
    private int formVersion;

    @Column(name = "session")
    private UUID session;

    @Column(name = "score", nullable = false)
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

    public void setInterpersonalSensitivity(boolean interpersonaSensitivity) {
        this.interpersonalSensitivity = interpersonaSensitivity;
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

    public void assignCard(CadtArchetype archetype, CardSelection cardSelection) {
        switch (archetype) {
            case RECEPTIVE -> setReceptive(cardSelection);
            case INNOVATOR -> setInnovator(cardSelection);
            case STRATEGIST -> setStrategist(cardSelection);
            case VISIONARY -> setVisionary(cardSelection);
            case LEADER -> setLeader(cardSelection);
            case BANKER -> setBanker(cardSelection);
            case SCIENTIST -> setScientist(cardSelection);
            case TRADESMAN -> setTradesman(cardSelection);
            default -> throw new IllegalStateException("Unexpected value: " + archetype);
        }
    }

    public void assignCompetence(CadtCompetence competence) {
        switch (competence) {
            case DISCIPLINE -> setDiscipline(true);
            case CLIENT_ORIENTED -> setClientOriented(true);
            case ENGAGEMENT -> setEngagement(true);
            case COOPERATION -> setCooperation(true);
            case LEADERSHIP -> setLeadership(true);
            case RELATIONSHIPS -> setRelationships(true);
            case DIRECTION -> setDirection(true);
            case MULTICULTURAL_SENSITIVITY -> setMulticulturalSensitivity(true);
            case JUDGEMENT -> setJudgement(true);
            case INDEPENDENCE -> setIndependence(true);
            case INITIATIVE -> setInitiative(true);
            case GOAL_SETTING -> setGoalSetting(true);
            case DECISIVENESS -> setDecisiveness(true);
            case FUTURE -> setFuture(true);
            case COMMUNICATION_SKILLS -> setCommunicationSkills(true);
            case BUSINESS_MINDED -> setBusinessMinded(true);
            case TENACITY -> setTenacity(true);
            case CONSCIENTIOUSNESS -> setConscientiousness(true);
            case INTERPERSONAL_SENSITIVITY -> setInterpersonalSensitivity(true);
            case FLEXIBILITY -> setFlexibility(true);
            case PERSUASIVENESS -> setPersuasiveness(true);
            case INNOVATION -> setInnovation(true);
            case PROBLEM_ANALYSIS -> setProblemAnalysis(true);
            case PLANIFICATION -> setPlanning(true);
            default -> throw new IllegalStateException("Unexpected value: " + competence);
        }
    }

    public void validate() throws InvalidProfileValueException {
        if (getReceptive() == null) {
            throw new InvalidProfileValueException("Archetypes 'receptive' is null.");
        }
        if (getInnovator() == null) {
            throw new InvalidProfileValueException("Archetypes 'innovator' is null.");
        }
        if (getStrategist() == null) {
            throw new InvalidProfileValueException("Archetypes 'strategist' is null.");
        }
        if (getVisionary() == null) {
            throw new InvalidProfileValueException("Archetypes 'visionary' is null.");
        }
        if (getLeader() == null) {
            throw new InvalidProfileValueException("Archetypes 'leader' is null.");
        }
        if (getBanker() == null) {
            throw new InvalidProfileValueException("Archetypes 'banker' is null.");
        }
        if (getScientist() == null) {
            throw new InvalidProfileValueException("Archetypes 'scientist' is null.");
        }
        if (getTradesman() == null) {
            throw new InvalidProfileValueException("Archetypes 'tradesman' is null.");
        }

        int competences = 0;
        if (isDiscipline()) {
            competences++;
        }
        if (isClientOriented()) {
            competences++;
        }
        if (isEngagement()) {
            competences++;
        }
        if (isCooperation()) {
            competences++;
        }
        if (isLeadership()) {
            competences++;
        }
        if (isRelationships()) {
            competences++;
        }
        if (isDirection()) {
            competences++;
        }
        if (isMulticulturalSensitivity()) {
            competences++;
        }
        if (isJudgement()) {
            competences++;
        }
        if (isIndependence()) {
            competences++;
        }
        if (isInitiative()) {
            competences++;
        }
        if (isGoalSetting()) {
            competences++;
        }
        if (isDecisiveness()) {
            competences++;
        }
        if (isFuture()) {
            competences++;
        }
        if (isCommunicationSkills()) {
            competences++;
        }
        if (isBusinessMinded()) {
            competences++;
        }
        if (isTenacity()) {
            competences++;
        }
        if (isConscientiousness()) {
            competences++;
        }
        if (isInterpersonalSensitivity()) {
            competences++;
        }
        if (isFlexibility()) {
            competences++;
        }
        if (isPersuasiveness()) {
            competences++;
        }
        if (isInnovation()) {
            competences++;
        }
        if (isProblemAnalysis()) {
            competences++;
        }
        if (isPlanning()) {
            competences++;
        }

        if (competences != SELECTED_COMPETENCES) {
            throw new InvalidProfileValueException("Competences must be " + SELECTED_COMPETENCES);
        }

    }
}
