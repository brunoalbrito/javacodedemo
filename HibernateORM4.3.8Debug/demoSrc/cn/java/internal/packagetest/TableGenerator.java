package cn.java.internal.packagetest;

import java.lang.annotation.Annotation;

import javax.persistence.Index;
import javax.persistence.UniqueConstraint;

/**
 * 定义一个注解
 * 
 * 本类从hibernate-jpa-2.1-api-1.0.0.Final.jar文件中拷贝出来
 * 
 * @author Administrator
 *
 */
public interface TableGenerator
    extends Annotation
{

    public abstract String name();

    public abstract String table();

    public abstract String catalog();

    public abstract String schema();

    public abstract String pkColumnName();

    public abstract String valueColumnName();

    public abstract String pkColumnValue();

    public abstract int initialValue();

    public abstract int allocationSize();

    public abstract UniqueConstraint[] uniqueConstraints();

    public abstract Index[] indexes();
}
