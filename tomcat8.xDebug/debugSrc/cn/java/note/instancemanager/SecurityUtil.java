package cn.java.note.instancemanager;


public final class SecurityUtil{
	private static final boolean packageDefinitionEnabled =
		         (System.getProperty("package.definition") == null &&
		           System.getProperty("package.access")  == null) ? false : true;
	public static boolean isPackageProtectionEnabled(){
        if (packageDefinitionEnabled && Globals.IS_SECURITY_ENABLED){
            return true;
        }
        return false;
    }

}