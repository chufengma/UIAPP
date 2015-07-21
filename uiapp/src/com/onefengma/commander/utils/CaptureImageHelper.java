package com.onefengma.commander.utils;


import com.onefengma.commander.config.Constant.RequesetCode;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

public class CaptureImageHelper {

	public static void captureFromCamera(Activity activity) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
			activity.startActivityForResult(takePictureIntent, RequesetCode.CAPTURE_FROM_CAMERA);
		}
	}
	
	public static void captureFromAublm(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
        intent.addCategory(Intent.CATEGORY_OPENABLE);  
        intent.setType("image/*");  
        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), RequesetCode.CAPTURE_FROM_ALBUM);   
	}

}
