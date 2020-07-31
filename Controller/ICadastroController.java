package br.com.company.webservice.controller;

/**
 * Imports
 */

/**
 * @author Leonardo Sartori
 * @author Lucas Fernando Frighetto
 */
public interface ICadastroController {
    Object inserir(String classe, Map<String, Object> myEntity) throws WebServiceException;
    Object inserir(String classe, Object myEntity) throws WebServiceException;
    Object alterar(String classe, Map<String, Object> myEntity) throws WebServiceException;
    Object alterar(String classe, Object myEntity) throws WebServiceException;
    Boolean excluir(String nameEntity, Object codigo) throws WebServiceException;
    Boolean excluir(String nameEntity, Map<String, Object> compositeKeys) throws WebServiceException;
}
