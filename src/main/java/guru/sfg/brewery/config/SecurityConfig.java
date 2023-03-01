package guru.sfg.brewery.config;

import guru.sfg.brewery.security.JpaUserDetailsService;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import guru.sfg.brewery.security.UrlParamAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public UrlParamAuthFilter urlParamAuthFilter(AuthenticationManager authenticationManager) {
        UrlParamAuthFilter filter = new UrlParamAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

        http.addFilterBefore(urlParamAuthFilter(authenticationManager()),
                        RestHeaderAuthFilter.class)
                .csrf().disable();

        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll()
                            .antMatchers("/", "/webjars/**", "/resources/**", "/beers", "/beers/find").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**").hasRole("ADMIN")
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/brewery/api/v1/breweries").hasAnyRole("CUSTOMER", "ADMIN")
                            .antMatchers("/brewery/breweries/**").hasAnyRole("CUSTOMER", "ADMIN");
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();

        //h2 console config
        http.headers().frameOptions().sameOrigin();
    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("123")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user2")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }

   // @Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(jpaUserDetailsService).passwordEncoder(passwordEncoder());

//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password("{bcrypt}$2a$10$jgX9q1QbTCzpN83.iK0ML.pOMIO1PKtTGL8RLxe4d5JE6BWE5R6PO")
//                .roles("ADMIN")
//                .and()
//                .withUser("user2")
//                .password("{sha256}86206824942f5de80cac66dc9fa41df8028f943a2b1c3b387028c798b6e11b902747a3d362e92485")
//                .roles("USER")
//                .and()
//                .withUser("scott")
//                .password("{bcrypt10}$2a$10$duCiEVFnY3sodCiHB2Iz2OGbLOu2kMgpEHghdgWHFwZp9t1oZme.C")
//                .roles("CUSTOMER");
//
   // }
}
