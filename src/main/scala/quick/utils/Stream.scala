package quick.utils

/**
  * Created by iodone on {20-6-10}.
  */
import scala.collection.immutable.LazyList
object Stream extends App {
  val s1 = LazyList.continually(LazyList(1,2,3)).flatten.take(10).toList
  val s2 = Iterator.continually(List(1,2,3)).flatten.take(10).toList
  val s3 = Iterator.iterate((1,1)) {acc => (acc._2, acc._1 + acc._2)}.map(_._1).take(10).toList
  val s4 = LazyList.iterate((1,1)) {acc => (acc._2, acc._1 + acc._2)}.map(_._1).take(10).toList

  println(s1)
  println(s2)
  println(s3)
  println(s4)


}
