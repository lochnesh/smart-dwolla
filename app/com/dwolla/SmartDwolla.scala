package com.dwolla

import com.dwolla.SmartDwollaParser.SendSentenceContext
import com.dwolla.dto.SmartSend
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

class SmartDwolla {

  def parse(input: String): Option[SmartSend] = {
    val lexer = new SmartDwollaLexer(new ANTLRInputStream(input))
    val tokens = new CommonTokenStream(lexer)
    val parser = new SmartDwollaParser(tokens)

    val context = parser.sendSentence()
    val walker = new ParseTreeWalker()
    val listener = new AntlrSmartDwollaListener()
    walker.walk(listener, context)

    listener.result match {
      case Some(ctx) =>
        Some(SmartSend(ctx.ID.toString, BigDecimal(ctx.AMOUNT.toString)))
      case None => None
    }
  }
}

class AntlrSmartDwollaListener extends SmartDwollaBaseListener {
  var result:Option[SendSentenceContext] = None

  override def enterSendSentence(ctx: SendSentenceContext) = {
    result = Some(ctx)
  }
}
