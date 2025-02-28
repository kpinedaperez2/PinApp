package com.local.clientes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.local.clientes.application.mapper.ClienteMapper;
import com.local.clientes.application.model.Metrica;
import com.local.clientes.application.usecase.ClienteService;
import com.local.clientes.infrastructure.config.JwtUtil;
import com.local.clientes.router.ClientsController;
import com.local.clientes.router.dto.AuthResponse;
import com.local.clientes.router.dto.ClienteDTO;
import com.local.clientes.router.dto.ClientePlusDto;
import com.local.clientes.router.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ClientsController.class)
public class ClientsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private ClienteService clienteService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ClientsController clientsController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testRegistrarCliente() throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO();

        doNothing().when(clienteService).registrarCliente(any());

        mockMvc.perform(post("/api/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(clienteService, times(1)).registrarCliente(any());
    }

    @Test
    public void testObtenerMetricas() throws Exception {
        Metrica metrica = new Metrica();

        when(clienteService.calcularMetricas()).thenReturn(metrica);

        mockMvc.perform(get("/api/metricas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(metrica)));

        verify(clienteService, times(1)).calcularMetricas();
    }

    @Test
    public void testListarClientesConMetrica() throws Exception {
        List<ClientePlusDto> clientes = List.of(new ClientePlusDto(), new ClientePlusDto());

        when(clienteService.obtenerClientesConMetrica()).thenReturn(clientes);

        mockMvc.perform(get("/api/listar"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").exists());

        verify(clienteService, times(1)).obtenerClientesConMetrica();
    }

    @Test
    public void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        AuthResponse authResponse = new AuthResponse("sample_token");

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtUtil.generateToken("username")).thenReturn("sample_token");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("sample_token"));

        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtil, times(1)).generateToken("username");
    }
}

