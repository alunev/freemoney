package controllers;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import dao.AccountDao;
import dao.UserDao;
import model.Account;
import model.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;
import views.html.accounts;
import views.html.edit_account;

import java.util.Optional;

public class AccountsController extends Controller {

    private final UserService userService;

    private final FormFactory formFactory;

    private final AccountDao accountDao;

    private final UserDao userDao;

    private final Config config;

    @Inject
    public AccountsController(UserService userService, FormFactory formFactory,
                              AccountDao accountDao, UserDao userDao, Config config) {
        this.userService = userService;
        this.formFactory = formFactory;
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.config = config;
    }

    public Result accounts() {
        return ok(accounts.render(userService.getUser(session()), config));
    }

    public Result showAddForm() {
        return showEditForm("");
    }

    public Result showEditForm(String accountId) {
        Account account = accountDao.findById(accountId);

        Form<Account> form = formFactory.form(Account.class);
        if (account != null) {
            form = form.fill(account);
        }

        return ok(edit_account.render(userService.getUser(session()), form, config));
    }

    public Result saveAccountForm() {
        Account account = formFactory.form(Account.class).bindFromRequest().get();
        Optional<User> user = userService.getUser(session());

        if (Strings.isNullOrEmpty(account.get_id())) {
            accountDao.save(account);

            user.ifPresent(u -> {
                u.addAccount(account);
                userDao.save(u);
            });
        } else {
            accountDao.save(account);
        }

        flash("success", "Saved successfully");

        return redirect(controllers.routes.AccountsController.showEditForm(account.get_id()));
    }

    public Result deleteAccount(String accountId) {
        Account account = accountDao.findById(accountId);

        accountDao.delete(account);

        return redirect(controllers.routes.AccountsController.accounts());
    }
}
