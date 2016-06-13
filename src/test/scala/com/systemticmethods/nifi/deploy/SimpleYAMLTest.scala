
package com.systemticmethods.nifi.deploy

import java.util

import org.junit.Test
import org.yaml.snakeyaml.Yaml
import net.jcazevedo.moultingyaml._
import net.jcazevedo.moultingyaml.DefaultYamlProtocol._

class SimpleYAMLTest {
  val yamlstr = """invoice: 34843
                  |date   : 2001-01-23
                  |billTo: &id001
                  |  given  : Chris
                  |  family : Dumars
                  |  address:
                  |    lines: |
                  |      458 Walkman Dr.
                  |      Suite #292
                  |    city    : Royal Oak
                  |    state   : MI
                  |    postal  : 48046
                  |shipTo: *id001
                  |product:
                  |  - sku         : BL394D
                  |    quantity    : 4
                  |    description : Basketball
                  |    price       : 450.00
                  |  - sku         : BL4438H
                  |    quantity    : 1
                  |    description : Super Hoop
                  |    price       : 2392.00
                  |tax  : 251.42
                  |total: 4443.52
                  |comments:
                  |  Late afternoon is best.
                  |  Backup contact is Nancy
                  |  Billsmer @ 338-4338.
                """.stripMargin

  @Test
  def test1:Unit = {
    val yaml = new Yaml()
    val res = yaml.load(yamlstr).asInstanceOf[util.LinkedHashMap[String, Object]]
    println("test1 " + res)
    println()
  }

  @Test
  def test2 = {
    val res = yamlstr.parseYaml
    println("test2 " + res)
    println()
  }

  @Test
  def test3 = {
    val yaml = new Yaml()
    val str = getresource("nifi-deploy.yml")
    val res = yaml.load(str).asInstanceOf[util.LinkedHashMap[String, Object]]
    println("test3 " + res)
    println()
  }

  @Test
  def test4 = {
    val res = getresource("nifi-deploy.yml").parseYaml
    println("test4 " + res)
    println()
  }

  // defne before use
  case class Color(name: String, red: Int, green: Int, blue: Int)
  object ColorProtocol extends DefaultYamlProtocol {
    implicit val colorFormat = yamlFormat4(Color)
  }

  @Test
  def test5 = {
    import ColorProtocol._
    val yaml = Color("CadetBlue", 95, 158, 160).toYaml
    val color = yaml.convertTo[Color]
    println("test5 " + yaml + " color " + color)
    println()
  }

  private def getresource(name:String) : String = {
    scala.io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(name)).mkString
  }

}