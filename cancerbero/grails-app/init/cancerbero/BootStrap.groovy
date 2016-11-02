package cancerbero

import com.yourapp.Role
import com.yourapp.User
import com.yourapp.UserRole

class BootStrap {

    def init = { servletContext ->
        User.withTransaction {
            Role role = new Role(authority:'ROLE_USER')
            role.save(flush: true)

            User user = new User(username:'jorge', password:'aguilera')
            user.save(flush: true)

            UserRole.create(user, role, true)
        }
    }
    def destroy = {
    }
}
