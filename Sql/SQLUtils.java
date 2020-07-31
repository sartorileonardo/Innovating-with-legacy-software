package com.company.api.biblio;

/**
 * Imports
 */

/**
 * @author Lucas Fernando Frighetto on 08/08/2019
 */
public class SQLUtils {

    public static String getKeysSchema(String table_name){
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT     colunas.column_name AS nome,\n");
        sb.append("           coluna_rotulo.column_name AS rotulo,\n");
        sb.append("           chaves_primarias.constraint_type AS tipochave,\n");
        sb.append("           colunas.data_type AS tipodado\n");
        sb.append("FROM       information_schema.columns colunas,\n");
        sb.append("           information_schema.key_column_usage chaves_colunas\n");
        sb.append("LEFT JOIN  information_schema.columns coluna_rotulo\n");
        sb.append("           ON (coluna_rotulo.table_name = chaves_colunas.table_name\n");
        sb.append("              AND coluna_rotulo.column_name IN ('nome', 'descricao'))\n");
        sb.append("LEFT JOIN  information_schema.table_constraints chaves_primarias\n");
        sb.append("           ON chaves_colunas.constraint_name = chaves_primarias.constraint_name\n");
        sb.append("WHERE      chaves_primarias.constraint_type = 'PRIMARY KEY'\n");
        sb.append("           AND colunas.table_name = chaves_colunas.table_name\n");
        sb.append("           AND colunas.column_name = chaves_colunas.column_name\n");
        sb.append("           AND chaves_colunas.table_name = '" + table_name + "'\n");

        return sb.toString();
    }

    public static String getKeysSchema(String table_name, Boolean includeForeign){
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT     colunas.column_name AS nome,\n");
        sb.append("           chaves_primarias.constraint_type AS tipochave,\n");
        if(includeForeign) {
            sb.append("           REPLACE(chaves_estrangeiras.constraint_name, '" + table_name + "_', '') AS referenciarestricao,\n");
            sb.append("           REPLACE(chaves_estrangeiras.unique_constraint_name, '_pkey', '') AS tabelareferenciada,\n");
            sb.append("           nomeschavesreferenciadas.column_name AS chavereferenciada,\n");
            sb.append("           colunas_estrangeiras.column_name AS nomereferenciada,\n");
        }
        sb.append("           colunas.data_type AS tipodado\n");
        sb.append("FROM       information_schema.columns colunas,\n");
        sb.append("           information_schema.key_column_usage chaves_colunas\n");
        sb.append("LEFT JOIN  information_schema.table_constraints chaves_primarias\n");
        sb.append("           ON chaves_colunas.constraint_name = chaves_primarias.constraint_name\n");
        if(includeForeign) {
            sb.append("LEFT JOIN  information_schema.referential_constraints chaves_estrangeiras\n");
            sb.append("           ON chaves_estrangeiras.constraint_name = chaves_primarias.constraint_name\n");
            sb.append("LEFT JOIN  information_schema.columns colunas_estrangeiras\n");
            sb.append("           ON (colunas_estrangeiras.table_name = REPLACE(chaves_estrangeiras.unique_constraint_name, '_pkey', '')\n");
            sb.append("              AND colunas_estrangeiras.column_name IN ('nome', 'descricao'))\n");
            sb.append("LEFT JOIN  information_schema.key_column_usage nomeschavesreferenciadas\n");
            sb.append("           ON (nomeschavesreferenciadas.table_name = REPLACE(chaves_estrangeiras.unique_constraint_name, '_pkey', '')\n");
            sb.append("               AND chaves_colunas.ordinal_position = nomeschavesreferenciadas.ordinal_position\n");
            sb.append("               AND nomeschavesreferenciadas.constraint_name = chaves_estrangeiras.unique_constraint_name)\n");
        }
        sb.append("WHERE      chaves_primarias.constraint_type IN (" + (includeForeign ? "'FOREIGN KEY', " : "" ) + "'PRIMARY KEY')\n");
        sb.append("           AND colunas.table_name = chaves_colunas.table_name\n");
        sb.append("           AND colunas.column_name = chaves_colunas.column_name\n");
        sb.append("           AND chaves_colunas.table_name = '" + table_name + "'\n");
        if(includeForeign) {
            sb.append("ORDER BY   tipochave DESC,\n");
            sb.append("           referenciarestricao,\n");
            sb.append("           CASE WHEN colunas.column_name = REPLACE(chaves_estrangeiras.constraint_name, '" + table_name + "_', '') THEN 0 ELSE 1 END;\n");
        }

        return sb.toString();
    }

