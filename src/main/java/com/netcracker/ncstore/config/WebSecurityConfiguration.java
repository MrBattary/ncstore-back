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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
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
    private final LogoutSuccessHandler logoutSuccessHandler;

    public WebSecurityConfiguration(final IJwtTokenService jwtTokenService,
                                    final AccessDeniedHandler accessDeniedHandler,
                                    final LogoutSuccessHandler logoutSuccessHandler) {
        this.jwtTokenService = jwtTokenService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.formLogin().disable();
        //logoutSuccessHandler() and implement LogoutSuccessHandler
        http.logout().logoutUrl("/signout").invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/signup/**", "/signin").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/products").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/products/{\\d+}").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/cart").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/cart").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, "/cart/{\\d+}").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/category").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/person/info/{\\d+}").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/company/info/{\\d+}").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/cart").hasAuthority("CUSTOMER")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/products/{\\d+}/detailed").hasAuthority("SUPPLIER")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/products/{\\d+}").hasAuthority("SUPPLIER")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, "/products/{\\d+}").hasAuthority("SUPPLIER")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    public void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new JwtAuthenticationProvider(jwtTokenService));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall getHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    @Bean
    @Primary
    public IJwtTokenService jwtTokenService(final JwtSettings settings) {
        return new JsonJwtTokenService(settings);
    }
}
