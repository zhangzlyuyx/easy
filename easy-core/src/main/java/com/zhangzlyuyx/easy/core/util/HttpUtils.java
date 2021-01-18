package com.zhangzlyuyx.easy.core.util;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.zhangzlyuyx.easy.core.ActionCallback;
import com.zhangzlyuyx.easy.core.Constant;
import com.zhangzlyuyx.easy.core.IResult;
import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.core.ResultCallback;

/**
 * http 工具类
 * @author zhangzlyuyx
 *
 */
public class HttpUtils {
	
	private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
	
	public static String DEFAULT_CHARSET = "UTF-8";
	
	public static int DEFAULT_BUFFER_SIZE = 1024;

	/**
	 * http 文件上传
	 * @param url
	 * @param headers
	 * @param params
	 * @param filename
	 * @param inputStream
	 * @param closeStream
	 * @return
	 */
	public static Result<String> httpFileUpload(String url, final Map<String, String> headers, final Map<String, String> params, final String filename, final InputStream inputStream, final boolean closeStream){

		//multipartEntityBuilder
		MultipartEntityBuilder multipartEntityBuilder = HttpClientRequest.createMultipartEntityBuilder();
		//params
		if(params != null && params.size() > 0) {
			for(Entry<String, String> kv : params.entrySet()) {
				multipartEntityBuilder.addTextBody(kv.getKey(), kv.getValue());
			}
		}
		//file
		if(inputStream != null) {
			multipartEntityBuilder.addBinaryBody("file", inputStream, ContentType.DEFAULT_BINARY, filename);
		}
		HttpEntity postEntity = multipartEntityBuilder.build();
		HttpClientRequest httpClientRequest = HttpClientRequest.createHttpPost(url, headers, postEntity);
		
		//执行post请求
		Result<String> retResult = httpRequestReturnString(httpClientRequest);
		
		//关闭输入流
		if(closeStream) {
			try {
				inputStream.close();
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}
		
		if(!retResult.isSuccess()) {
			return retResult;
		}
		
		return retResult;
	}
	
	/**
	 * http 文件下载
	 * @param url
	 * @param headers
	 * @param params
	 * @param outputStream
	 * @param closeStream
	 * @param headersCallback
	 * @return
	 */
	public static Result<String> httpFileDownload(final String url, final Map<String, String> headers, final Map<String, String> params, final OutputStream outputStream, final boolean closeStream, final ResultCallback<Map<String, String>> headersCallback) {
		//执行 get 请求
		Result<String> ret = httpGet(url, headers, params, new ResultCallback<HttpResponse>() {
			
			@Override
			public IResult<HttpResponse> result(HttpResponse response) {
				//status
				if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					return new Result<>(false, response.getStatusLine().toString());
				}
				InputStream inputStream = null;
				BufferedOutputStream bufferedOutputStream = null;
				try {
					if(headersCallback != null) {
						Map<String, String> responseHeaders = new HashMap<>();
						for(Header header : response.getAllHeaders()) {
							responseHeaders.put(header.getName(), header.getValue());
						}
						headersCallback.result(responseHeaders);
					}
					HttpEntity httpEntity = response.getEntity();
					inputStream = httpEntity.getContent();
					bufferedOutputStream = new BufferedOutputStream(outputStream);
					byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
					int i = -1;   
			        while ((i = inputStream.read(buffer)) != -1) {   
			        	bufferedOutputStream.write(buffer, 0, i);  
			        }
			        bufferedOutputStream.flush();
					EntityUtils.consume(httpEntity);
					return new Result<>(true, "");
				} catch (Exception e) {
					return new Result<>(false, e.getMessage());
				} finally {
					if(inputStream != null) {
						try {
							inputStream.close();
						} catch (Exception ex) {
							log.error(ex.getMessage(), ex);
						}
					}
					if(closeStream) {
						try {
							bufferedOutputStream.close();
						} catch (Exception ex) {
							log.error(ex.getMessage(), ex);
						}
					}
				}
			}
		});
		
		if(!ret.isSuccess()) {
			return ret;
		}
		
		return new Result<>(true, "请求成功!");
	}
	
	/**
	 * http GET 请求
	 * @param url
	 * @param headers headers参数
	 * @param params params参数
	 * @return
	 */
	public static Result<String> httpGetReturnString(String url, Map<String, String> headers, Map<String, String> params) {
		HttpClientRequest httpClientRequest = HttpClientRequest.createHttpGet(url, headers, params);
		return httpRequestReturnString(httpClientRequest);
	}
	
