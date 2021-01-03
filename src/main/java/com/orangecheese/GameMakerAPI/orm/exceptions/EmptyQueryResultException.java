package com.orangecheese.GameMakerAPI.orm.exceptions;

public class EmptyQueryResultException extends Exception {
    public EmptyQueryResultException(String query) {
        super("An empty result was returned by executing a query (QUERY: [" + query + "])");
    }
}
