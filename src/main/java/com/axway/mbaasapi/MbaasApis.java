package com.axway.mbaasapi;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.FormEncodingBuilder;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class MbaasApis {
	private String mConnectSid;
	private String mSessionId;
	private String mUser;
	private String mPassword;
	private String mOrgID;
	@SuppressWarnings("unused")
	private String mGuid;

	private String mAppKey;
	private String mMBaasUserEmail;
	private String mMBaasUserPassword = "password";

	// PRE-PROD
	static final String mDashBoardHost = "https://platform.axwaytest.net/api/v1/auth/login";
	static final String mbaasHostAPI = "https://preprod-api.cloud.appctest.com/v1";

	/**
	 * Constructor for MbaasApis will create an internal app key.
	 *
	 * @param user     the username for dashboard.
	 * @param password the password for dashboard when using it from CLI.
	 * @param orgID    the organisation ID on dashboard.
	 * @throws IOException If an I/O error occurs.
	 */
	public MbaasApis(String user, String password, String orgID) throws IOException {
		mUser = user;
		mPassword = password;
		mOrgID = orgID;

		HttpClientSingleton con = HttpClientSingleton.getInstance();

		RequestBody formBody = new FormEncodingBuilder().add("username", mUser).add("password", mPassword)
				.add("org_id", mOrgID).build();
		Request request = new Request.Builder().url(mDashBoardHost).post(formBody).build();

		Response response = con.newCall(request).execute();
		String result = response.body().string();
		response.body().close();

		if (response.code() == 200) {
			JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
			mConnectSid = jsonObject.get("result").getAsJsonObject().get("connect.sid").getAsString();

			Login360();
			CreateAppKey();
		}
	}

	/**
	 * Constructor for MbaasApis will re-use a pre-created app key.
	 *
	 * @param appKey a valid app key in the organisation.
	 */
	public MbaasApis(String appKey) {
		mAppKey = appKey;
	}

	/**
	 * Creates a User with random generated email and password endpoint:
	 * v1/users/create.json.
	 *
	 * @throws IOException If an I/O error occurs.
	 * @return a JsonObject.
	 */
	public JsonObject CreateUser() throws IOException {
		String baseURL = mbaasHostAPI + "/users/create.json?key=" + mAppKey + "&pretty_json=true";

		RandomStringGenerator session = new RandomStringGenerator();
		mMBaasUserEmail = "randuser22" + session.nextString() + "@gmail.com";

		HttpClientSingleton con = HttpClientSingleton.getInstance();

		RequestBody formBody = new FormEncodingBuilder().add("admin", "false").add("email", mMBaasUserEmail)
				.add("password", mMBaasUserPassword).add("password_confirmation", mMBaasUserPassword)
				.add("first_name", "testing2").add("last_name", "testing2").add("org_id", mOrgID).build();

		Request request = new Request.Builder().addHeader("User-Agent", "Mbaas test java agent").url(baseURL)
				.post(formBody).build();

		Response response = con.newCall(request).execute();
		String result = response.body().string();
		response.body().close();

		JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
		return jsonObject;
	}

	/**
	 * Creates a User with a provided email and password endpoint:
	 * v1/users/create.json.
	 * 
	 * @param mbaasUserEmail    the mbaas username.
	 * @param mbaasUserPassword the mbaas password.
	 * 
	 * @throws IOException If an I/O error occurs.
	 * @return a JsonObject.
	 */
	public JsonObject CreateUser(String mbaasUserEmail, String mbaasUserPassword) throws IOException {
		String baseURL = mbaasHostAPI + "/users/create.json?key=" + mAppKey + "&pretty_json=true";


		HttpClientSingleton con = HttpClientSingleton.getInstance();

		mMBaasUserEmail = mbaasUserEmail;
		mMBaasUserPassword = mbaasUserPassword;
		
        RequestBody formBody = new FormEncodingBuilder().add("admin", "false").add("email", mMBaasUserEmail)
				.add("password", mMBaasUserPassword).add("password_confirmation", mMBaasUserPassword)
				.add("first_name", "testing2").add("last_name", "testing2").add("org_id", mOrgID).build();

		Request request = new Request.Builder().addHeader("User-Agent", "Mbaas test java agent").url(baseURL)
				.post(formBody).build();

		Response response = con.newCall(request).execute();
		String result = response.body().string();
		response.body().close();

		JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
		return jsonObject;
	}
	
	/**
	 * Logins a User with saved email and password endpoint: v1/users/login.json.
	 * 
	 * @throws IOException If an I/O error occurs.
	 * @return a JsonObject.
	 */
	public JsonObject LoginUser() throws IOException {
		String baseURL = mbaasHostAPI + "/users/login.json?pretty_json=true" + "&key=" + mAppKey;

		HttpClientSingleton con = HttpClientSingleton.getInstance();

		RequestBody formBody = new FormEncodingBuilder().add("login", mMBaasUserEmail)
				.add("password", mMBaasUserPassword).build();

		Request request = new Request.Builder().addHeader("User-Agent", "Mbaas test java agent").url(baseURL)
				.post(formBody).build();

		Response response = con.newCall(request).execute();
		String result = response.body().string();
		response.body().close();

		JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
		if (response.code() == 200) {
			mSessionId = jsonObject.get("meta").getAsJsonObject().get("session_id").getAsString();
		}
		return jsonObject;
	}

	/**
	 * Creates a photo endpoint: v1/photos/create.json.
	 * 
	 * @param filepath the path to the file.
	 * @throws IOException If an I/O error occurs.
	 * @return a string corresponding to the photo id.
	 */
	public String CreatePhoto(String filepath) throws IOException {
		String baseURL = mbaasHostAPI + "/photos/create.json?pretty_json=true&ct=enterprise" + "&key=" + mAppKey;
		String id = "";

		Logger logger = Logger.getLogger("logger");
		HttpClientSingleton con = HttpClientSingleton.getInstance();

		String[] q = filepath.split("/");
		int idx = q.length - 1;

		RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addFormDataPart("photo", q[idx],
				RequestBody.create(MediaType.parse("application/octet-stream"), new File(filepath))).build();

		Request request = new Request.Builder().addHeader("User-Agent", "Mbaas test java agent")
				.addHeader("Cookie", "_session_id=" + mSessionId).url(baseURL).post(requestBody).build();
		Response response = con.newCall(request).execute();
		String result = response.body().string();
		response.body().close();

		if (response.code() == 200) {
			JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
			id = jsonObject.get("response").getAsJsonObject().get("photos").getAsJsonArray().get(0).getAsJsonObject()
					.get("id").getAsString();
		}

		logger.log(Level.INFO, "ID: " + id);
		return id;
	}

	/**
	 * Displays a photo information endpoint: v1/photos/show.json.
	 *
	 * @param id the id of the photo object.
	 * @throws IOException If an I/O error occurs.
	 * @return a JsonObject.
	 */
	public JsonObject ShowPhoto(String id) throws IOException {
		String baseURL = mbaasHostAPI + "/photos/show.json?pretty_json=true&ct=enterprise" + "&key=" + mAppKey;

		HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL).newBuilder();
		urlBuilder.addQueryParameter("photo_id", id);

		String url = urlBuilder.build().toString();
		HttpClientSingleton con = HttpClientSingleton.getInstance();
		Request request = new Request.Builder().addHeader("User-Agent", "Mbaas test java agent")
				.addHeader("Cookie", "_session_id=" + mSessionId).url(url).build();
		Response response = con.newCall(request).execute();
		String result = response.body().string();
		response.body().close();

		JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
		return jsonObject;
	}

	/**
	 * Delete a photo information endpoint: v1/photos/delete.json.
	 *
	 * @param id the id of the photo object.
	 * @throws IOException If an I/O error occurs.
	 * @return a JsonObject.
	 */
	public JsonObject DeletePhoto(String id) throws IOException {
		String baseURL = mbaasHostAPI + "/photos/delete.json?pretty_json=true&ct=enterprise" + "&key=" + mAppKey;

		HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL).newBuilder();
		urlBuilder.addQueryParameter("photo_id", id);

		String url = urlBuilder.build().toString();
		HttpClientSingleton con = HttpClientSingleton.getInstance();
		Request request = new Request.Builder().addHeader("User-Agent", "Mbaas test java agent")
				.addHeader("Cookie", "_session_id=" + mSessionId).url(url).delete().build();
		Response response = con.newCall(request).execute();
		String result = response.body().string();
		response.body().close();

		JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
		return jsonObject;
	}
	
	/**
	 * Private method to create an App Key end-point: v1/apps/create.json.
	 * 
	 * @throws IOException
	 *
	 */
	private void CreateAppKey() throws IOException {
		String baseURL = mbaasHostAPI + "/apps/create.json?pretty_json=true&ct=enterprise";

		RandomStringGenerator session = new RandomStringGenerator();
		HttpClientSingleton con = HttpClientSingleton.getInstance();
		RequestBody formBody = new FormEncodingBuilder().add("name", "javaapptest" + session.nextString())
				.add("description", "javaapptest" + session.nextString()).build();
		Request request = new Request.Builder().addHeader("Cookie", "_session_id=" + mSessionId)
				.addHeader("User-Agent", "Mbaas test java agent").url(baseURL).post(formBody).build();

		Response response = con.newCall(request).execute();
		String result = response.body().string();
		response.body().close();

		if (response.code() == 200) {
			JsonObject jsonObject = new JsonParser().parse(result.toString()).getAsJsonObject();
			mAppKey = jsonObject.get("response").getAsJsonObject().get("apps").getAsJsonArray().get(0).getAsJsonObject()
					.get("key").getAsString();
		}
	}

	/**
	 * Private method to login to endpoint: v1/admins/login360.json.
	 * 
	 * @throws IOException
	 *
	 */
	private void Login360() throws IOException {
		String baseURL = mbaasHostAPI + "/admins/login360.json?pretty_json=true&ct=enterprise";

		HttpClientSingleton con = HttpClientSingleton.getInstance();
		HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL).newBuilder();
		urlBuilder.addQueryParameter("connect.sid", mConnectSid);

		String url = urlBuilder.build().toString();

		Request request = new Request.Builder().addHeader("User-Agent", "Mbaas test java agent").url(url).build();

		Response response = con.newCall(request).execute();
		String result = response.body().string();
		response.body().close();

		if (response.code() == 200) {
			JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
			mSessionId = jsonObject.get("meta").getAsJsonObject().get("session_id").getAsString();
			JsonArray jsonResponses = jsonObject.get("response").getAsJsonArray();
			if (jsonResponses.isJsonArray()) {
				JsonObject firstElement = jsonResponses.get(0).getAsJsonObject();
				mGuid = firstElement.get("guid").getAsString();
			}
		}
	}
}
