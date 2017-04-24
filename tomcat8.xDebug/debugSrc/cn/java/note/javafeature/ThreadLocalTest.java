package cn.java.note.javafeature;



public class ThreadLocalTest {
	private final ThreadLocal<String> dispatchData = new ThreadLocal<>();
	public void test(){
		String dd = dispatchData.get();
        if (dd == null) {
            dd = new String("Test");
            dispatchData.set(dd);
        }
	}
	
	public static void main(String[] args) {

	}

}
