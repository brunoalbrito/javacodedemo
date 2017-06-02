/*
 * Distributed as part of mchange-commons-java 0.2.11
 *
 * Copyright (C) 2015 Machinery For Change, Inc.
 *
 * Author: Steve Waldman <swaldman@mchange.com>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of EITHER:
 *
 *     1) The GNU Lesser General Public License (LGPL), version 2.1, as 
 *        published by the Free Software Foundation
 *
 * OR
 *
 *     2) The Eclipse Public License (EPL), version 1.0
 *
 * You may choose which license to accept if you wish to redistribute
 * or modify this work. You may offer derivatives of this work
 * under the license you have chosen, or you may provide the same
 * choice of license which you have been offered here.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received copies of both LGPL v2.1 and EPL v1.0
 * along with this software; see the files LICENSE-EPL and LICENSE-LGPL.
 * If not, the text of these licenses are currently available at
 *
 * LGPL v2.1: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *  EPL v1.0: http://www.eclipse.org/org/documents/epl-v10.php 
 * 
 */

package com.mchange.v2.sql.filter;

import java.lang.Class;
import java.lang.Object;
import java.lang.String;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

/**
 * This class was generated by com.mchange.v2.codegen.intfc.DelegatorGenerator.
 */
public abstract class FilterDatabaseMetaData implements DatabaseMetaData
{
	protected DatabaseMetaData inner;
	
	
	private void __setInner( DatabaseMetaData inner )
	{
		this.inner = inner;
	}
	
	public FilterDatabaseMetaData(DatabaseMetaData inner)
	{ __setInner( inner ); }
	
	public FilterDatabaseMetaData()
	{}
	
	public void setInner( DatabaseMetaData inner )
	{ __setInner( inner ); }
	
	public DatabaseMetaData getInner()
	{ return inner; }
	
	public boolean autoCommitFailureClosesAllResultSets() throws SQLException
	{
		return inner.autoCommitFailureClosesAllResultSets();
	}
	
	public ResultSet getCatalogs() throws SQLException
	{
		return inner.getCatalogs();
	}
	
	public boolean allProceduresAreCallable() throws SQLException
	{
		return inner.allProceduresAreCallable();
	}
	
	public boolean allTablesAreSelectable() throws SQLException
	{
		return inner.allTablesAreSelectable();
	}
	
	public boolean dataDefinitionCausesTransactionCommit() throws SQLException
	{
		return inner.dataDefinitionCausesTransactionCommit();
	}
	
	public boolean dataDefinitionIgnoredInTransactions() throws SQLException
	{
		return inner.dataDefinitionIgnoredInTransactions();
	}
	
	public boolean deletesAreDetected(int a) throws SQLException
	{
		return inner.deletesAreDetected(a);
	}
	
	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException
	{
		return inner.doesMaxRowSizeIncludeBlobs();
	}
	
	public boolean generatedKeyAlwaysReturned() throws SQLException
	{
		return inner.generatedKeyAlwaysReturned();
	}
	
	public ResultSet getBestRowIdentifier(String a, String b, String c, int d, boolean e) throws SQLException
	{
		return inner.getBestRowIdentifier(a, b, c, d, e);
	}
	
	public String getCatalogSeparator() throws SQLException
	{
		return inner.getCatalogSeparator();
	}
	
	public String getCatalogTerm() throws SQLException
	{
		return inner.getCatalogTerm();
	}
	
	public ResultSet getClientInfoProperties() throws SQLException
	{
		return inner.getClientInfoProperties();
	}
	
	public ResultSet getColumnPrivileges(String a, String b, String c, String d) throws SQLException
	{
		return inner.getColumnPrivileges(a, b, c, d);
	}
	
	public ResultSet getColumns(String a, String b, String c, String d) throws SQLException
	{
		return inner.getColumns(a, b, c, d);
	}
	
	public Connection getConnection() throws SQLException
	{
		return inner.getConnection();
	}
	
	public ResultSet getCrossReference(String a, String b, String c, String d, String e, String f) throws SQLException
	{
		return inner.getCrossReference(a, b, c, d, e, f);
	}
	
