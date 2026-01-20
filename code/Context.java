import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class Context {
	public HttpServletRequest request;
	public HttpServletResponse response;
	
	public boolean testing;
	
	public Context() {
		this.testing = true;
		this(null, null);
	}
	
	public Context(HttpServletRequest request,
					HttpServletResponse response) {
		this.request = request;
		this.response = response;
		
		if (this.testing == true) {
			this.queryMap = new HashMap<String, String>();
		}
		if (this.testing == false) {
			this.method = this.request.getMethod();
		}
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
	
	public void addQuery(String key, String value) {
		if (this.queryMap == null) {
			this.queryMap = new HashMap<String, String>();
		}
		queryMap.put(key, value);
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
	
	public Map read() {
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
		Map result = Rockstar.toMap(body);
		return result;
	}
	
	public HttpSession getSession(boolean created) {
		return request.getSession(created);
	}
	
	// Deprecated?
	
	public String getParameter(String key) {
		return request.getParameter(key);
	}
	
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

}
