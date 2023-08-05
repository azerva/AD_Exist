/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import org.w3c.dom.events.MouseEvent;

/**
 *
 * @author Alex
 */
public class Util {

    /**
     * Agregamos nuestra imagen de logo del programa
     *
     * @return
     */
    public static Image getIconImage() {
        Image image = Toolkit.getDefaultToolkit().getImage(ClassLoader
                .getSystemResource("resources/logo.png"));
        return image;
    }

    public static String documentoActual() {
        String query = "for $docs in doc(\"colecciones.xml\")\n"
                + "return $docs";
        return query;
    }

    public static String consulta1() {
        String query = "for $libros in doc(\"colecciones.xml\")//libro[@publicacion=2002]\n"
                + "order by $libros/titulo ascending\n"
                + "return $libros/titulo";
        return query;
    }

    public static String consulta2() {
        String query = "for $libros in doc(\"colecciones.xml\")//libro\n"
                + "where count($libros/autor) > 1\n"
                + "return $libros/titulo";
        return query;
    }

    public static String consulta3() {
        String query = "for $prestamo in /biblioteca/prestamos/entrada\n"
                + "let $titulo := $prestamo/titulo\n"
                + "let $libro := /biblioteca/libros/libro[titulo = $titulo][1]\n"
                + "return <libro>\n"
                + "          {$titulo/text()}\n"
                + "          <primer_autor>{($libro/autor[1]/nombre, ' ', $libro/autor[1]/apellido)}</primer_autor>\n"
                + "          <paginas>{$libro/paginas}</paginas>\n"
                + "       </libro>";
        return query;
    }

    public static String consulta4() {
        String query = "for $lector in distinct-values(/biblioteca/prestamos/entrada/prestamo/lector/nombre)\n"
                + "let $prestamos := /biblioteca/prestamos/entrada[prestamo/lector/nombre = $lector]\n"
                + "let $paginas := sum(for $prestamo in $prestamos\n"
                + "                    let $titulo := $prestamo/titulo\n"
                + "                    let $libro := /biblioteca/libros/libro[titulo = $titulo][1]\n"
                + "                    return $libro/paginas)\n"
                + "return <lector>\n"
                + "         <nombre>{$lector}</nombre>\n"
                + "         <paginas>{$paginas}</paginas>\n"
                + "       </lector>";
        return query;
    }

    public static String insert() {
        String query = "update insert <evaluacion>10</evaluacion> into /biblioteca/libros/libro[@publicacion = 2007][last()]";
        return query;
    }

    public static String modify() {
        String query = "for $inicio in //prestamo/inicio\n"
                + "return update rename $inicio as 'fechainicio'";
        return query;
    }

    public static String sustituir() {
        String query = "update replace /biblioteca/libros/libro[@publicacion = 2005]/paginas/text() with 700";
        return query;
    }

    public static String delete() {
        String query = "for $dir in //direccion\n"
                + "return update delete $dir";
        return query;
    }

    public static void fillList(List<String> lista, JList list) {
        DefaultListModel<String> modeloLista = new DefaultListModel<>(); // Crea un modelo de lista vacío

        modeloLista.removeAllElements();
        // Agrega los elementos a la lista
        for (String elemento : lista) {
            modeloLista.addElement(elemento);
        }

        list.setModel(modeloLista); // Asigna el modelo de lista a tu JList

    }
}
