package cn.java.debug.debug;

import java.util.HashMap;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ExtendsQueueEntry;
import org.hibernate.cfg.Mappings;
import org.hibernate.cfg.NamedSQLQuerySecondPass;
import org.hibernate.cfg.ResultSetMappingSecondPass;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.mapping.FetchProfile;
import org.hibernate.mapping.MetadataSource;

public class XxxHbmDotXml extends Configuration {
	public static void main(String[] args) {

	}
	
	public void parseMapHbmDotXml() {
		Mappings mappings = createMappings();
		
		{
			/*
			 	// <hibernate-mapping ... >
			 	mappings.addToExtendsQueue( new ExtendsQueueEntry( name, packageName, metadataXml, entityNames ) );
			 	mappings.setSchemaName( ( schemaNode == null ) ? null : schemaNode.getValue() );
			 	mappings.setCatalogName( ( catalogNode == null ) ? null : catalogNode.getValue() );
			 	mappings.setDefaultCascade( ( dcNode == null ) ? "none" : dcNode.getValue() );
			 	mappings.setDefaultAccess( ( daNode == null ) ? "property" : daNode.getValue() );
			 	mappings.setDefaultLazy( dlNode == null || dlNode.getValue().equals( "true" ) );
			 	mappings.setAutoImport( ( aiNode == null ) || "true".equals( aiNode.getValue() ) );
			 	mappings.setDefaultPackage( packNode.getValue() );
			 	
			 	// <filter-def name="name" condition="defaultCondition">
			 	{
			 		FilterDefinition def = new FilterDefinition( name, defaultCondition, paramMappings );
					mappings.addFilterDefinition( def );
			 	}
			 	
			 	// <fetch-profile name="profileName">
			 	{
			 		FetchProfile profile = mappings.findOrCreateFetchProfile( profileName, MetadataSource.HBM );
			 		profile.addFetch( entityName, association, style );
			 	}
			 	
			 	// <identifier-generator name="strategy" class="generatorClassName" />
			 	mappings.getIdentifierGeneratorFactory().register( strategy, generatorClass );
			 	
			 	// <typedef name="typeName" class="typeClass">
			 	{
			 		mappings.addTypeDef( typeName, typeClass, parameters );
			 	}
			 	
			 	// <class lazy="false" ...>
			 	{
			 		mappings.addImport( entity.getEntityName(), entity.getEntityName() );
			 		mappings.addTable(...)
			 		mappings.addClass( rootclass );
			 	}
			 	
			 	// <subclass name="cn.java.demo.entity.SubClazz"></subclass>
			 	// <joined-subclass name="cn.java.demo.entity.JoinedClazz"></joined-subclass>
				// <union-subclass name="cn.java.demo.entity.UnionClazz"></union-subclass>
			 	mappings.addClass( subclass );
			 	
			 	// <query>
		 		mappings.addQuery( namedQuery.getName(), namedQuery );
		 		
		 		// <sql-query>
		 		mappings.addSecondPass( new NamedSQLQuerySecondPass( queryElem, path, mappings ) );
		 		
		 		// <resultset>
		 		mappings.addSecondPass( new ResultSetMappingSecondPass( resultSetElem, path, mappings ) );
		 		
		 		// <import>
		 		mappings.addImport( className, rename );
		 		
		 		// <database-object>
		 		mappings.addAuxiliaryDatabaseObject( auxDbObject );
		 		
			 */
			/*
			 	<filter-def name="name" condition="defaultCondition">
					<filter-param name="paramName0" type="paramType0" />
					<filter-param name="paramName1" type="paramType1" />
				</filter-def>
			 */
			HashMap paramMappings = new HashMap();
			paramMappings.put( "paramName0", "paramType0" );
			FilterDefinition def = new FilterDefinition( "filter0", "defaultCondition", paramMappings );
			mappings.addFilterDefinition( def );
		}
	}
}
