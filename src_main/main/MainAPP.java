package main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "src_main.main")
@SpringBootApplication
public class MainAPP {

    public static void main(String[] args) {
        SpringApplication.run(MainAPP.class, args);
    }
}