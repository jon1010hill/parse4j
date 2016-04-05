package org.parse4j;

import java.util.Map;

import org.json.JSONObject;
import org.parse4j.callback.FunctionCallback;
import org.parse4j.command.ParsePostCommand;
import org.parse4j.command.ParseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseCloud {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ParseCloud.class);
	
	@SuppressWarnings("unchecked")
	public static <T> T callFunction(String name, Map<String, ?> params,Parse parseContext)
			throws ParseException {
		
		T result = null;
		ParsePostCommand command = new ParsePostCommand("functions", name,parseContext);
		command.setData(new JSONObject(params));
		ParseResponse response = command.perform();
		
		if(!response.isFailed()) {
			JSONObject jsonResponse = response.getJsonObject();
			result = (T) jsonResponse.get("result");
			return result;
		}
		else {
			LOGGER.debug("Request failed.");
			throw response.getException();
		}
		
	}

	public static <T> void callFunctionInBackground(String name,
			Map<String, ?> params, FunctionCallback<T> callback, Parse parseContext) {

		CallFunctionInBackgroundThread<T> task = new CallFunctionInBackgroundThread<T>(name, params, callback,parseContext);
		ParseExecutor.runInBackground(task);
	}
	
	private static class CallFunctionInBackgroundThread<T> extends Thread {
		Map<String, ?> params;
		FunctionCallback<T> functionCallback;
		String name;
		private final Parse parseContext;
		
		public CallFunctionInBackgroundThread(String name, Map<String, ?> params, FunctionCallback<T> functionCallback, final Parse parseContext) {
			this.functionCallback = functionCallback;
			this.params = params;
			this.name = name;
			this.parseContext = parseContext;
		}

		public void run() {
			ParseException exception = null;
			T result = null;
			try {
				result = callFunction(name, params,parseContext);
			} catch (ParseException e) {
				LOGGER.debug("Request failed {}", e.getMessage());
				exception = e;
			}
			if (functionCallback != null) {
				functionCallback.done(result, exception);
			}
		}
	}

}
