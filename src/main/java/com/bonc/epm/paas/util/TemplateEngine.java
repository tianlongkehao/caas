package com.bonc.epm.paas.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

public class TemplateEngine {
	private static final Log log = LogFactory.getLog(TemplateEngine.class);
	private static HttpServletRequest   request;    // servlet参数，用于获取WebContent目录下模板路径
	private static String               confPath;   // 配置文件，包括完整绝对路径
    private static String               enter;      // 换行符
 // 构造函数，初始化数据
    public TemplateEngine(HttpServletRequest request, String confPath){
        TemplateEngine.request = request;
        TemplateEngine.confPath = confPath;
        TemplateEngine.enter = System.getProperty("line.separator");
    }
    
    @Value("${nginxConf.io.confpath}")
    private static String nginxConfPath;
    @Value("${nginxConf.io.cmdpath}")
    private static String nginxCmdPath;
    @Value("${nginxConf.io.confurl}")
    private static String nginxConfUrl;
    
    private static String template;
    static{
    	String path = TemplateEngine.class.getClassLoader().getResource("conf.tpl").getPath();
    	template = TemplateEngine.readConf(path); 
    }
    
    public static void generateConfig(Map<String, String> app,String configName){
		String datastring = TemplateEngine.replaceArgs(template, app);
		log.debug("datastring======="+datastring);
		log.debug("nginxConfPathName"+nginxConfPath+configName);
		TemplateEngine.writeConf(nginxConfPath+configName+".conf", datastring, true);
    }
    
    public static boolean cmdReloadConfig(){
    	log.debug("nginxcmdString====="+nginxCmdPath);
    	return CmdUtil.exeCmd(nginxCmdPath+" -s reload");
    }
    
    public static String getConfUrl(){
    	return nginxConfUrl;
    }
    
    public static boolean deleteConfig(String confName,String configName){
    	try {
    		// 读取配置文件
            String data = readConf(nginxConfPath+configName+".conf");
//    		//test
//            String data = readConf(configName);
//            data = data.replace(getObject(confName,configName), "");
//            writeConf(configName, data, false);
            // 过滤删除内容
            data = data.replace(getObject(confName,nginxConfPath+configName+".conf"), "");
            // 重新写回文件
            writeConf(nginxConfPath+configName+".conf", data, false);
		} catch (Exception e) {
			log.error("读取删除文件路径"+nginxConfPath+configName);
			return false;
		}
    	return true;
    }
    
    /**
     * 替换模板变量
     * 
     * @param template
     * @param data
     * @return
     */
    public static String replaceArgs(String template, Map<String, String> data){
        // sb用来存储替换过的内容，它会把多次处理过的字符串按源字符串序 存储起来。
        StringBuffer sb = new StringBuffer();
        try{
            Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
            Matcher matcher = pattern.matcher(template);
            while(matcher.find()){
                String name = matcher.group(1);// 键名
                String value = (String)data.get(name);// 键值
                if(value == null){
                    value = "";
                }else{
                    value = value.replaceAll("\\$", "\\\\\\$");
                }
                matcher.appendReplacement(sb, value);
            }
            matcher.appendTail(sb);
        }catch(Exception e){
        	log.error("sb:"+sb);
            e.printStackTrace();
        }
        return sb.toString();   //加一个空行（结束行）
    }
    
    /**
     * 写入到配置文件
     * 
     * @param confPath
     * @param stringData
     * @param isAppend 是否追加写入
     */
    public static void writeConf(String confPath, String stringData, boolean isAppend){
        try{
            File f = new File(confPath);
            if( !f.exists()){
                f.createNewFile();
            }
            String fileEncode = System.getProperty("file.encoding");
            FileOutputStream fos = new FileOutputStream(confPath, isAppend);
            OutputStreamWriter osw = new OutputStreamWriter(fos, fileEncode);
            osw.write(new String(stringData.getBytes(), fileEncode));
            osw.close();
            fos.close();
        }catch(Exception e){
        	log.error(e);
            e.printStackTrace();
        }
    }
    
    /**
     * 读取配置文件
     * 
     * @param confPath
     */
    public static String readConf(String confPath){
        StringBuffer sb = new StringBuffer();
        try{
            FileReader fr = new FileReader(confPath);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            br.close();
            fr.close();
        }catch(Exception e){
        	log.error(e);
            e.printStackTrace();
        }
        return sb.toString();
    }
    
    /**
     * 根据中括号内的confName查询对象
     * 
     * @param confName
     * @return
     */
    public static String getObject(String confName,String path){
        StringBuffer sb = new StringBuffer();
        try{
            FileReader fr = new FileReader(path);
            LineNumberReader nr = new LineNumberReader(fr);
            String line = "";
            int startLineNumber = -1;
            while((line = nr.readLine()) != null){
                // 匹配到开头
                if(line.indexOf("upstream " + confName + "{") >= 0){
                    startLineNumber = nr.getLineNumber();
                }
                if(startLineNumber != -1 && nr.getLineNumber() >= startLineNumber){
                    sb.append(line);
                    // 匹配到结束，以换行符结束
                    if(line.trim().equals("")){
                        break;
                    }
                }
            }
            nr.close();
            fr.close();
        }catch(Exception e){
        	log.error(e);
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static HttpServletRequest getRequest() {
		return request;
	}
	public static void setRequest(HttpServletRequest request) {
		TemplateEngine.request = request;
	}
	public static String getConfPath() {
		return confPath;
	}
	public static void setConfPath(String confPath) {
		TemplateEngine.confPath = confPath;
	}
	public static String getEnter() {
		return enter;
	}
	public static void setEnter(String enter) {
		TemplateEngine.enter = enter;
	}
	

}
