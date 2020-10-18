/**
 * Author: Tang Yuqian
 * Date: 2020/10/18
 */
package com.concurrency.in.practice.atomicvariables_nonblockingsynchronization;

import net.jcip.annotations.*;

/**
 * ResourceFactory
 * <p/>
 * Lazy initialization holder class idiom
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class ResourceFactory {

    // static子段会在加载期间解析，如下会执行初始化
    private static Resource againResource = new Resource();

    // private的Class在编译阶段不会加载到jvm中，从而将静态变量resource初始化吗？
    // --debug了之后，只有在调用到了getResource()之后ResourceHolder才被加载(它的static子段也会解析)。这就是jvm的惰性类加载技术吗？
    private static class ResourceHolder {
        public static Resource resource = new Resource();
    }

    public static Resource getResource() {
        return ResourceFactory.ResourceHolder.resource;
    }

    static class Resource {
    }

    public static void main(String[] args) {
        getResource();
    }
}
