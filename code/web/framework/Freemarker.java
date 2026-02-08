package web.framework;

import java.io.File;
import java.io.Writer;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.Version;

public class Freemarker {
	
	static Configuration detail;
	static Version version = Configuration.VERSION_2_3_32;
	
	static {
		try {
			detail = new Configuration(version);
			detail.setDirectoryForTemplateLoading(new File("."));
			detail.setDefaultEncoding("UTF-8");
			detail.setObjectWrapper(
					new DefaultObjectWrapper(version));
		} catch (Exception e) { 
			System.out.println(e);
		}
	}
	
	public static String render(String path, Map<String,Object> data) {
		try {
			Template template = detail.getTemplate(path);
			Writer writer = new StringWriter();
			template.process(data, writer);
			String result = writer.toString();
			writer.flush();
			return result;
		} catch (Exception e) {
			return "View Error: " + e.toString();
		}
	}
	
}

