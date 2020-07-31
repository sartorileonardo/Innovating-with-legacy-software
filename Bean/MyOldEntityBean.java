package com.company.ejb.session.projeto;

/**
 * Imports
 */

public class MyOldEntityBean implements SessionBean {

    public SessionContext context;

	public Context jndiContext;

    private static final String MODULO = "MEU_MODULO";

    public Integer inserir(MyEntity myEntity, Integer usuario) throws Exception{

        Integer retorno;

        try{
            PermissoesUsuario permissoes = this.getPermissaoUsuario(usuario);
            if (permissoes.getInserir()){

                MyEntityLocalHome homeGerenciais = (MyEntityLocalHome) jndiContext.lookup("java:comp/env/MyentityBean");
                MyentityLocal myentityLocal;

                myentityLocal = homeGerenciais.create(myEntity.getDescricao(),
                                                        myEntity.getTitulo(),
                                                        myEntity.getTipo(),
                                                        myEntity.getTotaliza(),
                                                        myEntity.getMeta(),
                                                        myEntity.getOrdem(),
                                                        myEntity.getEstrela(),
                                                        myEntity.getMostra(),
                                                        myEntity.getMostravendedor(),
                                                        myEntity.getAcumulativo(),
                                                        myEntity.getDiario());

                retorno = myentityLocal.getCodigo();

            }else {
                throw new BeanException("Usuario "+ usuario + "sem permisao para incluir cadastro gerencial");
            }

            return retorno;

        }catch (Exception e){
            context.setRollbackOnly();
            throw new BeanException("Ocorreu um erro ao incluir um cadastro gerencial"+ e.getMessage(), e.getMessage(),getClass(),e);
        }
    }

    public Integer alterar(MyEntity myEntity, Integer usuario) throws Exception {

        Integer retorno;

        try{
            PermissoesUsuario permissoes = this.getPermissaoUsuario(usuario);
            if(permissoes.getAlterar()){

                MyEntityLocalHome homeGerenciais = (MyEntityLocalHome) jndiContext.lookup("java:comp/env/MyentityBean");
                MyentityLocal myentityLocal;

                try{
                    myentityLocal = homeGerenciais.findByPrimaryKey(myEntity.getCodigo());
                    myentityLocal.setDescricao(myEntity.getDescricao());
                    myentityLocal.setTitulo(myEntity.getTitulo());
                    myentityLocal.setTabela(getTabelasLocal(myEntity.getTabela()));
                    myentityLocal.setTipo(myEntity.getTipo());
                    myentityLocal.setMeta(myEntity.getMeta());
                    myentityLocal.setOrdem(myEntity.getOrdem());
                    myentityLocal.setTotaliza(myEntity.getTotaliza());
                    myentityLocal.setEstrela(myEntity.getEstrela());
                    myentityLocal.setMostra(myEntity.getMostra());
                    myentityLocal.setMostravendedor(myEntity.getMostravendedor());
                    myentityLocal.setAcumulativo(myEntity.getAcumulativo());
                    myentityLocal.setDiario(myEntity.getDiario());

                    retorno = myentityLocal.getCodigo();

                }catch (ObjectNotFoundException e) {
                    throw new BeanException("Cadastro "+myEntity.getCodigo()+ "nao encontrado");
                }
            } else {
                throw new BeanException("Usuario"+usuario+" sem permissão para alterar o cadastro");
            }

            return retorno;

        } catch (Exception e){
            context.setRollbackOnly();
            throw new BeanException("Ocorreu um erro ao alterar um cadastro" + e.getMessage(), e.getMessage(),getClass(),e);
        }
    }

    public Boolean excluir(Integer codigo, Integer usuario) throws Exception {

        Boolean retorno;

		try {
			PermissoesUsuario permissoes = this.getPermissaoUsuario(usuario);
			if (permissoes.getExcluir()) {

				MyEntityLocalHome myEntityLocalHome = (MyEntityLocalHome) jndiContext.lookup("java:comp/env/MyentityBean");
				MyentityLocal myentityLocal;

				try {
					myentityLocal = myEntityLocalHome.findByPrimaryKey(codigo);
					myentityLocal.remove();
					retorno = true;

				} catch (ObjectNotFoundException e) {
					throw new BeanException("Cadastro \"" + codigo + "\" nao encontrado");
				}

			} else {
				throw new BeanException("Usuario \"" + usuario + "\" sem permissao para excluir");
			}

			return retorno;

		} catch (Exception e) {
			context.setRollbackOnly();
			throw new BeanException("Ocorreu um erro ao remover:" + e.getMessage(), e.getMessage(), getClass(), e);
		}
    }

