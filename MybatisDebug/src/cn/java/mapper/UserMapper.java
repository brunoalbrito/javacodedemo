package cn.java.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.java.entity.User;

public interface UserMapper {  
      
    //****** 查询  ******
    public User selectUserById1(String id);  
      
    @Select("select * from ${tablePrefix}user where id = #{id}")   
    public User selectUserById2(String id);
    
    @Select("select * from ${tablePrefix}user where name like \"%\"#{username}\"%\"")  
    public List<User> selectUserByNameLike(String username);  
      
    public List<User> selectUserAll();  
      
    //****** 插入  ******  
    public int insertUser1(User user);
      
    @Insert("insert into user(account,user_name,created_date) values(#{account},#{userName},now())")  
    public int insertUser2(User user);  
      
    //******* 修改  *******  
    @Update("update user set user_name=#{userName} where uid=#{uid}")  
    public int updateUser(User user);
      
      
    //******* 删除  *******  
    @Update("delete from user where uid = #{id}")  
    public int deleteUser(int id);  
      
      
    //******* 批量插入  *******  
    public int insertBatch(List list);  
      
	// --------------------- 查询 ----------------------
	// 执行 case.1，指定要查询的列
    // SELECT "album"."field1" AS "field1", "album"."field2" AS "field2_alias" FROM "album" WHERE field1=:where1 and field2=:where2   
	
    // 执行 case.2,指定别名
    // SELECT "table1_alias"."field1" AS "field1", "table1_alias"."field2" AS "field2_alias" FROM "album" AS "table1_alias" WHERE field1=:where1 and field2=:where2
	    
	// 执行 case.3，分组、排序、分页限制 
	//  SELECT "album"."field1" AS "field1", "album"."field2" AS "field2_alias"
	//  FROM "album" WHERE field1=:where1 and field2=:where2
	//  GROUP BY "field1", "field2"
	//  ORDER BY "field1" ASC, "field2" DESC
	//  LIMIT :limit OFFSET :offset
    
	// ---关联---
	//  SELECT "table1_alias"."t1_field1" AS "t1_field1", "table1_alias"."t1_field2" AS "t1_field2_alias", "table2_alias"."t2_field1" AS "t2_field1", "table2_alias"."t2_field2" AS "t2_field2", "table3_alias".*
	//  FROM "album" AS "table1_alias"
	//  LEFT JOIN "table2" AS "table2_alias" ON "table2_alias"."t1_field1_ref" AND "table1_alias"."t1_field1"
	//  LEFT JOIN "table3" AS "table3_alias" ON "table3_alias"."t1_field1_ref" AND "table1_alias"."t1_field1"
	//  WHERE field1=:where1 and field2=:where2
	//  GROUP BY "t1_field1", "t1_field2"
	//  ORDER BY "t1_field1" ASC, "t1_field2" DESC
	//  LIMIT :limit OFFSET :offset
    
	// ---子查询---
	//  执行 case.1，【子查询作为条件】
	//  SELECT *  from pg_user where id in(SELECT id  from pg_user);
	    
	//  执行 case.2，【子查询作为表】子查询出列表，再从列表中查询
	//  SELECT "sub_table2_alias"."t1_field1" AS "t1_field1", "sub_table2_alias"."t1_field2" AS "t1_field2_alias"
	//          FROM (SELECT "table2_alias"."t2_field1" AS "t2_field1", "table2_alias"."t2_field2" AS "t2_field2_alias" FROM "table2" AS "table2_alias") AS "sub_table2_alias"
	//                  LIMIT :limit
	    
	//  执行 case.3，【子查询作为字段】子查询只能返回单条
	//  SELECT "table1_alias"."t1_field1" AS "t1_field1", "table1_alias"."t1_field2" AS "t1_field2_alias", (SELECT "table2_alias"."t2_field2" AS "t2_field2_alias" FROM "table2" AS "table2_alias") AS "sub_field2_alias"
	//          FROM "album" AS "table1_alias" LIMIT :limit   
	    
	// ---联合查询---
	//  ( SELECT "table1_alias"."t1_field1" AS "t1_field1", "table1_alias"."t1_field2" AS "field2_alias" FROM "album" AS "table1_alias" )
	//      UNION ALL ( SELECT "table2_alias"."t2_field1" AS "t2_field1", "table2_alias"."t2_field2" AS "field2_alias" FROM "table2" AS "table2_alias" )
	
	//　---统计---
	//  SELECT "album"."count""("*")" AS "count_total" FROM "album"

}  