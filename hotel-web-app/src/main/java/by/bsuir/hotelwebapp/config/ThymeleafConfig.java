package by.bsuir.hotelwebapp.config;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;

public class ThymeleafConfig {
    public static ITemplateEngine buildTemplateEngine(IWebApplication webApp) {
        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(webApp);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        
        return templateEngine;
    }
}
