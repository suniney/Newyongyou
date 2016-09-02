package com.example.yanxu.newyongyou;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


/**
 * Created by yd on 2016/4/21.
 */
public class MyApplication extends Application {
    private static MyApplication myApplication = null;
    private final String HTTP_CACHE_FILENAME = "yongyouCache";
    private static Context context;

    public static MyApplication getApplication() {
        return myApplication;
    }

    private Handler mHandler;

    @Override
    public void onCreate() {
        mHandler = new Handler() {

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(context, "网络连接", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            ;
        };
        context = getApplicationContext();
        //设置http缓存，提升用户体验
//        int cacheSize = 10 * 1024 * 1024; // 10 MiB
//        final File baseDir = getApplicationContext().getExternalCacheDir();
//        final File cacheDir = new File(baseDir, HTTP_CACHE_FILENAME);
//        Cache cache = new Cache(cacheDir, cacheSize);
//        OkHttpUtils.getInstance(new OkHttpClient.Builder()
////                .cache(cache)
//                .retryOnConnectionFailure(true)
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Message msg = new Message();
////                        if (CommonUtils.haveNetworkConnection(context)) {
////                            msg.arg1 = -1;
////                            mHandler.sendMessage(msg);
////                        } else {
////                            msg.arg1 = 0;
////                            mHandler.sendMessage(msg);
////                        }
//                        Request originalRequest = chain.request();
//                        String cacheHeaderValue = isOnline()
//                                ? "public, max-age=2419200"
//                                : "public, only-if-cached, max-stale=2419200";
//                        Request request = originalRequest.newBuilder().build();
//
//                        Log.d("head", request.headers().toString());
//
//                        Response response = chain.proceed(request);
//                        return response.newBuilder()
//                                .removeHeader("Pragma")
//                                .removeHeader("Cache-Control")
//                                .header("Cache-Control", cacheHeaderValue)
//                                .build();
//                    }
//                })
//                .addNetworkInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        if (CommonUtils.isNetworkAvailable(context)) {
//                            Log.d("123", "net");
//                        } else {
//                            Log.d("123", "net2");
//                        }
//                        Request originalRequest = chain.request();
//                        String cacheHeaderValue = isOnline()
//                                ? "public, max-age=2419200"
//                                : "public, only-if-cached, max-stale=2419200";
//                        Request request = originalRequest.newBuilder().build();
//                        Log.d("head", request.headers().toString());
//                        Response response = chain.proceed(request);
//                        return response.newBuilder()
//                                .removeHeader("Pragma")
//                                .removeHeader("Cache-Control")
//                                .header("Cache-Control", cacheHeaderValue)
//                                .build();
//                    }
//                })
//                .build());
        myApplication = this;
        super.onCreate();
    }

//    /**
//     * 云端响应头拦截器，用来配置缓存策略
//     * Dangerous interceptor that rewrites the server's cache-control header.
//     */
//    public static final ThreadLocal<Interceptor> INTERCEPTOR = new ThreadLocal<Interceptor>() {
//        @Override
//        protected Interceptor initialValue() {
//            return new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//
//
//                    Response originalResponse = chain.proceed(chain.request());
//                    if (CommonUtils.isNetworkAvailable(context)) {
//                        int maxAge = 60; // read from cache for 1 minute
//                        return originalResponse.newBuilder()
//                                .header("Cache-Control", "public, max-age=" + maxAge)
//                                .build();
//                    } else {
//                        int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
//                        return originalResponse.newBuilder()
//                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                                .build();
//                    }
//                }
//            };
//        }
//    };

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}




