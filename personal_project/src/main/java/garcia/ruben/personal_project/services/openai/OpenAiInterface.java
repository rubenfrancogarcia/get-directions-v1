package garcia.ruben.personal_project.services.openai;

import garcia.ruben.personal_project.pojos.openai.ChatRequest;
import garcia.ruben.personal_project.pojos.openai.ChatResponse;
import garcia.ruben.personal_project.pojos.openai.UserAnalysisPojo;

public interface OpenAiInterface {
    ChatResponse outputRecommendations(ChatRequest chatRequest);
}
