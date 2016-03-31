package com.bonc.epm.paas.sso.casclient;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.bonc.epm.paas.sso.casclient.EnableCasClient.ValidationType;
import com.bonc.epm.paas.sso.filter.SSOFilter;

/**
 * Configuration class providing default CAS client infrastructure filters.
 * This configuration facility is typically imported into Spring's Application Context via
 * {@link net.unicon.cas.client.configuration.EnableCasClient EnableCasClient} meta annotation.
 *
 * @author Dmitriy Kopylenko
 * @see net.unicon.cas.client.configuration.EnableCasClient
 * @since 1.0.0
 */
@Configuration
public class CasClientConfiguration implements ImportAware {

    @Autowired
    CasClientConfigurationProperties configProps;

    private CasClientConfigurer casClientConfigurer;

    private ValidationType validationType;
    
    @Autowired
    private SSOFilter ssoFilter;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableCasClient.class.getName()));
        if (annotationAttributes == null) {
            throw new IllegalArgumentException("@EnableCasClient is not present on importing class " + importMetadata.getClassName());
        }
        this.validationType = annotationAttributes.getEnum("validationType");
    }

    @Bean
    public FilterRegistrationBean casValidationFilter() {
        final FilterRegistrationBean validationFilter = new FilterRegistrationBean();
       
        if(!configProps.getEnable()){
        	validationFilter.setFilter(ssoFilter);
        	return validationFilter;
        }
        final Filter targetCasValidationFilter;
        switch (this.validationType) {
            case CAS:
                targetCasValidationFilter = new Cas20ProxyReceivingTicketValidationFilter();
                break;
            case CAS3:
                targetCasValidationFilter = new Cas30ProxyReceivingTicketValidationFilter();
                break;
            default:
                throw new IllegalStateException("Unknown CAS validation type");
        }

        initFilter(validationFilter,
                targetCasValidationFilter,
                1,
                constructInitParams("casServerUrlPrefix", this.configProps.getServerUrlPrefix(), this.configProps.getClientHostUrl()),
                this.configProps.getValidationUrlPatterns());

        if (this.configProps.getUseSession() != null) {
            validationFilter.getInitParameters().put("useSession", String.valueOf(this.configProps.getUseSession()));
        }
        if (this.configProps.getRedirectAfterValidation() != null) {
            validationFilter.getInitParameters().put("redirectAfterValidation", String.valueOf(this.configProps.getRedirectAfterValidation()));
        }

        //Proxy tickets validation
        if (this.configProps.getAcceptAnyProxy() != null) {
            validationFilter.getInitParameters().put("acceptAnyProxy", String.valueOf(this.configProps.getAcceptAnyProxy()));
        }
        if (this.configProps.getAllowedProxyChains().size() > 0) {
            validationFilter.getInitParameters().put("allowedProxyChains",
                    StringUtils.collectionToDelimitedString(this.configProps.getAllowedProxyChains(), " "));
        }
        if (this.configProps.getProxyCallbackUrl() != null) {
            validationFilter.getInitParameters().put("proxyCallbackUrl", this.configProps.getProxyCallbackUrl());
        }
        if (this.configProps.getProxyReceptorUrl() != null) {
            validationFilter.getInitParameters().put("proxyReceptorUrl", this.configProps.getProxyReceptorUrl());
        }

        if (this.casClientConfigurer != null) {
            this.casClientConfigurer.configureValidationFilter(validationFilter);
        }
        return validationFilter;
    }

    @Bean
    public FilterRegistrationBean casAuthenticationFilter() {
        final FilterRegistrationBean authnFilter = new FilterRegistrationBean();
        if(!configProps.getEnable()){
        	authnFilter.setFilter(ssoFilter);
        	return authnFilter;
        }
        final Filter targetCasAuthnFilter = new AuthenticationFilter();

        initFilter(authnFilter,
                targetCasAuthnFilter,
                2,
                constructInitParams("casServerLoginUrl", this.configProps.getServerLoginUrl(), this.configProps.getClientHostUrl()),
                this.configProps.getAuthenticationUrlPatterns());

        if (this.configProps.getGateway() != null) {
            authnFilter.getInitParameters().put("gateway", String.valueOf(this.configProps.getGateway()));
        }

        if (this.casClientConfigurer != null) {
            this.casClientConfigurer.configureAuthenticationFilter(authnFilter);
        }
        return authnFilter;
    }


    @Bean
    public FilterRegistrationBean casHttpServletRequestWrapperFilter() {
        final FilterRegistrationBean reqWrapperFilter = new FilterRegistrationBean();
        if(!configProps.getEnable()){
        	reqWrapperFilter.setFilter(ssoFilter);
        	return reqWrapperFilter;
        }
        reqWrapperFilter.setFilter(new HttpServletRequestWrapperFilter());
        if (this.configProps.getRequestWrapperUrlPatterns().size() > 0) {
            reqWrapperFilter.setUrlPatterns(this.configProps.getRequestWrapperUrlPatterns());
        }
        reqWrapperFilter.setOrder(3);

        if (this.casClientConfigurer != null) {
            this.casClientConfigurer.configureHttpServletRequestWrapperFilter(reqWrapperFilter);
        }
        return reqWrapperFilter;
    }

    @Bean
    public FilterRegistrationBean casAssertionThreadLocalFilter() {
        final FilterRegistrationBean assertionTLFilter = new FilterRegistrationBean();
        if(!configProps.getEnable()){
        	assertionTLFilter.setFilter(ssoFilter);
        	return assertionTLFilter;
        }
        assertionTLFilter.setFilter(new AssertionThreadLocalFilter());
        if (this.configProps.getAssertionThreadLocalUrlPatterns().size() > 0) {
            assertionTLFilter.setUrlPatterns(this.configProps.getAssertionThreadLocalUrlPatterns());
        }
        assertionTLFilter.setOrder(4);

        if (this.casClientConfigurer != null) {
            this.casClientConfigurer.configureAssertionThreadLocalFilter(assertionTLFilter);
        }
        return assertionTLFilter;
    }


    @Autowired(required = false)
    void setConfigurers(Collection<CasClientConfigurer> configurers) {
        if (CollectionUtils.isEmpty(configurers)) {
            return;
        }
        if (configurers.size() > 1) {
            throw new IllegalStateException(configurers.size() + " implementations of " +
                    "CasClientConfigurer were found when only 1 was expected. " +
                    "Refactor the configuration such that CasClientConfigurer is " +
                    "implemented only once or not at all.");
        }
        this.casClientConfigurer = configurers.iterator().next();
    }

    private Map<String, String> constructInitParams(final String casUrlParamName, final String casUrlParamVal, final String clientHostUrlVal) {
        final Map<String, String> initParams = new HashMap<>(2);
        initParams.put(casUrlParamName, casUrlParamVal);
        initParams.put("serverName", clientHostUrlVal);
        return initParams;
    }

    private void initFilter(final FilterRegistrationBean filterRegistrationBean,
                            final Filter targetFilter,
                            int filterOrder,
                            final Map<String, String> initParams,
                            List<String> urlPatterns) {
        filterRegistrationBean.setFilter(targetFilter);
        filterRegistrationBean.setOrder(filterOrder);
        filterRegistrationBean.setInitParameters(initParams);
        if (urlPatterns.size() > 0) {
            filterRegistrationBean.setUrlPatterns(urlPatterns);
        }
    }
}
