// package ch.uzh.ifi.hase.soprafs21;

// import org.hibernate.annotations.common.util.impl.LoggerFactory;
// import org.jboss.logging.Logger;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import ch.uzh.ifi.hase.soprafs21.entity.User;
// import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

// @Configuration
// public class LoadDatabase {
// private static final Logger log = LoggerFactory.logger(LoadDatabase.class);

// @Bean
// CommandLineRunner initDatabase(UserRepository repo) {
// return args -> {
// log.info("Preloading " + repo.save(new User()));
// log.info("Preloading " + repo.save(new User()));
// };
// }
// }
