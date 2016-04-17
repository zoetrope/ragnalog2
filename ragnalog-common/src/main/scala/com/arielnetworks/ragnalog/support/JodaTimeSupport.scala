package com.arielnetworks.ragnalog.support

import org.joda.time.DateTime

object JodaTimeSupport {
  implicit def dateTimeOrdering: Ordering[DateTime] = Ordering.fromLessThan(_ isBefore _)
}
