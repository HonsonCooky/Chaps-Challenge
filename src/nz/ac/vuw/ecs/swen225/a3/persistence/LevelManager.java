package nz.ac.vuw.ecs.swen225.a3.persistence;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LevelManager {
    static ArrayList<InputStream> levelDescriptions = new ArrayList<>();
    static Set<Class> classSet = new HashSet<>();

    public static void loadLevels(){
        //Load strings
        File folder = new File("src/levels/");
        for(File f : folder.listFiles()){
            try {
                ZipFile zf = new ZipFile(f.getAbsolutePath());
                try {
                    zf.stream().filter(p -> p.getName().contains(".txt"))
                            .forEach(s -> {
                                try{
                                    levelDescriptions.add((zf.getInputStream(s)));
                                }catch (IOException e){
                                    throw new Error("Failed to load level from " + f.getAbsolutePath());
                                }});
                    levelDescriptions.forEach(s -> {
                        try {
                            System.out.println(new BufferedReader(new InputStreamReader(s)).readLine());
                        } catch (Exception e) {
                        }
                    });

                    zf.stream().filter(p -> p.getName().contains(".png"))
                            .forEach(s -> {
                                try {
                                    zf.getInputStream(s);
                                }catch (Exception e){}
                                });
                }finally {
                    zf.close();
                }
            }
            catch(Exception e){}
        }

        // Load classes
        try {
            JarFile jarFile = new JarFile("src/levels/level-1.zip");
            Enumeration<JarEntry> e = jarFile.entries();

            URL[] urls = {new URL("jar:file:" + "src/levels/level-1.zip" + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                Class c = cl.loadClass(className);
                classSet.add(c);
            }
        }catch(Exception e){}

        // Load resources

    }
}