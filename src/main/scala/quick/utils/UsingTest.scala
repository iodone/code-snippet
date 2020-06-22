package quick.utils

/**
  * Created by iodone on {20-6-10}.
  */
import java.io.{BufferedReader, FileReader}
import scala.util.{Try, Using}
import scala.io.Source

object UsingTest {

  def readfile(path: String) = {
    val s = Source.fromFile(path)
    val r = s.getLines.toSeq
    s.close
    r
  }


  // 单个资源管理
  def lines: Try[Seq[String]] = Using(
    new BufferedReader(new FileReader("/tmp/words/american-english"))
  ) { reader =>
    Iterator.continually(reader.readLine()).takeWhile(_ != null).toSeq
  }

  // 多个资源管理
  def lines1: Try[Seq[String]] = Using.Manager { use =>
    val r1 = use(new BufferedReader(new FileReader("/tmp/words/american-english")))
    val r2 = use(new BufferedReader(new FileReader("/tmp/words/words")))

    def lines(reader: BufferedReader): Iterator[String] =
      Iterator.continually(reader.readLine()).takeWhile(_ != null)

    (lines(r1) ++ lines(r2)).toSeq
  }

  // 不需要包装到Try
  def lines2: Seq[String] = Using.resource(new BufferedReader(new FileReader("/tmp/words/american-englis"))) {reader =>
    Iterator.continually(reader.readLine).takeWhile(_ != null).toSeq
  }

  def main(args: Array[String]) = {
    readfile("./src/main/scala/quick/utils/UsingTest.scala").foreach(println(_))
  }


}
