package br.com.company.webservice.controller;

/**
 * Imports
 */

/**
 * @author Leonardo Sartori
 * @author Lucas Fernando Frighetto
 */
@Controller
public class MyNewEntityController extends CadastroController {

    public List<Map<String, Object>> getAll(String filtro) throws WebServiceException {
        return getAll(this.getClass().getSimpleName(), filtro, true);
    }

    public Map<String, Object> get(String codigo) throws WebServiceException {
        return get(this.getClass().getSimpleName(), codigo, true);
    }

    public Integer inserir(Map<String, Object> myEntity) throws WebServiceException {
        return (Integer) inserir(this.getClass().getSimpleName(), myEntity);
    }

    public Integer alterar(Map<String, Object> myEntity) throws WebServiceException {
        return (Integer) alterar(this.getClass().getSimpleName(), myEntity);
    }

    public Boolean excluir(Integer codigo) throws WebServiceException {
        return excluir(this.getClass().getSimpleName(), codigo);
    }
}
