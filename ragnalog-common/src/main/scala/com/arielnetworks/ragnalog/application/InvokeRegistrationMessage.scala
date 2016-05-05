package com.arielnetworks.ragnalog.application

import org.joda.time.DateTime

case class InvokeRegistrationMessage
(
  containerId: String,
  archiveId: String,
  archiveFileName: String,
  filePath: String,
  logType: String,
  extra: String,
  indexName: String,
  priority: Int,
  invokedTime: DateTime
)

case class AcceptedMessage()

case class NotAcceptedMessage()
