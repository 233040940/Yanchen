package com.local.common.id;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO 自定义id生成容器
 * @date 2020-06-08 22:35
 */
public abstract class CustomIDGenerator<T> {


      protected abstract T createID();

      public T generateID() { return createID(); }
}
