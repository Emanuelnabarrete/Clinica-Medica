package com.example.clinica.demo.Controller;

import com.example.clinica.demo.Model.Consulta;
import com.example.clinica.demo.Model.Usuario;
import com.example.clinica.demo.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private UsuarioController usuarioController;

    // Agendar nova consulta (apenas PACIENTE pode agendar)
    @PostMapping
    public ResponseEntity<?> agendar(@RequestBody Consulta consulta,
                                     @RequestHeader("Authorization") String token) {
        Usuario usuario = UsuarioController.getUsuarioByToken(token);
        if (usuario == null || !usuario.getTipo().equalsIgnoreCase("PACIENTE")) {
            return ResponseEntity.status(401).body("Apenas pacientes podem agendar consultas.");
        }

        consulta.setPacienteId(usuario.getId());
        consulta.setStatus(true); // Ativa por padrão
        Consulta novaConsulta = consultaService.agendar(consulta);
        return ResponseEntity.ok(novaConsulta);
    }

    // Listar consultas do usuário logado
    @GetMapping
    public ResponseEntity<?> listarConsultas(@RequestHeader("Authorization") String token) {
        Usuario usuario = UsuarioController.getUsuarioByToken(token);
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado.");
        }

        List<Consulta> consultas;
        if (usuario.getTipo().equalsIgnoreCase("MEDICO")) {
            consultas = consultaService.listarTodas(); // Médico vê tudo
        } else {
            consultas = consultaService.listarPorPaciente(usuario.getId()); // Paciente vê só as dele
        }

        return ResponseEntity.ok(consultas);
    }

    // Cancelar consulta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable int id) {
        consultaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
