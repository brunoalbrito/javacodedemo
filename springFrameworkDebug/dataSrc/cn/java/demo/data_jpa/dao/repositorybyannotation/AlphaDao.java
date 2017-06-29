package cn.java.demo.data_jpa.dao.repositorybyannotation;

import javax.persistence.Entity;

import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Component;

import cn.java.demo.data_jpa.entity.Alpha;

@Component(value="alphaDao") // beanName="alphaDao"
@Entity // 代表本类是repository类
@RepositoryDefinition(domainClass=Alpha.class,idClass=Integer.class) // domainClass描述实体类型、idClass主键类型
public interface AlphaDao {
}