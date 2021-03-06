package com.arielnetworks.ragnalog.application.container.data

case class AddContainerRequest
(
  id: String,
  name: Option[String],
  description: Option[String]
)

case class UpdateContainerRequest
(
  name: String,
  description: Option[String]
)

case class ChangeContainerStatusRequest
(
  status: String
)

case class ContainerResponse
(
  id: String,
  name: String,
  description: Option[String],
  status: String
)
