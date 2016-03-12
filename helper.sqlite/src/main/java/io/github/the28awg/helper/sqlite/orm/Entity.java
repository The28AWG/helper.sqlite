package io.github.the28awg.helper.sqlite.orm;

/**
 * Created by the28awg on 12.03.16.
 */
public class Entity {
    private long id = -1;

    public long id() {
        return id;
    }

    public void id(long id) {
        this.id = id;
    }
}
