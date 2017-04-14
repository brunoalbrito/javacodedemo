package cn.java.file.monitor;

import java.util.concurrent.ThreadFactory;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * 监测器，监测时间间隔，设置文件观察器
 */
public class FileMonitorImpl implements FileMonitor{

    private final FileAlterationMonitor monitor;
    
    /**
     * 监测器线程名称
     */
    private static final String MONITOR_THREAD_NAME = "File MONITOR Daemon";
        
    /**
     * 监测器线程Daemon标记
     */
    private static final boolean DAEMON = false;
    
    
    /**
     * 定义监测时间间隔、文件观察器
     * @param interval    监测时间间隔
     * @param observer    文件观察者
     */
    FileMonitorImpl(int interval, final FileAlterationObserver observer) {
        this(interval, observer, 
            new BasicThreadFactory.Builder().
                    namingPattern(MONITOR_THREAD_NAME).daemon(DAEMON).build());        
    }    
    
    /**
     * 定义监测时间间隔、文件观察器和线程工厂
     * @param interval    监测时间间隔
     * @param observer    文件观察器
     * @param factory    线程工厂
     */
    FileMonitorImpl(int interval, final FileAlterationObserver observer, 
                           final ThreadFactory factory) {        
        this.monitor = new FileAlterationMonitor(interval, new FileAlterationObserver[] { observer });
        monitor.setThreadFactory(factory);        
    } 
    
    /**
     * 添加文件观察器
     * @param observer
     */
    @Override
    public void addObserver(FileAlterationObserver observer) {
        monitor.addObserver(observer);
    }

    /**
     * 删除文件观察器 
     * @param observer
     */
    @Override
    public void removeObserver(FileAlterationObserver observer) {
        monitor.removeObserver(observer);
    }

    /**
     * 获取注册的所有观察器
     * @return
     */
    @Override
    public Iterable<FileAlterationObserver> getObservers() {
        return monitor.getObservers();
    }

    /**
     * 启动监测器
     */
    @Override
    public void start() {
        try {
            monitor.start();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止监测器
     */
    @Override
    public void stop() {
        try {
            monitor.stop();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取监测时间间隔
     */
    @Override
    public long getInterval() {
        return monitor.getInterval(); 
    }

}