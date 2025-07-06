package com.example.demo.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * general configuration class
 */
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateAuditProvider")
@RequiredArgsConstructor
public class Config {
    /**
     * to auto persist date to db
     */
    @Bean
    @Qualifier(value = "dateAuditProvider")
    public CurrentDateTimeProvider dateAuditProvider() {
        return CurrentDateTimeProvider.INSTANCE;
    }

    /**
     * messagesource config
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    /**
     * message source validation messages integration
     */
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
