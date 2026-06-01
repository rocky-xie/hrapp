package top.btmdc.hr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import top.btmdc.hr.config.AsyncSyncConfiguration;
import top.btmdc.hr.config.EmbeddedSQL;
import top.btmdc.hr.config.JacksonConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        HrappApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        top.btmdc.hr.config.JacksonHibernateConfiguration.class,
    }
)
@EmbeddedSQL
public @interface IntegrationTest {}
