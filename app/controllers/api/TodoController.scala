package controllers.api

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.{Todo, TodoForm}
import play.api.data.FormError

import services.TodoService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class TodoController @Inject()(cc : ControllerComponents, todoService: TodoService) extends AbstractController(cc){

  implicit val todoFormat = Json.format[Todo]

  def getAll() = Action.async { implicit request: Request[AnyContent] =>
    todoService.listAllItems map { items =>
      Ok(Json.toJson(items))
    }
  }

}
