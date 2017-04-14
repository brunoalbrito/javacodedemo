package cn.java.file.monitor;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * 文件观察器
 * 
 * 当有文件创建、删除、或变更动作时，则消息通知监听器
 */
public class FileAlterationObserverImpl extends FileAlterationObserver /*implements FileObserver*/ {
    
    private static final long serialVersionUID = -7239227289538993830L;
    
    /**
     * 文件过滤器
     */
    private final FileFilter filter;
        
    /**
     * 设置要监听观察的目录，并设置文件过滤器和监听器，用以观察指定具有指定扩展名的文件
     * @param dir            观察监听的目录
     * @param filter        文件过滤器
     * @param listener        文件监听器
     */
    public FileAlterationObserverImpl(String dir, final FileFilter filter, 
                                   FileAlterationListener listener) {
        super(dir, filter, (IOCase) null);
        addListener(listener); 
        
        this.filter = filter;
        
        File directory = new File(dir);
        
        // 如果目录不存在
        if(!directory.exists()) {
            try {
                FileUtils.forceMkdir(directory);
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 如果存在的是文件
        else if(directory.exists() && directory.isFile()) {
            try {
                FileUtils.forceDelete(directory);
                FileUtils.forceMkdir(directory);
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 添加监听器
     */
    @Override
    public void addListener(final FileAlterationListener listener) {
        super.addListener(listener);
    }

    /**
     * 移除监听器
     */
    @Override
    public void removeListener(final FileAlterationListener listener) {
        super.removeListener(listener);
    }

    /**
     * 获取观察者对象的所有监听器
     */
    @Override
    public Iterable<FileAlterationListener> getListeners() {
        return super.getListeners();
    }
    
    /**
     * 初始化文件观察者
     */
    @Override
    public void initialize() throws Exception {
        super.initialize();
    }

    /**
     * 销毁文件观察者
     */
    @Override
    public void destroy() throws Exception {
        super.destroy();
    }
    
    /**
     * 获取所观察的目录
     */
    @Override
    public File getDirectory() {
        return super.getDirectory();
    }

    /**
     *  获取文件过滤器
     * @return
     */
    public FileFilter getFilter() {
        return filter;
    }    
}