	public int getDatabaseMajorVersion() throws SQLException
	{
		return inner.getDatabaseMajorVersion();
	}
	
	public int getDatabaseMinorVersion() throws SQLException
	{
		return inner.getDatabaseMinorVersion();
	}
	
	public String getDatabaseProductName() throws SQLException
	{
		return inner.getDatabaseProductName();
	}
	
	public String getDatabaseProductVersion() throws SQLException
	{
		return inner.getDatabaseProductVersion();
	}
	
	public int getDefaultTransactionIsolation() throws SQLException
	{
		return inner.getDefaultTransactionIsolation();
	}
	
	public int getDriverMajorVersion()
	{
		return inner.getDriverMajorVersion();
	}
	
	public int getDriverMinorVersion()
	{
		return inner.getDriverMinorVersion();
	}
	
	public String getDriverName() throws SQLException
	{
		return inner.getDriverName();
	}
	
	public String getDriverVersion() throws SQLException
	{
		return inner.getDriverVersion();
	}
	
	public ResultSet getExportedKeys(String a, String b, String c) throws SQLException
	{
		return inner.getExportedKeys(a, b, c);
	}
	
	public String getExtraNameCharacters() throws SQLException
	{
		return inner.getExtraNameCharacters();
	}
	
	public ResultSet getFunctionColumns(String a, String b, String c, String d) throws SQLException
	{
		return inner.getFunctionColumns(a, b, c, d);
	}
	
	public ResultSet getFunctions(String a, String b, String c) throws SQLException
	{
		return inner.getFunctions(a, b, c);
	}
	
	public String getIdentifierQuoteString() throws SQLException
	{
		return inner.getIdentifierQuoteString();
	}
	
	public ResultSet getImportedKeys(String a, String b, String c) throws SQLException
	{
		return inner.getImportedKeys(a, b, c);
	}
	
	public ResultSet getIndexInfo(String a, String b, String c, boolean d, boolean e) throws SQLException
	{
		return inner.getIndexInfo(a, b, c, d, e);
	}
	
	public int getJDBCMajorVersion() throws SQLException
	{
		return inner.getJDBCMajorVersion();
	}
	
	public int getJDBCMinorVersion() throws SQLException
	{
		return inner.getJDBCMinorVersion();
	}
	
	public int getMaxBinaryLiteralLength() throws SQLException
	{
		return inner.getMaxBinaryLiteralLength();
	}
	
	public int getMaxCatalogNameLength() throws SQLException
	{
		return inner.getMaxCatalogNameLength();
	}
	
	public int getMaxCharLiteralLength() throws SQLException
	{
		return inner.getMaxCharLiteralLength();
	}
	
	public int getMaxColumnNameLength() throws SQLException
	{
		return inner.getMaxColumnNameLength();
	}
	
	public int getMaxColumnsInGroupBy() throws SQLException
	{
		return inner.getMaxColumnsInGroupBy();
	}
	
	public int getMaxColumnsInIndex() throws SQLException
	{
		return inner.getMaxColumnsInIndex();
	}
	
	public int getMaxColumnsInOrderBy() throws SQLException
	{
		return inner.getMaxColumnsInOrderBy();
	}
	
	public int getMaxColumnsInSelect() throws SQLException
	{
		return inner.getMaxColumnsInSelect();
	}
	
	public int getMaxColumnsInTable() throws SQLException
	{
		return inner.getMaxColumnsInTable();
	}
	
	public int getMaxConnections() throws SQLException
	{
		return inner.getMaxConnections();
	}
	
	public int getMaxCursorNameLength() throws SQLException
	{
		return inner.getMaxCursorNameLength();
	}
	
	public int getMaxIndexLength() throws SQLException
	{
		return inner.getMaxIndexLength();
	}
	
	public int getMaxProcedureNameLength() throws SQLException
	{
		return inner.getMaxProcedureNameLength();
	}
	
	public int getMaxRowSize() throws SQLException
	{
		return inner.getMaxRowSize();
	}
	
