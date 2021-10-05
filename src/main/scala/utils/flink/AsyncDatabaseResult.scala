package utils.flink

import credentials.MongoDBCredentials
import io.db.MongoDriver
import model.Tweet
import org.apache.flink.streaming.api.functions.async.{AsyncFunction, ResultFuture}
import reactivemongo.api.commands.WriteResult
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
import scala.collection.JavaConverters._

case class AsyncDatabaseResult() extends AsyncFunction[Tweet, WriteResult] {

  lazy val mongoDriver: MongoDriver = new MongoDriver(MongoDBCredentials.mongoURLWithAuthData,
    MongoDBCredentials.dbName,
    MongoDBCredentials.collectionName)

  implicit lazy val exc: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(16))

  override def asyncInvoke(input: Tweet, resultFuture: ResultFuture[WriteResult]): Unit = {
    mongoDriver.createTweet(input).onComplete {
      case Success(value) => resultFuture.complete(List(value).asJava)
      case Failure(exception) => resultFuture.completeExceptionally(exception)
    }
  }
}
