package cn.java.demo.data_jpa.dao.repositorybyextend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import cn.java.demo.data_jpa.entity.FooOne;


@Component(value="fooDao") // beanName="fooDao"
// 实现JpaRepository接口代表本类是repository类
public interface FooOneDao extends JpaRepository<FooOne, Long> { // Foo描述实体类型、Long主键类型
}