#include <jni.h>
#include <stdlib.h>
#include <unistd.h>

JNIEXPORT void JNICALL Java_art_willstew_ai_NativeAI_logic(JNIEnv *env, jobject this, jobject rc) {

    // Get a reference to RobotControl class
    jclass rcCls = (*env)->GetObjectClass(env, rc);

    // Get references to RobotControl.moveNorth/South/East/West() functions
    jmethodID moveNorth = (*env)->GetMethodID(env, rcCls, "moveNorth", "()Z");
    jmethodID moveEast = (*env)->GetMethodID(env, rcCls, "moveEast", "()Z");
    jmethodID moveSouth = (*env)->GetMethodID(env, rcCls, "moveSouth", "()Z");
    jmethodID moveWest = (*env)->GetMethodID(env, rcCls, "moveWest", "()Z");

    // Get a reference to RobotControl.fire() function
    jmethodID fire = (*env)->GetMethodID(env, rcCls, "fire", "(II)Z");

    // Get a reference to RobotControl.getRobot() function
    jmethodID getRobot = (*env)->GetMethodID(env, rcCls, "getRobot", "()Lart/willstew/robots/RobotInfo;");
    // Get a reference to RobotControl.getAllRobots() function
    jmethodID getAllRobots = (*env)->GetMethodID(env, rcCls, "getAllRobots", "()[Lart/willstew/robots/RobotInfo;");
    
    // Call RobotControl.getRobot()
    jobject me = (*env)->CallObjectMethod(env, rc, getRobot);
    
    // Get a reference to the RobotInfo class
    jclass riCls = (*env)->GetObjectClass(env, me);

    // Get a reference to RobotInfo.getHealth()
    jmethodID getHealth = (*env)->GetMethodID(env, riCls, "getHealth", "()F");
    // Get a reference to RobotInfo.getX()
    jmethodID getX = (*env)->GetMethodID(env, riCls, "getX", "()I");
    // Get a reference to RobotInfo.getY()
    jmethodID getY = (*env)->GetMethodID(env, riCls, "getY", "()I");
    // Get a reference to RobotInfo.getName()
    jmethodID getName = (*env)->GetMethodID(env, riCls, "getName", "()Ljava/lang/String;");

    // Call RobotInfo.getHealth()
    //float health = (float)(*env)->CallFloatMethod(env, me, getHealth);
    
    int direction = 0;

    while(1 == 1) {
        
        jobjectArray robots = (*env)->CallObjectMethod(env, rc, getAllRobots);

        int i = 0;
        jsize len = (*env)->GetArrayLength(env, robots);

        // jint* body = (*env)->GetIntArrayElements(env, robots, 0);
        
        for (i = 0; i < len; i++) {
            // jobject target = body[i];
            jobject target = (jobject) (*env)->GetObjectArrayElement(env, robots, i);

            int targetX = (int)(*env)->CallObjectMethod(env, target, getX);
            int targetY = (int)(*env)->CallObjectMethod(env, target, getY);

            int meX = (int)(*env)->CallObjectMethod(env, me, getX);
            int meY = (int)(*env)->CallObjectMethod(env, me, getY);

            // Don't shoot yourself
            if (targetX == meX && targetY == meY) {
                break;
            }

            if ((abs(meX - targetX) <= 2) && (abs(meY - targetY) <= 2)) {
                // TODO Check if target is alive
                // FIRE!
                (*env)->CallBooleanMethod(env, rc, fire, targetX, targetY);
                if ((*env)->ExceptionCheck(env)) {
                    return;
                }
            }
        }

        
        switch (direction) {
            case 0:
                if ((int)(*env)->CallObjectMethod(env, rc, moveNorth) == 1) {
                    if ((*env)->ExceptionCheck(env)) {
                        return;
                    }
                    break;
                } else {
                    direction = 1;
                }

            case 1:
                if ((int)(*env)->CallObjectMethod(env, rc, moveEast) == 1) {
                    if ((*env)->ExceptionCheck(env)) {
                        return;
                    }
                    break;
                } else {
                    direction = 2;
                }
            
            case 2:
                if ((int)(*env)->CallObjectMethod(env, rc, moveSouth) == 1) {
                    if ((*env)->ExceptionCheck(env)) {
                        return;
                    }
                    break;
                } else {
                    direction = 3;
                }

            case 3:
                if ((int)(*env)->CallObjectMethod(env, rc, moveWest) == 1) {
                    if ((*env)->ExceptionCheck(env)) {
                        return;
                    }
                    break;
                } else {
                    direction = 0;
                }
            
            default:
                break;
        } 
    }
}