package model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.xmldb.api.*;
import org.exist.xmldb.EXistResource;

/**
 *
 * @author Alex
 */
public class ConnectionXML {

    //Registrar el driver proporcionado por el proveedor del producto.El driver viene expresado de la siguiente forma:
    static final String DRIVER = "org.exist.xmldb.DatabaseImpl";
    static final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db";
    static final String USER = "admin";
    static final String PASS = "ad";

    public List<String> connectionXML(String xquery) {
        List<String> lista = new ArrayList();
        Collection col = null;

        try {
            //Iniciamos el driver
            Class<?> cl = Class.forName(DRIVER);
            //Abrimos la conexión
            Database database = (Database) cl.newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);

            col = DatabaseManager.getCollection(URI, USER, PASS);
            XPathQueryService xpqs = (XPathQueryService) col.getService("XPathQueryService", "1.0");
            xpqs.setProperty("indent", "yes");
            ResourceSet result = xpqs.query(xquery);
            ResourceIterator i = result.getIterator();
            Resource res = null;
            while (i.hasMoreResources()) {
                try {
                    res = i.nextResource();
                    System.out.println(res.getContent());
                    String linea=  (String) res.getContent();
                    lista.add(linea);
                } finally {

                    try {
                        ((EXistResource) res).freeResources();
                    } catch (XMLDBException xe) {
                        xe.printStackTrace();
                    }
                }
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectionXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ConnectionXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ConnectionXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLDBException ex) {
            Logger.getLogger(ConnectionXML.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //dont forget to cleanup
            if (col != null) {
                try {
                    col.close();
                } catch (XMLDBException xe) {
                    xe.printStackTrace();
                }
            }
        }

        return lista;

    }

}
