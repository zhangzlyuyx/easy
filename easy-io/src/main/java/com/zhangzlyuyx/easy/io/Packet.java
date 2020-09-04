package com.zhangzlyuyx.easy.io;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Packet implements Serializable, Cloneable {

	protected static Logger log = LoggerFactory.getLogger(Packet.class);

	private static final long serialVersionUID = -5385440388666503985L;

	private int byteCount = 0;
	
	public int getByteCount() {
		return this.byteCount;
	}
	
	public void setByteCount(int byteCount) {
		this.byteCount = byteCount;
	}
	
	@Override
	protected Object clone() {
		try {
			Packet ret = (Packet) super.clone();
			return ret;
		} catch (CloneNotSupportedException e) {
			log.error("", e);
			return null;
		}
	}
}
