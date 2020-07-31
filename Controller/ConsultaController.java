package br.com.company.webservice.controller;

/**
 * Imports
 */

/**
 * @author Leonardo Sartori
 * @author Lucas Fernando Frighetto
 */
@Controller
public class ConsultaController {

    /**
     * @apiNote   Utilizado para consultar um registro por ID.
     * @return retorna um registro por ID.
     * @param tableName nome da tabela no database
     * @param uid valor do campo ID
     * @param columnName nome do campo ID
     * @implNote
     * public SetoresVO get(String uid) throws WebServiceException {
     *         return consultaController.get("setores", uid, "codigo");
     * }
     */
    public <T> T get(String tableName, Object uid, String columnName) throws WebServiceException {
        try {
            tableName = converterNomeDaClasseEmNomeTabela(tableName);
            return EJBInterfaces.get(Consultas.class).get(tableName, uid, columnName);
        } catch (Exception e) {
            throw new WebServiceException("Erro ao consultar, tabela: " + tableName + ", uid: " + uid + ", colunas: " + columnName, e);
        }
    }

    /**
     * @apiNote   Utilizado para consultar um registro por ID.
     * @return retorna um registro por ID.
     * @param tableName nome da tabela no database
     * @param classe classe da entidade
     * @param uid valor do campo ID
     * @implNote
     * public SetoresVO get(String codigo) throws WebServiceException {
     *         return consultaController.get("setores", SetoresVO.class, codigo);
     * }
     */
    public <T> T get(String tableName, Class<T> classe, Object uid) throws WebServiceException {
        return get(tableName,classe, uid, false, null);
    }

    /**
     * @apiNote   Utilizado para consultar um registro por ID.
     * @return retorna um registro por ID.
     * @param tableName nome da tabela no database
     * @param classe classe da entidade
     * @param uid valor do campo ID
     * @param isCrudERP true caso for um cadastro do erp onde são concatenados o codigo e descricao das chaves estrangeiras, false caso contrario
     * @implNote
     * public SetoresVO get(String codigo) throws WebServiceException {
     *         return consultaController.get("setores", SetoresVO.class, codigo);
     * }
     */
   public <T> T get(String tableName, Class<T> classe, Object uid, Boolean isCrudERP) throws WebServiceException {
        return get(tableName,classe, uid, isCrudERP, null);
    }

    /**
     * @apiNote   Utilizado para consultar um registro por ID.
     * @return retorna um registro por ID.
     * @param tableName nome da tabela no database
     * @param classe classe da entidade
     * @param uid valor do campo ID
     * @param columns colunas do database
     * @implNote
     * public TelasVO get(String codigo) throws WebServiceException {
     *         return consultaController.get("telas", TelasVO.class, codigo, ERKMapUtils.toHashset("codigo", "nome", "route"));
     * }
     */
    public <T> T get(String tableName, Class<T> classe, Object uid, HashSet<String> columns) throws WebServiceException {
        return get(tableName,classe, uid, false, columns);
    }

    /**
     * @apiNote   Utilizado para consultar um registro por ID.
     * @param tableName nome da tabela no database
     * @param classe classe da entidade
     * @param uid valor do campo ID
     * @param columns colunas do database
     * @param isCrudERP true caso for um cadastro do erp onde são concatenados o codigo e descricao das chaves estrangeiras, false caso contrario
     * @return retorna um registro por ID.
     * @implNote
     * public TelasVO get(String codigo) throws WebServiceException {
     *         return consultaController.get("telas", TelasVO.class, codigo, ERKMapUtils.toHashset("codigo", "nome", "route"));
     * }
     */
    public <T> T get(String tableName, Class<T> classe, Object uid, Boolean isCrudERP, HashSet<String> columns)  throws WebServiceException {
        try {
            tableName = converterNomeDaClasseEmNomeTabela(tableName);
            return EJBInterfaces.get(Consultas.class).get(tableName, classe, uid, isCrudERP, columns);
        } catch (Exception e) {
            throw new WebServiceException("Erro ao consultar, tablea: " + tableName + ", uid: " + uid, e);
        }
    }

    /**
     * @apiNote Utilizado para consultar um registro por ID.
     * @param tableName nome da tabela no database
     * @param uid valor do campo ID
     * @return retorna um registro por ID.
     * @implNote
     * public Map<String, Object> get(String uid) throws WebServiceException {
     *         return consultaController.get("tefconfiguracoes", uid);
     * }
     */
    public Map<String, Object> get(String tableName, Object uid)  throws WebServiceException {
        return get(tableName, uid, false, new HashSet<>());
    }

