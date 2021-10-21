package com.example.gateway.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author vigoss
 * @date 2019/2/1
 * 密码解密工具类
 */
@Slf4j
@Component
public class PasswordDecoderFilter extends AbstractGatewayFilterFactory {
	private static final String PASSWORD = "password";
	private static final String KEY_ALGORITHM = "AES";
	@Value("${security.encode.key:1234567812345678}")
	private String encodeKey;

	private static String decryptAES(String data, String pass) {
		AES aes = new AES(Mode.CBC, Padding.NoPadding,
			new SecretKeySpec(pass.getBytes(StandardCharsets.UTF_8), KEY_ALGORITHM),
			new IvParameterSpec(pass.getBytes(StandardCharsets.UTF_8)));
		byte[] result = aes.decrypt(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));
		return new String(result, StandardCharsets.UTF_8);
	}

	@Override
	public GatewayFilter apply(Object config) {
		return null;
	}
}
