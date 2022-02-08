package com.sapir.nanittest.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sapir.nanittest.R;
import com.sapir.nanittest.models.BabyUser;
import com.sapir.nanittest.utils.Consts;
import com.sapir.nanittest.viewmodels.MainViewModel;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.sapir.nanittest.utils.Utilities.isStoragePermissionGranted;

public class BirthdayActivity extends AppCompatActivity {

    private View background;
    private ImageButton backButton;
    private ImageButton cameraIcon;
    private LinearLayout shareBtn;
    private ConstraintLayout baseContainer;
    private ImageView ageNumberImage;
    private CircleImageView babyImage;
    private TextView ageInWords;
    private TextView babyName;
    private MainViewModel viewModel;
    private BabyUser babyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        initViews();
        initClickListeners();

        Intent intent = getIntent();
        babyUser = (BabyUser) intent.getSerializableExtra(Consts.BABY_OBJECT);
        updateUserName();
        updateBirthdayYears();
        changeScreenColorRandomly();
        updateBirthdayYears();
        updateBabyImage();
    }


    private void updateUserName() {
        if (babyUser != null && babyUser.getName() != null) {
            babyName.setText(String.format("TODAY %s IS ", babyUser.getName().toUpperCase()));
        }
    }

    private void updateBabyImage() {
        babyImage.setImageURI(Uri.parse(babyUser.getImagePath()));
    }

    private void updateBirthdayYears() {
        TypedArray backgroundImages = getResources().obtainTypedArray(R.array.loading_numbers);
        if (babyUser != null) {
            ageInWords.setText(String.format("%s old ", babyUser.getAgeType()).toUpperCase());
            ageNumberImage.setImageResource(backgroundImages.getResourceId(
                    babyUser.getAge(), R.drawable.number_0));
        }
        backgroundImages.recycle();
    }

    private void changeScreenColorRandomly() {
        TypedArray backgroundImages = getResources().obtainTypedArray(R.array.loading_background_images);
        TypedArray babyImages = getResources().obtainTypedArray(R.array.loading_baby_images);
        TypedArray appBackground = getResources().obtainTypedArray(R.array.loading_background_color);
        TypedArray cameraIcons = getResources().obtainTypedArray(R.array.loading_camera_icon_images);
        TypedArray imageBorderColor = getResources().obtainTypedArray(R.array.loading_border_color);

        int choice = (int) (Math.random() * backgroundImages.length());
        background.setBackgroundResource(backgroundImages.getResourceId(choice, R.drawable.i_os_bg_elephant));
        babyImage.setImageResource(babyImages.getResourceId(choice, R.drawable.baby_image_blue));
        babyImage.setBorderColor(getResources().getColor(imageBorderColor.getResourceId(choice, R.color.blue_image_stroke)));
        baseContainer.setBackgroundResource(appBackground.getResourceId(choice, R.color.blue_app_background));
        cameraIcon.setImageResource(cameraIcons.getResourceId(choice, R.drawable.camera_icon_blue));
        changeStatusBarColor(appBackground, choice);
        backgroundImages.recycle();
        babyImages.recycle();
        cameraIcons.recycle();
        imageBorderColor.recycle();
    }


    private void changeStatusBarColor(TypedArray color, int choice) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(color.getResourceId(
                choice, R.color.blue_app_background)));
    }

    private void initViews() {
        background = findViewById(R.id.birthday_background);
        baseContainer = findViewById(R.id.root_container);
        backButton = findViewById(R.id.back_button);
        cameraIcon = findViewById(R.id.camera_icon_button);
        shareBtn = findViewById(R.id.shareBtn);
        ageNumberImage = findViewById(R.id.age_number_image_view);
        babyImage = findViewById(R.id.baby_image);
        ageInWords = findViewById(R.id.age_in_words);
        babyName = findViewById(R.id.babyName);
    }

    private void initClickListeners() {

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStoragePermissionGranted(BirthdayActivity.this);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BirthdayActivity.super.onBackPressed();
                finish();
            }
        });

        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageResource();
            }
        });
    }

    private void uploadImageResource() {
        viewModel.uploadImageResource(this);
    }

    private void shareScreenLayout() {
        backButton.setVisibility(View.INVISIBLE);
        shareBtn.setVisibility(View.INVISIBLE);
        cameraIcon.setVisibility(View.INVISIBLE);
        Bitmap image = viewModel.getBitmapFromView(baseContainer);
        backButton.setVisibility(View.VISIBLE);
        shareBtn.setVisibility(View.VISIBLE);
        cameraIcon.setVisibility(View.VISIBLE);

        viewModel.openShareDialog(this, image);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        ArrayList<String> permissionsList = new ArrayList<>();

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults == null) {
            shareScreenLayout();
            return;
        }
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == 0) {
                permissionsList.add(permissions[i]);
            }
        }
        if (permissionsList.contains("android.permission.READ_EXTERNAL_STORAGE")) {
            shareScreenLayout();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri uri = data.getData();
            babyImage.setImageURI(uri);
            babyUser.setImagePath(uri.toString());
            viewModel.updateBabyUserInStorage(babyUser);
        }
    }

}