package io.github.lyubent

import io.github.lyubent.util.FileUtil

import scalaj.http.{Http, HttpOptions}

object LOLRequest {

  private var gameNotFoundCounter = 0

  /**
   * Generic get request function
   *
   * @param matchId ID of the game to be requested.
   */
  // TODO parameterise function better. Add option for various / multiple URLS
  // TODO requires logger. Not just sout.
  def sendGetRequest(matchId: Long): Unit = {

    println(s"Processing game $matchId")

    val url = "https://euw.api.pvp.net/api/lol/euw/v2.2/match/" + matchId.toString
    val result = Http(url).param("api_key", FileUtil.getConfigProperty("api_key"))
                          .option(HttpOptions.readTimeout(2000)).asString

    println(result.contentType)
    println("code" + result.code)

    if (result.code == 200) {
      // save somewhere.
      println("200 ")
      println(result.body)
    } else if (result.code == 404) {
      gameNotFoundCounter += 1
      println(s"404 Games not found so far: $gameNotFoundCounter")
    } else if (result.code == 429) {
      // sleep 10sec, we've ran out of requests.
      println("429 Sleeping 10 sec, request limit reached.")
      Thread.sleep(10000)
    }
    println(result.statusLine)
  }

  // generic main for loopin and building a dataset.
  // Can be much fancier (Spark streaming) but will be kept basic for the time being.
  def main(args: Array[String]): Unit = {
    // we know that 2962507849 exists, start from 1 up.
    // should be dynamic instead of hardcoded.
    var startingMID = 2962507850L
    while(true) {
      LOLRequest.sendGetRequest(startingMID)
      // API doesn't allow us to go faster than this.
      Thread.sleep(1000)
      startingMID += 1
    }
  }
}
