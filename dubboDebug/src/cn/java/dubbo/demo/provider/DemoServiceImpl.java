package cn.java.dubbo.demo.provider;
 
import cn.java.dubbo.demo.DemoService;
 
public class DemoServiceImpl implements DemoService {
 
    public String sayHello(String name) {
        return "Hello " + name;
    }
 
}