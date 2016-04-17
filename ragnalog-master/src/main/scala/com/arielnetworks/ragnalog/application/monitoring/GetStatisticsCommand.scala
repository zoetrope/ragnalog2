package com.arielnetworks.ragnalog.application.monitoring

case class GetStatisticsCommand
(
  index: String,
  typename: String,
  timeField: String
)
