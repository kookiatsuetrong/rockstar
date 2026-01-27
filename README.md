# Rockstar

A light framework for web service and web application.

![](rockstar.png)


```
/login ------------>        --> showLogInPage()

/service-search --->   /    --> showSearchResult()
                       '
/ ----------------->   '    --> showHomePage()
                       '
                       '
                       '-----> Rockstar framework
```

Directory structures
```
sample
'
'
'                                       Development Assets
'                                       ------------------
'-- code
'   '-- Main.java    -------------.
'                                 '
'-- text                          '
'   '-- Test.java                 '
'                                 '
'                                 '
'                                 '     Deployment Assets
'-- web.xml                       '     -----------------
'                                 '
'-- runtime                       '
'   '-- bobcat.jar                '
'   '-- json.jar                  '
'   '-- rockstar.jar              '
'   '-- mysql.jar                 '
'   '-- jakarta-mail.jar          '
'   '-- jakarta-activation.jar    '
'   '                             '
'   '-- Main.class  <-------------'
'
'-- web
    '-- index.jsp
    '-- detail.html

```

Java deployment descriptor (web.xml)
```
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
			<param-value>Web</param-value>
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
```

Sample code
```java
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

```

Compile
```bash
CPATH=runtime/bobcat25.jar
CPATH=$CPATH:"runtime/jakarta-activation.jar"
CPATH=$CPATH:"runtime/jakarta-mail.jar"
CPATH=$CPATH:"runtime/json.jar"
CPATH=$CPATH:"runtime/mysql.jar"
CPATH=$CPATH:"runtime/rockstar.jar"
CPATH=$CPATH:"runtime/"

javac -d runtime --class-path $CPATH --source-path code code/Start.java

java --class-path $CPATH Bobcat --deployment-descriptor web.xml --port 18000
```

Run
```bash
CPATH=runtime/bobcat25.jar
CPATH=$CPATH:"runtime/jakarta-activation.jar"
CPATH=$CPATH:"runtime/jakarta-mail.jar"
CPATH=$CPATH:"runtime/json.jar"
CPATH=$CPATH:"runtime/mysql.jar"
CPATH=$CPATH:"runtime/rockstar.jar"
CPATH=$CPATH:"runtime/"

java --class-path $CPATH Bobcat --deployment-descriptor web.xml

```
