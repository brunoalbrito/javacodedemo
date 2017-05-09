package cn.java.internal.packagetest;

import java.lang.annotation.Annotation;

public interface SequenceGenerator
    extends Annotation
{

    public abstract String name();

    public abstract String sequenceName();

    public abstract String catalog();

    public abstract String schema();

    public abstract int initialValue();

    public abstract int allocationSize();
}