mkdir runtime
mkdir code
mkdir web
mkdir web/WEB-INF

curl --output runtime/bobcat25.jar https://codestar.work/bobcat25.jar
curl --output runtime/json.jar https://codestar.work/json.jar
curl --output runtime/rockstar.jar https://codestar.work/rockstar.jar
curl --output runtime/mysql.jar https://codestar.work/mysql.jar
curl --output runtime/jakarta-activation.jar \
https://codestar.work/jakarta-activation.jar
curl --output runtime/jakarta-mail.jar \
https://codestar.work/jakarta-mail.jar

cat <<EOF > web/index.jsp
<% out.println("This is a JSP content"); %>
EOF

cat <<EOF > web/WEB-INF/sample-view.jsp
<% out.println("This is a view"); %>
EOF

cat <<EOF > web.xml
<web-app>
	<session-config>
		<cookie-config>
			<name>CARD</name>
		</cookie-config>
		<session-timeout>1440</session-timeout>
	</session-config>
	
	<servlet>
		<servlet-name>rockstar-servlet</servlet-name>
		<servlet-class>web.framework.Rockstar</servlet-class>
		<init-param>
			<param-name>class</param-name>
			<param-value>Start</param-value>
		</init-param>
		<init-param>
			<param-name>method</param-name>
			<param-value>main</param-value>
		</init-param>
	</servlet>
	<servlet-mapping>
		<servlet-name>rockstar-servlet</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
	
</web-app>
EOF


cat <<EOF > code/Start.java
import web.framework.Rockstar;
import web.framework.Redirect;
import web.framework.Handler;
import web.framework.Context;
import web.framework.View;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

class Start {

	void main() {
		Rockstar.handle( "GET /",              Start::showReport);
		Rockstar.handle( "GET /report",        Start::showReport);
		Rockstar.handle( "GET /check",         context -> "Rockstar 0.5");
		Rockstar.handle( "GET /service-check", Start::getVersion);
		Rockstar.handle("POST /list-branch",   Start::listBranches);
	}
	
	static Object showReport(Context context) {
		return Rockstar.render(context, "/WEB-INF/sample-view.jsp");
	}
	
	static Object getVersion(Context context) {
		JSONObject detail = new JSONObject();
		detail.put("version", "0.5");
		detail.put("framework", "Rockstar");
		return detail;
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

EOF
