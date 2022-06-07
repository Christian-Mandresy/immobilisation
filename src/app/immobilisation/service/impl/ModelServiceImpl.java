package app.immobilisation.service.impl;

import app.immobilisation.dao.DaoObject;
import app.immobilisation.model.BaseModel;
import app.immobilisation.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private DaoObject daoObject;

    @Override
    public void save(BaseModel baseModel)
    {
        daoObject.save(baseModel);
    }

    @Override
    public void update(BaseModel baseModel)
    {
        daoObject.update(baseModel);
    }

    @Override
    public List findall(BaseModel baseModel)
    {
        return daoObject.findall(baseModel);
    }

    @Override
    public List find(BaseModel baseModel) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return daoObject.find(baseModel);
    }

    @Override
    public List finBetween(BaseModel baseModel, String attr, Date date1, Date date2, int debut, int max) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return daoObject.findBetween(baseModel,attr,date1,date2,debut,max);
    }

    @Override
    public List findById(BaseModel baseModel) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return daoObject.findById(baseModel);
    }

    @Override
    public int countFind(BaseModel baseModel, String attr, Date date1, Date date2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return daoObject.CountRecherche(baseModel,attr,date1,date2);
    }
}
