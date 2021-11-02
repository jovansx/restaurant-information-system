package akatsuki.restaurantsysteminformation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestaurantSystemInformationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantSystemInformationApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner populateDatabase(PostRepository postRepository) {
//        return (args) -> {
//            postRepository.save(new Post("Title1", "Content1"));
//            postRepository.save(new Post("Title2", "Content2"));
//            postRepository.save(new Post("Title2", "Content3"));
//            postRepository.save(new Post("Title3", "Content4"));
//            log.info("Database is populated");
//        };
//    }
}
