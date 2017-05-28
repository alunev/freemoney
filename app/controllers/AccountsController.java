package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import com.google.common.base.Strings;
import com.google.inject.Inject;
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

import java.util.UUID;

public class AccountsController extends Controller {

    private final PlayAuthenticate auth;

    private final UserService userService;

    private final FormFactory formFactory;

    private final AccountDao accountDao;
    private final UserDao userDao;

    @Inject
    public AccountsController(PlayAuthenticate auth, UserService userService, FormFactory formFactory,
                              AccountDao accountDao, UserDao userDao) {
        this.auth = auth;
        this.userService = userService;
        this.formFactory = formFactory;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    public Result accounts() {
        return ok(accounts.render(auth, userService.getUser(session())));
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

        return ok(edit_account.render(auth, userService.getUser(session()), form));
    }

    public Result saveAccountForm() {
        Account account = formFactory.form(Account.class).bindFromRequest().get();
        User user = userService.getUser(session());

        if (Strings.isNullOrEmpty(account.getId())) {
            accountDao.save(account);

            user.addAccount(account);
            userDao.save(user);
        } else {
            accountDao.save(account);
        }


        flash("success", "Saved successfully");

        return redirect(controllers.routes.AccountsController.showEditForm(account.getId()));
    }

    public Result deleteAccount(String accountId) {
        Account account = accountDao.findById(accountId);

        accountDao.delete(account);

        return redirect(controllers.routes.AccountsController.accounts());
    }
}
