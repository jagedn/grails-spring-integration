package proxyweb

import grails.test.mixin.integration.Integration
import spock.lang.Shared
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@Integration
class ApiServiceSpec extends Specification {

    ApiService apiService

    @Shared  LoginResponseBean loginBean

    def setup() {
    }

    def cleanup() {
    }

    void "test login #username with #password"() {
        when:
            loginBean =  apiService.validateUser(username, password)
        then:
            loginBean.access_token
        where:
        username    | password
        'jorge'  | 'aguilera'
    }

    void "test dashboard with #documentId is #title"(){
        given: "un dashboard"
            DashboardBean bean = apiService.dashboard(loginBean.access_token, "$documentId")

        expect:
            bean.mainTitle.length()
            bean.mainTitle == title

        where:
        documentId | title
        1   | "Title 0"
        3   | "Title 2"

    }
}
