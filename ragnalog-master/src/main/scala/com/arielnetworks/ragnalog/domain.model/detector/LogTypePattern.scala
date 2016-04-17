package com.arielnetworks.ragnalog.domain.model.detector

import java.util.regex.Pattern

case class LogTypePattern(priority: Int, pattern: Pattern, typeName: String)

