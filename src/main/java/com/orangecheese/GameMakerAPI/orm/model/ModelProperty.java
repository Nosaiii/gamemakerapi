package com.orangecheese.GameMakerAPI.orm.model;

public class ModelProperty {
    private String propertyName;
    private Object value;

    public ModelProperty(String propertyName, Object initialValue) {
        this.propertyName = propertyName;
        set(initialValue);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void set(Object value) {
        this.value = value;
    }

    public <TValue> TValue get() {
        try {
            return (TValue) value;
        } catch(ClassCastException e) {
            e.printStackTrace();
        }

        return null;
    }
}