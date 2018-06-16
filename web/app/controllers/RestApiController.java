package controllers;

import com.google.inject.Inject;
import core.ParsingTransactionGenerator;
import core.TransactionExecutor;
import core.TransactionGenerator;
import dao.SmsDao;
import dao.TransactionDao;
import dao.UserDao;
import model.Sms;
import model.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

import java.util.Objects;
import java.util.Optional;
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
    private UserDao userDao;

    @Inject
    public RestApiController(SmsDao smsDao,
                             TransactionDao transactionDao,
                             ParsingTransactionGenerator transactionGenerator,
                             TransactionExecutor transactionExecutor,
                             UserService userService, UserDao userDao) {
        this.smsDao = smsDao;
        this.transactionDao = transactionDao;
        this.transactionGenerator = transactionGenerator;
        this.transactionExecutor = transactionExecutor;
        this.userService = userService;
        this.userDao = userDao;
    }

    public CompletionStage<Result> processSms() {
        Sms sms = Json.fromJson(request().body().asJson(), Sms.class);

        User user = userDao.findById(sms.getOwnerId());

        if (user == null) {
            return CompletableFuture.supplyAsync(() -> internalServerError("No user found with ID " + sms.getOwnerId()));
        }

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

    public CompletionStage<Result> getLastSync(String instanceId) {
        Optional<User> user = userService.getUser();

        return CompletableFuture.supplyAsync(() ->
                user.map(
                        user1 -> user1.getAppInstances().stream()
                                .filter(appInstance -> Objects.equals(appInstance.getInstanceId(), instanceId))
                                .findFirst()
                                .map(appInstance -> appInstance.getLastSync().toInstant().toEpochMilli())
                                .map(millis -> ok("" + millis))
                                .orElse(badRequest("No matching device found"))
                )
                        .orElseGet(() -> unauthorized("Login first")));
    }
}
