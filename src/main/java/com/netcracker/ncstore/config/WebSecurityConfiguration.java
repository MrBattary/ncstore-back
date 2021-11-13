package com.netcracker.ncstore.config;

import com.netcracker.ncstore.security.IJwtTokenService;
import com.netcracker.ncstore.security.JsonJwtTokenService;
import com.netcracker.ncstore.security.JwtSettings;
import com.netcracker.ncstore.security.filter.AJwtAuthFilter;
import com.netcracker.ncstore.security.filter.HeaderJwtAuthFilter;
import com.netcracker.ncstore.security.provider.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final IJwtTokenService jwtTokenService;
    private final AccessDeniedHandler accessDeniedHandler;

    /**
     * Constructor
     *
     * @param jwtTokenService - JWT Token Service
     */
    public WebSecurityConfiguration(final IJwtTokenService jwtTokenService,
                                    final AccessDeniedHandler accessDeniedHandler) {
        this.jwtTokenService = jwtTokenService;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.formLogin().disable();
        http.logout().disable();
        http.sessionManagement().disable();
        http.requestCache().disable();
        http.anonymous();

        RequestMatcher loginPageMatcher = new AntPathRequestMatcher("/signin");
        RequestMatcher notLoginPageMatcher = new NegatedRequestMatcher(loginPageMatcher);
        AJwtAuthFilter authFilter = new HeaderJwtAuthFilter(notLoginPageMatcher);
        http.addFilterBefore(authFilter, FilterSecurityInterceptor.class);

        /*
         * Examples:
         * 1) Two request with the same address, but different HTTP methods:
         * .authorizeRequests().antMatchers(HttpMethod.GET, "/request").permitAll()
         * .and
         * .authorizeRequests().antMatchers(HttpMethod.POST, "/request").authenticated()
         *
         * 2) One authority:
         * .authorizeRequests().antMatchers("/request").hasAuthority("ADMIN")
         *
         * 3) Any of authority:
         * .authorizeRequests().antMatchers("/request").hasAnyAuthority("ADMIN", "SUPPLIER")
         *
         * 4) All requests from one root: /request/first, /request/second, /request/third
         * .authorizeRequests().antMatchers("/request/**").permitAll()
         */

        http
                .authorizeRequests().antMatchers("/signup/**", "/signin").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/products").permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new JwtAuthenticationProvider(jwtTokenService));
    }

    /**
     * Abstract factory for the password encoder
     *
     * @return - PasswordEncoder realization
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall getHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    /**
     * Abstract factory for the JWT token service
     *
     * @param settings - settings
     * @return - JwtTokenService realization
     */
    @Bean
    @Primary
    public IJwtTokenService jwtTokenService(final JwtSettings settings) {
        return new JsonJwtTokenService(settings);
    }
}
