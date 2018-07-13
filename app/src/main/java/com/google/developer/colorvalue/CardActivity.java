package com.google.developer.colorvalue;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.app.LoaderManager;

import com.google.developer.colorvalue.data.CardProvider;
import com.google.developer.colorvalue.data.CardSQLite;
import com.google.developer.colorvalue.service.CardService;
import com.google.developer.colorvalue.ui.ColorView;

public class CardActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_CARD_ID = "extra_card_id";
    public static final int SINGLE_CARD_LOADER = 1;
    public static final String SAVED_COLOR_ID = "saved_color_id";

    private ColorView colorView;
    private TextView colorNameTextView, colorHexTextView;
    private int colorID;
    private Uri rowUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        colorView = (ColorView) findViewById(R.id.colorView);
        colorNameTextView = (TextView) findViewById(R.id.colorNameTextView);
        colorHexTextView = (TextView) findViewById(R.id.colorHexTextView);

        // Get the _ID of the tapped colour
        if(savedInstanceState != null) {
            colorID = savedInstanceState.getInt(SAVED_COLOR_ID);
        } else {
            colorID = getIntent().getExtras().getInt(EXTRA_CARD_ID);
        }

        // Create URI for this row
        rowUri = Uri.withAppendedPath(CardProvider.Contract.CONTENT_URI, Integer.toString(colorID));

        // Get the data from the content provider
        getLoaderManager().initLoader(SINGLE_CARD_LOADER, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_COLOR_ID, colorID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_delete) {
            // Call the Card Service's Delete Method
            CardService.deleteCard(this, rowUri);
            // End the Activity
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;

        if(id == SINGLE_CARD_LOADER) {
            loader = new CursorLoader(
                    this,
                    rowUri,
                    new String[]{
                            CardProvider.Contract.Columns._ID,
                            CardProvider.Contract.Columns.COLOR_HEX,
                            CardProvider.Contract.Columns.COLOR_NAME},
                    null,
                    null,
                    CardProvider.Contract.Columns._ID);
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == SINGLE_CARD_LOADER) {
            updateView(data);
        }
    }

    private void updateView(Cursor data) {
        if(data != null) {
            data. moveToFirst();
            String hex = data.getString(data.getColumnIndex(CardProvider.Contract.Columns.COLOR_HEX));
            String name = data.getString(data.getColumnIndex(CardProvider.Contract.Columns.COLOR_NAME));
            colorView.setBackgroundColor(Color.parseColor(hex));
            colorNameTextView.setText(name);
            colorHexTextView.setText(hex);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
