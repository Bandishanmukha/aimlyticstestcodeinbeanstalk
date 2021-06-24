package com.aimlytics.gateway.filters;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class RoutingFilter extends ZuulFilter {

	private static final Logger log = LoggerFactory.getLogger(RoutingFilter.class);
	@Value("${aws.accesskey}")
	private String accessKey;
	@Value("${aws.secretkey}")
	private String secretKey;
	@Value("${aws.endpointurl}")
	private String endPointUrl;
	@Value("${aws.bucket}")
	private String bucket;

	@Override
	public boolean shouldFilter() {
		RequestContext requestContext = RequestContext.getCurrentContext();
		String url = requestContext.getRequest().getRequestURL().toString();
		log.info("url");
		return url != null && (url.contains("/docs/"));
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext requestContext = RequestContext.getCurrentContext();
		try {
			String key = requestContext.getRequest().getRequestURL().toString();
			key = key.substring(key.indexOf("docs/") + 5, key.length());
			URL url = generatePresignedURL(key);
			MultiValueMap<String, String> queryMap = null;
			queryMap = UriComponentsBuilder.fromHttpUrl(URLDecoder.decode(url.toString(), "UTF-8")).build()
					.getQueryParams();
			log.info(">>> Query Map {}", queryMap);
			requestContext.setRequestQueryParams(
					queryMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
			log.info(">>>> URL :: {}", url);

		} catch (UnsupportedEncodingException e) {

		}

		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	private URL generatePresignedURL(String key) {
		log.info(" >>> Key  ::: {}", key);
		AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.AP_SOUTH_1).build();
		java.util.Date expiration = new java.util.Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 10;
		expiration.setTime(expTimeMillis);
		return s3.generatePresignedUrl(this.bucket, key, expiration,
				HttpMethod.GET);

	}

}
