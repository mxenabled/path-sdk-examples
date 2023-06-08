package com.mx.path.example.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mx.path.core.context.Session;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Inflates and loads the Session so that it can be used by the Gateway SDK.
 */
@Component
@Order(FilterOrder.SESSION_FILTER)
public class SessionFilter extends OncePerRequestFilter {
  /**
   * MDX Session Header Name
   */
  public static final String SESSION_HEADER = "mx-session-key";

  @Override
  protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String sessionId = request.getHeader(SESSION_HEADER);

    if (sessionId != null) {
      Session.loadSession(sessionId);
    }

    try {
      filterChain.doFilter(request, response);
      if (Session.current() != null) {
        Session.current().save();
      }
    } finally {
      Session.clearSession();
    }
  }
}
