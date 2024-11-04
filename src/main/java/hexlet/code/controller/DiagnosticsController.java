package hexlet.code.controller;

import io.sentry.Sentry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for diagnostic endpoints.
 * This controller provides methods to check the application's status.
 */
@RestController
public class DiagnosticsController {

    /**
     * Checks if the SENTRY_AUTH_TOKEN environment variable is set.
     *
     * @return a message indicating whether the token is set or not.
     */
    @GetMapping("/check-sentry-token")
    public String checkSentryToken() {
        String sentryToken = System.getenv("SENTRY_AUTH_TOKEN");

        try {
            throw new Exception("This is a test exception for Sentry.");
        } catch (Exception e) {
            Sentry.captureException(e);
        }

        return (sentryToken != null) ? "SENTRY_AUTH_TOKEN is set" : "SENTRY_AUTH_TOKEN is NOT set";
    }
}