    public static String get(String nomeTabela, Object uid, HashSet<String> columns, List<SchemaChavesTabelasVO> chaves) {
        StringBuilder select = new StringBuilder();

        if(columns == null || columns.isEmpty()){
            select.append("SELECT " + nomeTabela + ".*");
        } else {
            boolean first = true;
            for(String column : columns){
                if (first) {
                    select.append("SELECT " + nomeTabela + "." + column);
                    first = false;
                } else {
                    select.append(",\n" + nomeTabela + "." + column);
                }
            }
        }

        StringBuilder from = new StringBuilder();
        from.append("\nFROM " + nomeTabela + " ");

        StringBuilder where = new StringBuilder();

        StringBuilder onJoins = new StringBuilder();
        String restricao = "";
        for(SchemaChavesTabelasVO chave : chaves){
            if(chave.getTipoChave().equalsIgnoreCase("PRIMARY KEY")){
                uid = chave.getTipoDado().matches("numeric|integer|smallint|bigint") ? uid : "'" + uid + "'";
                where.append("\n WHERE " + nomeTabela + "." + chave.getNome() + " = " + uid);
            }

            if(chave.getNomeReferenciada() != null && chave.getTipoChave().equalsIgnoreCase("FOREIGN KEY") && (columns == null || columns.isEmpty() || columns.contains(chave.getNome()))){
                String tabelaJoin = chave.getTabelaReferenciada() + chave.getReferenciarestricao();
                String column = tabelaJoin + "." + chave.getNomeReferenciada();
                if(!select.toString().contains(column)) {
                    select.append(",\n ");
                    select.append(column);
                    select.append(" AS " + chave.getTabelaReferenciada() + chave.getNome());
                }
                String onJoin = nomeTabela + "." + chave.getNome() + " = " + tabelaJoin + "." + chave.getChaveReferenciada();
                if (restricao.equalsIgnoreCase(chave.getReferenciarestricao())) {
                    onJoins.append("\n AND " + onJoin);
                } else{
                    if(!onJoins.toString().isEmpty()){
                        from.append("\n ON (" + onJoins + ")");
                        onJoins = new StringBuilder();
                    }

                    from.append("\n LEFT JOIN " + chave.getTabelaReferenciada() + " " + tabelaJoin);
                    onJoins.append(onJoin);
                }
                restricao = chave.getReferenciarestricao();

            }
        }
        if(!onJoins.toString().isEmpty()){
            from.append("\n ON (" + onJoins + ")");
        }

        StringBuilder sb = new StringBuilder(select.toString() + from.toString() + where.toString());
        return sb.toString();
    }

    public static String getSchema(String table_name) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select constraints.ctype as key, \n");
        sb.append(" cols.column_name as nome,\n");
        sb.append(" cols.is_nullable as null,\n");
        sb.append(" cols.data_type as tipo, \n");
        sb.append(" cols.character_maximum_length\n");
        sb.append(" from  INFORMATION_SCHEMA.COLUMNS AS cols\n");
        sb.append(" left join (SELECT column_name, TC.CONSTRAINT_TYPE as ctype\n");
        sb.append("         FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS TC\n");
        sb.append("         INNER JOIN\n");
        sb.append("         INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KU\n");
        sb.append("         ON TC.CONSTRAINT_NAME = KU.CONSTRAINT_NAME AND\n");
        sb.append("         KU.table_name='" + table_name + "'\n");
        sb.append("         ORDER BY KU.TABLE_NAME, KU.ORDINAL_POSITION) as constraints\n");
        sb.append(" on cols.column_name = constraints.column_name\n");
        sb.append(" where cols.table_name = '" + table_name + "'\n");
        sb.append(" order by nome;\n");

