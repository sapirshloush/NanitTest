package com.sapir.nanittest.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.sapir.nanittest.R;
import com.sapir.nanittest.models.BabyUser;
import com.sapir.nanittest.utils.Consts;
import com.sapir.nanittest.utils.Utilities;
import com.sapir.nanittest.viewmodels.MainViewModel;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private Button showBirthdayScreen;
    private Button chooseImage;
    private EditText babyName;
    private DatePicker datePicker;
    private ImageView babyImage;
    private Uri imagePath;
    private BabyUser babyUser;
    private MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initViews();
        initClickListeners();
        observeData();
    }


    private void observeData() {
        babyUser = viewModel.getBabyUserInStorage();
        if (babyUser != null) {
            updateUI();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        observeData();
    }


    private void updateUI() {
        Date date;
        babyName.setText(babyUser.getName());
        if (babyUser.getImagePath() != null && !babyUser.getImagePath().isEmpty()) {
            babyImage.setImageURI(Uri.parse(babyUser.getImagePath()));
        }
        date = babyUser.getBirthday();
        datePicker.updateDate(Utilities.getYear(date),
                Utilities.getMonth(date), Utilities.getDay(date));
    }


    @Override
    protected void onResume() {
        super.onResume();
        observeData();
    }

    private void initViews() {

        showBirthdayScreen = findViewById(R.id.showBirthDayScreen);
        babyName = findViewById(R.id.userName);
        datePicker = findViewById(R.id.birthDayPicker);
        datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePicker.setMinDate(Utilities.limitTimePicker()); //Limit the range of the data picker
        babyImage = findViewById(R.id.userImage);
        chooseImage = findViewById(R.id.choose_image);
    }

    private void initClickListeners() {
        showBirthdayScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayBirthdayActivity();
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImageFromResource();
            }
        });

        babyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!babyName.getText().toString().isEmpty()) {
                    showBirthdayScreen.setVisibility(View.VISIBLE);
                } else {
                    showBirthdayScreen.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateImageFromResource() {
        ImagePicker.Companion.with(this).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            imagePath = data.getData();
            babyImage.setImageURI(imagePath);
            if (babyUser != null) {
                babyUser.setImagePath(imagePath.toString());
                viewModel.updateBabyUserInStorage(babyUser);
            }
        }

    }

    private void displayBirthdayActivity() {
        Intent intent = new Intent(getApplicationContext(),
                BirthdayActivity.class);
        setBabyUser();
        intent.putExtra(Consts.BABY_OBJECT, babyUser);
        startActivity(intent);
    }

    public void setBabyUser() {
        String name = babyName.getText().toString();
        int babyAge;
        String image = null;
        Date date = Utilities.getDate(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth());
        Period age = viewModel.getBabyAge(date);

        if (age.getPeriodType().getName().equals(PeriodType.months().getName())) {
            babyAge = age.getMonths();
        } else {
            babyAge = age.getYears();
        }
        if (imagePath != null) {
            image = imagePath.toString();
        } else if (babyUser != null && babyUser.getImagePath() != null) {
            image = babyUser.getImagePath();
        }

        babyUser = new BabyUser(name, image, babyAge,
                age.getPeriodType().getName(), date);
        viewModel.updateBabyUserInStorage(babyUser);
    }

}