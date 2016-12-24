package io.github.lyubent

import io.github.lyubent.util.FileUtil
import org.junit.{Assert, Test}
import org.scalatest.junit.JUnitSuite

class LOLRequestTest extends JUnitSuite {

  // mimic java static import by explicitly importing all funcs.
  import Assert._

  @Test
  def testStartingMatchID(): Unit = {
    val startingMID = 2962521447L
    assertEquals(startingMID, FileUtil.getConfigProperty("startingMatchID").toLong)
    Thread.sleep(2000)
  }

  // test that a match gives 404
  @Test
  def test404ByMID(): Unit = {
    assertEquals(LOLRequest.sendGetRequest(-1L), 404)
    Thread.sleep(2000)
  }

  // test that a match gets 200
  @Test
  def test200ByMID(): Unit = {
    assertEquals(LOLRequest.sendGetRequest(2962507850L), 200)
    Thread.sleep(2000)
  }
}
