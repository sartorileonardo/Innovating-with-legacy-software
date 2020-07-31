package com.company.ejb.session.cadastros;

/**
 * Imports
 */

/**
 * @author Leonardo Sartori
 * @author Lucas Fernando Frighetto
 * Esta classe deve ser incada por herança quando o desenvolvedor precisar de um CRUD simples,
 * recomenda-se utilizar em conjunto com a CadastroController.
 */
public abstract class CadastroBean implements ICadastroBean, SessionBean {

    private static final long serialVersionUID = 3159351593135611848L;
    Context jndiContext;
    SessionContext context;

    /**
     * @apiNote Utilizado para inserir uma entidade com chave simples
     * @param className
     * @param entity
     * @param codUsuario
     * @param codFilial
     * @param modulo
     * @param primaryKeyFieldName
     * @return retorna a chave do registro inserido
     * @implNote
     * public Integer inserir(SetoresVO myEntity, Integer codUsuario, Integer codFilial) throws Exception {
     * 		return (Integer) inserir(this.getClass().getSimpleName(), myEntity, codUsuario,  codFilial, MODULO_SETORES, "codigo");
     *}
     */
    public Object inserir(String className, Object entity, Integer codUsuario, Integer codFilial, String modulo, String primaryKeyFieldName) throws Exception {
        return inserir(className, entity, codUsuario, codFilial, modulo, primaryKeyFieldName, null, true);
    }

    /**
     * @apiNote Utilizado para inserir uma entidade quando esta entidade possui chave estrangeira declarada no Bean do CMP
     * @param className
     * @param entity
     * @param codUsuario
     * @param codFilial
     * @param modulo
     * @param primaryKeyFieldName
     * @param foreignKeysCompositeName
     * @return retorna a chave do registro inserido
     * @implNote
     * public Integer inserir(SetoresVO myEntity, Integer codUsuario, Integer codFilial) throws Exception {
     * 		return (Integer) inserir(this.getClass().getSimpleName(), myEntity, codUsuario,  codFilial, MODULO_SETORES, "codigo");
     *}
     */
    public Object inserir(String className, Object entity, Integer codUsuario, Integer codFilial, String modulo, String primaryKeyFieldName, Map<String, String> foreignKeysCompositeName) throws Exception {
        return inserir(className, entity, codUsuario, codFilial, modulo, primaryKeyFieldName, foreignKeysCompositeName, true);
    }

    public Object inserir(String className, Object objetoEntidade, Integer codUsuario, Integer codFilial, String modulo, String primaryKeyFieldName, Map<String, String> foreignKeysCompositeName, Boolean autoincremento) throws Exception {
        try {
            verificarPermissao(codUsuario, codFilial, modulo, "inserir");
            Map<String, Object> entidade = getMap(objetoEntidade);
            if (autoincremento) {
                setPrimaryKeyAutoIncrementDefaultValueCMP(entidade, primaryKeyFieldName);
            }
            if (foreignKeysCompositeName != null) {
                parseMapToLocalHome(entidade, foreignKeysCompositeName);
            }
            return create(className, primaryKeyFieldName, entidade);
        } catch (Exception e) {
            rollBackTransaction();
            throw e;
        }
    }

    /**
     * @apiNote Utilizado para inserir uma entidade quando esta entidade possui chave composta
     * @param className
     * @param entity
     * @param codUsuario
     * @param codFilial
     * @param modulo
     * @param uidReturn
     * @param compositeKeys
     * @return retorna a chave do registro inserido
     * @implNote
     * public Integer inserir(Map<String, Object> quebra, Integer codUsuario, Integer codFilial) throws CreateException, RemoteException, Exception {
     *         return (Integer) inserirComChaveComposta(this.getClass().getSimpleName(), quebra, codUsuario, codFilial, MODULO_QUEBRA_PARAMETRO_LIVRO_CONTABIL, "numero", "numero", "livrocontabil", "parametrolivrocontabil");
     *}
     */
    public Object inserirComChaveComposta(String className, Object entity, Integer codUsuario, Integer codFilial, String modulo, String uidReturn, String... compositeKeys) throws ERKNoPermissionException, ERKDuplicateKeyException, ERKNotFoundException, ERKBusinessException {
        try {
            verificarPermissao(codUsuario, codFilial, modulo, "inserir");
            Map<String, Object> map = getMap(entity);
            return create(className, map, uidReturn, compositeKeys);
        } catch (Exception e) {
            rollBackTransaction();
            HandleException.handle(e);
            throw new BeanException(e.getMessage());
        }
    }

