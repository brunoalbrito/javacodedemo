package cn.note;

public class Element_tree {
/**
 从代码来看， Containe和Engine等价的。
 
 
 标签的层次结构关系是：
 <Server>一个
	 <GlobalNamingResources>一个 ##############
	 	<Ejb></Ejb>多个
	 	<Environment></Environment>多个
	 	<LocalEjb></LocalEjb>多个
	 	<Resource></Resource>多个
	 	<ResourceEnvRef></ResourceEnvRef>多个
	 	<ServiceRef></ServiceRef>多个
	 	<Transaction></Transaction>一个
	 </GlobalNamingResources>
	 
	 <Listener></Listener>多个 ##############
	 <Listener></Listener>
	 ....
	 
	 <Service>多个 ##############
	 	<Listener></Listener>多个
	 	<Listener></Listener>
	 	....
	 	
	 	<Executor></Executor>多个 //做广播用的？
	 	<Executor></Executor>
	 	....
	 	
	 	<Connector>多个##############
		 	<Listener></Listener>多个
		 	<Listener></Listener>
		 	....
	 	</Connector>
	 	<Connector>多个##############
		 	<Listener></Listener>多个
		 	<Listener></Listener>
		 	....
	 	</Connector>
	 	....
	 	
	 	<Engine>一个##############  从代码来看， Container和Engine等价的。
	 		<Cluster>一个
					<Manager></Manager>一个
					<Channel>一个
						<Membership></Membership>一个
						
						<Sender>一个
							<Transport></Transport>一个
						</Sender>
						
						<Receiver></Receiver>一个
						
						<Interceptor>多个
							<Member></Member>多个
							<Member></Member>
							....
						</Interceptor>
						<Interceptor></Interceptor>
						...
						
					</Channel>
					
					<Valve></Valve>多个
					<Valve></Valve>
					...
					
					<Deployer></Deployer>一个
					
					
					<Listener></Listener>多个
				 	<Listener></Listener>
				 	....
				 	
				 	<ClusterListener></ClusterListener>多个
				 	<ClusterListener></ClusterListener>
				 	....
			</Cluster>
	 		
	 		<Listener></Listener>多个
		 	<Listener></Listener>
		 	....

			<Realm>一个##############
				<Realm></Realm>多个
				<Realm></Realm>
				....
			</Realm>
			
			<Valve></Valve>多个
			<Valve></Valve>
			....
			
			<Host>多个##############
				<Alias></Alias>多个
				<Alias></Alias>
				...
				
				<Cluster>一个
					<Manager></Manager>一个
					<Channel>一个
						<Membership></Membership>一个
						
						<Sender>一个
							<Transport></Transport>一个
						</Sender>
						
						<Receiver></Receiver>一个
						
						<Interceptor>多个
							<Member></Member>多个
							<Member></Member>
							....
						</Interceptor>
						<Interceptor></Interceptor>
						...
						
					</Channel>
					
					<Valve></Valve>多个
					<Valve></Valve>
					...
					
					<Deployer></Deployer>一个
					
					
					<Listener></Listener>多个
				 	<Listener></Listener>
				 	....
				 	
				 	<ClusterListener></ClusterListener>多个
				 	<ClusterListener></ClusterListener>
				 	....
				</Cluster>
				
				<Listener></Listener>多个
			 	<Listener></Listener>
			 	....
			 	
			 	<Realm>一个##############
					<Realm></Realm>多个
					<Realm></Realm>
					....
				</Realm>

				<Valve></Valve>多个
				<Valve></Valve>
				....
				
				<Context configClass="org.apache.catalina.startup.ContextConfig" >多个
					(............<wrapper>........</wrapper>............)
					<InstanceListener></InstanceListener>多个
					<InstanceListener></InstanceListener>
					...
					<Listener></Listener>多个
					<Listener></Listener>
					...
					
					<Loader></Loader>一个
					
					<Manager>一个
						<Store></Store>一个
					</Manager>
					
					<Parameter></Parameter>多个
					<Parameter></Parameter>
					...
					
					<Realm>一个##############
						<Realm></Realm>多个
						<Realm></Realm>
						....
					</Realm>
					
					<Resources></Resources>一个
					
					<ResourceLink></ResourceLink>多个
					<ResourceLink></ResourceLink>
					...
					
					<Valve></Valve>多个
					<Valve></Valve>
					...
					
					<WatchedResource></WatchedResource>多个
					<WatchedResource></WatchedResource>
					...
					
					<WrapperLifecycle></WrapperLifecycle>多个
					<WrapperLifecycle></WrapperLifecycle>
					...
					
					<WrapperListener></WrapperListener>多个
					<WrapperListener></WrapperListener>
					...
					
					<Ejb></Ejb>多个
				 	<Environment></Environment>多个
				 	<LocalEjb></LocalEjb>多个
				 	<Resource></Resource>多个
				 	<ResourceEnvRef></ResourceEnvRef>多个
				 	<ServiceRef></ServiceRef>多个
				 	<Transaction></Transaction>一个
				</Context>
				....
				
				
			</Host>
			
			<Host></Host>##############
			....
	 	</Engine>
	 </Service>
	 <Service>
	 	
	 	....
	 </Service>
	 ....
 </Server>
 

 */
}
