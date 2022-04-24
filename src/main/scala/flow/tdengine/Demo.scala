package flow.tdengine

/**
  * Created by iodone on {20-6-17}.
  */

import utils.Database

import java.util.Date


case class Meters(ts: Date, f1: Int, f2: Int, f3: Int)
case class AggMeters(f1: Int, f2: Int, f3: Int)



object Demo extends App {

  val taosConn = Database.taosDb.datasource.getConnection()

  def query(sql: String) = {
    val pst = taosConn.prepareStatement(sql)
    pst.setFetchSize(1003)
    pst.executeQuery()
  }


  var resultList: List[Meters] = Nil
  var result = query("select * from meters limit 10")
  while(result.next()) {
    resultList = resultList ::: Meters(result.getTimestamp("ts"), result.getInt("f1"), result.getInt("f2"), result.getInt("f2")) :: Nil
  }
  resultList.foreach {x =>
    println(x)
  }

  var result1List: List[AggMeters] = Nil
  val result1 = query("select avg(f1), max(f2), min(f2) from meters interval(10s)")
  while(result1.next()) {
    result1List = result1List ::: AggMeters(result1.getInt("avg(f1)"), result1.getInt("max(f2)"), result1.getInt("min(f2)")) :: Nil
  }
  result1List.foreach {x =>
    println(x)
  }

  System.in.read


}