    /**
     * @apiNote Utilizado para alterar uma entidade quando esta entidade possui chave estrangeira declarada no Bean do CMP
     * @param className
     * @param entity
     * @param codUsuario
     * @param codFilial
     * @param modulo
     * @param primaryKeyFieldName
     * @param foreignKeys
     * @return retorna a chave do registro inserido
     * @implNote
     * public Integer alterar(TelasVO myEntity, Integer codUsuario, Integer codFilial) throws CreateException, RemoteException, Exception {
     *         return (Integer) alterar(this.getClass().getSimpleName(), myEntity, codUsuario, codFilial, MODULO_TELAS, "codigo", setAttributeBeanCMPForeignKey("modulo", "ModulosBean"));
     *}
     */
    public Object alterar(String className, Object entity, Integer codUsuario, Integer codFilial, String modulo, String primaryKeyFieldName, Map<String, String> foreignKeys) throws Exception {
        try {
            verificarPermissao(codUsuario, codFilial, modulo, "alterar");
            Map<String, Object> map = getMap(entity);
            verificarPermissao(codUsuario, codFilial, modulo, "alterar");
            if (foreignKeys != null) {
                parseMapToLocalHome(map, foreignKeys);
            }
            return update(className, map, primaryKeyFieldName);
        } catch (Exception e) {
            rollBackTransaction();
            throw e;
        }
    }

    /**
     * @apiNote Utilizado para inserir uma entidade com chave simples
     * @param className
     * @param entity
     * @param codUsuario
     * @param codFilial
     * @param modulo
     * @param primaryKeyFieldName
     * @return retorna a chave do registro inserido
     * @implNote
     * public Integer alterar(SetoresVO myEntity, Integer codUsuario, Integer codFilial) throws Exception {
     * 		return (Integer) alterar(this.getClass().getSimpleName(), myEntity, codUsuario,  codFilial, MODULO_SETORES, "codigo");
     *}
     */
    public Object alterar(String className, Object entity, Integer codUsuario, Integer codFilial, String modulo, String primaryKeyFieldName) throws Exception {
        return alterar(className, entity, codUsuario, codFilial, modulo, primaryKeyFieldName, null);
    }

    /**
     * @apiNote Utilizado para alterar uma entidade quando esta entidade possui chave composta
     * @param className
     * @param entity
     * @param codUsuario
     * @param codFilial
     * @param modulo
     * @param uidReturn
     * @param compositeKeys
     * @return retorna a chave do registro inserido
     * @implNote
     * public Integer alterar(Map<String, Object> quebra, Integer codUsuario, Integer codFilial) throws CreateException, RemoteException, Exception {
     *         return (Integer) alterarComChaveComposta(this.getClass().getSimpleName(), quebra, codUsuario, codFilial, MODULO_QUEBRA_PARAMETRO_LIVRO_CONTABIL, "numero", "numero", "livrocontabil", "parametrolivrocontabil");
     *}
     */
    public Object alterarComChaveComposta(String className, Object entity, Integer codUsuario, Integer codFilial, String modulo, String uidReturn, String... compositeKeys) throws Exception {
        try {
            verificarPermissao(codUsuario, codFilial, modulo, "alterar");
            Map<String, Object> map = getMap(entity);
            return update(className, map, uidReturn, compositeKeys);
        } catch (Exception e) {
            rollBackTransaction();
            throw e;
        }
    }


