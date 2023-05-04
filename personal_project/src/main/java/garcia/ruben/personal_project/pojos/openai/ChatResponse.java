package garcia.ruben.personal_project.pojos.openai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatResponse {
    private String id;

    private String object;

    private long created;

    private String model;

    private Usage usuage;

    private Choices[] choices;
}
