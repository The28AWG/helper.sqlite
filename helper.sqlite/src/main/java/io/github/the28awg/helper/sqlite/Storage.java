package io.github.the28awg.helper.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by the28awg on 24.12.15.
 */
public class Storage {

    private Helper helper;
    private Context context;
    private ReentrantLock lock = new ReentrantLock();
    private AtomicInteger mWritableOpenCounter = new AtomicInteger();
    // доступная для чтения и записи база, более медленная
    private SQLiteDatabase writableDatabase;
    private AtomicInteger mReadableOpenCounter = new AtomicInteger();
    // более быстрая база, но доступная только для чтения
    private SQLiteDatabase readableDatabase;
    private Database database;

    private Storage() {

    }

    public void initialize(Context context, Database database) {
        try {
            lock.lock();
            if (this.context == null) {
                this.context = context;
                this.database = database;
                this.helper = new Helper(this.context, this.database);
            }
        } finally {
            lock.unlock();
        }
    }

    public SQLiteDatabase writableDatabase() {
        if (mWritableOpenCounter.incrementAndGet() == 1) {
            writableDatabase = helper.getWritableDatabase();
        }
        if (!writableDatabase.isOpen()) {
            writableDatabase = helper.getWritableDatabase();
        }
        return writableDatabase;
    }

    public SQLiteDatabase readableDatabase() {
        if (mReadableOpenCounter.incrementAndGet() == 1) {
            readableDatabase = helper.getReadableDatabase();
        }
        if (!readableDatabase.isOpen()) {
            readableDatabase = helper.getReadableDatabase();
        }
        return readableDatabase;
    }

    public void closeWritable() {
        if (mWritableOpenCounter.decrementAndGet() == 0) {
            if (writableDatabase != null && writableDatabase.isOpen()) {
                writableDatabase.close();
                writableDatabase = null;
            }
        }
    }

    public void closeReadable() {
        if (mReadableOpenCounter.decrementAndGet() == 0) {
            if (readableDatabase != null && readableDatabase.isOpen()) {
                readableDatabase.close();
                readableDatabase = null;
            }
        }
    }

    public Database database() {
        return database;
    }

    public static Storage storage() {
        return Holder.STORAGE;
    }

    private static class Holder {
        private static final Storage STORAGE = new Storage();
    }

    private class Helper extends SQLiteOpenHelper {

        private Database database;
        public Helper(Context context, Database database) {
            super(context, database.name(), null, database.version());
            this.database = database;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            database.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            database.upgrade(db, oldVersion, newVersion);
        }
    }
}