    /**
     * @apiNote Utilizado para excluir uma entidade de chave simples
     * @param className
     * @param codUsuario
     * @param codFilial
     * @param modulo
     * @param uid
     * @return retorna true em caso de sucesso
     * @implNote
     * public Boolean excluir(java.lang.Integer codigo, Integer codUsuario, Integer codFilial) throws Exception {
     *         return excluir(this.getClass().getSimpleName(), codUsuario, codFilial, MODULO_CARD, codigo);
     *}
     */
    public Boolean excluir(String className, Integer codUsuario, Integer codFilial, String modulo, Object uid) throws ERKNoPermissionException, ERKNoContentException, ERKNotFoundException, ERKDuplicateKeyException, InvocationTargetException, NoSuchMethodException, RemoveException, IllegalAccessException, ERKBusinessException {
        try {
            verificarPermissao(codUsuario, codFilial, modulo, "excluir");
            Object home = getBeanCMPClass(className);
            return remove(uid, home);
        } catch (Exception e) {
            rollBackTransaction();
            throw e;
        }
    }

    private Boolean remove(Object uid, Object home) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoveException, ERKDuplicateKeyException, ERKNotFoundException, ERKBusinessException {
        try {
            EJBLocalObject localHome = (EJBLocalObject) home.getClass().getMethod("findByPrimaryKey", uid.getClass()).invoke(home, uid);
            localHome.remove();
            return true;
        } catch (Exception e) {
            HandleException.handle(e);
            throw new BeanException("Erro ao excluir " + e.getMessage());
        }
    }

    /**
     * @apiNote Utilizado para excluir uma entidade de chave composta
     * @param className
     * @param codUsuario
     * @param codFilial
     * @param modulo
     * @param compositeKeys
     * @return retorna true em caso de sucesso
     * @implNote
     * public Boolean excluir(Map<String, Object> compositeKeys, Integer codUsuario, Integer codFilial) throws CreateException, RemoteException, Exception {
     *         return excluirComChaveComposta(this.getClass().getSimpleName(), codUsuario, codFilial, MODULO_QUEBRA_PARAMETRO_LIVRO_CONTABIL, compositeKeys);
     *}
     */
    public Boolean excluirComChaveComposta(String className, Integer codUsuario, Integer codFilial, String modulo, Map<String, Object> compositeKeys) throws ERKNoPermissionException, ERKNoContentException, ERKNotFoundException, ERKDuplicateKeyException, InvocationTargetException, NoSuchMethodException, InstantiationException, RemoveException, IllegalAccessException, ERKBusinessException {
        try {
            verificarPermissao(codUsuario, codFilial, modulo, "excluir");
            String[] keysName = ERKMapUtils.getKeysFromMap(compositeKeys);
            Object home = getBeanCMPClass(className);
            Object pk = getPkClass(className);

            setPublicAttributesFromPKClass(compositeKeys, pk, keysName);
            return remove(pk, home);
        } catch (Exception e) {
            rollBackTransaction();
            throw e;
        }
    }

    /**
     * @implNote Recebe um nome da classe da entidade, nome da chave primária, map com atributos da entidade e faz a invocação do método ejbCreate da classe Bean(CMP) da entidade.
     */
    private Object create(String className, String primaryKeyFieldName, Map<String, Object> map) throws ERKDuplicateKeyException, ERKNotFoundException, ERKBusinessException {
        primaryKeyFieldName = primaryKeyFieldName.substring(0, 1).toUpperCase() + primaryKeyFieldName.substring(1);
        try {
            Object home = getBeanCMPClass(className);
            Method create = home.getClass().getMethod("create", (Class<Map<String, Object>>) (Class) Map.class);
            Object local = create.invoke(home, map);
            return local.getClass().getMethod("get" + primaryKeyFieldName).invoke(local);
        } catch (Exception e) {
            rollBackTransaction();
            HandleException.handle(e);
            throw new BeanException("Erro ao salvar " + className + ". " + e.getMessage());
        }
    }

