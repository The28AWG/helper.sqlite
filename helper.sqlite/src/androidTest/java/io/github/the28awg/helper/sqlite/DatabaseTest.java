package io.github.the28awg.helper.sqlite;

import junit.framework.TestCase;

/**
 * Created by the28awg on 12.03.16.
 */
public class DatabaseTest extends TestCase {

    public void testDatabase() {
        Database database = Database.name("base.db")
                .table(
                        Table.name("phones").column(
                                Column.name("id").type(Type.INTEGER).notNull().autoincrement().unique()
                        ).column(Column.name("phone").type(Type.VARCHAR).notNull().unique())
                ).table(
                        Table.name("events")
                                .column(Column.name("id").type(Type.INTEGER).notNull().unique().primary())
                                .column(Column.name("phone").type(Type.VARCHAR).notNull().unique().primary())
                                .column(Column.name("data").type(Type.VARCHAR).notNull().unique())
                );
        database.create(null);
    }
}
