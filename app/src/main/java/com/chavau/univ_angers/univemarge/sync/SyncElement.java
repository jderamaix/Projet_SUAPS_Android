package com.chavau.univ_angers.univemarge.sync;

import com.chavau.univ_angers.univemarge.database.dao.IMergeable;
import com.chavau.univ_angers.univemarge.database.entities.Entity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SyncElement {

    private final String link;
    private final Class<? extends Entity[]> entityClass;
    private final IMergeable iMergeable;

    public SyncElement(String link, Class<? extends Entity[]> entityClass, IMergeable iMergeable) {
        this.link = link;
        this.entityClass = entityClass;
        this.iMergeable = iMergeable;
    }

    public void merge(String jsonResponse) throws IOException {
        // parse response with Jackson
        ObjectMapper mapper = new ObjectMapper();
        try{
            Entity[] objects = mapper.readValue(jsonResponse, entityClass);
            iMergeable.merge(objects);
        }catch(Exception e){
            Entity[] objects = mapper.readValue("["+jsonResponse+"]", entityClass);
            iMergeable.merge(objects);
        }

    }

    public String getLink() {
        return link;
    }

    public Class<? extends Entity[]> getEntityClass() {
        return entityClass;
    }

    public IMergeable getiMergeable() {
        return iMergeable;
    }
}
