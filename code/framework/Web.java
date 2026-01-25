package framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
		var detail = new HashMap<String,Object>();
		detail.put("version", "0.4");
		detail.put("framework", "Rockstar");
		return detail;
	}
	
	static Object getTotal(Context context) {
		Map m = context.read();
		var result = new HashMap<String, Object>();
		result.put("result", "OK");
		result.put("output", "3.1415926");
		return result;
	}
	
	static Object listBranches(Context context) {
		var list = new ArrayList<String>();
		list.add("Atlanta");
		list.add("Boston");
		list.add("Chicago");
		
		var m = new HashMap<String, Object>();
		m.put("name", "iCoffee");
		m.put("branches", list);
		return m;
	}
	
}
*/