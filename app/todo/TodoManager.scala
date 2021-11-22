package todo

import com.google.inject.Inject
import models.{Todo, TodoListModel}

import javax.inject.Singleton
import scala.concurrent.Future
import scala.util.Try

@Singleton
class TodoManager @Inject() (items: TodoListModel) {

  def addItem(item: Todo): Future[Try[String]] = {
    items.add(item)
  }

  def deleteItem(id: Long): Future[Try[String]] = {
    items.delete(id)
  }

  def updateItem(item: Todo): Future[Try[String]] = {
    items.update(item)
  }

  def getItem(id: Long): Future[Option[Todo]] = {
    items.get(id)
  }

  def listAllItems: Future[Option[Seq[Todo]]] = {
    items.listAll
  }
}