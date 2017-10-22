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
        Account acc2 = ObjectsFactory.createDummyAccount();
        User user = User.createUserWithAccounts("id1@mail.com", Sets.newHashSet(
                ObjectsFactory.createDummyAccount(),
                acc2,
                ObjectsFactory.createDummyAccount()
        ));

        assertThat("found account ?", user.getAccounts(), contains(acc2));
    }

    @Test
    public void removesAccountById() throws Exception {
        Account acc2 = ObjectsFactory.createDummyAccount();
        User user = User.createUserWithAccounts("id1@mail.com", Sets.newHashSet(
                ObjectsFactory.createDummyAccount(),
                acc2,
                ObjectsFactory.createDummyAccount()
        ));

        assertThat("found account ?", user.getAccounts(), hasItem(acc2));

        user.removeAccount(acc2);

        assertThat("found account ?", user.getAccounts(), not(hasItem(acc2)));

    }

    @Test
    public void sameIdCollapses() throws Exception {
        Account acc1 = ObjectsFactory.createDummyAccount();
        User user = User.createEmptyUser("id1", "id1@mail.com");

        user.addAccount(acc1);
        user.addAccount(acc1);
        user.addAccount(acc1);
        user.addAccount(acc1);

        assertThat(user.getAccounts(), hasSize(1));
    }

    @Test
    public void updateAccount() throws Exception {
        Account acc2 = ObjectsFactory.createDummyAccount();

        User user = User.createUserWithAccounts("id1@mail.com", Sets.newHashSet(
                ObjectsFactory.createDummyAccount(),
                acc2,
                ObjectsFactory.createDummyAccount()
        ));

        Account replacement = ObjectsFactory.createDummyAccountWithOwnerIdAndNumber("", "222new");

        user.addAccount(replacement);

        assertThat("found account ?", user.getAccounts(), contains(acc2));
    }

}