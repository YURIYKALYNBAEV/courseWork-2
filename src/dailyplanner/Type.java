package dailyplanner;

public enum Type {
    WORK("Рабочая"),
    PERSONAL("Личная");
    private final String name;

    Type(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name +
                " задача";
    }
}
