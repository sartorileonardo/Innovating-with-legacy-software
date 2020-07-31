package com.company.ejb.session.consultas;

/**
 * Imports
 */

/**
 * @author Lucas Fernando Frighetto
 */
public class ConsultasBean implements SessionBean {

    private static final long serialVersionUID = 1L;
    public Context jndiContext;
    public SessionContext context;

    public <T> T get(String tableName, Object uid, String columnName) throws Exception {
        HashSet<String> columns = new HashSet<>();
        columns.add(columnName);
        return (T) get(tableName, uid, false, columns).get(columnName);
    }

    public <T> T get(String tableName, Class<T> classe, Object uid) throws Exception {
        return ERKMapUtils.mapToVO(get(tableName, uid, false, null), classe);
    }

    public <T> T get(String tableName, Class<T> classe, Object uid, Boolean isCrudERP) throws Exception {
        return ERKMapUtils.mapToVO(get(tableName, uid, isCrudERP, null), classe);
    }

    public <T> T get(String tableName, Class<T> classe, Object uid, HashSet<String> columns) throws Exception {
        return ERKMapUtils.mapToVO(get(tableName, uid, false, columns), classe);
    }

    public <T> T get(String tableName, Class<T> classe, Object uid, Boolean isCrudERP, HashSet<String> columns) throws Exception {
        return ERKMapUtils.mapToVO(get(tableName, uid, isCrudERP, columns), classe);
    }

    public Map<String, Object> get(String tableName, Object uid) throws Exception {
        return  get(tableName, uid, false, null);
    }

    public Map<String, Object> get(String tableName, Object uid, Boolean isCrudERP) throws Exception {
        return  get(tableName, uid, isCrudERP, null);
    }

    public Map<String, Object> get(String tableName, Object uid, HashSet<String> columns) throws Exception {
        return get(tableName, uid, false, columns);
    }

    public Map<String, Object> get(String tableName, Object uid, Boolean isCrudERP, HashSet<String> columns) throws Exception {
        Sql sql = this.getSQL();

        try {
            ResultSet resultSet = sql.consultar(SQLUtils.getKeysSchema(tableName, isCrudERP));
            List<SchemaChavesTabelasVO> chaves = ERKMapUtils.getListFromSQL(resultSet, SchemaChavesTabelasVO.class);

            resultSet = sql.consultar(SQLUtils.get(tableName, uid, columns, chaves));
            if(!resultSet.first()){
                throw new ERKNotFoundException("Não foi encontrado registro com este identificador!");
            }
            Map<String, Object> values = ERKMapUtils.getMapFromSQL(resultSet);

            if(isCrudERP) {
                for (SchemaChavesTabelasVO chave : chaves) {
                    if (chave.getTipoChave().equalsIgnoreCase("FOREIGN KEY")) {

                        if(values.get(chave.getNome()) != null) {
                            Object refValue = values.get(chave.getReferenciarestricao());
                            Map<String, Object> refValues = refValue instanceof Map ? (Map<String, Object>) refValue : new HashMap<>();
                            refValues.put(chave.getNome(), values.get(chave.getNome()));
                            String label = (String) values.remove(chave.getTabelaReferenciada() + chave.getNome());
                            if (label != null) {
                                refValues.put(chave.getNomeReferenciada(), label);
                            }
                            values.put(chave.getReferenciarestricao(), refValues);
                        }
                    }
                }
            }

            return ERKMapUtils.getMapToKERP(values);

        } catch (Exception e) {
            if(e instanceof ERKNotFoundException){
                throw new ERKNotFoundException(e.getMessage());
            }
            throw new BeanException("Ocorreu um erro ao consultar: \"" + e.getMessage() + "\"", getClass(), e);
        } finally {
            if (sql != null) {
                sql.remove();
            }
        }
    }


    public <T> List<T> getAll(String tableName, Class<T> classe, HashSet<String> columns) throws Exception{
        return ERKMapUtils.getListVOFromListMap(getAll(tableName, null, columns, false), classe);
    }

