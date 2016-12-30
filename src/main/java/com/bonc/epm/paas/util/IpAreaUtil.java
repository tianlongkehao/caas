/*
 * 文件名：IpAreaUtil.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：zhoutao
 * 修改时间：2016年12月30日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 *根据ip地址查询地区信息
 * @author zhoutao
 * @version 2016年12月30日
 * @see IpAreaUtil
 * @since
 */
public class IpAreaUtil {
    
    public static String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
    
    public static String findArea(String ip) {  
        // 测试ip 219.136.134.157 中国=华南=广东省=广州市=越秀区=电信  
        String result = "";
        try {
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("ip", ip);
            String data = WebClientUtil.doPost(urlStr, params);
            JSONObject jsonObj = JSONObject.parseObject(data);
            String country = jsonObj.getJSONObject("data").getString("country");
            String area = jsonObj.getJSONObject("data").getString("area");
            String city = jsonObj.getJSONObject("data").getString("city");
            result = country + " " + area + " " + city;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }   
}
