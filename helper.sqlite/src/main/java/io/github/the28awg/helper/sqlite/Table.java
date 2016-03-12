package io.github.the28awg.helper.sqlite;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by the28awg on 11.03.16.
 */
public class Table {
    private String name;
    private Set<Column> columns = new HashSet<>();

    private Table(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static Table name(String name) {
        Database.requireNonNull(name);
        return new Table(name);
    }

    public Set<Column> columns() {
        return columns;
    }

    public Table column(Column column) {
        Database.requireNonNull(column);
        this.columns.add(column);
        return this;
    }
}
