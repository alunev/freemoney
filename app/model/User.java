package model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by red on 23.09.16.
 */

@Entity
@Table(name = "users", schema = "RedisK@redis_pu")
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "city")
    private String city;

    public static User copyOf(User user) {
        User user1 = new User();

        user1.setUserId(user.getUserId());
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());
        user1.setCity(user.getCity());

        return user1;
    }

    public static User createUser(String userId, String firstName, String lastName, String city) {
        User user = new User();

        user.userId = userId;
        user.firstName = firstName;
        user.lastName = lastName;
        user.city = city;

        return user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}