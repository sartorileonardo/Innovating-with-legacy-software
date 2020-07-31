package com.company.ejb.session.cadastros;

/**
 * Imports
 */

/**
 * @author Leonardo Sartori
 * @author Lucas Fernando Frighetto
 */
public interface ICadastroBean {
    Object inserir(String className, Object entidade, Integer codUsuario, Integer codFilial, String modulo, String primaryKeyFieldName, Map<String, String> foreignKeysCompositeName) throws Exception;
    Object inserir(String className, Object objetoEntidade, Integer codUsuario, Integer codFilial, String modulo, String primaryKeyFieldName, Map<String, String> foreignKeysCompositeName, Boolean autoincremento) throws Exception;
    Object inserirComChaveComposta(String className, Object entidade, Integer codUsuario, Integer codFilial, String modulo, String uidReturn, String... compositeKeys) throws ERKNoPermissionException, ERKDuplicateKeyException, ERKNotFoundException, ERKBusinessException;

    Object alterar(String className, Object entidade, Integer codUsuario, Integer codFilial, String modulo, String primaryKeyFieldName) throws CreateException, RemoteException, Exception;
    Object alterar(String className, Object entidade, Integer codUsuario, Integer codFilial, String modulo, String primaryKeyFieldName, Map<String, String> foreignKeys) throws CreateException, RemoteException, Exception;
    Object alterarComChaveComposta(String className, Object entidade, Integer codUsuario, Integer codFilial, String modulo, String uidReturn, String... compositeKeys) throws CreateException, RemoteException, Exception;

    Boolean excluir(String className, Integer codUsuario, Integer codFilial, String modulo, Object uid) throws CreateException, RemoteException, Exception;
    Boolean excluirComChaveComposta(String className, Integer codUsuario, Integer codFilial, String modulo, Map<String, Object> compositeKeys) throws CreateException, RemoteException, Exception;
}
