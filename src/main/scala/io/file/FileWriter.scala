package io.file

import play.api.libs.json.JsObject

import java.io.{BufferedWriter, FileWriter}

object FileWriter {

  def saveData(fileName: String, geoJsonFinal: JsObject): Unit = {
    val writer: BufferedWriter = new BufferedWriter(new FileWriter(fileName))
    try {
      writer.write(geoJsonFinal.toString())
    } finally {
      writer.close()
    }
  }

}
