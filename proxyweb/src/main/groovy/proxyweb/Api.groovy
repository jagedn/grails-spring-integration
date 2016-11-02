package proxyweb

import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway

/**
 * Created by jorge on 31/10/16.
 */
@MessagingGateway(name="apiGateway")
interface Api {

    @Gateway(requestChannel = "login.input" )
    LoginResponseBean login( LoginBean loginBean );


    @Gateway(requestChannel = "login.validate" )
    LoginResponseBean validateLogin( CommandBean commandBean);

    @Gateway(requestChannel = "dashboard.input" )
    DashboardBean buildDashboard( CommandBean commandBean );


}