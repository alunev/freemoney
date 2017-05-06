package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import dao.AccountDao;
import dao.TransactionCategoryDao;
import dao.TransactionDao;
import dao.UserDao;
import model.*;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;
import views.html.edit_transaction;
import views.html.transactions;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class TransactionsController extends Controller {

    private final PlayAuthenticate auth;

    private final UserService userService;

    private final TransactionDao transactionDao;

    private final TransactionCategoryDao transactionCategoryDao;

    private final UserDao userDao;

    private final AccountDao accountDao;

    private final FormFactory formFactory;

    @Inject
    public TransactionsController(PlayAuthenticate auth, UserService userService, TransactionDao transactionDao,
                                  TransactionCategoryDao transactionCategoryDao, UserDao userDao, AccountDao accountDao,
                                  FormFactory formFactory) {
        this.auth = auth;
        this.userService = userService;
        this.transactionDao = transactionDao;
        this.transactionCategoryDao = transactionCategoryDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.formFactory = formFactory;
    }

    public Result transactions() {
        return ok(transactions.render(auth, userService.getUser(session())));
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
                Collectors.toMap(TransactionCategory::getCategoryId, TransactionCategory::getName)
        );

        User user = userService.getUser(session());

        Map<String, String> accountsMap = accountDao.findByOwnerId(user.getUserId()).stream().collect(
                Collectors.toMap(Account::getId, Account::getTitle)
        );

        return ok(edit_transaction.render(auth, user, form, catMap, accountsMap));
    }

    public Result saveTransactionForm() {
        Transaction tx = formFactory.form(Transaction.class).bindFromRequest().get();

        if (Strings.isNullOrEmpty(tx.getTransactionId())) {
            tx.setTransactionId(UUID.nameUUIDFromBytes(tx.toString().getBytes()).toString());

            if (tx.getTransactionType() == TransactionType.EXPENSE) {
                tx.setDestAccount(Account.EXPENSE_ACCOUNT);
            } else if (tx.getTransactionType() == TransactionType.INCOME) {
                tx.setDestAccount(Account.INCOME_ACCOUNT);
            }

            transactionDao.updateTransientFields(tx);

            User user = userService.getUser(session());
            user.addTransaction(tx);

            userDao.save(user);
        } else {
            transactionDao.save(tx);
        }

        flash("success", "Saved successfully");

        return redirect(controllers.routes.TransactionsController.showEditForm(tx.getTransactionId()));
    }

    public Result deleteTransaction(String txId) {
        Transaction transaction = transactionDao.findById(txId);

        transactionDao.delete(transaction);

        return redirect(controllers.routes.TransactionsController.transactions());
    }
}
