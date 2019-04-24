package com.marceltbr.okready;

import com.marceltbr.okready.entities.*;
import com.marceltbr.okready.repositories.*;
import org.h2.tools.*;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;


@SpringBootApplication
public class OkreadyApplication extends WebMvcConfigurerAdapter{

	/**
	 * Start internal H2 server so we can query the DB from IDE
	 *
	 * @return H2 Server instance
	 * @throws SQLException
	 */
	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server h2Server() throws SQLException {
		return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
	}

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
	public CommandLineRunner initData(AppUserRepository appUserRepository, YearRepository yearRepository,
									  YearSemesterRepository yearSemesterRepository, SemesterRepository semesterRepository,
									  ObjectiveRepository objectiveRepo, SemesterObjectiveRepository semesterObjectiveRepo,
									  ResultRepository resultRepo, ObjectiveResultRepository objectiveResultRepo,
									  AppUserYearRepository appUserYearRepo) {

		return (args) -> {

			//create admin user
			AppUser admin = new AppUser("admin123", "admin123");
			appUserRepository.save(admin);

			//create initial year and semester data
			Year year = new Year(2018); yearRepository.save(year);

			AppUserYear appUserYear1 = new AppUserYear(year, admin);//link year to user
			appUserYearRepo.save(appUserYear1);

			Semester sem1 = new Semester(1, "1st");
			semesterRepository.save(sem1);
			YearSemester yearSemester = new YearSemester(year, sem1);
			yearSemesterRepository.save(yearSemester);

			//create objective
			Objective objective1 = new Objective("Do Sport", 150L);
			objectiveRepo.save(objective1);

			//create semesterObjectives
			SemesterObjective semObj1 = new SemesterObjective(sem1, objective1);
			semesterObjectiveRepo.save(semObj1);

			//create results
			Result result1 = new Result("Go Swimming", 10L, 1L);
			Result result2 = new Result("Go Jogging", 20L, 2L);

			resultRepo.save(result1); resultRepo.save(result2);

			//create objectiveResults

			ObjectiveResult objRes1 = new ObjectiveResult(objective1, result1);
			ObjectiveResult objRes2 = new ObjectiveResult(objective1, result2);

			objectiveResultRepo.save(objRes1);objectiveResultRepo.save(objRes2);

			/** Guest User DATA **/
			//create Guest User
			AppUser guest = new AppUser("guest", "guest");
			appUserRepository.save(guest);

			//create initial year and semester data
			Year year2 = new Year(2019); yearRepository.save(year2);

			AppUserYear appUserYear2 = new AppUserYear(year2, guest);//link year to user
			appUserYearRepo.save(appUserYear2);

			//create 1st semester of 2019
			Semester sem2 = new Semester(1, "1st");
			semesterRepository.save(sem2);
			YearSemester yearSemester2 = new YearSemester(year2, sem2);
			yearSemesterRepository.save(yearSemester2);

			//create objective
			Objective objective2 = new Objective("Do Sport", 120L);
			objectiveRepo.save(objective2);

				//create semesterObjectives
				SemesterObjective semObj2 = new SemesterObjective(sem2, objective2);
				semesterObjectiveRepo.save(semObj2);

				//create results
				Result result11 = new Result("Go Swimming", 10L, 5L);
				Result result12 = new Result("Go Jogging", 5L, 8L);
				Result result13 = new Result("Miss Training", -5L, 2L);

				resultRepo.save(result11); resultRepo.save(result12); resultRepo.save(result13);

				//create objectiveResults
				ObjectiveResult objRes11 = new ObjectiveResult(objective2, result11);
				ObjectiveResult objRes12 = new ObjectiveResult(objective2, result12);
				ObjectiveResult objRes13 = new ObjectiveResult(objective2, result13);

				objectiveResultRepo.save(objRes11);objectiveResultRepo.save(objRes12);
				objectiveResultRepo.save(objRes13);

			//create another objective
			Objective objective3 = new Objective("Increment Sales", 500L);
			objectiveRepo.save(objective3);

				//create semesterObjectives
				SemesterObjective semObj3 = new SemesterObjective(sem2, objective3);
				semesterObjectiveRepo.save(semObj3);

				//create results
				Result result21 = new Result("Sell one product/service", 10L, 25L);
				Result result22 = new Result("Make a comercial call to potential buyers.", 5L, 40L);
				Result result23 = new Result("Do a Social Media Ad", 10L, 2L);

				resultRepo.save(result21); resultRepo.save(result22); resultRepo.save(result23);

				//create objectiveResults
				ObjectiveResult objRes21 = new ObjectiveResult(objective3, result21);
				ObjectiveResult objRes22 = new ObjectiveResult(objective3, result22);
				ObjectiveResult objRes23 = new ObjectiveResult(objective3, result23);

				objectiveResultRepo.save(objRes21);objectiveResultRepo.save(objRes22);
				objectiveResultRepo.save(objRes23);






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
					//System.out.println("users is not empty");
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
				.antMatchers("/", "/index.html", "/app/login", "/app/logout", "/js/**", "/img/**", "/css/**", "/img/**",
						"/bower_components/**", "/fontawesome-free-5.8.1-web/**", "/home", "/partials/home.html", "/session_expired", "/partials/session_expired.html").permitAll()
				.antMatchers("/**", "/partials/**", "/api/**").hasAnyAuthority("USER")
				.anyRequest().fullyAuthenticated();


		http.formLogin()
				.loginPage("/app/login");


                /*.anyRequest().fullyAuthenticated().
                and().httpBasic();*/


		http.logout().logoutUrl("/app/logout");


		http.exceptionHandling().accessDeniedPage("/");

		//redirect to page when session expired
		http.sessionManagement()
		.maximumSessions(1).expiredUrl("/session_expired");
				//.and().invalidSessionUrl("/session_expired");


		// turn off checking for CSRF tokens
		http.csrf().disable();


		//let the front end handle this situation
			// if user is not authenticated, just send an authentication failure response
			//http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));



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