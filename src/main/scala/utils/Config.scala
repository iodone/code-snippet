package utils

/**
  * Created by iodone on {20-6-17}.
  */

import pureconfig._
import pureconfig.generic.auto._

object Config {
  case class DbConf(url: String, user: String, password: String, driver: String)
  case class DatabasesConf(taos: DbConf)

  lazy val dbsConfig =  ConfigSource.default.loadOrThrow[DatabasesConf]
}
