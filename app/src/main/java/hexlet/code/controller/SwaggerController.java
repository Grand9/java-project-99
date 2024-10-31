package hexlet.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling Swagger UI requests.
 * This class is designed for extension.
 * To safely extend this class, please override the methods with caution.
 */
@Controller
public class SwaggerController {

    /**
     * Redirects to the Swagger UI page.
     *
     * @return the path to the Swagger UI index page
     */
    @GetMapping("/swagger-ui/")
    public String swaggerUi() {
        return "forward:/swagger-ui/index.html";
    }
}
