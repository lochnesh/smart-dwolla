package com.dwolla

import _root_.java.net.{URLEncoder, URI}

import com.dwolla.dto.SmartSend
import com.dwolla.java.sdk.requests.{TokenRequest, SendRequest}
import com.dwolla.java.sdk.{Consts, OAuthServiceSync, DwollaServiceSync, DwollaTypedBytes}
import com.google.gson.Gson
import retrofit.RestAdapter.Builder

object SmartScalaDriver {
/*
  val BASE_URL = "https://uat.dwolla.com/oauth/rest"
  val BASE_OAUTH_URL = "https://uat.dwolla.com/oauth/v2"
  val SCOPES = "Send|AccountInfoFull|Funding|Contacts"
  val REDIRECT = "http://requestb.in/1hntplw1"
  val CLIENT_ID = sys.env("DWOLLA_CLIENT_ID")
  val CLIENT_SECRET = sys.env("DWOLLA_CLIENT_SECRET")
  var smartSend: Option[SmartSend] = None

  def main(args: Array[String]): Unit = {
    var command:String = ""

    args match {
      case(Array(c:String)) =>
        command = c
      case _ =>
        throw new NotImplementedError()
    }

    val parser = new SmartDwolla()
    smartSend = parser.parse(command)

    Desktop.getDesktop.browse(new URI(s"$BASE_OAUTH_URL/authenticate?response_type=code&scope=${encode(SCOPES)}&client_id=${encode(CLIENT_ID)}&redirect_uri=${encode(REDIRECT)}"))
  }

  private def encode(text: String): String = {
    URLEncoder.encode(text, "UTF-8")
  }

  class Http extends Controller {
    get("/callback") { request: Request =>
      val code = request.params("code")
      val oauth = new Builder().setEndpoint(BASE_OAUTH_URL).build().create(classOf[OAuthServiceSync])
      val token = oauth.getToken(new DwollaTypedBytes(new Gson, new TokenRequest(CLIENT_ID, CLIENT_SECRET, Consts.Api.AUTHORIZATION_CODE, REDIRECT, code)))
      val dwolla = new Builder().setEndpoint(BASE_URL).build().create(classOf[DwollaServiceSync])
      val actualSend = smartSend.getOrElse(throw new Exception("Send wasn't parsed"))
      val searchResponse = dwolla.getUserContacts(token.access_token, actualSend.receiver, "", 10)
      val contactId = searchResponse.Response(0).Id
      val response = dwolla.send(token.access_token, new DwollaTypedBytes(new Gson(), new SendRequest("1234", contactId, actualSend.amount.toDouble)))
      println(response.Success)
      Future(new ResponseBuilder())
    }
  }
  */
}
