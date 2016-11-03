package proxyweb.util

import groovy.json.JsonSlurper
import groovy.transform.CompileStatic

/**
 * Created by jorge on 6/10/16.
 */
@CompileStatic
class JsonFromPayload{

    public Object transform(String payload)throws Exception{
        JsonSlurper slurper = new JsonSlurper()
        def json = slurper.parseText(payload)
        json
    }
}
