package com.example.clinica.demo.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_Documento")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String tipo;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Usuario paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Usuario medico;

    private String laudo;

    public Documento() {}

    public Documento(int id, String tipo, Usuario paciente, Usuario medico, String laudo) {
        this.id = id;
        this.tipo = tipo;
        this.paciente = paciente;
        this.medico = medico;
        this.laudo = laudo;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Usuario getPaciente() { return paciente; }
    public void setPaciente(Usuario paciente) { this.paciente = paciente; }

    public Usuario getMedico() { return medico; }
    public void setMedico(Usuario medico) { this.medico = medico; }

    public String getLaudo() { return laudo; }
    public void setLaudo(String laudo) { this.laudo = laudo; }

    @Override
    public String toString() {
        return "Documento{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", paciente=" + paciente +
                ", medico=" + medico +
                ", laudo='" + laudo + '\'' +
                '}';
    }
}
