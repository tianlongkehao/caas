package com.bonc.epm.paas.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmdUtil {

	public static boolean exeCmd(String commandStr) {  
        BufferedReader br = null;  
        try {  
            Process p = Runtime.getRuntime().exec(commandStr);  
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));  
            String line = null;  
            StringBuilder sb = new StringBuilder();  
            while ((line = br.readLine()) != null) {  
                sb.append(line + "\n");  
            }  
            return true;
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;
        }   
        finally  
        {  
            if (br != null)  
            {  
                try {  
                    br.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }
    }  
}
