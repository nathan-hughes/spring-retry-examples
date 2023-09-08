package ndhtest;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyRetryableService {

    private final RandomNumberService randomNumberService;
  
    /*
    Some exceptions will be recoverable, some will not be. Not differentiating
    between recoverable and non-recoverable cases would waste time and resources.
    
    Non-Recoverable exceptions need to be the default case since there will always
    be the chance of programmer errors such as NPEs, IllegalArgumentExceptions, etc.
     
    Recoverable exceptions can be wrapped in a RetryableException so that they 
    explicitly get chosen for recovery.
    */
    @Retryable(value = {RetryableException.class}, maxAttempts = 2, backoff = @Backoff(delay=100)) 
    public int doStuff(String s) {

        log.info("doing stuff with {}, try = {}", s, RetrySynchronizationManager.getContext().getRetryCount() + 1);
        int i = randomNumberService.randomNumber();

        // simulate having something bad happen that is recoverable
        if (i % 2 == 0) {
            String issue = "oops";
            log.warn("condition = {}", issue);
            throw new RetryableException(issue);
        }

        // simulate having something bad happen that is not recoverable
        if (i % 5 == 0) {
            String issue = "ohnoes";
            log.warn("condition = {}", issue);
            throw new IllegalArgumentException(issue);
        }

        return i;
    }

    /*
    Provides default value and writes to log once retries are exhausted
    */
    @Recover
    public int recover(RetryableException e, String s) {

        log.info("in recover for RetryableException, s is {}", s);
        return -1;
    }

    /*
    Need to provide this so that non-recoverable exception doesn't get wrapped in an ExhaustedRetriesException
    with message that no recover method was found
    */
    @Recover
    public int recover(RuntimeException e, String s) {

        log.info("in recover for RuntimeException, s is {}", s);
        throw e;
    }
}