    /**
     * @implNote Recebe um nome da classe da entidade, map com atributos da entidade, nome do atributo de retorno (parte da chave primária), lista de atributos chave composta e faz a invocação do método ejbCreate da classe Bean(CMP) da entidade.
     */
    private Object create(String className, Map<String, Object> map, String uidReturn, String... compositeKeys) throws ERKDuplicateKeyException, ERKNotFoundException, ERKBusinessException {
        String nameMethodReturnFromCMPClass = "get" + uidReturn.substring(0, 1).toUpperCase() + uidReturn.substring(1);
        try {
            Object pk = getPkClass(className);
            setPublicAttributesFromPKClass(map, pk, compositeKeys);
            Object home = getBeanCMPClass(className);
            Method create = home.getClass().getMethod("create", (Class<Map<String, Object>>) (Class) Map.class);
            Object local = create.invoke(home, map);
            local = home.getClass().getMethod("findByPrimaryKey", pk.getClass()).invoke(home, pk);

            return local.getClass().getMethod(nameMethodReturnFromCMPClass).invoke(local);
        } catch (Exception e) {
            rollBackTransaction();
            HandleException.handle(e);
            throw new BeanException("Erro ao salvar " + className + ". " + e.getMessage());
        }
    }

    /**
     * @implNote Recebe uma instancia da classe EntityPK e faz o set dos atributos públicos da classe que formam a chave composta.
     */
    private void setPublicAttributesFromPKClass(Map<String, Object> map, Object pk, String[] compositeKeys) throws IllegalAccessException {
        for (String key : compositeKeys) {
            for (Field field : pk.getClass().getFields()) {
                if (field.getName().equalsIgnoreCase(key)) {
                    if (ERKMapUtils.isMap(map.get(key))) {
                        Map<String, Object> subMap = (Map<String, Object>) map.get(key);
                        for (Map.Entry<String, Object> entry : subMap.entrySet()) {
                            String subKey = entry.getKey();
                            Object subValue = entry.getValue();

                            if (subValue instanceof Integer && subKey.contains(key)) {
                                //Substituicao valor do subObjeto para atributo do objeto
                                map.replace(key, subValue);
                                //Set atributo chave da classe PK
                                field.set(pk, subValue);
                            }

                        }
                    } else {
                        field.set(pk, map.get(key));
                    }
                }
            }
        }

    }

    /**
     * @implNote Nas entidades de chave inteira auto increment, o método substitui o valor da chave por zero, este procedimento era feito manualmente anteriormente no método EJBCreate na entidade CMP.
     */
    private void setPrimaryKeyAutoIncrementDefaultValueCMP(Map<String, Object> map, String primaryKeyFieldName) {
        if (map.get(primaryKeyFieldName) == null) {
            map.replace(primaryKeyFieldName, 0);
        }
    }

    /**
     * @implNote Recebe um nome da classe PK e faz a invocação de uma instância desta classe.
     */
    private Object getPkClass(String className) throws InstantiationException, IllegalAccessException {
        className = className.toCharArray()[0] + className.substring(1).toLowerCase().replace("bean", "PK");
        try {
            return Class.forName("com.company.ejb.cmp." + className).newInstance();
        } catch (ClassNotFoundException n) {
            throw new BeanException("Não foi possível encontrar a classe " + className);
        }
    }


    /**
     * @implNote Recebe um objeto que pode ser um MAP ou VO e faz sua conversão para retornar um MAP ou VO.
     */
    private Map<String, Object> getMap(Object entidade) {
        return ERKMapUtils.isMap(entidade) ? (Map<String, Object>) entidade : ERKMapUtils.VOToMap(entidade);
    }

    /**
     * @implNote Recebe um nome de classe e retorna a invocação da instancia da classe via JNDI.
     */
    private Object getBeanCMPClass(String className) {
        try {
            return jndiContext.lookup("java:comp/env/" + className.toCharArray()[0] + className.substring(1).toLowerCase().replace("bean", "Bean"));
        } catch (NamingException e) {
            throw new BeanException("Não foi possível encontrar a classe " + className + " na estrutura CMP. " + e.getExplanation());
        }
    }

    /**
     * @implNote Recebe um nome de atributo que é considerado chave estrangeira(Composição) na classe de bean do CMP e seu tipo de bean e retorna um map com estes dados.
     */
    protected Map<String, String> setAttributeBeanCMPForeignKey(String nameKeyCMP, String parameterTypeKeyCMP) {
        return ERKMapUtils.toHashMapString(nameKeyCMP, parameterTypeKeyCMP);
    }

