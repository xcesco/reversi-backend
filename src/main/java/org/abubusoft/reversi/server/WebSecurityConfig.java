package org.abubusoft.reversi.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.abubusoft.reversi.server.web.AngularResourceResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
//	}

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //@formatter:off
    registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/META-INF/resources/")
            .resourceChain(true)
            .addResolver(new AngularResourceResolver());
    //@formatter:on

  }

//  @Bean
//  public Jackson2ObjectMapperBuilder objectMapperBuilder() {
//    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//    // builder.serializationInclusion(JsonInclude.Include.NON_NULL);
//    return builder.build();
//  }


//	@Autowired
//	private JwtAuthenticationEntryPoint unauthorizedHandler;
//
//	@Bean
//	public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
//		return new JwtAuthenticationFilter();
//	}

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
    http
            .httpBasic().disable()
            .cors().disable()
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            //	.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
            //.exceptionHandling()
            //	.authenticationEntryPoint(unauthorizedHandler)
            //.and()
            .authorizeRequests()
            //	parte angular
            .antMatchers("/public/**")
            .permitAll()
            .and()

            .authorizeRequests()
            .antMatchers(WebSocketConfig.WE_ENDPOINT + "/**")
            .permitAll()

            // configurazione interfaccia swagger
            .antMatchers("/api-docs")
            .permitAll()

            // servizi WEB public
            .antMatchers("/api/v1/**")
            .permitAll()

            // servizi WEB public
            .antMatchers("/api/v1/public/**")
            .permitAll()

            // servizi web secured
            .antMatchers("/api/v1/secured/**")
            .authenticated();

  }

  @Bean
  @Override
  public UserDetailsService userDetailsService() {
    UserDetails user =
            User.builder()
                    .username("user")
                    .password("password")
                    .roles("USER")
                    .build();

    return new InMemoryUserDetailsManager(user);
  }

}