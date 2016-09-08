package com.bonc.epm.paas.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ErrorController
 * @author zhoutao
 * @version 2016年9月6日
 * @see MainsiteErrorController
 * @since
 */
@Controller
public class MainsiteErrorController implements ErrorController {
    /**
     * ERROR_PATH
     */
    private static final String ERROR_PATH = "/error";
	 
    /**
     * Description: <br>
     * handleError
     * @return exception.jsp
     * @see
     */
    @RequestMapping(value=ERROR_PATH)
    public String handleError(){
        return "exception.jsp";
    }
	 
    /**
     * 获取错误页面路径
     */
    @Override
	public String getErrorPath() {
        return ERROR_PATH;
    }

}
