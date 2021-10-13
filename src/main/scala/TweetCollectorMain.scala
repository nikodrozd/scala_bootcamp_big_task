import com.typesafe.config.ConfigFactory
import credentials.TwitterCredentials
import model.Tweet
import org.apache.flink.streaming.api.datastream.{AsyncDataStream, DataStreamSource}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.twitter.TwitterSource
import utils.flink.{AsyncDatabaseResult, StringToTweetFlatMap, TweetStringFilter}

import java.util.concurrent.TimeUnit

object TweetCollectorMain extends App {

  val timeout = ConfigFactory.load().getInt("common.asyncTimeout")
  val capacity = ConfigFactory.load().getInt("common.unWaitCapacity")
  val jobName = ConfigFactory.load().getString("common.tweetCollectorJobName")

  val env = StreamExecutionEnvironment.getExecutionEnvironment
  val streamSource: DataStreamSource[String] = env.addSource(new TwitterSource(TwitterCredentials.getTwitterCredentials))

  val tweets = streamSource.filter(TweetStringFilter()).flatMap(StringToTweetFlatMap()).keyBy((tweet: Tweet) => tweet.id)
  AsyncDataStream.unorderedWait(tweets, AsyncDatabaseResult(), timeout, TimeUnit.MILLISECONDS, capacity)

  env.execute(jobName)

}