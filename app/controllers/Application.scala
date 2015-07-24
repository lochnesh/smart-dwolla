package controllers

import java.net.URLEncoder
import javax.inject.Inject

import com.dwolla.dto.SmartSend
import com.dwolla.java.sdk.requests.TokenRequest
import com.dwolla.java.sdk.{Consts, DwollaTypedBytes, OAuthServiceSync}
import com.google.gson.Gson
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import retrofit.RestAdapter.Builder

class Application @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
  val BASE_OAUTH_URL = "https://uat.dwolla.com/oauth/v2"
  val SCOPES = "Send|AccountInfoFull|Funding|Contacts"
  val REDIRECT = "http://smart-dwolla.herokuapp.com/callback"
  val CLIENT_ID = sys.env("DWOLLA_CLIENT_ID")
  val CLIENT_SECRET = sys.env("DWOLLA_CLIENT_SECRET")
  var smartSend: Option[SmartSend] = None


  def index = Action {
    Ok(views.html.index(s"$BASE_OAUTH_URL/authenticate?response_type=code&scope=${encode(SCOPES)}&client_id=${encode(CLIENT_ID)}&redirect_uri=${encode(REDIRECT)}"))
  }

  def callback(code: String) = Action { request =>
    val oauth = new Builder().setEndpoint(BASE_OAUTH_URL).build().create(classOf[OAuthServiceSync])
    val token = oauth.getToken(new DwollaTypedBytes(new Gson, new TokenRequest(CLIENT_ID, CLIENT_SECRET, Consts.Api.AUTHORIZATION_CODE, REDIRECT, code)))
    Redirect(routes.SmartTasks.create()).withSession("token" -> token.access_token)
  }

  private def encode(text:String): String = {
    URLEncoder.encode(text, "UTF-8")
  }
}

