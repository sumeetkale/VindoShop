package com.shopping.vindoshop.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.SortField;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.JDBCConnectionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

public class CommonUtil {
	public static SimpleDateFormat pattern = new SimpleDateFormat("dd/MM/yyyy");

	public static String exceptionHandler(Exception e) {
		String error = null;
		e.printStackTrace();
		if (e.getCause() != null)
			error = e.getCause().getMessage();
		else
			error = e.getMessage();
		if (e instanceof CommunicationsException
				|| e instanceof JDBCConnectionException
				|| e instanceof UnknownHostException
				|| (error != null && error.contains("Hibernate")))
			error = "Server is down, Please try again later !!";
		if (e instanceof ConstraintViolationException)
			error = error.split(",")[1].split("=")[1].replace("'", "");
		if (e instanceof org.hibernate.exception.ConstraintViolationException)
			error = error.split("key '")[1].split("_")[0]
					+ " already Registered";
		error = StringUtils.join(
				StringUtils.splitByCharacterTypeCamelCase(error), ' ');
		error = Character.toUpperCase(error.charAt(0)) + error.substring(1);
		return error;

	}

	public static Date getIstDateTime() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("IST"));
		Date date = new Date();
		SimpleDateFormat dateParser = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateParser.parse(format.format(date));
	}

	public static String getOTPMsgContent(String user, int otp) {
		if (user == null)
			user = "Customer";
		return "Dear "
				+ user
				+ ", Thank you for registering with VindoShop. Your default password is "
				+ otp + ". Please change it on first Login.";
	}

	public static String getOTPMsgContentForgot(String user, int otp) {
		if (user == null)
			user = "Customer";
		return "Dear " + user + ", Your system generated password is " + otp
				+ ". Please change it on next Login.";
	}

	public static int getRandom() {
		return 1000 + new Random().nextInt(9000);
	}

	public static Session getSession(SessionFactory sessionFactory) {
		Session session;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			session = sessionFactory.openSession();
		}
		session.setFlushMode(FlushMode.MANUAL);
		return session;
	}

	public static <T> T parseJsonToObject(Object jsonStr, Class<T> clazz)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(jsonStr, clazz);
	}

	public static String sendMsg(String msg, String phone) throws IOException {
		String url = "http://cloud.smsindiahub.in/vendorsms/pushsms.aspx?user=vaibhav.kanth&password=MCC@2013&msisdn=91"
				+ phone
				+ "&sid=VINDOS&gwid=2&fl=0&msg="
				+ URLEncoder.encode(msg);

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		String response = "";

		while ((inputLine = in.readLine()) != null) {
			response += inputLine;
		}
		System.out.println("sms response" + response);
		in.close();
		return response;
	}

	public static HashMap<String, Integer> sortList() {
		HashMap<String, Integer> sortlist = new HashMap<String, Integer>();
		sortlist.put("rating", SortField.DOUBLE);
		sortlist.put("discount", SortField.INT);
		sortlist.put("name", SortField.STRING);
		sortlist.put("popularity", SortField.STRING);
		sortlist.put("startDate", SortField.LONG);
		return sortlist;
	}

	public static byte[] extractBytes(String ImageName) throws IOException,
			URISyntaxException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		byte[] byteChunk = null;
		try {
			is = new URL(ImageName).openStream();
			byteChunk = new byte[4096]; // Or whatever size you want to read in
										// at a time.
			int n;

			while ((n = is.read(byteChunk)) > 0) {
				baos.write(byteChunk, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
			// Perform any other exception handling that's appropriate.
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return baos.toByteArray();
	}

}