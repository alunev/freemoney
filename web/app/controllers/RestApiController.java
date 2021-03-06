package controllers;

import auth.GoogleSignIn;
import com.google.inject.Inject;
import common.DateUtils;
import common.SessionParams;
import core.ParsingTransactionGenerator;
import core.TransactionExecutor;
import core.TransactionGenerator;
import dao.SmsDao;
import dao.TransactionDao;
import dao.UserDao;
import model.Sms;
import model.SmsBulk;
import model.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author red
 * @since 0.0.1
 */
public class RestApiController extends Controller {

    private final SmsDao smsDao;
    private final TransactionDao transactionDao;
    private final TransactionGenerator transactionGenerator;
    private final TransactionExecutor transactionExecutor;
    private final UserService userService;
    private final UserDao userDao;
    private final GoogleSignIn googleSignIn;

    @Inject
    public RestApiController(SmsDao smsDao,
                             TransactionDao transactionDao,
                             ParsingTransactionGenerator transactionGenerator,
                             TransactionExecutor transactionExecutor,
                             UserService userService, UserDao userDao, GoogleSignIn googleSignIn) {
        this.smsDao = smsDao;
        this.transactionDao = transactionDao;
        this.transactionGenerator = transactionGenerator;
        this.transactionExecutor = transactionExecutor;
        this.userService = userService;
        this.userDao = userDao;
        this.googleSignIn = googleSignIn;
    }

    public CompletionStage<Result> tokenSignIn() {
        Logger.info("Login attempt");

        Optional<String> idTokenString = Optional.ofNullable(request().body().asJson().asText());
        Optional<User> user;

        if (!idTokenString.isPresent()) {
            return CompletableFuture.completedStage(badRequest("No idtoken found"));
        }

        try {
            user = googleSignIn.processSignInToken(idTokenString.get());
        } catch (GeneralSecurityException | IOException e) {
            return CompletableFuture.completedStage(internalServerError("Failed to login", e.toString()));
        }

        if (user.isPresent()) {
            session(SessionParams.USER_AUTH_ID, user.get().getAuthId());
            return CompletableFuture.completedStage(ok(Json.toJson(user.get().get_id())));
        } else {
            return CompletableFuture.completedStage(unauthorized("Failed to login"));
        }
    }

    public CompletionStage<Result> currentUser() {
        return CompletableFuture.supplyAsync(() -> userService.getUser(session()))
                .thenApplyAsync(user -> {
                    if (user.isPresent()) {
                        return ok(Json.toJson(user));
                    } else {
                        return notFound();
                    }
                });
    }

    public CompletionStage<Result> processSms() {
        Sms sms = Json.fromJson(request().body().asJson(), Sms.class);

        User user = userDao.findById(sms.getOwnerId());
        if (user == null) {
            return CompletableFuture.supplyAsync(() -> internalServerError("No user found with ID " + sms.getOwnerId()));
        }

        return supplyAsyncSmsProcess(sms);
    }

    public CompletionStage<Result> processSmsBulk() throws IOException {
        SmsBulk bulk = Json.fromJson(request().body().asJson(), SmsBulk.class);

        if (bulk.getList().isEmpty()) {
            return CompletableFuture.completedStage(badRequest("Empty sms bulk"));
        }

        String userId = bulk.getUserId();
        User user = userDao.findById(userId);
        if (user == null) {
            return CompletableFuture.supplyAsync(() -> internalServerError("No user found with ID " + userId));
        }

        userDao.updateInstanceLastSyncTs(
                user,
                bulk.getDeviceId(),
                DateUtils.millisToZdt(bulk.getCreatedTs())
        );

        return bulk.getList().parallelStream()
                .map(this::supplyAsyncSmsProcess)
                .reduce((future1, future2) -> future1.thenCombineAsync(
                        future2,
                        (result1, result2) -> {
                            if (result1.status() == ok().status() && result2.status() == ok().status()) {
                                return ok();
                            } else {
                                return internalServerError("Some sms process failed");
                            }
                        }))
                .orElse(CompletableFuture.completedFuture(badRequest("Empty sms bulk")));
    }

    private CompletableFuture<Result> supplyAsyncSmsProcess(Sms sms) {
        return CompletableFuture.supplyAsync(() -> smsDao.save(sms))
                .thenApplyAsync(s -> transactionGenerator.generate(s, userDao.findById(sms.getOwnerId())))
                .thenApplyAsync(transactionDao::saveAll)
                .thenAcceptAsync(transactions -> transactions.forEach(
                        transactionExecutor::execute
                ))
                .thenApplyAsync(aVoid -> ok());
    }

    public CompletionStage<Result> getLastSync(String instanceId) {
        Optional<User> user = userService.getUser(session());

        return CompletableFuture.supplyAsync(() ->
                user.map(
                        user1 -> Optional.ofNullable(user1.getAppInstances()).stream()
                                .flatMap(Collection::stream)
                                .filter(appInstance -> Objects.equals(appInstance.getInstanceId(), instanceId))
                                .findFirst()
                                .map(appInstance -> appInstance.getLastSync().toInstant().toEpochMilli())
                                .map(millis -> ok(String.valueOf(millis)))
                                .orElse(ok(String.valueOf(DateUtils.startOfTime())))
                )
                        .orElseGet(() -> unauthorized("Login first")));
    }
}