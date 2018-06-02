package core.message;

import com.google.common.collect.Lists;
import common.DateUtils;
import dao.ObjectsFactory;
import model.MessagePattern;
import model.Sms;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/**
 * @author red
 * @since 0.0.1
 */
public class RegexBankMatcherTest {

    @Test
    public void canMatchBank() {
        Sms sms = ObjectsFactory.sampleTfSms();

        RegexBankMatcher bankMatcher = new RegexBankMatcher(Lists.newArrayList(
                new MessagePattern(
                        "user01",
                        "(.*)\\. Карта \\*(\\d{4}). (\\d+\\.\\d+) (.*). OKEY. Доступно (\\d+\\.\\d+) RUB",
                        "bank1",
                        DateUtils.now()
                ),
                new MessagePattern(
                        "user01",
                        "Покупка\\. LENTA. Карта \\*(\\d{4}). (\\d+\\.\\d+) RUB.",
                        "bank2",
                        DateUtils.now()
                ),
                new MessagePattern(
                        "user01",
                        "xxxxxxxxxxxxxxxxx",
                        "bank3",
                        DateUtils.now()
                )
        ));

        Optional<String> bestMatch = bankMatcher.getBestMatch(sms);

        assertThat(bestMatch).isPresent();
        assertThat(bestMatch.get()).isEqualTo("bank1");
    }

}