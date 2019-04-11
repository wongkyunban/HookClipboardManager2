package com.wong.hookclipboardmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 * @author WongKyunban
 * description
 * created at 2019-04-11 下午4:51
 * @version 1.0
 */
public class HookHelper {
    public static void hook(final Context context){
        try {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

            Field mService = ClipboardManager.class.getDeclaredField("mService");
            mService.setAccessible(true);

            // 第一步：得到系统的 mService
            final Object originService = mService.get(clipboardManager);


            Class<?> iClipboard = Class.forName("android.content.IClipboard");

            Object myServiceProxy = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{iClipboard}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    String methodName = method.getName();


                    if("setPrimaryClip".equals(methodName)){

                        ClipData clipData = (ClipData) args[0];
                        String str = (String) clipData.getItemAt(0).getText()+"哈哈哈我劫持你了！";


                        Field mItems = ClipData.class.getDeclaredField("mItems");
                        mItems.setAccessible(true);
                        ArrayList<ClipData.Item> items = (ArrayList<ClipData.Item>)mItems.get(clipData);
                        items.remove(0);
                        ClipData.Item item = new ClipData.Item(str);
                        items.add(item);

                    }
                    return method.invoke(originService,args);
                }
            });

            // 第三步：偷梁换柱，使用 myServiceProxy 替换系统的 mService
            mService.set(clipboardManager,myServiceProxy);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
