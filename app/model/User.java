package model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by red on 23.09.16.
 */

@Entity
@Table(name = "users", schema = "KunderaExamples@cassandra_pu")
public class User {
    @Id
    private String userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "city")
    private String city;

    public User(String userId, String firstName, String lastName, String city) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
    }

    public static User copyOf(User user) {
        return new User(user.getUserId(), user.getFirstName(), user.getLastName(), user.getCity());
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}