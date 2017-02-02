package utils

import akka.stream.Materializer
import com.google.inject.Inject
import org.slf4j.LoggerFactory
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Audit logger
  */
class AuditFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {
  lazy val logger = LoggerFactory.getLogger("audit")

  val excludePaths = Seq("/assets/")

  override def apply(nextFilter: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
    if (!excludePaths.exists(rh.path.startsWith)) {
      log(rh)
    }

    nextFilter(rh)
  }

  def log(rh: RequestHeader, extra: String = ""): Unit = {
    val (total, used, free) = memory()

    logger.info(s"${rh.remoteAddress}\t${rh.method}\tt${total}mb/u${used}mb/f${free}mb\t${rh.path}\t\t\t${rh.queryString}\t${extra}")
  }

  def memory(): (Long, Long, Long) = {
    val runTime = Runtime.getRuntime
    val freeMem = runTime.freeMemory()
    val totalMem = runTime.totalMemory()
    val used = totalMem - freeMem

    val mb: Int = 1 << 20
    (totalMem / mb, used / mb, freeMem / mb)
  }
}
