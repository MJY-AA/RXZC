package com.xsj.app.util.redis;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @PackageName:com.rxzc.rxzcapp.jedis
 * @Description:
 * @author:Xsj
 * @date 2020/4/20 0020 17:50
 */
public class ObjectUtil {
	/**
	 * 判断对象是否为空
	 * @param o
	 * @return
	 */
	public static boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		} else if (o instanceof String) {
			String s = (String) o;
			if ("".equals(s.trim())) {
				return true;
			} else {
				return false;
			}
		} else if (o instanceof Collection) {
			return ((Collection) o).isEmpty();
		} else if (o instanceof Map) {
			return ((Map) o).isEmpty();
		} else if (o.getClass().isArray()) {
			return Array.getLength(o) == 0;
		} else {
			return false;
		}
	}

	/**
	 * 将对象序列化
	 * @param o
	 * @return
	 */
	public static byte[] serialize(Object o) throws Exception {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			closeObjectOutputStream(oos);
		}
		return baos.toByteArray();
	}

	/**
	 * 将对象反序列化
	 * @param byteArr
	 * @return
	 */
	public static Object unSerialize(byte[] byteArr) throws Exception {
		ObjectInputStream ois = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArr);
		Object o = null;
		try {
			ois = new ObjectInputStream(bais);
			o = ois.readObject();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			closeObjectIutputStream(ois);
		}
		return o;
	}

	/**
	 * 关闭对象输出流
	 * @param oos
	 */
	private static void closeObjectOutputStream(ObjectOutputStream oos) throws Exception {
		if (oos != null) {
			try {
				oos.close();
			} catch (IOException e) {
				throw new Exception(e.getMessage());
			}
		}
		oos = null;
	}

	/**
	 * 关闭对象输入流
	 * @param ois
	 */
	private static void closeObjectIutputStream(ObjectInputStream ois) throws Exception {
		if (ois != null) {
			try {
				ois.close();
			} catch (IOException e) {
				throw new Exception(e.getMessage());
			}
		}
		ois = null;
	}


}
