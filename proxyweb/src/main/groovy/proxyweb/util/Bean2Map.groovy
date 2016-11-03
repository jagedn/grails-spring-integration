package proxyweb.util

import groovy.transform.CompileStatic


/**
 * Created by jorge on 3/11/16.
 */
// Ejemplo de un handler. Extrae las properties y las pasa como Map, ideoneo para enviar como json
@CompileStatic
class Bean2Map {

    public Bean2Map(){

    }

   public Map extractProperties(Object payload) {
        payload.properties
    }
}
