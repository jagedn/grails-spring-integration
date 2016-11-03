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
 * Created by jorge on 31/10/16.
 */
@Configuration
@IntegrationComponentScan("proxyweb")
class FlowsConfiguration {

    @Value('${api.documents.url}')
    String documentsEndpoint

    static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser()


    // recurso rest en otra maquina
    MessageHandler documentsHandler() {
        HttpRequestExecutingMessageHandler httpHandler =
                new HttpRequestExecutingMessageHandler("$documentsEndpoint/document/{documentId}");
        httpHandler.uriVariableExpressions = [
                'documentId' : EXPRESSION_PARSER.parseExpression('payload.param')
        ]
        httpHandler.expectedResponseType = String.class
        httpHandler.httpMethod = HttpMethod.GET
        return httpHandler;
    }

    // CommandBean -> validate token -> http resource -> .... otros recursos -> join todos en un dashaboard ->return
    @Bean
    public IntegrationFlow dashboardFlow() throws URISyntaxException{

        return IntegrationFlows.from("dashboard.input")
                .enrichHeaders( {s-> s.header('Content-Type','application/json') })
                .handle(documentsHandler())
                .wireTap('logging')
                .transform(new JsonFromPayload(),"transform")
                .transform(new DashboardFromPayload(),"fromMap")
                .get()
    }

}
