package dao.jongo;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jongo.Mapper;
import org.jongo.marshall.jackson.JacksonMapper;
import uk.co.panaxiom.playjongo.JongoMapperFactory;

/**
 * @author red
 * @since 0.0.1
 */
public class FmMapperFactory implements JongoMapperFactory {
    @Override
    public Mapper create() {
        return new JacksonMapper.Builder()
                .registerModule(new JavaTimeModule())
                .build();
    }
}
