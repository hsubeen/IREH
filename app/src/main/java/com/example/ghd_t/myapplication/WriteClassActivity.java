package com.example.ghd_t.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteClassActivity extends AppCompatActivity {
    private Spinner spinner_money_min, spinner_money_max;
    private ImageView img1, img2, img3, img4;
    private Uri imgUri;
    private String absolutePath;
    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;
    private static final int CROP_IMAGE = 2;


    private String outFilePath = Environment.getExternalStorageDirectory() + "/tmp.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_class);

        spinner_money_min = findViewById(R.id.spinner_money_min);
        spinner_money_max = findViewById(R.id.spinner_money_max);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);


        //앨범선택, 사진촬영, 취소 다이얼로그 생성
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDialog();
            }
        });
        String[] str = getResources().getStringArray(R.array.spinnerArray_forWrite_money);
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(WriteClassActivity.this,R.layout.spinner_item,str);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_money_min.setAdapter(adapter);
        spinner_money_max.setAdapter(adapter);

    }

    private void makeDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(WriteClassActivity.this,R.style.MyAlertDialogStyle);
        alt_bld.setTitle("사진 업로드").setIcon(R.drawable.check_dialog_64).setCancelable(
                false).setPositiveButton("사진촬영",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 사진 촬영 클릭
                        Log.v("알림", "다이얼로그 > 사진촬영 선택");
                        takePhoto();
                    }
                }).setNeutralButton("앨범선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Log.v("알림", "다이얼로그 > 앨범선택 선택");
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

    //사진 찍기 클릭
    public void takePhoto(){
        // 촬영 후 이미지 가져옴
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시 파일 경로 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        imgUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, FROM_CAMERA);
    }


    //앨범 선택 클릭
    public void selectAlbum(){
        //앨범에서 이미지 가져옴

        //앨범 열기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case FROM_ALBUM : {
                imgUri = data.getData();
                Log.v("알림", "앨범 선택 " + imgUri.getPath().toString());
            }

            case FROM_CAMERA : {
                //이미지를 가져온 후 리사이즈할 이미지 크기 설정
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(imgUri, "image/*");

                //crop 할 이미지 크기 조정
                intent.putExtra("outputX", 1000);    //x축 크기
                intent.putExtra("outputY", 1000);    //y축 크기
                intent.putExtra("aspectX", 1);      //x축 비율
                intent.putExtra("aspectY",1);       //y축 비율
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);

                startActivityForResult(intent, CROP_IMAGE);
                Log.v("알림", "사진촬영 후 CROP");
                break;
            }

            case CROP_IMAGE : {

                //크롭 후 이미지 받는 부분
                if(resultCode != RESULT_OK){
                    return;
                }

                final Bundle extras = data.getExtras();

                //crop한 이미지 저장 위한 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/IREH/" + System.currentTimeMillis() + ".jpg";

                if(extras!=null){

                    //crop된 비트맵

                    Bitmap bitmap = extras.getParcelable("data");

                    //ImageView에 비트맵 설정
                    img1.setImageBitmap(bitmap);

                    Log.v("알림", "ImageView에 로드 완료");

                    //crop된 이미지 갤러리에 저장
                    storeCropImage(bitmap,filePath);
                    absolutePath = filePath;
                    break;
                }

                // 카메라로 촬영한 임시 파일 삭제
                File file = new File(imgUri.getPath());
                if(file.exists()){
                    file.delete();
                }
            }
        }
    }

    //crop한 비트맵 저장
    private void storeCropImage(Bitmap bitmap, String filePath){
        String directory_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IREH";
        File directory_IREH = new File(directory_path);
        if(!directory_IREH.exists()){
            //IREH폴더 없을 시 생성
            directory_IREH.mkdir();
        }

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
