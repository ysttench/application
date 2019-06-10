package com.ysttench.application.auth.core.rdto;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

public class InvokeHelper {

	// K3 Cloud WebSite URL Example "http://192.168.19.113/K3Cloud/"
	public static String POST_K3CloudURL;

	// Cookie ֵ
	private static String CookieVal = null;

	private static Map map = new HashMap();
	static {
		map.put("Save",
				"Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Save.common.kdsvc");
		map.put("Draft",
				"Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Draft.common.kdsvc");
		map.put("View",
				"Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.View.common.kdsvc");
		map.put("Submit",
				"Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Submit.common.kdsvc");
		map.put("Audit",
				"Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Audit.common.kdsvc");
		map.put("BatchSave",
				"Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.BatchSave.common.kdsvc");
		map.put("UnAudit",
				"Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.UnAudit.common.kdsvc");
		map.put("StatusConvert",
				"Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.StatusConvert.common.kdsvc");
		map.put("Execute",
				"Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc");
	}

	// HttpURLConnection
	private static HttpURLConnection initUrlConn(String url, JSONArray paras)
			throws Exception {
		URL postUrl = new URL(POST_K3CloudURL.concat(url));
		HttpURLConnection connection = (HttpURLConnection) postUrl
				.openConnection();
		if (CookieVal != null) {
			connection.setRequestProperty("Cookie", CookieVal);
		}
		if (!connection.getDoOutput()) {
			connection.setDoOutput(true);
		}
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "application/json");
		DataOutputStream out = new DataOutputStream(
				connection.getOutputStream());

		UUID uuid = UUID.randomUUID();
		int hashCode = uuid.toString().hashCode();

		JSONObject jObj = new JSONObject();

		jObj.put("format", 1);
		jObj.put("useragent", "ApiClient");
		jObj.put("rid", hashCode);
		jObj.put("parameters", chinaToUnicode(paras.toString()));
		jObj.put("timestamp", new Date().toString());
		jObj.put("v", "1.0");

		out.writeBytes(jObj.toString());
		out.flush();
		out.close();

		return connection;
	}

	// Login
	public static Map<String,String> Login(String dbId, String user, String pwd, int lang)
			throws Exception {

		Map<String,String> resultMap = new HashMap<String,String>();
Boolean bResult = false;
		String sUrl = "Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc";

		JSONArray jParas = new JSONArray();
		jParas.put(dbId);// ����Id
		jParas.put(user);// �û���
		jParas.put(pwd);// ����
		jParas.put(lang);// ����

		HttpURLConnection connection = initUrlConn(sUrl, jParas);
		// ��ȡCookie
		String key = null;
		for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
			if (key.equalsIgnoreCase("Set-Cookie")) {
				String tempCookieVal = connection.getHeaderField(i);
				if (tempCookieVal.startsWith("kdservice-sessionid")) {
					CookieVal = tempCookieVal;
					break;
				}
			}
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String line;
		//System.out.println(" ============================= ");
		//System.out.println(" Contents of post request ");
		//System.out.println(" ============================= ");
		while ((line = reader.readLine()) != null) {
			String sResult = new String(line.getBytes(), "utf-8");
			JSONObject jobj = new JSONObject(sResult);
			bResult = line.contains("\"LoginResultType\":1");
			resultMap.put("Msg", jobj.get("Message").toString());
			if (bResult) {
				resultMap.put("errcode", "0");
				JSONObject jobj1 = new JSONObject(jobj.get("Context").toString());
				JSONObject jobj2 = new JSONObject(jobj1.get("CurrentOrganizationInfo").toString());
				resultMap.put("organ", jobj2.get("Name").toString());
				resultMap.put("organid", jobj2.get("ID").toString());
			}else {
				resultMap.put("errcode", "1");
			}
			System.out.println(jobj);
			
		}
		//System.out.println(" ============================= ");
		//System.out.println(" Contents of post request ends ");
		//System.out.println(" ============================= ");
		reader.close();

		connection.disconnect();

		return resultMap;
	}

	// Save
	public static String Save(String formId, String content) throws Exception {
		return Invoke("Save", formId, content);
	}
	// Draft
		public static String Draft(String formId, String content) throws Exception {
			return Invoke("Draft", formId, content);
		}

	// View
	public static String View(String formId, String content) throws Exception {
		return Invoke("View", formId, content);
	}

	// Submit
	public static String Submit(String formId, String content) throws Exception {
		return Invoke("Submit", formId, content);
	}
	// batchSave
		public static String batchSave(String formId, String content) throws Exception {
			return Invoke("BatchSave", formId, content);
		}

	// Audit
	public static String Audit(String formId, String content) throws Exception {
		return Invoke("Audit", formId, content);
	}

	// UnAudit
	public static String UnAudit(String formId, String content) throws Exception {
		return Invoke("UnAudit", formId, content);
	}
	// Execute
	public static String Execute(String formId, String content) throws Exception {
		return Invoke("Execute", formId, content);
	}
	// StatusConvert
	public static String StatusConvert(String formId, String content)
			throws Exception {
		return Invoke("StatusConvert", formId, content);
	}

	private static String Invoke(String deal, String formId, String content)
			throws Exception {
		String sResult="";
		String sUrl = map.get(deal).toString();
		JSONArray jParas = new JSONArray();
		if (!"Execute".equals(deal)) {
			jParas.put(formId);
		}
		jParas.put(content);

		HttpURLConnection connectionInvoke = initUrlConn(sUrl, jParas);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connectionInvoke.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			 sResult = new String(line.getBytes(), "utf-8");
			System.out.println(sResult);
		}
		reader.close();
		connectionInvoke.disconnect();
		return sResult;
	}


	/**
	 * ������ת��Unicode��
	 * 
	 * @param str
	 * @return
	 */
	public static String chinaToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int chr1 = (char) str.charAt(i);
			if (chr1 >= 19968 && chr1 <= 171941) {// ���ַ�Χ \u4e00-\u9fa5 (����)
				result += "\\u" + Integer.toHexString(chr1);
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}
}
