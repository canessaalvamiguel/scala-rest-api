package services

import com.google.inject.Inject
import models.{Todo, TodoList}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

class TodoService @Inject() (items: TodoList) {

  def addItem(item: Todo): Future[Try[String]] = {
    items.add(item)
  }

  def deleteItem(id: Long): Future[Int] = {
    items.delete(id)
  }

  def updateItem(item: Todo): Future[Int] = {
    items.update(item)
  }

  def getItem(id: Long): Future[Option[Todo]] = {
    items.get(id)
  }

  def listAllItems: Future[Seq[Todo]] = {
    items.listAll
  }
}