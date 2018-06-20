import org.assertj.core.util.Lists;
import play.Environment;
import play.Mode;
import play.http.HttpFilters;
import play.mvc.EssentialFilter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;

/**
 * This class configures filters that run on every request. This
 * class is queried by Play to get a list of filters.
 *
 * Play will automatically use filters from any class called
 * <code>Filters</code> that is placed the root package. You can load filters
 * from a different class by adding a `play.http.filters` setting to
 * the <code>application.conf</code> configuration file.
 */
@Singleton
public class Filters implements HttpFilters {

    private final Environment env;

    /**
     * @param env Basic environment settings for the current application.
     */
    @Inject
    public Filters(Environment env) {
        this.env = env;
    }

    @Override
    public List<EssentialFilter> getFilters() {
        if (env.mode().equals(Mode.DEV)) {
            return Lists.newArrayList();
        } else {
            return Collections.emptyList();
        }
    }

}
