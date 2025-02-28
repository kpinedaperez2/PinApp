package com.local.clientes.router;

import com.local.clientes.application.mapper.ClienteMapper;
import com.local.clientes.application.model.Metrica;
import com.local.clientes.application.usecase.ClienteService;
import com.local.clientes.infrastructure.config.JwtUtil;
import com.local.clientes.router.dto.AuthResponse;
import com.local.clientes.router.dto.ClienteDTO;
import com.local.clientes.router.dto.ClientePlusDto;

import com.local.clientes.router.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientsController {

    private final ClienteMapper clienteMapper;
    private final ClienteService clienteService;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ClientsController(ClienteMapper clienteMapper, ClienteService clienteService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.clienteMapper = clienteMapper;
        this.clienteService = clienteService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/registrar")
    public void registrarCliente(@RequestBody ClienteDTO clienteDTO) {
        clienteService.registrarCliente(clienteMapper.toClienteModel(clienteDTO));
    }

    @GetMapping("/metricas")
    public Metrica obtenerMetricas() {
        return clienteService.calcularMetricas();
    }

    @GetMapping("/listar")
    public List<ClientePlusDto> listarClientesConMetrica() {
        return clienteService.obtenerClientesConMetrica();
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        String token = jwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}