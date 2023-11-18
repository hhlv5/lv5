package com.lv5.lv5.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.StreamUtils;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "RequestLoggingWrapper")
public class RequestLoggingWrapper extends HttpServletRequestWrapper {

	private byte[] cachedInputStream;

	/**
	 * Constructs a request object wrapping the given request.
	 *
	 * @param request The request to wrap
	 * @throws IllegalArgumentException if the request is null
	 */
	public RequestLoggingWrapper(HttpServletRequest request) throws IOException {
		super(request);
		InputStream requestInputStream = request.getInputStream();
		// 캐시된 inputstream으로 read 해야 다음 filter에서도 읽을 수 있음,
		// request 안에 있는 inputstream 객체로 읽으면 안된다.
		this.cachedInputStream = StreamUtils.copyToByteArray(requestInputStream);
	}

	@Override
	public ServletInputStream getInputStream() {
		return new ServletInputStream() {
			private InputStream cachedBodyInputStream = new ByteArrayInputStream(cachedInputStream);

			@Override
			public boolean isFinished() {
				try {
					return cachedBodyInputStream.available() == 0;
				} catch (IOException e) {
					log.error("e.toString() :" + e.toString());
				}
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {
				throw new UnsupportedOperationException();
			}

			@Override
			public int read() throws IOException {
				return cachedBodyInputStream.read();
			}
		};
	}
}
