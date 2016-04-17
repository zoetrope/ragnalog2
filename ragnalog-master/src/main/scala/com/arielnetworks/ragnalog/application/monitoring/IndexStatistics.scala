package com.arielnetworks.ragnalog.application.monitoring

import org.joda.time.DateTime

case class IndexStatistics
(
  count: Long,
  minDate: DateTime,
  maxDate: DateTime
)