	/**
	 * http GET 请求
	 * @param url
	 * @param headers headers参数
	 * @param params params参数
	 * @return
	 */
	public static Result<JSONObject> httpGetReturnJson(String url, Map<String, String> headers, Map<String, String> params) {
		HttpClientRequest httpClientRequest = HttpClientRequest.createHttpGet(url, headers, params);
		return httpRequestReturnJson(httpClientRequest);
	}
	
	/**
	 * http GET 请求
	 * @param url
	 * @param headers
	 * @param responseCallback
	 * @return
	 */
	public static Result<String> httpGet(String url, Map<String, String> headers, ResultCallback<HttpResponse> responseCallback) {
		HttpClientRequest httpClientRequest = HttpClientRequest.createHttpGet(url, headers, null);
		return httpRequest(httpClientRequest, responseCallback);
	}
	
	/**
	 * http GET 请求
	 * @param url
	 * @param headers headers参数
	 * @param params params参数
	 * @param responseCallback 响应回调
	 * @return
	 */
	public static Result<String> httpGet(String url, Map<String, String> headers, Map<String, String> params, ResultCallback<HttpResponse> responseCallback) {
		HttpClientRequest httpClientRequest = HttpClientRequest.createHttpGet(url, headers, params);
		return httpRequest(httpClientRequest, responseCallback);
	}
	
	/**
	 * http POST 请求
	 * @param url
	 * @param headers headers参数
	 * @param params params参数
	 * @return
	 */
	public static Result<String> httpPostReturnString(String url, Map<String, String> headers, Map<String, String> params) {
		HttpClientRequest httpClientRequest = HttpClientRequest.createHttpPost(url, headers, params);
		return httpRequestReturnString(httpClientRequest);
	}
	
	/**
	 * http POST 请求
	 * @param url
	 * @param headers headers参数
	 * @param params params参数
	 * @return
	 */
	public static Result<JSONObject> httpPostReturnJson(String url, Map<String, String> headers, Map<String, String> params) {
		HttpClientRequest httpClientRequest = HttpClientRequest.createHttpPost(url, headers, params);
		return httpRequestReturnJson(httpClientRequest);
	}
	
	/**
	 * http POST 请求
	 * @param url
	 * @param headers headers参数
	 * @param params params参数
	 * @param responseCallback 响应回调
	 * @return
	 */
	public static Result<String> httpPost(String url, Map<String, String> headers, Map<String, String> params, ResultCallback<HttpResponse> responseCallback) {
		HttpClientRequest httpClientRequest = HttpClientRequest.createHttpPost(url, headers, params);
		return httpRequest(httpClientRequest, responseCallback);
	}
	
	/**
	 * http POST 请求
	 * @param url
	 * @param headers
	 * @param postEntity
	 * @param responseCallback
	 * @return
	 */
	public static Result<String> httpPost(String url, Map<String, String> headers, HttpEntity postEntity, ResultCallback<HttpResponse> responseCallback) {
		HttpClientRequest httpClientRequest = HttpClientRequest.createHttpPost(url, headers, postEntity);
		return httpRequest(httpClientRequest, responseCallback);
	}
	
	/**
	 * http PUT 请求
	 * @param url
	 * @param headers
	 * @param httpEntity
	 * @param responseCallback
	 * @return
	 */
	public static Result<String> httpPut(String url, Map<String, String> headers, HttpEntity putEntity, ResultCallback<HttpResponse> responseCallback) {
		HttpClientRequest httpClientRequest = HttpClientRequest.createHttpPut(url, headers, putEntity);
		return httpRequest(httpClientRequest, responseCallback);
	}
	
	/**
	 * http DELETE 请求
	 * @param url
	 * @param headers
	 * @param responseCallback
	 * @return
	 */
	public static Result<String> httpDelete(String url, Map<String, String> headers, HttpEntity deleteEntity, ResultCallback<HttpResponse> responseCallback) {
		HttpClientRequest httpClientRequest = HttpClientRequest.createHttpDelete(url, headers, deleteEntity);
		return httpRequest(httpClientRequest, responseCallback);
	}
	
