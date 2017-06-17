package cn.java.demo.data_mybatis.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

public interface FooMapper {
	
	public int insert();
	public int delete();
	public int update();
	public int selectOne();
	public int selectList();
	
	@Select(value={
    		"SELECT user0.id As id_alias,user0.username,user1.password FROM tb_user AS user0",
    		"LEFT JOIN tb_user AS user1 ON user0.id=user1.id",
    		"WHERE user0.id >= #{userid} ",
    })
    @MapKey(value="id_alias") 
	public Map selectJoin(Integer userid);
	
	
}