    /**
     * @implNote Recebe o nome da classe entidade, map de atributos da entidade, lista de chaves que formam sua chave composta e atualiza os dados via bean do CMP para entidade de chave composta.
     */
    private Object update(String className, Map<String, Object> map, String uidReturn, String... compositeKeys) throws Exception {
        try {
            String nameMethodReturnFromCMPClass = "get" + uidReturn.substring(0, 1).toUpperCase() + uidReturn.substring(1);
            Object home = getBeanCMPClass(className);
            Object pk = getPkClass(className);

            setPublicAttributesFromPKClass(map, pk, compositeKeys);

            Object local = home.getClass().getMethod("findByPrimaryKey", pk.getClass()).invoke(home, pk);

            setEntityCMPBean(map, uidReturn, local);

            return local.getClass().getMethod(nameMethodReturnFromCMPClass).invoke(local);
        } catch (InvocationTargetException i) {
            throw new ERKNotFoundException("Não foi encontrado registro com este identificador.");
        }
    }

    /**
     * @implNote Recebe o nome da classe entidade, map de atributos da entidade, nome da chave primária e atualiza os dados via bean do CMP para entidade de chave simples (ex: codigo ou id inteiro).
     */
    private Object update(String className, Map<String, Object> map, String pkeyName) throws Exception {
        try {
            Object home = getBeanCMPClass(className);
            Object localHome = home.getClass().getMethod("findByPrimaryKey", map.get(pkeyName).getClass()).invoke(home, map.get(pkeyName));
            setEntityCMPBean(map, pkeyName, localHome);
            return ((EJBLocalObject) localHome).getPrimaryKey();
        } catch (Exception e) {
            HandleException.handle(e);
            throw new BeanException("Erro ao salvar " + className + ". " + e.getMessage());
        }
    }

    /**
     * @implNote Recebe um map com os atributos da entidade, um nome de chave primária e uma instância de objeto local e faz a passagem de valores para os métodos set da classe bean do CMP.
     */
    private Object setEntityCMPBean(Map<String, Object> map, String pkeyName, Object localHome) throws IllegalAccessException, InvocationTargetException {
        for (Method localHomeMethod : localHome.getClass().getMethods()) {
            String methodName = localHomeMethod.getName();
            if (methodName.startsWith("set")) {
                methodName = methodName.substring("set".length());
                String key = ERKMapUtils.getIgnoreCaseKey(methodName, map);
                if (key != null && !key.equalsIgnoreCase(pkeyName)) {
                    try {
                        Class voAttributeType = localHomeMethod.getParameterTypes()[0];
                        Object setValue = voAttributeType.getName().startsWith("com.company.ejb.cmp.") ? map.get(key) : ERKMapUtils.parseFromMap(voAttributeType, key, map);
                        localHomeMethod.invoke(localHome, setValue);
                    } catch (ClassCastException caste) {
                        if (caste.getMessage().matches("Cannot cast java.lang.Boolean to boolean")) {
                            localHomeMethod.invoke(localHome, (Boolean) (map.get(key)));
                        }
                    }
                }
            }
        }
        return ((EJBLocalObject) localHome).getPrimaryKey();
    }

    private void parseMapToLocalHome(Map<String, Object> map, Map<String, String> foreignKeys) throws Exception {
        for (Map.Entry<String, String> entry : foreignKeys.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();

            Object localHome = getBeanCMPClass(value);

            for (Method method : localHome.getClass().getMethods()) {
                if (method.getName().matches("findByPrimaryKey")) {
                    Class voAttributeType = method.getParameterTypes()[0];
                    Object setValue = ERKMapUtils.parseFromMap(voAttributeType, key, map);
                    if (map.get(key) != null) {
                        map.put(key, method.invoke(localHome, setValue));
                    }
                }
            }
        }
    }

