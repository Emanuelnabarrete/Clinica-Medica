package com.example.clinica.demo.Controller;

import com.example.clinica.demo.Model.Usuario;
import com.example.clinica.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Sessões em memória (token -> usuário)
    private static final Map<String, Usuario> sessions = new HashMap<>();

    // Cadastrar novo usuário
    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.salvar(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    // Login simples
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String senha) {
        Optional<Usuario> usuario = usuarioService.login(email, senha);
        if (usuario.isPresent()) {
            String token = UUID.randomUUID().toString();
            sessions.put(token, usuario.get());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        sessions.remove(token);
        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    // Recuperar usuário da sessão
    public static Usuario getUsuarioByToken(String token) {
        return sessions.get(token);
    }

    // Listar todos os usuários
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable int id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable int id, @RequestBody Usuario usuario) {
        Optional<Usuario> existente = usuarioService.buscarPorId(id);
        if (existente.isPresent()) {
            usuario.setId(id);
            return ResponseEntity.ok(usuarioService.salvar(usuario));
        }
        return ResponseEntity.notFound().build();
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
