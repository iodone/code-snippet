package quick.future
import java.util.concurrent.Executors.newCachedThreadPool
import java.util.concurrent.{CancellationException, CompletableFuture, CompletionException, CountDownLatch}

object Cancelling {

  val execPool = newCachedThreadPool()

  def test0() = {
    val waitLatch = new CountDownLatch(1)

    val future = CompletableFuture.runAsync(() => {
      def foo() = {
        try {
          System.out.println("Wait")
          while(true) {
            println("hello")
            println(Thread.currentThread().isInterrupted())
            if (Thread.currentThread().isInterrupted()) {
              println("world")
              throw new InterruptedException()
            }
//            waitLatch.await() //cancel should interrupt
          }
          System.out.println("Done")
        } catch {
          case e: CancellationException =>
            System.out.println("Canceled")
            throw new RuntimeException(e)
          case e: InterruptedException =>
            System.out.println("Interrupted")
            throw new RuntimeException(e)
        }
      }

      foo()
    }, execPool)


    Thread.sleep(10) //give it some time to start (ugly, but works)

    future.cancel(true)
    System.out.println("Cancel called")

    assert(future.isCancelled)


    assert(future.isDone)
    Thread.sleep(100) //give it some time to finish

    io.StdIn.readLine

  }
  // 调用FutrueTask的cancel，当前线程处于：wait,join,sleep状态时可以被打断，并抛出InterruptedException; 线程在运行时，也不会抛出InterruptedException, 但是可以通过Thread.currentThread.
  // isInterrupted 判断当前线程是否被调用了取消
  // 而CompletableFuture的cancel，在线程的任何状态都不会抛出InterruptedException，Thread.currentThread.isInterrupted 也无法判断。
  def test1() = {
    val waitLatch = new CountDownLatch(1)

    val future = execPool.submit(new Runnable {
      override def run(): Unit = {
        try {
          System.out.println("Wait")
//          waitLatch.await() //cancel should interrupt
          while(true) {
            println("hello")
            println(Thread.currentThread().isInterrupted())
            if (Thread.currentThread().isInterrupted) {
              println("world")
              throw new InterruptedException()
            }
            //            waitLatch.await() //cancel should interrupt
          }

          System.out.println("Done")
        } catch {
          case e: CancellationException =>
            System.out.println("Canceled ex")
            throw new RuntimeException(e)
          case e: InterruptedException =>
            System.out.println("Interrupted")
            throw new RuntimeException(e)
        }
      }
    })


    Thread.sleep(10) //give it some time to start (ugly, but works)

    future.cancel(true)
    System.out.println("Cancel called")

    assert(future.isCancelled)

    assert(future.isDone)
    Thread.sleep(100) //give it some time to finish

  }



  def main(args: Array[String]) = {
    test1
    ()
  }

}


