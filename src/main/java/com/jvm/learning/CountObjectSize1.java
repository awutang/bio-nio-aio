/**
 * Author: Tang Yuqian
 * Date: 2020/11/1
 */
package com.jvm.learning;

import org.openjdk.jol.info.ClassLayout;


/**
 * 未开启指针压缩：
 * com.jvm.learning.CountObjectSize1 object internals:
 *  OFFSET  SIZE     TYPE DESCRIPTION                               VALUE
 *       0     4          (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
 *       4     4          (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
 *       8     4          (object header)                           28 80 ad 0f (00101000 10000000 10101101 00001111) (263028776)
 *      12     4          (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
 *      16     8   double CountObjectSize1.c                        30.0
 *      24     4      int CountObjectSize1.a                        10
 *      28     4      int CountObjectSize1.b                        20
 * Instance size: 32 bytes
 * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
 *
 *
 * 开启：
 * /Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/bin/java -Dvisualvm.id=535619662167018 -XX:+UseCompressedOops "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=63037:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath /Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/lib/packager.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/lib/tools.jar:/Users/ayutang/mygithub/bio-nio-aio/target/classes:/Users/ayutang/mygithub/github-project/lib/javax.annotation.jar:/Users/ayutang/mygithub/github-project/lib/javax.servlet.jar:/Users/ayutang/mygithub/github-project/lib/javax.jms.jar:/Users/ayutang/mygithub/github-project/lib/javax.transaction.jar:/Users/ayutang/mygithub/github-project/lib/javax.servlet.jsp.jar:/Users/ayutang/mygithub/github-project/lib/javax.resource.jar:/Users/ayutang/mygithub/github-project/lib/javax.ejb.jar:/Users/ayutang/mygithub/github-project/lib/javax.persistence.jar:/Users/ayutang/mygithub/github-project/lib/javax.servlet.jsp.jstl.jar:/Users/ayutang/mavenRepository/org/javatuples/javatuples/1.2/javatuples-1.2.jar:/Users/ayutang/mavenRepository/org/apache/commons/commons-lang3/3.10/commons-lang3-3.10.jar:/Users/ayutang/mavenRepository/net/jcip/jcip-annotations/1.0/jcip-annotations-1.0.jar:/Users/ayutang/mavenRepository/org/bouncycastle/bcprov-jdk15on/1.64/bcprov-jdk15on-1.64.jar:/Users/ayutang/mavenRepository/commons-io/commons-io/20030203.000550/commons-io-20030203.000550.jar:/Users/ayutang/mavenRepository/org/projectlombok/lombok/1.18.10/lombok-1.18.10.jar:/Users/ayutang/mavenRepository/org/slf4j/slf4j-api/2.0.0-alpha1/slf4j-api-2.0.0-alpha1.jar:/Users/ayutang/mavenRepository/com/alibaba/fastjson/1.2.71/fastjson-1.2.71.jar:/Users/ayutang/mavenRepository/junit/junit/3.8.2/junit-3.8.2.jar:/Users/ayutang/mavenRepository/org/openjdk/jol/jol-core/0.9/jol-core-0.9.jar com.jvm.learning.CountObjectSize1
 * # WARNING: Unable to attach Serviceability Agent. You can try again with escalated privileges. Two options: a) use -Djol.tryWithSudo=true to try with sudo; b) echo 0 | sudo tee /proc/sys/kernel/yama/ptrace_scope
 * com.jvm.learning.CountObjectSize1 object internals:
 *  OFFSET  SIZE     TYPE DESCRIPTION                               VALUE
 *       0     4          (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
 *       4     4          (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
 *       8     4          (object header)                           05 c1 00 f8 (00000101 11000001 00000000 11111000) (-134168315)
 *      12     4      int CountObjectSize1.a                        10
 *      16     8   double CountObjectSize1.c                        30.0
 *      24     4      int CountObjectSize1.b                        20
 *      28     4          (loss due to the next object alignment)
 * Instance size: 32 bytes
 * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 *
 */
public class CountObjectSize1 {

    private int a = 10;
    private int b = 20;
    private double c = 30.0;

    public static void main(String[] args) {
        CountObjectSize1 objectSize1 = new CountObjectSize1();
        System.out.println(ClassLayout.parseInstance(objectSize1).toPrintable());
    }
}
