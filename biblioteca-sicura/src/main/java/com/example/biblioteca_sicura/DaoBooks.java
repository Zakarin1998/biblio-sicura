package com.example.biblioteca_sicura;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import java.util.List;

public class DaoBooks implements IDao<Book,Long> {
    EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("libreria");

    public List<Book> list() throws DaoException {
        List<Book> listaLibri;

        EntityManager entityManager = sessionFactory.createEntityManager();
        TypedQuery<Book> result = entityManager.createQuery("select us from  us", Book.class);
        listaLibri= result.getResultList();
        entityManager.close();
        return listaLibri;
    }

    public Long add(Book book) throws DaoException {
        try{
            EntityManager entityManager = sessionFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(book);
            entityManager.getTransaction().commit();
            entityManager.close();
        }catch(Exception e){
            e.printStackTrace();
            throw new DaoException("Errore nell'Add");
        }
        return 1L;
    }

    public Long delete(Long aLong) throws DaoException {
        try {
            EntityManager entityManager = sessionFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(aLong);
            entityManager.getTransaction().commit();
            entityManager.close();
        }catch(Exception e){
            e.printStackTrace();
            throw new DaoException("Errore nel delete");
        }
        return 1L;
    }

    public Long update(Long aLong, Book book) throws DaoException {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Book libro=entityManager.find(Book.class,aLong);
        book.setIsbn(libro.getIsbn());
        entityManager.merge(book);
        entityManager.getTransaction().commit();
        entityManager.close();
        return 0L;
    }

    public Book getById(Long aLong) throws DaoException {
        Book libro;
        try{
            EntityManager entityManager = sessionFactory.createEntityManager();
            entityManager.getTransaction().begin();
            libro=entityManager.find(Book.class,aLong);
            entityManager.getTransaction().commit();
            entityManager.close();
        }catch(Exception e){
            e.printStackTrace();
            throw new DaoException("Errore nel getById");
        }
        return libro;
    }

    public List<Book> find(String s) throws DaoException {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        TypedQuery<Book> result = entityManager.createQuery("select us from Book us where us.titolo='"+s+"' or us.autore='"+s+"' or us.editore='"+s+"' or us.img='"+s+"' ", Book.class);
        entityManager.getTransaction().commit();
        entityManager.close();
        return result.getResultList();
    }

    public List<Book> find(Book book) throws DaoException {
        List<Book> listaLibri=null;
        try{

            EntityManager entityManager = sessionFactory.createEntityManager();
            entityManager.getTransaction().begin();
            String query="select us from Book us where "; //or us.autore= or us.editore= or us.immagine= "
            if(book.getTitolo()!=null){
                query+="us.nome="+book.getTitolo()+" ";
            }
            if(book.getAutore()!=null){
                query+="or us.autore="+book.getAutore()+" ";
            }
            if(book.getEditore()!=null){
                query+="or us.editore="+book.getEditore()+" ";
            }
            if(book.getImg()!=null){
                query+="or us.immagine="+book.getImg()+" ";
            }
            TypedQuery<Book> result = entityManager.createQuery(query, Book.class);
            listaLibri= result.getResultList();
        }catch(Exception e){
            e.printStackTrace();
            throw new DaoException("Errore nel findString");

        }
        return listaLibri;
    }

}
