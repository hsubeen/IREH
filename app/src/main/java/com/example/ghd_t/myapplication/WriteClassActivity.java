package com.example.ghd_t.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.Manifest;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WriteClassActivity extends AppCompatActivity {
    private Spinner spinner_money_min, spinner_money_max;
    private ImageView img1, img2, img3, img4;
    private Uri imgUri, photoURI, downloadUrl1, downloadUrl2, downloadUrl3,downloadUrl4;
    private String mCurrentPhotoPath;
    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;
    private Button btn_write_class;
    private EditText write_class_title, write_class_content, write_class_person;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private int containerImageView = -1;
    private int[] flag = {-1,-1,-1,-1,-1};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_class);


        //TedPermission 라이브러리 -> 카메라 권한 획득
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(WriteClassActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(WriteClassActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

        //FOR Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();


        spinner_money_min = findViewById(R.id.spinner_money_min);
        spinner_money_max = findViewById(R.id.spinner_money_max);

        write_class_person = findViewById(R.id.write_class_person);
        write_class_title = findViewById(R.id.write_class_title);
        write_class_content = findViewById(R.id.write_class_content);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);

        btn_write_class = findViewById(R.id.btn_write_class);
        //작성 완료 버튼 클릭
        btn_write_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //업로드 하시겠습니까? 다이얼로그 생성
                makeConfirmDialog();
            }
        });

        //앨범선택, 사진촬영, 취소 다이얼로그 생성
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               containerImageView = 1;
               makeDialog();
            }
        });

        //앨범선택, 사진촬영, 취소 다이얼로그 생성
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containerImageView = 2;
                makeDialog();
            }
        });

        //앨범선택, 사진촬영, 취소 다이얼로그 생성
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containerImageView = 3;
                makeDialog();
            }
        });

        //앨범선택, 사진촬영, 취소 다이얼로그 생성
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containerImageView = 4;
                makeDialog();
            }
        });

        String[] str = getResources().getStringArray(R.array.spinnerArray_forWrite_money);
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(WriteClassActivity.this,R.layout.spinner_item,str){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), "font/nanumsquarel.ttf");
                ((TextView) v).setTypeface(externalFont);

                return v;
            }
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "font/nanumsquarel.ttf");
                ((TextView) v).setTypeface(externalFont);
                v.setBackgroundColor(Color.WHITE);
                ((TextView) v).setTextColor(Color.BLACK);

                return v;
            }
        };
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_money_min.setAdapter(adapter);
        spinner_money_max.setAdapter(adapter);
    }

    private void makeDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(WriteClassActivity.this,R.style.MyAlertDialogStyle);
        alt_bld.setTitle("사진 업로드").setIcon(R.drawable.check_dialog_64).setCancelable(
                true).setPositiveButton("사진촬영",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 사진 촬영 클릭
                        Log.v("알림", "다이얼로그 > 사진촬영 선택");

                        switch (containerImageView){
                            case 1:
                                flag[1] = 0;
                                break;
                            case 2:
                                flag[2] = 0;
                                break;
                            case 3:
                                flag[3] = 0;
                                break;
                            case 4:
                                flag[4] = 0;
                                break;
                        }
                        takePhoto();
                    }
                }).setNeutralButton("앨범선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Log.v("알림", "다이얼로그 > 앨범선택 선택");
                        switch (containerImageView){
                            case 1:
                                flag[1] = 1;
                                break;
                            case 2:
                                flag[2] = 1;
                                break;
                            case 3:
                                flag[3] = 1;
                                break;
                            case 4:
                                flag[4] = 1;
                                break;
                        }
                        //앨범에서 선택
                        selectAlbum();
                }
                }).setNegativeButton("취소   ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("알림", "다이얼로그 > 취소 선택");
                        // 취소 클릭. dialog 닫기.
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }


    //모집 글 작성 중 뒤로가기 버튼 눌렸을 때
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(WriteClassActivity.this,R.style.MyAlertDialogStyle);
                alt_bld.setTitle("글을 작성중입니다.").setMessage("작성을 중지하시겠습니까?").setIcon(R.drawable.check_dialog_64).setCancelable(
                        false).setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //현재 액티비티 종료
                                finish();
                            }
                        }).setNegativeButton("아니오", null);
                AlertDialog alert = alt_bld.create();
                alert.show();
        }
        return true;
    }

    //사진 찍기 클릭
    public void takePhoto(){
        // 촬영 후 이미지 가져옴
        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null){
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(photoFile!=null){
                    Uri providerURI = FileProvider.getUriForFile(this,getPackageName(),photoFile);
                    imgUri = providerURI;
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(intent, FROM_CAMERA);
                }
            }
        }else{
            Log.v("알림", "저장공간에 접근 불가능");
            return;
        }

    }

    //사진 새로운 이미지로 만들기
    public File createImageFile() throws IOException{
        String imgFileName = System.currentTimeMillis() + ".jpg";
        File imageFile= null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "ireh");

        if(!storageDir.exists()){
            //없으면 만들기
            Log.v("알림","storageDir 존재 x " + storageDir.toString());
            storageDir.mkdirs();
        }
        Log.v("알림","storageDir 존재함 " + storageDir.toString());
        imageFile = new File(storageDir,imgFileName);

        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    //앨범 선택 클릭
    public void selectAlbum(){
        //앨범에서 이미지 가져옴

        //앨범 열기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, FROM_ALBUM);
    }

    //촬영한 사진 설정한 경로에 저장하기
    public void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File  f = null;
        Uri contentUri;

        f = new File(mCurrentPhotoPath);
        contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this,"사진이 갤러리에 저장되었습니다",Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //DB업로드 절차
        final String cu = mAuth.getUid();
        String filename = cu + "_" + System.currentTimeMillis();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://ireh-950523.appspot.com/").child("WriteClassImage/" + filename);
        UploadTask uploadTask;
        Uri file;

        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case FROM_ALBUM : {
                //앨범에서 가져오기
                if(data.getData()!=null){
                    try{
                        Bitmap bitmap;
                        //받은 URI를 photoURI에 저장 한 후, Bitmap을 받아 각 이미지뷰에 설정.
                        photoURI = data.getData();

                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);

                        file = photoURI;
                        uploadTask = storageRef.putFile(file);

                        //이미지뷰에 따라 처리
                        switch (containerImageView){
                            case 1:
                                img1.setImageBitmap(bitmap);
                                // Register observers to listen for when the download is done or if it fails
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle unsuccessful uploads
                                        Log.v("알림", "사진 업로드 실패1");
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                        downloadUrl1 = taskSnapshot.getDownloadUrl();
                                        Log.v("알림", "사진 업로드 성공1 " + downloadUrl1);
                                    }
                                });
                                break;
                            case 2:
                                img2.setImageBitmap(bitmap);
                                // Register observers to listen for when the download is done or if it fails
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle unsuccessful uploads
                                        Log.v("알림", "사진 업로드 실패2");
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                        downloadUrl2 = taskSnapshot.getDownloadUrl();
                                        Log.v("알림", "사진 업로드 성공2 " + downloadUrl2);
                                    }
                                });
                                break;
                            case 3:
                                img3.setImageBitmap(bitmap);
                                // Register observers to listen for when the download is done or if it fails
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle unsuccessful uploads
                                        Log.v("알림", "사진 업로드 실패3");
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                        downloadUrl3 = taskSnapshot.getDownloadUrl();
                                        Log.v("알림", "사진 업로드 성공3 " + downloadUrl3);
                                    }
                                });
                                break;
                            case 4:
                                img4.setImageBitmap(bitmap);
                                // Register observers to listen for when the download is done or if it fails
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle unsuccessful uploads
                                        Log.v("알림", "사진 업로드 실패4");
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                        downloadUrl4 = taskSnapshot.getDownloadUrl();
                                        Log.v("알림", "사진 업로드 성공4 " + downloadUrl4);
                                    }
                                });
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.v("알림","앨범에서 가져오기 에러");
                    }
                }
                break;
            }

            case FROM_CAMERA : {
                //카메라 촬영
                try{
                    Log.v("알림", "FROM_CAMERA 처리");
                    //촬영한 사진 갤러리에 저장
                    galleryAddPic();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),  imgUri);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);

                    options.inSampleSize = 4;
                    file = Uri.fromFile(new File(mCurrentPhotoPath));
                    uploadTask = storageRef.putFile(file);

                    //이미지뷰에 따라 처리
                    switch (containerImageView){
                        case 1:
                            //img1.setImageURI(imgUri);
                            img1.setImageBitmap(bitmap);
                            // Register observers to listen for when the download is done or if it fails
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle unsuccessful uploads
                                    Log.v("알림", "사진 업로드 실패1");
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                    downloadUrl1 = taskSnapshot.getDownloadUrl();
                                    Log.v("알림", "사진 업로드 성공1 " + downloadUrl1);
                                }
                            });
                            break;
                        case 2:
                            //img2.setImageURI(imgUri);
                            img2.setImageBitmap(bitmap);
                            // Register observers to listen for when the download is done or if it fails
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle unsuccessful uploads
                                    Log.v("알림", "사진 업로드 실패2");
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                    downloadUrl2 = taskSnapshot.getDownloadUrl();
                                    Log.v("알림", "사진 업로드 성공2 " + downloadUrl2);
                                }
                            });
                            break;
                        case 3:
                            //img3.setImageURI(imgUri);
                            img3.setImageBitmap(bitmap);
                            // Register observers to listen for when the download is done or if it fails
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle unsuccessful uploads
                                    Log.v("알림", "사진 업로드 실패3");
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                    downloadUrl3 = taskSnapshot.getDownloadUrl();
                                    Log.v("알림", "사진 업로드 성공3 " + downloadUrl3);
                                }
                            });
                            break;
                        case 4:
                            //img4.setImageURI(imgUri);
                            img4.setImageBitmap(bitmap);
                            // Register observers to listen for when the download is done or if it fails
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle unsuccessful uploads
                                    Log.v("알림", "사진 업로드 실패4");
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                    downloadUrl4 = taskSnapshot.getDownloadUrl();
                                    Log.v("알림", "사진 업로드 성공4 " + downloadUrl4);
                                }
                            });
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    //입력한 모든 정보를 한번에 DB로 전송 -> 모집 글 작성 완료
    public void sendDB(){
        final String cu = mAuth.getUid();

        long ct = System.currentTimeMillis();
        //현재시간
        String ct_str = Long.toString(ct);

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(WriteClassActivity.this, R.style.MyAlertDialogStyle);
        alt_bld.setTitle("작성 완료").setIcon(R.drawable.check_dialog_64).setMessage("글을 게시했습니다.").setCancelable(
                false);
        final AlertDialog alert = alt_bld.create();
        alert.show();
        if(downloadUrl1 != null && downloadUrl2 != null && downloadUrl3 != null && downloadUrl4 != null){

            WriteClassData writeClassData = new WriteClassData(write_class_title.getText().toString(), write_class_content.getText().toString(),
            write_class_person.getText().toString(), spinner_money_min.getSelectedItem().toString(), spinner_money_max.getSelectedItem().toString(),
            downloadUrl1.toString(),downloadUrl2.toString(),downloadUrl3.toString(),downloadUrl4.toString(), cu);

            mDatabase.child("WriteClass").child(ct_str).setValue(writeClassData);
            Log.v("알림", "작성 내용 데이터베이스 저장 성공 ");


            new Handler().postDelayed(new Runnable()
            {
                @Override public void run()
                {
                    // DB등록 성공 1.5초 후 MainActivity로 전환
                    alert.dismiss();
                    finish();
                }
            }, 3000);

        }else{
            if(flag[1]==-1 || flag[2] ==-1 || flag[3]==-1 || flag[4] == -1){
                Toast.makeText(WriteClassActivity.this, "모든 사진을 업로드해주세요.", Toast.LENGTH_LONG).show();
                alert.dismiss();
            }else{
                Toast.makeText(WriteClassActivity.this, "서버에 사진을 업로드중입니다. \n잠시 후 시도해주세요.", Toast.LENGTH_LONG).show();
                alert.dismiss();
            }

        }
    }

    public void makeConfirmDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(WriteClassActivity.this, R.style.MyAlertDialogStyle);
        alt_bld.setTitle("작성 완료").setIcon(R.drawable.check_dialog_64).setMessage("글을 게시하시겠습니까?").setCancelable(
                false).setPositiveButton("네",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 네 클릭
                        if(write_class_title.getText().length()==0
                                || write_class_content.getText().length() == 0
                                || write_class_person.getText().length() == 0){
                            Toast.makeText(WriteClassActivity.this,"모든 정보를 입력해주세요", Toast.LENGTH_LONG).show();
                        }else {
                            //서버에 전송
                                sendDB();

                        }
                    }
                }).setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 아니오 클릭. dialog 닫기.
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }


}
