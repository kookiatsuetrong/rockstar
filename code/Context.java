import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

public class Context {
	public HttpServletRequest request;
	public HttpServletResponse response;
	
	public String getParameter(String key) {
		return request.getParameter(key);
	}
	
	public JSONObject getJson() {
		String buffer = "";
		try {
			var input = request.getInputStream();
			while (true) {
				int k = input.read();
				if (k == -1) break;
				buffer += (char)k;
			}
		} catch (Exception e) { }

		JSONObject body = new JSONObject(buffer);
		System.out.println(body);
		return body;
	}
	
	public HttpSession getSession(boolean created) {
		return request.getSession(created);
	}

}
