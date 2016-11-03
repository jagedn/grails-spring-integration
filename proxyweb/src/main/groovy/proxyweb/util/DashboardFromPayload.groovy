package proxyweb.util

import groovy.transform.CompileStatic
import proxyweb.DashboardBean

/**
 * Created by jorge on 3/11/16.
 */
@CompileStatic
class DashboardFromPayload  {

    public static DashboardBean fromMap(Map payload)throws Exception{
        new DashboardBean(mainTitle: "$payload.title")
    }
}
