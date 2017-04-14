package cn.java.file.monitor;

import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * 文件监测器角色
 */
public interface FileMonitor {

    /**
     * 注册观察器
     * @param observer    观察器
     */
    void addObserver(FileAlterationObserver observer);
        
    /**
     * 删除观察器
     * @param observer    观察器
     */
    void removeObserver(FileAlterationObserver observer);    
    
    /**
     * 获取注册的所有观察器
     * @return    观察器集合
     */
    Iterable<FileAlterationObserver> getObservers();    
    
    /**
     * 启动监测器
     */
    void start();    
    
    /**
     * 停止监测器
     */
    void stop();    
    
    /**
     * 获取监测间隔时间
     * @return    间隔时间（单位：毫秒）
     */
    long getInterval();    
    
}