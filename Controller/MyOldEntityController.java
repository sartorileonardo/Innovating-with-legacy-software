package br.com.company.webservice.controller;

/**
 * Imports
 */

/**
 * @author Leonardo Sartori
 * @author Lucas Fernando Frighetto
 */
@Controller
public class MyOldEntityController {

    public List<Map<String, Object>> getAll(String filtro) throws WebServiceException {
        try{
            if(StringUtils.isNumeric(filtro)){
                return EJBInterfaces.getMyOldEntity().getOne(filtro);
            }
            return EJBInterfaces.getMyOldEntity().getAll(filtro);
        }catch(Exception e){
            thow new WebServiceException(e);
        }
    }

    public Map<String, Object> get(String codigo) throws WebServiceException {
        try{
            return EJBInterfaces.getMyOldEntity().getOne(codigo);
        }catch(Exception e){
            thow new WebServiceException(e);
        }
    }

    public Integer inserir(Map<String, Object> myEntity) throws WebServiceException {
        try{
            return EJBInterfaces.getMyOldEntity().inserir(myEntity);
        }catch(Exception e){
            thow new WebServiceException(e);
        }
    }

    public Integer alterar(Map<String, Object> myEntity) throws WebServiceException {
        try{
            return EJBInterfaces.getMyOldEntity().alterar(myEntity);
        }catch(Exception e){
            thow new WebServiceException(e);
        }
    }

    public Boolean excluir(Integer codigo) throws WebServiceException {
        try{
            return EJBInterfaces.getMyOldEntity().excluir(codigo);
        }catch(Exception e){
            thow new WebServiceException(e);
        }
    }
}
