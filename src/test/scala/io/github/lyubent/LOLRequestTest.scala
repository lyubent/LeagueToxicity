package io.github.lyubent

import io.github.lyubent.util.FileUtil
import org.junit.{Assert, Test}
import org.scalatest.junit.JUnitSuite

class LOLRequestTest extends JUnitSuite {

  // mimic java static import by explicitly importing all funcs.
  import Assert._

  @Test
  def testStartingMatchID(): Unit = {
    assertEquals(2962521447L, FileUtil.getConfigProperty("startingMatchID").toLong)
    Thread.sleep(2000)
  }

  // test that a match gives 404
  @Test
  def test404ByMID(): Unit = {
    assertEquals(404, LOLRequest.sendGetRequest(-1L))
    Thread.sleep(2000)
  }

  // test that a match gets 200
  @Test
  def test200ByMID(): Unit = {
    assertEquals(200, LOLRequest.sendGetRequest(2962507850L))
    Thread.sleep(2000)
  }
}
