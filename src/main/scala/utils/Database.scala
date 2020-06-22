package utils

/**
  * Created by iodone on {20-6-17}.
  */
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import utils.Config._

class Database(config: DbConf) {

  val datasource = {
    val hikariConfig = new HikariConfig()

    hikariConfig.setJdbcUrl(config.url)
    hikariConfig.setUsername(config.user)
    hikariConfig.setPassword(config.password)
    hikariConfig.setDriverClassName(config.driver)

    // default setup
    hikariConfig.setMinimumIdle(3);           //minimum number of idle connection
    hikariConfig.setMaximumPoolSize(10);      //maximum number of connection in the pool
    hikariConfig.setConnectionTimeout(10000); //maximum wait milliseconds for get connection from pool
    hikariConfig.setIdleTimeout(60000);       // max idle time for recycle idle connection
    hikariConfig.setConnectionTestQuery("describe log.dn"); //validation query
    hikariConfig.setValidationTimeout(3000);   //validation query timeout

    new HikariDataSource(hikariConfig)
  }

}


object Database {

  lazy val taosDb = new Database(dbsConfig.taos)

}
