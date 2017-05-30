package cn.java.codec.json.gson;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GsonObjectFormatUtil {
	/** 
     * 缩进 
     * @param indentCount 
     * @return 
     */  
    private static String indentStrBuild(int indentCount){  
        String strTemp = "";  
        for(int i=0;i<indentCount;i++){  
            strTemp += "    ";  
        }  
        return strTemp;  
    }  
      
    /** 
     * 打印初始结构是map的Java对象 
     * @param map 
     * @param indentCount 
     * @return 
     */  
    public static String indentShowMap(Map<String, Object> map,int indentCount){  
        Iterator iterator =  map.keySet().iterator();  
        String strTemp = "{\n";  
        while (iterator.hasNext()) {  
            String keyName = (String) iterator.next();  
            Object value = map.get(keyName);  
            if(value.getClass().toString().indexOf("java.lang.Double")!=-1){  
                strTemp +=  indentStrBuild(indentCount+1)+keyName+" : "+value.toString()+",\n";  
            }  
            else if(value.getClass().toString().indexOf("java.lang.String")!=-1){  
                strTemp +=  indentStrBuild(indentCount+1)+keyName+" : "+value.toString()+",\n";  
            }  
            else if(value.getClass().toString().indexOf("com.google.gson.internal.LinkedTreeMap")!=-1){  
                strTemp += indentStrBuild(indentCount+1)+keyName+" : "+ indentShowMap((Map<String, Object>) value,indentCount+1)+",\n";  
            }  
            else if(value.getClass().toString().indexOf("List")!=-1){  
                strTemp += indentStrBuild(indentCount+1)+keyName+" : "+ indentShowList((List) value,indentCount+1)+",\n";  
            }  
        }  
        if(-1 != strTemp.lastIndexOf(",\n")){  
            if(strTemp.length()==(strTemp.lastIndexOf(",\n")+2)){  
                strTemp = strTemp.substring(0, strTemp.lastIndexOf(",\n"))+"\n";  
            }  
        }  
        strTemp += ""+indentStrBuild(indentCount)+"}";  
        return strTemp;  
    }  
      
    /** 
     * 打印初始结构是list的Java对象 
     * @param list 
     * @param indentCount 
     * @return 
     */  
    private static String indentShowList(List list,int indentCount){  
        String strTemp = "[\n";  
        Iterator iterator = list.iterator();  
        while (iterator.hasNext()) {  
            Object value = iterator.next();  
            if(value.getClass().toString().indexOf("java.lang.Double")!=-1){  
                strTemp += indentStrBuild(indentCount+1)+value+",\n";  
            }  
            else if(value.getClass().toString().indexOf("java.lang.String")!=-1){  
                strTemp += indentStrBuild(indentCount+1)+value+",\n";  
            }  
            else if(value.getClass().toString().indexOf("com.google.gson.internal.LinkedTreeMap")!=-1){  
                strTemp += indentStrBuild(indentCount+1)+indentShowMap((Map<String, Object>) value,indentCount+1)+",\n";  
            }  
            else if(value.getClass().toString().indexOf("List")!=-1){  
                strTemp += indentStrBuild(indentCount+1)+indentShowList((List) value,indentCount+1)+",\n";  
            }  
        }  
        if(-1 != strTemp.lastIndexOf(",\n")){  
            if(strTemp.length()==(strTemp.lastIndexOf(",\n")+2)){  
                strTemp = strTemp.substring(0, strTemp.lastIndexOf(",\n"))+"\n";  
            }  
        }  
        strTemp += ""+indentStrBuild(indentCount)+"]";  
        return strTemp;  
    }  
      
}
