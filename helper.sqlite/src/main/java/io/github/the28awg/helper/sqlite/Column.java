package io.github.the28awg.helper.sqlite;

/**
 * Created by the28awg on 11.03.16.
 */
public class Column {
    private String name;
    private Type type = Type.VARCHAR;
    private boolean primary = false;
    private boolean notNull = false;
    private boolean autoincrement = false;
    private boolean unique = false;

    private Column(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static Column name(String name) {
        Database.requireNonNull(name);
        return new Column(name);
    }

    public Type type() {
        return type;
    }

    public Column type(Type type) {
        Database.requireNonNull(type);
        this.type = type;
        return this;
    }

    public boolean isPrimary() {
        return primary;
    }

    public Column primary() {
        this.primary = true;
        return this;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public Column notNull() {
        this.notNull = true;
        return this;
    }

    public boolean isAutoincrement() {
        return autoincrement;
    }

    public Column autoincrement() {
        this.autoincrement = true;
        return this;
    }

    public boolean isUnique() {
        return unique;
    }

    public Column unique() {
        this.unique = true;
        return this;
    }
}
