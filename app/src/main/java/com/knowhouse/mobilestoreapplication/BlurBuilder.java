package com.knowhouse.mobilestoreapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class BlurBuilder {
    private static final float BITMAP_SCALE = 1.0f;
    private static final float BLUR_RADIUS = 10.0f;

    public static Bitmap blur(Context context, Bitmap bitmap){
        int width = Math.round(bitmap.getWidth() * BITMAP_SCALE);
        int height = Math.round(bitmap.getHeight()* BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(bitmap,width,height,false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);


        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs,inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs,outputBitmap);
        scriptIntrinsicBlur.setRadius(BLUR_RADIUS);
        scriptIntrinsicBlur.setInput(tmpIn);
        scriptIntrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }
}
