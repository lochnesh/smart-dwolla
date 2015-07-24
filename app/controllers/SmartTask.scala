package controllers

import javax.inject.Inject

import com.dwolla.SmartDwolla
import com.dwolla.java.sdk.requests.SendRequest
import com.dwolla.java.sdk.{DwollaTypedBytes, DwollaServiceSync}
import com.google.gson.Gson
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import retrofit.RestAdapter.Builder

class SmartTasks @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val BASE_URL = "https://uat.dwolla.com/oauth/rest"

  def create() = Action {
    Ok(views.html.smartTask(smartForm))
  }

  val smartForm: Form[SmartTask] = Form(
    mapping(
      "command" -> text
    )(SmartTask.apply)(SmartTask.unapply)
  )

  def build() = Action { implicit request =>
    smartForm.bindFromRequest.fold(
      hasErrors => {},
      smartForm => {
        val token = request.session.get("token").getOrElse(throw new Exception("No token found"))
        val parser = new SmartDwolla
        val smartSend = parser.parse(smartForm.command).getOrElse(throw new Exception("unable to parse"))
        val dwolla = new Builder().setEndpoint(BASE_URL).build().create(classOf[DwollaServiceSync])
        val searchResponse = dwolla.getUserContacts(token, smartSend.receiver, "", 1)
        val contactId = searchResponse.Response(0).Id
        val response = dwolla.send(token, new DwollaTypedBytes(new Gson(), new SendRequest("1234", contactId, smartSend.amount.toDouble)))
        Redirect(routes.SmartTasks.create())
      }
    )
    Redirect(routes.SmartTasks.create())
  }
}

case class SmartTask(command: String)
