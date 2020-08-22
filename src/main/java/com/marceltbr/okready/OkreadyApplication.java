package com.marceltbr.okready;

import com.marceltbr.okready.entities.*;
import com.marceltbr.okready.entities.motivator.*;
import com.marceltbr.okready.entities.okrs.*;
import com.marceltbr.okready.repositories.*;
import com.marceltbr.okready.repositories.motivator.*;
import com.marceltbr.okready.repositories.okrs.*;
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
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static com.marceltbr.okready.HelperFunctions.*;


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

//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//
//		/**
//		 * Redirecting to homepage if the user refreshes the page
//		 */
//
//		//registry.addRedirectViewController("/new_semester", "/");
//		//registry.addRedirectViewController("/profile", "/");
//
//		//		registry.addViewController("/error").setViewName("error");
//
//		//registry.addViewController("/").setViewName("404");
//	}

//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer() {
//
//		return new EmbeddedServletContainerCustomizer() {
//			@Override
//			public void customize(ConfigurableEmbeddedServletContainer container) {
//
//				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED,
//						"/401.html");
//				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND,
//						"/404.html");
//				ErrorPage error500Page = new ErrorPage(
//						HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
//				ErrorPage error505Page = new ErrorPage(
//						HttpStatus.HTTP_VERSION_NOT_SUPPORTED, "/505.html");
//				ErrorPage error506Page = new ErrorPage(
//						HttpStatus.METHOD_NOT_ALLOWED, "/405.html");
//				container.addErrorPages(error401Page, error404Page,
//						error500Page, error505Page, error506Page);
//			}
//		};
//	}




	public static void main(String[] args) {
		SpringApplication.run(OkreadyApplication.class, args);
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED,
						"/index.html");
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND,
						"/index.html");
				ErrorPage error500Page = new ErrorPage(
						HttpStatus.INTERNAL_SERVER_ERROR, "/index.html");
				ErrorPage error505Page = new ErrorPage(
						HttpStatus.HTTP_VERSION_NOT_SUPPORTED, "/index.html");
				ErrorPage error506Page = new ErrorPage(
						HttpStatus.METHOD_NOT_ALLOWED, "/index.html");



				container.addErrorPages(error401Page, error404Page,
						error500Page, error505Page, error506Page);
			}
		};
	}




	@Bean
	public CommandLineRunner initData(AppUserRepository appUserRepository, YearRepository yearRepository,
									  YearSemesterRepository yearSemesterRepository, SemesterRepository semesterRepository,
									  ObjectiveRepository objectiveRepo, SemesterObjectiveRepository semesterObjectiveRepo,
									  ResultRepository resultRepo, ObjectiveResultRepository objectiveResultRepo,
									  AppUserYearRepository appUserYearRepo, QuoteRepository quoteRepository,
									  CategoryQuoteRepository categoryQuoteRepository,
									  CategoryRepository categoryRepository, MotivatorRepository motivatorRepository,
									  AppUserMotivatorRepository appUserMotivatorRepository) {

		return (args) -> {

			boolean isExistsAdmin = appUserRepository.existsByUsername("pinguso87");

			if(!isExistsAdmin) {
				//create admin user
				AppUser admin = new AppUser("pinguso87", "pinguso87");
				appUserRepository.save(admin);


				//create initial year and semester data
				Year year = new Year(2018);
				yearRepository.save(year);

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

				resultRepo.save(result1);
				resultRepo.save(result2);

				//create objectiveResults

				ObjectiveResult objRes1 = new ObjectiveResult(objective1, result1);
				ObjectiveResult objRes2 = new ObjectiveResult(objective1, result2);

				objectiveResultRepo.save(objRes1);
				objectiveResultRepo.save(objRes2);


				Motivator adminMotivator = makeMotivator(motivatorRepository);
				makeAppUserMotivator(adminMotivator, admin, appUserMotivatorRepository);


			}

			/** Guest User DATA **/


			boolean isExistsGuest = appUserRepository.existsByUsername("guest");

			if(!isExistsGuest) {
				//create Guest User
				AppUser guest = new AppUser("guest", "guest");
				appUserRepository.save(guest);

				//create initial year and semester data
				Year year2 = new Year(2019);
				yearRepository.save(year2);

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

				resultRepo.save(result11);
				resultRepo.save(result12);
				resultRepo.save(result13);

				//create objectiveResults
				ObjectiveResult objRes11 = new ObjectiveResult(objective2, result11);
				ObjectiveResult objRes12 = new ObjectiveResult(objective2, result12);
				ObjectiveResult objRes13 = new ObjectiveResult(objective2, result13);

				objectiveResultRepo.save(objRes11);
				objectiveResultRepo.save(objRes12);
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

				resultRepo.save(result21);
				resultRepo.save(result22);
				resultRepo.save(result23);

				//create objectiveResults
				ObjectiveResult objRes21 = new ObjectiveResult(objective3, result21);
				ObjectiveResult objRes22 = new ObjectiveResult(objective3, result22);
				ObjectiveResult objRes23 = new ObjectiveResult(objective3, result23);

				objectiveResultRepo.save(objRes21);
				objectiveResultRepo.save(objRes22);
				objectiveResultRepo.save(objRes23);


				/**
				 *
				 * Create guest user default motivator content
				 *
				 */

				String category_name1 = "Comfort Zone";
				String category_name2 = "Breaking It Down";
				String category_name3 = "Complaining/Excuses";
				makeCategory(category_name1, categoryRepository); makeCategory(category_name2, categoryRepository);
				makeCategory(category_name3, categoryRepository);

				Category category1 = makeCategory(category_name1, categoryRepository);
				String content1_1 = "To break out of your confort zone use your imagination to feel the painful consequences of not doing something you need to do";
				String content1_2 = "Sometimes our mind will serve us, but sometimes it will work against us";
				Quote quote1_1 = makeQuote(content1_1, quoteRepository); Quote quote1_2 = makeQuote(content1_2, quoteRepository);
				makeCategoryQuote(category1, quote1_1, categoryQuoteRepository);
				makeCategoryQuote(category1, quote1_2, categoryQuoteRepository);


				Category category2 = makeCategory(category_name2, categoryRepository);
				String content2_1 = "Plan your steps in a very specific way, so that you can move faster into achieving your goals";
				String content2_2 = "Better to take small steps in the right direction than to make a great leap forward only to stumble backwards";
				Quote quote2_1 = makeQuote(content2_1, quoteRepository); Quote quote2_2 = makeQuote(content2_2, quoteRepository);
				makeCategoryQuote(category2, quote2_1, categoryQuoteRepository);
				makeCategoryQuote(category2, quote2_2, categoryQuoteRepository);

				Category category3 = makeCategory(category_name3, categoryRepository);
				String content3_1 = "Invest your complaining into better things, like fixing the problem";
				String content3_2 = "Complaining does not achieve anything other than stop your progress";
				String content3_3 = "Stop complaining and start doing";
				Quote quote3_1 = makeQuote(content3_1, quoteRepository); Quote quote3_2 = makeQuote(content3_2, quoteRepository);
				Quote quote3_3 = makeQuote(content3_3, quoteRepository);
				makeCategoryQuote(category3, quote3_1, categoryQuoteRepository);
				makeCategoryQuote(category3, quote3_2, categoryQuoteRepository);
				makeCategoryQuote(category3, quote3_3, categoryQuoteRepository);


				Motivator guestMotivator = makeMotivator(motivatorRepository);
				makeAppUserMotivator(guestMotivator, guest, appUserMotivatorRepository);

			}


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

				//System.out.println("Username: " + username);

				if (!users.isEmpty()) {
					AppUser user = users.get(0);
					//System.out.println("users is not empty");
					return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					System.out.println("something happened. Unknown User");
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
						"/bower_components/**", "/fontawesome-free-5.8.1-web/**", "/home", "/partials/home.html", "/session_expired",
						"/partials/session_expired.html", "/favicon.ico", "/trophy_favicon/**", "/partials/error.html", "/show_error/**", "/errors/**").permitAll()
				.antMatchers("/**", "/api/**").hasAnyAuthority("USER")
				.anyRequest().fullyAuthenticated();


		http.formLogin()
				.loginPage("/app/login");


                /*.anyRequest().fullyAuthenticated().
                and().httpBasic();*/


		http.logout().logoutUrl("/app/logout");

		//http.exceptionHandling().accessDeniedPage("/errors/401"); //home

		//redirect to page when session expired
		http.sessionManagement()
		.maximumSessions(1).expiredUrl("/home");


		//				.and().invalidSessionUrl("/session_expired");

		//				.expiredSessionStrategy(new SessionInformationExpiredStrategy() {
		//			@Override
		//			public void onExpiredSessionDetected(SessionInformationExpiredEvent eventØ) throws IOException, ServletException {
		//				eventØ.getResponse().sendRedirect("/session_expired");
		//				return;
		//			}
		//		});


		// turn off checking for CSRF tokens
		http.csrf().disable();


			// if user is not authenticated, just send an authentication failure response
//			http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		//	handle exceptions
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> {

			System.out.println( ">>>Exception Message:" + exc.getMessage() );
			exc.printStackTrace();

//			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			MyErrorController errorContr = new MyErrorController();
			errorContr.myErrorHandler(req, res);

		});


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