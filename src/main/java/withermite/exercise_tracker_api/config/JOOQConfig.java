package withermite.exercise_tracker_api.config;

import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.jooq.autoconfigure.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JOOQConfig {

    @Bean
    public DefaultConfigurationCustomizer customizeConfig() {
        return (DefaultConfiguration config) -> {
            config.settings().withReturnDefaultOnUpdatableRecord(true);
            config.set(new AppRecordMapperProvider(config));
            config.set(new AppRecordUnmapperProvider(config));
        };
    }
}
