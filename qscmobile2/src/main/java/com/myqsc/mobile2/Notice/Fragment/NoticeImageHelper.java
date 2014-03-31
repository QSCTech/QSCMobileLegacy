package com.myqsc.mobile2.Notice.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.myqsc.mobile2.uti.LogHelper;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by richard on 13-10-18.
 */
public class NoticeImageHelper {
    public static Thread thread = null;
    public static void initPic(final String url, final ImageView imageView) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URLConnection connection = new URL(url).openConnection();
                    InputStream inputStream = connection.getInputStream();
                    final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    final ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(200);
                    int current = 0;

                    while ((current = bufferedInputStream.read()) != -1) {
                        byteArrayBuffer.append((byte)current);
                    }

                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeByteArray(byteArrayBuffer.toByteArray(), 0, byteArrayBuffer.length(), options);

                    final int imageHeight = options.outHeight;
                    final int imageWidth = options.outWidth;

                    if (thread.isInterrupted()) {
                        LogHelper.e("Thread Interrupted");
                        return;
                    }

                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            int width = imageView.getWidth();
                            int height = (int) (imageHeight * ((double) width / imageWidth));

                            options.inSampleSize = calculateInSampleSize(options, width, height);
                            options.inJustDecodeBounds = false;
                            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrayBuffer.toByteArray(), 0,
                                    byteArrayBuffer.toByteArray().length, options);
                            LogHelper.d("Width:" + bitmap.getWidth() + " Height:" + bitmap.getHeight());
                            imageView.setImageBitmap(bitmap);
                            byteArrayBuffer.clear();
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
