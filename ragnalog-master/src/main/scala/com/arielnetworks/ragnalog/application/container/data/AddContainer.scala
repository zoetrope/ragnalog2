package com.arielnetworks.ragnalog.application.container.data

case class AddContainerRequest
(
  id: String,
  name: String,
  description: Option[String]
)

case class ContainerResponse
(
  id: String,
  name: String,
  description: Option[String],
  status: String
)
