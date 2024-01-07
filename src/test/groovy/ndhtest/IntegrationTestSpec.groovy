package ndhtest

import spock.lang.Specification
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.spockframework.spring.SpringBean
import spock.lang.Subject

@SpringBootTest(classes = [MyRetryableService, RandomNumberService])
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
}
