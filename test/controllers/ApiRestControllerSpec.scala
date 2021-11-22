package controllers

import controllers.api.TodoController
import models.{Todo, TodoTableDef}
import org.scalatest.BeforeAndAfterAll

import java.time.LocalDateTime
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._
import slick.driver.MySQLDriver.api._

class ApiRestControllerSpec() extends PlaySpec with GuiceOneAppPerTest with Injecting with BeforeAndAfterAll  {

  var idItem = 1L

  override def beforeAll(): Unit = {
    val db = Database.forURL("jdbc:mysql://localhost:3306/scalatestdb?useSSL=false&user=root&password=root", driver="com.mysql.cj.jdbc.Driver")
     db.run(
      sql"""TRUNCATE TABLE todo""".as[(String, String)]
    )
    Thread.sleep(5000)
    super.beforeAll()
  }



  "ApiRest POST" should {

    "insert item" in {
      val currentTime: String = LocalDateTime.now().toString
      val todoName = "testName-"+currentTime
      val controller = inject[TodoController]
      val api = controller
        .add()
        .apply(
          FakeRequest(POST, "/api/todos/add")
            .withJsonBody(Json.obj("name" -> todoName))
          )

      status(api) mustBe OK
      contentType(api) mustBe Some("text/plain")
      contentAsString(api) must include ("TodoItem successfully added")
    }
  }

  "ApiRest GET" should {

    "list items" in {
      val controller = inject[TodoController]
      val api = controller.getAll().apply(FakeRequest(GET, "/api/todos"))

      status(api) mustBe OK
      contentType(api) mustBe Some("application/json")
      contentAsString(api) must include ("testName-")
    }

    "list one item" in {
      val controller = inject[TodoController]
      val api = controller.getById(idItem).apply(FakeRequest(GET, "api/todos/"+idItem))

      status(api) mustBe OK
      contentType(api) mustBe Some("application/json")
      contentAsString(api) must include ("testName-")
    }
  }

  "ApiRest UPDATE" should {

    "update item" in {
      val currentTime: String = LocalDateTime.now().toString
      val todoName = "testName-"+currentTime
      val controller = inject[TodoController]
      val api = controller
        .update(idItem)
        .apply(
          FakeRequest(PUT, "/api/todos/update/"+idItem)
            .withJsonBody(Json.obj("name" -> todoName, "isComplete" -> false))
        )

      status(api) mustBe OK
      contentType(api) mustBe Some("text/plain")
      contentAsString(api) must include ("TodoItem successfully updated")
    }
  }
}
