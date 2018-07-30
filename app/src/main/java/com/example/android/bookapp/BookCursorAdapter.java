package com.example.android.bookapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookapp.data.BookContract;

public class BookCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the medicine data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current medicine can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView price = (TextView) view.findViewById(R.id.price);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        RelativeLayout parentView = (RelativeLayout) view.findViewById(R.id.id_relative_layout);

        // Find the columns of medicine attributes that we're interested in
        int idColumnIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRODUCT_QUANTITY);

        // Read the medicine attributes from the Cursor for the current medicine
        final int rowId = cursor.getInt(idColumnIndex);
        String bookName = cursor.getString(nameColumnIndex);
        double bookPrice = cursor.getDouble(priceColumnIndex);
        final int bookQuantity = cursor.getInt(quantityColumnIndex);

        if (bookQuantity <= 1) {
            quantityTextView.setText(bookQuantity + " " + "unit");
        } else {
            quantityTextView.setText(bookQuantity + " " + "units");
        }

        // Update the TextViews with the attributes for the current medicine
        name.setText(bookName);
        price.setText(String.valueOf(bookPrice));


        parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Detail activity
                Intent intent = new Intent(context, EditorActivity.class);

                // Form the content URI that represents clicked medicine.
                Uri currentInventoryUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, rowId);

                // Set the URI on the data field of the intent
                intent.setData(currentInventoryUri);

                context.startActivity(intent);
            }
        });


        Button sellButton = (Button) view.findViewById(R.id.sell_button);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the Text
                String text = quantityTextView.getText().toString();
                // Split it
                String[] splittedText = text.split(" ");
                // Take the first part
                int quantity = Integer.parseInt(splittedText[0]);

                if (quantity == 0) {
                    Toast.makeText(context, "No more in stock", Toast.LENGTH_SHORT).show();
                } else if (quantity > 0) {
                    quantity = quantity - 1;

                    String quantityString = Integer.toString(quantity);

                    ContentValues values = new ContentValues();
                    values.put(BookContract.BookEntry.COLUMN_PRODUCT_QUANTITY, quantityString);

                    Uri currentInventoryUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, rowId);

                    int rowsAffected = context.getContentResolver().update(currentInventoryUri, values, null, null);

                    if (rowsAffected != 0) {
                        /* update text view if database update is successful */
                        if (bookQuantity <= 1) {
                            quantityTextView.setText(quantity + " " + "unit");
                        } else {
                            quantityTextView.setText(quantity + " " + "units");
                        }
                    } else {
                        Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
