package io.github.the28awg.helper.sqlite.orm;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by the28awg on 12.03.16.
 */
public interface Mapper<T> {

    ContentValues mapper(T mapper);

    T mapper(Cursor cursor);
}
