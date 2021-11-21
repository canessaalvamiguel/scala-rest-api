package actors

import akka.pattern.pipe
import models.{Todo}
import todo.{TodoActor, TodoManager}

object TodoStorageActor {

  sealed trait StorageMessage

  case class StoreItem(item: Todo)
      extends StorageMessage

  case class RemoveItem(id: Long)
      extends StorageMessage

  case class UpdateItem(item: Todo)
      extends StorageMessage

  case class GetItem(id: Long)
      extends StorageMessage

  case class ListAllItems()
      extends StorageMessage
}

class TodoStorageActor(todoManager: TodoManager) extends TodoActor {
  import actors.TodoStorageActor._
  import context.dispatcher

  override def handleMessage: Receive = {
    case StoreItem(item) =>
      todoManager.addItem(item) pipeTo sender

    case RemoveItem(id) =>
      todoManager.deleteItem(id) pipeTo sender

    case UpdateItem(item) =>
      todoManager.updateItem(item) pipeTo sender

    case GetItem(id) =>
      todoManager.getItem(id) pipeTo sender

    case ListAllItems() =>
      todoManager.listAllItems pipeTo sender
  }
}
