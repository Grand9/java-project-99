package hexlet.code.config;

import io.sentry.spring.jakarta.EnableSentry;
import org.springframework.context.annotation.Configuration;

@EnableSentry(dsn = "https://482d489228161b7ed6e06bb6017d838e@o4508223780945920.ingest.de.sentry.io/4508223796281424")
@Configuration
public class SentryConfiguration {
}
