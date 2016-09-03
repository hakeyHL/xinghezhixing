package hasoffer.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provider {
 
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"/spring/dubbo-spring.xml"});
        context.start();
        System.out.println("init end");
        System.in.read(); // 按任意键退出
    }
 
}