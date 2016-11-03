package proxyweb.si

import groovy.json.internal.LazyMap
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.http.HttpMethod
import org.springframework.integration.annotation.IntegrationComponentScan
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.dsl.channel.MessageChannels
import org.springframework.integration.dsl.support.GenericHandler
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler
import org.springframework.integration.transformer.AbstractPayloadTransformer
import org.springframework.messaging.MessageHandler
import org.springframework.messaging.PollableChannel
import proxyweb.DashboardBean
import proxyweb.LoginBean
import proxyweb.LoginResponseBean
import proxyweb.util.DashboardFromPayload
import proxyweb.util.JsonFromPayload
/**
 * Created by jorge on 3/11/16.
 */
@Configuration
@IntegrationComponentScan("proxyweb")
class SystemConfiguration {

    @Bean
    public IntegrationFlow logginFlow(){
        return IntegrationFlows.from("logging").handle( new GenericHandler() {
            @Override
            Object handle(Object payload, Map headers) {
                System.out.println payload
            }
        }).get()
    }
}
