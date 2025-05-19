package com.example.clinica.demo.service;

import com.example.clinica.demo.Model.Consulta;
import com.example.clinica.demo.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    public Consulta agendar(Consulta consulta) {
        consulta.setStatus(true); // ativa por padrão
        return consultaRepository.save(consulta);
    }

    public List<Consulta> listarPorPaciente(int pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId);
    }


    public Optional<Consulta> buscarPorId(int id) {
        return consultaRepository.findById(id);
    }

    public void cancelar(int id) {
        consultaRepository.deleteById(id);
    }

    public List<Consulta> listarTodas() {

         return consultaRepository.findAll();

    }


}

