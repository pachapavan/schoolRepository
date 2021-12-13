package com.school.app.config;

import io.github.jhipster.config.JHipsterProperties;
import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.school.app.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.school.app.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.school.app.domain.User.class.getName());
            createCache(cm, com.school.app.domain.Authority.class.getName());
            createCache(cm, com.school.app.domain.User.class.getName() + ".authorities");
            createCache(cm, com.school.app.domain.Staff.class.getName());
            createCache(cm, com.school.app.domain.Staff.class.getName() + ".salaries");
            createCache(cm, com.school.app.domain.Staff.class.getName() + ".teachers");
            createCache(cm, com.school.app.domain.Student.class.getName());
            createCache(cm, com.school.app.domain.Student.class.getName() + ".classes");
            createCache(cm, com.school.app.domain.Student.class.getName() + ".markes");
            createCache(cm, com.school.app.domain.Student.class.getName() + ".attendences");
            createCache(cm, com.school.app.domain.Student.class.getName() + ".fees");
            createCache(cm, com.school.app.domain.Student.class.getName() + ".busRouteNames");
            createCache(cm, com.school.app.domain.ClassName.class.getName());
            createCache(cm, com.school.app.domain.ClassName.class.getName() + ".sections");
            createCache(cm, com.school.app.domain.Section.class.getName());
            createCache(cm, com.school.app.domain.Subject.class.getName());
            createCache(cm, com.school.app.domain.StudentMarkes.class.getName());
            createCache(cm, com.school.app.domain.StudentMarkes.class.getName() + ".subjects");
            createCache(cm, com.school.app.domain.StudentMarkes.class.getName() + ".classes");
            createCache(cm, com.school.app.domain.Attendence.class.getName());
            createCache(cm, com.school.app.domain.StudentFee.class.getName());
            createCache(cm, com.school.app.domain.BusRoute.class.getName());
            createCache(cm, com.school.app.domain.BusRoute.class.getName() + ".busRoutes");
            createCache(cm, com.school.app.domain.BusRoute.class.getName() + ".busStops");
            createCache(cm, com.school.app.domain.BusRouteName.class.getName());
            createCache(cm, com.school.app.domain.BusStops.class.getName());
            createCache(cm, com.school.app.domain.StaffSalary.class.getName());
            createCache(cm, com.school.app.domain.Page.class.getName());
            createCache(cm, com.school.app.domain.Page.class.getName() + ".genericObjectsLists");
            createCache(cm, com.school.app.domain.Page.class.getName() + ".flexBoxes");
            createCache(cm, com.school.app.domain.Spacing.class.getName());
            createCache(cm, com.school.app.domain.Spacing.class.getName() + ".margins");
            createCache(cm, com.school.app.domain.Spacing.class.getName() + ".paddings");
            createCache(cm, com.school.app.domain.FlexBox.class.getName());
            createCache(cm, com.school.app.domain.FlexBox.class.getName() + ".elements");
            createCache(cm, com.school.app.domain.Attributes.class.getName());
            createCache(cm, com.school.app.domain.Elements.class.getName());
            createCache(cm, com.school.app.domain.Elements.class.getName() + ".elements");
            createCache(cm, com.school.app.domain.Button.class.getName());
            createCache(cm, com.school.app.domain.Text.class.getName());
            createCache(cm, com.school.app.domain.Table.class.getName());
            createCache(cm, com.school.app.domain.Head.class.getName());
            createCache(cm, com.school.app.domain.Head.class.getName() + ".labels");
            createCache(cm, com.school.app.domain.Label.class.getName());
            createCache(cm, com.school.app.domain.Label.class.getName() + ".objectContainingStrings");
            createCache(cm, com.school.app.domain.Body.class.getName());
            createCache(cm, com.school.app.domain.Body.class.getName() + ".tabelValues");
            createCache(cm, com.school.app.domain.TabelValues.class.getName());
            createCache(cm, com.school.app.domain.TabelValues.class.getName() + ".objectContainingStrings");
            createCache(cm, com.school.app.domain.TabelValues.class.getName() + ".displayAtts");
            createCache(cm, com.school.app.domain.DisplayAtt.class.getName());
            createCache(cm, com.school.app.domain.DisplayAtt.class.getName() + ".badgeTypes");
            createCache(cm, com.school.app.domain.BadgeType.class.getName());
            createCache(cm, com.school.app.domain.FormWrap.class.getName());
            createCache(cm, com.school.app.domain.Badge.class.getName());
            createCache(cm, com.school.app.domain.Icon.class.getName());
            createCache(cm, com.school.app.domain.Image.class.getName());
            createCache(cm, com.school.app.domain.ObjectContainingString.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }
}
