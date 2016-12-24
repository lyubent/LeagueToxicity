import io.github.lyubent.DynamicMatchQuery
import org.apache.spark.sql.SparkSession
import org.junit.{Test, After, Before, Assert}
import org.scalatest.junit.JUnitSuite

class DynamicMatchQueryTest extends JUnitSuite {

  import Assert._

  @Before
  def initResource(): Unit = {
    val spark = SparkSession.builder()
                            .appName("Toxic_League_UNIT")
                            .master("local[2]")
                            .getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")
  }

  @After
  def disposeResource(): Unit = {
    getSession().stop()
  }

  @Test
  def verifySessionActive(): Unit = {
    val array = getSession().sparkContext.parallelize(List(1,2)).collect()
    assertArrayEquals(Array(1,2), array)
  }

  @Test
  def verifyFlex(): Unit = {
    val spark = getSession()
    val dmq = new DynamicMatchQuery(spark)
    val baseDF = dmq.getBaseDF()
    assertEquals(392, dmq.rankedFlex(baseDF).count())
  }

  @Test
  def verifySoloQ(): Unit = {
    val spark = getSession()
    val dmq = new DynamicMatchQuery(spark)
    val baseDF = dmq.getBaseDF()
    assertEquals(584, dmq.rankedSoloQ(baseDF).count())
  }

  def getSession(): SparkSession = {
    SparkSession.builder().getOrCreate()
  }
}
