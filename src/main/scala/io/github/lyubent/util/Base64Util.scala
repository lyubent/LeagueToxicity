package io.github.lyubent.util

import java.util.Properties

import scalaj.http.Base64

object Base64Util {

  /**
   * Encodes utf8 string to base64 string.
   *
   * @param data - string to be encoded
   * @return Base64 encoded String
   */
  def encode(data: String): String = {
    Base64.encodeString(data)
  }

  /**
   * Decodes base64 string to utf8 string.
   *
   * @param data - string to be decoded
   * @return utf8 String
   */
  def decode(data: String): String = {
    Base64.decodeString(data)
  }
}
