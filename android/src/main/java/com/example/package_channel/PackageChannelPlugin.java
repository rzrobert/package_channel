package com.example.package_channel;

import androidx.annotation.NonNull;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** PackageChannelPlugin */
public class PackageChannelPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private Context applicationContext;
  private MethodChannel channel;

  /** Plugin registration. */
//  @SuppressWarnings("deprecation")
//  public static void registerWith(io.flutter.plugin.common.PluginRegistry.Registrar registrar) {
//    final PackageChannelPlugin instance = new PackageChannelPlugin();
//    instance.onAttachedToEngine(registrar.context(), registrar.messenger());
//  }
  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    onAttachedToEngine(binding.getApplicationContext(), binding);
  }

//  private void onAttachedToEngine(Context applicationContext, @NonNull FlutterPluginBinding flutterPluginBinding) {
  private void onAttachedToEngine(Context applicationContext, @NonNull FlutterPluginBinding flutterPluginBinding) {
    this.applicationContext = applicationContext;
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "package_channel");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    try {
      if (call.method.equals("getChannel")) {
        String channel = "Unknown channel";
        PackageManager pm = applicationContext.getPackageManager();
        ApplicationInfo info = pm.getApplicationInfo(applicationContext.getPackageName(), PackageManager.GET_META_DATA);
        if (info != null) {
          if (info.metaData != null){
            channel = info.metaData.getString("UMENG_CHANNEL");
          }
        }
        result.success(channel);
//        result.success("Android " + android.os.Build.VERSION.RELEASE);
      } else {
        result.notImplemented();
      }
    }  catch (PackageManager.NameNotFoundException ex) {
      result.error("Name not found", ex.getMessage(), null);
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    applicationContext = null;
    channel.setMethodCallHandler(null);
    channel = null;
  }
}
