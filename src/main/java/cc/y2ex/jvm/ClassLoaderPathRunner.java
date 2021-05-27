package cc.y2ex.jvm;

import sun.misc.Launcher;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 获取类加载器加载路径信息
 *
 * @author Yanci丶
 * @date 2021-05-26
 */
public class ClassLoaderPathRunner {

    public static void main(String[] args) {

        URL[] bootstrapUrlClassLoaderURLs = Launcher.getBootstrapClassPath().getURLs();
        System.out.println("BootstrapClassLoader启动类加载器所加载的目录：");
        for (int i = 0; i < bootstrapUrlClassLoaderURLs.length; i++){
            System.out.println(bootstrapUrlClassLoaderURLs[i]);
        }
        System.out.println("--------------------------------------------");

        ClassLoader extClassLoader = ClassLoader.getSystemClassLoader().getParent();
        URLClassLoader extUrlClassLoader = (URLClassLoader) extClassLoader;
        URL[] extUrlClassLoaderURLs = extUrlClassLoader.getURLs();
        System.out.println("ExtClassLoader扩展类加载器所加载的目录：");
        for (int i = 0; i < extUrlClassLoaderURLs.length; i++){
            System.out.println(extUrlClassLoaderURLs[i]);
        }
        System.out.println("ExtClassLoader扩展类加载器父加载器："+extClassLoader.getParent());
        System.out.println("--------------------------------------------");

        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(appClassLoader);
        URLClassLoader appUrlClassLoader = (URLClassLoader) appClassLoader;
        URL[] appUrlClassLoaderURLs = appUrlClassLoader.getURLs();
        System.out.println("AppClassLoader应用类加载器所加载的目录：");
        for (int i = 0; i < appUrlClassLoaderURLs.length; i++){
            System.out.println(appUrlClassLoaderURLs[i]);
        }
        System.out.println("AppClassLoader应用类加载器父加载器："+appClassLoader.getParent());
    }
}
