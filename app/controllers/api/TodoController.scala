package controllers.api

import models.Todo
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton
class TodoController @Inject()(cc : ControllerComponents) extends AbstractController(cc){

  implicit val todoFormat = Json.format[Todo]

  def getAll = Action{
    val todo = Todo(1, "item 1", false);
    Ok(Json.toJson(todo))
  }

}
