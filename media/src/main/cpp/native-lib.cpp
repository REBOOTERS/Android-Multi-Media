//
// Created by rookie on 2/11/21.
//

#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_engineer_android_media_ui_FFmpegActivity_getFFmpegInfo(
        JNIEnv *env,
        jobject /*this*/
) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_engineer_android_media_ui_FFmpegActivity_getHello(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}