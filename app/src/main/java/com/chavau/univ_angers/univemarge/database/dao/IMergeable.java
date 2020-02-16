package com.chavau.univ_angers.univemarge.database.dao;

import com.chavau.univ_angers.univemarge.database.entities.Entity;

public interface IMergeable {

    void merge(Entity[] entities);
}
