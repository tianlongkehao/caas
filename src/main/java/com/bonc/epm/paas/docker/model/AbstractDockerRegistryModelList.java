package com.bonc.epm.paas.docker.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractDockerRegistryModelList<T> extends AbstractDockerRegistryModel implements Iterable<T> {

	private List<T> items = new ArrayList<T>();

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
