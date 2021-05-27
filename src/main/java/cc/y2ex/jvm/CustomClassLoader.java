package cc.y2ex.jvm;

import java.io.*;
import java.lang.reflect.Method;

/**
 * 自定义类加载器实现
 *
 * @author Yanci丶
 * @date 2021-05-27
 */
public class CustomClassLoader extends ClassLoader {

    private String classPath;

    public CustomClassLoader(String classPath) {
        this.classPath = classPath;
    }

    @Override
    public Class<?> findClass(final String name)
            throws ClassNotFoundException
    {
        try {
            byte[] bytes = loadByte(name);
            if (bytes == null) {
                throw new ClassNotFoundException(name);
            }
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception pae) {
            throw new ClassNotFoundException(name);
        }
    }

    private byte[] loadByte(String name){
        name = name.replaceAll("\\.","/").concat(".class");
        String filePath = classPath + name;
        try {
            InputStream in = new FileInputStream(filePath);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            CustomClassLoader customClassLoader = new CustomClassLoader("D://");
            Class<?> customModelClass = customClassLoader.loadClass("cc.y2ex.jvm.CustomModel");
            Object instance = customModelClass.newInstance();
            Method method = customModelClass.getDeclaredMethod("method", null);
            method.invoke(instance);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
