package com.systemticmethods.nifi.deploy

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FlatSpec}

/**
  see http://doc.scalatest.org/2.2.6/#org.scalatest.FlatSpec
  */

@RunWith(classOf[JUnitRunner])
abstract class UnitTest extends FlatSpec with BeforeAndAfter {

}
