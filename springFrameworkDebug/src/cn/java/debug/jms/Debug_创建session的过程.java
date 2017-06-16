package cn.java.debug.jms;

public class Debug_创建session的过程 {
	/*
	 	--------------创建session的过程------------------------
	 	org.springframework.jms.core.JmsTemplate.execute(SessionCallback<T> action, boolean startConnection) 
	 	{
	 		Connection conToClose = null;
			Session sessionToClose = null;
	 		conToClose = org.springframework.jms.support.JmsAccessor.createConnection()
	 		{
				org.springframework.jms.connection.CachingConnectionFactory.createConnection()
				{
					org.springframework.jms.connection.SingleConnectionFactory.createConnection() 
					{
						return getSharedConnectionProxy(getConnection(){
							org.springframework.jms.connection.SingleConnectionFactory.initConnection()
							{
								this.connection = doCreateConnection();
								{
									ConnectionFactory cf = getTargetConnectionFactory(); // === org.apache.activemq.spring.ActiveMQConnectionFactory
									if (Boolean.FALSE.equals(this.pubSubMode) && cf instanceof QueueConnectionFactory) { // 队列模式
										return ((QueueConnectionFactory) cf).createQueueConnection();
									}
									else if (Boolean.TRUE.equals(this.pubSubMode) && cf instanceof TopicConnectionFactory) { // 订阅模式
										return ((TopicConnectionFactory) cf).createTopicConnection();
									}
									else {
										return getTargetConnectionFactory().createConnection();
										{
											org.apache.activemq.ActiveMQConnectionFactory.createConnection()
											{
												org.apache.activemq.ActiveMQConnectionFactory.createActiveMQConnection(String userName, String password)
												{
													ActiveMQConnection connection = null;
											        try {
											            Transport transport = createTransport();
											            {
											            	return TransportFactory.connect(brokerURL);
											            	{
											            		// location == tcp://localhost:61616
											            		TransportFactory tf = findTransportFactory(location);
											            		{
											            			String scheme = location.getScheme();
											            			tf = (TransportFactory)TRANSPORT_FACTORY_FINDER.newInstance(scheme);
											            			{
											            				// “META-INF/services/org/apache/activemq/transport/tcp”文件
											            				TransportFactory tf = org.apache.activemq.transport.tcp.TcpTransportFactory;
											            			}
											            		}
		        												return tf.doConnect(location); // location == tcp://localhost:61616
		        												{
		        													// “META-INF/services/org/apache/activemq/wireformat/default”文件
																 	URI location = new URI("tcp://localhost:61616");
																 	
																 	WireFormatFactory factory = new org.apache.activemq.openwire.OpenWireFormatFactory();
																 	factory.setHost(location.getHost())
																 	WireFormat format = factory.createWireFormat();
																 	
																 	SocketFactory socketFactory = SocketFactory.getDefault();
																 	Transport transport = new org.apache.activemq.transport.tcp.TcpTransport(format, socketFactory, location, null);
																 	
																 	{
																 		TcpTransport tcpTransport = (TcpTransport)transport.narrow(TcpTransport.class);
																 		tcpTransport.setSocketOptions(socketOptions);
																 		if (format instanceof org.apache.activemq.openwire.OpenWireFormat) {
																            transport = new org.apache.activemq.transport.WireFormatNegotiator(transport, (OpenWireFormat)format, tcpTransport.getMinmumWireFormatVersion());
																        }
																 	}
																 	
																 	transport = new org.apache.activemq.transport.MutexTransport(transport);
														        	transport = new org.apache.activemq.transport.ResponseCorrelator(transport);
		        													return transport;
		        												}
											            	}
											            }
											            connection = ActiveMQConnectionFactory.createActiveMQConnection(transport, factoryStats);
														{
															ActiveMQConnection connection = new org.apache.activemq.ActiveMQConnection(transport, getClientIdGenerator(),getConnectionIdGenerator(), factoryStats);
															return connection;
														}
											            connection.setUserName(userName);
											            connection.setPassword(password);
											
											            ActiveMQConnectionFactory.configureConnection(connection);
											            {
											            	connection.setPrefetchPolicy(getPrefetchPolicy());
													        connection.setDisableTimeStampsByDefault(isDisableTimeStampsByDefault());
													        connection.setOptimizedMessageDispatch(isOptimizedMessageDispatch());
													        ......
											            	if (transportListener != null) {
													            connection.addTransportListener(transportListener);
													        }
													        if (exceptionListener != null) {
													            connection.setExceptionListener(exceptionListener);
													        }
													        if (clientInternalExceptionListener != null) {
													            connection.setClientInternalExceptionListener(clientInternalExceptionListener);
													        }
											            }
											
											            transport.start(); // 开启事务 ------------------0
											
											            if (clientID != null) {
											                connection.setDefaultClientID(clientID);
											            }
											
											            return connection;
											        } catch (JMSException e) {
											        }
												
												}
											}
										
										}
									}
								
								}
								prepareConnection(this.connection);
							}
						});
					}
				}
	 		}
	 		sessionToClose = org.springframework.jms.support.JmsAccessor.createSession(conToClose) 
	 		{
	 			return con.createSession(isSessionTransacted(), getSessionAcknowledgeMode()); // ------------------1
	 		}
	 		sessionToUse = sessionToClose;
	 		return action.doInJms(sessionToUse); // 回调
	 	}
	 */
}
