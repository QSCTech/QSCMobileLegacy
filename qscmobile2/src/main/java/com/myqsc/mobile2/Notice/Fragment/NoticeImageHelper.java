package com.myqsc.mobile2.Notice.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by richard on 13-10-18.
 */
public class NoticeImageHelper {
    public static void initPic(final String url, final ImageView imageView) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URLConnection connection = new URL(url).openConnection();
                    InputStream inputStream = connection.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(200);
                    int current = 0;

                    while ((current = bufferedInputStream.read()) != -1) {
                        byteArrayBuffer.append((byte)current);
                    }
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrayBuffer.toByteArray(), 0,
                            byteArrayBuffer.toByteArray().length);
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}