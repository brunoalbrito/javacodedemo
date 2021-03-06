/*
 * This class autogenerated by com.mchange.v2.c3p0.codegen.BeangenDataSourceGenerator.
 * Tue Dec 08 22:06:06 PST 2015
 * DO NOT HAND EDIT!
 */

package com.mchange.v2.c3p0.impl;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.cfg.C3P0Config;
import com.mchange.v2.c3p0.impl.C3P0ImplUtils;
import com.mchange.v2.naming.JavaBeanObjectFactory;
import com.mchange.v2.naming.JavaBeanReferenceMaker;
import com.mchange.v2.naming.ReferenceIndirector;
import com.mchange.v2.naming.ReferenceMaker;
import com.mchange.v2.ser.IndirectlySerialized;
import com.mchange.v2.ser.Indirector;
import com.mchange.v2.ser.SerializableUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

/**
 * This class was generated by com.mchange.v2.c3p0.codegen.BeangenDataSourceGenerator.
 */
public class PoolBackedDataSourceBase extends IdentityTokenResolvable implements Referenceable, Serializable
{
	protected PropertyChangeSupport pcs = new PropertyChangeSupport( this );
	
	protected PropertyChangeSupport getPropertyChangeSupport()
	{ return pcs; }
	protected VetoableChangeSupport vcs = new VetoableChangeSupport( this );
	
	protected VetoableChangeSupport getVetoableChangeSupport()
	{ return vcs; }
	private ConnectionPoolDataSource connectionPoolDataSource;
	private String dataSourceName = C3P0Config.initializeStringPropertyVar("dataSourceName", C3P0Defaults.dataSourceName());
	private Map extensions = C3P0Config.initializeExtensions();
	private String factoryClassLocation = C3P0Config.initializeStringPropertyVar("factoryClassLocation", C3P0Defaults.factoryClassLocation());
	private volatile String identityToken;
	private int numHelperThreads = C3P0Config.initializeIntPropertyVar("numHelperThreads", C3P0Defaults.numHelperThreads());
	
	public synchronized ConnectionPoolDataSource getConnectionPoolDataSource()
	{ return connectionPoolDataSource; }
	
	public synchronized void setConnectionPoolDataSource( ConnectionPoolDataSource connectionPoolDataSource ) throws PropertyVetoException
	{
		ConnectionPoolDataSource oldVal = this.connectionPoolDataSource;
		if ( ! eqOrBothNull( oldVal, connectionPoolDataSource ) )
			vcs.fireVetoableChange( "connectionPoolDataSource", oldVal, connectionPoolDataSource );
		this.connectionPoolDataSource = connectionPoolDataSource; // com.mchange.v2.c3p0.WrapperConnectionPoolDataSource
		if ( ! eqOrBothNull( oldVal, connectionPoolDataSource ) )
			pcs.firePropertyChange( "connectionPoolDataSource", oldVal, connectionPoolDataSource );
	}
	
	public synchronized String getDataSourceName()
	{ return dataSourceName; }
	
	public synchronized void setDataSourceName( String dataSourceName )
	{
		String oldVal = this.dataSourceName;
		this.dataSourceName = dataSourceName;
		if ( ! eqOrBothNull( oldVal, dataSourceName ) )
			pcs.firePropertyChange( "dataSourceName", oldVal, dataSourceName );
	}
	
	public synchronized Map getExtensions()
	{ return Collections.unmodifiableMap( extensions ); }
	
	public synchronized void setExtensions( Map extensions )
	{
		this.extensions = Collections.unmodifiableMap( extensions );
	}
	
	public synchronized String getFactoryClassLocation()
	{ return factoryClassLocation; }
	
	public synchronized void setFactoryClassLocation( String factoryClassLocation )
	{
		this.factoryClassLocation = factoryClassLocation;
	}
	
	public String getIdentityToken()
	{ return identityToken; }
	
	public void setIdentityToken( String identityToken )
	{
		String oldVal = this.identityToken;
		this.identityToken = identityToken;
		if ( ! eqOrBothNull( oldVal, identityToken ) )
			pcs.firePropertyChange( "identityToken", oldVal, identityToken );
	}
	
	public synchronized int getNumHelperThreads()
	{ return numHelperThreads; }
	
	public synchronized void setNumHelperThreads( int numHelperThreads )
	{
		int oldVal = this.numHelperThreads;
		this.numHelperThreads = numHelperThreads;
		if ( oldVal != numHelperThreads )
			pcs.firePropertyChange( "numHelperThreads", oldVal, numHelperThreads );
	}
	
	public void addPropertyChangeListener( PropertyChangeListener pcl )
	{ pcs.addPropertyChangeListener( pcl ); }
	
	public void addPropertyChangeListener( String propName, PropertyChangeListener pcl )
	{ pcs.addPropertyChangeListener( propName, pcl ); }
	
	public void removePropertyChangeListener( PropertyChangeListener pcl )
	{ pcs.removePropertyChangeListener( pcl ); }
	
	public void removePropertyChangeListener( String propName, PropertyChangeListener pcl )
	{ pcs.removePropertyChangeListener( propName, pcl ); }
	
	public PropertyChangeListener[] getPropertyChangeListeners()
	{ return pcs.getPropertyChangeListeners(); }
	
	public void addVetoableChangeListener( VetoableChangeListener vcl )
	{ vcs.addVetoableChangeListener( vcl ); }
	
	public void removeVetoableChangeListener( VetoableChangeListener vcl )
	{ vcs.removeVetoableChangeListener( vcl ); }
	
