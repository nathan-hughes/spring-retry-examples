package ndhtest;

import spock.lang.Specification
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.beans.factory.annotation.Autowired
//import org.spockframework.spring.SpringBean
import org.springframework.retry.RetryContext
import org.springframework.retry.support.RetrySynchronizationManager

//@SpringBootTest
class HappyPathSpec extends Specification {

//    @SpringBean
    RandomNumberService randomNumberService = Stub(RandomNumberService)

    RetryContext retryContext = Stub()

//    @Autowired
    MyRetryableService myRetryableService = new MyRetryableService(randomNumberService)

    def "happyPath"() {

        given:
        randomNumberService.randomNumber() >> 1
        retryContext.retryCount >> 0
        RetrySynchronizationManager.register(retryContext)

        when:
        int result = myRetryableService.doStuff('happypathtest')

        then:
        result == 1
    }

    def "retryableFailure"() {
        given:
        randomNumberService.randomNumber() >> 2
        retryContext.retryCount >> 0
        RetrySynchronizationManager.register(retryContext)
 
        when:
        myRetryableService.doStuff('retryableFailureTest')

        then:
        thrown RetryableException 
    }
}
