package cn.java.internal.typecontributor;

import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.metamodel.spi.TypeContributions;
import org.hibernate.metamodel.spi.TypeContributor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.BasicType;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.UserType;

public class TypeContributorImpl/* implements TypeContributor */{

	public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
		if(serviceRegistry instanceof StandardServiceRegistryImpl){
			
		}
		/*
		 	注册类型
			typeContributions.contributeType(BasicType type);
			typeContributions.contributeType(UserType type, String[] keys);
			typeContributions.contributeTypecontributeType(CompositeUserType type, String[] keys)
		 */
	}

}
