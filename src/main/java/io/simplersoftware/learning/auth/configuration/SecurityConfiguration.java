package io.simplersoftware.learning.auth.configuration;

//import com.paperwork.paperworkapp.auth.exception.AuthenticationEntryPointException;
//import com.paperwork.paperworkapp.auth.exception.RestAccessDeniedException;
import io.simplersoftware.learning.auth.util.AuthRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
//    @Autowired
//    private AuthenticationEntryPointException authenticationEntryPoint;
//    @Autowired
//    private RestAccessDeniedException restAccessDenied;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager( ) throws Exception{
        return authenticationManagerBean();
    }

    @Bean
    public AuthRequestFilter rsaAuthenticationFilter() {
        return new AuthRequestFilter();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            //.exceptionHandling()
           // .authenticationEntryPoint(authenticationEntryPoint)
           // .accessDeniedHandler(restAccessDenied)
           // .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/authenticate", "/register", "/publicKey").permitAll()
            .anyRequest().authenticated();

        http.addFilterBefore(rsaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}