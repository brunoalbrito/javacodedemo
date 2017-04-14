package cn.java.file.monitor;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;

/**
 * 文件观察器角色
 */
public interface FileObserver {
    
    /**
     * 添加监听器
     * @param listener
     */
    void addListener(final FileAlterationListener listener);
        
    /**
     * 删除监听器
     * @param listener
     */
    void removeListener(final FileAlterationListener listener);
        
    /**
     * 获取注册的监听器
     * @return
     */
    Iterable<FileAlterationListener> getListeners();    
    
    /**
     * 初始化观察器
     * @throws Exception
     */
    void initialize() throws Exception;    
    
    /**
     * 销毁观察器
     * @throws Exception
     */
    void destroy() throws Exception;    
    
    /**
     * 获取观察的目录
     * @return
     */
    File getDirectory();
    
    /**
     * 获取文件过滤器
     * 
     * @return
     */
    public FileFilter getFilter();
}