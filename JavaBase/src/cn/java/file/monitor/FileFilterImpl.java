package cn.java.file.monitor;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

/**
 * 文件过滤器
 */
public class FileFilterImpl implements FileFilter{
    
    private String[] extensions;
    
    public FileFilterImpl(String... extensions) {
        this.extensions = extensions;
    }
    
    /**
     * 是否接受该文件
     */
    @Override
    public boolean accept(File pathname) {
        return FilenameUtils.isExtension(pathname.getName(), extensions); 
    }

    /**
     * 获取定义的扩展名
     * @return  
     */
    @Override
    public String[] getExtensions() {
        return extensions;
    }

}