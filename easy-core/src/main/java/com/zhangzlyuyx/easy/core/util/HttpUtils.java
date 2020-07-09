package com.zhangzlyuyx.easy.core.util;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		final Result<String> retResult = new Result<>(true, "请求成功");
		//执行post请求
		Result<String> ret = httpPost(url, new ResultCallback<HttpPost>() {
			@Override
			public IResult<HttpPost> result(HttpPost httpPost) {
				//headers
				if(headers != null && headers.size() > 0) {
					for(Entry<String, String> kv : headers.entrySet()) {
						httpPost.addHeader(kv.getKey(), kv.getValue());
					}
				}
				MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
				multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				multipartEntityBuilder.setCharset(Charset.forName(DEFAULT_CHARSET));
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
				HttpEntity httpEntity = multipartEntityBuilder.build();
				httpPost.setEntity(httpEntity);
				return new Result<>(true, "", httpPost);
			}
		}, new ResultCallback<HttpResponse>() {
			
			@Override
			public IResult<HttpResponse> result(HttpResponse response) {
				
				try {
					HttpEntity httpEntity = response.getEntity();
					String body = EntityUtils.toString(httpEntity, DEFAULT_CHARSET);
					EntityUtils.consume(httpEntity);
					retResult.setData(body);
					return new Result<>(true, "请求成功!");
				} catch (Exception ex) {
					return new Result<>(false, ex.getMessage());
				}
			}
		});
		
		//关闭输入流
		if(closeStream) {
			try {
				inputStream.close();
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}
		
		if(!ret.isSuccess()) {
			return ret;
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
		final Result<String> retResult = new Result<>(true, "");
		Result<String> retGet = httpGet(url, headers, params, new ResultCallback<HttpResponse>() {
			@Override
			public IResult<HttpResponse> result(HttpResponse response) {
				try {
					HttpEntity responeEntity = response.getEntity();
					String data = EntityUtils.toString(responeEntity, DEFAULT_CHARSET);
					EntityUtils.consume(responeEntity);
					retResult.setData(data);
					return new Result<>(true, "", response);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					return new Result<>(false, e.getMessage());
				}
			}
		});
		if(!retGet.isSuccess()) {
			return retGet;
		}
		return retResult;
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
		try {
			URIBuilder uriBuilder = new URIBuilder(url);
			List<NameValuePair> parameters = new LinkedList<>();
			//params
			if(params != null && params.size() > 0) {
				for(Entry<String, String> kv : params.entrySet()) {
					parameters.add(new BasicNameValuePair(kv.getKey(), kv.getValue()));
				}
			}
			uriBuilder.setParameters(parameters);
			final Map<String, String> getHeaders = headers;
			return httpGet(uriBuilder.build().toString(), new ResultCallback<HttpGet>() {
				@Override
				public IResult<HttpGet> result(HttpGet httpGet) {
					//headers
					if(getHeaders != null && getHeaders.size() > 0) {
						for(Entry<String, String> kv : getHeaders.entrySet()) {
							httpGet.addHeader(kv.getKey(), kv.getValue());
						}
					}
					return new Result<>(true, "", httpGet);
				}
			}, responseCallback);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Result<>(false, e.getMessage());
		}
	}

	/**
	 * http GET 请求
	 * @param url
	 * @param getCallback
	 * @param responseCallback
	 * @return
	 */
	public static Result<String> httpGet(String url, ResultCallback<HttpGet> getCallback, ResultCallback<HttpResponse> responseCallback) {
		HttpGet httpGet = new HttpGet(url);
		if(getCallback != null) {
			getCallback.result(httpGet);
		}
		return httpRequest(httpGet, null, responseCallback);
	}
	
	/**
	 * http POST 请求
	 * @param url
	 * @param headers headers参数
	 * @param params params参数
	 * @return
	 */
	public static Result<String> httpPostReturnString(String url, Map<String, String> headers, Map<String, String> params) {
		final Result<String> retResult = new Result<>(true, "");
		Result<String> retGet = httpPost(url, headers, params, new ResultCallback<HttpResponse>() {
			@Override
			public IResult<HttpResponse> result(HttpResponse response) {
				try {
					HttpEntity responeEntity = response.getEntity();
					String data = EntityUtils.toString(responeEntity, DEFAULT_CHARSET);
					EntityUtils.consume(responeEntity);
					retResult.setData(data);
					return new Result<>(true, "", response);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					return new Result<>(false, e.getMessage());
				}
			}
		});
		if(!retGet.isSuccess()) {
			return retGet;
		}
		return retResult;
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
		try {
			List<NameValuePair> parameters = new LinkedList<>();
			//params
			if(params != null && params.size() > 0) {
				for(Entry<String, String> kv : params.entrySet()) {
					parameters.add(new BasicNameValuePair(kv.getKey(), kv.getValue()));
				}
			}
			final UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
			final Map<String, String> postHeaders = headers;
			return httpPost(url, new ResultCallback<HttpPost>() {
				@Override
				public IResult<HttpPost> result(HttpPost httpPost) {
					//headers
					if(postHeaders != null && postHeaders.size() > 0) {
						for(Entry<String, String> kv : postHeaders.entrySet()) {
							httpPost.addHeader(kv.getKey(), kv.getValue());
						}
					}
					//params
					httpPost.setEntity(formEntity);
					return new Result<>(true, "", httpPost);
				}
			}, responseCallback);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Result<>(false, e.getMessage());
		}
	}
	
	/**
	 * http POST 请求
	 * @param url
	 * @param postCallback
	 * @param responseCallback
	 * @return
	 */
	public static Result<String> httpPost(String url, ResultCallback<HttpPost> postCallback, ResultCallback<HttpResponse> responseCallback) {
		HttpPost httpPost = new HttpPost(url);
		if(postCallback != null) {
			postCallback.result(httpPost);
		}
		return httpRequest(httpPost, null, responseCallback);
	}
	
	/**
	 * http 请求
	 * @param httpRequest 请求对象
	 * @param configCallback 配置回调
	 * @param responseCallback 响应回调
	 * @return
	 */
	public static Result<String> httpRequest(HttpRequestBase httpRequest, ResultCallback<RequestConfig.Builder> configCallback, ResultCallback<HttpResponse> responseCallback) {
		CloseableHttpClient closeableHttpClient = null;
		CloseableHttpResponse closeableHttpResponse = null;
		try {
			//创建客户端
			closeableHttpClient = HttpClientBuilder.create().build();
			//创建请求配置
			RequestConfig.Builder configBuilder = RequestConfig.custom();
			if(configCallback != null) {
				configCallback.result(configBuilder);
			}
			//应用配置
			httpRequest.setConfig(configBuilder.build());
			//执行请求
			closeableHttpResponse = closeableHttpClient.execute(httpRequest);
			//判断相应状态
			if (closeableHttpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return new Result<>(false, closeableHttpResponse.getStatusLine().toString());
			}
			if(responseCallback != null) {
				responseCallback.result(closeableHttpResponse);
				return new Result<String>(true, "请求成功");
			} else {
				HttpEntity responeEntity = closeableHttpResponse.getEntity();
				String data = EntityUtils.toString(responeEntity, DEFAULT_CHARSET);
				EntityUtils.consume(responeEntity);
				return new Result<String>(true, "请求成功", data);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Result<>(false, e.getMessage());
		} finally {
			if(closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
					closeableHttpClient = null;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if(closeableHttpResponse != null) {
				try {
					closeableHttpResponse.close();
					closeableHttpResponse = null;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
