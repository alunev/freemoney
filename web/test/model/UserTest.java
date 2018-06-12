package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import common.DateUtils;
import dao.ObjectsFactory;
import org.junit.Test;
import play.libs.Json;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;


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

        assertThat(user.getAccounts()).contains(acc2);
    }

    @Test
    public void removesAccountById() throws Exception {
        Account acc2 = ObjectsFactory.createDummyAccount();
        User user = User.createUserWithAccounts("id1@mail.com", Sets.newHashSet(
                ObjectsFactory.createDummyAccount(),
                acc2,
                ObjectsFactory.createDummyAccount()
        ));

        assertThat(user.getAccounts()).contains(acc2);

        user.removeAccount(acc2);

        assertThat(user.getAccounts()).doesNotContain(acc2);

    }

    @Test(expected = IllegalArgumentException.class)
    public void duplicateIdThrowsException() throws Exception {
        Account acc1 = ObjectsFactory.createDummyAccount();
        User user = User.createEmptyUser("id1", "id1@mail.com");

        user.addAccount(acc1);
        user.addAccount(acc1);
        user.addAccount(acc1);
        user.addAccount(acc1);

        assertThat(user.getAccounts()).hasSize(1);
    }

    @Test
    public void updateAccount() throws Exception {
        Account acc2 = ObjectsFactory.createDummyAccountWithId("1234");

        User user = User.createUserWithAccounts("id1@mail.com", Sets.newHashSet(
                ObjectsFactory.createDummyAccount(),
                acc2,
                ObjectsFactory.createDummyAccountWithId("1111")
        ));

        Account replacement = ObjectsFactory.createDummyAccountWithId("1234");

        user.removeAccount(acc2);
        assertThat(user.getAccounts()).doesNotContain(acc2);

        user.addAccount(replacement);
        assertThat(user.getAccounts()).contains(replacement);
    }

    @Test
    public void createJson() {
        Account acc1 = ObjectsFactory.createDummyAccountWithId("1");
        Account acc2 = ObjectsFactory.createDummyAccountWithId("1");

        User user = new UserBuilder().withEmail("id1@mail.com")
                .withAccounts(Sets.newHashSet(
                        acc1,
                        acc2
                ))
                .withTransactions(Set.of(
                        new Transaction(
                                "1",
                                "test_owner",
                                TransactionType.EXPENSE,
                                BigDecimal.ONE,
                                BigDecimal.ZERO,
                                acc1.getId(),
                                Account.EXPENSE_ACCOUNT.getId(),
                                TransactionCategory.UNDEFINED.getId(),
                                DateUtils.now()
                        ),
                        new Transaction(
                                "2",
                                "test_owner",
                                TransactionType.EXPENSE,
                                BigDecimal.ONE,
                                BigDecimal.ZERO,
                                acc1.getId(),
                                Account.EXPENSE_ACCOUNT.getId(),
                                TransactionCategory.UNDEFINED.getId(),
                                DateUtils.now()
                        )))
                .withAppInstances(Lists.newArrayList(
                        new AppInstance("abcd", ZonedDateTime.now()),
                        new AppInstance("1234", ZonedDateTime.now())
                ))
                .build();

        JsonNode json = Json.toJson(user);

        assertThat(json).isNotNull();

        User fromJson = Json.fromJson(json, User.class);

        assertThat(fromJson).isNotNull();
        assertThat(user).isEqualTo(fromJson);
    }
}