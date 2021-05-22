package com.example.YunDays.ui.user;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.example.YunDays.R;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.example.YunDays.userclass.UserClass;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Intent.ACTION_CREATE_DOCUMENT;
import static android.content.Intent.ACTION_OPEN_DOCUMENT;


public class UserFragment extends Fragment implements View.OnClickListener {

    private static final int CHOOSE_PHOTO = 1;
    private View root;
    private ImageView mohuImage;
    private ImageView circleImage;
    private TextView userInformation;
    private TextView feedbacks;
    private TextView settings;
    private final String TAG = "Sqyl";
    private Bitmap bitmap;
    private static String path = "/sdcard/YunDays/";
    private Intent intent_jump;

    private UserClass userClass;
    private TextView text_userName;
    private String mFilePath;

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventSQLiteOperation operation = new EventSQLiteOperation();
        userClass = operation.searchUserClass(getActivity());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_user, container, false);
        }
        //图像处理
        mohuImage = root.findViewById(R.id.userImg_mohu);
        circleImage = root.findViewById(R.id.userImg_circle);

        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");
        if(bt != null) {
            Drawable drawable = new BitmapDrawable(bt);
        }
        //背景模糊实现
        // 参数20 表示模糊背景图片的放大参数 越大背景图片越模糊
        Glide.with(getActivity())
                .load(R.mipmap.ic_user_img_chtholly)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(
                        getActivity(), 10, 1)))
                .into(mohuImage);
        //头像圆形实现
        Glide.with(getActivity())
                .load(R.mipmap.ic_user_img_chtholly)
                .into(circleImage);
        //文本绑定
        text_userName = root.findViewById(R.id.text_userName);
        text_userName.setText(userClass.getUserName());
        text_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FriendActivity.class));
            }
        });
        userInformation = root.findViewById(R.id.text_userInformation);
        feedbacks = root.findViewById(R.id.text_feedback);
        settings = root.findViewById(R.id.text_settings);

        //监听器添加
        circleImage.setOnClickListener(this);
        userInformation.setOnClickListener(this);
        feedbacks.setOnClickListener(this);
        settings.setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userImg_circle:
                setDialog();
                break;
                //选择照片按钮
                case R.id.btn_choose_img:
                    //动态申请权限
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        // 调用系统相册
                        choseHeadImageFromGallery();
                        //Toast.makeText(getActivity(), "功能开发中", Toast.LENGTH_SHORT).show();
                    }
                    break;
                //拍照按钮
                case R.id.btn_open_camera:
                    choseHeadImageFromCameraCapture();
                    break;

            case R.id.text_userInformation:
                intent_jump = new Intent(getActivity(), UserInformationActivity.class);
                startActivity(intent_jump);
                break;
            case R.id.text_feedback:
                intent_jump = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent_jump);
                break;
            case R.id.text_settings:
                intent_jump = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent_jump);
                break;
        }
    }
    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Log.i(TAG, "choseHeadImageFromGallery: photo1");
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }
    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Log.i(TAG, "choseHeadImageFromCameraCapture: camera1");
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory(), "Pictures");
        if(!fileDir.exists()) {
            fileDir.getParentFile().mkdir();
        }
        mFilePath = fileDir.getAbsolutePath() + "/IMG_" + System.currentTimeMillis() + ".jpg";
        Uri uri;
        uri = Uri.fromFile(new File(mFilePath));
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    private void setDialog() {
        Dialog mCameraDialog = new Dialog(getActivity(), R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                R.layout.userimage_bottom_dialog, null);
        //初始化视图
        root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
        root.findViewById(R.id.btn_open_camera).setOnClickListener(this);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i(TAG, "onActivityResult: result");
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), "取消", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                Log.i(TAG, "onActivityResult: photo into crop");
                cropRawPhoto(data.getData());
                break;
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    Log.i(TAG, "onActivityResult: camera into crop");
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getActivity(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case CODE_RESULT_REQUEST:
                if (data != null) {
                    Log.i(TAG, "onActivityResult: show 1");
                    setImageToHeadView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {
        Log.i(TAG, "cropRawPhoto: crop in");
        Intent intent_url = new Intent(ACTION_OPEN_DOCUMENT);
        intent_url.setDataAndType(uri, "image/*");
        intent_url.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent_url.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        // 设置裁剪
        intent_url.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent_url.putExtra("aspectX", 1);
        intent_url.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent_url.putExtra("outputX", output_X);
        intent_url.putExtra("outputY", output_Y);
        intent_url.putExtra("return-data", true);

        startActivityForResult(intent_url, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        //extras 没进来
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            //圆形头像实现
//            Glide.with(getActivity())
//                    .load(photo)
//                    .into(circleImage);
            circleImage.setImageBitmap(photo);
            Log.i(TAG, "setImageToHeadView: set finish");
            //背景模糊实现
            // 参数20 表示模糊背景图片的放大参数 越大背景图片越模糊
            Glide.with(getActivity())
                    .load(photo)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(
                            getActivity(), 10, 1)))
                    .into(mohuImage);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    // 用户允许授权
                    /* ————调用系统相册————*/
                    choseHeadImageFromGallery();
                    Toast.makeText(getActivity(), "功能开发中", Toast.LENGTH_SHORT).show();
                } else {
                    // 用户拒绝授权
                    Toast.makeText(getActivity(), "请同意授权后再进行操作！",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
    }

}