package io.jvm.asm

import java.io.FileOutputStream

import org.apache.commons.io.IOUtils
import org.specs2.Specification

class PrivatizationSpec extends Specification {

  def is = s2"""
  Privatization should default access to your classes
    If your class is public           $ifPublic
    If your class is package          $ifPackage
    If your class is package private  $ifPackagePrivate
  """

  private def getResource(ba: String) = {
    val inputStream = getClass.getResourceAsStream(ba)
    try IOUtils.toByteArray(inputStream) finally inputStream.close()
  }

  private def testJavap(ba: Array[Byte], name: String) = {
    import scala.sys.process._
    val tmp = java.io.File.createTempFile(name, ".class")
    val fis = new FileOutputStream(tmp)
    fis.write(ba)
    fis.close()
    val javapResult = Seq("javap", tmp.getAbsolutePath).!!
    javapResult must not contain "public class"
  }

  def ifPublic = {
    val className: String = "/test/TestClassPublic"
    val testClass = getResource(className)
    (Privatization.apply(testClass) must beTrue) & testJavap(testClass, className)
  }

  def ifPackage = {
    val className: String = "/test/TestClassPackage"
    val testClass = getResource(className)
    (Privatization.apply(testClass) must beTrue) & testJavap(testClass, className)
  }

  def ifPackagePrivate = {
    val className: String = "/test/TestClass"
    val testClass = getResource(className)
    (!Privatization.apply(testClass) must beTrue) & testJavap(testClass, className)
  }
}
