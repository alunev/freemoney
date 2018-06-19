package controllers;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import dao.AccountDao;
import dao.TransactionCategoryDao;
import dao.TransactionDao;
import dao.UserDao;
import model.Account;
import model.Transaction;
import model.TransactionCategory;
import model.TransactionType;
import model.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;
import views.html.edit_transaction;
import views.html.transactions;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class TransactionsController extends Controller {

    private final UserService userService;

    private final TransactionDao transactionDao;

    private final TransactionCategoryDao transactionCategoryDao;

    private final UserDao userDao;

    private final AccountDao accountDao;

    private final FormFactory formFactory;

    private final Config config;

    @Inject
    public TransactionsController(UserService userService, TransactionDao transactionDao,
                                  TransactionCategoryDao transactionCategoryDao, UserDao userDao, AccountDao accountDao,
                                  FormFactory formFactory, Config config) {
        this.userService = userService;
        this.transactionDao = transactionDao;
        this.transactionCategoryDao = transactionCategoryDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.formFactory = formFactory;
        this.config = config;
    }

    public Result transactions() {
        return ok(transactions.render(userService.getUser(session()), config));
    }


    public Result showAddForm() {
        return showEditForm("");
    }

    public Result showEditForm(String txId) {
        Transaction transaction = transactionDao.findById(txId);

        Form<Transaction> form = formFactory.form(Transaction.class);
        if (transaction != null) {
            form = form.fill(transaction);
        }

        Map<String, String> catMap = transactionCategoryDao.findAll().stream().collect(
                Collectors.toMap(TransactionCategory::getId, TransactionCategory::getName)
        );

        Optional<User> user = userService.getUser(session());

        Map<String, String> accountsMap = user
                .map(u -> accountDao.findByOwnerId(u.getId()).stream())
                .map(accountStream -> accountStream.collect(Collectors.toMap(Account::getId, Account::getTitle)))
                .orElse(Collections.emptyMap());

        return ok(edit_transaction.render(user, form, catMap, accountsMap, config));
    }

    public Result saveTransactionForm() {
        Transaction tx = formFactory.form(Transaction.class).bindFromRequest().get();

        if (Strings.isNullOrEmpty(tx.getId())) {
            tx.setId(UUID.nameUUIDFromBytes(tx.toString().getBytes()).toString());

            if (tx.getTransactionType() == TransactionType.EXPENSE) {
                tx.setDestAccount(Account.EXPENSE_ACCOUNT);
            } else if (tx.getTransactionType() == TransactionType.INCOME) {
                tx.setDestAccount(Account.INCOME_ACCOUNT);
            }

            Optional<User> user = userService.getUser(session());

            user.ifPresent(u -> {
                u.addTransaction(tx);
                userDao.save(u);
            });

        } else {
            transactionDao.save(tx);
        }

        flash("success", "Saved successfully");

        return redirect(controllers.routes.TransactionsController.showEditForm(tx.getId()));
    }

    public Result deleteTransaction(String txId) {
        Transaction transaction = transactionDao.findById(txId);

        transactionDao.delete(transaction);

        return redirect(controllers.routes.TransactionsController.transactions());
    }
}
