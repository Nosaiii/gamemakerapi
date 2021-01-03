package com.orangecheese.GameMakerAPI.orm.modelfacade;

import com.orangecheese.GameMakerAPI.orm.Query;
import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnection;
import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnectionProperties;
import com.orangecheese.GameMakerAPI.orm.exceptions.EmptyQueryResultException;
import com.orangecheese.GameMakerAPI.orm.exceptions.UndefinedModelException;
import com.orangecheese.GameMakerAPI.orm.model.IModelMapper;
import com.orangecheese.GameMakerAPI.orm.model.Model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModelService {
    private final DatabaseConnection connection;

    private final Map<Class<? extends Model>, ModelRegistration> registrations;

    public ModelService(DatabaseConnectionProperties properties) {
        connection = new DatabaseConnection(properties);
        registrations = new HashMap<>();
    }

    public void register(ModelRegistrationProperties properties) {
        if(registrations.containsKey(properties.getModelClass())) {
            return;
        }

        IModelMapper mapper = properties.getModelMapper();
        IModelFactory<? extends Model> factory = registerFacade(properties);
        String tableName = properties.getTableName();
        ModelRegistration registration = new ModelRegistration(mapper, factory, tableName);

        registrations.put(properties.getModelClass(), registration);
    }

    private <T extends Model> IModelFactory<T> registerFacade(ModelRegistrationProperties properties) {
        final ModelService modelService = this;

        return new IModelFactory<T>() {
            @Override
            public String buildQuery() {
                return "SELECT * " +
                        "FROM " + properties.getTableName();
            }

            @Override
            public Query<T> all() {
                ResultSet result = null;
                try {
                    result = connection.executeQuery(buildQuery());
                } catch (EmptyQueryResultException e) {
                    e.printStackTrace();
                }
                Objects.requireNonNull(result);

                IModelMapper mapper = registrations.get(properties.getModelClass()).getMapper();
                return new Query<T>(mapper, modelService, result);
            }
        };
    }

    public DatabaseConnection getConnection() {
        return connection;
    }

    public ModelRegistration getRegistration(Class<? extends Model> modelClass) throws UndefinedModelException {
        if(!registrations.containsKey(modelClass)) {
            throw new UndefinedModelException(modelClass);
        }

        return registrations.get(modelClass);
    }

    public <TResult extends Model> Query<TResult> get(Class<TResult> modelClass) throws UndefinedModelException {
        return (Query<TResult>) getRegistration(modelClass).getFactory().all();
    }
}