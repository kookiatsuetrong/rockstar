import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class Context {
	public HttpServletRequest request;
	public HttpServletResponse response;
	
	public String getParameter(String key) {
		return request.getParameter(key);
	}
	
	/*
	public JSONObject getJson() {
		return getJSON();
	}
	*/
	
	public JSONObject getJSON() {
		String buffer = "";
		try {
			var input  = request.getInputStream();
			var reader = new InputStreamReader(input, 
								StandardCharsets.UTF_8);
			while (true) {
				int k = reader.read();
				if (k == -1) break;
				buffer += (char)k;
			}
		} catch (Exception e) { }

		JSONObject body = new JSONObject(buffer);
		return body;
	}
	
	public HttpSession getSession(boolean created) {
		return request.getSession(created);
	}

}
