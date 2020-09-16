package com.zhangzlyuyx.easy.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.core.ActionResultCallback;
import com.zhangzlyuyx.easy.core.Result;

import cn.hutool.core.util.NetUtil;

/**
 * 网络 - 工具类
 * @author zhangzlyuyx
 *
 */
public class NetUtils {

	private static final Logger log = LoggerFactory.getLogger(NetUtils.class);
	
	public final static String LOCAL_IP = "127.0.0.1";
	
	/**
	 * 获取本机网卡IP地址
	 * @return
	 */
	public static InetAddress getLocalhost() {
		return NetUtil.getLocalhost();
	}
	
	/**
	 * 获得指定地址信息中的MAC地址，使用分隔符“-”
	 * @param inetAddress
	 * @return
	 */
	public static String getMacAddress(InetAddress inetAddress) {
		return NetUtil.getMacAddress(inetAddress);
	}
	
	/**
	 * 主机是否可以访问
	 * @param host 主机
	 * @param timeout 超时时间
	 * @return
	 */
	public static boolean isHostReachable(String host, int timeout) {
		try {
            return InetAddress.getByName(host).isReachable(timeout);
        } catch (UnknownHostException e) {
        	log.error("", e);
        } catch (IOException e) {
        	log.error("", e);
        }
        return false;
	}
	
	/**
	 * 主机端口是否可以连接
	 * @param host 主机
	 * @param port 端口号
	 * @param timeout 超时时间
	 * @return
	 */
	public static boolean isHostConnectable(String host, int port, Integer timeout) {
		Socket socket = null;
		try {
			socket = new Socket();
			if(timeout != null && timeout.intValue() > 0) {
				socket.setSoTimeout(timeout.intValue());
			}
			socket.connect(new InetSocketAddress(host, port));
			return true;
		} catch (Exception e) {
			log.error("", e);
			return false;
		} finally {
			if(socket != null) {
				try {
					socket.close();
				} catch (Exception ex) {
					log.error("", ex);
				}
			}
		}
	}
	
	/**
	 * 是否为有效的端口
	 * @param port 端口号
	 * @return
	 */
	public static boolean isValidPort(int port) {
		// 有效端口是0～65535
		return port >= 0 && port <= 0xFFFF;
	}
	
	/**
	 * 检测本地端口可用性
	 * @param port
	 * @return
	 */
	public static boolean isUsableLocalPort(int port) {
		if (false == isValidPort(port)) {
			// 给定的IP未在指定端口范围中
			return false;
		}
		try {
			new Socket(LOCAL_IP, port).close();
			// socket链接正常，说明这个端口正在使用
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	
	/**
	 * tcp 发送数据
	 * @param host 主机
	 * @param port 端口
	 * @param connectTimeout 连接超时时间
	 * @param readTimeout 读取超时时间
	 * @param sendBytes 要发送的字节数据
	 * @param readCallback 数据读取回调
	 * @return
	 */
	public static Result<String> tcpSocketSend(String host, int port, Integer connectTimeout, Integer readTimeout, final byte[] sendBytes, ActionResultCallback<InputStream, String> readCallback) {
		return tcpSocketSend(host, port, connectTimeout, readTimeout, new ActionResultCallback<OutputStream, String>() {
			
			@Override
			public Result<String> action(OutputStream obj) {
				if(sendBytes != null && sendBytes.length > 0) {
					try {
						obj.write(sendBytes);
						obj.flush();
					} catch (Exception e) {
						log.error("", e);
						return new Result<>(false, e.getMessage());
					}
				}
				return new Result<>(true, "");
			}
		}, readCallback);
	}
	
	/**
	 * tcp 发送数据
	 * @param host 主机
	 * @param port 端口
	 * @param connectTimeout 连接超时时间
	 * @param readTimeout 数据接收超时时间
	 * @param sendCallback 数据发送回调
	 * @param readCallback 数据读取回调
	 * @return
	 */
	public static Result<String> tcpSocketSend(String host, int port, Integer connectTimeout, Integer readTimeout,  ActionResultCallback<OutputStream, String> sendCallback, ActionResultCallback<InputStream, String> readCallback) {
		Socket socket = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			SocketAddress socketAddress = new InetSocketAddress(host, port);
			socket = new Socket();
			//connectTimeout
			if(connectTimeout != null && connectTimeout.intValue() > 0) {
				socket.connect(socketAddress, connectTimeout.intValue());
			} else {
				socket.connect(socketAddress, 5000);
			}
			//readTimeout
			if(readTimeout != null && readTimeout.intValue() > 0) {
				socket.setSoTimeout(readTimeout);
			}
			//outputStream
			if(sendCallback != null) {
				outputStream = socket.getOutputStream();
				Result<String> ret = sendCallback.action(outputStream);
				if(!ret.isSuccess()) {
					throw new Exception(ret.getMsg());
				}
			}
			
			if(readCallback != null) {
				//inputStream
				inputStream = socket.getInputStream();
				Result<String> ret = readCallback.action(inputStream);
				if(!ret.isSuccess()) {
					throw new Exception(ret.getMsg());
				}
			}
			
			//return
			return new Result<>(true, "");
		} catch (SocketTimeoutException e) {
			log.error("", e);
			return new Result<>(false, "连接服务器超时");
		} catch (SocketException e) {
			log.error("", e);
			return new Result<>(false, e.getMessage());
		} catch (Exception e) {
			return new Result<>(false, "请求失败:" + e.getMessage());
		} finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if(socket != null) {
				try {
					socket.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * udp 发送数据
	 * @param host 主机名
	 * @param port 端口号 
	 * @param bytes 字节数据
	 * @return
	 */
	public static Result<String> udpSocketSend(String host, int port, byte[] bytes) {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			SocketAddress socketAddress = new InetSocketAddress(host, port);
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length, socketAddress);
			socket.send(packet);
			socket.close();
			socket = null;
			return new Result<>(true, "");
		} catch (Exception e) {
			log.error("", e);
			return new Result<>(false, e.getMessage());
		} finally {
			if(socket != null) {
				socket.close();
				socket = null;
			}
		}
	}
	
	
	public static void main(String[] args) {
		
	}
}
