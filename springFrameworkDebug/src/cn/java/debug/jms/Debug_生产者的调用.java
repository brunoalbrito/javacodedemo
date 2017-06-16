package cn.java.debug.jms;

public class Debug_生产者的调用 {
	/*
	 	-------------生产者的调用-------------------------
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
