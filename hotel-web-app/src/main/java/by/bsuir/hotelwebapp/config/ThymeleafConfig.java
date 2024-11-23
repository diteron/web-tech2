package by.bsuir.hotelwebapp.config;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import jakarta.servlet.ServletContext;

public class ThymeleafConfig {
    private static JakartaServletWebApplication jakartaWebApp;
    private static TemplateEngine templateEngine;
    private static boolean isInit = false;

    public static TemplateEngine getTemplateEngine(ServletContext servletsContext) {
        if (!isInit) {
            init(servletsContext);
            isInit = true;
        }

        return templateEngine;
    }

    public static JakartaServletWebApplication getJakartaServletWebApplication(ServletContext servletsContext) {
        if (!isInit) {
            init(servletsContext);
            isInit = true;
        }

        return jakartaWebApp;
    }

    private static void init(ServletContext servletsContext) {
        jakartaWebApp = JakartaServletWebApplication.buildApplication(servletsContext);

        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(jakartaWebApp);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }
}
