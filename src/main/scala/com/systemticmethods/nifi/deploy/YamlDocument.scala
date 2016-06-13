package com.systemticmethods.nifi.deploy

import java.util

import org.yaml.snakeyaml.Yaml

import scala.collection.JavaConverters._
import scala.collection.mutable

case class YamlDocument(yamlstr: String) {
  private val yaml = new Yaml()
  // load returns a LinkedHashMap or an ArrayList
  private val fields: Iterable[Any] = yaml.load(yamlstr) match {
      case fields: util.LinkedHashMap[String, Object] => fields.asScala
      case fields: util.ArrayList[Object] => fields.asScala
      case unknown:Any => println(s"Warning: Unknown yaml $unknown"); List[Object]()
    }

  def chunk:YamlChunk = YamlChunk(fields)
}

case class YamlChunk(datum: Object) {
  val mapping:Option[mutable.Map[String, Object]] = datum match {
    case fields: mutable.Map[String, Object] => Option(fields)
    case fields: util.LinkedHashMap[String, Object] => Option(fields.asScala)
    case _ => None
  }

  val sequence:Option[mutable.Buffer[Object]] = datum match {
    case fields: mutable.Buffer[Object] => Option(fields)
    case fields: util.ArrayList[Object] => Option(fields.asScala)
    case _ => None
  }

  val iterator: Option[Iterable[Any]] = datum match {
    case fields: mutable.Map[String, Object] => Option(fields)
    case fields: util.LinkedHashMap[String, Object] => Option(fields.asScala)
    case fields: mutable.Buffer[Object] => Option(fields)
    case fields: util.ArrayList[Object] => Option(fields.asScala)
    case _ => None
  }

  def isMapping():Boolean = {
     mapping.isDefined
  }

  def isSequence():Boolean = {
    sequence.isDefined
  }

  def isScalar():Boolean = {
    !sequence.isDefined && !mapping.isDefined
  }

  def getMapping(field: String): Option[YamlChunk] = {
    mapping.flatMap(fld => fld.get(field).map(obj => YamlChunk(obj)))
  }

  def getSequence(ix: Int): Option[YamlChunk] = {
    sequence.map(fld => YamlChunk(fld(ix)))
  }

}


