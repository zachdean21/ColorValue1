package com.google.developer.colorvalue.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.developer.colorvalue.CardActivity;
import com.google.developer.colorvalue.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private int hexColumn, nameColumn, iDColumn;

    public CardAdapter(Context context, Cursor cursor) {
        this.mCursor = cursor;
        this.mContext = context;
        populateColumnPositions();
    }

    private void populateColumnPositions() {
        if(mCursor == null) {
            return;
        } else {
            iDColumn = mCursor.getColumnIndex(CardProvider.Contract.Columns._ID);
            hexColumn = mCursor.getColumnIndex(CardProvider.Contract.Columns.COLOR_HEX);
            nameColumn = mCursor.getColumnIndex(CardProvider.Contract.Columns.COLOR_NAME);
        }
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(CardAdapter.ViewHolder holder, int position) {
        // TODO bind data to view

        mCursor.moveToPosition(position);

        String hex = mCursor.getString(hexColumn);
        String name = mCursor.getString(nameColumn);
        int id = mCursor.getInt(iDColumn);

        holder.cardID = id;
        holder.name.setText(hex);
        holder.cardView.setBackgroundColor(Color.parseColor(hex));

        if(Color.luminance(Color.parseColor(hex)) <= 0.5) {
            holder.name.setTextColor(mContext.getResources().getColor(R.color.cardview_light_background, null));
        } else {
            holder.name.setTextColor(mContext.getResources().getColor(R.color.cardview_dark_background, null));
        }

    }

    @Override
    public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
    }

    /**
     * Return a {@link Card} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position Cursor item position
     * @return A new {@link Card}
     */
    public Card getItem(int position) {
        if (mCursor.moveToPosition(position)) {
            return new Card(mCursor);
        }
        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(Cursor data) {
        // Close the current cursor if one exists
        if(mCursor != null) {
            mCursor.close();
        }
        mCursor = data;
        populateColumnPositions();
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public CardView cardView;
        public Integer cardID;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.color_name);
            cardView = (CardView) itemView.findViewById(R.id.card);
            cardID = null;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CardActivity.class);
                    intent.putExtra(CardActivity.EXTRA_CARD_ID, cardID);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
