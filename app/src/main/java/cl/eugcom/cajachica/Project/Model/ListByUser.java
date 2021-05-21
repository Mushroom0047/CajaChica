package cl.eugcom.cajachica.Project.Model;

import android.graphics.Bitmap;

public class ListByUser {
    //List with user income& spend data from SB
    private String id_igreso;
    private String id_gasto;
    private String id_usuario;
    private String img_ingreso;
    private String img_gasto;
    private String nombre_foto;
    private String valor_ingreso;
    private String valor_gasto;
    private String fecha_ingreso;
    private String fecha_gasto;

    public String getId_igreso() {
        return id_igreso;
    }

    public void setId_igreso(String id_igreso) {
        this.id_igreso = id_igreso;
    }

    public String getId_gasto() {
        return id_gasto;
    }

    public void setId_gasto(String id_gasto) {
        this.id_gasto = id_gasto;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getImg_ingreso() {
        return img_ingreso;
    }

    public void setImg_ingreso(String img_ingreso) {
        this.img_ingreso = img_ingreso;
    }

    public String getImg_gasto() {
        return img_gasto;
    }

    public void setImg_gasto(String img_gasto) {
        this.img_gasto = img_gasto;
    }

    public String getNombre_foto() {
        return nombre_foto;
    }

    public void setNombre_foto(String nombre_foto) {
        this.nombre_foto = nombre_foto;
    }

    public String getValor_ingreso() {
        return valor_ingreso;
    }

    public void setValor_ingreso(String valor_ingreso) {
        this.valor_ingreso = valor_ingreso;
    }

    public String getValor_gasto() {
        return valor_gasto;
    }

    public void setValor_gasto(String valor_gasto) {
        this.valor_gasto = valor_gasto;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getFecha_gasto() {
        return fecha_gasto;
    }

    public void setFecha_gasto(String fecha_gasto) {
        this.fecha_gasto = fecha_gasto;
    }
}
