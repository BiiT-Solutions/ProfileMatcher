package com.biit.profile.persistence.entities;

/*-
 * #%L
 * Profile Matcher (Persistence)
 * %%
 * Copyright (C) 2024 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import com.biit.profile.persistence.entities.cadt.CadtCompetence;
import com.biit.profile.persistence.entities.exceptions.InvalidProfileValueException;
import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class SearchableCompetences<KEY> extends Element<KEY> {

    @Serial
    private static final long serialVersionUID = 6865358733517748019L;

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

    public void setAllCompetences(boolean value) {
        setDiscipline(value);
        setClientOriented(value);
        setEngagement(value);
        setCooperation(value);
        setLeadership(value);
        setRelationships(value);
        setDirection(value);
        setMulticulturalSensitivity(value);
        setJudgement(value);
        setIndependence(value);
        setInitiative(value);
        setGoalSetting(value);
        setDecisiveness(value);
        setFuture(value);
        setCommunicationSkills(value);
        setBusinessMinded(value);
        setTenacity(value);
        setConscientiousness(value);
        setInterpersonalSensitivity(value);
        setFlexibility(value);
        setPersuasiveness(value);
        setInnovation(value);
        setProblemAnalysis(value);
        setPlanning(value);
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


    public Set<CadtCompetence> getSelectedCompetences() {
        final Set<CadtCompetence> competences = new HashSet<>();
        if (isDiscipline()) {
            competences.add(CadtCompetence.DISCIPLINE);
        }
        if (isClientOriented()) {
            competences.add(CadtCompetence.CLIENT_ORIENTED);
        }
        if (isEngagement()) {
            competences.add(CadtCompetence.ENGAGEMENT);
        }
        if (isCooperation()) {
            competences.add(CadtCompetence.COOPERATION);
        }
        if (isLeadership()) {
            competences.add(CadtCompetence.LEADERSHIP);
        }
        if (isRelationships()) {
            competences.add(CadtCompetence.RELATIONSHIPS);
        }
        if (isDirection()) {
            competences.add(CadtCompetence.DIRECTION);
        }
        if (isMulticulturalSensitivity()) {
            competences.add(CadtCompetence.MULTICULTURAL_SENSITIVITY);
        }
        if (isJudgement()) {
            competences.add(CadtCompetence.JUDGEMENT);
        }
        if (isIndependence()) {
            competences.add(CadtCompetence.INDEPENDENCE);
        }
        if (isInitiative()) {
            competences.add(CadtCompetence.INITIATIVE);
        }
        if (isGoalSetting()) {
            competences.add(CadtCompetence.GOAL_SETTING);
        }
        if (isDecisiveness()) {
            competences.add(CadtCompetence.DECISIVENESS);
        }
        if (isFuture()) {
            competences.add(CadtCompetence.FUTURE);
        }
        if (isCommunicationSkills()) {
            competences.add(CadtCompetence.COMMUNICATION_SKILLS);
        }
        if (isBusinessMinded()) {
            competences.add(CadtCompetence.BUSINESS_MINDED);
        }
        if (isTenacity()) {
            competences.add(CadtCompetence.TENACITY);
        }
        if (isConscientiousness()) {
            competences.add(CadtCompetence.CONSCIENTIOUSNESS);
        }
        if (isInterpersonalSensitivity()) {
            competences.add(CadtCompetence.INTERPERSONAL_SENSITIVITY);
        }
        if (isFlexibility()) {
            competences.add(CadtCompetence.FLEXIBILITY);
        }
        if (isPersuasiveness()) {
            competences.add(CadtCompetence.PERSUASIVENESS);
        }
        if (isInnovation()) {
            competences.add(CadtCompetence.INNOVATION);
        }
        if (isProblemAnalysis()) {
            competences.add(CadtCompetence.PROBLEM_ANALYSIS);
        }
        if (isPlanning()) {
            competences.add(CadtCompetence.PLANIFICATION);
        }
        return competences;
    }

    public abstract void validate() throws InvalidProfileValueException;

}
