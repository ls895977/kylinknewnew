package com.jky.baselibrary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class BaseDBManager {
    /**
     * Insert a row into the specified {@code table} and notify any subscribed queries.
     *
     * @see SQLiteDatabase#insert(String, String, ContentValues)
     */
    public abstract long insert(@NonNull String table, @NonNull ContentValues values);

    /**
     * Delete rows from the specified {@code table} and notify any subscribed queries. This method
     * will not trigger a notification if no rows were deleted.
     *
     * @see SQLiteDatabase#delete(String, String, String[])
     */
    public abstract int delete(@NonNull String table, @Nullable String whereClause,
                               @Nullable String... whereArgs);

    /**
     * Update rows in the specified {@code table} and notify any subscribed queries. This method
     * will not trigger a notification if no rows were updated.
     *
     * @see SQLiteDatabase#update(String, ContentValues, String, String[])
     */
    public abstract int update(@NonNull String table, @NonNull ContentValues values,
                               @Nullable String whereClause, @Nullable String... whereArgs);

    /**
     * Runs the provided SQL and returns a {@link Cursor} over the result set.
     *
     * @see SQLiteDatabase#rawQuery(String, String[])
     */
    public abstract Cursor query(@NonNull String sql, @NonNull String... selectionArgs);

    public abstract void addColumn(String tableName, String columnName, String dataType);
}
