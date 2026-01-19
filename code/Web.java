import org.json.JSONObject;

class Web {

	void main() {
		Rockstar.handle( "GET /",              Web::showReport);
		Rockstar.handle( "GET /report",        Web::showReport);
		Rockstar.handle( "GET /check",         context -> "Rockstar 0.2");
		Rockstar.handle( "GET /service-check", Web::serveVersion);
		Rockstar.handle("POST /get-total",     Web::getTotal);
	}
	
	static Object showReport(Context context) {
		return Rockstar.render(context, "/WEB-INF/sample-view.jsp");
	}
	
	static Object serveVersion(Context context) {
		JSONObject detail = new JSONObject();
		detail.put("version", "0.2");
		detail.put("framework", "Rockstar");
		return detail;
	}
	
	static Object getTotal(Context context) {
		JSONObject data = context.getJson();
		System.out.println(data);
		data.put("output", "OK");
		return data;
	}
	
}
