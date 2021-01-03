package com.orangecheese.GameMakerAPI.orm;

import com.orangecheese.GameMakerAPI.orm.exceptions.ModelNotSyncedWithDatabaseException;
import com.orangecheese.GameMakerAPI.orm.model.IModelMapper;
import com.orangecheese.GameMakerAPI.orm.model.Model;
import com.orangecheese.GameMakerAPI.orm.model.ModelProperty;
import com.orangecheese.GameMakerAPI.orm.modelfacade.ModelService;
import com.orangecheese.helpers.ICloneable;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;

public class Query<T extends Model> implements ICloneable<Query<T>> {
    private List<T> collection;

    public Query(IModelMapper modelMapper, ModelService modelService, ResultSet resultSet) {
        collection = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Model model = modelMapper.Map(modelService, resultSet);
                collection.add((T) model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Query(List<T> fromList) {
        collection = fromList;
    }

    private Query(Query<T> base) {
        collection = new ArrayList<>();
        collection.addAll(base.collection);
    }

    public boolean all(Predicate<T> predicate) {
        for (T m : collection) {
            if (!predicate.test(m)) {
                return false;
            }
        }

        return true;
    }

    public boolean any(Predicate<T> predicate) {
        for (T m : collection) {
            if (predicate.test(m)) {
                return true;
            }
        }

        return false;
    }

    public double average(String propertyName) {
        return sum(propertyName) / collection.size();
    }

    public boolean contains(T model) {
        for (T m : collection) {
            if (m == model) {
                return true;
            }
        }

        return false;
    }

    public int count() {
        return collection.size();
    }

    public Query<T> distinct() {
        Query<T> cloned = clone();

        for (T m : collection) {
            if (!cloned.contains(m)) {
                cloned.collection.add(m);
            }
        }

        return cloned;
    }

    public T first() {
        if(collection.isEmpty()) {
            return null;
        }

        return collection.get(0);
    }

    public T firstOrDefault(Predicate<T> predicate) {
        return where(predicate).first();
    }

    public T last() {
        if(collection.isEmpty()) {
            return null;
        }

        return collection.get(collection.size() - 1);
    }

    public <V> Map<V, Query<T>> groupBy(String propertyName) {
        Map<V, Query<T>> groupedMap = new HashMap<>();

        for (T m : collection) {
            ModelProperty property = m.getProperty(propertyName);
            V propertyValue = property.get();

            Query<T> query = new Query<>(this);
            if (groupedMap.containsKey(propertyValue)) {
                query = groupedMap.get(propertyValue);
            }

            query.collection.add(m);
            groupedMap.put(propertyValue, query);
        }

        return groupedMap;
    }

    public <V> Query<T> orderBy(String propertyName) {
        Query<T> cloned = clone();

        cloned.collection.sort((o1, o2) -> {
            ModelProperty property1 = o1.getProperty(propertyName), property2 = o2.getProperty(propertyName);
            V propertyValue1 = property1.get(), propertyValue2 = property2.get();

            if (!(propertyValue1 instanceof Comparable) || !(propertyValue2 instanceof Comparable)) {
                throw new IllegalArgumentException("Given properties can not be compared");
            }

            Comparable<V> propertyComparable = (Comparable<V>) propertyValue1;
            return propertyComparable.compareTo(propertyValue2);
        });

        return cloned;
    }

    public Query<T> orderByDescending(String fieldName) {
        return orderBy(fieldName).reverse();
    }

    public Query<T> reverse() {
        Query<T> cloned = clone();
        cloned.collection = new ArrayList<>();

        for (int i = collection.size() - 1; i >= 0; i--) {
            cloned.collection.add(collection.get(i));
        }

        return cloned;
    }

    public double max(String propertyName) {
        if(collection.isEmpty()) {
            return 0;
        }

        double highest = Double.MIN_VALUE;

        for(T m : collection) {
            ModelProperty property = m.getProperty(propertyName);
            double propertyValue = property.get();

            if(propertyValue > highest) {
                highest = propertyValue;
            }
        }

        return highest;
    }

    public double min(String propertyName) {
        if(collection.isEmpty()) {
            return 0;
        }

        double lowest = Double.MAX_VALUE;

        for(T m : collection) {
            ModelProperty property = m.getProperty(propertyName);
            double propertyValue = property.get();

            if(propertyValue < lowest) {
                lowest = propertyValue;
            }
        }

        return lowest;
    }

    public <S> List<S> select(String propertyName) {
        List<S> list = new ArrayList<>();

        for(T m : collection) {
            list.add(m.getProperty(propertyName).get());
        }

        return list;
    }

    public double sum(String propertyName) {
        double sum = 0;

        for(T m : collection) {
            ModelProperty property = m.getProperty(propertyName);
            double propertyValue = property.get();

            sum += propertyValue;
        }

        return sum;
    }

    public Query<T> where(Predicate<T> predicate) {
        Query<T> cloned = clone();

        for(T m : collection) {
            if(!predicate.test(m)) {
                cloned.collection.remove(m);
            }
        }

        return cloned;
    }

    public T[] toArray() {
        return (T[]) collection.toArray(new Model[collection.size()]);
    }

    public List<T> toList() {
        return collection;
    }

    @Override
    public Query<T> clone() {
        Query<T> cloned = new Query<T>(this);
        return cloned;
    }
}
