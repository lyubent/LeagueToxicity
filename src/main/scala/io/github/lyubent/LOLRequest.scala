package io.github.lyubent

import java.net.SocketTimeoutException
import io.github.lyubent.util.{FileUtil, LoggerUtil}

import scalaj.http.{Http, HttpOptions, HttpResponse}

object LOLRequest {

  private var gameNotFoundCounter = 0
  private val logger = LoggerUtil.getLogger("LOLRequest")

  /**
   * Generic get request function
   *
   * @param matchId ID of the game to be requested.
   * @return Int representing status of get request.
   */
  def sendGetRequest(matchId: Long): Int = {

    val url = "https://euw.api.pvp.net/api/lol/euw/v2.2/match/" + matchId.toString

    try {
      val result = Http(url).param("api_key", FileUtil.getConfigProperty("api_key"))
                            .option(HttpOptions.readTimeout(2000)).asString
      logger.info(s"Fetching $matchId status: ${result.statusLine}")
      handleGetRequest(result)
    } catch {
      // TODO Possibly propagate error.
      case ste: SocketTimeoutException => {
        logger.error("SocketTimeoutException - API Reply was too slow", ste)
        -1
      }
    }
  }

  /**
   * Processes result of the get request. Saves too file if successful request.
   *
   * @param result HttpResponse[String] Reply of the get request.
   * @return Int representing status of get request.
   */
  def handleGetRequest(result: HttpResponse[String]): Int = {
    result.code match {
      case 200 => {
        FileUtil.appendToFile(result.body + "\n", FileUtil.getConfigProperty("api_output"))
      }
      case 404 => {
        gameNotFoundCounter += 1
        logger.warn(s"Games not found so far: $gameNotFoundCounter")
      }
      case 429 => {
        // sleep 10sec, we've ran out of requests.
        logger.warn("Sleeping 10 sec, request limit reached.")
        // todo this needs to be an incremental time out
        // every sequencial timeout = double this time out.
        // if we dont hit a timeout 5 requests in a row reset it.
        Thread.sleep(10000)
      }
      case _ => {
        logger.warn(s"Unexpected get reply ${result.headers}")
      }
    }

    result.code
  }

  // Generic main for loopin and building a dataset.
  // Can be much fancier (Spark streaming) but will be kept basic for the time being.
  def main(args: Array[String]): Unit = {
    var startingMatchID = FileUtil.getConfigProperty("startingMatchID").toLong
    while(true) {
      val status = LOLRequest.sendGetRequest(startingMatchID)
      // API doesn't allow us to go faster than 1 sec per request.
      Thread.sleep(1000)

      // if we time out, we will retry with the same MID.
      if (status != 429)
        startingMatchID += 1
    }
  }
}
