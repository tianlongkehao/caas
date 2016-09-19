/*
 * 文件名：ResultPager.java
 * 版权：Copyright by bonc
 * 描述：
 * 修改人：root
 * 修改时间：2016年9月19日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;

@ConfigurationProperties(prefix = "result.pager")
public class ResultPager {
    private static Integer pageSize = 10;
    
    public Integer getPageSize() {
        return pageSize;
     }

    public void setPageSize(Integer pageSize) {
        ResultPager.pageSize = pageSize;
     }


    /**
     * 
     * Description:
      *  构造PageRequest对象,
     * springData包中的 PageRequest类已经实现了Pageable接口
     * @param pageNumber Integer
     * @param pagzSize Integer
     * @return 
     * @see
     */
    public static PageRequest buildPageRequest(Integer pageNumber, Integer size) {
        if (null == pageNumber) {
            pageNumber = 1;
          }
        if (null == size) {
            size = pageSize;
          }
        return new PageRequest(pageNumber - 1, size, null);
    }
    
    /**
     * 
     * Description:
      * 构造PageRequest对象,
     * springData包中的 PageRequest类已经实现了Pageable接口
     * @param pageNumber String
     * @param pagzSize String
     * @return 
     * @see
     */
    public static PageRequest buildPageRequest(String pageNumber, String size) {
        if (StringUtils.isBlank(pageNumber)) {
            pageNumber = "1";
          }
        if (StringUtils.isBlank(size)) {
            size = pageSize.toString();
          }
        return buildPageRequest(Integer.valueOf(pageNumber),Integer.valueOf(size));
    }
}
