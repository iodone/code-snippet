package quick.future

/**
  * Created by iodone on {20-5-19}.
  */

/**
  * Refence: https://blog.colinbreck.com/calling-blocking-code-there-is-no-free-lunch/
  */

import com.typesafe.scalalogging.StrictLogging
import java.util.concurrent.Executors

import scala.concurrent._
import scala.io.StdIn


object Service {
  def blockingCall() = {
    Thread.sleep(1000)
    1
  }
}

/**
  * 有blocking, 会增加额外的线程去执行，1秒钟执行完成，100个任务并发执行，如果并发太多，会导致OOM（创建线程导致）
  * 无blocking, 类似FixedThreadPool, 25秒完成
  * global线程池是基于ForkJoin实现，在显示blocking的代码块中开启额外线程，避免 thread  starvation
  * forkjoin类似用户态的线程切换，可以指定并发度,但是不好精确控制
  */
object GlobalThreadPoolBlocking extends App with StrictLogging {
  import scala.concurrent.ExecutionContext.Implicits.global
  (1 to 100) foreach { n =>
    Future {
      logger.info("Starting Future: " + n)
      blocking {
        Service.blockingCall()
      }
      logger.info("Ending Future: " + n)
    }
  }

  StdIn.readLine()
  sys.exit()
}

/**
  * 有无blocking 没有区别，不会增加额外的线程，25秒完成所有任务（threadPoolSize = 4, 跟线程池大小有关）
  */
object FixedSizeThreadPoolBlocking extends App with StrictLogging {
  implicit val ec = ExecutionContext.fromExecutorService {
    Executors.newFixedThreadPool(10)
  }

  (1 to 100) foreach { n =>
    Future {
      logger.info("Starting Future: " + n)
//      blocking {
        Service.blockingCall()
//      }
      logger.info("Ending Future: " + n)
    }
  }

  StdIn.readLine()
  sys.exit()
}

/**
  * 有无blocking一样，会开启额外线程（不指定大小），类似blocking的global行为
  */
object CachedThreadPoolBlocking extends App with StrictLogging {
  implicit val ec = ExecutionContext.fromExecutorService {
    Executors.newCachedThreadPool()
  }

  (1 to 100) foreach { n =>
    Future {
      logger.info("Starting Future: " + n)
      blocking {
        Service.blockingCall()
      }
      logger.info("Ending Future: " + n)
    }
  }

  StdIn.readLine()
  sys.exit()
}

