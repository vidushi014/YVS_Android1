package com.example.voice_notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

public class ListOfAudo extends AppCompatActivity {

    private static final String APP_TAG = "Apptag";
    private RecyclerView audioList;
    private File[] allFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_audo);
        audioList = findViewById(R.id.audio_list_view);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        Context context = getApplicationContext();

// Find the videos that are stored on a device by querying the video collection.
//        Uri collection;
//        String[] projection;
//        String selection;
//        String[] selectionArgs;
//        try (Cursor cursor = context.getContentResolver().query(
//                collection,
//                projection,
//                selection,
//                selectionArgs,
//                sortOrder
//        )) {
//            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
//            while (cursor.moveToNext()) {
//                long id = cursor.getLong(idColumn);
//                Uri videoUri = ContentUris.withAppendedId(
//                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
//                extractVideoLocationInfo(videoUri);
//            }
//        }
//
//        private void extractVideoLocationInfo(Uri videoUri) {
//            try {
//                retriever.setDataSource(context, videoUri);
//            } catch (RuntimeException e) {
//                Log.e(APP_TAG, "Cannot retrieve video file", e);
//            }
//            // Metadata should use a standardized format.
//            String locationMetadata = retriever.extractMetadata(
//                    MediaMetadataRetriever.METADATA_KEY_LOCATION);
//        }
//    }


    }
}