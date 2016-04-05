package org.parse4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parse4j.operation.ParseFieldOperations;

public class ParseDecoder {

	@SuppressWarnings("rawtypes")
	public static Object decode(Object object,Parse parseContext) {

		if ((object instanceof JSONArray)) {
			return convertJSONArrayToList((JSONArray) object,parseContext);
		}

		if (!(object instanceof JSONObject)) {
			return object;
		}
		
		JSONObject jsonObject = (JSONObject) object;
		
		String typeString = jsonObject.optString("__type", null);
		if (typeString == null) {
			return convertJSONObjectToMap(jsonObject,parseContext);
		}

		if (typeString.equals("Date")) {
			String iso = jsonObject.optString("iso");
			return Parse.parseDate(iso);
		}

		if (typeString.equals("Bytes")) {
			String base64 = jsonObject.optString("base64");
			return Base64.decodeBase64(base64);
		}

		if (typeString.equals("Pointer")) {
			return decodePointer(jsonObject.optString("className"),
					jsonObject.optString("objectId"),parseContext);
		}

		if (typeString.equals("File")) {
			return new ParseFile(jsonObject.optString("name"),
					jsonObject.optString("url"),parseContext);
		}

		if (typeString.equals("GeoPoint")) {
			double latitude, longitude;
			try {
				latitude = jsonObject.getDouble("latitude");
				longitude = jsonObject.getDouble("longitude");
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			return new ParseGeoPoint(latitude, longitude);
		}	

		if (typeString.equals("Relation")) {
			return new ParseRelation(jsonObject,parseContext);
		}
		
		if (typeString.equals("Object")) {
			return convertJSONObjectToParseObject(jsonObject,parseContext);
		}
		
		String opString = jsonObject.optString("__op", null);
	    if (opString != null) {
	      try {
	        return ParseFieldOperations.decode(jsonObject);
	      } catch (JSONException e) {
	        throw new RuntimeException(e);
	      }
	    }		

		return null;
		
	}
	
	private static ParseObject convertJSONObjectToParseObject(JSONObject jsonObject, final Parse parseContext) {
		if (jsonObject == null) return null; 
		ParseObject parseObject = ParseObject.createWithoutData(jsonObject.optString("className"),
				jsonObject.optString("objectId"),parseContext);
		parseObject.setData(jsonObject, true);
		return parseObject;
	}

	private static ParseObject decodePointer(String className, String objectId, final Parse parseContext) {
	    return ParseObject.createWithoutData(className, objectId,parseContext);
	  }	

	private static List<Object> convertJSONArrayToList(JSONArray array, final Parse parseContext) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			list.add(decode(array.opt(i),parseContext));
		}
		return list;
	}

	private static Map<String, Object> convertJSONObjectToMap(JSONObject object, final Parse parseContext) {
		Map<String, Object> outputMap = new HashMap<String, Object>();
		Iterator<?> it = object.keys();
		while (it.hasNext()) {
			String key = (String) it.next();
			Object value = object.opt(key);
			outputMap.put(key, decode(value,parseContext));
		}
		return outputMap;
	}

}
