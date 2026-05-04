package com.pedala.api.shared.service;

import com.pedala.api.bike.domain.Bike;
import com.pedala.api.bike.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${app.gemini.api-key}")
    private String apiKey;

    private final BikeRepository bikeRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public String askChatbot(String userMessage) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash:generateContent";

        // Obter as bikes disponiveis para informar o chatbot
        List<Bike> bikes = bikeRepository.findAll();
        StringBuilder catalogo = new StringBuilder();
        for (Bike b : bikes) {
            catalogo.append(b.getNome()).append(" (").append(b.getCategoria()).append("): R$").append(b.getPrecoSemanal()).append("/sem. ");
        }

        String systemPrompt = String.format("""
            # Assistente Pedala (IA)
            ## Papel
            Voce atende clientes da assinatura de bicicletas Pedala. Responda em PT-BR.

            ## Informacoes do servico
            - Assinatura de bikes com planos semanal, quinzenal e mensal.
            - Processo: escolher bike, selecionar plano, definir data de inicio, entrega agendada.
            - Pagamento: quando ha fatura pendente, o cliente solicita no painel; aprovacao mantem contrato ativo.
            - Vistoria: ocorre na devolucao; funcionario avalia bike e registra observacoes antes de finalizar.
            - Devolucao: cliente solicita no painel e a equipe coleta para vistoria.

            ## Catalogo atual
            %s

            ## Estilo de resposta
            - Curta e sucinta, maximo 2 frases.
            - Sem listas, sem emojis.
            - Se faltar informacao, pedir um detalhe objetivo.

            ## Resposta esperada
            Curta e direta, focada no aluguel de bicicletas.

            Mensagem do usuario: %s
            """, catalogo, userMessage);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", systemPrompt)
                        ))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-goog-api-key", apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            log.info("Chamando Gemini API: {}", url);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    if (content != null) {
                        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                        if (parts != null && !parts.isEmpty()) {
                            return (String) parts.get(0).get("text");
                        }
                    }
                }
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("Erro de Cliente na Gemini API: {} - Status: {} - Body: {}", 
                url, e.getStatusCode(), e.getResponseBodyAsString());
            return "Erro na API Gemini: " + e.getStatusCode() + ". Verifique se o modelo 'gemini-flash-latest' está disponível para sua chave e se ela possui saldo/permissão.";
        } catch (Exception e) {
            log.error("Erro generico na API da Gemini: ", e);
            return "Desculpe, ocorreu um erro inesperado ao falar com o Gemini. Verifique o console do backend.";
        }

        return "Não consegui formular uma resposta agora.";
    }
}
