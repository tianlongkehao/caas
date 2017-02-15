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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *根据ip地址查询RouteTable
 * @author lkx
 * @version 2017年2月15日 14:23:05
 * @see RouteTableUtils
 * @since
 */
public class RouteTableUtils {

    /**
     * LOG
     */
    private static final Logger LOG = LoggerFactory.getLogger(RouteTableUtils.class);

    public static String urlBefore = "http://";
    public static String urlAfter = ":8282/flash/jobs/check/routetable";

    public static String getRouteTable(String ip) {
        String result = "";
        try {
        	String url = urlBefore + ip + urlAfter;
            result = WebClientUtil.doGetForJson(url, null);
        }
        catch (Exception e) {
            LOG.error("get RouteTable error" + e.getMessage());
        }
        return result;
    }
}
