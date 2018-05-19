package core.message;

import com.google.common.collect.Lists;
import common.DateUtils;
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
        Sms sms = new Sms("01",
                "android-01",
                "12345",
                "Покупка. Карта *2222. 3344.5 RUB. OKEY. Доступно 12345.92 RUB",
                DateUtils.now()
        );

        RegexBankMatcher bankMatcher = new RegexBankMatcher(Lists.newArrayList(
                new MessagePattern(
                        "01",
                        "Покупка\\. Карта \\*(\\d{4}). (\\d+\\.\\d+) RUB. OKEY. Доступно (\\d+\\.\\d+) RUB",
                        "bank1",
                        DateUtils.now()
                ),
                new MessagePattern(
                        "01",
                        "Покупка\\. LENTA. Карта \\*(\\d{4}). (\\d+\\.\\d+) RUB.",
                        "bank2",
                        DateUtils.now()
                ),
                new MessagePattern(
                        "01",
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