import credentials.TwitterCredentials
import model.Tweet
import org.apache.flink.streaming.api.datastream.{AsyncDataStream, DataStreamSource}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.twitter.TwitterSource
import org.apache.flink.util.Collector
import utils.flink.{AsyncDatabaseResult, StringToTweetFlatMap, TweetStringFilter}

import java.util.concurrent.TimeUnit

object TweetCollectorMain extends App {

  val env = StreamExecutionEnvironment.getExecutionEnvironment
  val streamSource: DataStreamSource[String] = env.addSource(new TwitterSource(TwitterCredentials.getTwitterCredentials))

  val tweets = streamSource.filter(TweetStringFilter()).flatMap(StringToTweetFlatMap()).keyBy((tweet: Tweet) => tweet.id)
  AsyncDataStream.unorderedWait(tweets, AsyncDatabaseResult(), 3000, TimeUnit.MILLISECONDS, 100)

  env.execute("Test")

}

