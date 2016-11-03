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
import org.springframework.integration.dsl.support.GenericHandler
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler
import org.springframework.integration.transformer.AbstractPayloadTransformer
import org.springframework.messaging.MessageHandler
import proxyweb.DashboardBean
import proxyweb.LoginBean
import proxyweb.LoginResponseBean
import proxyweb.util.Bean2Map
import proxyweb.util.JsonFromPayload
/**
 * Created by jorge on 3/11/16.
 */
@Configuration
@IntegrationComponentScan("proxyweb")
class SecurityConfiguration {


    @Value('${api.authentication.url}')
    String authenticationEndpoint

    // spring security rest en otra maquina
    MessageHandler loginHandler() {
        HttpRequestExecutingMessageHandler httpHandler =
                new HttpRequestExecutingMessageHandler("$authenticationEndpoint/api/login");
        httpHandler.expectedResponseType = LoginResponseBean.class
        return httpHandler;
    }

    // spring security rest en otra maquina
    MessageHandler validateTokenHandler() {
        HttpRequestExecutingMessageHandler httpHandler =
                new HttpRequestExecutingMessageHandler("$authenticationEndpoint/api/validate");
        httpHandler.expectedResponseType = LoginResponseBean.class
        return httpHandler;
    }


    // LoginBean -> Json -> http -> return
    @Bean
    public IntegrationFlow loginFlow() throws URISyntaxException{
        return IntegrationFlows.from("login.input")
                .handle( new Bean2Map() )
                .wireTap("logging")
                .enrichHeaders( {s-> s.header('Content-Type','application/json') })
                .handle(loginHandler())
                .get()
    }

    // Token -> http -> return
    @Bean
    public IntegrationFlow validateFlow() throws URISyntaxException{

        return IntegrationFlows.from("login.validate")
                .enrichHeaders( {s->
            s.header('Content-Type','application/json')
            s.headerExpression('Authorization','"Bearer "+payload.token')
        })
                .handle(new Bean2Map())
                .handle(validateTokenHandler())
                .get()
    }

}
