package com.example.yanxu.newyongyou.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxu.newyongyou.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonUtils {
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    /**
     * 网络请求
     *
     * @param context
     * @return
     */

    public static boolean isNetworkAvailable(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity == null) {
                Log.w("Utility", "couldn't get connectivity manager");
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].isAvailable()) {
                            Log.d("Utility", "network is available");
                            return true;
                        }
                    }
                }
            }
        }
        Log.d("Utility", "network is not available");
        return false;
    }

    /**
     * 从Assets中读取图片
     */
    public static Bitmap getImageFromAssetsFile(String fileName, Context context) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    /**
     * 显示Toast
     *
     * @param context
     * @param tvString
     */
    public static void ToastShow(Context context, String tvString) {//获取LayoutInflater对象，该对象能把XML文件转换为与之一直的View对象

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_toast, null);
        TextView chapterNameTV = (TextView) view.findViewById(R.id.tv_toast);
        chapterNameTV.setText(tvString);
        Toast toast = new Toast(context);

//        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

//    public static void postRequest(String url, AQuery aq,
//                                   AjaxCallback<JSONObject> cb, List<NameValuePair> pairs) {
//        String sign = MsgDigestUtil.getDigestParam(pairs);
//        // make digest sign str
//        pairs.add(new BasicNameValuePair("sign", sign));
//        HttpEntity entity = null;
//        try {
//            entity = new UrlEncodedFormEntity(pairs, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Map<String, Object> params = new HashMap<String, Object>();
//
//        params.put(Constants.POST_ENTITY, entity);
//        // execute request
////		aq.progress(new AQProgressDialog(aq.getContext())).ajax(url, params, JSONObject.class,
////				cb);
//        aq.progress(aq.getContext()).ajax(url, params, JSONObject.class,
//                cb);
//    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

    /**
     * 提现金额
     *
     * @param pwd
     * @return
     */
    public static boolean isCorrectMoney(String pwd) {
        Pattern p = Pattern.compile("^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$");
        Matcher m = p.matcher(pwd);
        System.out.println(m.matches() + "-pwd-");
        return m.matches();
    }

    /**
     * 密码匹配，以字母开头，长度 在6-18之间，只能包含字符、数字和下划线。
     *
     * @param pwd
     * @return
     */
    public static boolean isCorrectUserPwd(String pwd) {
//        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$");
        Pattern p = Pattern.compile("^[0-9A-Za-z]{6,20}$");
        Matcher m = p.matcher(pwd);
        System.out.println(m.matches() + "-pwd-");
        return m.matches();
    }

    /**
     * 手机号码格式匹配
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "-telnum-");
        return m.matches();
    }

    /**
     * 手机号加密
     *
     * @param phoneNumber
     * @return
     */
    private static String getProtectedMobile(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(phoneNumber.subSequence(0, 3));
        builder.append("****");
        builder.append(phoneNumber.subSequence(7, 11));
        return builder.toString();
    }

    /**
     * 验证身份证位数,15位和18位身份证
     *
     * @param code
     * @return
     */

    public static boolean verifyLength(String code) {
        int length = code.length();
        if (length == 15 || length == 18) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 防止重复点击
     *
     * @return
     */
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * <p>
     * 将文件转成base64 字符串
     * </p>
     *
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        file.delete();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    public static void saveMyBitmap(Bitmap mBitmap) throws IOException {
        File f = new File("/sdcard/avatar.png");
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        System.out.print("保存了头像");
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String saveBitmap(Bitmap photo) {
        Log.e("liub", "保存图片");
        File f = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_PICTURES, System.currentTimeMillis() + ".png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            photo.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i("liub", "已经保存");
            return f.getAbsolutePath();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

    public static String saveAvatar(Bitmap photo) {
        Log.e("liub", "保存图片");
        File avatar = new File("/sdcard/avatar.png");
        if (avatar.exists()) {
            avatar.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(avatar);
            photo.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i("liub", "头像保存");
            return avatar.getAbsolutePath();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return avatar.getAbsolutePath();
    }

    /**
     * Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap to Drawable
     *
     * @param bitmap
     * @param mcontext
     * @return
     */
    public static Drawable bitmapToDrawble(Bitmap bitmap, Context mcontext) {
        Drawable drawable = new BitmapDrawable(mcontext.getResources(), bitmap);
        return drawable;
    }

//    public static void postRequest4Array(String url, AQuery aq,
//                                         AjaxCallback<JSONArray> cb, List<NameValuePair> pairs) {
//        String sign = MsgDigestUtil.getDigestParam(pairs);
//        // make digest sign str
//        pairs.add(new BasicNameValuePair("sign", sign));
//        HttpEntity entity = null;
//        try {
//            entity = new UrlEncodedFormEntity(pairs, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Map<String, Object> params = new HashMap<String, Object>();
//
//        params.put(Constants.POST_ENTITY, entity);
//        aq.progress(aq.getContext()).ajax(url, params, JSONArray.class, cb);
//        // execute request
////		aq.progress(new AQProgressDialog(aq.getContext())).ajax(url, params, JSONArray.class,
////				cb);
//    }

    public static boolean validateETEmpty(Context con, List<View> list) {
        for (View v : list) {
            EditText et = (EditText) v;
            if (et.getText().length() <= 0) {
                Toast.makeText(con, "[" + et.getHint() + "]", Toast.LENGTH_LONG)
                        .show();
                return false;
            }
            if (et.getTag() != null && "phone".equals(et.getTag().toString())
                    && et.getText().length() != 11) {
                Toast.makeText(con, "[" + "请输入正确格式的手机号码" + "]",
                        Toast.LENGTH_LONG).show();
                return false;
            }

        }
        return true;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Drawable setButtonDrawableRight(Context context, int resid) {
        Drawable d;
        Resources res = context.getResources();
        d = res.getDrawable(resid);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        return d;
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public static Intent startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 240);
        intent.putExtra("outputY", 240);
        intent.putExtra("return-data", true);
        return intent;
    }

    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".png";

    }

    public static String getURLStringParams(String url,
                                            HashMap<String, String> params) {

        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        for (String key : params.keySet()) {
            sb.append(key);
            sb.append("=");
            sb.append(params.get(key));
            sb.append("&");
        }
        if ('&' == sb.charAt(sb.length() - 1)) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();

    }

    public static String getJsonString(JSONObject obj, String key) {
        String res = "";
        try {
            if (!obj.isNull(key))
                res = obj.getString(key);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return res;
    }

    public static JSONObject getJsonObject(JSONObject obj, String key) {
        JSONObject res = null;
        try {
            if (!obj.isNull(key))
                res = obj.getJSONObject(key);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return res;
    }

    public static int getJsonInt(JSONObject obj, String key) {
        int res = 0;
        try {
            if (!obj.isNull(key))
                res = obj.getInt(key);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return res;
    }

    public static JSONArray getJsonArray(JSONObject obj, String key) {
        JSONArray res = null;
        try {
            if (!obj.isNull(key))
                res = obj.getJSONArray(key);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    public static void sendBroadCast(Context con, String action, String flag, String flagvalue) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(flag, flagvalue);
        con.sendOrderedBroadcast(intent, null);
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return [0] - width,[1] - height
     */
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        int[] result = {width, height};
        return result;
    }

}
