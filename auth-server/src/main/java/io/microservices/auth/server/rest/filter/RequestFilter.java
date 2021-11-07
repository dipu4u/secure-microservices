package io.microservices.auth.server.rest.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class RequestFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        LOG.info("Request received {}, auth type {}", request.getRequestURI(), request.getAuthType());
        Enumeration<String> headers = request.getHeaderNames();
        while(headers.hasMoreElements()) {
            String name = headers.nextElement();
            LOG.debug("Header name {} = {}", name, request.getHeader(name));
        }
        filterChain.doFilter(request, response);
    }

}
