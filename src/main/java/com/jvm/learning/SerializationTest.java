/**
 * Author: Tang Yuqian
 * Date: 2020/11/11
 */
package com.jvm.learning;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 难道enum依赖外层Object的serialId进行标记的？那其他普通类勒，可以试验下，哈哈
 */
public class SerializationTest {
    public static void main(String[] args) {
        System.out.println("序列化：");
        try(ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("object.txt"))) {
            Nation nation = new Nation("China", "1000");
            Teacher teacher = new Teacher("Tom", 53, SexEnum.Female, nation);
            System.out.println("序列化之前的hash：" + teacher.hashCode());
            out.writeObject(teacher);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("反序列化：");
        try(ObjectInputStream input = new ObjectInputStream(
                new FileInputStream("object.txt"))) {
            /**Enum:
             * 1.在更改Enum之前生成的文件可以用更改后的Enum成功反序列化成对象
             * 2.用更改之后的Enum序列化生成文件，这个文件可以用更改前的Enum反序列化吗？
             *      2.1 序列化时用的Enum类中的枚举值是新加的None--java.io.InvalidObjectException: enum constant None does not exist in class com.jvm.learning.SexEnum
             *      2.2 序列化时用的Enum类中的枚举值是旧的Female --可以,所以Enum是根据外层的serialId来标记的
             *
             * 如果Teacher类中的成员对象是普通类对象呢？Nation中不设置SerialId
             * 1.用更改前的Nation序列化生成的文件，可以用更改后的Nation反序列化吗？
             * -- java.io.WriteAbortedException: writing aborted; java.io.NotSerializableException: com.jvm.learning.Nation
             * 所以内部对象的类需要指定serialId,指定了之后无论Nation怎么改都没有问题
             *      */


            Teacher teacher = (Teacher) input.readObject();
            System.out.println("Teacher的名字：" + teacher.getName());
            System.out.println("Teacher的年龄：" + teacher.getAge());
            System.out.println("序列化之后的hash：" + teacher.hashCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