        return sb.toString();
    }

    public static String getPrimaryKey(String table_name) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT column_name");
        sb.append(" FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS TC");
        sb.append(" INNER JOIN");
        sb.append(" INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KU");
        sb.append(" ON TC.CONSTRAINT_TYPE = 'PRIMARY KEY' AND");
        sb.append(" TC.CONSTRAINT_NAME = KU.CONSTRAINT_NAME AND");
        sb.append(" KU.table_name='" + table_name + "'");
        sb.append(" ORDER BY KU.TABLE_NAME, KU.ORDINAL_POSITION;");
        return sb.toString();
    }

    public static String getColumns(String table_name) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT column_name");
        sb.append(" FROM INFORMATION_SCHEMA.columns");
        sb.append(" WHERE table_name = '" + table_name + "'");
        return sb.toString();
    }

    public static String getForeignKeys(String table) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT");
        sb.append(" k1.column_name,");
        sb.append(" k2.table_name AS referenced_table_name,");
        sb.append(" k2.column_name AS referenced_column_name");
        sb.append(" FROM information_schema.key_column_usage k1");
        sb.append(" JOIN information_schema.referential_constraints fk USING (constraint_schema, constraint_name)");
        sb.append(" JOIN information_schema.key_column_usage k2");
        sb.append(" ON k2.constraint_schema = fk.unique_constraint_schema");
        sb.append(" AND k2.constraint_name = fk.unique_constraint_name");
        sb.append(" AND k2.ordinal_position = k1.position_in_unique_constraint");
        sb.append(" WHERE k1.table_name = '" + table + "';");

        return sb.toString();
    }

    public static String getTableOfForeignKey(String table, String fkey) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT k2.table_name AS table");
        sb.append(" FROM information_schema.key_column_usage k1");
        sb.append(" JOIN information_schema.referential_constraints fk USING (constraint_schema, constraint_name)");
        sb.append(" JOIN information_schema.key_column_usage k2");
        sb.append(" ON k2.constraint_schema = fk.unique_constraint_schema");
        sb.append(" AND k2.constraint_name = fk.unique_constraint_name");
        sb.append(" AND k2.ordinal_position = k1.position_in_unique_constraint");
        sb.append(" WHERE k1.table_name = '" + table + "' AND k1.column_name = '" + fkey + "';");
        return sb.toString();
    }

    public static String getMany(String table_name, String uid, String label) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " + uid + ", " + label);
        sb.append(" FROM " + table_name);
        sb.append(" ORDER BY " + label + ", " + uid);
        return sb.toString();
    }


    public static String get(String table_name, String column_name, String uid, Boolean numericUid) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT *");
        sb.append(" FROM " + table_name);
        if (numericUid) {
            sb.append(" WHERE " + column_name + " = " + uid + "");
        } else {
            sb.append(" WHERE TO_ASCII(UPPER(CAST(" + column_name + " AS VARCHAR))) = TO_ASCII(UPPER(CAST('" + uid + "' AS VARCHAR)))");
        }
        return sb.toString();
    }

    public static String get(String table_name, String column_name, String uid, HashSet<String> columns, Boolean numericUid) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        int count = 1;
        for (String column : columns) {
            sb.append(column);
            if (count != columns.size()) {
                sb.append(", ");
            }
            count++;
        }
        sb.append(" FROM " + table_name);
        if (numericUid) {
            sb.append(" WHERE " + column_name + " = " + uid + "");
        } else {
            sb.append(" WHERE TO_ASCII(UPPER(CAST(" + column_name + " AS VARCHAR))) = TO_ASCII(UPPER(CAST('" + uid + "' AS VARCHAR)))");
        }

        return sb.toString();
    }

    public static String getAll(String table_name, HashSet<String> columns) {
        if (columns == null || columns.isEmpty()) {
            return getAll(table_name);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        int count = 1;
        for (String column : columns) {
            sb.append(column);
            if (count != columns.size()) {
                sb.append(", ");
            }
            count++;
        }
        sb.append(" FROM " + table_name);
        return sb.toString();
    }

    public static String getAll(String table_name, HashSet<String> columns, String uid, String label, String search_value) {
        if (columns == null || columns.isEmpty()) {
            return getAll(table_name);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        int count = 1;
        for (String column : columns) {
            sb.append(column);
            if (count != columns.size()) {
                sb.append(", ");
            }
            count++;
        }
        sb.append(" FROM " + table_name);
        if(uid != null && label != null && search_value != null){
            sb.append(" WHERE TO_ASCII(UPPER(CAST(" + uid + " AS VARCHAR))) = TO_ASCII(UPPER('" + search_value + "'))\n");
            sb.append(" OR TO_ASCII(" + label + ") ILIKE TO_ASCII('%" + search_value + "%')\n");
            sb.append(" ORDER BY " + "\n");
            sb.append(" CASE WHEN UPPER(CAST(" + uid + " AS VARCHAR)) = UPPER('" + search_value + "') THEN 0 ELSE 1 END,\n");
            sb.append(" CASE WHEN TO_ASCII(" + label + ") ILIKE TO_ASCII('" + search_value + "%') THEN 0 ELSE 1 END,\n");
            sb.append(label + ",\n");
            sb.append(uid);
        }
        return sb.toString();
    }

    public static String getColumnTypes(String table_name) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT column_name, data_type");
        sb.append(" FROM INFORMATION_SCHEMA.COLUMNS");
        sb.append(" WHERE");
        sb.append(" TABLE_NAME = '");
        sb.append(table_name);
        sb.append("'");
        return sb.toString();
    }

    public static String getColumnTypes(String table_name, String column_name) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT column_name, data_type");
        sb.append(" FROM INFORMATION_SCHEMA.COLUMNS");
        sb.append(" WHERE");
        sb.append(" TABLE_NAME = '" + table_name + "'");
        sb.append(" AND COLUMN_NAME = '" + column_name + "'");
        return sb.toString();
    }

    public static String getMany(String table_name, String uid, String label, String search_value) {
        StringBuilder sb = new StringBuilder();
        if (search_value == null || search_value.matches("\\s*")) {
            sb.append("SELECT " + uid + ",\n");
            sb.append(label + "\n");
            sb.append("FROM " + table_name + "\n");
            sb.append("ORDER BY " + label + ",\n");
            sb.append(uid);
            return sb.toString();
        }

        sb.append("SELECT " + uid + ", " + "\n");
        sb.append(label + "\n");
        sb.append(" FROM " + table_name + "\n");
        sb.append(" WHERE TO_ASCII(UPPER(CAST(" + uid + " AS VARCHAR))) = TO_ASCII(UPPER('" + search_value + "'))\n");
        sb.append(" OR TO_ASCII(" + label + ") ILIKE TO_ASCII('%" + search_value + "%')\n");
        sb.append(" ORDER BY " + "\n");
        sb.append(" CASE WHEN UPPER(CAST(" + uid + " AS VARCHAR)) = UPPER('" + search_value + "') THEN 0 ELSE 1 END,\n");
        sb.append(" CASE WHEN TO_ASCII(" + label + ") ILIKE TO_ASCII('" + search_value + "%') THEN 0 ELSE 1 END,\n");
        sb.append(label + ",\n");
        sb.append(uid);
        return sb.toString();
    }

    public static String getMany(String table_name, HashSet<String> columns, String uid, String label, String search_value, Boolean numericUid) {
        StringBuilder sb = new StringBuilder();
        if (search_value == null || search_value.matches("\\s*")) {
            sb.append("SELECT " + uid + ",\n");
            sb.append(label + "\n");
            sb.append("FROM " + table_name + "\n");
            sb.append("ORDER BY " + label + ",\n");
            sb.append(uid);
            return sb.toString();
        }

        if(columns != null && !columns.isEmpty()){
            sb.append("SELECT ");
            int count = 1;
            for (String column : columns) {
                sb.append(column);
                if (count != columns.size()) {
                    sb.append(", ");
                }
                count++;
            }
        }else {
            sb.append("SELECT " + uid + ", " + "\n");
            sb.append(label + "\n");
        }
        sb.append(" FROM " + table_name + "\n");

        if (numericUid) {
            if (search_value.matches("\\d+")) {
                sb.append(" WHERE " + uid + " = " + search_value + "\n");
                sb.append(" OR TO_ASCII(" + label + ") ILIKE TO_ASCII('%" + search_value + "%')\n");
            } else {
                sb.append(" WHERE TO_ASCII(" + label + ") ILIKE TO_ASCII('%" + search_value + "%')\n");
            }
        } else {
            sb.append(" WHERE TO_ASCII(UPPER(CAST(" + uid + " AS VARCHAR))) = TO_ASCII(UPPER('" + search_value + "'))\n");
            sb.append(" OR TO_ASCII(" + label + ") ILIKE TO_ASCII('%" + search_value + "%')\n");
        }


        sb.append(" ORDER BY " + "\n");
        sb.append(" CASE WHEN UPPER(CAST(" + uid + " AS VARCHAR)) = UPPER('" + search_value + "') THEN 0 ELSE 1 END,\n");
        sb.append(" CASE WHEN TO_ASCII(" + label + ") ILIKE TO_ASCII('" + search_value + "%') THEN 0 ELSE 1 END,\n");
        sb.append(label + ",\n");
        sb.append(uid);
        return sb.toString();
    }

    public static String getAll(String table_name) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * \n");
        sb.append("FROM " + table_name);

        return sb.toString();
    }


}
