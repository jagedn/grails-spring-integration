package proxyweb.util

import groovy.json.JsonSlurper
import org.springframework.integration.transformer.AbstractPayloadTransformer

/**
 * Created by jorge on 6/10/16.
 */
class JsonFromPayload extends AbstractPayloadTransformer<String, Object>{
    @Override
    protected Object transformPayload(String payload)throws Exception{
        JsonSlurper slurper = new JsonSlurper()
        def json = slurper.parseText(payload)
        json
    }
}
