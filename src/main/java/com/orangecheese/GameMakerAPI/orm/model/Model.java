package com.orangecheese.GameMakerAPI.orm.model;

import com.orangecheese.GameMakerAPI.orm.Query;
import com.orangecheese.GameMakerAPI.orm.exceptions.ModelNotSyncedWithDatabaseException;
import com.orangecheese.GameMakerAPI.orm.exceptions.UndefinedModelException;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;
import com.orangecheese.helpers.Tuple;

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
        try {
            if(Arrays.asList(getPrimaryKeyColumns()).contains(propertyName) && !properties.containsKey(propertyName)) {
                throw new ModelNotSyncedWithDatabaseException(getClass());
            }
        } catch(ModelNotSyncedWithDatabaseException e) {
            e.printStackTrace();
        }

        return properties.get(propertyName);
    }

    public void createProperty(String propertyName, Object propertyValue) {
        ModelProperty property = new ModelProperty(propertyName, propertyValue);
        properties.put(propertyName, property);
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

                if (valueIterator.hasNext()) {
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
        Object[] generatedKeyValues = modelService.getConnection().executeUpdateQuery(query);

        for(int i = 0; i < generatedKeyValues.length; i++) {
            createProperty(getPrimaryKeyColumns()[i], generatedKeyValues[i]);
        }

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
            ModelProperty primaryKeyProperty = getProperty(primaryKeyColumn);
            String primaryKeyValue = primaryKeyProperty.get();
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

    public <T extends Model> T hasOne(Class<T> modelClass) {
        String standardForeignKeyColumn = getClass().getSimpleName().replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase() + "_id";
        return hasOne(modelClass, standardForeignKeyColumn);
    }

    public <T extends Model> T hasOne(Class<T> modelClass, String... foreignKeyColumns) {
        return hasMany(modelClass, foreignKeyColumns).first();
    }

    public <T extends Model> Query<T> hasMany(Class<T> modelClass, Class<? extends PivotModel<? extends Model, ? extends Model>> pivotClass) {
        List<T> list = new ArrayList<>();

        try {
            Query<? extends PivotModel<? extends Model, ? extends Model>> pivotModelQuery = modelService.get(pivotClass);
            for (PivotModel<? extends Model, ? extends Model> pivotModel : pivotModelQuery.toArray()) {
                list.add(pivotModel.get(modelClass));
            }

            if(list.isEmpty()) {
                return new Query<>(new ArrayList<>());
            }

            return filterCollection(new Query<>(list), list.get(0).getPrimaryKeyColumns(), getPrimaryKeyColumns());
        } catch (UndefinedModelException e) {
            e.printStackTrace();
        }

        return new Query<>(list);
    }

    public <T extends Model> Query<T> hasMany(Class<T> modelClass) {
        String standardForeignKeyColumn = getClass().getSimpleName().replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase() + "_id";
        return hasMany(modelClass, standardForeignKeyColumn);
    }

    public <T extends Model> Query<T> hasMany(Class<T> modelClass, String... foreignKeyColumns) {
        try {
            return filterCollection(modelService.get(modelClass), foreignKeyColumns, getPrimaryKeyColumns());
        } catch (UndefinedModelException e) {
            e.printStackTrace();
        }

        return new Query<>(new ArrayList<>());
    }

    private <T extends Model> Query<T> filterCollection(Query<T> query, String[] targetColumns, String[] originColumns) {
        List<Tuple<String, String>> columns = new ArrayList<>();

        for (int i = 0; i < originColumns.length; i++) {
            columns.add(new Tuple<>(targetColumns[i], originColumns[i]));
        }

        for (Tuple<String, String> column : columns) {
            query = query.where(m -> m.getProperty(column.getItem1()).get().equals(getProperty(column.getItem2()).get()));
        }

        return query;
    }

    public abstract String[] getPrimaryKeyColumns();
}