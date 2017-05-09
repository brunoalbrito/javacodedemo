package cn.java.noteagain;

public class FinalNote2Parameter {

	public static void main(String[] args) {
	/**
	 参数：
	 	org.apache.catalina.core.StandardWrapperFacade
		org.apache.catalina.connector.RequestFacade
		org.apache.catalina.connector.ResponseFacade

		参数又被分装了一层，减少了内部API的数量
			ServletConfig = new StandardWrapperFacade(org.apache.catalina.core.StandardWrapper);//这对这个Servlet的配置数据
			HttpServletRequest = org.apache.catalina.connector.Request.getRequest() == new RequestFacade(org.apache.catalina.connector.Request)
			HttpServletResponse = org.apache.catalina.connector.Response.getResponse() == new ResponseFacade(org.apache.catalina.connector.Response);
			
			ServletContext == this.getServletContext();
			{
				this.getServletConfig().getServletContext(); // == new StandardWrapperFacade(org.apache.catalina.core.StandardWrapper).getServletContext()
				{
					ServletConfig = (ServletConfig)org.apache.catalina.core.StandardWrapperFacade; 
					ServletConfig.getServletContext(); 
					{
						return org.apache.catalina.core.StandardWrapperFacade.getServletContext()
						{
							return org.apache.catalina.core.StandardWrapper.getServletContext()
							{
								if (StandardWrapper.parent == null)
						            return (null);
						        else if (!(StandardWrapper.parent instanceof Context))
						            return (null);
						        else
						            return (((Context) StandardWrapper.parent).getServletContext());
									{
										if (context == null) {
								            context = new org.apache.catalina.core.ApplicationContext(getBasePath(), this);
								            if (altDDName != null)
								                context.setAttribute(Globals.ALT_DD_ATTR,altDDName);
								        }
								        return (context.getFacade());//org.apache.catalina.core.ApplicationContext.getFacade()  非封装了一层，减少了API数量
								        {
								        	return (this.facade); // this.facade = new ApplicationContextFacade(org.apache.catalina.core.ApplicationContext);
								        }
									}
							}
						}
					}
				}
			}
	 */
	}

}
