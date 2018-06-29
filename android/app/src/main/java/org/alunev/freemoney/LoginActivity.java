package org.alunev.freemoney;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.alunev.freemoney.client.RestService;
import org.alunev.freemoney.client.RestServiceFactory;
import org.alunev.freemoney.client.SmsSyncer;
import org.alunev.freemoney.device.SmsReader;
import org.alunev.freemoney.model.Sms;
import org.alunev.freemoney.utils.Preferences;
import org.alunev.freemoney.service.SmsUploadJobService;
import org.alunev.freemoney.utils.Utils;
import org.alunev.freemoney.views.smslist.SmsListFragment;
import org.alunev.freemoney.views.smslist.SmsListRecyclerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements SmsListFragment.OnListFragmentInteractionListener {
    private static final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 1234;

    private RestService restService;

    private View mProgressView;
    private GoogleSignInClient signInClient;
    private SharedPreferences preferences;

    private ExecutorService smsSyncExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.sign_in_button).setOnClickListener(this::onClick);
        findViewById(R.id.sign_out_button).setOnClickListener(this::onClick);
        findViewById(R.id.run_sms_sync).setOnClickListener(this::onClick);


        mProgressView = findViewById(R.id.login_progress);

        restService = new RestServiceFactory(getApplicationContext()).createService();

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        scheduleSmsUpload();

        initGooggleSignIn();
    }

    @Override
    protected void onStart() {
        super.onStart();

        signInClient.silentSignIn()
                .addOnCompleteListener(this, this::handleSignInResult);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        TextView currentUser = (TextView) findViewById(R.id.currentUser);
        View signInButton = findViewById(R.id.sign_in_button);
        View signOutButton = findViewById(R.id.sign_out_button);
        View runSyncButton = findViewById(R.id.run_sms_sync);
        RecyclerView smsList = (RecyclerView) findViewById(R.id.sms_list_fragment);

        String userId = Utils.getUserId(getApplicationContext());
        if (account != null) {
            Log.i(TAG, "Logged in: " + account.getDisplayName());
            currentUser.setText("Logged in as: " + account.getDisplayName());
            currentUser.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.VISIBLE);
            runSyncButton.setVisibility(View.VISIBLE);

            smsList.setAdapter(new SmsListRecyclerAdapter(new SmsReader(getApplicationContext()).readAllSMS(), this));
            smsList.getAdapter().notifyDataSetChanged();

            signInButton.setVisibility(View.GONE);
        } else {
            Log.i(TAG, "Logged out");
            currentUser.setText("");
            currentUser.setVisibility(View.GONE);
            signOutButton.setVisibility(View.GONE);
            runSyncButton.setVisibility(View.GONE);

            smsList.setAdapter(new SmsListRecyclerAdapter(new SmsReader(getApplicationContext()).readAllSMS(), this));
            smsList.getAdapter().notifyDataSetChanged();

            signInButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void scheduleSmsUpload() {
        Context context = getApplicationContext();
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("some_key", "some_value");

        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(SmsUploadJobService.class)
                // uniquely identifies the job
                .setTag("org.alunev.freemoney.service.SmsUploadJobService")
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(1, 10))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints()
                .setExtras(myExtrasBundle)
                .build();

        dispatcher.mustSchedule(myJob);
    }

    private void initGooggleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .requestProfile()
                .build();


        signInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        signInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    preferences.edit()
                            .remove(Preferences.USER_AUTH_ID)
                            .apply();

                    preferences.edit()
                            .remove(Preferences.USER_ID)
                            .apply();

                    updateUI(null);
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            preferences.edit()
                    .putString(Preferences.USER_AUTH_ID, account.getId())
                    .apply();

            restService.tokenSignIn(idToken).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String userId = response.body();

                    Log.i(TAG, "Logged in to backend userId = " + userId);

                    preferences.edit()
                            .putString(Preferences.USER_ID, userId)
                            .apply();


                    // Signed in successfully, show authenticated UI.
                    updateUI(account);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "Failed to log in to backend", t);
                }
            });
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode(), e);
            updateUI(null);
        }
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.run_sms_sync:
                runSmsSync();
                break;
            // ...
        }
    }

    private void runSmsSync() {
        smsSyncExecutor.submit(() -> {
            new SmsSyncer(getApplicationContext()).runSync();
        });
    }

    @Override
    public void onListFragmentInteraction(Sms item) {
        Log.i(TAG, "from fragment: " + item);
    }
}

