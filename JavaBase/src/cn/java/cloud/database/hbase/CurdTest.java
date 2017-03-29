package cn.java.cloud.database.hbase;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author zhouzhian
 * http://www.cnblogs.com/ggjucheng/p/3381328.html
 */
public class CurdTest {

	// 声明静态配置
    static Configuration conf = null;
    static {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "localhost");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
    }
    
    /**
     * 判断表是否存在
     * @param tableName
     * @return
     * @throws IOException
     */
  	private static boolean isExist(String tableName) throws IOException {
  		HBaseAdmin admin = new HBaseAdmin(conf);
  		return admin.tableExists(tableName);
  	}
  	
    /**
     * 创建表
     * @tableName 表名
     * @family 列族列表
     */
    public static void creatTable(String tableName, String[] family)
            throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        HTableDescriptor desc = new HTableDescriptor(tableName);
        for (int i = 0; i < family.length; i++) {
            desc.addFamily(new HColumnDescriptor(family[i]));
        }
        if (admin.tableExists(tableName)) {
            System.out.println("table Exists!");
            System.exit(0);
        } else {
            admin.createTable(desc);
            System.out.println("create table Success!");
        }
    }


    /**
     * 为表添加数据（适合知道有多少列族的固定表）
     * @rowKey rowKey
     * @tableName 表名
     * @column1 第一个列族列表
     * @value1 第一个列的值的列表
     * @column2 第二个列族列表
     * @value2 第二个列的值的列表
     */
    public static void addData(String rowKey, String tableName,
            String[] column1, String[] value1, String[] column2, String[] value2)
            throws IOException {
        Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey
        HTable table = new HTable(conf, Bytes.toBytes(tableName));// HTabel负责跟记录相关的操作如增删改查等获取表
        HColumnDescriptor[] columnFamilies = table.getTableDescriptor() // 获取所有的列族
                .getColumnFamilies();

        for (int i = 0; i < columnFamilies.length; i++) {
            String familyName = columnFamilies[i].getNameAsString(); // 获取列族名
            if (familyName.equals("article")) { // article列族put数据
                for (int j = 0; j < column1.length; j++) {
                    put.add(Bytes.toBytes(familyName),
                            Bytes.toBytes(column1[j]), Bytes.toBytes(value1[j]));
                }
            }
            if (familyName.equals("author")) { // author列族put数据
                for (int j = 0; j < column2.length; j++) {
                    put.add(Bytes.toBytes(familyName),
                            Bytes.toBytes(column2[j]), Bytes.toBytes(value2[j]));
                }
            }
        }
        table.put(put);
        System.out.println("add data Success!");
    }
    
    /**
     * 根据rwokey查询
     * 
     * @rowKey rowKey
     * 
     * @tableName 表名
     */
    public static Result getResult(String tableName, String rowKey)
            throws IOException {
        Get get = new Get(Bytes.toBytes(rowKey));
        HTable table = new HTable(conf, Bytes.toBytes(tableName));// 获取表
        Result result = table.get(get);
        
        //  输出结果,raw方法返回所有keyvalue数组
        for (KeyValue rowKV : result.raw()) { // result.list()
			System.out.print("行名:" + new String(rowKV.getRow()) + " ");
			System.out.print("时间戳:" + rowKV.getTimestamp() + " ");
			System.out.print("列族名:" + new String(rowKV.getFamily()) + " ");
			System.out.print("列名:" + new String(rowKV.getQualifier()) + " ");
			System.out.println("值:" + new String(rowKV.getValue()));
			System.out.println("-------------------------------------------");
		}
        
        //
        List<Cell> list = result.listCells();
        for (Cell cell : list) {
        	System.out.print("列族名:" + new String(cell.getFamilyArray()) + " ");
        	System.out.print("列名:" + new String(cell.getQualifierArray()) + " ");
        	System.out.println("值:" + new String(cell.getValueArray()));
		}
        return result;
    }


    /**
     * 遍历查询hbase表
     * @tableName 表名
     */
    public static void getResultScann(String tableName) throws IOException {
        Scan scan = new Scan();
        ResultScanner results = null;
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        try {
        	results = table.getScanner(scan);
            for (Result result : results) {
                for (KeyValue rowKV : result.list()) {
                    System.out.println("row:" + Bytes.toString(rowKV.getRow()));
                    System.out.println("family:"
                            + Bytes.toString(rowKV.getFamily()));
                    System.out.println("qualifier:"
                            + Bytes.toString(rowKV.getQualifier()));
                    System.out
                            .println("value:" + Bytes.toString(rowKV.getValue()));
                    System.out.println("timestamp:" + rowKV.getTimestamp());
                    System.out
                            .println("-------------------------------------------");
                }
            }
        } finally {
        	results.close();
        }
    }

    /**
     * 遍历查询hbase表，指定扫描区间
     * @tableName 表名
     */
    public static void getResultScann(String tableName, String startRowkey,
            String stopRowkey) throws IOException {
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes(startRowkey));
        scan.setStopRow(Bytes.toBytes(stopRowkey));
        ResultScanner rs = null;
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        try {
            rs = table.getScanner(scan);
            for (Result r : rs) {
                for (KeyValue kv : r.list()) {
                    System.out.println("row:" + Bytes.toString(kv.getRow()));
                    System.out.println("family:"
                            + Bytes.toString(kv.getFamily()));
                    System.out.println("qualifier:"
                            + Bytes.toString(kv.getQualifier()));
                    System.out
                            .println("value:" + Bytes.toString(kv.getValue()));
                    System.out.println("timestamp:" + kv.getTimestamp());
                    System.out
                            .println("-------------------------------------------");
                }
            }
        } finally {
            rs.close();
        }
    }

    /**
     * 查询表中的某一列（某行某列簇里的某列）
     * @tableName 表名
     * @rowKey rowKey
     */
    public static void getResultByColumn(String tableName, String rowKey,
            String familyName, String columnName) throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName)); // 获取指定列族和列修饰符对应的列
        Result result = table.get(get);
        for (KeyValue kv : result.list()) {
            System.out.println("family:" + Bytes.toString(kv.getFamily()));
            System.out
                    .println("qualifier:" + Bytes.toString(kv.getQualifier()));
            System.out.println("value:" + Bytes.toString(kv.getValue()));
            System.out.println("Timestamp:" + kv.getTimestamp());
            System.out.println("-------------------------------------------");
        }
    }

    /**
     * 更新表中的某一列
     * 
     * @tableName 表名
     * @rowKey rowKey
     * @familyName 列族名
     * @columnName 列名
     * @value 更新后的值
     */
    public static void updateTable(String tableName, String rowKey,
            String familyName, String columnName, String value)
            throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(familyName), Bytes.toBytes(columnName),
                Bytes.toBytes(value));
        table.put(put);
        System.out.println("update table Success!");
    }

    /**
     * 查询某列数据的多个版本（设置指定版本的数据）
     * @tableName 表名
     * @rowKey rowKey
     * @familyName 列族名
     * @columnName 列名
     */
    public static void getResultByVersion(String tableName, String rowKey,
            String familyName, String columnName) throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
        get.setMaxVersions(5);
        Result result = table.get(get);
        for (KeyValue kv : result.list()) {
            System.out.println("family:" + Bytes.toString(kv.getFamily()));
            System.out
                    .println("qualifier:" + Bytes.toString(kv.getQualifier()));
            System.out.println("value:" + Bytes.toString(kv.getValue()));
            System.out.println("Timestamp:" + kv.getTimestamp());
            System.out.println("-------------------------------------------");
        }
    }

    /**
     * 删除一条(行)数据的指定的列
     * @tableName 表名
     * @rowKey rowKey
     * @familyName 列族名
     * @columnName 列名
     */
    public static void deleteColumn(String tableName, String rowKey,
            String falilyName, String columnName) throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Delete deleteColumn = new Delete(Bytes.toBytes(rowKey));
        deleteColumn.deleteColumns(Bytes.toBytes(falilyName),
                Bytes.toBytes(columnName));
        table.delete(deleteColumn);
        System.out.println(falilyName + ":" + columnName + "is deleted!");
    }

    /**
     * 删除一条(行)数据
     * @tableName 表名
     * @rowKey rowKey
     */
    public static void deleteAllColumn(String tableName, String rowKey)
            throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Delete deleteAll = new Delete(Bytes.toBytes(rowKey));
        table.delete(deleteAll);
        System.out.println("all columns are deleted!");
    }

    /**
     * 删除多条数据
     * @param tableName
     * @param rows
     * @throws Exception
     */
 	public static void delMultiRows(String tableName, String[] rows)
 			throws Exception {
 		HTable table = new HTable(conf, tableName);
 		List<Delete> delList = new ArrayList<Delete>();
 		for (String row : rows) {
 			Delete del = new Delete(Bytes.toBytes(row));
 			delList.add(del);
 		}
 		table.delete(delList);
 	}

    /**
     * 删除表
     * @tableName 表名
     */
    public static void deleteTable(String tableName) throws IOException {
        HBaseAdmin admin = new HBaseAdmin(conf);
        if (admin.tableExists(tableName)) {
        	admin.disableTable(tableName);
        	admin.deleteTable(tableName);
        	System.out.println(tableName + "is deleted!");
        }
    }

    public static void main(String[] args) throws Exception {

        // 创建表
        String tableName = "blog2";
        String[] family = { "article", "author" };
        creatTable(tableName, family);

        // 为表添加数据
        String[] column1 = { "title", "content", "tag" };
        String[] value1 = {
                "Head First HBase",
                "HBase is the Hadoop database. Use it when you need random, realtime read/write access to your Big Data.",
                "Hadoop,HBase,NoSQL" };
        String[] column2 = { "name", "nickname" };
        String[] value2 = { "nicholas", "lee" };
        addData("rowkey1", tableName, column1, value1, column2, value2);
        addData("rowkey2", tableName, column1, value1, column2, value2);
        addData("rowkey3", tableName, column1, value1, column2, value2);

        // 遍历查询
        getResultScann(tableName, "rowkey4", "rowkey5");
        
        // 根据row key范围遍历查询
        getResultScann(tableName, "rowkey4", "rowkey5");

        // 查询
        getResult(tableName, "rowkey1");

        // 查询某一列的值
        getResultByColumn(tableName,"rowkey1", "author", "name");

        // 更新列
        updateTable(tableName, "rowkey1", "author", "name", "bin");

        // 查询某一列的值
        getResultByColumn(tableName, "rowkey1", "author", "name");

        // 查询某列的多版本
        getResultByVersion(tableName, "rowkey1", "author", "name");

        // 删除一列
        deleteColumn(tableName, "rowkey1", "author", "nickname");

        // 删除所有列
        deleteAllColumn(tableName, "rowkey1");

        // 删除表
        deleteTable(tableName);

    }
}
