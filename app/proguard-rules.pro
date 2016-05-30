# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in d:\Users\admin\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-ignorewarnings
-libraryjars src/main/jniLibs/armeabi/libdatabase_sqlcipher.so
-libraryjars src/main/jniLibs/armeabi/libsqlcipher_android.so
-libraryjars src/main/jniLibs/armeabi/libstlport_shared.so
-libraryjars src/main/jniLibs/armeabi-v7a/libdatabase_sqlcipher.so
-libraryjars src/main/jniLibs/armeabi-v7a/libsqlcipher_android.so
-libraryjars src/main/jniLibs/armeabi-v7a/libstlport_shared.so
-libraryjars src/main/jniLibs/x86/libdatabase_sqlcipher.so
-libraryjars src/main/jniLibs/x86/libsqlcipher_android.so
-libraryjars src/main/jniLibs/x86/libstlport_shared.so
# 这里根据具体的SDK版本修改
#-libraryjars libs/bmob_v3.4.6.jar
#日志打印
#-libraryjars libs/klog.jar
  #数据库加密
#-libraryjars libs/ormlite-android-sqlcipher-4.46.jar
#-libraryjars libs/ormlite-core-4.46.jar
#-libraryjars libs/guava-r09.jar
#-libraryjars libs/sqlcipher-3.1.0.jar
 #Umeng统计需要依赖umeng-analytics-v6.0.0.jar   utdid4all-1.0.4.jar
#-libraryjars libs/umeng-analytics-v6.0.0.jar
#-libraryjars libs/utdid4all-1.0.4.jar
 #QQ
#-libraryjars libs/mta-sdk-1.6.2.jar
#-libraryjars libs/open_sdk_r5509.jar
#-libraryjars libs/libammsdk.jar


-keepattributes Signature

# 不混淆BmobSDK
-keep class cn.bmob.v3.** {*;}

# 保证继承自BmobObject、BmobUser类的JavaBean不被混淆
-keep class * extends cn.bmob.v3.BmobObject {
    *;
}

-keep class com.tencent.mm.sdk.** {
   *;
}
# 如果你使用了okhttp、okio的包，请添加以下混淆代码
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-keep interface com.squareup.okhttp.** { *; }
-dontwarn okio.**
# 如果你使用了support v4包，请添加如下混淆代码
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
# 如果你需要兼容6.0系统，请不要混淆org.apache.http.legacy.jar
 -dontwarn android.net.compatibility.**
 -dontwarn android.net.http.**
 -dontwarn com.android.internal.http.multipart.**
 -dontwarn org.apache.commons.**
 -dontwarn org.apache.http.**
 -keep class android.net.compatibility.**{*;}
 -keep class android.net.http.**{*;}
 -keep class com.android.internal.http.multipart.**{*;}
 -keep class org.apache.commons.**{*;}
 -keep class org.apache.http.**{*;}

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *;}
-keep interface com.alibaba.fastjson.** { *; }
-dontwarn com.android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.app.** { *; }


-dontwarn com.morgoo.droidplugin.**
-keep class com.morgoo.droidplugin.** { *;}
-keep interface com.morgoo.droidplugin.** { *; }
-keep class com.morgoo.helper.** { *;}
-keep interface com.morgoo.helper.** { *; }

-dontwarn com.aspsine.swipetoloadlayout.**
-keep class com.aspsine.swipetoloadlayout.** { *; }
-keep interface com.aspsine.swipetoloadlayout.** { *; }

-dontwarn com.mxn.soul.flowingdrawer_core.**
-keep class com.mxn.soul.flowingdrawer_core.** { *; }
-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** { *; }

-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.** { *; }

-dontwarn com.android.support.design.**
-keep class com.android.support.design.** { *; }

-dontwarn com.pacific.adapter.**
-keep class com.pacific.adapter.** { *; }

-dontwarn android.support.annotation.**
-keep class android.support.annotation.** { *; }

 #Umeng
 -keepclassmembers class * {
    public <init> (org.json.JSONObject);
 }
-keep public class com.dlh.opensourcelib.R$*{
public static final int *;
}
#QQ互联
-keep class * extends android.app.Dialog

-dontwarn net.sqlcipher.**
-keep class net.sqlcipher.** { *; }
-keep interface net.sqlcipher.** { *; }

-dontwarn net.sqlcipher.CursorWindow.**
-keep class net.sqlcipher.CursorWindow.** { *; }
-keep interface net.sqlcipher.CursorWindow.** { *; }

-dontwarn net.sqlcipher.database.**
-keep class net.sqlcipher.database.** { *; }
-keep interface net.sqlcipher.database.** { *; }
-dontwarn com.tencent.stat.**
-keep class com.tencent.stat.** { *; }
-keep interface com.tencent.stat.** { *; }

-dontwarn com.ta.utdid2.**
-keep class com.ta.utdid2.** { *; }
-keep interface com.ta.utdid2.** { *; }

-dontwarn com.ut.device.**
-keep class com.ut.device.** { *; }
-keep interface com.ut.device.** { *; }

-dontwarn com.j256.ormlite.**
-keep class com.j256.ormlite.** { *; }
-keep interface com.j256.ormlite.** { *; }

-dontwarn com.tencent.**
-keep class com.tencent.** { *; }
-keep interface com.tencent.** { *; }
-dontwarn com.socks.**
-keep class com.socks.** { *; }
-keep interface com.socks.** { *; }
-dontwarn com.google.common.**
-keep class com.google.common.** { *; }
-keep interface com.google.common.** { *; }

-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
-keep public class android.content.Intent

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}

