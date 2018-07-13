package com.google.developer.colorvalue.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Arrays;

import static com.google.developer.colorvalue.data.CardProvider.Contract.CONTENT_URI;
import static com.google.developer.colorvalue.data.CardProvider.Contract.TABLE_NAME;

public class CardProvider extends ContentProvider {

    /** Matcher identifier for all cards */
    private static final int CARD = 100;
    /** Matcher identifier for one card */
    private static final int CARD_WITH_ID = 102;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.google.developer.colorvalue/cards
        sUriMatcher.addURI(CardProvider.Contract.CONTENT_AUTHORITY, TABLE_NAME, CARD);
        // content://com.google.developer.colorvalue/cards/#
        sUriMatcher.addURI(CardProvider.Contract.CONTENT_AUTHORITY, TABLE_NAME + "/#", CARD_WITH_ID);
    }

    private CardSQLite mCardSQLite;

    @Override
    public boolean onCreate() {
        mCardSQLite = new CardSQLite(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
            @Nullable String selection, @Nullable String[] selectionArgs,
            @Nullable String sortOrder) {
        // TODO Implement query function by Uri all cards or single card by id

        Cursor cursor = null;
        SQLiteDatabase db = mCardSQLite.getReadableDatabase();
        int URIMatch = sUriMatcher.match(uri);

        switch(URIMatch) {
            case CARD:
                cursor = db.query(Contract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CARD_WITH_ID:
                long rowID = ContentUris.parseId(uri);
                String rowSelection = Contract.Columns._ID +" = ?";
                cursor = db.query(Contract.TABLE_NAME, projection,
                        rowSelection, new String[] {Long.toString(rowID)},
                        null, null, sortOrder);
                break;
        }

        return cursor;

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // TODO Implement insert new color and return Uri with ID

        SQLiteDatabase db = mCardSQLite.getWritableDatabase();
        long rowID = -1;
        Uri rowURI = null;
        int URIMatch = sUriMatcher.match(uri);
        switch(URIMatch) {
            case CARD:
                rowID = db.insert(Contract.TABLE_NAME, null, values);
                // content://com.google.developer.colorvalue/cards/rowID
                rowURI = ContentUris.withAppendedId(Contract.CONTENT_URI, rowID);
                break;
            case CARD_WITH_ID:
                Log.d("Insert", "Came into CARD_WITH_ID");
                break;
        }

        return  rowURI;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        // TODO delete card by Uri

        SQLiteDatabase db = mCardSQLite.getWritableDatabase();
        int rowsDeleted = -1;

        int URIMatch = sUriMatcher.match(uri);
        switch(URIMatch) {
            case CARD:
                Log.d("Delete", "Came into CARD");
                break;
            case CARD_WITH_ID:
                long rowID = ContentUris.parseId(uri);
                String rowSelection = Contract.Columns._ID +" = ?";
                rowsDeleted = db.delete(
                        Contract.TABLE_NAME,
                        rowSelection,
                        new String[] {Long.toString(rowID)});
                break;
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
            @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("This provider does not support updates");
    }

    /**
     * Database contract
     */
    public static class Contract {
        public static final String TABLE_NAME = "cards";
        public static final String CONTENT_AUTHORITY = "com.google.developer.colorvalue";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
                .authority(CONTENT_AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();

        public static final class Columns implements BaseColumns {
            public static final String COLOR_HEX = "hex";
            public static final String COLOR_NAME = "name";
        }
    }

}
