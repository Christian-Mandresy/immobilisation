package app.immobilisation.service;

import app.immobilisation.model.BaseModel;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

public interface ModelService {
    void save(BaseModel baseModel);

    void update(BaseModel baseModel);

    List findall(BaseModel baseModel);

    List find(BaseModel baseModel) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    List finBetween(BaseModel baseModel, String attr, Date date1, Date date2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    List findById(BaseModel baseModel) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}
