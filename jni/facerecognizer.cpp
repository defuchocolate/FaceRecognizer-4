#include <jni.h>
// #include "../opencv-android-sdk/native/jni/include/opencv2/contrib/contrib.hpp"
#include "opencv2/contrib/contrib.hpp"

//
// the create****FaceRecognizer functions return a cv::Ptr<FaceRecognizer>.
// if this thing leaves the scope(our function returns), it will destroy the instance.
// so we have to something about that (adding a refcount):
//

//template<typename Y>
//Y * extractPtr(cv::Ptr<Y> & y)
//{
//#if (CV_MAJOR_VERSION > 2) // 3.0 / master
//    new (&y)cv::Ptr<Y>(y); // inplace new ;)
//    return y.get();
//#else                      // 2.4
//    y.addref();
//    return y.obj;
//#endif
//}

#ifdef __cplusplus
extern "C" {
#endif

// don't worry about the _10 , _1 resolves to a single underscore

JNIEXPORT jlong JNICALL Java_com_eim_facerecognition_LBPHFaceRecognizer_createLBPHFaceRecognizer_10(JNIEnv* env, jclass);
JNIEXPORT jlong JNICALL Java_com_eim_facerecognition_LBPHFaceRecognizer_createLBPHFaceRecognizer_10(JNIEnv* env, jclass) {
    try {
    	cv::Ptr<cv::FaceRecognizer> pfr = cv::createLBPHFaceRecognizer();
    	pfr.addref();
        // cv::FaceRecognizer * pfr = extractPtr<cv::FaceRecognizer>(cv::createLBPHFaceRecognizer());
        return (jlong) pfr.obj;
    } catch (...) {
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "sorry, dave..");
    }
    return 0;
}

JNIEXPORT jlong JNICALL Java_com_eim_facerecognition_LBPHFaceRecognizer_createLBPHFaceRecognizer_11(JNIEnv* env, jclass, jint radius);
JNIEXPORT jlong JNICALL Java_com_eim_facerecognition_LBPHFaceRecognizer_createLBPHFaceRecognizer_11(JNIEnv* env, jclass, jint radius) {
    try {
    	cv::Ptr<cv::FaceRecognizer> pfr = cv::createLBPHFaceRecognizer((int)radius);
    	pfr.addref();
        // cv::FaceRecognizer * pfr = extractPtr<cv::FaceRecognizer>(cv::createLBPHFaceRecognizer((int)radius));
        return (jlong) pfr.obj;
    } catch (...) {
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "sorry, dave..");
    }
    return 0;
}

JNIEXPORT jlong JNICALL Java_com_eim_facerecognition_LBPHFaceRecognizer_createLBPHFaceRecognizer_12(JNIEnv* env, jclass, jint radius, jint neighbours);
JNIEXPORT jlong JNICALL Java_com_eim_facerecognition_LBPHFaceRecognizer_createLBPHFaceRecognizer_12(JNIEnv* env, jclass, jint radius, jint neighbours) {
    try {
    	cv::Ptr<cv::FaceRecognizer> pfr = cv::createLBPHFaceRecognizer((int)radius,(int)neighbours);
    	pfr.addref();
        // cv::FaceRecognizer * pfr = extractPtr<cv::FaceRecognizer>(cv::createLBPHFaceRecognizer((int)radius,(int)neighbours));
        return (jlong) pfr.obj;
    } catch (...) {
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "sorry, dave..");
    }
    return 0;
}

// and so on for the remaining args ....

#ifdef __cplusplus
}
#endif