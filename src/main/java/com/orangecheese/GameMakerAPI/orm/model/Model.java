package com.orangecheese.GameMakerAPI.orm.model;

import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnection;
import com.orangecheese.GameMakerAPI.orm.exceptions.UndefinedModelException;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public abstract class Model {
    protected final ModelService modelService;
    private boolean isNew;

    protected Map<String, ModelProperty> properties;

    public Model(ModelService modelService, ResultSet resultSet) throws SQLException {
        this.modelService = modelService;
        isNew = false;

        properties = new HashMap<>();

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            String columnName = resultSetMetaData.getColumnName(i);
            properties.put(columnName, new ModelProperty(columnName, resultSet.getObject(i)));
        }
    }

    public Model(ModelService modelService) {
        this.modelService = modelService;
        isNew = true;

        properties = new HashMap<>();
    }

    public ModelProperty getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    public ModelProperty createProperty(String propertyName, Object propertyValue) {
        ModelProperty property = new ModelProperty(propertyName, propertyValue);
        properties.put(propertyName, property);
        return property;
    }

    public void save() throws UndefinedModelException {
        StringBuilder queryBuilder = new StringBuilder();

        String tableName = modelService.getRegistration(getClass()).getTableName();

        if (isNew) {
            List<String> columns = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            for (ModelProperty property : properties.values()) {
                columns.add(property.getPropertyName());
                values.add(property.get());
            }

            queryBuilder.append("INSERT INTO ").append(tableName).append(" ");
            queryBuilder.append("(").append(String.join(", ", columns)).append(" ").append(")");
            queryBuilder.append("VALUES ");
            queryBuilder.append("(");

            Iterator<Object> valueIterator = values.iterator();
            while (valueIterator.hasNext()) {
                Object value = valueIterator.next();
                queryBuilder.append("'").append(value).append("'");

                if(valueIterator.hasNext()) {
                    queryBuilder.append(", ");
                }
            }

            queryBuilder.append(")");
        } else {
            queryBuilder.append("UPDATE ").append(tableName).append(" SET ");

            Iterator<ModelProperty> modelPropertyIterator = properties.values().iterator();
            while (modelPropertyIterator.hasNext()) {
                ModelProperty property = modelPropertyIterator.next();
                String propertyName = property.getPropertyName();
                Object propertyValue = property.get();

                queryBuilder.append(propertyName).append(" = '").append(propertyValue).append("'");

                if (modelPropertyIterator.hasNext()) {
                    queryBuilder.append(", ");
                } else {
                    queryBuilder.append(" ");
                }
            }

            queryBuilder.append(getModelSpecificWhereClause());
        }

        String query = queryBuilder.toString();
        modelService.getConnection().executeUpdateQuery(query);

        isNew = false;
    }

    public void delete() throws UndefinedModelException {
        String tableName = modelService.getRegistration(getClass()).getTableName();

        String query = "DELETE FROM " + tableName + " " +
                getModelSpecificWhereClause();
        modelService.getConnection().executeUpdateQuery(query);
    }

    private Map<String, String> getPrimaryKeyValuePairs() {
        Map<String, String> primaryKeyValues = new HashMap<>();
        for (String primaryKeyColumn : getPrimaryKeyColumns()) {
            String primaryKeyValue = null;
            ModelProperty primaryKeyProperty = getProperty(primaryKeyColumn);
            primaryKeyValue = primaryKeyProperty.get();

            primaryKeyValues.put(primaryKeyColumn, primaryKeyValue);
        }

        return primaryKeyValues;
    }

    private String getModelSpecificWhereClause() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("WHERE ");

        Map<String, String> primaryKeyValuePairs = getPrimaryKeyValuePairs();

        Iterator<String> primaryKeyIterator = primaryKeyValuePairs.keySet().iterator();
        while (primaryKeyIterator.hasNext()) {
            String primaryKey = primaryKeyIterator.next();
            String primaryKeyValue = primaryKeyValuePairs.get(primaryKey);

            stringBuilder.append(primaryKey).append(" = '").append(primaryKeyValue).append("' ");
            if (primaryKeyIterator.hasNext()) {
                stringBuilder.append("AND ");
            }
        }

        return stringBuilder.toString();
    }

    public abstract String[] getPrimaryKeyColumns();
}