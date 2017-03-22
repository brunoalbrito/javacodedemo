package cn.java.dubbo.note;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;

public class ExtensionLoader {
	
	private static final String SERVICES_DIRECTORY = "META-INF/services/";
    private static final String DUBBO_DIRECTORY = "META-INF/dubbo/";
    private static final String DUBBO_INTERNAL_DIRECTORY = DUBBO_DIRECTORY + "internal/";
    
    // ......
    
	private Map<String, Class<?>> loadExtensionClasses() {
        final SPI defaultAnnotation = type.getAnnotation(SPI.class);
        if(defaultAnnotation != null) {
            String value = defaultAnnotation.value();
            if(value != null && (value = value.trim()).length() > 0) {
                String[] names = NAME_SEPARATOR.split(value);
                if(names.length > 1) {
                    throw new IllegalStateException("more than 1 default extension name on extension " + type.getName()
                            + ": " + Arrays.toString(names));
                }
                if(names.length == 1) cachedDefaultName = names[0];
            }
        }
        
        Map<String, Class<?>> extensionClasses = new HashMap<String, Class<?>>();
        loadFile(extensionClasses, DUBBO_INTERNAL_DIRECTORY);
        loadFile(extensionClasses, DUBBO_DIRECTORY);
        loadFile(extensionClasses, SERVICES_DIRECTORY);
        return extensionClasses;
    }
	
	// ......
	private void loadFile(Map<String, Class<?>> extensionClasses, String dir) {
        String fileName = dir + type.getName();
        try {
            Enumeration<java.net.URL> urls;
            ClassLoader classLoader = findClassLoader();
            if (classLoader != null) {
                urls = classLoader.getResources(fileName);
            } else {
                urls = ClassLoader.getSystemResources(fileName);
            }
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    java.net.URL url = urls.nextElement();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
                        try {
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                final int ci = line.indexOf('#');
                                if (ci >= 0) line = line.substring(0, ci); // 去除注释
                                line = line.trim();
                                if (line.length() > 0) {
                                    try {
                                        String name = null;
                                        int i = line.indexOf('=');
                                        if (i > 0) {
                                            name = line.substring(0, i).trim(); // 扩展名
                                            line = line.substring(i + 1).trim(); // 扩展类
                                        }
                                        if (line.length() > 0) {
                                            Class<?> clazz = Class.forName(line, true, classLoader);
                                            if (! type.isAssignableFrom(clazz)) { // 保证“以接口名称作为文件名”的文件中的“实现类”都实现“接口”
                                                throw new IllegalStateException("Error when load extension class(interface: " +
                                                        type + ", class line: " + clazz.getName() + "), class " 
                                                        + clazz.getName() + "is not subtype of interface.");
                                            }
                                            if (clazz.isAnnotationPresent(Adaptive.class)) { // “实现类”中声明了注解 @Adaptive
                                                if(cachedAdaptiveClass == null) {
                                                    cachedAdaptiveClass = clazz; 
                                                } else if (! cachedAdaptiveClass.equals(clazz)) { // 只能有一个Adaptive类
                                                    throw new IllegalStateException("More than 1 adaptive class found: "
                                                            + cachedAdaptiveClass.getClass().getName()
                                                            + ", " + clazz.getClass().getName());
                                                }
                                            } else { // 没有注解 @Adaptive
                                                try {
                                                    clazz.getConstructor(type); // “实现类”含有此“接口”作为参数的构造函数
                                                    Set<Class<?>> wrappers = cachedWrapperClasses;
                                                    if (wrappers == null) {
                                                        cachedWrapperClasses = new ConcurrentHashSet<Class<?>>();
                                                        wrappers = cachedWrapperClasses;
                                                    }
                                                    wrappers.add(clazz);
                                                } catch (NoSuchMethodException e) {
                                                    clazz.getConstructor(); // 没有构造函数
                                                    if (name == null || name.length() == 0) {
                                                        name = findAnnotationName(clazz); // 获得注解中配置扩展名
                                                        if (name == null || name.length() == 0) {
                                                            if (clazz.getSimpleName().length() > type.getSimpleName().length()
                                                                    && clazz.getSimpleName().endsWith(type.getSimpleName())) {
                                                            	// 去除类中接口的后缀，转成小写作为扩展名称
                                                                name = clazz.getSimpleName().substring(0, clazz.getSimpleName().length() - type.getSimpleName().length()).toLowerCase();
                                                            } else {
                                                                throw new IllegalStateException("No such extension name for the class " + clazz.getName() + " in the config " + url);
                                                            }
                                                        }
                                                    }
                                                    String[] names = NAME_SEPARATOR.split(name); // 有多个名称
                                                    if (names != null && names.length > 0) {
                                                        Activate activate = clazz.getAnnotation(Activate.class); // 
                                                        if (activate != null) {
                                                            cachedActivates.put(names[0], activate);
                                                        }
                                                        for (String n : names) {
                                                            if (! cachedNames.containsKey(clazz)) {
                                                                cachedNames.put(clazz, n);
                                                            }
                                                            Class<?> c = extensionClasses.get(n);
                                                            if (c == null) {
                                                                extensionClasses.put(n, clazz);
                                                            } else if (c != clazz) {
                                                                throw new IllegalStateException("Duplicate extension " + type.getName() + " name " + n + " on " + c.getName() + " and " + clazz.getName());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Throwable t) {
                                        IllegalStateException e = new IllegalStateException("Failed to load extension class(interface: " + type + ", class line: " + line + ") in " + url + ", cause: " + t.getMessage(), t);
                                        exceptions.put(line, e);
                                    }
                                }
                            } // end of while read lines
                        } finally {
                            reader.close();
                        }
                    } catch (Throwable t) {
                        logger.error("Exception when load extension class(interface: " +
                                            type + ", class file: " + url + ") in " + url, t);
                    }
                } // end of while urls
            }
        } catch (Throwable t) {
            logger.error("Exception when load extension class(interface: " +
                    type + ", description file: " + fileName + ").", t);
        }
    }
}
