package garcia.ruben.personal_project.utility.data;

public enum Keywords {
    Nature("Nature"),
    Luxury("Luxury"),
    History("History"),
    Foodie("Foodie"),

    Art("Art"),

    Music("Music"),

    Unique("Weird Unique Things"),

    Culture("Culture"),

    Attractions("Attractions");

    private final String value;

    Keywords(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
