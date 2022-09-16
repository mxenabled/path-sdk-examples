package com.mx.path.example.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mx.path.model.context.RequestContext;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Inflates and registers the RequestContext so it can be used by the Gateway SDK.
 */
@Component
@Order(FilterOrder.REQUEST_CONTEXT_FILTER)
public final class MdxRequestContextFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String clientId = null;
    String path = request.getRequestURI();
    if (path != null) {
      String[] urlParts = path.split("/");
      clientId = urlParts[1];
    }

    RequestContext.builder()
        .clientId(clientId)
        .path(path)
        .build()
        .register();

    try {
      filterChain.doFilter(request, response);
    } finally {
      RequestContext.clear();
    }
  }
}
