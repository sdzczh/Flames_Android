package app.com.pgy.Activitys.Base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.TextView;

import com.oginotihiro.cropview.CropUtil;
import com.oginotihiro.cropview.CropView;

import java.io.File;

import app.com.pgy.R;
import app.com.pgy.Utils.FileUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.TitleView;

/**
 * Created by YX on 2018/5/4.
 */

public class CropImgActivity extends BaseActivity implements View.OnClickListener {

    TitleView titleView;
    CropView civ_img;
    TextView tv_ok,tv_cancel;
    Uri mUri;

    @Override
    public int getContentViewId() {
        return R.layout.activity_base_crop_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView = findViewById(R.id.activity_crop_title);
        civ_img = findViewById(R.id.civ_cropview);
        tv_ok = findViewById(R.id.tv_activity_crop_ok);
        tv_cancel = findViewById(R.id.tv_activity_crop_cancel);

        titleView.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUri = getIntent().getData();
        initCropView();
        tv_ok.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    private void initCropView(){
        if(mUri != null){
            civ_img.of(mUri).asSquare().withOutputSize(150,150).initialize(CropImgActivity.this);
        }else {
            showToast("图片数据为空！请重试");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_activity_crop_ok:
                saveCropImg();
                break;
            case R.id.tv_activity_crop_cancel:
                finish();
                break;
            default:
                break;
        }
    }
    private void saveCropImg(){
        Bitmap bitmap = civ_img.getOutput();
        if (bitmap == null){
            showToast("获取裁剪图片失败");
            return;
        }
        String cache = FileUtils.getLocalPath()+"pgy" + TimeUtils.timeStampStr()+".png";
        File cropFile = new File(cache);
        if (!cropFile.exists()) {
            cropFile.delete();
        }
        Uri destination;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            destination =  FileProvider.getUriForFile(mContext, "app.com.pgy.fileprovider",cropFile);
        } else{
            destination = Uri.fromFile(cropFile);
        }
        if(CropUtil.saveOutput(CropImgActivity.this, destination, bitmap, 100)){
            setResult(Activity.RESULT_OK,getIntent().putExtra("file",cache));
        }else{
            showToast("修改裁剪失败");
        }
        finish();
    }
}
