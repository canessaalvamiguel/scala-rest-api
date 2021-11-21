package controllers.api

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.{Todo, TodoForm}
import play.api.Logger
import services.TodoService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

@Singleton
class TodoController @Inject()(cc : ControllerComponents, todoService: TodoService) extends AbstractController(cc){

  implicit val todoFormat = Json.format[Todo]
  val logger: Logger = Logger(this.getClass())

  def getAll() = Action.async { implicit request: Request[AnyContent] =>
    todoService.listAllItems map { items =>
      Ok(Json.toJson(items))
    }
  }

  def getById(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    todoService.getItem(id) map {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound("Item not found")
    }
  }

  def add() = Action.async { implicit request: Request[AnyContent] =>
    TodoForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error! Invalid data received"))
      },
      data => {
        val newTodoItem = Todo(0, data.name, data.isComplete)
        todoService.addItem(newTodoItem).map {
          case Success(value) => Ok(value)
          case Failure(exception) =>
            logger.error(s"Error occurred while saving data, exception: ${exception}")
            BadRequest(s"Something went wrong. Please try again later.")
        }
      })
  }

  def update(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    TodoForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error! Invalid data received\""))
      },
      data => {
        val todoItem = Todo(id, data.name, data.isComplete)
        todoService.updateItem(todoItem).map{
          case Success(value) => Ok(value)
          case Failure(exception) =>
            logger.error(s"Error occurred while updating data, exception: ${exception}")
            BadRequest(s"Something went wrong. Please try again later.")
        }
      })
  }

  def delete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    todoService.deleteItem(id) map {
      case Success(value) => Ok(value)
      case Failure(exception) =>
        logger.error(s"Error occurred while deleting data, exception: ${exception}")
        BadRequest(s"Something went wrong. Please try again later.")
    }
  }
}
