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

  val smartConfirmForm: Form[SmartTaskConfirm] = Form(
    mapping(
      "command" -> text,
      "dwollaId" -> text,
      "name" -> text,
      "image" -> text,
      "amount" -> bigDecimal,
      "pin" -> text
    )(SmartTaskConfirm.apply)(SmartTaskConfirm.unapply)
  )

  def build() = Action { implicit request =>
    smartForm.bindFromRequest.fold(
      hasErrors => {
        Redirect(routes.SmartTasks.create())
      },
      smartForm => {
        val token = request.session.get("token").getOrElse(throw new Exception("No token found"))
        val parser = new SmartDwolla
        val smartSend = parser.parse(smartForm.command).getOrElse(throw new Exception("unable to parse"))
        val dwolla = new Builder().setEndpoint(BASE_URL).build().create(classOf[DwollaServiceSync])
        val searchResponse = dwolla.getUserContacts(token, smartSend.receiver, "", 1)
        val contact = searchResponse.Response(0)
        val contactId = contact.Id
        val confirm = SmartTaskConfirm(smartForm.command, contact.Id, contact.Name, contact.Image, smartSend.amount, "")
        Ok(views.html.smartTaskConfirm(smartConfirmForm.fill(confirm)))
      }
    )
  }

  def complete() = Action { implicit request =>
    smartConfirmForm.bindFromRequest.fold(
      hasErrors => {},
      form => {
        val token = request.session.get("token").getOrElse(throw new Exception("No token found"))
        val dwolla = new Builder().setEndpoint(BASE_URL).build().create(classOf[DwollaServiceSync])
        val response = dwolla.send(token, new DwollaTypedBytes(new Gson(), new SendRequest(form.pin, form.dwollaId, form.amount.toDouble)))
      }
    )
    Redirect(routes.SmartTasks.create())
  }
}

case class SmartTask(command: String)
case class SmartTaskConfirm(command: String, dwollaId: String, name: String, image: String, amount: BigDecimal, pin: String)
