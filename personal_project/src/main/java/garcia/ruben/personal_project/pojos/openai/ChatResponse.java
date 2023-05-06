package garcia.ruben.personal_project.pojos.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponse {
    private String id;

    private String object;

    private long created;

    private String model;

    private Usage usage;

    private Choices[] choices;
}
