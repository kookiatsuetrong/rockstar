import java.util.HashMap;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class Rockstar extends HttpServlet {
	
	static HashMap<String, Handler> 
	map = new HashMap<>();
	
	public String encoding = "UTF-8";
	
	static void 
	handle(String path, Handler handler) {
		Rockstar.map.put(path, handler);
	}
	
	@Override public void
	init() {
		// TODO: Setup character encoding here
		
		String cl = this.getInitParameter("class");
		String me = this.getInitParameter("method");
		
		if (cl == null) return;
		if (me == null) return;
		
		try {
			Class clazz     = Class.forName(cl);
			Constructor cns = clazz.getDeclaredConstructor();
			cns.setAccessible(true);
			Object app = cns.newInstance();
			Method m = clazz.getDeclaredMethod(me);
			m.setAccessible(true);
			m.invoke(app);
		} catch (Exception e) { }
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
		Handler h = map.get(pattern);
		
		// 1. Find internal handler
		if (valid(h)) {
			Object o = h.handle(context);
			if (o == null) return;
			
			if (o instanceof String)   this.send(context, (String)o);
			if (o instanceof Redirect) this.send(context, (Redirect)o);
			
			if (o instanceof Map) {
				JSONObject result = fromMap((Map)o);
				context.response.setHeader("Content-Type", 
								"application/json; charset=utf-8");
				this.send(context, result.toString());
			}
			
			if (o instanceof JSONObject) {
				context.response.setHeader("Content-Type", 
								"application/json; charset=utf-8");
				this.send(context, o.toString());
			}
			
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

		// 4. Alias, such as /whatever-here
		
		
		// 6. Service not found
		if (pattern.startsWith("/service")) {
			this.sendServiceNotFound(context);
			return;
		}
		
		// 7. Not found
		this.send(context, 404, "Not Found");		
	}
	
	public void send(Context context, Redirect r) {
		try {
			context.response.sendRedirect(r.location);
		} catch (Exception e) { }
		/*
		try {
			context.response.setStatus(301);
			context.response.setCharacterEncoding(encoding);
			context.response.setHeader("Location", r.location);
			
			String text = "Redirect to " + r.location;			
			PrintWriter out = context.response.getWriter();
			out.println(text);
		} catch (Exception e) { }
		*/
	}
	
	public void send(Context context, String text) {
		this.send(context, 200,  text);
	}
	
	public void send(Context context, int code, String text) {
		try {
			context.response.setStatus(code);
			context.response.setCharacterEncoding(encoding);
			PrintWriter out = context.response.getWriter();
			out.println(text);
		} catch (Exception e) { }
	}
	
	public void sendServiceNotFound(Context context) {
		JSONObject reply = new JSONObject();
		reply.put("result", "ERROR");
		reply.put("reason", "Service not found");
		this.send(context, 404, reply.toString());
	}
	
	public static Object render(Context context, String path) {
		try {
			var rd = context.request.getRequestDispatcher(path);
			rd.forward(context.request, context.response);
		} catch (Exception e) { }
	
		return new View(path);
	}

	public static Map toMap(JSONObject input) {
		Map result = new TreeMap<String, Object>();
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
		List list = new ArrayList<Object>();
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
		
		Set<String> keys = map.keySet();
		for (String k : keys) {
			Object value = map.get(k);
			if (value instanceof Map) {
				JSONObject inner = fromMap((Map)value);
				result.put(k, inner);
				continue;
			}
			if (value instanceof List) {
				JSONArray inner = fromList((List)value);
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
			if (value instanceof Map) {
				JSONObject inner = fromMap((Map)value);
				array.put(inner);
				continue;
			}
			if (value instanceof List) {
				JSONArray inner = fromList((List)value);
				array.put(inner);
				continue;
			}
			array.put(value);
		}
		return array;
	}
	
	public static boolean valid(Object o) {
		return o != null;
	}
	
}
