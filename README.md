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
		<servlet-class>Rockstar</servlet-class>
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
import org.json.JSONObject;

class Start {
	
	void main() {
		Rockstar.handle( "GET /",              Start::showReport);
		Rockstar.handle( "GET /report",        Start::showReport);
		Rockstar.handle( "GET /check",         context -> "Rockstar 0.3");
		Rockstar.handle( "GET /service-check", Start::getVersion);
		Rockstar.handle("POST /get-total",     Start::getTotal);
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
		JSONObject data = context.getJson();
		System.out.println(data);
		data.put("output", "OK");
		return data;
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

java --class-path $CPATH Bobcat --deployment-descriptor web.xml --port 17000
```

Run
```bash
CPATH=runtime/bobcat25.jar
CPATH=$CPATH:"runtime/jakarta-activation.jar"
CPATH=$CPATH:"runtime/jakarta-mail.jar"
CPATH=$CPATH:"runtime/json.jar"
CPATH=$CPATH:"runtime/mysql.jar"
CPATH=$CPATH:"runtime/rockstar.jar"

java --class-path $CPATH Bobcat --deployment-descriptor web.xml

```
