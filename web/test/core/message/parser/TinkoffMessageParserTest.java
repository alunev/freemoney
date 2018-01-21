package core.message.parser;

import com.google.common.collect.Lists;
import common.DateUtils;
import model.MessagePattern;
import model.Sms;
import org.junit.Test;

import static model.TransactionType.EXPENSE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author red
 * @since 0.0.1
 */
public class TinkoffMessageParserTest {

    @Test
    public void parse() {
        Sms sms = new Sms("01",
                "android-01",
                "12345",
                "Покупка. Карта *2222. 3344.5 RUB. OKEY. Доступно 12345.92 RUB",
                DateUtils.now());

        MessagePattern pattern = new MessagePattern(
                "01",
                "(.*)\\. Карта \\*(\\d{4}). (\\d+\\.\\d+) (.*). OKEY. Доступно (\\d+\\.\\d+) RUB",
                "bank1",
                DateUtils.now());


        TinkoffMessageParser parser = new TinkoffMessageParser();
        ParseResult result = parser.parse(sms, Lists.newArrayList(pattern));

        assertThat(result.getTransactionType()).isEqualTo(EXPENSE);
    }
}