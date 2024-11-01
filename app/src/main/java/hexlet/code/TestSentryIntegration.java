package hexlet.code;

import io.sentry.Sentry;

public class TestSentryIntegration {
    public static void main(String[] args) {
        try {
            throw new Exception("This is a test.");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }
}
