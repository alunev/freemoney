/*
 * Copyright © 2014 Florian Hars, nMIT Solutions GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package auth.basic;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.password.DefaultUsernamePasswordAuthUser;
import com.feth.play.module.pa.providers.wwwauth.basic.BasicAuthProvider;
import com.feth.play.module.pa.user.AuthUser;
import play.inject.ApplicationLifecycle;
import play.mvc.Http.Context;
import play.twirl.api.Content;
import views.html.login;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * A really simple basic auth provider that accepts one hard coded user
 */
@Singleton
public class AlwaysValidBasicAuthProvider extends BasicAuthProvider {

    @Inject
    public AlwaysValidBasicAuthProvider(final PlayAuthenticate auth, final ApplicationLifecycle lifecycle) {
        super(auth, lifecycle);
    }

    @Override
    protected AuthUser authenticateUser(String username, String password) {
        return new DefaultUsernamePasswordAuthUser(password, username);
    }

    @Override
    public String getKey() {
        return "basic";
    }

    /**
     * Diplay the normal login form if HTTP authentication fails
     */
    @Override
    protected Content unauthorized(Context context) {
        return login.render(this.auth, null, "");
    }
}
