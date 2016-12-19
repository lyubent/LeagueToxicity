package io.github.lyubent.util

import java.util.Properties

import com.typesafe.scalalogging.Logger
import org.apache.log4j.PropertyConfigurator

object LoggerUtil {
  def getLogger(name: String): Logger = {
    val logger = Logger(name)
    val props = new Properties()
    props.load(getClass().getResourceAsStream("/log4j.properties"))
    PropertyConfigurator.configure(props)

    logger
  }
}
