package proxyweb

import grails.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired

@Transactional
class ApiService {

    @Autowired
    Api api

    LoginResponseBean validateUser( String login, String password) {
        api.login(new LoginBean(username:login,password:password))
    }


    DashboardBean dashboard( String token, String documentId ){

        CommandBean cmd = new CommandBean(token:token, action:'dashboard', param:documentId)
        LoginResponseBean loginResponseBean = api.validateLogin(cmd)
        println "El token es valido $loginResponseBean.username"

        DashboardBean ret = api.buildDashboard(cmd )
        println "El servicio ha obtenido un dashboard $ret.mainTitle"
        ret
    }
}
