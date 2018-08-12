package com.marceltbr.okready;

import com.marceltbr.okready.entities.AppUser;
import com.marceltbr.okready.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;



@SpringBootApplication
public class OkreadyApplication extends WebMvcConfigurerAdapter{


	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		/**
		 * Redirecting to homepage if the user refreshes the page
		 */

		//registry.addRedirectViewController("/new_semester", "/");
		//registry.addRedirectViewController("/profile", "/");


		registry.addViewController("/").setViewName("404");
	}

	public static void main(String[] args) {
		SpringApplication.run(OkreadyApplication.class, args);
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
			}
		};
	}


	@Bean
	public CommandLineRunner initData(AppUserRepository appUserRepository) {

		return (args) -> {


			AppUser admin = new AppUser("admin123", "admin123");
			appUserRepository.save(admin);
		};
	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {


	@Autowired
	AppUserRepository appUserRepo;

//	@Bean
//	public PasswordEncoder passwordEncoder(){
//		PasswordEncoder encoder = new BCryptPasswordEncoder();
//		return encoder;
//	}

	@Bean
	UserDetailsService UserDetailsService() {

		return new UserDetailsService() {


			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				List<AppUser> users = appUserRepo.findByUsername(username);

				System.out.println("Username: " + username);

				if (!users.isEmpty()) {
					AppUser user = users.get(0);
					System.out.println("users is not empty");
					return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					System.out.println("something happened");
					throw new UsernameNotFoundException("Unknown user: " + username);
				}
			}
		};

	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.antMatchers("/", "/index.html", "/app/login", "/app/logout", "/js/**", "/img/**", "/css/**",
						"/bower_components/**", "/partials/home.html").permitAll()
				.antMatchers("/**", "/partials/**", "/api/**").hasAnyAuthority("USER")
				.anyRequest().fullyAuthenticated();


		http.formLogin()
				.loginPage("/app/login")
				.permitAll();
                /*.anyRequest().fullyAuthenticated().
                and().httpBasic();*/

		http.logout().logoutUrl("/app/logout")
		.permitAll();

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	/**
	 * Takes a request as an argument. Gets the session from the request. If it exists, its Auth attributes get removed
	 */
	private void clearAuthenticationAttributes(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}


}