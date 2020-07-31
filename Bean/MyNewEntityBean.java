package com.company.ejb.session.projetok;

/**
 * Imports
 */

public class MyNewEntityBean extends CadastroBean {

    private static final String MODULO = "MY_MODULO";

    public Integer inserir (MyEntity myEntity, Integer codUsuario, Integer codFilial) throws Exception {
        return (Integer) inserir(this.getClass().getSimpleName(), myEntity, codUsuario, codFilial, MODULO, "codigo");
    }

    public Integer alterar (MyEntity myEntity, Integer codUsuario, Integer codFilial) throws Exception {
        return (Integer) alterar(this.getClass().getSimpleName(), myEntity, codUsuario, codFilial, MODULO, "codigo");
    }

    public Boolean excluir (Integer codigo, Integer codUsuario, Integer codFilial) throws Exception {
        return excluir(this.getClass().getSimpleName(), codUsuario, codFilial, MODULO, codigo);
    }

}