	/**
	 * http 请求
	 * @param httpRequest 请求对象
	 * @param configCallback 配置回调
	 * @param responseCallback 响应回调
	 * @return
	 */
	public static Result<String> httpRequest(HttpRequestBase httpRequest, final ResultCallback<RequestConfig.Builder> requestConfigCallback, ResultCallback<HttpResponse> responseCallback) {
		HttpClientRequest httpClientRequest = new HttpClientRequest(httpRequest);
		if(requestConfigCallback != null) {
			httpClientRequest.setRequestConfigBuilderCallback(new ActionCallback<RequestConfig.Builder>() {
				
				@Override
				public void action(Builder obj) {
					
					requestConfigCallback.result(obj);
				}
			});
		}
		return httpRequest(httpClientRequest, responseCallback);
	}
	
	/**
	 * http 请求 json
	 * @param httpClientRequest 请求参数
	 * @return
	 */
	public static Result<JSONObject> httpRequestReturnJson(HttpClientRequest httpClientRequest) {
		Result<String> ret = httpRequestReturnString(httpClientRequest);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		try {
			JSONObject json = JSONObject.parseObject(ret.getData());
			return new Result<JSONObject>(true, "", json);
		} catch (Exception e) {
			log.error("", e);
			return new Result<>(false, e.getMessage());
		}
	}
	
	/**
	 * http 请求 string
	 * @param httpClientRequest 请求参数
	 * @return
	 */
	public static Result<String> httpRequestReturnString(HttpClientRequest httpClientRequest) {
		return httpRequest(httpClientRequest, null);
	}
	
	/**
	 * http 请求
	 * @param httpClientRequest 请求参数
	 * @return
	 */
	public static Result<String> httpRequest(HttpClientRequest httpClientRequest, final ResultCallback<HttpResponse> responseCallback) {
		final Result<String> retResult = new Result<>(false, "");
		httpClientRequest.setResponseCallback(new ActionCallback<HttpClientResponse>() {
			
			@Override
			public void action(HttpClientResponse httpClientResponse) {
				
				if(responseCallback != null) {
					IResult<HttpResponse> ret = responseCallback.result(httpClientResponse.getHttpResponse());
					if(!ret.isSuccess()) {
						retResult.setCode(false);
						retResult.setMsg(ret.getMsg());
					} else {
						retResult.setCode(true);
						retResult.setMsg("");
					}
				} else {
					try {
						HttpEntity responseEntity = httpClientResponse.getHttpEntity();
						String body = EntityUtils.toString(responseEntity, DEFAULT_CHARSET);
						EntityUtils.consume(responseEntity);
						retResult.setCode(true);
						retResult.setData(body);
					} catch (Exception e) {
						retResult.setCode(false);
						retResult.setMsg(e.getMessage());
					}
				}
			}
		});
		Result<HttpClientResponse> ret = httpRequest(httpClientRequest);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		return retResult;
	}
	
