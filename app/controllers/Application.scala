package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.Play.current
import models._
import scala.io._
import components.JsonTranslator

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Check out /employees."))
  }
  
  def employees = Action {
    val source = Source.fromFile("yammer.json")
    val content = source.mkString
    
    val json = Json.parse(content)
    
    val list = json.as[List[JsObject]]
    
    val outJson = JsonTranslator.translateJson(list)
      
    Ok(Json.stringify(outJson))
      .as(JSON)
      .withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
  }

}