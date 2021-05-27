package cc.y2ex.jvm;

import sun.applet.AppletClassLoader;
import sun.misc.Launcher;
import sun.misc.PerfCounter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * 自定义类加载器实现（打破双亲委派机制）
 *
 * @author Yanci丶
 * @date 2021-05-27
 */
public class CustomBreakParentsClassLoader extends ClassLoader {

    private String classPath;

    private final ClassLoader parent = ClassLoader.getSystemClassLoader();

    public CustomBreakParentsClassLoader(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            // 首先会检查该类是否已经被本类加载器加载，如果已经被加载则直接返回
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    // 如果父加载器没有加载到该类，则自己去加载。这里会调用URLClassLoader类的indClass()方法
                    c = findClass(name);
                    // this is the defining class loader; record the stats
                    PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    PerfCounter.getFindClasses().increment();
                }

                if (c == null){
                    try {
                        c = this.getParent().loadClass(name);
                    } catch (ClassNotFoundException e) {
                        // ClassNotFoundException thrown if class not found
                        // from the non-null parent class loader
                    }
                }
                if (c == null){
                    throw new ClassNotFoundException();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    @Override
    protected Class<?> findClass(final String name){
        try {
            byte[] bytes = loadByte(name);
            if (bytes == null) {
                throw null;
            }
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception pae) {
            return null;
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
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            CustomBreakParentsClassLoader customClassLoader = new CustomBreakParentsClassLoader("D://");
            Class<?> customModelClass = customClassLoader.loadClass("cc.y2ex.jvm.CustomModel");
            Object instance = customModelClass.newInstance();
            Method method = customModelClass.getDeclaredMethod("method", null);
            method.invoke(instance);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
