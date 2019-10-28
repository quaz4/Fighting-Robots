#include <jni.h>

#include <unistd.h>

JNIEXPORT void JNICALL Java_art_willstew_ai_NativeAI_logic(JNIEnv *env, jobject this, jobject rc) {

    // Get a reference to RobotControl
    jclass rcCls = (*env)->GetObjectClass(env, rc);

    jmethodID moveNorth = (*env)->GetMethodID(env, rcCls, "moveNorth", "()Z");
    jmethodID moveEast = (*env)->GetMethodID(env, rcCls, "moveEast", "()Z");
    jmethodID moveSouth = (*env)->GetMethodID(env, rcCls, "moveSouth", "()Z");
    jmethodID moveWest = (*env)->GetMethodID(env, rcCls, "moveWest", "()Z");

    jmethodID fire = (*env)->GetMethodID(env, rcCls, "fire", "(II)Z");

    jmethodID getRobot = (*env)->GetMethodID(env, rcCls, "getRobot", "()Ljava/art/willstew/robots/RobotInfo;");

    // jboolean res = (*env)->CallObjectMethod(env, rc, moveNorth);

    jobject me = (*env)->CallObjectMethod(env, rc, getRobot);
    jmethodID getHealth = (*env)->GetMethodID(env, me, "getHealth", "()F");
    jobject health = (*env)->CallObjectMethod(env, me, getHealth); // TODO what...


    // jfloat health = (*env)->CallTypeMethod(env, obj, method, method arguments);

    // jfloat health = (*env)->CallObjectMethod(env, me, getHealth);

    while(1 == 1) {
        int direction = 0;

        switch (direction) {
            case 0:
                if ((int)(*env)->CallObjectMethod(env, rc, moveNorth) == 1) {
                    sleep(1);
                    break;
                } else {
                    direction = 1;
                }

            case 1:
                if ((int)(*env)->CallObjectMethod(env, rc, moveEast) == 1) {
                    sleep(1);
                    break;
                } else {
                    direction = 2;
                }
            
            case 2:
                if ((int)(*env)->CallObjectMethod(env, rc, moveSouth) == 1) {
                    sleep(1);
                    break;
                } else {
                    direction = 3;
                }

            case 3:
                if ((int)(*env)->CallObjectMethod(env, rc, moveWest) == 1) {
                    sleep(1);
                    break;
                } else {
                    direction = 0;
                }
            
            default:
                break;
        } 
    }
}