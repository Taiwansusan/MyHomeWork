package com.example.myhomework.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.myhomework.R;

public class LogoView extends RelativeLayout {
    public final static int SHOW_IMAGE = 1;
    public final static int SHOW_TEXT = 2;

    public final static int CIRCLEIMAGE = 1;
    public final static int SQUAREIMAGE = 2;


    private ImageView imageView;
    private TextView textView;
    Context context;

    public LogoView(Context context) {
        this(context, null);
        this.context = context;
    }

    public LogoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public LogoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_logo, this);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        show(SHOW_TEXT);
    }

    /**
     * 設置顯示內容
     * @param text 文字
     * @param imageUrl 圖片網址
     */
    public void setContent(String text, String imageUrl) {
        setText(text);
        setImage(imageUrl);
    }

    /**
     * 設置文字，當沒有圖片時將會顯示第一個字
     * @param text 文字
     */
    public void setText(String text) {
        setText(text, 2);
    }

    public void setText(String text, int textCount) {
        textView.setText(!TextUtils.isEmpty(text) ? text.substring(0, textCount).toUpperCase() : "");
    }

    /**
     * 顯示圖片
     * @param url 圖片網址
     */
    public void setImage(String url) {
        setImage(url, 0);
    }

    @SuppressLint("StaticFieldLeak")
    public void setImage(String url, int imageType) {
        if (!TextUtils.isEmpty(url)) {
            // 使用 Glide 工具來顯示網址圖片
            ImageUtils imageUtils = new ImageUtils(context);
            String fileName;
            fileName = url.replace(":", "").replace("/", "").replace(".", "");
            if (imageType == CIRCLEIMAGE) {
                imageUtils.circleDisplay(false, imageView, url, "LogoView_circle" + fileName);
            } else if (imageType == SQUAREIMAGE){
                imageUtils.squareDisplay(false, imageView, url, "LogoView_square" + fileName);
            } else {
                imageUtils.display(imageView, url, "LogoView_" + fileName);
            }
            show(SHOW_IMAGE);
        } else {
            imageView.setImageResource(imageType == CIRCLEIMAGE
                    ? R.drawable.bg_default_company_logo_circle
                    : R.drawable.alert_background_white);
            show(SHOW_TEXT);
        }
    }




    /**
     * 顯示圖片或者文字
     * @param type 圖片:{@link #SHOW_IMAGE}, 文字: {@link #SHOW_TEXT}
     */
    public void show(int type) {
        if (type == SHOW_IMAGE) {
            imageView.setVisibility(VISIBLE);
            textView.setVisibility(GONE);
        } else {
            imageView.setVisibility(VISIBLE);
            textView.setVisibility(VISIBLE);
        }
    }
}
