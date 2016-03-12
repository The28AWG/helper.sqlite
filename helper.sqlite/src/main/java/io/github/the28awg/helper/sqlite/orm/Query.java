package io.github.the28awg.helper.sqlite.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.github.the28awg.helper.sqlite.Storage;

/**
 * Created by the28awg on 11.03.16.
 */
public class Query {

    public static int delete(String table, String whereClause, String[] whereArgs) {
        try {
            return Storage.storage().writableDatabase().delete(table, whereClause, whereArgs);
        } finally {
            Storage.storage().closeWritable();
        }
    }

    public static int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return updateWithOnConflict(table, values, whereClause, whereArgs, SQLiteDatabase.CONFLICT_NONE);
    }

    public static int updateWithOnConflict(String table, ContentValues values, String whereClause, String[] whereArgs, int conflictAlgorithm) {
        try {
            return Storage.storage().writableDatabase().updateWithOnConflict(table, values, whereClause, whereArgs, conflictAlgorithm);
        } finally {
            Storage.storage().closeWritable();
        }
    }

    public static <T> void insert(String table, Mapper<T> mapper, T[] contents) {
        Storage.storage().writableDatabase().beginTransaction();
        for (T content : contents) {
            try {
                Storage.storage().writableDatabase().insert(table, null, mapper.mapper(content));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Storage.storage().writableDatabase().setTransactionSuccessful();
        Storage.storage().writableDatabase().endTransaction();
        Storage.storage().closeWritable();
    }

    public static <T> List<T> select(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, int recordCount, int startOffset, Mapper<T> mapper) {
        List<T> result = new ArrayList<>();
        String limit = null;
        if (recordCount > 0) {
            if (startOffset > 0) {
                limit = String.format("%d, %d", startOffset, recordCount);
            } else {
                limit = String.format("%d", recordCount);
            }
        }
        Cursor cursor = null;
        try {
            cursor = Storage.storage().readableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor.moveToFirst()) {
                do {
                    result.add(mapper.mapper(cursor));
                } while (cursor.moveToNext());
            }
            return result;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            Storage.storage().closeReadable();
        }
    }

    public static long count(String table) {
        try {
            return DatabaseUtils.longForQuery(Storage.storage().readableDatabase(), "select count(*) from " + table, null);
        } catch (Exception ignore) {
            return 0;
        } finally {
            Storage.storage().closeReadable();
        }
    }
}
