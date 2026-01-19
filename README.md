# rockstar

A small framework for web service &amp; web application


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

Command Line
```
CPATH=runtime/bobcat25.jar
CPATH=$CPATH:"runtime/jakarta.activation.jar"
CPATH=$CPATH:"runtime/jakarta.mail.jar"
CPATH=$CPATH:"runtime/json.jar"
CPATH=$CPATH:"runtime/mysql.jar"
CPATH=$CPATH:"runtime/rockstar.jar"

java --class-path $CPATH Bobcat --deployment-descriptor web.xml

```

