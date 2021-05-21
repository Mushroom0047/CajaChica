package cl.eugcom.cajachica.Project.Model;

public class Category {
    private String id_ga, id_in, nombre_in, nombre_ga;

    public Category(){
    }
    public void createIn(String id_i, String nombre){
        this.id_in = id_i;
        this.nombre_in = nombre;
    }
    public void createGa(String id_g, String nombre){
        this.id_ga = id_g;
        this.nombre_ga = nombre;
    }

    public String getId_ga() {
        return id_ga;
    }

    public void setId_ga(String id_ga) {
        this.id_ga = id_ga;
    }

    public String getId_in() {
        return id_in;
    }

    public void setId_in(String id_in) {
        this.id_in = id_in;
    }

    public String getNombre_in() {
        return nombre_in;
    }

    public void setNombre_in(String nombre_in) {
        this.nombre_in = nombre_in;
    }

    public String getNombre_ga() {
        return nombre_ga;
    }

    public void setNombre_ga(String nombre_ga) {
        this.nombre_ga = nombre_ga;
    }
}
