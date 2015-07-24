package com.dwolla

import com.dwolla.dto.SmartSend
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class SmartDwollaSpec extends Specification {
  trait Setup extends Scope {
    val smartDwolla = new SmartDwolla
  }

  "SmartDwolla" should {
    "send to a person" in new Setup {
      SmartSend("skyler", 5) must_== smartDwolla.parse("send 5 to skyler").get
      SmartSend("Skyler", 5) must_== smartDwolla.parse("Send 5 to Skyler").get
      SmartSend("Skyler", 5) must_== smartDwolla.parse("Send 5 To Skyler").get
      SmartSend("Skyler",1.11) must_== smartDwolla.parse("Send 1.11 Skyler").get
      SmartSend("Skyler", 5) must_== smartDwolla.parse("Send Skyler $5").get
    }

    "send given various dollar formats" in new Setup {
      SmartSend("Skyler",5.1) must_== smartDwolla.parse("Send 5.1 to Skyler").get
      SmartSend("Skyler",5.12) must_== smartDwolla.parse("Send 5.12 to Skyler").get
      SmartSend("Skyler",56.12) must_== smartDwolla.parse("Send 56.12 to Skyler").get
      SmartSend("Skyler",.12) must_== smartDwolla.parse("Send .12 to Skyler").get
      SmartSend("Skyler",.1) must_== smartDwolla.parse("Send .1 to Skyler").get
    }

    "send given a dollar sign is included" in new Setup {
      SmartSend("Skyler",5.1) must_== smartDwolla.parse("Send $5.1 to Skyler").get
      SmartSend("Skyler",5.12) must_== smartDwolla.parse("Send $5.12 to Skyler").get
      SmartSend("Skyler",56.12) must_== smartDwolla.parse("Send $56.12 to Skyler").get
      SmartSend("Skyler",.12) must_== smartDwolla.parse("Send $.12 to Skyler").get
      SmartSend("Skyler",.1) must_== smartDwolla.parse("Send $.1 to Skyler").get
    }

    "send using different slang" in new Setup {
      SmartSend("Skyler", 1) must_== smartDwolla.parse("Send Skyler 1 dollar").get
      SmartSend("Skyler", 8) must_== smartDwolla.parse("Send Skyler 8 dollars").get
      SmartSend("Skyler", 8) must_== smartDwolla.parse("Send 8 dollars to Skyler").get
      SmartSend("Skyler", 8) must_== smartDwolla.parse("Send 8 Dollars to Skyler").get
      SmartSend("Skyler", 1) must_== smartDwolla.parse("Send 1 Dollar to Skyler").get
    }

//    "send using first and last name" in new Setup {
//      SmartSend("Jason Mead", 1) must_== smartDwolla.parse("Send 1 To Jason Mead").get
//    }
  }
}
