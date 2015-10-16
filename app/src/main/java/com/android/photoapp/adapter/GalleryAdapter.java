package com.android.photoapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.photoapp.R;
import com.android.photoapp.ui.PhotoGalleryActivity;
import com.android.photoapp.util.Util;

public class GalleryAdapter extends CursorAdapter {

    public GalleryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolder {
        public final ImageView photo;
        public final TextView caption;
        public final TextView location;

        public ViewHolder(View view) {
            photo = (ImageView) view.findViewById(R.id.photo);
            caption = (TextView) view.findViewById(R.id.caption);
            location = (TextView) view.findViewById(R.id.location);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.photo.setImageBitmap(Util.getBitmapFromPath(cursor.getString(PhotoGalleryActivity.COL__PHOTO_URL)));
        viewHolder.caption.setText(cursor.getString(PhotoGalleryActivity.COL_PHOTO_CAPTION));
        String location = "Location is " + cursor.getString(PhotoGalleryActivity.COL_PHOTO_LATITUDE) + " ,"
                + cursor.getString(PhotoGalleryActivity.COL_PHOTO_LONGITUDE);
        viewHolder.location.setText(location);
    }
}
