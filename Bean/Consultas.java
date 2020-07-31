package com.company.ejb.interfaces.consultas;

/**
 * Imports
 */

/**
 * @author Lucas Fernando Frighetto
 */
public interface Consultas extends EJBObject {

    <T> T get(String tableName, Object uid, String columnName) throws CreateException, RemoteException, Exception;

    <T> T get(String tableName, Class<T> classe, Object uid) throws CreateException, RemoteException, Exception;

    <T> T get(String tableName, Class<T> classe, Object uid, Boolean isCrudERP) throws CreateException, RemoteException, Exception;

    <T> T get(String tableName, Class<T> classe, Object uid, HashSet<String> columns) throws CreateException, RemoteException, Exception;

    <T> T get(String tableName, Class<T> classe, Object uid, Boolean isCrudERP, HashSet<String> columns) throws CreateException, RemoteException, Exception;

    Map<String, Object> get(String tableName, Object uid) throws CreateException, RemoteException, Exception;

    Map<String, Object> get(String tableName, Object uid, Boolean isCrudERP) throws CreateException, RemoteException, Exception;

    Map<String, Object> get(String tableName, Object uid, HashSet<String> columns) throws CreateException, RemoteException, Exception;

    Map<String, Object> get(String tableName, Object uid, Boolean isCrudERP, HashSet<String> columns) throws CreateException, RemoteException, Exception;

    <T> List<T> getAll(String tableName, Class<T> classe, HashSet<String> columns) throws CreateException, RemoteException, Exception;

    <T> List<T> getAll(String tableName, Class<T> classe, String searchInput) throws CreateException, RemoteException, Exception;

    <T> List<T> getAll(String tableName, Class<T> classe, String searchInput, Boolean concatDescCod) throws CreateException, RemoteException, Exception;

    <T> List<T> getAll(String tableName, Class<T> classe, String searchInput, HashSet<String> columns) throws CreateException, RemoteException, Exception;

    <T> List<T> getAll(String tableName, Class<T> classe, String searchInput, HashSet<String> columns, Boolean concatDescCod) throws CreateException, RemoteException, Exception;

    List<Map<String, Object>> getAll(String tableName, HashSet<String> columns) throws CreateException, RemoteException, Exception;

    List<Map<String, Object>> getAll(String tableName, String searchInput) throws CreateException, RemoteException, Exception;

    List<Map<String, Object>> getAll(String tableName, String searchInput, Boolean concatDescCod) throws CreateException, RemoteException, Exception;

    List<Map<String, Object>> getAll(String tableName, String searchInput, HashSet<String> columns) throws CreateException, RemoteException, Exception;

    List<Map<String, Object>> getAll(String tableName, String searchInput, HashSet<String> columns, Boolean concatDescCod) throws CreateException, RemoteException, Exception;
}
