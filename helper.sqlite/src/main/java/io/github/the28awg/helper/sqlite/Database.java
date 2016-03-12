package io.github.the28awg.helper.sqlite;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by the28awg on 11.03.16.
 */
public class Database {
    private String name;
    private int version = 1;
    private Set<Table> tables = new HashSet<>();

    private Database(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static Database name(String name) {
        requireNonNull(name);
        return new Database(name);
    }

    public int version() {
        return version;
    }

    public Database version(int version) {
        requireNonNull(version);
        this.version = version;
        return this;
    }

    public Set<Table> tables() {
        return tables;
    }

    public Database table(Table table) {
        requireNonNull(table);
        this.tables.add(table);
        return this;
    }

    protected void create(SQLiteDatabase db) {
        for (Table table: tables) {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE `").append(table.name()).append("` (");
            boolean autoincrement = false;
            Set<String> primary = new HashSet<>();
//            for (Column column : table.columns()) {
//                if (column.isAutoincrement()) {
//                    autoincrement = column;
//                }
//                if (column.isPrimary()) {
//                    primary.add(column);
//                }
//            }

            Iterator<Column> iterator = table.columns().iterator();
            while (iterator.hasNext()) {
                Column column = iterator.next();
                builder.append("`").append(column.name()).append("` ").append(column.type().type());
                if (column.isNotNull()) {
                    builder.append(" NOT NULL");
                }
                if (column.isAutoincrement() && !autoincrement) {
                    autoincrement = true;
                    builder.append(" PRIMARY KEY AUTOINCREMENT");
                }
                if (column.isPrimary() && !autoincrement) {
                    primary.add(column.name());
                }
                if (column.isUnique()) {
                    builder.append(" UNIQUE");
                }
                if (iterator.hasNext()) {
                    builder.append(",");
                }
            }
            if (!autoincrement) {
                Iterator<String> keys = primary.iterator();
                builder.append(", PRIMARY KEY(");
                while (keys.hasNext()) {
                    builder.append(keys.next());
                    if (keys.hasNext()) {
                        builder.append(",");
                    }
                }
                builder.append(")");
            }
            builder.append(");");
            System.out.println(builder.toString());
        }
    }

    protected void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected static <T> T requireNonNull(T o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return o;
    }
}
