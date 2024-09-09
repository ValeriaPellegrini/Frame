package com.telecom.data;

public class Activity {
    private String nro_id;
    private String secuencia;
    private String descripcion;

    public Activity ( String nro_id, String secuencia, String descripcion)
    {
        this.nro_id=nro_id;
        this.secuencia=secuencia;
        this.descripcion=descripcion;
    }

    public String getNro_id() {
        return nro_id;
    }

    public void setNro_id(String nro_id) {
        this.nro_id = nro_id;
    }

    public String getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActivityData ()
    {
        return "Actividad con Nro_id:"+ this.getNro_id()+", Nro Secuencia:"+this.getSecuencia()+ ", Descripcion: "+this.getDescripcion();

    }
}
