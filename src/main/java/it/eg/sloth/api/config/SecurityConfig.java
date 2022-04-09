package it.eg.sloth.api.config;

import it.eg.sloth.api.filter.AuthenticationJwtCertFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] WHITELIST = {
            // -- swagger ui
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml"
    };

    public static final String BASE_URI = "/api/v1/document/**";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, BASE_URI).permitAll()
//                .antMatchers(HttpMethod.PUT, BASE_URI).hasAnyAuthority(RULE_WRITER, RULE_ADMIN)
                .antMatchers(HttpMethod.POST, "/api/v1/security/login").permitAll()
//                .antMatchers(HttpMethod.DELETE, BASE_URI).hasAuthority(RULE_ADMIN)
                .antMatchers(WHITELIST).permitAll()
                //All
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthenticationJwtCertFilter(authenticationManager(), "public_key_jwt.pem"));
    }


}