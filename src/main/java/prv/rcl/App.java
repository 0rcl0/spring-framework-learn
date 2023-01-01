package prv.rcl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication
public class App {
    public static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        log.info("Hello World!");
        SpringApplication.run(App.class,args);
    }
}
