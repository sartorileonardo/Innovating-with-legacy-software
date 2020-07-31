package br.com.company.webservice.controller;

/**
 * Imports
 */

/**
 * @author Leonardo Sartori
 * @author Lucas Fernando Frighetto
 * Esta classe deve ser incada por herança quando o desenvolvedor precisar de um CRUD simples,
 * a classe deve ser utilizada em conjunto com a CadastroBean para o fluxo funcionar corretamente.
 */
@Controller
public class CadastroController extends ConsultaController implements ICadastroController {

    @Autowired
    private Register register;

    /**
     * @apiNote Utilizado para invocar um método inserir da interface
     * @param className
     * @param myEntity
     * @return retorna a chave do registro inserido.
     * @implNote
     * public Integer inserir(Map<String, Object> impressora) throws WebServiceException {
     *         return (Integer) inserir(this.getClass().getSimpleName(), impressora);
     * }
     */
    public Object inserir(String className, Map<String, Object> myEntity) throws WebServiceException {
        try {
            return save(className, "inserir", myEntity);
        } catch (Exception e) {
            throw new WebServiceException("Ocorreu um erro ao inserir. " + e.getMessage(), e);
        }
    }

    /**
     * @apiNote Utilizado para invocar um método inserir da interface
     * @param className
     * @param myEntity
     * @return retorna a chave do registro inserido.
     * @implNote
     * public Integer inserirCobertura(CoberturasVO coberturasVO) throws WebServiceException {
     *         return (Integer) inserir(this.getClass().getSimpleName(), coberturasVO);
     * }
     */
    public Object inserir(String className, Object myEntity) throws WebServiceException {
        try {
            return save(className, "inserir", myEntity);
        } catch (Exception e) {
            throw new WebServiceException("Ocorreu um erro ao inserir. " + e.getMessage(), e);
        }
    }

    /**
     * @apiNote Utilizado para invocar um método alterar da interface
     * @param className
     * @param myEntity
     * @return retorna a chave do registro inserido.
     * @implNote
     * public Integer alterar(Map<String, Object> impressora) throws WebServiceException {
     *         return (Integer) alterar(this.getClass().getSimpleName(), impressora);
     * }
     */
    public Object alterar(String className, Map<String, Object> myEntity) throws WebServiceException {
        try {
            return save(className, "alterar", myEntity);
        } catch (Exception e) {
            throw new WebServiceException("Ocorreu um erro ao alterar. " + e.getMessage(), e);
        }
    }

    /**
     * @apiNote Utilizado para invocar um método alterar da interface
     * @param className
     * @param myEntity
     * @return retorna a chave do registro inserido.
     * @implNote
     * public Integer editarCobertura(CoberturasVO coberturasVO) throws WebServiceException {
     *         return (Integer) alterar(this.getClass().getSimpleName(), coberturasVO);
     *}
     */
    public Object alterar(String className, Object myEntity) throws WebServiceException {
        try {
            return save(className, "alterar", myEntity);
        } catch (Exception e) {
            throw new WebServiceException("Ocorreu um erro ao alterar. " + e.getMessage(), e);
        }
    }

    /**
     * @apiNote Utilizado para invocar um método excluir da interface
     * @param className
     * @param uid
     * @return retorna true em caso de sucesso
     * @implNote
     * public Boolean excluir(Integer codigo) throws WebServiceException {
     *         return excluir(this.getClass().getSimpleName(), codigo);
     *}
     */
    public Boolean excluir(String className, Object uid) throws WebServiceException {
        try {
            return excluir(className, "excluir", uid);
        } catch (Exception e) {
            throw new WebServiceException(e);
        }
    }

    /**
     * @apiNote Utilizado para invocar um método excluir da interface quando a entidade possui uma chave composta
     * @param className
     * @param compositeKeys
     * @return retorna true em caso de sucesso
     * @implNote
     * public Boolean excluir(Integer numero, Integer livrocontabil, Integer parametrolivrocontabil) throws WebServiceException {
     *         Map<String, Object> compositeKeys = ERKMapUtils.parseTwoListToMap(Arrays.asList("numero", "livrocontabil", "parametrolivrocontabil"), Arrays.asList(numero, livrocontabil, parametrolivrocontabil));
     *         return excluir(this.getClass().getSimpleName(), compositeKeys);
     *}
     */
    public Boolean excluir(String className, Map<String, Object> compositeKeys) throws WebServiceException {
        try {
            return excluir(className, "excluir", compositeKeys);
        } catch (Exception e) {
            throw new WebServiceException(e);
        }
    }

    private String converterNomeDaClasseEmNomeTabela(String className) {
        return className != null ? className.toLowerCase().replace("controller", "") : null;
    }

    private String converterNomeDaClasseEmNomeEntidade(String className) {
        return className != null ? className.replace("Controller", "") : null;
    }

    private Object save(String nameEntity, String nameMethod, Map<String, Object> map) throws WebServiceException, ERKNoPermissionException, ERKNoContentException, ERKDuplicateKeyException, ERKBusinessException, ERKNotFoundException {
        try {
            nameEntity = converterNomeDaClasseEmNomeEntidade(nameEntity);
            Object entityBean = getMetodoEjbInterface(nameEntity);
            Method save = getOperation(entityBean, nameMethod);
            if (firstParameterFromInserirIsMap(save)) {
                return saveMap(entityBean, map, save);
            }
            return saveVO(entityBean, nameEntity, map, save);
        } catch (Exception e) {
            HandleException.handle(e);
            throw new WebServiceException(e);
        }
    }

