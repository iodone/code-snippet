package quick.utils

import scala.util.control.Breaks.break
import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}

import java.io.{InputStream, OutputStream}
import java.net.InetSocketAddress
import java.util

/**
 * @author: odone 
 * @date: 2022/4/29 11:26 上午
 * */

object FullGc extends App {

  val server = HttpServer.create(new InetSocketAddress(8000), 100)
  server.createContext("/", new RootHandler())
  server.setExecutor(null)

  server.start()



  val list = new util.LinkedList[AnyRef]
  val it: util.ListIterator[AnyRef] = list.listIterator
  while ( {
    true
  }) {
    while ( {
      true
    }) {
      it.add(new AnyRef)
      if (!it.hasNext) break //todo: break is not supported
      it.next
    }
    while ( {
      it.hasPrevious
    }) {
      it.previous
      it.add(new AnyRef)
      it.previous
    }
  }

  println("Hit any key to exit...")
  System.in.read()
  server.stop(0)
}

class RootHandler extends HttpHandler {

  def handle(t: HttpExchange) {
    displayPayload(t.getRequestBody)
    sendResponse(t)
  }

  private def displayPayload(body: InputStream): Unit = {
    println()
    println("******************** REQUEST START ********************")
    println()
    copyStream(body, System.out)
    println()
    println("********************* REQUEST END *********************")
    println()
  }

  private def copyStream(in: InputStream, out: OutputStream) {
    Iterator
      .continually(in.read)
      .takeWhile(x => -1 != x)
      .foreach(out.write)
  }

  private def sendResponse(t: HttpExchange) {
    val response = "Ack!"
    t.sendResponseHeaders(200, response.length())
    val os = t.getResponseBody
    os.write(response.getBytes)
    os.close()
  }

}