	/**
	 * http 请求
	 * @param httpClientRequest 请求参数
	 * @return
	 */
	public static Result<HttpClientResponse> httpRequest(HttpClientRequest httpClientRequest) {
		
		HttpClientResponse httpClientResponse = new HttpClientResponse();
		
		try {
			//httpClientBuilder
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			
			//clientBuilderCallback
			if(httpClientRequest.getClientBuilderCallback() != null) {
				httpClientRequest.getClientBuilderCallback().action(httpClientBuilder);
			}	
			
			//httpClient
			CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
			httpClientRequest.setHttpClient(closeableHttpClient);
			
			//requestConfig
			RequestConfig.Builder configBuilder = RequestConfig.custom();
			
			//RequestConfigBuilderCallback
			if(httpClientRequest.getRequestConfigBuilderCallback() != null) {
				httpClientRequest.getRequestConfigBuilderCallback().action(configBuilder);
			}
			httpClientRequest.getHttpRequest().setConfig(configBuilder.build());
			
			//RequestCallback
			if(httpClientRequest.getRequestCallback() != null) {
				httpClientRequest.getRequestCallback().action(httpClientRequest);
			}
			
			//execute
			CloseableHttpResponse closeableHttpResponse = null;
			if(httpClientRequest.getHttpContext() != null) {
				closeableHttpResponse = closeableHttpClient.execute(httpClientRequest.getHttpRequest(), httpClientRequest.getHttpContext());
			} else {
				closeableHttpResponse = closeableHttpClient.execute(httpClientRequest.getHttpRequest());
			}
			httpClientResponse.setHttpResponse(closeableHttpResponse);
			
			//终止请求
			if(!httpClientResponse.isSuccessful()) {
				httpClientRequest.getHttpRequest().abort();
			}
			
			//记录重定向地址
			if(httpClientRequest.getHttpContext() != null && closeableHttpResponse != null && !closeableHttpResponse.containsHeader(Constant.HTTP_HEADER_Referer)) {
				try {
					HttpHost httpHost = (HttpHost)httpClientRequest.getHttpContext().getAttribute(ExecutionContext.HTTP_TARGET_HOST);
					HttpUriRequest httpUriRequest = (HttpUriRequest)httpClientRequest.getHttpContext().getAttribute(ExecutionContext.HTTP_REQUEST);
					closeableHttpResponse.setHeader(Constant.HTTP_HEADER_Referer, httpUriRequest.getURI().isAbsolute() ? httpUriRequest.getURI().toString() : (httpHost.toURI() + httpUriRequest.getURI().toString()));
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			//ResponseCallback
			if(httpClientRequest.getResponseCallback() != null) {
				httpClientRequest.getResponseCallback().action(httpClientResponse);
			}
			
			//return
			return new Result<HttpUtils.HttpClientResponse>(httpClientResponse.isSuccessful(), closeableHttpResponse.getStatusLine().toString(), httpClientResponse);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Result<>(false, e.getMessage());
		} finally {
			httpClientResponse.close();
			httpClientRequest.close();
		}
	}
	
	/**
	 * http 客户端请求参数
	 *
	 */
	public static class HttpClientRequest implements Closeable {
		
		/**
		 * http 请求对象(HttpGet/HttpPost/HttpPut/HttpDelete)
		 */
		private HttpRequestBase httpRequest;
		
		public HttpRequestBase getHttpRequest() {
			return this.httpRequest;
		}
		
		public void setHttpRequest(HttpRequestBase httpRequest) {
			this.httpRequest = httpRequest;
		}
		
		private HttpContext httpContext = new BasicHttpContext();
		
		public HttpContext getHttpContext() {
			return this.httpContext;
		}
		
		public void setHttpContext(HttpContext httpContext) {
			this.httpContext = httpContext;
		}
		
		private CloseableHttpClient httpClient;
		
		public CloseableHttpClient getHttpClient() {
			return this.httpClient;
		}
		
		public void setHttpClient(CloseableHttpClient httpClient) {
			this.httpClient = httpClient;
		}
		
		/**
		 * 客户端构建回调
		 */
		private ActionCallback<HttpClientBuilder> clientBuilderCallback;
		
		public ActionCallback<HttpClientBuilder> getClientBuilderCallback() {
			return clientBuilderCallback;
		}
		
		public void setClientBuilderCallback(ActionCallback<HttpClientBuilder> clientBuilderCallback) {
			this.clientBuilderCallback = clientBuilderCallback;
		}
		
		/**
		 *请求 配置构建回调
		 */
		private ActionCallback<RequestConfig.Builder> requestConfigBuilderCallback;
		
		public ActionCallback<RequestConfig.Builder> getRequestConfigBuilderCallback() {
			return requestConfigBuilderCallback;
		}
		
		public void setRequestConfigBuilderCallback(ActionCallback<RequestConfig.Builder> requestConfigBuilderCallback) {
			this.requestConfigBuilderCallback = requestConfigBuilderCallback;
		}
		
		/**
		 * 请求回调
		 */
		private ActionCallback<HttpClientRequest> requestCallback;
		
		public ActionCallback<HttpClientRequest> getRequestCallback() {
			return this.requestCallback;
		}
		
		public void setRequestCallback(ActionCallback<HttpClientRequest> requestCallback) {
			this.requestCallback = requestCallback;
		}
		
		/**
		 * 响应回调
		 */
		private ActionCallback<HttpClientResponse> responseCallback;
		
		public ActionCallback<HttpClientResponse> getResponseCallback() {
			return this.responseCallback;
		}
		
		public void setResponseCallback(ActionCallback<HttpClientResponse> responseCallback) {
			this.responseCallback = responseCallback;
		}
		
		/**
		 * 初始化
		 * @param httpRequest
		 */
		public HttpClientRequest(HttpRequestBase httpRequest) {
			this.httpRequest = httpRequest;
		}

		@Override
		public void close() {
			//httpRequest
			if(this.httpRequest != null) {
				try {
					this.httpRequest.releaseConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.httpRequest = null;
			}
			//httpClient
			if(this.httpClient != null) {
				try {
					this.httpClient.getConnectionManager().shutdown();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					this.httpClient.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.httpClient = null;
			}
		}
		
		/**
		 * 创建 http GET 请求
		 * @param uri
		 * @param headers
		 * @param params
		 * @return
		 */
		public static HttpClientRequest createHttpGet(String uri, Map<String, String> headers, Map<String, String> params) {
			try {
				//params
				if(params != null && params.size() > 0) {
					URIBuilder uriBuilder = new URIBuilder(uri);
					for(Entry<String, String> kv : params.entrySet()) {
						uriBuilder.addParameter(kv.getKey(), kv.getValue());
					}
					uri = uriBuilder.toString();
				}
				HttpGet httpGet = new HttpGet(uri);
				HttpClientRequest httpClientRequest = new HttpClientRequest(httpGet);
				httpClientRequest.loadHeaders(headers);
				return httpClientRequest;
			} catch (Exception e) {
				log.error("", e);
				return null;
			}
		}
		
		/**
		 *创建 http POST 请求
		 * @param uri
		 * @param headers
		 * @param params
		 * @return
		 * @throws UnsupportedEncodingException
		 */
		public static HttpClientRequest createHttpPost(String uri, Map<String, String> headers, Map<String, String> params) {
			try {
				UrlEncodedFormEntity formEntity = createUrlEncodedFormEntity(params);
				return createHttpPost(uri, headers, formEntity);
			} catch (Exception e) {
				log.error("", e);
				return null;
			}
		}
		
		/**
		 * 创建 http POST 请求
		 * @param uri
		 * @param headers
		 * @param postEntity
		 * @return
		 */
		public static HttpClientRequest createHttpPost(String uri, Map<String, String> headers, HttpEntity postEntity) {
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setEntity(postEntity);
			HttpClientRequest httpClientRequest = new HttpClientRequest(httpPost);
			httpClientRequest.loadHeaders(headers);
			return httpClientRequest;
		}
		
		/**
		 * 创建 http PUT 请求
		 * @param uri
		 * @param headers
		 * @param putEntity
		 * @return
		 */
		public static HttpClientRequest createHttpPut(String uri, Map<String, String> headers, HttpEntity putEntity) {
			HttpPut httpPut = new HttpPut(uri);
			httpPut.setEntity(putEntity);
			HttpClientRequest httpClientRequest = new HttpClientRequest(httpPut);
			httpClientRequest.loadHeaders(headers);
			return httpClientRequest;
		}
		
		/**
		 * 创建 http DELETE 请求
		 * @param uri
		 * @param headers
		 * @param deleteEntity
		 * @return
		 */
		public static HttpClientRequest createHttpDelete(String uri, Map<String, String> headers, HttpEntity deleteEntity) {
			try {
				HttpRequestBase httpRequest = null;
				if(deleteEntity == null) {
					httpRequest = new HttpDelete(uri);
				} else {
					httpRequest = new HttpEntityEnclosingRequestBase() {
						@Override
						public String getMethod() {
							return "DELETE";
						}
					};
					httpRequest.setURI(new URI(uri));
				}
				HttpClientRequest httpClientRequest = new HttpClientRequest(httpRequest);
				httpClientRequest.loadHeaders(headers);
				return httpClientRequest;
			} catch (Exception e) {
				log.error("", e);
				return null;
			}
		}
		
		/**
		 * UrlEncodedFormEntity
		 * @param params
		 * @return
		 * @throws UnsupportedEncodingException
		 */
		public static UrlEncodedFormEntity createUrlEncodedFormEntity(Map<String, String> params) throws UnsupportedEncodingException {
			return createUrlEncodedFormEntity(params, DEFAULT_CHARSET);
		}
		
		/**
		 * UrlEncodedFormEntity
		 * @param params
		 * @param charset
		 * @return
		 * @throws UnsupportedEncodingException
		 */
		public static UrlEncodedFormEntity createUrlEncodedFormEntity(Map<String, String> params, String charset) throws UnsupportedEncodingException {
			List<NameValuePair> parameters = new LinkedList<>();
			//params
			if(params != null && params.size() > 0) {
				for(Entry<String, String> kv : params.entrySet()) {
					parameters.add(new BasicNameValuePair(kv.getKey(), kv.getValue()));
				}
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, charset);
			return formEntity;
		}
		
		/**
		 * StringEntity
		 * @param string
		 * @return
		 */
		public static StringEntity createStringEntity(String string) {
			return createStringEntity(string, DEFAULT_CHARSET);
		}
		
		/**
		 * StringEntity
		 * @param string
		 * @param charset
		 * @return
		 */
		public static StringEntity createStringEntity(String string, String charset) {
			return new StringEntity(string, charset);
		}
		
		/**
		 * MultipartEntityBuilder
		 * @return
		 */
		public static MultipartEntityBuilder createMultipartEntityBuilder() {
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			//浏览器兼容模式
			multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			//请求的编码格式
			multipartEntityBuilder.setCharset(Charset.forName(DEFAULT_CHARSET));
			multipartEntityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
			return multipartEntityBuilder;
		}
		
		/**
		 * 加载 headers
		 * @param headers
		 */
		public void loadHeaders(Map<String, String> headers) {
			//if(this.httpRequest != null) {
			//HACK:fix
			if(this.httpRequest == null) {
				return;
			}
			//headers
			if(headers != null && headers.size() > 0) {
				for(Entry<String, String> kv : headers.entrySet()) {
					this.httpRequest.addHeader(kv.getKey(), kv.getValue());
				}
			}
		}
	}
	
	/**
	 * http 客户端响应
	 *
	 */
	public static class HttpClientResponse implements Closeable {

		private CloseableHttpResponse httpResponse;
		
		public CloseableHttpResponse getHttpResponse() {
			return this.httpResponse;
		}
		
		public void setHttpResponse(CloseableHttpResponse httpResponse) {
			this.httpResponse = httpResponse;
		}
		
		/**
		 * 状态
		 * @return
		 */
		public boolean isSuccessful() {
			if(this.httpResponse == null) {
				return false;
			}
			StatusLine statusLine = this.httpResponse.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
				return true;
			}
			return false;
		}
		
		public HttpEntity getHttpEntity() {
			if(this.httpResponse == null) {
				return null;
			}
			HttpEntity httpEntity = this.httpResponse.getEntity();
			return httpEntity;
		}
		
		@Override
		public void close() {
			if(this.httpResponse != null) {
				try {
					this.httpResponse.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.httpResponse = null;
			}
		}
		
	}
	
	/**
	 * 设置 url 参数信息
	 * @param url
	 * @param params
	 * @return
	 */
	public static Result<String> setUrlParameter(String url, Map<String, String> params) {
		try {
			URIBuilder uriBuilder = new URIBuilder(url != null ? url : "");
			for(Entry<String, String> kv : params.entrySet()) {
				uriBuilder.addParameter(kv.getKey(), kv.getValue());
			}
			return new Result<String>(true, "", uriBuilder.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Result<>(false, e.getMessage(), url);
		}
	}
	
	/**
	 * 设置 url 用户信息
	 * @param url
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	public static Result<String> setUrlUserInfo(String url, String username, String password){
		try {
			URIBuilder uriBuilder = new URIBuilder(url != null ? url : "");
			uriBuilder.setUserInfo(username != null ? username : "", password != null ? password : "");
			return new Result<String>(true, "", uriBuilder.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Result<>(false, e.getMessage(), url);
		}
	}
	
	/**
	 * 获取 http 响应  header信息
	 * @param response
	 * @param headerName
	 * @return
	 */
	public static Header getHttpResponseHeader(HttpResponse response, String headerName) {
		if(response == null) {
			return null;
		}
		return response.getFirstHeader(headerName);
	}
	
	/**
	 * 获取 http 响应  header值
	 * @param response
	 * @param headerName
	 * @return
	 */
	public static String getHttpResponseHeaderValue(HttpResponse response, String headerName) {
		Header header = getHttpResponseHeader(response, headerName);
		return header != null ? header.getValue() : null;
	}
	
	/**
	 * 获取 http 响应 header 元素列表
	 * @param response
	 * @param headerName
	 * @return
	 */
	public static HeaderElement[] getHttpResponseHeaderElements(HttpResponse response, String headerName) {
		Header header = getHttpResponseHeader(response, headerName);
		return header != null ? header.getElements(): null;
	}
}
