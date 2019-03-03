package com.marmoush.birj.dao;

import com.marmoush.birj.model.AbstractEntity;

public interface CrudAPI {
    public <E extends AbstractEntity> E create(E entity);

    public <E extends AbstractEntity> E read(String key);

    public <E extends AbstractEntity> E update(E entity);

    public <E extends AbstractEntity> void delete(String key);
}