	public int getMaxSchemaNameLength() throws SQLException
	{
		return inner.getMaxSchemaNameLength();
	}
	
	public int getMaxStatementLength() throws SQLException
	{
		return inner.getMaxStatementLength();
	}
	
	public int getMaxStatements() throws SQLException
	{
		return inner.getMaxStatements();
	}
	
	public int getMaxTableNameLength() throws SQLException
	{
		return inner.getMaxTableNameLength();
	}
	
	public int getMaxTablesInSelect() throws SQLException
	{
		return inner.getMaxTablesInSelect();
	}
	
	public int getMaxUserNameLength() throws SQLException
	{
		return inner.getMaxUserNameLength();
	}
	
	public String getNumericFunctions() throws SQLException
	{
		return inner.getNumericFunctions();
	}
	
	public ResultSet getPrimaryKeys(String a, String b, String c) throws SQLException
	{
		return inner.getPrimaryKeys(a, b, c);
	}
	
	public ResultSet getProcedureColumns(String a, String b, String c, String d) throws SQLException
	{
		return inner.getProcedureColumns(a, b, c, d);
	}
	
	public String getProcedureTerm() throws SQLException
	{
		return inner.getProcedureTerm();
	}
	
	public ResultSet getProcedures(String a, String b, String c) throws SQLException
	{
		return inner.getProcedures(a, b, c);
	}
	
	public ResultSet getPseudoColumns(String a, String b, String c, String d) throws SQLException
	{
		return inner.getPseudoColumns(a, b, c, d);
	}
	
	public int getResultSetHoldability() throws SQLException
	{
		return inner.getResultSetHoldability();
	}
	
	public RowIdLifetime getRowIdLifetime() throws SQLException
	{
		return inner.getRowIdLifetime();
	}
	
	public String getSQLKeywords() throws SQLException
	{
		return inner.getSQLKeywords();
	}
	
	public int getSQLStateType() throws SQLException
	{
		return inner.getSQLStateType();
	}
	
	public String getSchemaTerm() throws SQLException
	{
		return inner.getSchemaTerm();
	}
	
	public ResultSet getSchemas(String a, String b) throws SQLException
	{
		return inner.getSchemas(a, b);
	}
	
	public ResultSet getSchemas() throws SQLException
	{
		return inner.getSchemas();
	}
	
	public String getSearchStringEscape() throws SQLException
	{
		return inner.getSearchStringEscape();
	}
	
	public String getStringFunctions() throws SQLException
	{
		return inner.getStringFunctions();
	}
	
	public ResultSet getSuperTables(String a, String b, String c) throws SQLException
	{
		return inner.getSuperTables(a, b, c);
	}
	
	public ResultSet getSuperTypes(String a, String b, String c) throws SQLException
	{
		return inner.getSuperTypes(a, b, c);
	}
	
	public String getSystemFunctions() throws SQLException
	{
		return inner.getSystemFunctions();
	}
	
	public ResultSet getTablePrivileges(String a, String b, String c) throws SQLException
	{
		return inner.getTablePrivileges(a, b, c);
	}
	
	public ResultSet getTableTypes() throws SQLException
	{
		return inner.getTableTypes();
	}
	
	public ResultSet getTables(String a, String b, String c, String[] d) throws SQLException
	{
		return inner.getTables(a, b, c, d);
	}
	
	public String getTimeDateFunctions() throws SQLException
	{
		return inner.getTimeDateFunctions();
	}
	
	public ResultSet getTypeInfo() throws SQLException
	{
		return inner.getTypeInfo();
	}
	
	public ResultSet getUDTs(String a, String b, String c, int[] d) throws SQLException
	{
		return inner.getUDTs(a, b, c, d);
	}
	
	public String getUserName() throws SQLException
	{
		return inner.getUserName();
	}
	
	public ResultSet getVersionColumns(String a, String b, String c) throws SQLException
	{
		return inner.getVersionColumns(a, b, c);
	}
	
	public boolean insertsAreDetected(int a) throws SQLException
	{
		return inner.insertsAreDetected(a);
	}
	
