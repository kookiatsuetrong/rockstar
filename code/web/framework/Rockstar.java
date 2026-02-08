package web.framework;

import java.io.File;
import java.io.PrintWriter;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class Rockstar extends HttpServlet {
	
	static HashMap<String, Handler> map = new HashMap<>();
	
	public String encoding = "UTF-8";
	
	public static void 
	handle(String path, Handler handler) {
		Rockstar.map.put(path, handler);
	}
	
	static ArrayList<Handler> list = new ArrayList<>();
	
	public static void
	addFallback(Handler handler) {
		list.add(handler);
	}
	
	public static boolean
	valid(Object o) {
		return o != null;
	}
	
	@Override public void
	init() {
		// TODO: Setup character encoding here
		String cl = this.getInitParameter("class");
		String me = this.getInitParameter("method");
		
		if (cl == null) return;
		if (me == null) return;
		
		try {
			Class<?> clz = Class.forName(cl);
			Constructor<?> cns = clz.getDeclaredConstructor();
			cns.setAccessible(true);
			Object app = cns.newInstance();
			Method m = clz.getDeclaredMethod(me);
			m.setAccessible(true);
			m.invoke(app);
			System.out.println("Rockstar framework is ready.");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	@Override public void 
	service(HttpServletRequest request,
			HttpServletResponse response) {
		String verb = request.getMethod();
		String uri  = request.getRequestURI();
		String pattern = verb + " " + uri;
		
		System.out.println("Request Pattern: " + pattern);
		
		try {
			request.setCharacterEncoding(this.encoding);
		} catch (Exception e) { }
		
		Context context = new Context(request, response);
		Handler handler = map.get(pattern);
		
		// 1. Find internal handler
		if (valid(handler)) {
			Object result = handler.handle(context);
			processHandler(context, result);
			return;
		}
		
		var sc = getServletContext();

		// 2. JSP file
		/*
		if (uri.endsWith(".jsp")) {
			try {
				System.out.println("JSP " + pattern);
				var rd = sc.getRequestDispatcher(uri);
				rd.forward(request, response);
				return;
			} catch (Exception e) { }
		}
		*/
		
		// 3. Static file
		try {
			var path  = sc.getRealPath(uri);
			var file  = new File(path);
			var found = file.exists();

			// TODO: Some static files must be protected. For example:
			//
			//       /uploaded/contact-1234-123.*
			//                 ^^^^^^^
			//                 '-------------------> Allow only administrator
			//
			//       /uploaded/hidden-12345.png
			//                        ^^^^^
			//                        '------------> Allow only the owner
			
			if (found) {
				System.out.println("Static file " + file);
				var rd = sc.getNamedDispatcher("default");
				rd.forward(request, response);
				return;
			}
		} catch (Exception e) { }

		// 4. Alias for fallback, such as /whatever-here
		for (Handler target : list) {
			Object result = target.handle(context);
			if (result == null) continue; // process next fallback
			processHandler(context, result);
			return; // don't process next alias
		}
		
		// 5. Service not found
		if (pattern.startsWith("/service")) {
			JSONObject reply = new JSONObject();
			reply.put("result", "ERROR");
			reply.put("reason", "Service not found");
			String text = reply.toString();
			send(context, 404, "application/json", text);
			return;
		}
		
		// 6. Not found
		this.send(context, 404, "text/html", "Not Found");		
	}
	
	void send(Context context, int code, String type, String text) {
		try {
			context.response.setStatus(code);
			context.response.setHeader("Content-Type", type + ";charset=utf-8");
			context.response.setCharacterEncoding(encoding);
			PrintWriter out = context.response.getWriter();
			out.println(text);
		} catch (Exception e) { }
	}
	
	void send(Context context, String text) {
		this.send(context, 200, "text/html", text);
	}
	
	void send(Context context, Redirect r) {
		try {
			context.response.sendRedirect(r.location);
		} catch (Exception e) { }
	}
	
	void send(Context context, Map map) {
		String text = fromMap(map).toString();
		this.send(context, 200, "application/json", text);
	}
	
	void send(Context context, List list) {
		String text = fromList(list).toString();
		this.send(context, 200, "application/json", text);
	}
	
	void send(Context context, JSONObject data) {
		if (data == null) data = new JSONObject();
		this.send(context, 200, "application/json", data.toString());
	}
	
	void send(Context context, JSONArray data) {
		if (data == null) data = new JSONArray();
		this.send(context, 200, "application/json", data.toString());
	}
	
	void processHandler(Context context, Object result) {
		if (result == null) return;
		if (result instanceof String     s) this.send(context, s);
		if (result instanceof Redirect   r) this.send(context, r);
		if (result instanceof Map        m) this.send(context, m);
		if (result instanceof List       l) this.send(context, l);
		if (result instanceof JSONObject o) this.send(context, o);		
		if (result instanceof JSONArray  a) this.send(context, a);
		
		if (result instanceof View       v) this.render(context, v);
		
		// TODO: Add fallback
	}
	
	void render(Context context, View view) {
		try {
			var rd = context.request.getRequestDispatcher(view.location);
			rd.forward(context.request, context.response);
		} catch (Exception e) { }
	}
	
	
	/* Obsoleted
	void redirect(Context context, String path) {
		this.send(context, new Redirect(path));
	}
	*/
	
	/* Obsoleted
	public static Object render(Context context, String path) {
		try {
			var rd = context.request.getRequestDispatcher(path);
			rd.forward(context.request, context.response);
		} catch (Exception e) { }
	
		return new View(path);
	}
	*/

	public static Map toMap(JSONObject input) {
		TreeMap<String, Object> result = new TreeMap<String, Object>();
		if (input == null) return result;
		
		Iterator<String> keys = input.keys();
		while (keys.hasNext()) {
			String k = keys.next();
			Object value = input.get(k);
			
			if (value instanceof JSONObject) {
				Map inner = toMap((JSONObject)value);
				result.put(k, inner);
				continue;
			}
			if (value instanceof JSONArray) {
				List inner = toList((JSONArray)value);
				result.put(k, inner);
				continue;
			}
			
			result.put(k, value);
		}
		return result;
	}
	
	public static List toList(JSONArray array) {
		ArrayList<Object> list = new ArrayList<>();
		if (array == null) return list;
		
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			
			if (value instanceof JSONObject) {
				Map inner = toMap((JSONObject)value);
				list.add(inner);
				continue;
			}
			if (value instanceof JSONArray) {
				List inner = toList((JSONArray)value);
				list.add(inner);
				continue;
			}
			list.add(value);
		}
		return list;
	}
	
	public static JSONObject fromMap(Map map) {
		JSONObject result = new JSONObject();
		if (map == null) return result;
		
		Map<String,Object> m = new TreeMap<>();
		for (Object k : map.keySet()) {
			String s = (String)k;
			Object e = map.get(k);
			m.put(s, e);
		}
	
		Set<String> keys = m.keySet();
		for (String k : keys) {
			Object value = m.get(k);
			if (value instanceof Map p) {
				JSONObject inner = fromMap(p);
				result.put(k, inner);
				continue;
			}
			if (value instanceof List l) {
				JSONArray inner = fromList(l);
				result.put(k, inner);
				continue;
			}
			result.put(k, value);
		}
		return result;
	}
	
	public static JSONArray fromList(List list) {
	 	JSONArray array = new JSONArray();
		if (list == null) return array;
		
		for (Object value : list) {
			if (value instanceof Map m) {
				JSONObject inner = fromMap(m);
				array.put(inner);
				continue;
			}
			if (value instanceof List item) {
				JSONArray inner = fromList(item);
				array.put(inner);
				continue;
			}
			array.put(value);
		}
		return array;
	}
	
}
