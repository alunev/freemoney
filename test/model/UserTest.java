package model;

import com.google.common.collect.Sets;
import dao.ObjectsFactory;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

/**
 * @author red
 * @since 0.0.1
 */
public class UserTest {
    @Test
    public void getAccountById() throws Exception {
        Account acc2 = ObjectsFactory.createDummyAccountWithId("acc2");
        User user = User.createUserWithAccounts("id1", "id1@mail.com", Sets.newHashSet(
                ObjectsFactory.createDummyAccountWithId("acc1"),
                acc2,
                ObjectsFactory.createDummyAccountWithId("acc3")
        ));

        assertThat("found account ?", user.getAccounts(), contains(acc2));
    }

    @Test
    public void removesAccountById() throws Exception {
        Account acc2 = ObjectsFactory.createDummyAccountWithId("acc2");
        User user = User.createUserWithAccounts("id1", "id1@mail.com", Sets.newHashSet(
                ObjectsFactory.createDummyAccountWithId("acc1"),
                acc2,
                ObjectsFactory.createDummyAccountWithId("acc3")
        ));

        assertThat("found account ?", user.getAccounts(), hasItem(acc2));

        user.removeAccount(acc2);

        assertThat("found account ?", user.getAccounts(), not(hasItem(acc2)));

    }

    @Test
    public void sameIdCollapses() throws Exception {
        Account acc1 = ObjectsFactory.createDummyAccountWithId("acc1");
        User user = User.createEmptyUser("id1", "id1@mail.com");

        user.addAccount(acc1);
        user.addAccount(acc1);
        user.addAccount(acc1);
        user.addAccount(acc1);

        assertThat(user.getAccounts(), hasSize(1));
    }

    @Test
    public void updateAccount() throws Exception {
        Account acc2 = ObjectsFactory.createDummyAccountWithId("acc2");

        User user = User.createUserWithAccounts("id1", "id1@mail.com", Sets.newHashSet(
                ObjectsFactory.createDummyAccountWithId("acc1"),
                acc2,
                ObjectsFactory.createDummyAccountWithId("acc3")
        ));

        Account replacement = ObjectsFactory.createDummyAccountWithId("acc2");
        replacement.setNumber("222new");
        user.addAccount(replacement);

        assertThat("found account ?", user.getAccounts(), contains(acc2));
    }

}