/**
 * Author: Tang Yuqian
 * Date: 2020/10/18
 */
package com.jvm.learning;

public class Test4 {
    public static Test4 test4 = new Test4();

    public static void main(String[] args) {
        /**栈：main操作数栈
         * Test4 demo = new Test4();的汇编操作如下：
         * 1.new:创建一个空对象并将其引用放入栈顶
         * 2.dup：复制栈顶元素，入栈。
         * 3.invokespecial:构造现场（包括将main操作数栈顶元素出栈赋值给Test4的this引用，
         * this变量在构造方法本地变量表localVariableTable的index0处(variable名字为this))、执行构造方法
         * 4.astore_1:将main操作数栈顶元素出栈放入main的localVariableTable的index1处,variable名字为demo
         */
        Test4 demo = new Test4();

        /**
         * 1.getstatic:获取System的out静态字段,压入main操作数栈顶
         * 2.aload_1:将main的localVariableTable的index1处的demo入栈
         * 3.invokevirtual:构造现场与环境(包括将)、执行add method； 主线程虚拟机栈会新建一个add的栈帧，
         *      1.bipush 10:将10入add操作数栈
         *      2.istore_1:将栈顶元素出栈存入localVariableTable的index 1处，即将10赋值给a
         *      3.bipush 20:将20入add操作数栈
         *      4.istore_2:将栈顶元素出栈存入localVariableTable的index 2处，即将20赋值给b
         *      5.iload_1:将localVariableTable的index 1处的int值入栈即10入栈
         *      6.iload_2:将localVariableTable的index 2处的int值入栈即20入栈
         *      7.iadd:将栈顶和第二个栈中的两个int值分别出栈，相加，将结果入栈
         *      8.ireturn:栈顶出栈，返回int值
         *          1.将局部变量开始指针、操作数栈当前指针恢复成main的（add方法的返回地址中会记录main的局部变量表及操作数栈的地址)
         *          2.将程序计数器中的值改成main中add处的下一条地址即15
         *          3.将add返回值压入main的栈顶
         *          4.将add方法的栈帧内存回收
         * 4.invokevirtual：执行println方法
         */
        System.out.println(demo.add());
    }

    public int add () {
        int a = 10;
        int b = 20;
        return a + b;
    }
}
