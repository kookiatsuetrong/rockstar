package web.framework;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 * There are two kind of context
 * 1. real HTTP context
 * 2. test context
 * 
 * For real context, HTTP GET request
 *                   access data from query("variable")
 * For real context, HTTP POST request
 *                   access data from read() as a JSON
 * 
 * For real context, HTTP session variable
 *                   context.getSession(string)
 * 
 * For test context, HTTP GET request
 *                   can be simulate by addQuery(key, value)
 * For test context, HTTP POST request
 *                   can be simulate by setBodyBuffer(json-string)
 * For test context, HTTP Session variable
 *                   context.setSession(key, value)
 * 
 * 
 */

public class Context {
	
	public HttpServletRequest request;
	public HttpServletResponse response;
	
	public boolean testing;
	
	public Context() {
		this.testing    = true;
		this.queryMap   = new HashMap<>();
		this.sessionMap = new HashMap<>();
	}
	
	public Context(HttpServletRequest request,
					HttpServletResponse response) {
		this.request  = request;
		this.response = response;
		this.method   = this.request.getMethod();
	}
	
	// For Unit testing to mockup HTTP request
	protected String method = "GET";
	
	public void setMethod(String s) {
		this.method = s;
	}
	
	public String getMethod() {
		return this.method;
	}
	
	// query(x) --> HTTP GET  / request.getParameter(x)
	// read()   --> HTTP POST
	
	protected HashMap<String, String> queryMap;
	HashMap<String, Object> sessionMap;
	
	// for testing
	public void addQuery(String key, String value) {
		if (this.testing == true) {
			this.queryMap.put(key, value);
		}
	}
	
	public String query(String key) {
		if (this.testing == true) {
			return this.queryMap.get(key);
		}
		
		return request.getParameter(key);
	}
	
	protected String bodyBuffer = "{}";
	
	public void setBodyBuffer(String buffer) {
		this.bodyBuffer = buffer;
	}
	
	public Map<String, Object> read() {
		if (testing == false) {
			bodyBuffer = "";
			try {
				var input  = request.getInputStream();
				var reader = new InputStreamReader(input, 
									StandardCharsets.UTF_8);
				while (true) {
					int k = reader.read();
					if (k == -1) break;
					bodyBuffer += (char)k;
				}
			} catch (Exception e) { }
		}
		
		System.out.println("bodyBuffer " + bodyBuffer);
		JSONObject body = new JSONObject(bodyBuffer);
		Map<String, Object> result = Rockstar.toMap(body);
		return result;
	}
	
	public HttpSession getSession(boolean created) {
		return request.getSession(created);
	}
	
	public Object getSession(String name) {
		if (this.testing) {
			return sessionMap.get(name);
		}
		HttpSession session = request.getSession(true);
		return session.getAttribute(name);
	}
	
	public void setSession(String name, Object value) {
		if (this.testing) {
			this.sessionMap.put(name, value);
			return;
		}
		HttpSession session = request.getSession(true);
		session.setAttribute(name, value);
	}
	
	public void removeSession(String name) {
		if (this.testing) {
			this.sessionMap.remove(name);
			return;
		}
		HttpSession session = request.getSession(true);
		session.removeAttribute(name);
	}
		
	// Deprecated?
	
	public String getParameter(String key) {
		return request.getParameter(key);
	}
	
	/*
	public boolean isLoggedIn() {
		HttpSession session = this.request.getSession();
		String email = (String)session.getAttribute("email");
		return email != null;
	}
	*/
	
	/*
	public JSONObject getDetail() {
		return getJSON();
	}
	
	public JSONObject getJson() {
		return getJSON();
	}
	
	public JSONObject getJSON() {
		if (bodyBuffer == null) {
			this.read();
		}
		JSONObject body = new JSONObject(bodyBuffer);
		return body;
	}
	*/
}
