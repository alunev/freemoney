package model;

import com.google.common.collect.Lists;
import dao.ObjectsFactory;
import org.junit.Test;

import javax.naming.spi.ObjectFactory;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.*;

/**
 * @author red
 * @since 0.0.1
 */
public class UserTest {
    @Test
    public void getAccountById() throws Exception {
        Account acc2 = ObjectsFactory.createDummyAccountWithId("acc2");
        User user = User.createUser("id1", "id1@mail.com", Lists.newArrayList(
                ObjectsFactory.createDummyAccountWithId("acc1"),
                acc2,
                ObjectsFactory.createDummyAccountWithId("acc3")
        ));

        Optional<Account> foundAccount = user.getAccountById("acc2");
        assertThat("found account ?", foundAccount.isPresent(), is(true));
        assertThat("acc2 account", foundAccount.get(), is(acc2));
    }

    @Test
    public void removesAccountById() throws Exception {
        Account acc2 = ObjectsFactory.createDummyAccountWithId("acc2");
        User user = User.createUser("id1", "id1@mail.com", Lists.newArrayList(
                ObjectsFactory.createDummyAccountWithId("acc1"),
                acc2,
                ObjectsFactory.createDummyAccountWithId("acc3")
        ));

        assertThat("found account ?", user.getAccounts(), hasItem(acc2));

        user.removeAccount(acc2);

        assertThat("found account ?", user.getAccounts(), not(hasItem(acc2)));

    }

}