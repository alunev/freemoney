package controllers;

import com.google.inject.Inject;
import core.ParsingTransactionGenerator;
import core.TransactionExecutor;
import core.TransactionGenerator;
import dao.SmsDao;
import dao.TransactionDao;
import model.Sms;
import model.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author red
 * @since 0.0.1
 */
public class RestApiController extends Controller {

    private SmsDao smsDao;

    private TransactionDao transactionDao;

    private TransactionGenerator transactionGenerator;
    private TransactionExecutor transactionExecutor;
    private UserService userService;

    @Inject
    public RestApiController(SmsDao smsDao,
                             TransactionDao transactionDao,
                             ParsingTransactionGenerator transactionGenerator,
                             TransactionExecutor transactionExecutor,
                             UserService userService) {
        this.smsDao = smsDao;
        this.transactionDao = transactionDao;
        this.transactionGenerator = transactionGenerator;
        this.transactionExecutor = transactionExecutor;
        this.userService = userService;
    }

    public CompletionStage<Result> processSms() {
        User user = userService.getUser(session());
        Sms sms = Json.fromJson(request().body().asJson(), Sms.class);

        // push message to persistent queue and then async:
        //  - saving to mongo (for history and traceability)
        //  - generating of transactions based on sms data
        //  - execute transactions on our side: update account balances accordingly
        //  - (?) push data change to UI

        return CompletableFuture.supplyAsync(() -> smsDao.save(sms))
                .thenApplyAsync(s -> transactionGenerator.generate(s, user))
                .thenApplyAsync(transactions -> transactionDao.saveAll(transactions))
                .thenAcceptAsync(transactions -> transactions.forEach(
                        transaction -> transactionExecutor.execute(transaction)
                ))
                .thenApplyAsync(aVoid -> ok());
    }
}