	public boolean isCatalogAtStart() throws SQLException
	{
		return inner.isCatalogAtStart();
	}
	
	public boolean locatorsUpdateCopy() throws SQLException
	{
		return inner.locatorsUpdateCopy();
	}
	
	public boolean nullPlusNonNullIsNull() throws SQLException
	{
		return inner.nullPlusNonNullIsNull();
	}
	
	public boolean nullsAreSortedAtEnd() throws SQLException
	{
		return inner.nullsAreSortedAtEnd();
	}
	
	public boolean nullsAreSortedAtStart() throws SQLException
	{
		return inner.nullsAreSortedAtStart();
	}
	
	public boolean nullsAreSortedHigh() throws SQLException
	{
		return inner.nullsAreSortedHigh();
	}
	
	public boolean nullsAreSortedLow() throws SQLException
	{
		return inner.nullsAreSortedLow();
	}
	
	public boolean othersDeletesAreVisible(int a) throws SQLException
	{
		return inner.othersDeletesAreVisible(a);
	}
	
	public boolean othersInsertsAreVisible(int a) throws SQLException
	{
		return inner.othersInsertsAreVisible(a);
	}
	
	public boolean othersUpdatesAreVisible(int a) throws SQLException
	{
		return inner.othersUpdatesAreVisible(a);
	}
	
	public boolean ownDeletesAreVisible(int a) throws SQLException
	{
		return inner.ownDeletesAreVisible(a);
	}
	
	public boolean ownInsertsAreVisible(int a) throws SQLException
	{
		return inner.ownInsertsAreVisible(a);
	}
	
	public boolean ownUpdatesAreVisible(int a) throws SQLException
	{
		return inner.ownUpdatesAreVisible(a);
	}
	
	public boolean storesLowerCaseIdentifiers() throws SQLException
	{
		return inner.storesLowerCaseIdentifiers();
	}
	
	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException
	{
		return inner.storesLowerCaseQuotedIdentifiers();
	}
	
	public boolean storesMixedCaseIdentifiers() throws SQLException
	{
		return inner.storesMixedCaseIdentifiers();
	}
	
	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException
	{
		return inner.storesMixedCaseQuotedIdentifiers();
	}
	
	public boolean storesUpperCaseIdentifiers() throws SQLException
	{
		return inner.storesUpperCaseIdentifiers();
	}
	
	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException
	{
		return inner.storesUpperCaseQuotedIdentifiers();
	}
	
	public boolean supportsANSI92EntryLevelSQL() throws SQLException
	{
		return inner.supportsANSI92EntryLevelSQL();
	}
	
	public boolean supportsANSI92FullSQL() throws SQLException
	{
		return inner.supportsANSI92FullSQL();
	}
	
	public boolean supportsANSI92IntermediateSQL() throws SQLException
	{
		return inner.supportsANSI92IntermediateSQL();
	}
	
	public boolean supportsAlterTableWithAddColumn() throws SQLException
	{
		return inner.supportsAlterTableWithAddColumn();
	}
	
	public boolean supportsAlterTableWithDropColumn() throws SQLException
	{
		return inner.supportsAlterTableWithDropColumn();
	}
	
	public boolean supportsBatchUpdates() throws SQLException
	{
		return inner.supportsBatchUpdates();
	}
	
	public boolean supportsCatalogsInDataManipulation() throws SQLException
	{
		return inner.supportsCatalogsInDataManipulation();
	}
	
	public boolean supportsCatalogsInIndexDefinitions() throws SQLException
	{
		return inner.supportsCatalogsInIndexDefinitions();
	}
	
	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException
	{
		return inner.supportsCatalogsInPrivilegeDefinitions();
	}
	
	public boolean supportsCatalogsInProcedureCalls() throws SQLException
	{
		return inner.supportsCatalogsInProcedureCalls();
	}
	
	public boolean supportsCatalogsInTableDefinitions() throws SQLException
	{
		return inner.supportsCatalogsInTableDefinitions();
	}
	
	public boolean supportsColumnAliasing() throws SQLException
	{
		return inner.supportsColumnAliasing();
	}
	
