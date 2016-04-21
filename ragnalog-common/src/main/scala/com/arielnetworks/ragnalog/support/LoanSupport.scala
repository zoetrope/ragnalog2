package com.arielnetworks.ragnalog.support

import java.io.Closeable

trait LoanSupport {
  def using[A, R <: Closeable](r: R)(f: R => A): A = {
    try {
      f(r)
    } finally {
      r.close()
    }
  }
}

object LoanSupport extends LoanSupport
