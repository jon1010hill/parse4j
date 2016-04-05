package org.parse4j;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parse4j.operation.ParseFieldOperations;
import org.parse4j.util.ParseRegistry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Parse {

	private String apiEndPoint = ParseConstants.API_ENDPOINT;
	private String sessionId;
	private String mApplicationId;
	private String mRestAPIKey;
	private String mMasterKey;
	private static final DateFormat dateFormat;
	private boolean isRootMode;

	static {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		format.setTimeZone(new SimpleTimeZone(0, "GMT"));
		dateFormat = format;
		
	}

	public void initialize(String applicationId, String restAPIKey,String apiEndPoint) {
	
		this.initialize(applicationId, restAPIKey);
		this.apiEndPoint = apiEndPoint;
	}
	
	public void initialize(String applicationId, String restAPIKey) {
		this.mApplicationId = applicationId;
		this.mRestAPIKey = restAPIKey;
		this.isRootMode = false;
		ParseRegistry.registerDefaultSubClasses();
		ParseFieldOperations.registerDefaultDecoders(this);
	}

	/**
	 * Don't use it in client app! Use it only if know what you are doing.
	 * If someone get your master key he can bypass all of your app's security!
	 *
	 * @param applicationId your app id
	 * @param masterKey your master key
	 */
	public void initializeAsRoot (String applicationId, String masterKey) {
		mApplicationId = applicationId;
		mMasterKey = masterKey;
		isRootMode = true;
		ParseRegistry.registerDefaultSubClasses();
		ParseFieldOperations.registerDefaultDecoders(this);
	}
	
	public void initializeAsRoot (String applicationId, String masterKey,String apiEndPoint) {
		this.apiEndPoint = apiEndPoint;
		this.initializeAsRoot(applicationId, masterKey);
	}
	public synchronized void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getApplicationId() {
		return mApplicationId;
	}

	public String getRestAPIKey() {
		return mRestAPIKey;
	}

	public boolean isIsRootMode() {
		return isRootMode;
	}

	public String getParseAPIUrl(String context) {
		return this.apiEndPoint + "/" + context;
	}

	public static synchronized String encodeDate(Date date) {
		return dateFormat.format(date);
	}

	public static synchronized Date parseDate(String dateString) {
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	public String getMasterKey() {
		return mMasterKey;
	}

	public static boolean isInvalidKey(String key) {
		return "objectId".equals(key) || "createdAt".equals(key)
				|| "updatedAt".equals(key);
	}

	public static boolean isValidType(Object value) {
		return ((value instanceof JSONObject))
				|| ((value instanceof JSONArray))
				|| ((value instanceof String))
				|| ((value instanceof Number))
				|| ((value instanceof Boolean))
				|| (value == JSONObject.NULL)
				|| ((value instanceof ParseObject))
				// || ((value instanceof ParseACL))
				|| ((value instanceof ParseFile))
				|| ((value instanceof ParseRelation))
				|| ((value instanceof ParseGeoPoint))
				|| ((value instanceof Date))
				|| ((value instanceof byte[]))
				|| ((value instanceof List))
				|| ((value instanceof Map));
	}

	@SuppressWarnings("rawtypes")
	public static String join(Collection<String> items, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		Iterator iter = items.iterator();
		if (iter.hasNext()) {
			buffer.append((String) iter.next());
			while (iter.hasNext()) {
				buffer.append(delimiter);
				buffer.append((String) iter.next());
			}
		}
		return buffer.toString();
	}

	public String getSessionId() {
		
		return this.sessionId;
	}


}