package io.microservices.transaction.rest.filter;

import io.microservices.transaction.authentication.JwtTokenVerifier;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String TOKEN_PREFIX = "Bearer";
    private final JwtTokenVerifier tokenParser;

    public JwtAuthenticationFilter(JwtTokenVerifier tokenParser) {
        this.tokenParser = tokenParser;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        LOG.info("Request received. Path {}", request.getRequestURI());
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isNotEmpty(authHeader) && authHeader.startsWith(TOKEN_PREFIX)) {
            authHeader = authHeader.substring(6).trim();
            Authentication authentication = tokenParser.getAuthentication(authHeader);
            if (Objects.nonNull(authentication)) {
                LOG.info("Valid token. Process request");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }
}
