package prv.rcl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import prv.rcl.pojo.Ticket;
import prv.rcl.service.UserService;

/**
 * Hello world!
 */
@SpringBootApplication
public class App {
    public static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
//        log.info("Hello World!");
//        SpringApplication.run(App.class,args);
        // 创建一个 读取 Resource
        ClassPathResource classPathResource = new ClassPathResource("bean.xml");
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
        // XmlBeanFactory 创建
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(classPathResource);
        // BeanFactory 方法执行
        UserService bean = xmlBeanFactory.getBean("test", UserService.class);
        Ticket ticket = xmlBeanFactory.getBean("ticket", Ticket.class);
        bean.execute();
    }
}
