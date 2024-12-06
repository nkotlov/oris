package dz3;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String phoneNumber;
    private Integer yearsExperience;

    public User(Long id, String firstName, String lastName, Integer age, String email, String phoneNumber, Integer yearsExperience) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.yearsExperience = yearsExperience;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getYearsExperience() {
        return yearsExperience;
    }
}
