package top.btmdc.hr.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        var ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, top.btmdc.hr.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, top.btmdc.hr.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, top.btmdc.hr.domain.Authority.class.getName());
            createCache(cm, top.btmdc.hr.domain.Position.class.getName());
            createCache(cm, top.btmdc.hr.domain.Person.class.getName());
            createCache(cm, top.btmdc.hr.domain.Skill.class.getName());
            createCache(cm, top.btmdc.hr.domain.SkillLevel.class.getName());
            createCache(cm, top.btmdc.hr.domain.PersonSkill.class.getName());
            createCache(cm, top.btmdc.hr.domain.PositionSkillRequirement.class.getName());
            createCache(cm, top.btmdc.hr.domain.PositionAssignment.class.getName());
            createCache(cm, top.btmdc.hr.domain.PositionMatch.class.getName());
            createCache(cm, top.btmdc.hr.domain.SuccessionCandidate.class.getName());
            createCache(cm, top.btmdc.hr.domain.PositionRisk.class.getName());
            createCache(cm, top.btmdc.hr.domain.PersonRisk.class.getName());
            createCache(cm, top.btmdc.hr.domain.SkillUpgradeRecord.class.getName());
            createCache(cm, top.btmdc.hr.domain.StaffSubstitution.class.getName());
            createCache(cm, top.btmdc.hr.domain.PositionRiskEvaluation.class.getName());
            createCache(cm, top.btmdc.hr.domain.TrainingGoal.class.getName());
            createCache(cm, top.btmdc.hr.domain.TrainingRecord.class.getName());
            createCache(cm, top.btmdc.hr.domain.SkillAssessment.class.getName());
            createCache(cm, top.btmdc.hr.domain.TrustObservation.class.getName());
            createCache(cm, top.btmdc.hr.domain.Evaluation.class.getName());
            createCache(cm, top.btmdc.hr.domain.ImprovementPlan.class.getName());
            createCache(cm, top.btmdc.hr.domain.KeyResponsibilityCategory.class.getName());
            createCache(cm, top.btmdc.hr.domain.CandidateProfile.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }
}
