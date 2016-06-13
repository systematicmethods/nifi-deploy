package com.systemticmethods.nifi.deploy

import scala.collection.mutable

class YamlDocumentTest extends UnitTest {

  behavior of "Yaml Document"

  it should "read Example 2.1. Sequence of Scalars" in {
    val yamlstr = """- Mark McGwire
                    |- Sammy Sosa
                    |- Ken Griffey
                  """.stripMargin
    val doc = YamlDocument(yamlstr)
    //println(doc.chunk)
    assert(doc.chunk != null)
    assert(doc.chunk.isSequence())
    doc.chunk.sequence.map(arr => assertResult(arr(0)) {
      "Mark McGwire"
    })
    doc.chunk.sequence.map(arr => assertResult(arr(1)) {
      "Sammy Sosa"
    })
    doc.chunk.sequence.map(arr => assertResult(arr(2)) {
      "Ken Griffey"
    })
    val str = doc.chunk.iterator.get.mkString(",")
    assert(str == "Mark McGwire,Sammy Sosa,Ken Griffey")
  }

  it should "read Example 2.2.  Mapping Scalars to Scalars" in {
    val yamlstr = """hr:  65    # Home runs
                |avg: 0.278 # Batting average
                |rbi: 147   # Runs Batted In""".stripMargin
    val doc = YamlDocument(yamlstr)
    assert(doc.chunk != null)
    assert(doc.chunk.isMapping())
    doc.chunk.mapping.map(achunk => achunk("hr") == 65)
    doc.chunk.mapping.map(achunk => achunk("avg") == 0.278)
    doc.chunk.mapping.map(achunk => achunk("rbi") == 147)
    val str = doc.chunk.iterator.get.mkString(",")
    assert(str == "hr -> 65,avg -> 0.278,rbi -> 147")
  }

  it should "read Example 2.3.  Mapping Scalars to Sequences" in {
    val yamlstr = """american:
                    |  - Boston Red Sox
                    |  - Detroit Tigers
                    |  - New York Yankees
                    |national:
                    |  - New York Mets
                    |  - Chicago Cubs
                    |  - Atlanta Braves""".stripMargin
    val doc = YamlDocument(yamlstr)
    assert(doc.chunk != null)
    assert(doc.chunk.isMapping())
    val americanopt = doc.chunk.getMapping("american")
    //println(s"americanopt ${americanopt} Seq ${americanopt.get.datum.getClass.getName}")
    assert(americanopt.isDefined)
    assert(americanopt.get.isSequence())
    assert(americanopt.get.sequence.get.length == 3)
    val DetroitTigers = americanopt.flatMap(opt => opt.getSequence(1))
    assert(DetroitTigers.isDefined)
    assert(DetroitTigers.get.datum == "Detroit Tigers")
    //americanopt.map(opt => opt.)
  }

  it should "read Example 2.27. Invoice" in {
    val example = """---
                    |invoice: 34843
                    |date   : 2001-01-23
                    |bill-to: &id001
                    |    given  : Chris
                    |    family : Dumars
                    |    address:
                    |        lines: |
                    |            458 Walkman Dr.
                    |            Suite #292
                    |        city    : Royal Oak
                    |        state   : MI
                    |        postal  : 48046
                    |ship-to: *id001
                    |product:
                    |    - sku         : BL394D
                    |      quantity    : 4
                    |      description : Basketball
                    |      price       : 450.00
                    |    - sku         : BL4438H
                    |      quantity    : 1
                    |      description : Super Hoop
                    |      price       : 2392.00
                    |tax  : 251.42
                    |total: 4443.52
                    |comments:
                    |    Late afternoon is best.
                    |    Backup contact is Nancy
                    |    Billsmer @ 338-4338.""".stripMargin
    val doc = YamlDocument(example)
    assert(doc.chunk != null)
    assert(doc.chunk.isMapping())
    val product:Option[YamlChunk] = doc.chunk.getMapping("product")
    //println(s"product $product")
    //println(s"product ${product.get.datum.getClass.getName}")
    assert(product.isDefined)
    assert(product.get.isSequence())
    //product.foreach(obj => println(s"obj $obj"))
    val res = product.map(obj =>  obj.isSequence())
    assert(res.get == true)
    val sku = product.get.getSequence(0)
    assert(sku.isDefined)
    assert(sku.get.isMapping())
    assert(sku.get.getMapping("sku").get.isScalar())
    assert(sku.get.getMapping("sku").get.datum == "BL394D")
    assert(sku.get.getMapping("quantity").get.isScalar())
    assert(sku.get.getMapping("quantity").get.datum == 4)
  }


}
