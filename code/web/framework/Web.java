package web.framework;

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

class Web {

	void main() {
		Rockstar.handle( "GET /",                 Web::showHome);
		Rockstar.handle( "GET /home",             Web::showHome);
		Rockstar.handle( "GET /report",           Web::showReport);
		Rockstar.handle( "GET /check",            context -> "Rockstar 0.5");
		Rockstar.handle( "GET /service-check",    Web::getVersion);
		Rockstar.handle("POST /get-total",        Web::getTotal);
		Rockstar.handle( "GET /service-branches", Web::listBranches);
	}
	
	static Object showHome(Context context) {
		return new View("/WEB-INF/sample-view.jsp");
	}
	
	static Object showReport(Context context) {
		Map<String, Object> model = new TreeMap<>();
		model.put("brand", "iCoffee");
		model.put("products", new String[] { "Latte", "Mocha", "Espresso" } );
		String buffer = Freemarker.render("/web/WEB-INF/report.html", model);
		return buffer;
	}
	
	static Object getVersion(Context context) {
		var detail = new TreeMap<String,Object>();
		detail.put("version", "0.5");
		detail.put("framework", "Rockstar");
		return detail;
	}
	
	static Object getTotal(Context context) {
		Map m = context.read();
		var result = new TreeMap<String, Object>();
		result.put("result", "OK");
		result.put("output", "3.1415926");
		return result;
	}
	
	static Object listBranches(Context context) {
		var list = new ArrayList<String>();
		list.add("Atlanta");
		list.add("Boston");
		list.add("Chicago");
		
		var m = new TreeMap<String, Object>();
		m.put("name", "iCoffee");
		m.put("branches", list);
		return m;
	}
	
}
