import org.json.JSONObject;

class Web {

	void main() {
		Rockstar.handle( "GET /",              Web::showReport);
		Rockstar.handle( "GET /report",        Web::showReport);
		Rockstar.handle( "GET /check",         context -> "Rockstar 0.3");
		Rockstar.handle( "GET /service-check", Web::getVersion);
		Rockstar.handle("POST /get-total",     Web::getTotal);
	}
	
	static Object showReport(Context context) {
		return Rockstar.render(context, "/WEB-INF/sample-view.jsp");
	}
	
	static Object getVersion(Context context) {
		JSONObject detail = new JSONObject();
		detail.put("version", "0.3");
		detail.put("framework", "Rockstar");
		return detail;
	}
	
	static Object getTotal(Context context) {
		JSONObject data = context.getJSON();
		System.out.println(data);
		data.put("output", "OK");
		return data;
	}
	
}
