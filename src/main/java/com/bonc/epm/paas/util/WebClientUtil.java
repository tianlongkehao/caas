package com.bonc.epm.paas.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bonc.epm.paas.dao.VisitThirdInterfaceLogDao;
import com.bonc.epm.paas.entity.User;
import com.bonc.epm.paas.entity.VisitThirdInterfaceLog;

/**
 * http请求公共类
 *
 * @author ke_wang
 * @since WebClientUtil1.0 Date:2016-05-19
 */
@Component
public class WebClientUtil {
	/**
	 * 说明：日志实例 功能：打印日志
	 */
	private static Logger LOGGER = Logger.getLogger(WebClientUtil.class);

	private static VisitThirdInterfaceLogDao visitThirdInterfaceLogDao;

	@Autowired
	public WebClientUtil(VisitThirdInterfaceLogDao visitThirdInterfaceLogDao){
		WebClientUtil.visitThirdInterfaceLogDao = visitThirdInterfaceLogDao;
	}
	/**
	 * 通过post获取网络资源
	 *
	 * @param url String
	 * @param params Map<String,Object>
	 * @return resultBuffer
	 * @see
	 * @exception Exception
	 */
	public static String doPost(String url, Map<String, Object> params) {
		LOGGER.info("调用：-" + url + "接口，参数列表：-" + params);
		String parameterData = null;
		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;
		VisitThirdInterfaceLog log = new VisitThirdInterfaceLog();
		try {
			if (params != null) {
				parameterData = "";
				for (String key : params.keySet()) {
					parameterData += (parameterData.equals("") ? "" : "&") + key + "="
							+ URLEncoder.encode(String.valueOf(params.get(key)), "UTF8");
				}
			}
			// 将请求信息存在数据库中
			User currentUser = CurrentUserUtils.getInstance().getUser();
			log.setCreateBy(currentUser.getId());
			log.setCreateDate(new Date());
			log.setParam(parameterData);
			log.setUrl(url);
			log = visitThirdInterfaceLogDao.save(log);
			Date startTime = new Date();
			// 获得一个http连接
			HttpURLConnection httpURLConnection = getHttpURLConn(url, parameterData, "POST");

			if (parameterData != null) {
				outputStream = httpURLConnection.getOutputStream();
				outputStreamWriter = new OutputStreamWriter(outputStream);
				outputStreamWriter.write(parameterData.toString());
				outputStreamWriter.flush();
				/*
				 * DataOutputStream out = new
				 * DataOutputStream(httpURLConnection.getOutputStream());
				 * out.writeBytes(parameterData); out.flush();
				 */
			}

			// 保存返回的信息到数据库中
			Date endTime = new Date();
			log.setReturnCode(httpURLConnection.getResponseCode());
			log.setResponseTime(endTime.getTime()-startTime.getTime());

			if (httpURLConnection.getResponseCode() == 200) {
				inputStream = httpURLConnection.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream);
				reader = new BufferedReader(inputStreamReader);
				while ((tempLine = reader.readLine()) != null) {
					resultBuffer.append(tempLine);
				}
				log.setReturnContent(resultBuffer.toString());
			} else {
				throw new Exception(
						"HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
			}
		} catch (Exception e) {
			log.setErrorMsg(e.getMessage());
			e.printStackTrace();
		} finally {
			visitThirdInterfaceLogDao.save(log);
			// 关闭输入输出流
			closeStream(outputStream, outputStreamWriter, inputStream, inputStreamReader, reader);
		}
		return resultBuffer.toString();
	}

	/**
	 * 通过get获取网络资源
	 *
	 * @param url String
	 * @param params Map<String,Object>
	 * @return resultBuffer
	 * @exception Exception
	 */
	public static String doGet(String url, Map<String, Object> params) {
		LOGGER.info("调用：-" + url + "接口，参数列表：-" + params);
		String parameterData = null;
		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;
		VisitThirdInterfaceLog logs = new VisitThirdInterfaceLog();
		try {
			if (params != null) {
				parameterData = "";
				for (String key : params.keySet()) {
					parameterData += (parameterData.equals("") ? "" : "&") + key + "="
							+ URLEncoder.encode(String.valueOf(params.get(key)).replace(" ", ""), "UTF8");
				}
			}
			// 将请求信息存在数据库中
			User currentUser = CurrentUserUtils.getInstance().getUser();
			logs.setCreateBy(currentUser.getId());
			logs.setCreateDate(new Date());
			logs.setParam(parameterData);
			logs.setUrl(url);
			logs = visitThirdInterfaceLogDao.save(logs);
			Date startTime = new Date();

			// 获得一个http连接
			HttpURLConnection httpURLConnection = getHttpURLConn(url, parameterData, "GET");

			if (parameterData != null) {
				outputStream = httpURLConnection.getOutputStream();
				outputStreamWriter = new OutputStreamWriter(outputStream);
				outputStreamWriter.write(parameterData.toString());
				outputStreamWriter.flush();
			}

			// 保存返回的信息到数据库中
			Date endTime = new Date();
			logs.setReturnCode(httpURLConnection.getResponseCode());
			logs.setResponseTime(endTime.getTime()-startTime.getTime());

			if (httpURLConnection.getResponseCode() == 200) {
				inputStream = httpURLConnection.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream);
				reader = new BufferedReader(inputStreamReader);
				while ((tempLine = reader.readLine()) != null) {
					resultBuffer.append(tempLine);
				}
				logs.setReturnContent(resultBuffer.toString());
			} else {
				throw new Exception(
						"HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
			}
		} catch (Exception e) {
			logs.setErrorMsg(e.getMessage());
			e.printStackTrace();
		} finally {
			visitThirdInterfaceLogDao.save(logs);
			// 关闭输入输出流
			closeStream(outputStream, outputStreamWriter, inputStream, inputStreamReader, reader);
		}
		return resultBuffer.toString();
	}

	/**
	 * 获取一个http连接
	 *
	 * @param url String
	 * @param parameterData String
	 * @param mode String
	 * @return httpURLConnection
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 * @see
	 */
	private static HttpURLConnection getHttpURLConn(String url, String parameterData, String mode)
			throws MalformedURLException, IOException, ProtocolException {
		URL localURL = new URL(url);
		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setConnectTimeout(60000); // 设置连接超时为10s
		httpURLConnection.setReadTimeout(60000); // 读取数据超时也是10s
		httpURLConnection.setRequestMethod(mode);
		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpURLConnection.setRequestProperty("Content-Length",
				String.valueOf(parameterData == null ? 0 : parameterData.length()));
		return httpURLConnection;
	}

	/**
	 * 关闭输入输出流
	 *
	 * @param outputStream OutputStream
	 * @param outputStreamWriter OutputStreamWriter
	 * @param inputStream InputStream
	 * @param inputStreamReader InputStreamReader
	 * @param reader BufferedReader
	 * @see IOException
	 * @exception IOException
	 */
	private static void closeStream(OutputStream outputStream, OutputStreamWriter outputStreamWriter,
			InputStream inputStream, InputStreamReader inputStreamReader, BufferedReader reader) {
		if (outputStreamWriter != null) {
			try {
				outputStreamWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (inputStreamReader != null) {
			try {
				inputStreamReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
