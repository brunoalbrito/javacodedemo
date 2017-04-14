package cn.java.file.monitor;

/**
 * 文件过滤器角色，扩展自java.io.FileFilter
 */
public interface FileFilter extends java.io.FileFilter {
    
    /**
     * 获取定义的扩展名
     * 
     * @return
     */
    String[] getExtensions();
}