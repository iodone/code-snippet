package quick.utils

import System.nanoTime
/**
 * @author: odone 
 * @date: 2022/6/20 14:45
 * */

object Profile extends App {

  def profile[R](code: => R, t: Long = nanoTime) = (code, nanoTime - t)

  def testFunc: Unit = {
    (1 to 1000) foreach println
  }

  // usage:
  val (result, time) = profile {
    /* block of code to be profiled*/
  }

  val (result2, time2) = profile(testFunc)
  println(time2)

}
