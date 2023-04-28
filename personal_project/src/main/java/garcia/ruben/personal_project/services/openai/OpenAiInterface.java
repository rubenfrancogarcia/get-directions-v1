package garcia.ruben.personal_project.services.openai;

import garcia.ruben.personal_project.pojos.openai.PromptPojo;
import garcia.ruben.personal_project.pojos.openai.UserAnalysisPojo;

public interface OpenAiInterface {
    void outputRecommendations(PromptPojo promptPojo);

    void analyzeUserProfile(UserAnalysisPojo pojo);

    void configurationsSetup();

    void recalibrateResponse();
}
