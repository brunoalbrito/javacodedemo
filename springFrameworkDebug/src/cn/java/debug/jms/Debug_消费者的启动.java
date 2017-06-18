package cn.java.debug.jms;

import java.lang.reflect.InvocationTargetException;

import javax.jms.Destination;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.JmsResponse;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.listener.adapter.ReplyFailureException;
import org.springframework.jms.support.JmsUtils;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.util.MethodInvoker;

import cn.java.demo.jmstag.activemq.byxml.consumer.QueueConsumer;

public class Debug_消费者的启动 {
	{
		/**
		 	<jms:listener-container destination-type="queue" response-destination-type="queue" message-converter="simpleMessageConverter0" 
				connection-factory="cachingConnectionFactory0" container-class="org.springframework.jms.listener.SimpleMessageListenerContainer">
				<jms:listener ref="queueConsumer" method="consumerDefaultQueueMethod" destination="defaultQuqueName0" />
			</jms:listener-container>
		 */
		
		// 消息转换器
		SimpleMessageConverter messageConverter = new SimpleMessageConverter();
		
		// 连接工厂
		ActiveMQConnectionFactory targetConnectionFactory = new ActiveMQConnectionFactory();
		targetConnectionFactory.setBrokerURL("tcp://localhost:61616");
		targetConnectionFactory.setUserName("userName");
		targetConnectionFactory.setPassword("password");
		
		// 连接缓存
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setTargetConnectionFactory(targetConnectionFactory);
		
		// 通知给谁
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter();
		messageListenerAdapter.setDelegate(new QueueConsumer());
		messageListenerAdapter.setDefaultListenerMethod("consumerDefaultQueueMethod");
		messageListenerAdapter.setMessageConverter(messageConverter);
		
		// 监听器容器，DefaultMessageListenerContainer、SimpleMessageListenerContainer
		SimpleMessageListenerContainer defaultMessageListenerContainer = new SimpleMessageListenerContainer(); // 实现了SmartLifecycle接口
		defaultMessageListenerContainer.setPubSubDomain(false);
		defaultMessageListenerContainer.setSubscriptionDurable(false);
		defaultMessageListenerContainer.setSubscriptionShared(false);
		defaultMessageListenerContainer.setReplyPubSubDomain(false);
		defaultMessageListenerContainer.setMessageConverter(messageConverter);
		defaultMessageListenerContainer.setMessageListener(messageListenerAdapter);
		defaultMessageListenerContainer.setConnectionFactory(connectionFactory);
		defaultMessageListenerContainer.setDestinationName("defaultQuqueName0");
	}
	
