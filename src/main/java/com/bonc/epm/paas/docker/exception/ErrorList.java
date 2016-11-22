/*
 * 文件名：ErrorList.java
 * 版权：Copyright by www.bonc.com.cn
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月18日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.docker.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author ke_wang
 * @version 2016年11月18日
 * @see ErrorList
 * @since
 */

public class ErrorList implements Iterable<Error> {

    private List<Error> errors = new ArrayList<Error>();

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
    
    @Override
    public Iterator<Error> iterator() {
        return getErrors().iterator();
    }

    public int size() {
        return getErrors().size();
    }

    public Error get(int i) {
        return getErrors().get(i);
    }

    @Override
    public String toString() {
        return " [items=" + StringUtils.join(getErrors(), ',') + "]";
    }

}
