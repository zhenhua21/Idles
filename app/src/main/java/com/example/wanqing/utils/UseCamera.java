package com.example.wanqing.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dahuahua on 2017/4/3.
 */

public class UseCamera {

    public static String mCurrentPhotoPath = null;

    /*
    *   调用相机
    * */
    public static Intent dispatchTakePictureIntent(Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //判断takePictureIntent是否有activity
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null){
            //创建相片存储路径
            File photoFile = createImageFile(context);;

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(
                        context,
                        "com.example.android.fileprovider",
                        photoFile
                );

                //photoUri是相片存储路径，并且存储了相片
                //也可以从onActivityResult中获取缩略图
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                return takePictureIntent;
            }
        }
        return null;
    }

    /*
    * 创建一个存储图片的文件
    * */
    private static File createImageFile(Context context) {
        //create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;

        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!image.exists()) {
            return null;
        }else {
            mCurrentPhotoPath = image.getAbsolutePath();
            return image;
        }
    }

}
