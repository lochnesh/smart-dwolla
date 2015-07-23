package controllers

import java.net.URLEncoder
import javax.inject.Inject

import com.dwolla.dto.SmartSend
import com.dwolla.java.sdk.requests.TokenRequest
import com.dwolla.java.sdk.{DwollaTypedBytes, Consts, OAuthServiceSync}
import com.google.gson.Gson
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc._
import retrofit.RestAdapter.Builder

class Application @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
  val BASE_URL = "https://uat.dwolla.com/oauth/rest"
  val BASE_OAUTH_URL = "https://uat.dwolla.com/oauth/v2"
  val SCOPES = "Send|AccountInfoFull|Funding|Contacts"
  val REDIRECT = "http://smart-dwolla.herokuapp.com/callback"
  val CLIENT_ID = sys.env("DWOLLA_CLIENT_ID")
  val CLIENT_SECRET = sys.env("DWOLLA_CLIENT_SECRET")
  var smartSend: Option[SmartSend] = None

  val smartForm: Form[SmartTask] = Form(
    mapping(
      "command" -> text
    )(SmartTask.apply)(SmartTask.unapply)
  )

  def index = Action {
    Ok(views.html.index(s"$BASE_OAUTH_URL/authenticate?response_type=code&scope=${encode(SCOPES)}&client_id=${encode(CLIENT_ID)}&redirect_uri=${encode(REDIRECT)}"))
  }

  def callback(code: String) = Action {
    val oauth = new Builder().setEndpoint(BASE_OAUTH_URL).build().create(classOf[OAuthServiceSync])
    val token = oauth.getToken(new DwollaTypedBytes(new Gson, new TokenRequest(CLIENT_ID, CLIENT_SECRET, Consts.Api.AUTHORIZATION_CODE, REDIRECT, code)))
    Redirect(routes.Application.newSmartTask())
  }

  def newSmartTask() = Action {
    Ok(views.html.smartTask(smartForm))
  }

  def smartTask() = Action { implicit request =>
    smartForm.bindFromRequest()
    Redirect(routes.Application.newSmartTask())
  }

  private def encode(text:String): String = {
    URLEncoder.encode(text, "UTF-8")
  }
}

case class SmartTask(command: String)
