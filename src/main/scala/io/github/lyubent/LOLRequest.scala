package io.github.lyubent

import io.github.lyubent.util.{FileUtil, LoggerUtil}

import scalaj.http.{Http, HttpOptions}

object LOLRequest {

  private var gameNotFoundCounter = 0
  private val logger = LoggerUtil.getLogger("LOLRequest")

  /**
   * Generic get request function
   *
   * @param matchId ID of the game to be requested.
   * @return Int representing status of get request.
   */
  // todo Protect from socket timeout exceptions
  def sendGetRequest(matchId: Long): Int = {

    val url = "https://euw.api.pvp.net/api/lol/euw/v2.2/match/" + matchId.toString
    val result = Http(url).param("api_key", FileUtil.getConfigProperty("api_key"))
                          .option(HttpOptions.readTimeout(2000)).asString
    logger.info(s"Fetching $matchId status: ${result.statusLine}")

    result.code match {
      case 200 => {
        // save somewhere.
        // TODO save this to file
        // result.body
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
