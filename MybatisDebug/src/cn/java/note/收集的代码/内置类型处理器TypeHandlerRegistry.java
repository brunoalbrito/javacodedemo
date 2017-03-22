package cn.java.note.收集的代码;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.apache.ibatis.type.ArrayTypeHandler;
import org.apache.ibatis.type.BigDecimalTypeHandler;
import org.apache.ibatis.type.BigIntegerTypeHandler;
import org.apache.ibatis.type.BlobByteObjectArrayTypeHandler;
import org.apache.ibatis.type.BlobInputStreamTypeHandler;
import org.apache.ibatis.type.BlobTypeHandler;
import org.apache.ibatis.type.BooleanTypeHandler;
import org.apache.ibatis.type.ByteArrayTypeHandler;
import org.apache.ibatis.type.ByteObjectArrayTypeHandler;
import org.apache.ibatis.type.ByteTypeHandler;
import org.apache.ibatis.type.CharacterTypeHandler;
import org.apache.ibatis.type.ClobReaderTypeHandler;
import org.apache.ibatis.type.ClobTypeHandler;
import org.apache.ibatis.type.DateOnlyTypeHandler;
import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.DoubleTypeHandler;
import org.apache.ibatis.type.FloatTypeHandler;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LongTypeHandler;
import org.apache.ibatis.type.NClobTypeHandler;
import org.apache.ibatis.type.NStringTypeHandler;
import org.apache.ibatis.type.ShortTypeHandler;
import org.apache.ibatis.type.SqlDateTypeHandler;
import org.apache.ibatis.type.SqlTimeTypeHandler;
import org.apache.ibatis.type.SqlTimestampTypeHandler;
import org.apache.ibatis.type.StringTypeHandler;
import org.apache.ibatis.type.TimeOnlyTypeHandler;

public class 内置类型处理器TypeHandlerRegistry {

