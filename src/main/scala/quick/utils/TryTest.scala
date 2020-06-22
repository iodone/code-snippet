package quick.utils

/**
  * Created by iodone on {20-6-19}.
  */
import scala.util.{Try,Failure, Using}


case class ConnectionException(msg: String = "Connection exception") extends Exception {
  override def getMessage: String = msg
}
case class SqlException(msg: String = "Sql exception") extends Exception {
  override def getMessage: String = msg
}
case class ParamException(msg: String = "Param exception") extends Exception {
  override def getMessage: String = msg
}

case class InternalException(msg: String = "Internal exception") extends Exception {
  override def getMessage: String = msg
}

object TryTest {


  var throwException = false
  var queryException = false

  case class Connection(db: String) {
    def query(sql: String) =  if (queryException) throw new SqlException else s"query ${db} using ${sql}"
    def close() = println(s"connection closed")
  }


  def mockGetConnection(db: String) = {
    if (!throwException)
      new Connection(db)
    else throw new ConnectionException
  }

  def getDbResult(db: String): Try[String] = {
    Try {
      val conn = mockGetConnection(db)
      val r = Try {
        conn.query("select * from test")
      }.recoverWith {
        case e: SqlException => Failure(new ParamException(e.getMessage))
      }
      conn.close()
      r
    }.flatten
  }

  // Use scala.util.Using
  def getDbResult3(db: String): Try[String] = {
    import Using.Releasable
    implicit val connReleasable: Releasable[Connection] = (conn: Connection) => conn.close

    Using.Manager {use =>
      val conn = use(mockGetConnection(db))
      conn.query("select * from test")
    }.recoverWith {
      case e: SqlException => Failure(new ParamException(e.getMessage))
      case e: ConnectionException => Failure(new ParamException(e.getMessage))
    }
  }

  def getDbResult1(db: String): Try[String] = {
    import Using.Releasable
    implicit val connReleasable: Releasable[Connection] = (conn: Connection) => conn.close

    Using(mockGetConnection(db)) {conn =>
      conn.query("select * from test")
    }.recoverWith {
      case e: SqlException => Failure(new ParamException(e.getMessage))
      case e: ConnectionException => Failure(new ParamException(e.getMessage))
    }
  }

  def getDbResult2(db: String): Try[String] = {
    Try(mockGetConnection(db)).flatMap {conn =>
      val r = Try {
        conn.query("select * from test")
      }.recoverWith {
        case e: SqlException => Failure(new ParamException(e.getMessage))
      }
      conn.close()
      r
    }
  }

  def main(args: Array[String]) = {

    Option("").fold("") { x =>
      println(x)
      val msg = "\\w*Exception:.*$".r.findAllIn(x).toList
      if (msg.isEmpty) x else msg.last
    }

    var r = ""

    // case getResult
    // normal
    throwException = false
    queryException = false
    r = getDbResult("db0").fold(fa =>  {println(fa.getMessage); "error"}, _.toString)
    println(s"result: ${r}")

    // throw connect exception
    throwException = true
    queryException = false
    r = getDbResult("db0").fold(_.getMessage, _.toString)
    println(s"result: ${r}")

    // throw query exception
    throwException = false
    queryException = true
    r = getDbResult("db0").fold(_.getMessage, _.toString)
    println(s"result: ${r}")

    // case getResult1
    // normal
    throwException = false
    queryException = false
    r = getDbResult1("db0").fold(_.getMessage, _.toString)
    println(s"result: ${r}")

    // throw connect exception
    throwException = true
    queryException = false
    r = getDbResult1("db0").fold(_.getMessage, _.toString)
    println(s"result: ${r}")

    // throw connect exception
    throwException = false
    queryException = true
    r = getDbResult1("db0").fold(_.getMessage, _.toString)
    println(s"result: ${r}")

    // throw connect exception
    throwException = false
    queryException = true
    r = getDbResult2("db0").fold(_.getMessage, _.toString)
    println(s"result: ${r}")
  }
}
