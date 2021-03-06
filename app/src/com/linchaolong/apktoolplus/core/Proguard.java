package com.linchaolong.apktoolplus.core;

import com.linchaolong.apktoolplus.utils.ClassHelper;
import com.linchaolong.apktoolplus.utils.Cmd;
import com.linchaolong.apktoolplus.utils.Debug;
import com.linchaolong.apktoolplus.utils.ZipHelper;

import java.io.File;

/**
 * Created by linchaolong on 2015/11/16.
 */
public class Proguard {

    public static final String TAG = Proguard.class.getSimpleName();

    private static final String proguardGUIPath = "proguard/proguardgui.jar";
    private static final String proguardPath = "proguard/proguard.jar";
    private static final String retracePath = "proguard/retrace.jar";

    /**
     * 文件完整性校验
     */
    public static void checkCompleted(){
        Debug.d("文件完整性校验");

        //proguard/examples.zip
        File examples = new File(AppManager.getTempDir(), "proguard/examples");
        if(!examples.exists()){
            checkResource("proguard/examples.zip", new Callback<File>() {
                @Override
                public void callback(File file) {
                    if(file.exists()){
                        // 解压examples.zip
                        ZipHelper.unzip(file,examples);
                        file.delete();
                    }
                }
            });
        }

        //proguard/proguard.jar
        checkResource(proguardPath,null);
        //proguard/proguardgui.jar
        checkResource(proguardGUIPath,null);
        //proguard/retrace.jar
        checkResource(retracePath,null);

    }

    /**
     * 把Proguard/src目录下资源原样解压到temp目录下
     *
     * @param resPath
     * @param releasedCallback
     */
    private static void checkResource(String resPath, Callback<File> releasedCallback){
        File proguardJar = new File(AppManager.getTempDir(), resPath);
        if(!proguardJar.exists()){
            Debug.d( resPath+"不存在，正在修复资源...");
            File releaseFile = new File(AppManager.getTempDir(), resPath);
            boolean result = ClassHelper.releaseResourceToFile(resPath, releaseFile);
            if(result){
            }else{
                Debug.e( resPath+"修复失败");
            }
            if(releasedCallback != null){
                releasedCallback.callback(releaseFile);
            }
        }
    }

    public static void proguardGUI(){
        checkCompleted();
        Cmd.exec("java -jar "+new File(AppManager.getTempDir(), proguardGUIPath).getPath());
    }

    public static void proguard(File proFile){
        checkCompleted();
        //java -jar ../lib/proguard.jar @proguard.pro
        Cmd.exec("java -jar "+new File(AppManager.getTempDir(), proguardPath).getPath() + " @"+proFile.getPath());
    }
}
