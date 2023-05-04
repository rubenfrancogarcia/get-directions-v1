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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class OpenAiWebAppImpl implements OpenAiInterface {

    private static final Logger logger = LogManager.getLogger(OpenAiWebAppImpl.class);

    @Value("${openai.api.key}")
    private String openAiKey;

    private WebClient client = WebClient.create();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ChatResponse outputRecommendations(ChatRequest chatRequest) {
        String response;
        ChatResponse chatResponseObject;
        try {
            response = client.post().uri(new URI("https://api.openai.com/v1/chat/completions"))
                    .header("Authorization", openAiKey).accept(MediaType.APPLICATION_JSON)
                    .bodyValue(chatRequest).retrieve().bodyToMono(String.class).block();
        } catch (URISyntaxException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }

        try {
            chatResponseObject = objectMapper.readValue(response, ChatResponse.class);
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
        return recommendation.split("\n");
    }


}
