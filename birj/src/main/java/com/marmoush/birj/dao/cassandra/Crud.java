package com.marmoush.birj.dao.cassandra;

import com.marmoush.birj.dao.CrudAPI;
import com.marmoush.birj.model.AbstractEntity;

public class Crud implements CrudAPI {

    @Override
    public <E extends AbstractEntity> E create(E entity) {

	return null;
    }

    @Override
    public <E extends AbstractEntity> E read(String key) {
	return null;
    }

    @Override
    public <E extends AbstractEntity> E update(E entity) {
	return null;
    }

    @Override
    public <E extends AbstractEntity> void delete(String key) {

    }

}
