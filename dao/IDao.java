package com.example.biblioteca_sicura.dao;

import java.util.List;

public interface IDao <T,I> {

    List<T> list() throws DaoException;
    I add(T elemento)throws DaoException;
    I delete(I id) throws DaoException;
    I update(I id, T elemento)  throws DaoException;
    T getById(I indice)  throws DaoException;
    List<T> find(String searchText)  throws DaoException;
    List<T> find(T searchText) throws DaoException;

}
