package cn.java.demo.mapper_mybatis.serviceimpl;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cn.java.demo.mapper_mybatis.dao.mapper.FooMapper;
import cn.java.demo.mapper_mybatis.service.FooService;

public class FooServiceImpl implements FooService{

	private SqlSessionFactory mybatisSessionFactory;

	public void setMybatisSessionFactory(SqlSessionFactory mybatisSessionFactory) {
		this.mybatisSessionFactory = mybatisSessionFactory;
	}


	public void testMethod() {
		SqlSession sqlSession = mybatisSessionFactory.openSession(true); // 自动提交的方式打开
		FooMapper fooMapper = sqlSession.getMapper(FooMapper.class);
		Map map = fooMapper.selectJoin(0);
		for (Map.Entry<Integer,Map> entry : (Set<Map.Entry<Integer, Map>>)map.entrySet()) {
			Integer userId = entry.getKey();
			Map row = entry.getValue();
			if(userId == row.get("id_alias")){
				System.out.println("row.get(\"username\") = " + row.get("username") + ", row.get(\"username\") = " + row.get("username"));;
				System.out.println(row);
			}
		}
		sqlSession.close();
	}
}
