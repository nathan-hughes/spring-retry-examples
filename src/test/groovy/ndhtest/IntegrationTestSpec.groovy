package ndhtest

import spock.lang.Specification
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.spockframework.spring.SpringBean
import spock.lang.Subject

// the classes attrib on this annotation was causing the problem
// @SpringBootTest(classes = [MyRetryableService, RandomNumberService])
@SpringBootTest 
class IntegrationTestSpec extends Specification {

    @SpringBean
    RandomNumberService randomNumberService = Stub(RandomNumberService)

    @Subject
    @Autowired
    MyRetryableService myRetryableService

    def "test success the first time"() {
        given:
        randomNumberService.randomNumber() >> 1

        when:
        int result = myRetryableService.doStuff('hello')

        then:
        result == 1
    }

    def "test one retry"() {
        given:
        int count = 0
        def numbers = [2, 1]
        randomNumberService.randomNumber() >> { numbers[count++] }

        when:
        int result = myRetryableService.doStuff('hello again')

        then:
        result == 1
    }

    def "check that nonrecoverable exception is not caught"() {
        given:
        randomNumberService.randomNumber() >> { throw new NullPointerException() }

        when:
        int result = myRetryableService.doStuff('npe test')

        then:
        thrown(NullPointerException)
    }
}