	public boolean supportsConvert(int a, int b) throws SQLException
	{
		return inner.supportsConvert(a, b);
	}
	
	public boolean supportsConvert() throws SQLException
	{
		return inner.supportsConvert();
	}
	
	public boolean supportsCoreSQLGrammar() throws SQLException
	{
		return inner.supportsCoreSQLGrammar();
	}
	
	public boolean supportsCorrelatedSubqueries() throws SQLException
	{
		return inner.supportsCorrelatedSubqueries();
	}
	
	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException
	{
		return inner.supportsDataDefinitionAndDataManipulationTransactions();
	}
	
	public boolean supportsDataManipulationTransactionsOnly() throws SQLException
	{
		return inner.supportsDataManipulationTransactionsOnly();
	}
	
	public boolean supportsDifferentTableCorrelationNames() throws SQLException
	{
		return inner.supportsDifferentTableCorrelationNames();
	}
	
	public boolean supportsExpressionsInOrderBy() throws SQLException
	{
		return inner.supportsExpressionsInOrderBy();
	}
	
	public boolean supportsExtendedSQLGrammar() throws SQLException
	{
		return inner.supportsExtendedSQLGrammar();
	}
	
	public boolean supportsFullOuterJoins() throws SQLException
	{
		return inner.supportsFullOuterJoins();
	}
	
	public boolean supportsGetGeneratedKeys() throws SQLException
	{
		return inner.supportsGetGeneratedKeys();
	}
	
	public boolean supportsGroupBy() throws SQLException
	{
		return inner.supportsGroupBy();
	}
	
	public boolean supportsGroupByBeyondSelect() throws SQLException
	{
		return inner.supportsGroupByBeyondSelect();
	}
	
	public boolean supportsGroupByUnrelated() throws SQLException
	{
		return inner.supportsGroupByUnrelated();
	}
	
	public boolean supportsIntegrityEnhancementFacility() throws SQLException
	{
		return inner.supportsIntegrityEnhancementFacility();
	}
	
	public boolean supportsLikeEscapeClause() throws SQLException
	{
		return inner.supportsLikeEscapeClause();
	}
	
	public boolean supportsLimitedOuterJoins() throws SQLException
	{
		return inner.supportsLimitedOuterJoins();
	}
	
	public boolean supportsMinimumSQLGrammar() throws SQLException
	{
		return inner.supportsMinimumSQLGrammar();
	}
	
	public boolean supportsMixedCaseIdentifiers() throws SQLException
	{
		return inner.supportsMixedCaseIdentifiers();
	}
	
	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException
	{
		return inner.supportsMixedCaseQuotedIdentifiers();
	}
	
	public boolean supportsMultipleOpenResults() throws SQLException
	{
		return inner.supportsMultipleOpenResults();
	}
	
	public boolean supportsMultipleResultSets() throws SQLException
	{
		return inner.supportsMultipleResultSets();
	}
	
	public boolean supportsMultipleTransactions() throws SQLException
	{
		return inner.supportsMultipleTransactions();
	}
	
	public boolean supportsNamedParameters() throws SQLException
	{
		return inner.supportsNamedParameters();
	}
	
	public boolean supportsNonNullableColumns() throws SQLException
	{
		return inner.supportsNonNullableColumns();
	}
	
	public boolean supportsOpenCursorsAcrossCommit() throws SQLException
	{
		return inner.supportsOpenCursorsAcrossCommit();
	}
	
	public boolean supportsOpenCursorsAcrossRollback() throws SQLException
	{
		return inner.supportsOpenCursorsAcrossRollback();
	}
	
	public boolean supportsOpenStatementsAcrossCommit() throws SQLException
	{
		return inner.supportsOpenStatementsAcrossCommit();
	}
	
	public boolean supportsOpenStatementsAcrossRollback() throws SQLException
	{
		return inner.supportsOpenStatementsAcrossRollback();
	}
	
	public boolean supportsOrderByUnrelated() throws SQLException
	{
		return inner.supportsOrderByUnrelated();
	}
	
