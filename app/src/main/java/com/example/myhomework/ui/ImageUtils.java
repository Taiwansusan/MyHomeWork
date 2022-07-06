package com.example.myhomework.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImageUtils {
    private static String TAG = ImageUtils.class.getName();
    private static ImageUtils mImageUtils;
    private static int IMAGE_DEF_SIZE = 200;

    private Context mContext;

    private static Handler mHandler;

    private LruCache<String, Bitmap> mMemoryCache;
    int maxMemory = (int) Runtime.getRuntime().maxMemory();
    int cacheSize = maxMemory / 8; // 設定cache記憶體大小為程式的記憶體總大小的1/8

    //解決錯位問題，定義一個存標記的集合
    private Map<ImageView, String> mTags = new LinkedHashMap<ImageView, String>();

    public static ImageUtils getInstance(Context context){
        if(mImageUtils == null){
            mImageUtils = new ImageUtils(context);
        }
        return mImageUtils;
    }

    public ImageUtils(Context context) {
        this.mContext = context;

        if (mHandler == null) {
            //實例化Handler
            mHandler = new Handler();
        }
        if (this.mMemoryCache == null) {
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount();
                }
            };
        }
    }

    public Bitmap getCircleBitmap(Bitmap bmp) {
        Bitmap output = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(bmp.getWidth() / 2 + 0.7f,
                bmp.getHeight() / 2 + 0.7f, bmp.getWidth() / 2 + 0.7f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        if(!bmp.isRecycled()) {
            canvas.drawBitmap(bmp, rect, rect, paint);
//            bmp.recycle();
        }

        return output;
    }


    /**
     * 給imageView加載url對應的圖片
     *
     * @param iv
     * @param url
     */
    public void display(ImageView iv, String url,String fileName) {
        display(false,iv,url,fileName);
    }

    public void display(boolean updateImg,ImageView iv, String url,String fileName) {
        display(updateImg, iv, url, fileName, IMAGE_DEF_SIZE,false);
    }

    /**
     * 給imageView加載url對應的圖片
     *
     * @param iv
     * @param url
     */
    public void display(boolean updateImg,ImageView iv, String url,String fileName, int size,boolean noneedfix) {

        if(url.isEmpty()){
            return;
        }
        fileName = fileName.replaceAll("[\\W]", "");

        if(!fileName.isEmpty()&&!updateImg) {
            //從本地獲取
            Bitmap bitmap = loadFromLocal(fileName);
            if (bitmap != null) {
                //本地有,顯示
                iv.setImageBitmap(bitmap);
                return;
            }
        }

        //從網絡中獲取
        loadFromNet(iv, url, 0, fileName, size,noneedfix);

    }

    /**
     * 給imageView加載url對應的圖片
     *
     * @param iv
     * @param url
     */
    public void circleDisplay(ImageView iv, String url,String fileName) {
        circleDisplay(false,iv,url,fileName);
    }


    /**
     * 給imageView加載url對應的圖片
     *
     * @param iv
     * @param url
     */
    public void circleDisplay(boolean updateImg,ImageView iv, String url,String fileName) {
        if(url.isEmpty()){
            return;
        }
        fileName = fileName.replaceAll("[\\W]", "");
        if(!fileName.isEmpty()&&!updateImg) {
            //從本地獲取
            Bitmap bitmap = loadFromLocal(fileName);
            if (bitmap != null) {
                //本地有,顯示
                iv.setImageBitmap(getCircleBitmap(bitmap));
                return;
            }
        }

        //從網絡中獲取
        loadFromNet(iv, url,1,fileName);
    }


    // 正方形
    public void squareDisplay(boolean updateImg,ImageView iv, String url,String fileName) {
        if(url.isEmpty()){
            return;
        }
        fileName = fileName.replaceAll("[\\W]", "");
        if(!fileName.isEmpty()&&!updateImg) {
            //從本地獲取
            Bitmap bitmap = loadFromLocal(fileName);
            if (bitmap != null) {
                //本地有,顯示
                iv.setImageBitmap(getRoundedCornerBitmap(bitmap));
                return;
            }
        }

        //從網絡中獲取
        loadFromNet(iv, url,3,fileName);
    }




    public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, 25.0f, 25.0f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    /**
     *
     * @param iv ImageView
     * @param url Http
     * @param shape photoshape 0:default 1: circle
     */
    private void loadFromNet(ImageView iv, String url,int shape,String fileName) {
        loadFromNet(iv, url, shape, fileName, IMAGE_DEF_SIZE,false);
    }
    private void loadFromNet(ImageView iv, String url,int shape,String fileName, int size,boolean noneedfix) {
        mTags.put(iv, url);//url是ImageView最新的地址
        if (noneedfix) size = 0;
        //耗時操作
        new Thread(new LoadImageTask(iv, url,fileName,shape, size)).start();
    }


    private class LoadImageTask implements Runnable {
        private ImageView iv;
        private String    url;
        private String fileName;
        private int     shape;
        private Bitmap bitmap;
        private int size;


        public LoadImageTask(ImageView iv, String url,String fileName,int shape, int size) {
            this.iv = iv;
            this.url = url;
            this.fileName = fileName;
            this.shape = shape;
            this.size = size;
        }

        @Override
        public void run() {

            try {
                if(url!=null){
                    bitmap = mMemoryCache.get(url);
                }
                if(bitmap!=null)
                    if(bitmap.isRecycled())
                        bitmap = null;

                if(bitmap == null) {
                    if (url != null) {
                        URL imageurl = new URL(url);
                        HttpURLConnection connection = (HttpURLConnection) imageurl.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        int status = connection.getResponseCode();
                        boolean redirect = false;

                        if (status != HttpURLConnection.HTTP_OK) {
                            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                                    || status == HttpURLConnection.HTTP_MOVED_PERM
                                    || status == HttpURLConnection.HTTP_SEE_OTHER)
                                redirect = true;
                        }

                        if (redirect) {

                            // get redirect url from "location" header field
                            String newUrl = connection.getHeaderField("Location");
                            url = newUrl;

                            // open the new connnection again
                            connection = (HttpURLConnection) new URL(url).openConnection();
                            mTags.put(iv, newUrl);


                        }
                        InputStream input = connection.getInputStream();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;

                        BitmapFactory.decodeStream(input, null, options);
                        options.inPurgeable = true;
                        options.inInputShareable = true;
                        if (size !=0) {
                            options.inSampleSize = compressionImage(options.outHeight, options.outWidth, size);
                        }
                        options.inJustDecodeBounds = false;
                        input.close();
                        // 使用 decodeStream 後 不能再次使用同一個 InputStream
                        imageurl = new URL(url);
                        connection = (HttpURLConnection) imageurl.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        input = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(input, null, options);
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(bitmap!=null) {
                    //顯示到UI,當前是子線程,需要使用Handler。其中post方法是執行在主線程的
                    String recentlyUrl = mTags.get(iv);
                    if (url.equals(recentlyUrl)) {

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap bmp = bitmap;
                                    if (shape == 0) {
                                        iv.setImageBitmap(bmp);
                                    } else if (shape == 1) {
                                        bmp = getCircleBitmap(bitmap);
                                        iv.setImageBitmap(bmp);
                                    } else if (shape == 2) {
                                        bmp = changeBitMapSize(bitmap);
                                        iv.setImageBitmap(bmp);
                                    } else if ((shape == 3)) {
                                        bmp = getRoundedCornerBitmap(bitmap);
                                        iv.setImageBitmap(bmp);
                                    }

                                    //存儲到本地
                                    save2Local(bmp, fileName);
                                    mMemoryCache.put(url, bmp);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        }
    }


    //=============================================================================================

    public void save2Local(Bitmap bitmap, String fileName) throws FileNotFoundException {
        File file = getCacheFile(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        try {

            /**
             * 用來壓縮圖片大小
             * Bitmap.CompressFormat format 圖像的壓縮格式；
             * int quality 圖像壓縮率，0-100。 0 壓縮100%，100意味著不壓縮；
             * OutputStream stream 寫入壓縮數據的輸出流；
             * 返回值：如果成功地把壓縮數據寫入輸出流，則返回true。
             */
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, fos);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    /**
     * 從本地獲取圖片
     *
     * @param fileName
     * @return bitmap
     */
    private Bitmap loadFromLocal(String fileName) {
        //本地需要存儲路徑
        File file = getCacheFile(fileName);

        if (file.exists()) {
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            return bitmap;
        }

        return null;
    }

    /**
     * 獲取緩存文件路徑(緩存目錄)
     *
     * @return 緩存的文件
     */
    private File getCacheFile(String fileName) {
        String name = fileName;
        File dir = new File(mContext.getCacheDir()+"/icon");
        if (!dir.exists()) {
            //文件不存在,就創建
            dir.mkdirs();
        }
        return new File(dir, name);

    }



    public Bitmap changeBitMapSize(Bitmap bmp) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        if (height > width) {
            matrix.postScale(4.0f, 4.0f);
        } else {
            matrix.postScale(2.5f, 2.5f);
        }
        Bitmap resizeBmp = null;
        if(!bmp.isRecycled()) {
            resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }

        return resizeBmp;

    }

    public int compressionImage(int height, int width){
        return compressionImage(height, width, IMAGE_DEF_SIZE);
    }

    public int compressionImage(int height, int width, int size){
        int inSampleSize = 1;

        if (height > size || width > size) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > size && (halfWidth / inSampleSize) > size) {
                inSampleSize *= 2;
            }
        }

        if(inSampleSize > 4){
            inSampleSize = 4;
        }

        return inSampleSize;
    }


}
