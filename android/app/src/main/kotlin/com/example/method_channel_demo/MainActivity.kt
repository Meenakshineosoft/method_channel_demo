package com.example.method_channel_demo

import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import android.os.Handler;

class MainActivity: FlutterActivity() {
    companion object {
        private const val CHANNEL = "com.example.methodchannel/interop"
        private const val METHOD_PLATFORM_VERSION = "getPlatformVersion"
        private const val METHOD_GET_NUMBER = "getNumber"
        private const val METHOD_GET_LIST = "getList"
        private const val METHOD_CALL_ME = "callMe"
    }

    private val handler: Handler = Handler()
    private var pendingResult : MethodChannel.Result? = null
    private lateinit var channel: MethodChannel

    override fun configureFlutterEngine( @NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        super.configureFlutterEngine(flutterEngine)

        channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        channel.setMethodCallHandler({methodCall: MethodCall, result: MethodChannel.Result ->
            if (methodCall.method == METHOD_PLATFORM_VERSION) {
                result.success("Android");
            }
            else if (methodCall.method == METHOD_GET_NUMBER) {
              pendingResult = result
                handler.postDelayed({
                 pendingResult?.success(2020)
                }, 5000)
            }
            else if (methodCall.method == METHOD_GET_LIST) {
                val name = methodCall.argument<String>("name").toString()
                val age = methodCall.argument<Int>("age")
                Log.d("Android", "name = ${name}, age = ${age}")
                val list = listOf("data0", "data1", "data2")
                result.success(list)
            }
            else if (methodCall.method == METHOD_CALL_ME) {
                channel.invokeMethod("callMe", listOf("a, b"), object : MethodChannel.Result{
                    override fun success(result: Any?) {
                        Log.d("Android", "result = $result")
                    }

                    override fun error(
                        errorCode: String,
                        errorMessage: String?,
                        errorDetails: Any?
                    ) {
                        Log.d("Android","$errorCode, $errorMessage, $errorDetails" )
                    }

                    override fun notImplemented() {
                       Log.d("Android", "not Implemented")
                    }
                })

                result.success(null)
            }
          else

              result.notImplemented()

        })
    }



} 


