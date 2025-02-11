package com.biit.profile.persistence.entities.cadt;

import com.biit.profile.persistence.entities.SearchableCompetences;
import com.biit.profile.persistence.entities.exceptions.InvalidProfileValueException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class CadtIndividualProfile extends SearchableCompetences<Long> {

    @Serial
    private static final long serialVersionUID = -2465157070391914318L;

    private static final int SELECTED_COMPETENCES = 10;
    private static final int ARCHETYPES_BY_GROUP = 4;

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


    @Column(name = "drools_id")
    private String droolsId;

    @Column(name = "form_version", nullable = false)
    private int formVersion;

    @Column(name = "session")
    private UUID session;

    @Column(name = "score", nullable = false)
    private Double score;

    public CadtIndividualProfile() {
        this.score = 0d;
        this.formVersion = 1;
    }

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
        if (archetype != null) {
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

        final Set<CardSelection> feminineSelections = new HashSet<>(List.of(getReceptive(), getInnovator(), getStrategist(), getVisionary()));
        final Set<CardSelection> maleSelections = new HashSet<>(List.of(getLeader(), getBanker(), getScientist(), getTradesman()));

        if (feminineSelections.size() < ARCHETYPES_BY_GROUP) {
            throw new InvalidProfileValueException("Archetypes selection malformed. Current selections are '" + feminineSelections + "'.");
        }

        if (maleSelections.size() < ARCHETYPES_BY_GROUP) {
            throw new InvalidProfileValueException("Archetypes selection malformed. Current selections are '" + maleSelections + "'.");
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
