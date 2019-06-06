package com.wust.browserbaseuc;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tipInfo;
    private boolean exsitUC=false;
    private String browserName,browserPackage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tipInfo=findViewById(R.id.tv_tip);
        tipInfo.setText("正在加载，请稍候……");
        /*new Thread(new Runnable() {
            @Override
            public void run() {*/
                PackageManager pm=getPackageManager();
                List<PackageInfo> list=pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

                for (PackageInfo packageInfo:list) {
                    String appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                    String packageName = packageInfo.packageName;
            /*   uc浏览器"："com.uc.browser", "com.uc.browser.ActivityUpdate“
                 opera："com.opera.mini.android", "com.opera.mini.android.Browser"
                qq浏览器："com.tencent.mtt", "com.tencent.mtt.MainActivity"*/
                    if (appName.contains("UC浏览器")||packageName.contains("com.UCMobile")) {
                        System.out.println(appName + "\n" + packageName+"\n");
                        browserName=appName;
                        browserPackage=packageName;
                        exsitUC=true;
                        break;
                    }
                }

                if (exsitUC){
                    /*Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://github.com/YouAreOnlyOne");
                    intent.setData(content_url);
                    intent.setClassName("com.UCMobile","com.uc.browser.InnerUCMobile");//打开UC浏览器
//                    intent.setClassName("com.tencent.mtt","com.tencent.mtt.MainActivity");//打开QQ浏览器
                    startActivity(intent);*/

                    /*Uri uri = Uri.parse("https://github.com/YouAreOnlyOne");
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.setClassName("com.UCMobile","com.uc.browser.InnerUCMobile");//打开UC浏览器
//                    intent.setClassName("com.tencent.mtt","com.tencent.mtt.MainActivity");//打开QQ浏览器
                    startActivity(intent);*/

                    openBrowser(MainActivity.this,URLConfig.ServiceURL);

                }else {
                    new AlertDialog.Builder(MainActivity.this).setTitle("系统提示")
                            .setMessage("系统缺少必要的组件，您是否前往下载？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent();
                                    //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                    intent.setAction("android.intent.action.VIEW");
                                    Uri content_url = Uri.parse(URLConfig.BrowserURL);
                                    intent.setData(content_url);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("取消",null).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tipInfo.setText("系统缺少必要的组件，无法正常运行。安装相关的组件之后重新启动即可正常运行！");
                        }
                    });
                }

       /*     }
        }).start();*/

    }




    /**
     *   从手机上搜索已安装浏览器程序打开网页,默认使用系统浏览器。
     *   将 context 替换为当前上下文环境，ActivityClass or  Context
     */
    public void openBrowser(Context context,String url) {
//        String[] browser = {"com.tencent.mtt", "com.UCMobile", "com.uc.browser", "com.oupeng.browser", "com.oupeng.mini.android", "com.android.browser"};
        String[] browser = {"com.UCMobile", "com.uc.browser"};

        Intent intent = null;
        for (String br : browser) {
            if (CheckApkExist.checkApkExist(context, br)) {
                String clsName = null;
                try {
                    PackageManager pm = context.getApplicationContext().getPackageManager();
                    Intent intent1 = pm.getLaunchIntentForPackage(br);
                    ComponentName act = intent1.resolveActivity(pm);
                    clsName = act.getClassName();
                } catch (Exception e) {
                }
                if (clsName == null) {
                    break;
                }
                intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                intent.setClassName(br, clsName);
                break;
            }
        }
        if (intent == null) {
            intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
        }
        context.startActivity(intent);
    }

}
