package cn.java.note.beans.propertychange;


public class Test {

	public static void main(String[] args) {
		User mUser = new User();
		mUser.addPropertyChangeListener(new UserPropertyChangeListener(mUser));
		mUser.setUsername("username1");
		mUser.setPassword("password1");
		System.out.println(mUser);
	}

}