    /**
     * @apiNote Utilizado para consultar um registro por ID.
     * @param tableName nome da tabela no database
     * @param uid valor do campo ID
     * @param isCrudERP true caso for um cadastro do erp onde são concatenados o codigo e descricao das chaves estrangeiras, false caso contrario
     * @return retorna um registro por ID.
     * @implNote
     * public Map<String, Object> get(String uid) throws WebServiceException {
     *         return consultaController.get("tefconfiguracoes", uid, true);
     * }
     */
    public Map<String, Object> get(String tableName, Object uid, Boolean isCrudERP) throws WebServiceException {
        return get(tableName, uid, isCrudERP, new HashSet<>());
    }

    /**
     * @apiNote   Utilizado para consultar um registro por ID.
     * @return retorna um registro por ID.
     * @param tableName nome da tabela no database
     * @param uid valor do campo ID
     * @param columns colunas do database
     * @implNote
     * public Map<String, Object> get(String codigo) throws WebServiceException {
     *         return consultaController.get("telas", codigo, "codigo", "nome", "route");
     * }
     */
    public Map<String, Object> get(String tableName, Object uid, String... columns)  throws WebServiceException {
        return get(tableName, uid, ERKMapUtils.toHashSet(columns));
    }

    /**
     * @apiNote   Utilizado para consultar um registro por ID.
     * @return retorna um registro por ID.
     * @param tableName nome da tabela no database
     * @param uid valor do campo ID
     * @param columns colunas do database
     * @implNote
     * public Map<String, Object> get(String codigo) throws WebServiceException {
     *         return consultaController.get("telas", codigo, ERKMapUtils.toHashset("codigo", "nome", "route"));
     * }
     */
    public Map<String, Object> get(String tableName, Object uid, HashSet<String> columns) throws WebServiceException {
        return get(tableName, uid, false, columns);
    }

    /**
     * @apiNote   Utilizado para consultar um registro por ID.
     * @param tableName nome da tabela no database
     * @param uid valor do campo ID
     * @param columns colunas do database
     * @param isCrudERP true caso for um cadastro do erp onde são concatenados o codigo e descricao das chaves estrangeiras, false caso contrario
     * @return retorna um registro por ID.
     * @implNote
     * public Map<String, Object> get(String codigo) throws WebServiceException {
     *         return consultaController.get("telas", codigo, true, "codigo", "nome", "route");
     * }
     */
    public Map<String, Object> get(String tableName, Object uid, Boolean isCrudERP, String... columns)  throws WebServiceException {
        return get(tableName, uid, isCrudERP, ERKMapUtils.toHashSet(columns));
    }

