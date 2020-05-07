package com.kronos.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;
		
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select TEMPUSER, PASSWORD, STATUS from T_USER where TEMPUSER=?")
				.authoritiesByUsernameQuery("SELECT USER_ID, ROLE_NAME AS ROLE FROM  T_USER_ROLE, T_ROLE WHERE "
						+"USER_ID= ?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http 

		.authorizeRequests()
		
		 
				// Los recursos estáticos no requieren autenticación
				.antMatchers("/error/**","/push/**","/images/**","/.well-known/**","/bootstrap/**", "/sw.js","/js/**", "/css/**","/push/register","/api/accords/uploadPdf/**","/icon.jpg","icon.jpg"
						,"/forgotPassword/**","/api/password/**").permitAll()
				// Las vistas públicas no requieren autenticación
				.antMatchers("/push/**","/forgotPassword/**","/accords/list/**","/images/**","/","/push/register","/api/accords/**","/api/notifications/**","/accords/edit/**",
						"/api/accords/uploadPdf/**","/icon.jpg","/api/accords/updateUrl/**").permitAll()
				// Todas las demás URLs de la Aplicación requieren autenticación
				
				.antMatchers("/accords/addAccord/**","/accords/editAccord/**").hasAnyAuthority("Concejo Municipal")
				.antMatchers("/townHall/**").hasAuthority("Secretaria de Alcaldia")
				.anyRequest().authenticated()
				//.and()
				//.csrf().ignoringAntMatchers("/forgotPassword/**","/api/password/**")
				
				.and()
				// El formulario de Login no requiere autenticacion
				.formLogin().loginPage("/login").failureUrl("/login-error")
				
				.permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/forbidden").and()

			
		.sessionManagement()
	    .maximumSessions(1)
	     .expiredUrl("/logout").and();

	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	


}