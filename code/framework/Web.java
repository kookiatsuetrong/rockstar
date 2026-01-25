package framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
class Web {

	void main() {
		Rockstar.handle( "GET /",                 Web::showReport);
		Rockstar.handle( "GET /report",           Web::showReport);
		Rockstar.handle( "GET /check",            context -> "Rockstar 0.4");
		Rockstar.handle( "GET /service-check",    Web::getVersion);
		Rockstar.handle("POST /get-total",        Web::getTotal);
		Rockstar.handle( "GET /service-branches", Web::listBranches);
	}
	
	static Object showReport(Context context) {
		return Rockstar.render(context, "/WEB-INF/sample-view.jsp");
	}
	
	static Object getVersion(Context context) {
		Map detail = new HashMap<String, Object>();
		detail.put("version", "0.4");
		detail.put("framework", "Rockstar");
		return detail;
	}
	
	static Object getTotal(Context context) {
		Map m = context.read();
		m.put("result", "OK");
		m.put("output", "3.1415926");
		return m;
	}
	
	static Object listBranches(Context context) {
		List list = new ArrayList<String>();
		list.add("Atlanta");
		list.add("Boston");
		list.add("Chicago");
		
		Map m = new HashMap<String, Object>();
		m.put("name", "iCoffee");
		m.put("branches", list);
		return m;
	}
	
}
*/