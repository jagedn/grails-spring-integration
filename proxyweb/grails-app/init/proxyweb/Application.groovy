package proxyweb

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.integration.config.EnableIntegration

@ComponentScan(basePackages = ['proxyweb.si'])
@EnableIntegration
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}