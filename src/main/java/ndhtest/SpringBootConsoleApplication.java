package ndhtest;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
@Slf4j
@RequiredArgsConstructor
public class SpringBootConsoleApplication implements CommandLineRunner {

    private final MyRetryableService myRetryableService;

    public static void main(String ... args) throws Exception {
        log.info("starting application");
        SpringApplication.run(SpringBootConsoleApplication.class);
        log.info("finishing application");
    }

    @Override
    public void run(String ... args) {
        log.info("executing command line runner");
        try {
            int i = myRetryableService.doStuff("hello");
            log.info("myRetryableService returned {}", i);
        } catch (Exception e) {
            log.error("caught exception", e);
        }
    }
}

