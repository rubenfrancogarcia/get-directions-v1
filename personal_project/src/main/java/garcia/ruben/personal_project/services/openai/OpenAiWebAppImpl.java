package garcia.ruben.personal_project.services.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import garcia.ruben.personal_project.pojos.openai.ChatRequest;
import garcia.ruben.personal_project.pojos.openai.ChatResponse;
import garcia.ruben.personal_project.pojos.openai.Choices;
import garcia.ruben.personal_project.pojos.openai.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.Collections;

@Service
public class OpenAiWebAppImpl implements OpenAiInterface {

    private static final Logger logger = LogManager.getLogger(OpenAiWebAppImpl.class);

    @Value("${openai.api.key}")
    private String openAiKey;
    //private WebClient.Builder client = WebClient.builder();
    private ObjectMapper objectMapper = new ObjectMapper();

    private RestTemplate restTemplate;

    private ClientHttpConnector connector() {
        return new
                ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection()));
    }

    @Override
    public ChatResponse outputRecommendations(ChatRequest chatRequest) {
        String response = null;
        ChatResponse chatResponseObject;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(openAiKey);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity httpEntity = new HttpEntity(chatRequest, headers);
            restTemplate = new RestTemplate();
            response = restTemplate.postForEntity("https://api.openai.com/v1/chat/completions", httpEntity, String.class).getBody();
           /* response = client.build().post().uri(new URI("https://api.openai.com/v1/chat/completions"))
                    .headers(h -> h.setBearerAuth(openAiKey)).accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(BodyInserters.fromValue(chatRequest)).retrieve().bodyToMono(String.class).block();*/
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }

        try {
            chatResponseObject = objectMapper.readValue(response, ChatResponse.class);
            logger.info("response of content {}", chatResponseObject.getChoices()[0].getMessage().getContent());
        } catch (JsonProcessingException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        return chatResponseObject;
    }

    public String[] processChatResponseObject(ChatResponse chatResponse) {
        Choices[] choices = chatResponse.getChoices();
        Message message = choices[0].getMessage();
        String recommendation = message.getContent();
        logger.info("reformated response of content" +  recommendation);
        return recommendation.split("\n");
    }


}