    private Object save(String nameEntity, String nameMethod, Object myEntity) throws WebServiceException {
        try {
            nameEntity = converterNomeDaClasseEmNomeEntidade(nameEntity);
            Object entityBean = getMetodoEjbInterface(nameEntity);
            Method save = getOperation(entityBean, nameMethod);
            if (firstParameterFromInserirIsMap(save)) {
                return saveMap(entityBean, (Map<String, Object>) myEntity, save);
            }
            return saveVO(entityBean, nameEntity, myEntity, save);
        } catch (Exception e) {
            throw new WebServiceException(e);
        }
    }

    /**
     * @apiNote Deve ser chamado quando houver a necessidade de invocar o método inserir da Interface da entidade que chamará o bean session da entidade, o método inserir da entidade que há como parâmetro o VO da entidade.
     */
    private Object saveVO(Object entityBean, String nameEntity, Object myEntity, Method saveMethod) throws WebServiceException, ERKNoPermissionException, ERKNoContentException, ERKDuplicateKeyException, ERKBusinessException, ERKNotFoundException {
        try {
            Class voClass = getVOClass(nameEntity);
            if(ERKMapUtils.isMap(myEntity)){
                myEntity = ERKMapUtils.mapToVO((Map) myEntity, voClass);
            }
            return saveMethod.invoke(entityBean, myEntity, register.getUsuario(), register.getErkFilial());
        } catch (Exception e) {
            HandleException.handle(e);
            throw new WebServiceException(e);
        }
    }

    private boolean firstParameterFromInserirIsMap(Method save) {
        return save.getParameterTypes()[0].getName().equals("java.util.Map");
    }

    private Boolean ehMap(Method metodo) {
        for (Object paramType : metodo.getParameterTypes()) {
            if (((Class) paramType).getName().equals("java.util.Map")) {
                return true;
            }
        }
        return false;
    }

    /**
     * @apiNote Deve ser chamado quando houver a necessidade de invocar o método inserir da Interface da entidade que chamará o bean session da entidade, o método inserir da entidade que há como parâmetro o Map da entidade.
     */
    private Object saveMap(Object entityBean, Map<String, Object> map, Method saveMethod) throws ERKNoPermissionException, WebServiceException, ERKNoContentException, ERKDuplicateKeyException, ERKBusinessException, ERKNotFoundException {
        try {
            return saveMethod.invoke(entityBean, map, register.getUsuario(), register.getErkFilial());
        } catch (Exception e) {
            HandleException.handle(e);
            throw new WebServiceException(e);
        }
    }

    private Class getVOClass(String nameEntity) throws WebServiceException {
        try {
            return Class.forName("com.company.value." + nameEntity + "VO");
        } catch (ClassNotFoundException e) {
            throw new WebServiceException("Não foi possível encontrar a classe VO para a entidade " + nameEntity + ". Verifique a nomenclatura da classe VO. " + e.getMessage());
        }
    }

    private Boolean excluir(String className, String nameMethod, Object uid) throws WebServiceException, ERKNoPermissionException, ERKNoContentException, ERKNotFoundException, ERKBusinessException, ERKDuplicateKeyException {
        try {
            className = converterNomeDaClasseEmNomeEntidade(className);
            Object entityBean = getMetodoEjbInterface(className);
            Method excluir = getOperation(entityBean, nameMethod);
            if (ERKMapUtils.isMap(uid)) {
                //Excluir para chave composta
                excluir.invoke(entityBean, uid, register.getUsuario(), register.getErkFilial());
                return true;
            }
            //Excluir para chave simples
            excluir.invoke(entityBean, uid, register.getUsuario(), register.getErkFilial());
            return true;
        } catch (Exception e) {
            HandleException.handle(e);
            throw new WebServiceException(e);
        }
    }

    private Object getMetodoEjbInterface(String nameEntity) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, WebServiceException {
        try {
            return EJBInterfaces.get(Class.forName("com.company.ejb.interfaces.cadastros." + nameEntity));
        } catch (ClassNotFoundException e) {
            try {
                return EJBInterfaces.get(Class.forName("com.company.ejb.interfaces." + nameEntity));
            } catch (ClassNotFoundException ex) {
                try {
                    return EJBClient.getEjbHome(nameEntity, Class.forName("com.company.ejb.interfaces.cadastros." + nameEntity + "Home"));
                } catch (ClassNotFoundException exc) {
                    try{
                        return EJBInterfaces.get(Class.forName("com.company.ejb.interfaces.projetok." + nameEntity));
                    } catch (ClassNotFoundException cl) {
                        throw new WebServiceException("Nao foi possivel encontrar a interface da entidade.");
                    }
                }
            }
        }
    }

    private Method getOperation(Object entityBean, String operationName) throws WebServiceException {
        Method retorno = null;
        try {
            return entityBean.getClass().getMethod(operationName, (Class<Map<String, Object>>) (Class) Map.class, Integer.class, Integer.class);
        } catch (NoSuchMethodException e) {
            for (Method method : entityBean.getClass().getMethods()) {
                if (method.getName().matches(operationName)) {
                    if (retorno != null) {
                        throw new WebServiceException("Há mais de um método com o nome " + operationName);
                    }
                    retorno = method;
                }
            }
            return retorno;
        }

    }


}
