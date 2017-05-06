package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import dao.AccountDao;
import model.Account;
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

    @Inject
    public AccountsController(PlayAuthenticate auth, UserService userService, FormFactory formFactory, AccountDao accountDao) {
        this.auth = auth;
        this.userService = userService;
        this.formFactory = formFactory;
        this.accountDao = accountDao;
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

        if (Strings.isNullOrEmpty(account.getId())) {
            account.setId(UUID.nameUUIDFromBytes(account.getNumber().getBytes()).toString());
            account.setOwnerId(userService.getUser(session()).getUserId());
        }

        accountDao.save(account);

        flash("success", "Saved successfully");

        return redirect(controllers.routes.AccountsController.showEditForm(account.getId()));
    }
}
