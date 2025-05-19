package com.example.clinica.demo.repository;

import com.example.clinica.demo.Model.Documento;
import com.example.clinica.demo.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Integer> {
    List<Documento> findByPaciente(Usuario paciente);
}
