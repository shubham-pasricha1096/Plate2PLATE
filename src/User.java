public class User {
    private int id;
    private String name;
    private String email;
    private String location;
    private String certificate;

    public User(int id, String name, String email, String location, String certificate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.location = location;
        this.certificate = certificate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getLocation() { return location; }
    public String getCertificate() { return certificate; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", certificate='" + certificate + '\'' +
                '}';
    }
}