	/*
	 public TypeHandlerRegistry() {
	    register(Boolean.class, new BooleanTypeHandler());
	    register(boolean.class, new BooleanTypeHandler());
	    register(JdbcType.BOOLEAN, new BooleanTypeHandler());
	    register(JdbcType.BIT, new BooleanTypeHandler());
	
	    register(Byte.class, new ByteTypeHandler());
	    register(byte.class, new ByteTypeHandler());
	    register(JdbcType.TINYINT, new ByteTypeHandler());
	
	    register(Short.class, new ShortTypeHandler());
	    register(short.class, new ShortTypeHandler());
	    register(JdbcType.SMALLINT, new ShortTypeHandler());
	
	    register(Integer.class, new IntegerTypeHandler());
	    register(int.class, new IntegerTypeHandler());
	    register(JdbcType.INTEGER, new IntegerTypeHandler());
	
	    register(Long.class, new LongTypeHandler());
	    register(long.class, new LongTypeHandler());
	
	    register(Float.class, new FloatTypeHandler());
	    register(float.class, new FloatTypeHandler());
	    register(JdbcType.FLOAT, new FloatTypeHandler());
	
	    register(Double.class, new DoubleTypeHandler());
	    register(double.class, new DoubleTypeHandler());
	    register(JdbcType.DOUBLE, new DoubleTypeHandler());
	
	    register(Reader.class, new ClobReaderTypeHandler());
	    register(String.class, new StringTypeHandler());
	    register(String.class, JdbcType.CHAR, new StringTypeHandler());
	    register(String.class, JdbcType.CLOB, new ClobTypeHandler());
	    register(String.class, JdbcType.VARCHAR, new StringTypeHandler());
	    register(String.class, JdbcType.LONGVARCHAR, new ClobTypeHandler());
	    register(String.class, JdbcType.NVARCHAR, new NStringTypeHandler());
	    register(String.class, JdbcType.NCHAR, new NStringTypeHandler());
	    register(String.class, JdbcType.NCLOB, new NClobTypeHandler());
	    register(JdbcType.CHAR, new StringTypeHandler());
	    register(JdbcType.VARCHAR, new StringTypeHandler());
	    register(JdbcType.CLOB, new ClobTypeHandler());
	    register(JdbcType.LONGVARCHAR, new ClobTypeHandler());
	    register(JdbcType.NVARCHAR, new NStringTypeHandler());
	    register(JdbcType.NCHAR, new NStringTypeHandler());
	    register(JdbcType.NCLOB, new NClobTypeHandler());
	
	    register(Object.class, JdbcType.ARRAY, new ArrayTypeHandler());
	    register(JdbcType.ARRAY, new ArrayTypeHandler());
	
	    register(BigInteger.class, new BigIntegerTypeHandler());
	    register(JdbcType.BIGINT, new LongTypeHandler());
	
	    register(BigDecimal.class, new BigDecimalTypeHandler());
	    register(JdbcType.REAL, new BigDecimalTypeHandler());
	    register(JdbcType.DECIMAL, new BigDecimalTypeHandler());
	    register(JdbcType.NUMERIC, new BigDecimalTypeHandler());
	
	    register(InputStream.class, new BlobInputStreamTypeHandler());
	    register(Byte[].class, new ByteObjectArrayTypeHandler());
	    register(Byte[].class, JdbcType.BLOB, new BlobByteObjectArrayTypeHandler());
	    register(Byte[].class, JdbcType.LONGVARBINARY, new BlobByteObjectArrayTypeHandler());
	    register(byte[].class, new ByteArrayTypeHandler());
	    register(byte[].class, JdbcType.BLOB, new BlobTypeHandler());
	    register(byte[].class, JdbcType.LONGVARBINARY, new BlobTypeHandler());
	    register(JdbcType.LONGVARBINARY, new BlobTypeHandler());
	    register(JdbcType.BLOB, new BlobTypeHandler());
	
	    register(Object.class, UNKNOWN_TYPE_HANDLER);
	    register(Object.class, JdbcType.OTHER, UNKNOWN_TYPE_HANDLER);
	    register(JdbcType.OTHER, UNKNOWN_TYPE_HANDLER);
	
	    register(Date.class, new DateTypeHandler());
	    register(Date.class, JdbcType.DATE, new DateOnlyTypeHandler());
	    register(Date.class, JdbcType.TIME, new TimeOnlyTypeHandler());
	    register(JdbcType.TIMESTAMP, new DateTypeHandler());
	    register(JdbcType.DATE, new DateOnlyTypeHandler());
	    register(JdbcType.TIME, new TimeOnlyTypeHandler());
	
	    register(java.sql.Date.class, new SqlDateTypeHandler());
	    register(java.sql.Time.class, new SqlTimeTypeHandler());
	    register(java.sql.Timestamp.class, new SqlTimestampTypeHandler());
	
	    // mybatis-typehandlers-jsr310
	    try {
	      // since 1.0.0
	      register("java.time.Instant", "org.apache.ibatis.type.InstantTypeHandler");
	      register("java.time.LocalDateTime", "org.apache.ibatis.type.LocalDateTimeTypeHandler");
	      register("java.time.LocalDate", "org.apache.ibatis.type.LocalDateTypeHandler");
	      register("java.time.LocalTime", "org.apache.ibatis.type.LocalTimeTypeHandler");
	      register("java.time.OffsetDateTime", "org.apache.ibatis.type.OffsetDateTimeTypeHandler");
	      register("java.time.OffsetTime", "org.apache.ibatis.type.OffsetTimeTypeHandler");
	      register("java.time.ZonedDateTime", "org.apache.ibatis.type.ZonedDateTimeTypeHandler");
	      // since 1.0.1
	      register("java.time.Month", "org.apache.ibatis.type.MonthTypeHandler");
	      register("java.time.Year", "org.apache.ibatis.type.YearTypeHandler");
	
	    } catch (ClassNotFoundException e) {
	      // no JSR-310 handlers
	    }
	
	    // issue #273
	    register(Character.class, new CharacterTypeHandler());
	    register(char.class, new CharacterTypeHandler());
	  }
	 */
}
