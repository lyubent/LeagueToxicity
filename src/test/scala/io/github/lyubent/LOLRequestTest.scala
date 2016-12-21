package io.github.lyubent

import io.github.lyubent.util.FileUtil
import org.junit.{Assert, Test}
import org.scalatest.junit.JUnitSuite

class LOLRequestTest extends JUnitSuite {

  // mimic java static import by explicitly importing all funcs.
  import Assert._

  @Test
  def teststartingMatchID(): Unit = {
    val startingMID = 2962507850L
    assertEquals(startingMID, FileUtil.getConfigProperty("startingMatchID").toLong)
  }

  // test that a match gives 404
  @Test
  def test404ByMID(): Unit = {
    assertEquals(LOLRequest.sendGetRequest(-1L), 404)
  }

  // test that a match gets 200
  @Test
  def test200ByMID(): Unit = {
    assertEquals(LOLRequest.sendGetRequest(2962507850L), 200)
  }
}
