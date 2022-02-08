package com.sapir.nanittest.viewmodels;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import androidx.lifecycle.ViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.sapir.nanittest.repositories.UserBabyRepository;
import com.sapir.nanittest.models.BabyUser;
import com.sapir.nanittest.utils.AgeCalculator;
import com.sapir.nanittest.utils.Utilities;
import org.joda.time.Period;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Objects;

public class MainViewModel extends ViewModel {

    private static final String MEDIA_PATH = "Pictures/Screen Layouts/";
    private static final String MIME_TYPE = "image/jpeg";


    @SuppressLint("SimpleDateFormat")
    public Period getBabyAge(Date date) {
        return AgeCalculator.getBabyAge(Utilities.getYear(date),
                Utilities.getMonth(date), Utilities.getDay(date));
    }

    public void uploadImageResource(Activity activity) {
        ImagePicker.Companion.with(activity)
                .start();
    }


    public void openShareDialog(Activity activity, Bitmap image) {
        Uri imageUri = saveImage(image, activity);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType("*/*");
        activity.startActivity(intent);
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap returnBitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);
        return returnBitmap;
    }

    protected Uri saveImage(Bitmap finalBitmap, Activity activity) {
        OutputStream outputStream ;
        Uri imageUri;
        ContentResolver resolver = activity.getContentResolver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "screenLayout" +".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,MIME_TYPE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, MEDIA_PATH);
        }

        try {
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
            outputStream =  resolver.openOutputStream(Objects.requireNonNull(imageUri) );
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return imageUri;
    }

    public void updateBabyUserInStorage(BabyUser babyUser) {
        UserBabyRepository.getInstance().updateBabyUserInStorage(babyUser);
    }

    public BabyUser getBabyUserInStorage() {
        return UserBabyRepository.getInstance().getBabyUserFromStorage();
    }
}