    /**
     * @implNote Recebe os dados do usuário e filial e retorna excessões caso o usuário não tenha permissão para realizar alguma das quatro operações (consultar, inserir, alterar, excluir).
     */
    private void verificarPermissao(Integer codUsuario, Integer codFilial, String modulo, String operacao) throws ERKNoPermissionException, ERKNotFoundException {
        try {
            if (modulo != null) {
                moduloExist(modulo);
                String MSG_PERMISSAO = "Usuario " + codUsuario + " da Filial " + codFilial + " sem permissao para " + operacao + ".";
                if (operacao.equalsIgnoreCase("inserir")) {
                    if (!getPermissoesUsuario(codUsuario, modulo).getInserir()) {
                        throw new ERKNoPermissionException(MSG_PERMISSAO);
                    }
                } else if (operacao.equalsIgnoreCase("alterar")) {
                    if (!getPermissoesUsuario(codUsuario, modulo).getAlterar()) {
                        throw new ERKNoPermissionException(MSG_PERMISSAO);
                    }
                } else if (operacao.equalsIgnoreCase("excluir")) {
                    if (!getPermissoesUsuario(codUsuario, modulo).getExcluir()) {
                        throw new ERKNoPermissionException(MSG_PERMISSAO);
                    }
                } else if (operacao.equalsIgnoreCase("consultar")) {
                    if (!getPermissoesUsuario(codUsuario, modulo).getConsultar()) {
                        throw new ERKNoPermissionException(MSG_PERMISSAO);
                    }
                } else if (operacao.equalsIgnoreCase("nivel1")) {
                    if (!getPermissoesUsuario(codUsuario, modulo).getNivel1()) {
                        throw new ERKNoPermissionException(MSG_PERMISSAO);
                    }
                } else if (operacao.equalsIgnoreCase("nivel2")) {
                    if (!getPermissoesUsuario(codUsuario, modulo).getNivel2()) {
                        throw new ERKNoPermissionException(MSG_PERMISSAO);
                    }
                } else if (operacao.equalsIgnoreCase("nivel3")) {
                    if (!getPermissoesUsuario(codUsuario, modulo).getNivel3()) {
                        throw new ERKNoPermissionException(MSG_PERMISSAO);
                    }
                }
            }

        } catch (ERKNoPermissionException n) {
            throw new ERKNoPermissionException(n.getMessage());
        } catch (ERKNotFoundException n) {
            throw new ERKNotFoundException(n.getMessage());
        } catch (Exception e) {
            throw new BeanException("Ocorreu um erro ao consultar as permissões de usuário." + e.getMessage());
        }
    }

    private void moduloExist(String modulo) throws Exception {
        Sql sql = this.getSQL();
        try {
            ResultSet rs = sql.consultar(SQLModulos.getModulo(null, modulo));
            if(!rs.first()){
                throw new ERKNotFoundException("O modulo: " + modulo + " não foi encontrado. Verifique se o módulo utilizado consta na classe Bean e tabela modulos.");
            }
        } catch (ERKNotFoundException nt) {
            throw new ERKNotFoundException(nt);
        } finally {
            if (sql != null) {
                sql.remove();
            }
        }
    }


    /**
     *@implNote Recebe os dados de usuário e módulo para consultar a permissão
     */
    protected PermissoesUsuario getPermissoesUsuario(Integer codUsuario, String modulo) {
        try {
            Object ref = jndiContext.lookup("Usuarios");
            UsuariosHome homeUsuarios = (UsuariosHome) PortableRemoteObject.narrow(ref, UsuariosHome.class);
            Usuarios usuarios = homeUsuarios.create();
            return usuarios.permissaoModulo(codUsuario, modulo);
        } catch (Exception e) {
            throw new BeanException("Erro ao consultar as permissoes do usuario para o modulo \"" + modulo + "\"");
        }
    }

    /**
     *@implNote Utilizado para reaproveitar uma instancia de SQL
     *@return Retorna uma instancia de SQL.
     */
    protected Sql getSQL() throws Exception {
        Sql sql;
        SqlHome home = (SqlHome) jndiContext.lookup("java:comp/env/SqlBean");
        sql = home.create();
        return sql;
    }

    private void rollBackTransaction() {
        this.context.setRollbackOnly();
    }


    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }

    public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
        context = arg0;
    }

    public void ejbCreate() throws CreateException, NamingException {
        jndiContext = new InitialContext();
    }
}
