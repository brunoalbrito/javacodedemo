package cn.java.note.beans.propertyeditor;

import java.beans.PropertyEditorSupport;

public class UserSexEditor extends PropertyEditorSupport {  
  
    private final String[] options = {"Male", "Female"};  
  
    //1. 代表可选属性值的字符串标识数组  
    @Override  
    public String[] getTags() {  
        return options;  
    }  
  
    //2. 代表属性初始值的字符串  
    @Override  
    public String getJavaInitializationString() {  
        return "" + getValue();  
    }  
  
    //3. 将内部属性值转换为对应的字符串表示形式，供属性编辑器显示之用  
    @Override  
    public String getAsText() {  
        int value = (Integer) getValue();  
        return options[value];  
    }  
  
    //4. 将外部设置的字符串转换为内部属性的值  
    @Override  
    public void setAsText(String s) {  
        for (int i = 0; i < options.length; i++) {  
            if (options[i].equals(s)) {  
                setValue(i);
                return;  
            }  
        }  
    }
}  