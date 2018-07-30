package com.example.android.bookapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bookapp.data.BookContract.BookEntry;
import com.example.android.bookapp.data.BookDbHelper;

/**
        * Displays list of Books that were entered and stored in the app.
        */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the BOOK data loader
     */
    private static final int BOOK_LOADER = 0;

    /**
     * Adapter for the ListView
     */
    BookCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        // Find the ListView which will be populated with the medicine data
        ListView medicineListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        medicineListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of medicine data in the Cursor.
        // There is no medicine data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new BookCursorAdapter(this, null);
        medicineListView.setAdapter(mCursorAdapter);

        // Kick off the loader
        getSupportLoaderManager().initLoader(BOOK_LOADER, null, this);

    }




    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertBook() {


        // Create a ContentValues object where column names are the keys,
        // and Trials's book attributes are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, "TRIALS");
        values.put(BookEntry.COLUMN_PRODUCT_PRICE, "23.79");
        values.put(BookEntry.COLUMN_PRODUCT_QUANTITY, 1);
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, "Isabel Buchanan");
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE, 564326843);

        // Insert a new row for Trial book in the database, returning the ID of that new row.
        // The first argument for db.insert() is the books table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Trial book.
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
    }

    /**
     * Helper method to delete all medicines in the database.
     */
    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from books database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert new book" menu option
            case R.id.action_insert_dummy_data:
                insertBook();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllBooks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRODUCT_PRICE,
                BookEntry.COLUMN_PRODUCT_QUANTITY};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                BookEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null); // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link MedicineCursorAdapter} with this new cursor containing updated medicine data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