	/*
		org.springframework.jms.listener.AbstractJmsListeningContainer.start()
		{
			org.springframework.jms.listener.SimpleMessageListenerContainer.doStart()
			{
				org.springframework.jms.listener.AbstractJmsListeningContainer.doStart()
				{
					// Lazily establish a shared Connection, if necessary.
					if (sharedConnectionEnabled()) { // true
						establishSharedConnection();
						{
							org.springframework.jms.listener.AbstractJmsListeningContainer.establishSharedConnection()
							{
								synchronized (this.sharedConnectionMonitor) {
									if (this.sharedConnection == null) {
										this.sharedConnection = createSharedConnection(); // 创建连接
										{
											Connection con = createConnection();
											{
												return org.springframework.jms.support.JmsAccessor.createConnection() 
												{
													return getConnectionFactory().createConnection();
												}
											}
											try {
												SimpleMessageListenerContainer.prepareSharedConnection(con);
												{
													AbstractJmsListeningContainer.prepareSharedConnection(connection);
													{
														String clientId = getClientId();
														if (clientId != null) {
															connection.setClientID(clientId);
														}
													}
													connection.setExceptionListener(this);
												}
												return con;
											}
											catch (JMSException ex) {
												JmsUtils.closeConnection(con);
												throw ex;
											}
										}
									}
								}
							}
						}
					}
			
					// Reschedule paused tasks, if any.
					synchronized (this.lifecycleMonitor) {
						this.running = true;
						this.lifecycleMonitor.notifyAll();
						resumePausedTasks();
						{
							org.springframework.jms.listener.AbstractJmsListeningContainer.resumePausedTasks()
							{
								synchronized (this.lifecycleMonitor) {
									if (!this.pausedTasks.isEmpty()) {
										for (Iterator<?> it = this.pausedTasks.iterator(); it.hasNext();) {
											Object task = it.next();
											try {
												doRescheduleTask(task);
												{
													throw new UnsupportedOperationException(ClassUtils.getShortName(getClass()) + " does not support rescheduling of tasks");
												}
												it.remove();
											}
											catch (RuntimeException ex) {
												logRejectedTask(task, ex);
												// Keep the task in paused mode...
											}
										}
									}
								}
							}
						}
					}
			
					// Start the shared Connection, if any.
					if (sharedConnectionEnabled()) { // true
						startSharedConnection();
						{
							org.springframework.jms.listener.AbstractJmsListeningContainer.startSharedConnection()
							{
								synchronized (this.sharedConnectionMonitor) {
									this.sharedConnectionStarted = true;
									if (this.sharedConnection != null) {
										try {
											this.sharedConnection.start(); // 开启连接
										}
									}
								}
							}
						}
					}
				}
			}
			
			// 初始化消费者
			org.springframework.jms.listener.SimpleMessageListenerContainer.initializeConsumers()
			{
				synchronized (this.consumersMonitor) {
					if (this.consumers == null) {
						this.sessions = new HashSet<Session>(this.concurrentConsumers); // concurrentConsumers == 1
						this.consumers = new HashSet<MessageConsumer>(this.concurrentConsumers);  // concurrentConsumers == 1
						Connection con = getSharedConnection(); //sharedConnection
						for (int i = 0; i < this.concurrentConsumers; i++) {
							Session session = createSession(con); // 创建session
							{
								return org.springframework.jms.support.JmsAccessor.createSession(Connection con)
							}
							MessageConsumer consumer = createListenerConsumer(session);
							{
								Destination destination = getDestination(); // 消费“哪个目标”
								{
									org.springframework.jms.listener.AbstractMessageListenerContainer.getDestination()
									{
										return (this.destination instanceof Destination ? (Destination) this.destination : null);
									}
								}
								if (destination == null) {
									destination = resolveDestinationName(session, getDestinationName());
									{
										org.springframework.jms.support.destination.JmsDestinationAccessor.resolveDestinationName(Session session, String destinationName) 
										{
											return getDestinationResolver().resolveDestinationName(session, destinationName, isPubSubDomain());
										}
									}
								}
								
								MessageConsumer consumer = createConsumer(session, destination);
								{
									if (isPubSubDomain() && destination instanceof Topic) { // 消费模式
										if (isSubscriptionShared()) {
											// createSharedConsumer((Topic) dest, subscription, selector);
											// createSharedDurableConsumer((Topic) dest, subscription, selector);
											Method method = (isSubscriptionDurable() ?
													createSharedDurableConsumerMethod : createSharedConsumerMethod);
											try {
												return (MessageConsumer) method.invoke(session, destination, getSubscriptionName(), getMessageSelector());
											}
										}
										else if (isSubscriptionDurable()) {
											return session.createDurableSubscriber(
													(Topic) destination, getSubscriptionName(), getMessageSelector(), isPubSubNoLocal());
										}
										else {
											// Only pass in the NoLocal flag in case of a Topic (pub-sub mode):
											// Some JMS providers, such as WebSphere MQ 6.0, throw IllegalStateException
											// in case of the NoLocal flag being specified for a Queue.
											return session.createConsumer(destination, getMessageSelector(), isPubSubNoLocal());
										}
									}
									else {
										return session.createConsumer(destination, getMessageSelector()); // 创建消费者
									}
								}
						
								if (this.taskExecutor != null) {
									consumer.setMessageListener(new MessageListener() { // 注册监听器
										@Override
										public void onMessage(final Message message) {
											taskExecutor.execute(new Runnable() { // 使用taskExecutor执行
												@Override
												public void run() {
													processMessage(message, session);
												}
											});
										}
									});
								}
								else {
									consumer.setMessageListener(new MessageListener() { // 注册监听器
										@Override
										public void onMessage(Message message) { // 不使用taskExecutor执行
											processMessage(message, session);
											{
												org.springframework.jms.listener.SimpleMessageListenerContainer.processMessage(Message message, Session session)
												{
													boolean exposeResource = isExposeListenerSession();
													if (exposeResource) {
														TransactionSynchronizationManager.bindResource(
																getConnectionFactory(), new LocallyExposedJmsResourceHolder(session));
													}
													try {
														AbstractMessageListenerContainer.executeListener(session, message);
														{
															org.springframework.jms.listener.AbstractMessageListenerContainer.executeListener(Session session, Message message)
															{
																AbstractMessageListenerContainer.doExecuteListener(session, message);
																{
																	if (!isAcceptMessagesWhileStopping() && !isRunning()) {
																		rollbackIfNecessary(session); // 回滚，如果需要
																		throw new MessageRejectedWhileStoppingException();
																	}
															
																	try {
																		AbstractMessageListenerContainer.invokeListener(session, message);
																		{
																			// listener === org.springframework.jms.listener.adapter.MessageListenerAdapter
																			Object listener = AbstractMessageListenerContainer.getMessageListener();
																			if (listener instanceof SessionAwareMessageListener) {
																				doInvokeListener((SessionAwareMessageListener) listener, session, message); // !!!!
																				{
																					Connection conToClose = null;
																					Session sessionToClose = null;
																					try {
																						Session sessionToUse = session;
																						if (!isExposeListenerSession()) {
																							// We need to expose a separate Session.
																							conToClose = createConnection(); // 创建连接
																							sessionToClose = createSession(conToClose); // 创建session
																							sessionToUse = sessionToClose;
																						}
																						// Actually invoke the message listener...
																						// listener === org.springframework.jms.listener.adapter.MessageListenerAdapter
																						listener.onMessage(message, sessionToUse); // 调用监听器方法
																						{
																							org.springframework.jms.listener.adapter.MessageListenerAdapter.onMessage(Message message, Session session)
																							{
																								// Check whether the delegate is a MessageListener impl itself.
																								// In that case, the adapter will simply act as a pass-through.
																								Object delegate = getDelegate(); // !!! cn.java.demo.jmstag.byxml.consumer.QueueConsumer
																								if (delegate != this) {
																									if (delegate instanceof SessionAwareMessageListener) {
																										if (session != null) {
																											((SessionAwareMessageListener<Message>) delegate).onMessage(message, session);
																											return;
																										}
																										else if (!(delegate instanceof MessageListener)) {
																											throw new javax.jms.IllegalStateException("MessageListenerAdapter cannot handle a " +
																													"SessionAwareMessageListener delegate if it hasn't been invoked with a Session itself");
																										}
																									}
																									if (delegate instanceof MessageListener) {
																										((MessageListener) delegate).onMessage(message);
																										return;
																									}
																								}
																						
																								// Regular case: find a handler method reflectively.
																								Object convertedMessage = extractMessage(message);
																								{
																									try {
																										MessageConverter converter = getMessageConverter();
																										if (converter != null) {
																											return converter.fromMessage(message); // 使用“消息转换器”转换消息
																										}
																										return message;
																									}
																									catch (JMSException ex) {
																										throw new MessageConversionException("Could not convert JMS message", ex);
																									}
																								}
																								String methodName = getListenerMethodName(message, convertedMessage); // 获取“监听器方法”
																								{
																									org.springframework.jms.listener.adapter.MessageListenerAdapter.getDefaultListenerMethod()
																									{
																										return this.defaultListenerMethod; // "consumerDefaultQueueMethod"
																									}
																								}
																								if (methodName == null) {
																									throw new javax.jms.IllegalStateException("No default listener method specified: " +
																											"Either specify a non-null value for the 'defaultListenerMethod' property or " +
																											"override the 'getListenerMethodName' method.");
																								}
																						
																								// Invoke the handler method with appropriate arguments.
																								Object[] listenerArguments = buildListenerArguments(convertedMessage); // 创建“监听器方法”参数
																								{
																									return new Object[] {extractedMessage};
																								}
																								
																								Object result = invokeListenerMethod(methodName, listenerArguments); // 调用“监听器方法”
																								{
																									try {
																										MethodInvoker methodInvoker = new MethodInvoker();
																										methodInvoker.setTargetObject(getDelegate());
																										methodInvoker.setTargetMethod(methodName);
																										methodInvoker.setArguments(arguments);
																										methodInvoker.prepare();
																										return methodInvoker.invoke();
																									}
																									catch (InvocationTargetException ex) {
																									}
																								}
																								
																								if (result != null) { // !!! 如果有返回值
																									handleResult(result, message, session); // 处理返回值
																									{
																										if (session != null) {
																											try {
																												// request === message
																												Message response = buildMessage(session, result); // 创建消息
																												{
																													Object content = preProcessResponse(result instanceof JmsResponse
																															? ((JmsResponse<?>) result).getResponse() : result);
																											
																													MessageConverter converter = getMessageConverter(); // 转换消息
																													if (converter != null) {
																														if (content instanceof org.springframework.messaging.Message) {
																															return this.messagingMessageConverter.toMessage(content, session);
																														}
																														else {
																															return converter.toMessage(content, session);
																														}
																													}
																											
																													if (!(content instanceof Message)) {
																														throw new MessageConversionException(
																																"No MessageConverter specified - cannot handle message [" + content + "]");
																													}
																													return (Message) content;
																												}
																												postProcessResponse(request, response);
																												{
																													String correlation = request.getJMSCorrelationID();
																													if (correlation == null) {
																														correlation = request.getJMSMessageID();
																													}
																													response.setJMSCorrelationID(correlation);
																												}
																												Destination destination = getResponseDestination(request, response, session, result);// 目标对象
																												{
																													if (result instanceof JmsResponse) {
																														JmsResponse<?> jmsResponse = (JmsResponse) result;
																														Destination destination = jmsResponse.resolveDestination(getDestinationResolver(), session);
																														if (destination != null) {
																															return destination;
																														}
																													}
																													return getResponseDestination(request, response, session);
																													{
																														Destination replyTo = request.getJMSReplyTo();
																														if (replyTo == null) {
																															replyTo = resolveDefaultResponseDestination(session);
																															if (replyTo == null) {
																																throw new InvalidDestinationException("Cannot determine response destination: " +
																																		"Request message does not contain reply-to destination, and no default response destination set.");
																															}
																														}
																														return replyTo;
																													}
																												}
																												sendResponse(session, destination, response);// 把返回结果发送到新的队列
																												{
																													MessageProducer producer = session.createProducer(destination); // 创建“生产者”
																													try {
																														postProcessProducer(producer, response); // 空方法
																														producer.send(response); // 发送数据
																													}
																													finally {
																														JmsUtils.closeMessageProducer(producer);
																													}
																												}
																											}
																											catch (Exception ex) {
																												throw new ReplyFailureException("Failed to send reply with payload [" + result + "]", ex);
																											}
																										}
																								
																										else {
																											// No JMS Session available
																											if (logger.isWarnEnabled()) {
																												logger.warn("Listener method returned result [" + result +
																														"]: not generating response message for it because of no JMS Session given");
																											}
																										}
																									}
																								}
																								else {
																									logger.trace("No result object given - no result to handle");
																								}
																							}
																						}
																						// Clean up specially exposed Session, if any.
																						if (sessionToUse != session) {
																							if (sessionToUse.getTransacted() && isSessionLocallyTransacted(sessionToUse)) {
																								// Transacted session created by this container -> commit.
																								JmsUtils.commitIfNecessary(sessionToUse); // 提交事务，如果需要
																							}
																						}
																					}
																					finally {
																						JmsUtils.closeSession(sessionToClose);
																						JmsUtils.closeConnection(conToClose);
																					}
																				}
																			}
																			else if (listener instanceof MessageListener) {
																				doInvokeListener((MessageListener) listener, message);
																			}
																			else if (listener != null) {
																				throw new IllegalArgumentException(
																						"Only MessageListener and SessionAwareMessageListener supported: " + listener);
																			}
																			else {
																				throw new IllegalStateException("No message listener specified - see property 'messageListener'");
																			}
																		}
																	}
																	commitIfNecessary(session, message);
																}
															}
														}
													}
													finally {
														if (exposeResource) {
															TransactionSynchronizationManager.unbindResource(getConnectionFactory());
														}
													}
												}
											}
										}
									});
								}
								return consumer;
							}
							this.sessions.add(session);
							this.consumers.add(consumer);
						}
					}
				}
			}
		}
		
	 */

}
