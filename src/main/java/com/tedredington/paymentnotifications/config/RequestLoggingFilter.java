package com.tedredington.paymentnotifications.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {

            String headers = Collections.list(wrappedRequest.getHeaderNames())
                    .stream()
                    .collect(Collectors.toMap(name -> name, wrappedRequest::getHeader, (a, b) -> a))
                    .toString();

            String requestBody = getBody(wrappedRequest.getContentAsByteArray(), wrappedRequest.getCharacterEncoding());
            String responseBody = getBody(wrappedResponse.getContentAsByteArray(), wrappedResponse.getCharacterEncoding());

            logger.info("REQUEST: {} {} Headers: {} Body: {}",
                    wrappedRequest.getMethod(),
                    wrappedRequest.getRequestURI(),
                    headers,
                    formatBody(requestBody));

            logger.info("RESPONSE: {} Body: {}",
                    wrappedResponse.getStatus(),
                    formatBody(responseBody));

            wrappedResponse.copyBodyToResponse();
        }
    }

    private String getBody(byte[] content, String encoding){
        if (content.length == 0) return "";
        try {
            return new String(content, encoding != null ? encoding : StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            logger.warn("Could not decode body", e);
            return "[decode error]";
        }
    }

    private String formatBody(String body) {
        if (body == null || body.isBlank()) return "[empty]";
        try {
            Object json = objectMapper.readValue(body, Object.class);
            return "\n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Exception e) {
            return body; // not JSON, just return as-is
        }
    }
}