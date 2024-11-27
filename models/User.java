package models;

public interface User {
    String getName();
    String getId();

    default String displayInfo() {
        return String.format("Name: %s, ID: %s", getName(), getId());
    }
}
