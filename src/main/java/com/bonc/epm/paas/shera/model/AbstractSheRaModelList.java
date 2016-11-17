/*
 * 文件名：AbstractSheRaModelList.java
 * 版权：Copyright by www.huawei.com
 * 描述：
 * 修改人：ke_wang
 * 修改时间：2016年11月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.bonc.epm.paas.shera.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;



/**
 * @author ke_wang
 * @version 2016年11月10日
 * @see AbstractSheRaModelList
 * @since
 */

public abstract class AbstractSheRaModelList<T> extends AbstractSheRaModel implements Iterable<T> {
    private List<T> items = new ArrayList<T>();

    protected AbstractSheRaModelList(Kind kind) {
        super(kind);
    }
    
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Iterator<T> iterator() {
        return getItems().iterator();
    }

    public int size() {
        return getItems().size();
    }

    public T get(int i) {
        return getItems().get(i);
    }

    @Override
    public String toString() {
        return " [items=" + StringUtils.join(getItems(), ',') + "]";
    }
}
