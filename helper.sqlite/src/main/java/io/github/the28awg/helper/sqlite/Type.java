package io.github.the28awg.helper.sqlite;

/**
 * Created by the28awg on 11.03.16.
 */
public enum Type {
    VARCHAR("VARCHAR"), INTEGER("INTEGER");

    private String type;
    Type(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
