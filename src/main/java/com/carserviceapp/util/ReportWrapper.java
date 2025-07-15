package com.carserviceapp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Generic wrapper for reporting or collection utilities.
 * Used for wrapping collections in context-aware manner in ServiceManager.
 */
public class ReportWrapper<T> {
    private final List<T> items;

    public ReportWrapper(Collection<T> source) {
        this.items = new ArrayList<>(source);
    }

    public List<T> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void addItem(T item) {
        items.add(item);
    }

    public void removeItem(T item) {
        items.remove(item);
    }

    public T get(int index) {
        return items.get(index);
    }
}
