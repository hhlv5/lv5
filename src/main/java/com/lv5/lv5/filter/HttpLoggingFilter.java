package com.lv5.lv5.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "HttpLoggingFilter")
@Order(0)
public class HttpLoggingFilter extends OncePerRequestFilter {

	private static void logRequest(RequestLoggingWrapper request) throws IOException {
		String queryString = "";
		if (request.getQueryString() != null) { // querystring이 존재하는 경우에만 받아오고 아니면 공백
			queryString = request.getQueryString();
		}
		log.info("Request : {} uri : {} content-type : {}", request.getMethod(), request.getRequestURI() + queryString,
			request.getContentType());
		logPayload("Request", request.getContentType(), request.getInputStream());
	}

	private static void logResponse(ContentCachingResponseWrapper response) throws IOException {
		logPayload("Response", response.getContentType(), response.getContentInputStream());
	}

	private static void logPayload(String prefix, String contentType, InputStream inputStream) throws IOException {
		boolean visible = isVisible(MediaType.valueOf(contentType == null ? "applycation/json" : contentType));
		if (visible) {
			byte[] content = StreamUtils.copyToByteArray(inputStream);
			if (content.length > 0) {
				String contentString = new String(content);
				log.info("{} Payload: {}", prefix, contentString);
			}
		} else {
			log.info("{} Payload: Binary Content", prefix);
		}
	}

	private static boolean isVisible(MediaType mediaType) {
		final List<MediaType> VISIBLE_TYPES = Arrays.asList(MediaType.APPLICATION_FORM_URLENCODED, // form type
			MediaType.APPLICATION_JSON, // json
			MediaType.APPLICATION_XML, // xml
			MediaType.TEXT_PLAIN, // text/plain
			MediaType.TEXT_HTML, // text/html
			MediaType.APPLICATION_FORM_URLENCODED // unrecoreded xxx form
		);

		return VISIBLE_TYPES.stream().anyMatch((visibleType) -> visibleType.includes(mediaType));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		MDC.put("traceId", UUID.randomUUID().toString()); // 현재 http 접속 id
		if (isAsyncDispatch(request)) { // 비동기 작업인 경우 그냥 넘긴다 근데 왜?
			filterChain.doFilter(request, response);
		} else {
			// 이미 사용되고있는 HttpServletRequestWrapper, ContentCachingResponseWrapper 를 사용하지 않도록 주의한다.
			// 필터 체인 안에서 이미 읽었던 request의 body나 response의 body를 다시 읽으려고 하면 내용이 비어있어서? EOF Exception 발생
			doFilterWrapped(new RequestLoggingWrapper(request), new ResponseLoggingWrapper(response), filterChain);
		}
		MDC.clear();
	}

	protected void doFilterWrapped(RequestLoggingWrapper request, ContentCachingResponseWrapper response,
		FilterChain filterChain) throws IOException, ServletException {
		try {
			logRequest(request);
			filterChain.doFilter(request, response);
		} finally { // 얘가 캐치 하면 안된다.
			logResponse(response);

			response.copyBodyToResponse();
		}

	}

}
