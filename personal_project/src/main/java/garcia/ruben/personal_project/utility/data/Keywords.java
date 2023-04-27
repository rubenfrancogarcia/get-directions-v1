package garcia.ruben.personal_project.utility.data;

public enum Keywords {
    Nature("nature"),
    Luxury("luxury"),
    History("history"),
    Foodie("foodie");

    private final String value;

    Keywords(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
