package services

import actors.TodoStorageActor
import actors.TodoStorageActor._
import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import com.google.inject.Inject
import models.{Todo, TodoListModel}
import todo.AkkaMessages.InvalidMessageException
import todo.TodoManager

import javax.inject.Singleton
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.MINUTES
import scala.util.Try

class TodoService @Inject() (itemsManager: TodoManager) {

  implicit val timeout = akka.util.Timeout(5, MINUTES)
  object TodoStorage{
    def props: Props = Props(new TodoStorageActor(itemsManager))
  }

  val system = ActorSystem("TodoActorSystem")
  val todoStorage = system.actorOf(TodoStorage.props, "TodoActorStorage")

  def addItem(item: Todo): Future[Try[String]] = {
    (todoStorage ?  StoreItem(item)).mapTo[Try[String]]
  }

  def deleteItem(id: Long): Future[Try[String]] = {
    (todoStorage ?  RemoveItem(id)).mapTo[Try[String]]
  }

  def updateItem(item: Todo): Future[Try[String]] = {
    (todoStorage ?  UpdateItem(item)).mapTo[Try[String]]
  }

  def getItem(id: Long): Future[Option[Todo]] = {
    (todoStorage ?  GetItem(id)).mapTo[Option[Todo]]
  }

  def listAllItems: Future[Option[Seq[Todo]]] = {
    (todoStorage ?  ListAllItems()).mapTo[Option[Seq[Todo]]]
  }




  //      items.listAll.map(elements => elements.map(element => element.copy(name = element.name + " holis")))

  //    def alterName(elements: Seq[Todo]) : Seq[Todo] = {
  //      elements.map(element =>
  //        element.copy(name = element.name + " holis2")
  //      )
  //    }
  //    for{
  //      elements <- items.listAll
  //      result = alterName(elements)
  //    }yield{
  //      result
  //    }
}