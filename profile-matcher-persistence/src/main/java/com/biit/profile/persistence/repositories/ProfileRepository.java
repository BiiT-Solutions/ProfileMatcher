package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.Profile;
import com.biit.server.persistence.repositories.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface ProfileRepository extends ElementRepository<Profile, Long> {

    Optional<Profile> findByName(String name);

    Optional<Profile> findByNameByHash(String name);

    Set<Profile> findByTrackingCode(String trackingCode);

    Set<Profile> findByTrackingCodeByHash(String trackingCode);

    Set<Profile> findByType(String type);

    Set<Profile> findByTypeByHash(String type);

    @Query("""
               SELECT p FROM Profile p WHERE
                  ((CASE WHEN p.discipline = true AND 'disiciline' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.clientOriented = true AND 'client-oriented' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.engagement = true AND 'engagement' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.cooperation = true AND 'cooperation' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.leadership = true AND 'leadership' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.relationships = true AND 'building-and-maintaining' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.direction = true AND 'direction' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.multiculturalSensitivity = true AND 'multicultural-sensitivity' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.judgement = true AND 'judgement' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.independence = true AND 'independence' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.initiative = true AND 'initiative' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.goalSetting = true AND 'goal-setting' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.future = true AND 'future' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.decisiveness = true AND 'decisiveness' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.communicationSkills = true AND 'communication-skills' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.businessMinded = true AND 'business-minded' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.tenacity = true AND 'tenacity' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.conscientiousness = true AND 'conscientiousness' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.interpersonalSensitivity = true AND 'interpersonal-sensitivity' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.flexibility = true AND 'flexibility' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.persuasiveness = true AND 'persuasiveness' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.innovation = true AND 'innovation' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.problemAnalysis = true AND 'problem-analysis' IN :competences THEN 1 ELSE 0 END) +
                  (CASE WHEN p.planning = true AND 'planification' IN :competences THEN 1 ELSE 0 END)) >= :threshold
            """)
    List<Profile> findByCompetencesIn(Collection<String> competences, int threshold);

}
