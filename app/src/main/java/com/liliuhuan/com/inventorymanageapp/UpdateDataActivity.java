package com.liliuhuan.com.inventorymanageapp;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.liliuhuan.com.inventorymanageapp.bean.MainBean;
import com.liliuhuan.com.inventorymanageapp.db.RealmHelper;
import com.liliuhuan.com.inventorymanageapp.evnet.CloseEvent;
import com.liliuhuan.com.inventorymanageapp.evnet.RefreshMainEvent;
import com.liliuhuan.com.inventorymanageapp.util.ImageLoader;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.reactivex.functions.Consumer;

public class UpdateDataActivity extends AppCompatActivity {
    String mCurrentPhotoPath = null;
    @Bind(R.id.image_)
    ImageView image;
    @Bind(R.id.et_name_)
    EditText etName;
    @Bind(R.id.btn_save)
    Button btnSave;
    private MainBean bean;
    private int form = 0;//0添加1修改
    private RealmHelper realmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        realmHelper = RealmHelper.getInstance();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            form = extras.getInt("from");
            bean = (MainBean) extras.getSerializable("bean");
            if (bean != null) {
                ImageLoader.loadUriImage(this,bean.imagePath,image);
                etName.setText(bean.name);
            }
        }


    }
    public void onEventMainThread(CloseEvent event) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }


    static final int REQUEST_TAKE_PHOTO = 2;

    private void dispatchFullTakePictureIntent() {//全尺寸
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            // Continue only if the File was successfully created

            if (photoFile != null) {
                Uri photoURI = null;
                if (Build.VERSION.SDK_INT >= 24) {
                    photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.liliuhuan.com.inventorymanageapp.fileprovider", photoFile);
                } else {
                    photoURI = Uri.fromFile(photoFile);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);//私有的外部不能访问，相册中不会显示
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            ImageLoader.loadUriImage(this,mCurrentPhotoPath,image);
        }
    }

    @OnClick({R.id.btn_save, R.id.image_})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                String strName = etName.getText().toString();
                if (form == 0){
                    if (TextUtils.isEmpty(strName) || TextUtils.isEmpty(mCurrentPhotoPath)){
                        Toast.makeText(this,"填写名称及图片拍照！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<MainBean> mainTagsList = realmHelper.getMainTagsList();
                    MainBean bean;
                    if (mainTagsList == null){
                        bean = new MainBean(0,mCurrentPhotoPath,strName);
                    }else {
                        bean = new MainBean(mainTagsList.size(),mCurrentPhotoPath,strName);
                    }
                    realmHelper.insertMainTag(bean);
                }else {
                    if (TextUtils.isEmpty(mCurrentPhotoPath)){
                        realmHelper.updateMainName(bean.id,strName);
                    }else {
                        realmHelper.updateMainNameImagePath(bean.id,strName,mCurrentPhotoPath);
                    }
                }
                EventBus.getDefault().post(new RefreshMainEvent());
                finish();
                break;
            case R.id.image_:
                new RxPermissions(this)
                        .requestEach(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted){
                            dispatchFullTakePictureIntent();
                        }
                    }
                });
                break;
        }
    }
}
