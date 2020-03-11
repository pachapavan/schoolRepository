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
