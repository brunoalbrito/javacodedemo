package cn.java.debug;

public class Debug {

	public static void main(String[] args) {

		/*
		 	获取连接池的过程：
			 	轮询“创建连接池”的任务
			 	{
			 		com.mchange.v2.async.ThreadPoolAsynchronousRunner是创建连接池异步的异步处理器
			 	}
			 		
			 	发放“创建连接池”的任务
			 	{
			 		当要获取连接池的时候，会给ThreadPoolAsynchronousRunner对的pendingTasks属性放入一个任务，
			 			ThreadPoolAsynchronousRunner会定时轮询pendingTasks是否有待处理的任务，有的话就进行处理（即创建连接池）
			 	}
			 		
			 	-------------轮询“创建连接池”的任务-------------------
			 	com.mchange.v2.async.ThreadPoolAsynchronousRunner {
			 		private ThreadPoolAsynchronousRunner(...){
			 			recreateThreadsAndTasks(); // !!!! 轮询pendingTasks接受任务 - 创建连接池
			 			{
			 				for (int i = 0; i < num_threads; ++i)
							{
								Thread t = new PoolThread(i, daemon); // 轮询pendingTasks接受任务 - 创建连接池
								{
									public void run()
									{
										thread_loop:
											while (true)
											{
											
												Runnable myTask;
												synchronized ( ThreadPoolAsynchronousRunner.this )
												{
													while ( !should_stop && pendingTasks.size() == 0 )
														ThreadPoolAsynchronousRunner.this.wait( POLL_FOR_STOP_INTERVAL ); // 异步获取
													if (should_stop) 
														break thread_loop;
						
													if (! available.remove( this ) )
														throw new InternalError("An unavailable PoolThread tried to check itself out!!!");
													myTask = (Runnable) pendingTasks.remove(0);
													currentTask = myTask;
												}
												try
												{ 
													if (max_individual_task_time > 0)
														setMaxIndividualTaskTimeEnforcer();
													myTask.run(); // !!!! 会创建连接，并把连接放入连接池
												}
											}
									}
								}
								managed.add( t );
								available.add( t );
								t.start();  // 启动线程
							}
			 			}
			 		}
			 	}
			 	
			 	-------------发放“创建连接池”的任务-------------------
			 	com.mchange.v2.c3p0.impl.C3P0PooledConnectionPoolManager.getPool()
			 	{
			 		com.mchange.v2.c3p0.impl.C3P0PooledConnectionPoolManager.getPool(DbAuth auth)
			 		{
				 		C3P0PooledConnectionPool out = (C3P0PooledConnectionPool) authsToPools.get(auth);
						if (out == null)
						{
							out = createPooledConnectionPool(auth); // !!!! com.mchange.v2.c3p0.impl.C3P0PooledConnectionPool
							{
								// 创建连接池
								C3P0PooledConnectionPool out =  new C3P0PooledConnectionPool( cpds,...);
								{
									synchronized (fact)
									{
										fact.setMin( min );
										...
										// com.mchange.v2.resourcepool.BasicResourcePoolFactory
										rp = fact.createPool( manager ); // 发放“创建连接池”任务
										{
											ResourcePool child = new com.mchange.v2.resourcepool.BasicResourcePool( mgr, ...);
											{
												BasicResourcePool.ensureStartResources();  // 发放“创建连接池”任务
												{
													BasicResourcePool.recheckResizePool();  // 发放“创建连接池”任务
													{
														BasicResourcePool._recheckResizePool();   // 发放“创建连接池”任务
														{
															if ((shrink_count = msz - pending_removes - target_pool_size) > 0)
																shrinkPool( shrink_count );
															else if ((expand_count = target_pool_size - (msz + pending_acquires)) > 0)
																expandPool( expand_count );  // 发放“创建连接池”任务
																{
																	assert Thread.holdsLock(this);
																	if ( USE_SCATTERED_ACQUIRE_TASK )
																	{
																		// taskRunner === com.mchange.v2.async.ThreadPoolAsynchronousRunner
																		for (int i = 0; i < count; ++i)
																			taskRunner.postRunnable( new ScatteredAcquireTask(){  // 发放“创建连接池”任务
																				int attempts_remaining;  // 尝试获取次数
																				
																				public void run()
																				{
																					try
																					{
																						BasicResourcePool.this.doAcquireAndDecrementPendingAcquiresWithinLockOnSuccess(); // 会创建连接，并把连接放入连接池
																						{
																							BasicResourcePool.doAcquire( DECREMENT_ON_SUCCESS );//!!!
																							{
																								// 获取数据库连接
																								// mgr === com.mchange.v2.c3p0.impl.C3P0PooledConnectionPool.C3P0PooledConnectionPool(...).PooledConnectionResourcePoolManager
																								Object resc = mgr.acquireResource(); //note we acquire the resource while we DO NOT hold the pool's lock!
																								{
																									public Object acquireResource() throws Exception
																									{
																									 	if ( connectionCustomizer == null)
																										{
																											out = (auth.equals( C3P0ImplUtils.NULL_AUTH ) ? cpds.getPooledConnection() :
																														cpds.getPooledConnection( auth.getUser(), auth.getPassword() ) );
																											// cpds === com.mchange.v2.c3p0.WrapperConnectionPoolDataSource
																											 WrapperConnectionPoolDataSource.getPooledConnection( auth.getUser(), auth.getPassword() )
																											 {
																											 	this.getPooledConnection( user, password, null, null );
																											 	{
																											 		// nds === com.mchange.v2.c3p0.DriverManagerDataSource
																													DataSource nds = getNestedDataSource();
																													Connection conn = null;
																													conn = nds.getConnection(user, password);
																													{
																														ensureDriverLoaded();
																														{
																															if (! isDriverClassLoaded())
																												            {
																												                if (driverClass != null)
																												                    Class.forName( driverClass ); // 加载驱动
																												                setDriverClassLoaded( true );
																												            }
																														}
	
																												        Driver driver = driver();
																												        {
																												        	driver = (Driver) Class.forName( driverClass ).newInstance();
																												        	return driver;
																												        }
																												        Connection out = driver.connect( jdbcUrl, properties );  // 连接数据库 
																													}
																													
																													if ( this.isUsesTraditionalReflectiveProxies( user ) )
																													{
																														//return new C3P0PooledConnection( new com.mchange.v2.c3p0.test.CloseReportingConnection( conn ), 
																														return new C3P0PooledConnection( conn,
																																connectionTester,
																																this.isAutoCommitOnClose( user ), 
																																this.isForceIgnoreUnresolvedTransactions( user ),
																																cc,
																																pdsIdt);
																													}
																													else
																													{
																														return new NewPooledConnection( conn, 
																																connectionTester,
																																this.isAutoCommitOnClose( user ), 
																																this.isForceIgnoreUnresolvedTransactions( user ),
																																this.getPreferredTestQuery( user ),
																																cc,
																																pdsIdt); 
																													}
																												}
																											 }
																										}
																									}
																								}
																								synchronized(this) //assimilate resc if we do need it
																								{
																									try
																									{
																										msz = managed.size();
																										if (!broken && msz < target_pool_size)
																											assimilateResource(resc); // 把连接放入连接池
																											{
																												managed.put(resc, new PunchCard());
																												unused.add(0, resc);  // 把连接放入连接池
																											}
																										else
																											destroy = true;
																						
																										if (decrement_policy == DECREMENT_ON_SUCCESS)
																											_decrementPendingAcquires();
																									}
																									finally
																									{
																										if (decrement_policy == DECREMENT_WITH_CERTAINTY)
																											_decrementPendingAcquires();
																									}
																								}
																							}
																						}
																					}
																					catch (Exception e)
																					{
																						if (attempts_remaining == 0) //last try in a round... 尝试次数为0
																						{
																							// ... 失败处理机制
																						}
																						else
																						{
																							TimerTask doNextAcquire = new TimerTask()
																							{
																								public void run()
																								{ 
																									taskRunner.postRunnable( new ScatteredAcquireTask( attempts_remaining - 1, false ) ); // 重新发放任务，尝试次数 - 1
																								}
																							};
																							cullAndIdleRefurbishTimer.schedule( doNextAcquire, acq_attempt_delay );
																						}
																					}
																				}
																			});
																	}
																	else
																	{
																		for (int i = 0; i < count; ++i)
																			taskRunner.postRunnable( new AcquireTask() );
																	}
																}
														}
													}
												}
											}
											liveChildren.add( child );
											return child;
										}
									}
								}
								return out;
							}
							authsToPools.put( auth, out );
						}
			 		}
			 	}
			 	
			 	-------------“获取数据库连接”-------------------
			 	com.mchange.v2.c3p0.ComboPooledDataSource.getConnection() 
			 	{
				 	Connection com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource.getConnection() 
				 	{
				 		// com.mchange.v2.c3p0.impl.C3P0PooledConnectionPool.checkoutPooledConnection();
				 		PooledConnection pc = getPoolManager().getPool().checkoutPooledConnection();
				 		{
				 			PooledConnection pc = (PooledConnection) C3P0PooledConnectionPool.checkoutAndMarkConnectionInUse();  // !!! 获取连接，并标记为被使用中
				 			{
				 				Object out = null; 
								boolean success = false;
								while (! success)
								{
									try
									{
										// rp === com.mchange.v2.resourcepool.BasicResourcePool
										out = rp.checkoutResource( checkoutTimeout ); // !!! 获取连接
										{
											return BasicResourcePool.checkoutResource( 0 ); // !!!!
											{
												// !!! timeout === 0
												Object resc = BasicResourcePool.prelimCheckoutResource( timeout );
												{
													int available = unused.size(); // 未被使用的数量
													if (available == 0) // 没有可获取的连接
													{
														awaitAvailable(timeout); //throws timeout exception
													}
													Object  resc = unused.get(0); // 获取一个资源
													unused.remove(0);
													return resc;
												}

											}
										}
										if (out instanceof AbstractC3P0PooledConnection)
										{
											// cast should succeed, because effectiveStatementCache implies c3p0 pooled Connections
											AbstractC3P0PooledConnection acpc = (AbstractC3P0PooledConnection) out;
											Connection physicalConnection = acpc.getPhysicalConnection(); // !!!
											success = tryMarkPhysicalConnectionInUse(physicalConnection);
										}
										else
											success = true; //we don't pool statements from non-c3p0 PooledConnections
									}
								}
								return out;
				 			}
				 			// cl === com.mchange.v2.c3p0.impl.C3P0PooledConnectionPool.ConnectionEventListenerImpl
							pc.addConnectionEventListener( cl ); // 添加监听器，在回收“关闭”连接的时候有用
							return pc;
				 		}
				 		// pc === com.mchange.v2.c3p0.impl.C3P0PooledConnection
	       				return pc.getConnection();
				 	}
			 	}
			 	
			 	-------------“关闭数据库连接”-------------------
			 	com.mchange.v2.c3p0.impl.C3P0PooledConnection.ProxyConnectionInvocationHandler.invoke(Object proxy, Method m, Object[] args)
			 	{
			 		try
					{
						String mname = m.getName(); // 调用方法名
						if (activeConnection != null)
						{
							...
							
							else if ( mname.equals("close") )//!!! 关闭连接
							{
								//the PooledConnection doesn't have to be okay
		
								Exception e = doSilentClose( proxy, false, this.txn_known_resolved );
								if (! connection_error_signaled)
									ces.fireConnectionClosed(); // !!! 以“触发事件的方式”把连接重新放回“连接池”
									{
										com.mchange.v2.c3p0.util.ConnectionEventSupport.fireConnectionClosed()
										{
											Set mlCopy;
											synchronized (this) {
												mlCopy = (Set) mlisteners.clone(); // 克隆事件列表
											}
											// source === com.mchange.v2.c3p0.impl.C3P0PooledConnection
											ConnectionEvent evt = new ConnectionEvent(source);
											for (Iterator i = mlCopy.iterator(); i.hasNext();) { // 迭代事件列表
												ConnectionEventListener cl = (ConnectionEventListener) i.next();
												cl.connectionClosed(evt); // 触发关闭事件
												{
													如：com.mchange.v2.c3p0.impl.C3P0PooledConnectionPool.ConnectionEventListenerImpl.connectionClosed(ConnectionEvent evt)
													{
														doCheckinResource( evt );
														{
															//rp.checkinResource( evt.getSource() ); 
															checkinPooledConnection( (PooledConnection) evt.getSource() );  // !!! 把连接重新放回“连接池”
															{
																 com.mchange.v2.c3p0.impl.C3P0PooledConnectionPool.checkinPooledConnection(PooledConnection pcon)
																 {
																 	pcon.removeConnectionEventListener( cl );
																	unmarkConnectionInUseAndCheckin( pcon ); 
																	{
																		try
																		{
																			AbstractC3P0PooledConnection acpc = (AbstractC3P0PooledConnection) pcon;
																			Connection physicalConnection = acpc.getPhysicalConnection();
																			unmarkPhysicalConnectionInUse(physicalConnection);
																		}
																		// rp === com.mchange.v2.resourcepool.BasicResourcePool
																		rp.checkinResource(pcon);
																		{
																			if ( unlocked_do_checkin_managed ) doCheckinManaged( resc );
																			{
																				com.mchange.v2.resourcepool.BasicResourcePool.doCheckinManaged(Object resc) 
																				{
																					class RefurbishCheckinResourceTask implements Runnable
																					{
																						public void run()
																						{
																							unused.add(0,  resc ); // 放入未使用容器
																						}
																					}
																					Runnable doMe = new RefurbishCheckinResourceTask();
																					if ( force_synchronous_checkins ) doMe.run();
																					else taskRunner.postRunnable( doMe );
																				}
																			}
																		}
																	}
																 }
															}
														}
													}

												}
											}
										}
									}
								//System.err.println("close() called on a ProxyConnection.");
								if (e != null)
								{
									// 					    System.err.print("user close exception -- ");
									// 					    e.printStackTrace();
									throw e;
								}
								else
									return null;
							}	
							
							...
					 	}
			 		}
			 		catch (InvocationTargetException e)
					{
						...
					}
		 */
	}

}
