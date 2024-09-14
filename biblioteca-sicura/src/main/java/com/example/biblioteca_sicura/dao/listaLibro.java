package com.example.biblioteca_sicura.dao;
import it.alfasoft.database.DaoException;
import it.alfasoft.database.bibliotecadao.DaoLibro;
import it.alfasoft.database.bibliotecadao.Libro;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

@Path("/ricerca")
public class listaLibro{

    /*public String returnDbName(){
        String db=null;
        try(InputStream input = getClass().getClassLoader().getResourceAsStream("configuration.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            db = prop.getProperty("dbName");
            System.out.println(db);
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    return db;
    }
    String nomeDb= new listaLibro().returnDbName();*/


    @GET

    @Produces(MediaType.APPLICATION_JSON)
    public Response trova(@QueryParam("parola") String parola) throws DaoException, ClassNotFoundException {
        if(parola==null) {
            return Response.ok(new DaoLibro(/*nomeDb*/"corso").list()).build();
        }else{
            return  Response.ok(new DaoLibro(/*nomeDb*/"corso").find(parola)).build();
        }
    }
    @POST
    @Path("/addBook")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response aggiungiLibro( Libro libro) throws DaoException, ClassNotFoundException {
        return Response.ok(new DaoLibro(/*nomeDb*/"corso").add(libro)).build();
    }
    @PUT
    @Path("/aggiorna/{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response aggiornaLibro(@PathParam("isbn")Long isbn,Libro libro) throws DaoException, ClassNotFoundException {
        return Response.ok(new DaoLibro(/*nomeDb*/"corso").update(isbn,libro)).build();
    }
    @DELETE
    @Path("/cancella/{isbn}")
    public Response deleteLibro(@PathParam("isbn")Long isbn) throws DaoException, ClassNotFoundException {
        return Response.ok(new DaoLibro(/*nomeDb*/"corso").delete(isbn)).build();
    }
}