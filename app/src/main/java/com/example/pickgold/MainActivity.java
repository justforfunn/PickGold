package com.example.pickgold;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.List;

import static com.example.pickgold.ClickNodeList.HE_WO_XIN_PACKAGE_NAME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static boolean sIsButtonClicked = false;

    private Button mStartHeWoXin;
    private LinearLayout mLinearLayout;
    private FrameLayout mFrameLayout;
    private boolean mFlag = false;
    private static final String SHAKE_GOLD_TIMES="shake_gold_times";
    private static final String SHARE_PREFERENCE_NAME ="settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartHeWoXin = (Button) findViewById(R.id.startHeWoXin);
        mLinearLayout = (LinearLayout) findViewById(R.id.day_record_detail_title);
        mFrameLayout= (FrameLayout) findViewById(R.id.day_record_container);

        mFlag =isServiceEnabled();
        mStartHeWoXin.setOnClickListener(this);
        updateUI();

        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment fragment=fragmentManager.findFragmentById(R.id.day_record_container);
        if (fragment==null){
            fragment=new DayRecordFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.day_record_container,fragment)
                    .commit();
        }

        SharedPreferences sharedPreferences=getSharedPreferences(SHARE_PREFERENCE_NAME,0);
        ClickNodeList.resetQueue(sharedPreferences.getInt(SHAKE_GOLD_TIMES,10));
    }

    private void updateUI() {
        if (DataList.getInstance(this.getApplicationContext()).getDayRecordList().size()<=0){
            mLinearLayout.setVisibility(View.INVISIBLE);
            mFrameLayout.setVisibility(View.INVISIBLE);
        } else {
            mFrameLayout.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.getResource:
                Uri uri = Uri.parse(getString(R.string.code_url));
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                break;
            case R.id.set_shake_gold_times:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("设置换一批的次数");
                final EditText editText=new EditText(this);
                editText.setText(String.valueOf(ClickNodeList.getTimes()));
                builder.setView(editText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            int times=Integer.parseInt(editText.getText().toString());
                            ClickNodeList.resetQueue(times);
                            SharedPreferences sharedPreferences=getSharedPreferences(SHARE_PREFERENCE_NAME,0);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putInt(SHAKE_GOLD_TIMES,times);
                            editor.commit();
                        }catch (Exception e){
                            Toast.makeText(MainActivity.this,"只能输入正整数",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.instructions:
                AlertDialog.Builder builder1=new AlertDialog.Builder(this);
                builder1.setTitle("使用说明");
                builder1.setMessage(R.string.instructions);
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder1.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startHeWoXin:
                if (!mFlag)
                    showOpenAccessibilityServiceDialog();
                else {
                    boolean result=doStartApplicationWithPackageName(HE_WO_XIN_PACKAGE_NAME);
                    Toast.makeText(MainActivity.this,!result?"无法启动和我信":"成功启动和我信",Toast.LENGTH_SHORT).show();
                    if (result){
                        //成功打开
                        sIsButtonClicked=true;
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        mFlag =isServiceEnabled();
    }

    /**
     * 通过包名启动一个app
     * @param packageName String类型的包名
     * @return 是否启动成功
     */
    private boolean doStartApplicationWithPackageName(String packageName) {
        boolean flag=false;
        //通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return flag;
        }
        //创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageInfo.packageName);
        //通过getPackageManager()的queryIntentActivities方法遍历

        List<ResolveInfo> resolveInfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveInfoList.iterator().next();
        if (resolveinfo != null) {
            //packagename = 参数packname
            String packageName1 = resolveinfo.activityInfo.packageName;
            //这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            //LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            //设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName1, className);

            intent.setComponent(cn);
            startActivity(intent);
            flag=true;
        }
        return flag;
    }

    /**
     * 打开服务的对话框
     */
    private void showOpenAccessibilityServiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("需要开启辅助服务");
        builder.setPositiveButton("打开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAccessibilityServiceSettings();
            }
        });
        builder.show();
    }

    /**
     * 打开辅助服务的设置
     */
    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this,getString(R.string.open_service_tips), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 PickGoldAccessibilityService 是否启用状态
     * @return 服务是否被启用
     */
    private boolean isServiceEnabled() {
        //先获取到AccessibilityManager然后根据反馈类型获取到已开启服务的列表
        boolean flag=false;
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            //com.example.pickgold/.PickGoldAccessibilityService
            if (info.getId().equals(getPackageName() + "/.PickGoldAccessibilityService")) {
                flag = true;
            }
        }
        return flag;
    }
}
