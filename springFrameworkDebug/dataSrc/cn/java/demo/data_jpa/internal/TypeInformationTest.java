package cn.java.demo.data_jpa.internal;

import java.util.List;

import org.springframework.data.repository.Repository;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

public class TypeInformationTest {

	public static interface UserRepository extends Repository<UserRepository, Integer> {

	}
	
	public static void printTypeInformation(Class repositoryInterface) {
		TypeInformation<?> information = ClassTypeInformation.from(repositoryInterface);
		List<TypeInformation<?>> arguments = information.getSuperTypeInformation(Repository.class).getTypeArguments(); // 泛型参数
		for (TypeInformation<?> typeInformation : arguments) {
			System.out.println("typeInformation.getType() = " + typeInformation.getType());
		}
	}
	
	public static void main(String[] args) {
		printTypeInformation(UserRepository.class);
	}
}
