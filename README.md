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

```

Compile and run on Unix, macOS, Linux, ...
```bash
CPATH=runtime/bobcat25.jar
CPATH=$CPATH:"runtime/jakarta-activation.jar"
CPATH=$CPATH:"runtime/jakarta-mail.jar"
CPATH=$CPATH:"runtime/json.jar"
CPATH=$CPATH:"runtime/mysql.jar"
CPATH=$CPATH:"runtime/freemarker.jar"
CPATH=$CPATH:"runtime/"

javac -d runtime --class-path $CPATH \
--source-path code code/web/framework/*.java

java --class-path $CPATH Bobcat --deployment-descriptor web.xml --port 1800
```

Compile and run on Windows

```
set RUNTIME=runtime\bobcat25.jar
set RUNTIME=%RUNTIME%;runtime\jakarta-activation.jar
set RUNTIME=%RUNTIME%;runtime\jakarta-mail.jar
set RUNTIME=%RUNTIME%;runtime\json.jar
set RUNTIME=%RUNTIME%;runtime\mysql.jar
set RUNTIME=%RUNTIME%;runtime\freemarker.jar
set RUNTIME=%RUNTIME%;runtime\

javac -d runtime --class-path %RUNTIME% --source-path code code\web\framework\*.java

java --class-path %RUNTIME% Bobcat --deployment-descriptor web.xml --port 1800

```












