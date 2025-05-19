package com.example.clinica.demo.Controller;

import com.example.clinica.demo.Model.Documento;
import com.example.clinica.demo.Model.Usuario;
import com.example.clinica.demo.repository.DocumentoRepository;
import com.example.clinica.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioController usuarioController;

    // Criar novo documento (apenas médicos)
    @PostMapping
    public ResponseEntity<?> criarDocumento(@RequestBody Documento documento,
                                            @RequestHeader("Authorization") String token) {
        Usuario medico = UsuarioController.getUsuarioByToken(token);
        if (medico == null || !medico.getTipo().equalsIgnoreCase("MEDICO")) {
            return ResponseEntity.status(401).body("Apenas médicos podem criar documentos.");
        }

        // Buscar paciente pelo ID recebido no JSON
        Usuario paciente = usuarioRepository.findById(documento.getPaciente().getId()).orElse(null);
        if (paciente == null) {
            return ResponseEntity.badRequest().body("Paciente não encontrado.");
        }

        documento.setMedico(medico);
        documento.setPaciente(paciente);

        Documento salvo = documentoRepository.save(documento);
        return ResponseEntity.ok(salvo);
    }

    // Listar todos os documentos (admin ou médico)
    @GetMapping
    public ResponseEntity<List<Documento>> listarTodos() {
        return ResponseEntity.ok(documentoRepository.findAll());
    }

    // Listar documentos de um paciente específico
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<Documento>> listarPorPaciente(@PathVariable int id) {
        Usuario paciente = usuarioRepository.findById(id).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(documentoRepository.findByPaciente(paciente));
    }
}
