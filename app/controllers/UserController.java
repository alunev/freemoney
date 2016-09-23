package controllers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.User;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;

public class UserController extends Controller
{
    static EntityManagerFactory emf;

    public Result persist()
    {
        EntityManager em = getEmf().createEntityManager();

        User user = new User("0001", "John", "Smith", "London");

        em.persist(user);
        em.close();

        return ok("User 0001 record persisted for persistence unit cassandra_pu");
    }

    public Result find()
    {
        EntityManager em = getEmf().createEntityManager();
        User user = em.find(User.class, "0001");
        em.close();
        return ok("Found User in database with the following details:" + printUser(user));
    }

    public Result update()
    {
        EntityManager em = getEmf().createEntityManager();
        User user = em.find(User.class, "0001");

        User updatedUser = User.copyOf(user);
        updatedUser.setCity("New York");

        em.merge(user);
        user = em.find(User.class, "0001");

        return ok("Record updated:" + printUser(user));
    }

    public Result delete()
    {
        EntityManager em = getEmf().createEntityManager();
        User user = em.find(User.class, "0001");

        em.remove(user);
        user = em.find(User.class, "0001");
        return ok("Record deleted:" + printUser(user));
    }

    private static EntityManagerFactory getEmf()
    {
        if (emf == null)
        {
            emf = Persistence.createEntityManagerFactory("cassandra_pu");
        }
        return emf;
    }

    private static String printUser(User user)
    {
        if (user == null)
            return "Record not found";

        return "\n--------------------------------------------------" + "\nuserId:" + user.getUserId() + "\nfirstName:"
                + user.getFirstName() + "\nlastName:" + user.getLastName() + "\ncity:" + user.getCity();
    }
}