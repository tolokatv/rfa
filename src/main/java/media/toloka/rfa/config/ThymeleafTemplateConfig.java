package media.toloka.rfa.config;

// https://blog.codeleak.pl/2017/03/getting-started-with-thymeleaf-3-text.html

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;

@Configuration
public class ThymeleafTemplateConfig {

    @Value("${media.toloka.rfa.server.dirconfigtemplate}")
    private String dirconfigtemplate;

    @Bean
    public SpringTemplateEngine htmlspringTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.addTemplateResolver(textTemplateResolver());
        templateEngine.addTemplateResolver(htmlTemplateResolver());
//        templateEngine.addTemplateResolver(textTemplateResolver());
        return templateEngine;
    }
    @Bean
    public SpringTemplateEngine textspringTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(textTemplateResolver());
//        templateEngine.addTemplateResolver(htmlTemplateResolver());
//        templateEngine.addTemplateResolver(textTemplateResolver());
        return templateEngine;
    }

    @Bean
    public ClassLoaderTemplateResolver htmlTemplateResolver(){
        ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
        emailTemplateResolver.setPrefix("/templates/");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
        emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return emailTemplateResolver;
    }

    @Bean
    public ClassLoaderTemplateResolver textTemplateResolver() {
        ClassLoaderTemplateResolver texttemplateResolver = new ClassLoaderTemplateResolver();
        texttemplateResolver.setPrefix(dirconfigtemplate);
        texttemplateResolver.setSuffix(".txt");
        texttemplateResolver.setTemplateMode(TemplateMode.TEXT);
        texttemplateResolver.setCharacterEncoding("UTF8");
        texttemplateResolver.setCheckExistence(true);
        texttemplateResolver.setCacheable(false);
        return texttemplateResolver;
    }
}