	public VetoableChangeListener[] getVetoableChangeListeners()
	{ return vcs.getVetoableChangeListeners(); }
	private boolean eqOrBothNull( Object a, Object b )
	{
		return
			a == b ||
			(a != null && a.equals(b));
	}
	
	private static final long serialVersionUID = 1;
	private static final short VERSION = 0x0001;
	
	private void writeObject( ObjectOutputStream oos ) throws IOException
	{
		oos.writeShort( VERSION );
		try
		{
			//test serialize
			SerializableUtils.toByteArray(connectionPoolDataSource);
			oos.writeObject( connectionPoolDataSource );
		}
		catch (NotSerializableException nse)
		{
			com.mchange.v2.log.MLog.getLogger( this.getClass() ).log(com.mchange.v2.log.MLevel.FINE, "Direct serialization provoked a NotSerializableException! Trying indirect.", nse);
			try
			{
				Indirector indirector = new com.mchange.v2.naming.ReferenceIndirector();
				oos.writeObject( indirector.indirectForm( connectionPoolDataSource ) );
			}
			catch (IOException indirectionIOException)
			{ throw indirectionIOException; }
			catch (Exception indirectionOtherException)
			{ throw new IOException("Problem indirectly serializing connectionPoolDataSource: " + indirectionOtherException.toString() ); }
		}
		oos.writeObject( dataSourceName );
		try
		{
			//test serialize
			SerializableUtils.toByteArray(extensions);
			oos.writeObject( extensions );
		}
		catch (NotSerializableException nse)
		{
			com.mchange.v2.log.MLog.getLogger( this.getClass() ).log(com.mchange.v2.log.MLevel.FINE, "Direct serialization provoked a NotSerializableException! Trying indirect.", nse);
			try
			{
				Indirector indirector = new com.mchange.v2.naming.ReferenceIndirector();
				oos.writeObject( indirector.indirectForm( extensions ) );
			}
			catch (IOException indirectionIOException)
			{ throw indirectionIOException; }
			catch (Exception indirectionOtherException)
			{ throw new IOException("Problem indirectly serializing extensions: " + indirectionOtherException.toString() ); }
		}
		oos.writeObject( factoryClassLocation );
		oos.writeObject( identityToken );
		oos.writeInt(numHelperThreads);
	}
	
	private void readObject( ObjectInputStream ois ) throws IOException, ClassNotFoundException
	{
		short version = ois.readShort();
		switch (version)
		{
			case VERSION:
				// we create an artificial scope so that we can use the name o for all indirectly serialized objects.
				{
					Object o = ois.readObject();
					if (o instanceof IndirectlySerialized) o = ((IndirectlySerialized) o).getObject();
					this.connectionPoolDataSource = (ConnectionPoolDataSource) o;
				}
				this.dataSourceName = (String) ois.readObject();
				// we create an artificial scope so that we can use the name o for all indirectly serialized objects.
				{
					Object o = ois.readObject();
					if (o instanceof IndirectlySerialized) o = ((IndirectlySerialized) o).getObject();
					this.extensions = (Map) o;
				}
				this.factoryClassLocation = (String) ois.readObject();
				this.identityToken = (String) ois.readObject();
				this.numHelperThreads = ois.readInt();
				this.pcs = new PropertyChangeSupport( this );
				this.vcs = new VetoableChangeSupport( this );
				break;
			default:
				throw new IOException("Unsupported Serialized Version: " + version);
		}
	}
	
	// JDK7 add-on
	public Logger getParentLogger() throws SQLFeatureNotSupportedException
	{ return C3P0ImplUtils.PARENT_LOGGER;}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append( super.toString() );
		sb.append(" [ ");
		sb.append( "connectionPoolDataSource -> " + connectionPoolDataSource );
		sb.append( ", ");
		sb.append( "dataSourceName -> " + dataSourceName );
		sb.append( ", ");
		sb.append( "extensions -> " + extensions );
		sb.append( ", ");
		sb.append( "factoryClassLocation -> " + factoryClassLocation );
		sb.append( ", ");
		sb.append( "identityToken -> " + identityToken );
		sb.append( ", ");
		sb.append( "numHelperThreads -> " + numHelperThreads );
		
		String extraToStringInfo = this.extraToStringInfo();
		if (extraToStringInfo != null)
			sb.append( extraToStringInfo );
		sb.append(" ]");
		return sb.toString();
	}
	
	protected String extraToStringInfo()
	{ return null; }
	
	final static JavaBeanReferenceMaker referenceMaker = new com.mchange.v2.naming.JavaBeanReferenceMaker();
	
	static
	{
		referenceMaker.setFactoryClassName( "com.mchange.v2.c3p0.impl.C3P0JavaBeanObjectFactory" );
		referenceMaker.addReferenceProperty("connectionPoolDataSource");
		referenceMaker.addReferenceProperty("dataSourceName");
		referenceMaker.addReferenceProperty("extensions");
		referenceMaker.addReferenceProperty("factoryClassLocation");
		referenceMaker.addReferenceProperty("identityToken");
		referenceMaker.addReferenceProperty("numHelperThreads");
	}
	
	public Reference getReference() throws NamingException
	{
		return referenceMaker.createReference( this );
	}
	
	private PoolBackedDataSourceBase()
	{}
	
	public PoolBackedDataSourceBase( boolean autoregister )
	{
		if (autoregister)
		{
			this.identityToken = C3P0ImplUtils.allocateIdentityToken( this );
			C3P0Registry.reregister( this );
		}
	}
}
