package ua.com.foxminded.service.services;

import java.util.List;

import ua.com.foxminded.exception.ServiceException;

public interface ModelsService<T> {
    T get(int id) throws ServiceException;

    List<T> getAll() throws ServiceException;

    void add(T entity) throws ServiceException;

    void delete(int id) throws ServiceException;
    
    void update(T entity) throws ServiceException;
}
