package com.orangecheese.GameMakerAPI.orm.modelfacade;

import com.orangecheese.GameMakerAPI.orm.Query;
import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnection;
import com.orangecheese.GameMakerAPI.orm.connection.DatabaseConnectionProperties;
import com.orangecheese.GameMakerAPI.orm.exceptions.DuplicateModelRegistration;
import com.orangecheese.GameMakerAPI.orm.exceptions.EmptyQueryResultException;
import com.orangecheese.GameMakerAPI.orm.exceptions.MissingModelConstructor;
import com.orangecheese.GameMakerAPI.orm.exceptions.UndefinedModelException;
import com.orangecheese.GameMakerAPI.orm.model.IModelMapper;
import com.orangecheese.GameMakerAPI.orm.model.Model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModelService {
    private final DatabaseConnection connection;

    private final Map<Class<? extends Model>, IModelMapper> modelMappers;
    private final Map<Class<? extends Model>, IModelFactory<? extends Model>> singletonFactories;

    public ModelService(DatabaseConnectionProperties properties) {
        connection = new DatabaseConnection(properties);

        modelMappers = new HashMap<>();
        singletonFactories = new HashMap<>();
    }

    public void register(ModelRegistrationProperties properties) throws DuplicateModelRegistration {
        if(modelMappers.containsKey(properties.getModelClass()) || singletonFactories.containsKey(properties.getModelClass())) {
            throw new DuplicateModelRegistration();
        }

        modelMappers.put(properties.getModelClass(), properties.getModelMapper());
        singletonFactories.put(properties.getModelClass(), registerFacade(properties));
    }

    private <T extends Model> IModelFactory<T> registerFacade(ModelRegistrationProperties properties) {
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

                IModelMapper mapper = modelMappers.get(properties.getModelClass());
                return new Query<T>(mapper, connection, result);
            }

            @Override
            public T create() {
                try {
                    Constructor<T> constructor = (Constructor<T>) properties.getModelClass().getConstructor(DatabaseConnection.class);
                    return constructor.newInstance(connection);
                } catch(NoSuchMethodException e) {
                    new MissingModelConstructor(properties.getModelClass()).printStackTrace();
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
    }

    public <TResult extends Model> Query<TResult> get(Class<TResult> modelClass) throws UndefinedModelException {
        if(!singletonFactories.containsKey(modelClass)) {
            throw new UndefinedModelException(modelClass);
        }

        return (Query<TResult>) singletonFactories.get(modelClass).all();
    }

    public <TResult extends Model> TResult create(Class<TResult> modelClass) throws UndefinedModelException {
        if(!singletonFactories.containsKey(modelClass)) {
            throw new UndefinedModelException(modelClass);
        }

        return (TResult) singletonFactories.get(modelClass).create();
    }
}