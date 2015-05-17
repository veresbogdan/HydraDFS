package hydradfs.utils;

import java.io.Serializable;

public class MyData<K> implements Serializable {
    private static final long serialVersionUID = 2098774660703812030L;

    private K key;

    private K domain;

    private K content;

    private K data;

    public K key() {
        return key;
    }

    public MyData<K> key(K key) {
        this.key = key;
        return this;
    }

    public Object domain() {
        return domain;
    }

    public MyData<K> domain(K domain) {
        this.domain = domain;
        return this;
    }

    public K content() {
        return content;
    }

    public MyData<K> content(K content) {
        this.content = content;
        return this;
    }

    public K data() {
        return data;
    }

    public MyData<K> data(K data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "MyData{" +
                "key=" + key +
                ", domain=" + domain +
                ", content=" + content +
                '}';
    }
}
