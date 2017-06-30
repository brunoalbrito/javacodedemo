package cn.java.demo.data_common.application_listener;

import org.springframework.context.ApplicationListener;
import org.springframework.data.repository.init.RepositoriesPopulatedEvent;
import org.springframework.data.repository.init.RepositoryPopulator;
import org.springframework.data.repository.init.ResourceReaderRepositoryPopulator;
import org.springframework.data.repository.init.UnmarshallingResourceReader;
import org.springframework.data.repository.support.Repositories;

public class RepositoriesPopulatedEventApplicationListener implements ApplicationListener<RepositoriesPopulatedEvent> {

	@Override
	public void onApplicationEvent(RepositoriesPopulatedEvent event) {
		Repositories repositories = event.getRepositories();
		RepositoryPopulator repositoryPopulator = event.getSource();
		
		if(repositoryPopulator instanceof ResourceReaderRepositoryPopulator){
			
		}
		else if(repositoryPopulator instanceof UnmarshallingResourceReader){
			
		}
	}


}