        public List<MyTntity> getAll(Integer codigo, String descricao, Integer usuario) throws Exception {

        SqlHome home;
		Sql sql = null;
		List<MyTntity> retorno;

        try{
            home = (SqlHome) jndiContext.lookup("java:comp/env/SqlBean");
            sql = home.create();
            retorno = new ArrayList<MyTntity>();

            ResultSet rs = sql.consultar(SQLMyEntity.getAll(codigo, descricao));
            if (rs.first()){

                do{
                    MyTntity myEntity = new MyTntity();
                    myEntity.setCodigo(rs.getInt("codigo"));
                    myEntity.setDescricao(rs.getString("descricao"));
                    myEntity.setTitulo(rs.getString("titulo"));
                    myEntity.setTabela((Integer)rs.getObject("tabela"));
                    myEntity.setMeta(rs.getBigDecimal("meta"));
                    myEntity.setOrdem((Integer) rs.getObject("ordem"));
                    myEntity.setTipo((Integer) rs.getObject("tipo"));
                    myEntity.setTotaliza(rs.getBoolean("totaliza"));
                    myEntity.setEstrela(rs.getBoolean("estrela"));
                    myEntity.setMostra(rs.getBoolean("mostra"));
                    myEntity.setMostravendedor(rs.getBoolean("mostravendedor"));
                    myEntity.setAcumulativo(rs.getBoolean("acumulativo"));
                    myEntity.setDiario(rs.getBoolean("diario"));

                    retorno.add(myEntity);

                } while (rs.next());
            }

            return retorno;

        } catch (Exception e){
            throw new BeanException("Ocorreu um erro ao retornar a lista de myEntity" +e.getMessage(), e.getMessage(), e.getClass(), e );
        }finally {
            if(sql!=null){
                sql.remove();
            }
        }

    }

    public MyTntity get(Integer codigo) throws Exception {

        SqlHome home;
        Sql sql = null;
        MyTntity retorno = new MyTntity();

        try{
            home = (SqlHome) jndiContext.lookup("java:comp/env/SqlBean");
            sql = home.create();

            ResultSet rs = sql.consultar(SQLMyTntity.get(codigo, null));
            if (rs.first()){

                do{
                    MyTntity myEntity = new MyTntity();
                    myEntity.setCodigo(rs.getInt("codigo"));
                    myEntity.setDescricao(rs.getString("descricao"));
                    myEntity.setTitulo(rs.getString("titulo"));
                    myEntity.setTabela((Integer)rs.getObject("tabela"));
                    myEntity.setMeta(rs.getBigDecimal("meta"));
                    myEntity.setOrdem((Integer) rs.getObject("ordem"));
                    myEntity.setTipo((Integer) rs.getObject("tipo"));
                    myEntity.setTotaliza(rs.getBoolean("totaliza"));
                    myEntity.setEstrela(rs.getBoolean("estrela"));
                    myEntity.setMostra(rs.getBoolean("mostra"));
                    myEntity.setMostravendedor(rs.getBoolean("mostravendedor"));
                    myEntity.setAcumulativo(rs.getBoolean("acumulativo"));
                    myEntity.setDiario(rs.getBoolean("diario"));

                    retorno = myEntity;

                } while (rs.next());
            }

            return retorno;

        } catch (Exception e){
            throw new BeanException("Ocorreu um erro ao consultar myEntity" +e.getMessage(), e.getMessage(), e.getClass(), e );
        }finally {
            if(sql!=null){
                sql.remove();
            }
        }

    }

    private PermissoesUsuario getPermissaoUsuario(Integer usuario) throws Exception {
		Object ref = jndiContext.lookup("Usuarios");
		UsuariosHome homeUsuarios = (UsuariosHome) PortableRemoteObject.narrow(ref, UsuariosHome.class);
		Usuarios usuarios = homeUsuarios.create();
		return usuarios.permissaoModulo(usuario, MODULO);
    }

    public void ejbActivate() throws EJBException, RemoteException {}

	public void ejbPassivate() throws EJBException, RemoteException {}

	public void ejbRemove() throws EJBException, RemoteException {}

	public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
		context = arg0;
	}
	
	public void ejbCreate() throws CreateException, NamingException {
		jndiContext = new InitialContext();
	}

}

