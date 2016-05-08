package com.arielnetworks.ragnalog.test

import java.util

import org.scalatest.matchers.{MatchResult, Matcher}
import org.yaml.snakeyaml.Yaml

import scala.collection.JavaConverters._
import scala.collection.mutable

trait CustomMatchers {

  sealed trait Difference

  case class Differing(key: String, left: Object, right: Object) extends Difference

  case class LeftOnly(key: String, value: Object) extends Difference

  case class RightOnly(key: String, value: Object) extends Difference

  class SameYamlMatcher(expected: String, ignoreFields: Set[String] = Set.empty) extends Matcher[String] {

    private def checkEqual(left: util.Map[String, Object], right: util.Map[String, Object])(implicit diffs: mutable.ListBuffer[Difference]): Unit = {

      val onlyRight: mutable.Map[String, Object] = new util.HashMap[String, Object](right).asScala

      for (entry <- left.entrySet().asScala) {
        val leftKey = entry.getKey
        val leftValue = entry.getValue
        if (ignoreFields.contains(leftKey)) {
          if (right.containsKey(leftKey)) {
            onlyRight.remove(leftKey)
          }
        } else if (right.containsKey(leftKey)) {
          onlyRight.remove(leftKey)

          val rightValue = right.get(leftKey)

          if (leftValue.isInstanceOf[util.Map[_, _]] && rightValue.isInstanceOf[util.Map[_, _]]) {
            checkEqual(
              leftValue.asInstanceOf[util.Map[String, Object]],
              rightValue.asInstanceOf[util.Map[String, Object]])
          } else if (rightValue != leftValue) {
            diffs += new Differing(leftKey, leftValue, rightValue)
          }
        } else {
          diffs += new LeftOnly(leftKey, leftValue)
        }
      }
      onlyRight.foreach(r => diffs += new RightOnly(r._1, r._2))
    }


    override def apply(left: String): MatchResult = {

      val expectedYaml = new Yaml().load(expected).asInstanceOf[util.Map[String, Object]]
      val actualYaml = new Yaml().load(left).asInstanceOf[util.Map[String, Object]]

      val diff = new mutable.ListBuffer[Difference]
      checkEqual(expectedYaml, actualYaml)(diff)

      diff.foreach(println)

      MatchResult(
        diff.isEmpty,
        s"wasNotSameYaml\n${diff.map(_.toString).mkString("\n")}",
        "wasSameYaml"
      )
    }
  }

  def sameYaml(expected: String, ignoreFields: Set[String] = Set.empty) = new SameYamlMatcher(expected, ignoreFields)
}

object CustomMatchers extends CustomMatchers