    /**
     * @apiNote   Utilizado para consultar um registro por ID.
     * @param tableName nome da tabela no database
     * @param uid valor do campo ID
     * @param isCrudERP true caso for um cadastro do erp onde são concatenados o codigo e descricao das chaves estrangeiras, false caso contrario
     * @param columns colunas do database
     * @return retorna um registro por ID.
     * @implNote
     * public Map<String, Object> get(String codigo) throws WebServiceException {
     *         return consultaController.get("telas", codigo, false, ERKMapUtils.toHashset("codigo", "nome", "route"));
     * }
     */
    public Map<String, Object> get(String tableName, Object uid, Boolean isCrudERP, HashSet<String> columns) throws WebServiceException {
        try {
            tableName = converterNomeDaClasseEmNomeTabela(tableName);
            return EJBInterfaces.get(Consultas.class).get(tableName, uid, isCrudERP, columns);
        } catch (Exception e) {
            throw new WebServiceException("Erro ao consultar, tablea: " + tableName + ", uid: " + uid, e);
        }
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros com as colunas especificadas no hashset.
     * @param tableName nome da tabela no database
     * @param classe classe da entidade
     * @param columns colunas do database
     * @return retorna uma lista de registros do VO especificado via parametro
     * @implNote
     * public List<SetoresVO> getAll(String filter) throws WebServiceException {
     *         return consultaController.getAll("setores", SetoresVO.class, ERKMapUtils.toHashset("codigo", "descricao"));
     * }
     */
    public <T> List<T> getAll(String tableName, Class<T> classe, HashSet<String> columns)  throws WebServiceException {
        return getAll(tableName, classe, null, columns, false);
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros por filtro.
     * @param tableName nome da tabela no database
     * @param classe classe da entidade
     * @param searchInput filtro digitado pelo usuario em um campo de busca
     * @return retorna uma lista de registros do VO especificado via parametro
     * @implNote
     * public List<SetoresVO> getAll(String filter) throws WebServiceException {
     *         return consultaController.getAll("setores", SetoresVO.class, filter);
     * }
     */
    public <T> List<T> getAll(String tableName, Class<T> classe, String searchInput) throws WebServiceException {
        return getAll(tableName, classe, searchInput, null, false);
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros por filtro com possibilidade de concatenação do codigo e descricao.
     * @param tableName nome da tabela no database
     * @param classe classe da entidade
     * @param searchInput filtro digitado pelo usuario em um campo de busca
     * @param concatDescCod true caso preciso concatenar codigo com descricao, caso contrario false
     * @return retorna uma lista de registros do VO especificado via parametro
     * @implNote
     * public List<SetoresVO> getAll(String filter) throws WebServiceException {
     *         return consultaController.getAll("setores", SetoresVO.class, filter, true);
     * }
     */
    public <T> List<T> getAll(String tableName, Class<T> classe, String searchInput, Boolean concatDescCod)  throws WebServiceException {
        return getAll(tableName, classe, searchInput, null, concatDescCod);
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros por filtro e escolha de colunas pelo hashset.
     * @param tableName nome da tabela no database
     * @param classe classe da entidade
     * @param searchInput filtro digitado pelo usuario em um campo de busca
     * @param columns colunas do database
     * @return retorna uma lista de registros do VO especificado via parametro
     * @implNote
     * public List<SetoresVO> getAll(String filter) throws WebServiceException {
     *         return consultaController.getAll("setores", SetoresVO.class, filter, ERKMapUtils.toHashset("codigo", "descricao"));
     * }
     */
    public <T> List<T> getAll(String tableName, Class<T> classe, String searchInput, HashSet<String> columns) throws WebServiceException {
        return getAll(tableName, classe, searchInput, columns, false);
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros por filtro com possibilidade de concatenação e escolha de colunas pelo hashset.
     * @param tableName nome da tabela no database
     * @param classe classe da entidade
     * @param searchInput filtro digitado pelo usuario em um campo de busca
     * @param columns colunas do database
     * @param concatDescCod true caso preciso concatenar codigo com descricao, caso contrario false
     * @return retorna uma lista de registros do VO especificado via parametro
     * @implNote
     * public List<SetoresVO> getAll(String filter) throws WebServiceException {
     *         return consultaController.getAll("setores", SetoresVO.class, filter, ERKMapUtils.toHashset("codigo", "descricao"), true);
     * }
     */
    public <T> List<T> getAll(String tableName, Class<T> classe, String searchInput, HashSet<String> columns, Boolean concatDescCod) throws WebServiceException {
        try {
            tableName = converterNomeDaClasseEmNomeTabela(tableName);
            return EJBInterfaces.get(Consultas.class).getAll(tableName, classe, searchInput, columns, concatDescCod);
        } catch (Exception e) {
            throw new WebServiceException("Erro ao consultar, tablea: " + tableName, e);
        }
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros por filtro com possibilidade de concatenação e escolha de colunas pelo hashset.
     * @param tableName nome da tabela no database
     * @param columns colunas do database
     * @return retorna uma lista de registros do VO especificado via parametro
     * @implNote
     * public List<Map<String, Object>> getAll(String filter) throws WebServiceException {
     *         return consultaController.getAll("setores", SetoresVO.class, filter, ERKMapUtils.toHashset("codigo", "descricao"), true);
     * }
     */
    public List<Map<String, Object>> getAll(String tableName, HashSet<String> columns)  throws WebServiceException {
        return getAll(tableName, null, columns, false);
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros e escolha de colunas pelo hashset.
     * @param tableName nome da tabela no database
     * @param columns colunas do database
     * @return retorna uma lista de registros
     * @implNote
     * public List<Map<String, Object> getAll(String filter) throws WebServiceException {
     *         return consultaController.getAll("telas", ERKMapUtils.toHashset("codigo", "descricao", "route"));
     * }
     */
    public List<Map<String, Object>> getAll(String tableName, String ...columns)  throws WebServiceException {
        return getAll(tableName, null, ERKMapUtils.toHashSet(columns), false);
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros por um filtro.
     * @param tableName nome da tabela no database
     * @param searchInput filtro digitado pelo usuario em um campo de busca
     * @return retorna uma lista de registros
     * @implNote
     * public List<Map<String, Object>> getAll(String filtro) throws WebServiceException {
     *         return getAll("telas", filtro);
     * }
     */
    public List<Map<String, Object>> getAll(String tableName, String searchInput) throws WebServiceException {
        return getAll(tableName, searchInput, new HashSet<>(), false);
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros por um filtro com posibilidade de concatenação de codigo e descricao.
     * @param tableName nome da tabela no database
     * @param searchInput filtro digitado pelo usuario em um campo de busca
     * @param concatDescCod true caso preciso concatenar codigo com descricao, caso contrario false
     * @return retorna uma lista de registros
     * @implNote
     * public List<Map<String, Object>> getAll(String filtro) throws WebServiceException {
     *         return getAll("telas", filtro, true);
     * }
     */
    public List<Map<String, Object>> getAll(String tableName, String searchInput, Boolean concatDescCod) throws WebServiceException {
        return getAll(tableName, searchInput, new HashSet<>(), concatDescCod);
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros por um filtro com escolha de colunas via hashset
     * @param tableName nome da tabela no database
     * @param searchInput filtro digitado pelo usuario em um campo de busca
     * @param columns colunas do database
     * @return retorna uma lista de registros
     * @implNote
     * public List<Map<String, Object>> getAll(String filtro) throws WebServiceException {
     *         return getAll("telas", filtro, ERKMapUtils.toHashset("codigo", "nome", "route"));
     * }
     */
    public List<Map<String, Object>> getAll(String tableName, String searchInput, HashSet<String> columns)  throws WebServiceException {
        return getAll(tableName, searchInput, columns, false);
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros por um filtro com posibilidade de concatenação e escolha de colunas via hashset
     * @param tableName nome da tabela no database
     * @param searchInput filtro digitado pelo usuario em um campo de busca
     * @param columns colunas do database
     * @return retorna uma lista de registros
     * @implNote
     * public List<Map<String, Object>> getAll(String filtro) throws WebServiceException {
     *         return getAll("telas", filtro, ERKMapUtils.toHashset("codigo", "nome", "route"));
     * }
     */
    public List<Map<String, Object>> getAll(String tableName, String searchInput, String... columns)  throws WebServiceException {
        return getAll(tableName, searchInput, ERKMapUtils.toHashSet(columns), false);
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros por um filtro com posibilidade de concatenação e escolha de colunas
     * @param tableName nome da tabela no database
     * @param columns colunas do database
     * @param concatDescCod true caso preciso concatenar codigo com descricao, caso contrario false
     * @return retorna uma lista de registros
     * @implNote
     * public List<Map<String, Object>> getAll(String filtro) throws WebServiceException {
     *         return getAll("telas", filtro, "codigo", "nome", "route");
     * }
     */
    public List<Map<String, Object>> getAll(String tableName, String searchInput, Boolean concatDescCod, String... columns) throws WebServiceException {
        return getAll(tableName, searchInput, ERKMapUtils.toHashSet(columns), concatDescCod);
    }

    /**
     * @apiNote Utilizado para consultar uma lista de registros por um filtro com posibilidade de concatenação e escolha de colunas
     * @param tableName nome da tabela no database
     * @param searchInput filtro digitado pelo usuario em um campo de busca
     * @param columns colunas do database
     * @param concatDescCod true caso preciso concatenar codigo com descricao, caso contrario false
     * @return retorna uma lista de registros
     * @implNote
     * public List<Map<String, Object>> getAll(String filtro) throws WebServiceException {
     *         return getAll("telas", filtro, ERKMapUtils.toHashset("codigo", "nome", "route"), true);
     * }
     */
    public List<Map<String, Object>> getAll(String tableName, String searchInput, HashSet<String> columns, Boolean concatDescCod)  throws WebServiceException {
        try {
            tableName = converterNomeDaClasseEmNomeTabela(tableName);
            return EJBInterfaces.getConsultas().getAll(tableName, searchInput, columns, concatDescCod);
        } catch (Exception e) {
            throw new WebServiceException("Erro ao consultar." + e.getMessage(), e);
        }
    }

    /**
     * @apiNote Método utilizado quando é necessário converter um nome de Classe Controller em um nome de tabela de banco de dados (K-ERP)
     */
    private String converterNomeDaClasseEmNomeTabela(String tabela) {
        return tabela != null ? tabela.toLowerCase().replace("controller", "") : null;
    }

}
