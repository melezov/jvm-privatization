package io.jvm.asm

import java.io.FileOutputStream

import org.apache.commons.io.IOUtils
import org.specs2.Specification

class PrivatizationSpec extends Specification {

  def is = s2"""
  Privatization should default access to your classes
    Package private class will stay the same                      $packagePrivateWillNotChange
    Public class change access to package private                 $publicWillChange
    Inner Private class will stay the same                        $innerPrivateWillNotChange
    Inner Package private class will stay the same                $packagePackagePrivateWillNotChange
    Inner Protected change access to package private              $innerProtectedWillNotChange
    Inner Public change access to package private                 $innerPublicWillChange
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

  def packagePrivateWillNotChange = {
    val className: String = "/classes/PackagePrivateClass.clazz"
    val originalClass = getResource(className)
    val testClass = originalClass.clone()
    (Privatization.apply(testClass) must beFalse) & (originalClass === testClass)
  }

  def publicWillChange = {
    val className: String = "/classes/PublicClass.clazz"
    val originalClass = getResource(className)
    val testClass = originalClass.clone()

    (Privatization.apply(testClass) must beTrue) & (originalClass !== testClass) & testJavap(testClass, className)
  }

  def innerPrivateWillNotChange = {
    val className: String = "/classes/PublicClass$InnerPrivateClass.clazz"
    val originalClass = getResource(className)
    val testClass = originalClass.clone()

    (Privatization.apply(testClass) must beFalse) & (originalClass === testClass)
  }

  def packagePackagePrivateWillNotChange = {
    val className: String = "/classes/PublicClass$InnerPackagePrivateClass.clazz"
    val originalClass = getResource(className)
    val testClass = originalClass.clone()

    (Privatization.apply(testClass) must beFalse) & (originalClass === testClass)
  }

  def innerProtectedWillNotChange = {
    val className: String = "/classes/PublicClass$InnerProtectedClass.clazz"
    val originalClass = getResource(className)
    val testClass = originalClass.clone()

    (Privatization.apply(testClass) must beTrue) & (originalClass !== testClass) & testJavap(testClass, className)
  }

  def innerPublicWillChange = {
    val className: String = "/classes/PublicClass$InnerPublicClass.clazz"
    val originalClass = getResource(className)
    val testClass = originalClass.clone()

    (Privatization.apply(testClass) must beTrue) & (originalClass !== testClass) & testJavap(testClass, className)
  }

}
