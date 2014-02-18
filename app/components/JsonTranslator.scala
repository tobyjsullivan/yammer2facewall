package components

import play.api.libs.json._
import models.Employee

object JsonTranslator {
  def translateJson(list: List[JsObject]): JsValue = {
    val employees = list.flatMap { j =>
		    val maybeFirstName = (j \ "first_name").asOpt[String]
		    val maybeLastName = (j \ "last_name").asOpt[String]
		    val maybeEmails = (j \ "contact" \ "email_addresses").as[List[JsObject]].map{e => (e \ "address").asOpt[String] }
		    val maybeRole = (j \ "job_title").asOpt[String].filter(_ != "")
		    
		    if(maybeFirstName.isDefined && maybeLastName.isDefined && maybeEmails.size > 0 && maybeEmails(0).isDefined) {
		      List(Employee(maybeFirstName.get, maybeLastName.get, maybeEmails(0).get, maybeRole))
		    } else {
		      List()
		    }
	      }
      
    val employeeJson = employees.map { _ match { 
      case e @ Employee(_, _, _, Some(_)) => Map("firstName" -> e.firstName, "lastName" -> e.lastName, "email" -> e.email, "role" -> e.role.get)
      case e: Employee => Map("firstName" -> e.firstName, "lastName" -> e.lastName, "email" -> e.email)
    }}.map {m => Json.toJson(m)}
    val setJson = Json.toJson(employeeJson)
    Json.toJson(Map("users" -> setJson))
  }
}