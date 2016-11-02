package documents

import com.yourapp.Document

class BootStrap {

    def init = { servletContext ->

        (0..10).each{
            new Document(title:"Title $it", pages:it).save(flush:true)
        }

    }
    def destroy = {
    }
}