	public boolean supportsOuterJoins() throws SQLException
	{
		return inner.supportsOuterJoins();
	}
	
	public boolean supportsPositionedDelete() throws SQLException
	{
		return inner.supportsPositionedDelete();
	}
	
	public boolean supportsPositionedUpdate() throws SQLException
	{
		return inner.supportsPositionedUpdate();
	}
	
	public boolean supportsResultSetConcurrency(int a, int b) throws SQLException
	{
		return inner.supportsResultSetConcurrency(a, b);
	}
	
	public boolean supportsResultSetHoldability(int a) throws SQLException
	{
		return inner.supportsResultSetHoldability(a);
	}
	
	public boolean supportsResultSetType(int a) throws SQLException
	{
		return inner.supportsResultSetType(a);
	}
	
	public boolean supportsSavepoints() throws SQLException
	{
		return inner.supportsSavepoints();
	}
	
	public boolean supportsSchemasInDataManipulation() throws SQLException
	{
		return inner.supportsSchemasInDataManipulation();
	}
	
	public boolean supportsSchemasInIndexDefinitions() throws SQLException
	{
		return inner.supportsSchemasInIndexDefinitions();
	}
	
	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException
	{
		return inner.supportsSchemasInPrivilegeDefinitions();
	}
	
	public boolean supportsSchemasInProcedureCalls() throws SQLException
	{
		return inner.supportsSchemasInProcedureCalls();
	}
	
	public boolean supportsSchemasInTableDefinitions() throws SQLException
	{
		return inner.supportsSchemasInTableDefinitions();
	}
	
	public boolean supportsSelectForUpdate() throws SQLException
	{
		return inner.supportsSelectForUpdate();
	}
	
	public boolean supportsStatementPooling() throws SQLException
	{
		return inner.supportsStatementPooling();
	}
	
	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException
	{
		return inner.supportsStoredFunctionsUsingCallSyntax();
	}
	
	public boolean supportsStoredProcedures() throws SQLException
	{
		return inner.supportsStoredProcedures();
	}
	
	public boolean supportsSubqueriesInComparisons() throws SQLException
	{
		return inner.supportsSubqueriesInComparisons();
	}
	
	public boolean supportsSubqueriesInExists() throws SQLException
	{
		return inner.supportsSubqueriesInExists();
	}
	
	public boolean supportsSubqueriesInIns() throws SQLException
	{
		return inner.supportsSubqueriesInIns();
	}
	
	public boolean supportsSubqueriesInQuantifieds() throws SQLException
	{
		return inner.supportsSubqueriesInQuantifieds();
	}
	
	public boolean supportsTableCorrelationNames() throws SQLException
	{
		return inner.supportsTableCorrelationNames();
	}
	
	public boolean supportsTransactionIsolationLevel(int a) throws SQLException
	{
		return inner.supportsTransactionIsolationLevel(a);
	}
	
	public boolean supportsTransactions() throws SQLException
	{
		return inner.supportsTransactions();
	}
	
	public boolean supportsUnion() throws SQLException
	{
		return inner.supportsUnion();
	}
	
	public boolean supportsUnionAll() throws SQLException
	{
		return inner.supportsUnionAll();
	}
	
	public boolean updatesAreDetected(int a) throws SQLException
	{
		return inner.updatesAreDetected(a);
	}
	
	public boolean usesLocalFilePerTable() throws SQLException
	{
		return inner.usesLocalFilePerTable();
	}
	
	public boolean usesLocalFiles() throws SQLException
	{
		return inner.usesLocalFiles();
	}
	
	public String getURL() throws SQLException
	{
		return inner.getURL();
	}
	
	public boolean isReadOnly() throws SQLException
	{
		return inner.isReadOnly();
	}
	
	public ResultSet getAttributes(String a, String b, String c, String d) throws SQLException
	{
		return inner.getAttributes(a, b, c, d);
	}
	
	public boolean isWrapperFor(Class a) throws SQLException
	{
		return inner.isWrapperFor(a);
	}
	
	public Object unwrap(Class a) throws SQLException
	{
		return inner.unwrap(a);
	}
	
}