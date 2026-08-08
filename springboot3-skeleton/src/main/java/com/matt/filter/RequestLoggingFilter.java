package com.matt.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Filter for logging HTTP request and response details including:
 * - Request method, URI, headers, parameters, and body
 * - Response status and body
 * - Time elapsed for request processing
 *
 * <p>Multipart (file upload) requests are not cached to avoid
 * buffering large binary content into memory.</p>
 */
public class RequestLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    /**
     * Header name for user ID, typically set by the frontend or
     * decrypted from a token and appended by the business gateway.
     */
    private static final String USER_ID_HEADER = "X-User-Id";

    /**
     * Maximum request body bytes to cache for logging (1 MB).
     */
    private static final int REQUEST_CACHE_LIMIT = 1024 * 1024;

    private static final int MAX_PAYLOAD_LENGTH = 2000;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Skip body caching for multipart (file upload) requests
        boolean isMultipart = isMultipartRequest(httpRequest);

        ContentCachingRequestWrapper requestWrapper = isMultipart
                ? null
                : new ContentCachingRequestWrapper(httpRequest, REQUEST_CACHE_LIMIT);

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

        long startTime = System.currentTimeMillis();

        logRequest(httpRequest);

        try {
            chain.doFilter(requestWrapper != null ? requestWrapper : httpRequest, responseWrapper);
        } finally {
            long elapsed = System.currentTimeMillis() - startTime;

            // Log request body after chain execution (body is consumed during processing)
            if (requestWrapper != null) {
                logRequestBody(requestWrapper);
            } else {
                log.info("Request body logging skipped (multipart/file-upload)");
            }

            logResponse(responseWrapper, elapsed);

            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== Request ==========\n");
        sb.append("Method: ").append(request.getMethod()).append("\n");
        sb.append("URI: ").append(request.getRequestURI());

        String queryString = request.getQueryString();
        if (queryString != null) {
            sb.append("?").append(queryString);
        }
        sb.append("\n");

        // Log user ID from header (set by frontend or business gateway)
        String userId = request.getHeader(USER_ID_HEADER);
        if (userId != null && !userId.isBlank()) {
            sb.append("UserId: ").append(userId).append("\n");
        }

        // Log headers
        Map<String, String> headers = getRequestHeaders(request);
        if (!headers.isEmpty()) {
            sb.append("Headers: ").append(headers).append("\n");
        }

        // Log parameters (for query params and form data)
        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {
            sb.append("Parameters: ").append(toParameterString(params)).append("\n");
        }

        sb.append("=============================");
        log.info(sb.toString());
    }

    private void logRequestBody(ContentCachingRequestWrapper requestWrapper) {
        byte[] content = requestWrapper.getContentAsByteArray();
        if (content.length > 0) {
            String body = new String(content, StandardCharsets.UTF_8);
            if (body.length() > MAX_PAYLOAD_LENGTH) {
                body = body.substring(0, MAX_PAYLOAD_LENGTH) + "... (truncated)";
            }
            log.info("\n========== Request Body ==========\n{}\n==================================", body);
        }
    }

    private void logResponse(ContentCachingResponseWrapper response, long elapsed) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== Response ==========\n");
        sb.append("Status: ").append(response.getStatus()).append("\n");
        sb.append("Time Elapsed: ").append(elapsed).append("ms\n");

        // Log response body
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String body = new String(content, StandardCharsets.UTF_8);
            if (body.length() > MAX_PAYLOAD_LENGTH) {
                body = body.substring(0, MAX_PAYLOAD_LENGTH) + "... (truncated)";
            }
            sb.append("Body: ").append(body).append("\n");
        }

        sb.append("==============================");
        log.info(sb.toString());
    }

    private boolean isMultipartRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }

    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if (!isSensitiveHeader(name)) {
                headers.put(name, request.getHeader(name));
            }
        }
        return headers;
    }

    private boolean isSensitiveHeader(String headerName) {
        String lower = headerName.toLowerCase();
        return lower.contains("authorization")
                || lower.contains("cookie")
                || lower.contains("token");
    }

    private String toParameterString(Map<String, String[]> params) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append("=");
            if (entry.getValue().length == 1) {
                sb.append(entry.getValue()[0]);
            } else {
                sb.append("[").append(String.join(", ", entry.getValue())).append("]");
            }
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
}
