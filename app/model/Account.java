package model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "accounts", schema = "RedisK@redis_pu")
public class Account {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "number")
    private String number;
}
