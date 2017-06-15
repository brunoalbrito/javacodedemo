package cn.java.debug.jms;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import cn.java.demo.jmstag.message.EmailMessage;

public class Debug {

	/*
	 	org.springframework.jms.config.JmsNamespaceHandler
	 	{
	 		org.springframework.context.SmartLifecycle
			
			org.springframework.context.support.DefaultLifecycleProcessor
			org.springframework.context.LifecycleProcessor 自启动
	 	}
	 	
	 	org.springframework.context.support.AbstractApplicationContext.finishRefresh()



	 	https://my.oschina.net/zhzhenqin/blog/86586
	 	http://blog.csdn.net/moonsheep_liu/article/details/6684948
	 */
	public static void main(String[] args) {
		JmsTemplate jmsTemplate = new JmsTemplate();
		{
//			Destination destination = new ActiveMQTopic("activeMQTopicXX");
			Destination destination = new ActiveMQQueue("activeMQTopicXX");
			jmsTemplate.setDefaultDestination(destination); // 目标
			jmsTemplate.setMessageConverter(new SimpleMessageConverter()); // 消息转换器
			
			{
				
				CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
				connectionFactory.setSessionCacheSize(10);
				connectionFactory.setCacheProducers(false);
				{
					ActiveMQConnectionFactory targetConnectionFactory = new ActiveMQConnectionFactory();
					targetConnectionFactory.setBrokerURL("tcp://localhost:61616");
					targetConnectionFactory.setUserName("userName");
					targetConnectionFactory.setPassword("password");
					connectionFactory.setTargetConnectionFactory(targetConnectionFactory);
				}
				jmsTemplate.setConnectionFactory(connectionFactory); // 连接工厂
			}
			
		}
		jmsTemplate.convertAndSend("emailQueue",new EmailMessage("info@example.com", "Hello"));
	}
	
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
		-------------发送消息的过程-------------------------
		// 代码展开jmsTemplate.convertAndSend("emailQueue",new EmailMessage("info@example.com", "Hello"));
		execute(new SessionCallback<Object>() {
			@Override
			public Object doInJms(Session session) throws JMSException {
				// destinationName === "emailQueue"
				// message === cn.java.demo.jmstag.message.EmailMessage
				Destination destination = JmsDestinationAccessor.resolveDestinationName(session, destinationName); // 要有个创建Destination的过程
				{
					getDestinationResolver().resolveDestinationName(session, destinationName, isPubSubDomain());
					{
						org.springframework.jms.support.destination.DynamicDestinationResolver.resolveDestinationName(session, destinationName, isPubSubDomain());
						{
							if (pubSubDomain) {
								return resolveTopic(session, destinationName);
								{
									return session.createTopic(topicName); // ------------------2-case.0
								}
							}
							else {
								return resolveQueue(session, destinationName);
								{
									return session.createQueue(queueName); // ------------------2-case.1
								}
							}
						}
					}
				}
				Message message = getRequiredMessageConverter().toMessage(message, session); // -------3  消息转换，如session.createTextMessage(text);
				MessageProducer producer = JmsTemplate.createProducer(session, destination);
				{
					MessageProducer producer = doCreateProducer(session, destination);
					{
						return session.createProducer(destination); // ------------------4
					}
					if (!isMessageIdEnabled()) {
						producer.setDisableMessageID(true);
					}
					if (!isMessageTimestampEnabled()) {
						producer.setDisableMessageTimestamp(true);
					}
					return producer;
				}
				doSend(producer, message);
				{
					if (isExplicitQosEnabled()) {
						producer.send(message, getDeliveryMode(), getPriority(), getTimeToLive());
					}
					else {
						producer.send(message); // ------------------5
					}
				}
				// Check commit - avoid commit call within a JTA transaction.
				if (session.getTransacted() && isSessionLocallyTransacted(session)) {
					// Transacted session created by this template -> commit.
					JmsUtils.commitIfNecessary(session); // 提交事务 ------------------6
					{
						try {
							session.commit();
						}
					}
				}
				return null;
			}
		}, false);
	 	-------------发送消息的过程-------------------------
	 	// 代码展开jmsTemplate.convertAndSend(new(),new EmailMessage("info@example.com", "Hello"));
		execute(new SessionCallback<Object>() {
			@Override
			public Object doInJms(Session session) throws JMSException {
				// destination === org.apache.activemq.command.ActiveMQTopic
				// message === cn.java.demo.jmstag.message.EmailMessage
				MessageProducer producer = createProducer(session, destination);
				{
					MessageProducer producer = doCreateProducer(session, destination);
					{
						return session.createProducer(destination);
					}
					if (!isMessageIdEnabled()) {
						producer.setDisableMessageID(true);
					}
					if (!isMessageTimestampEnabled()) {
						producer.setDisableMessageTimestamp(true);
					}
					return producer;
				}
				//  org.springframework.jms.support.converter.SimpleMessageConverter.toMessage(message, session);
				Message message = getRequiredMessageConverter().toMessage(message, session); // 消息转换，如session.createTextMessage(text);
				
				doSend(producer, message);
				{
					if (isExplicitQosEnabled()) {
						producer.send(message, getDeliveryMode(), getPriority(), getTimeToLive());
					}
					else {
						producer.send(message);
					}
				}
				
				// Check commit - avoid commit call within a JTA transaction.
				if (session.getTransacted() && isSessionLocallyTransacted(session)) {
					// Transacted session created by this template -> commit.
					JmsUtils.commitIfNecessary(session);
					{
						try {
							session.commit();
						}
					}
				}
				return null;
			}
		}, false);

	 	
	 	--------------------------------------
	 	tcp://localhost:61616 {
			// “META-INF/services/org/apache/activemq/transport/tcp”文件{
				TransportFactory tf = org.apache.activemq.transport.tcp.TcpTransportFactory;
				Transport transport = tf.doConnect("tcp://localhost:61616");
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
			 	}
			 	
				{
					connection = createActiveMQConnection(transport, factoryStats);
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
		            transport.start();
				}
			}
	 	}
	 	
	 	
	 	stomp://localhost:123 {
			“META-INF/services/org/apache/activemq/transport/stomp”文件{
				TransportFactory tf = org.apache.activemq.transport.stomp.StompTransportFactory;
				Transport transport = tf.doConnect("tcp://localhost:61616");
				{
					connection = createActiveMQConnection(transport, factoryStats);
		            connection.setUserName(userName);
		            connection.setPassword(password);
		            configureConnection(connection);
		            transport.start();
				}
			}
	 	}
	 	
	 	amqp://localhost:123 {
			文件META-INF/services/org/apache/activemq/transport/amqp 中{
				TransportFactory tf = org.apache.activemq.transport.amqp.AmqpTransportFactory;
				Transport transport = tf.doConnect("tcp://localhost:61616");
				{
					connection = createActiveMQConnection(transport, factoryStats);
		            connection.setUserName(userName);
		            connection.setPassword(password);
		            configureConnection(connection);
		            transport.start();
				}
			}
	 	}
	 	ws://localhost:123 {
			文件META-INF/services/org/apache/activemq/transport/ws 中{
				TransportFactory tf = org.apache.activemq.transport.ws.WSTransportFactory;
				Transport transport = tf.doConnect("tcp://localhost:61616");
				{
					connection = createActiveMQConnection(transport, factoryStats);
		            connection.setUserName(userName);
		            connection.setPassword(password);
		            configureConnection(connection);
		            transport.start();
				}
			}
	 	}
		
	 */

}
