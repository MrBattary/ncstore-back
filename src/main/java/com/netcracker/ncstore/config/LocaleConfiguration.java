package com.netcracker.ncstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * This class is used to configure anything related to locale
 */
@Configuration
public class LocaleConfiguration {

    @Value("${locale.default.code}")
    private String defaultLocaleCode;

    /**
     * This method is used to configure LocaleResolver.
     * It uses basic Spring AcceptHeaderLocaleResolver,
     * but sets default locale based on application.properties
     * @return AcceptHeaderLocaleResolver
     */
    @Bean
    public LocaleResolver localeResolver(){
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.forLanguageTag(defaultLocaleCode));
        return localeResolver;
    }
}
