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
import proxyweb.util.JsonFromPayload

/**
 * Created by jorge on 31/10/16.
 */
@Configuration
@IntegrationComponentScan("proxyweb")
class FlowsConfiguration {

    @Value('${api.authentication.url}')
    String authenticationEndpoint

    @Value('${api.documents.url}')
    String documentsEndpoint

    static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser()

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

    // Ejemplo de un handler. Extrae las properties y las pasa como Map, ideoneo para enviar como json
    GenericHandler bean2Map(){
        return new GenericHandler<Object>(){
            Object handle(Object payload, Map<String, Object> headers){
                payload.properties
            }
        }
    }

    // Ejemplo de un handler para tracear a nuestra manera. Seguro que existe otro mejor
    GenericHandler printlnHandler(){
        new GenericHandler() {
            @Override
            Object handle(Object payload, Map headers) {
                println "the payload $payload"
                payload
            }
        }
    }

    // LoginBean -> Json -> http -> return
    @Bean
    public IntegrationFlow loginFlow() throws URISyntaxException{
        return IntegrationFlows.from("login.input")
                .handle(bean2Map())
                .handle(printlnHandler())
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
                .handle(bean2Map())
                .handle(validateTokenHandler())
                .get()
    }

    // CommandBean -> validate token -> http resource -> .... otros recursos -> join todos en un dashaboard ->return
    @Bean
    public IntegrationFlow dashboardFlow() throws URISyntaxException{

        return IntegrationFlows.from("dashboard.input")
                .enrichHeaders( {s-> s.header('Content-Type','application/json') })
                .handle(documentsHandler())
                .transform(String.class, new JsonFromPayload())
                .transform(LazyMap.class, new AbstractPayloadTransformer<LazyMap, DashboardBean>() {
                    @Override
                    protected DashboardBean transformPayload(LazyMap payload)throws Exception{
                        new DashboardBean(mainTitle: payload.title)
                    }
                })
                .get()
    }
}