    public <T> List<T> getAll(String tableName, Class<T> classe, String searchInput)  throws Exception{
        return ERKMapUtils.getListVOFromListMap(getAll(tableName, searchInput, false), classe);
    }

    public <T> List<T> getAll(String tableName, Class<T> classe, String searchInput, Boolean concatDescCod)  throws Exception{
        return ERKMapUtils.getListVOFromListMap(getAll(tableName, searchInput, concatDescCod), classe);
    }

    public  <T> List<T> getAll(String tableName, Class<T> classe, String searchInput, HashSet<String> columns)  throws Exception{
        return ERKMapUtils.getListVOFromListMap(getAll(tableName, searchInput, columns, false), classe);
    }

    public <T> List<T> getAll(String tableName, Class<T> classe, String searchInput, HashSet<String> columns, Boolean concatDescCod) throws Exception{
        return ERKMapUtils.getListVOFromListMap(getAll(tableName, searchInput, columns, concatDescCod), classe);
    }

    public List<Map<String, Object>> getAll(String tableName, HashSet<String> columns)  throws Exception{
        return getAll(tableName, null, columns, false);
    }

    public List<Map<String, Object>> getAll(String tableName, String searchInput) throws Exception{
        return getAll(tableName, searchInput, null, false);
    }

    public List<Map<String, Object>> getAll(String tableName, String searchInput, Boolean concatDescCod)  throws Exception{
        return getAll(tableName, searchInput, null, concatDescCod);
    }

    public List<Map<String, Object>> getAll(String tableName, String searchInput, HashSet<String> columns) throws Exception{
        return getAll(tableName, searchInput, columns, false);
    }

    public List<Map<String, Object>> getAll(String tableName, String searchInput, HashSet<String> columns, Boolean concatDescCod)  throws Exception{
        Sql sql = this.getSQL();
        try {
            ResultSet resultSet = sql.consultar(SQLUtils.getKeysSchema(tableName));
            SchemaChavesTabelasVO chave = ERKMapUtils.mapToVO(ERKMapUtils.getMapFromSQL(resultSet), SchemaChavesTabelasVO.class);
            Boolean numericUid = chave.getTipoChave().matches("numeric|integer|smallint|bigint");
            if(concatDescCod) {
                String label = columns != null && columns.size() == 1 ? (String) columns.toArray()[0] : chave.getRotulo();
                if (chave.getNome() != null && label != null) {
                    resultSet = sql.consultar(SQLUtils.getMany(tableName, null, chave.getNome(), label, searchInput, numericUid));
                    return ERKMapUtils.getListMapFromSQL(chave.getNome(), resultSet, concatDescCod);
                } else {
                    throw new BeanException("Não foi possível executar a consulta default, deve-se especificar o nome do campo da busca.");
                }
            }
            resultSet =  sql.consultar(SQLUtils.getMany(tableName, columns, chave.getNome(), chave.getRotulo(), searchInput, numericUid));
            return ERKMapUtils.getListMapFromSQL(resultSet);
        } catch (Exception e) {
            throw new BeanException("Ocorreu um erro ao consultar: \"" + e.getMessage() + "\"", getClass(), e);
        } finally {
            if (sql != null) {
                sql.remove();
            }
        }
    }

    private Sql getSQL() throws Exception {
        Sql sql;
        SqlHome home = (SqlHome) jndiContext.lookup("java:comp/env/SqlBean");
        sql = home.create();
        return sql;
    }


    @Override
    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {
        context = sessionContext;
    }

    public void ejbCreate() throws CreateException, NamingException {
        jndiContext = new InitialContext();
    }

    @Override
    public void ejbRemove() throws EJBException, RemoteException {

    }

    @Override
    public void ejbActivate() throws EJBException, RemoteException {

    }

    @Override
    public void ejbPassivate() throws EJBException, RemoteException {

    }
}
