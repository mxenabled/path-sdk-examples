package com.mx.testing

import static org.mockito.Mockito.mock

import com.mx.path.core.context.RequestContext
import com.mx.path.core.context.Session
import com.mx.path.core.context.tracing.CustomTracer
import com.mx.testing.fakes.FakeEncryptionService
import com.mx.testing.fakes.FakeSessionRepository

import io.opentracing.Tracer

class TestUtils {
  static startFakedSession() {
    def sessionRepository = new FakeSessionRepository()
    def encryptionService = new FakeEncryptionService()
    Session.setRepositorySupplier({ -> sessionRepository })
    Session.setEncryptionServiceSupplier({ -> encryptionService })
    CustomTracer.setTracer(mock(Tracer))

    Session.createSession()
    Session.current().setUserId("userId")
    Session.current().setDeviceModel("iPhone")
    Session.current().setDeviceOperatingSystem("ios")
    Session.current().setDeviceOperatingSystemVersion("3.2.1")
    Session.current().setDeviceId("c8460d0b-191a-4f77-9114-00190d09ea94")

    RequestContext.builder().clientId("clientId").build().register()
  }

  static endFakedSession() {
    Session.setRepositorySupplier({ -> null })
    Session.setEncryptionServiceSupplier({ -> null })
    CustomTracer.setTracer(null)
    Session.clearSession()
    RequestContext.clear()
  }
}
