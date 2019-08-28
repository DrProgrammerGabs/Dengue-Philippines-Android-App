package com.p000hl.android.core.utils;

import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* renamed from: com.hl.android.core.utils.ReflectHelp */
public class ReflectHelp {
    public static Object getProperty(Object owner, String fieldName) throws Exception {
        return owner.getClass().getField(fieldName).get(owner);
    }

    public static Object getStaticProperty(String className, String fieldName) throws Exception {
        Class ownerClass = Class.forName(className);
        Object obj = ownerClass.newInstance();
        Field field = ownerClass.getField(fieldName);
        field.set(obj, "miao  peng");
        return field.get(obj);
    }

    public static Object invokeMethod(Object owner, String methodName, Class[] argsType, Object[] argsValue) throws Exception {
        Method method = owner.getClass().getDeclaredMethod(methodName, argsType);
        method.setAccessible(true);
        return method.invoke(owner, argsValue);
    }

    public static Object invokeMethod(String className, String methodName, Class[] argsType, Object[] argsValue) throws Exception {
        Class<?> ownerClass = Class.forName(className);
        Object owner = ownerClass.newInstance();
        Method method = ownerClass.getDeclaredMethod(methodName, argsType);
        method.setAccessible(true);
        return method.invoke(owner, argsValue);
    }

    public static Object newInstance(String className, Class[] argsType, Object[] argsValue) throws Exception {
        Object obj = null;
        if (StringUtils.isEmpty(className)) {
            Log.e("hl", "class " + className + " not exists");
            return obj;
        }
        try {
            return Class.forName(className).getConstructor(argsType).newInstance(argsValue);
        } catch (Exception e) {
            Log.e("hl", "class " + className + "created exception,please check do you have this class", e);
            e.printStackTrace();
            return obj;
        }
    }

    public static boolean isInstance(Object obj, Class cls) {
        return cls.isInstance(obj);
    }

    public static Object getByArray(Object array, int index) {
        return Array.get(array, index);
    }
}
