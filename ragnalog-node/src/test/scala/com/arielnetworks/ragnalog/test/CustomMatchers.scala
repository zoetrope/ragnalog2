package com.arielnetworks.ragnalog.test

import java.util

import org.scalatest.matchers.{MatchResult, Matcher}
import org.yaml.snakeyaml.Yaml

import scala.collection.JavaConversions._

//TODO: deprecated
import scala.collection.mutable

trait CustomMatchers {

  sealed trait Difference

  case class Differing(key: String, left: Object, right: Object) extends Difference

  case class LeftOnly(key: String, value: Object) extends Difference

  case class RightOnly(key: String, value: Object) extends Difference

  class SameYamlMatcher(expected: String, ignoreFields: Set[String] = Set.empty) extends Matcher[String] {

    private def checkEqual(left: java.util.Map[String, Object], right: java.util.Map[String, Object])(implicit diffs: mutable.ListBuffer[Difference]): Unit = {

      val onlyRight: mutable.Map[String, Object] = new util.HashMap[String, Object](right)

      for (entry <- left.entrySet()) {
        val leftKey = entry.getKey
        val leftValue = entry.getValue
        if (ignoreFields.contains(leftKey)) {
          if (right.contains(leftKey)) {
            onlyRight.remove(leftKey)
          }
        } else if (right.contains(leftKey)) {
          onlyRight.remove(leftKey)

          val rightValue = right.get(leftKey)

          if (leftValue.isInstanceOf[java.util.Map[_, _]] && rightValue.isInstanceOf[java.util.Map[_, _]]) {
            checkEqual(
              leftValue.asInstanceOf[java.util.Map[String, Object]],
              rightValue.asInstanceOf[java.util.Map[String, Object]])
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

      val expectedYaml = new Yaml().load(expected).asInstanceOf[java.util.Map[String, Object]]
      val actualYaml = new Yaml().load(left).asInstanceOf[java.util.Map[String, Object]]

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
