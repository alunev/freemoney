package core.message;

import com.google.common.collect.Sets;
import common.DateUtils;
import core.message.parser.RegexMessageParser;
import model.MessagePattern;
import model.Sms;
import org.junit.Test;

import static model.TransactionType.EXPENSE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author red
 * @since 0.0.1
 */
public class RegexMessageParserTest {

    @Test
    public void canParseExpense() {
        Sms sms = new Sms("01",
                "android-01",
                "12345",
                "Покупка. Карта *2222. 3344.5 RUB. OKEY. Доступно 12345.92 RUB",
                DateUtils.nowTs());

        MessagePattern pattern = new MessagePattern(
                "01",
                "Покупка\\. Карта \\*(\\d{4}). (\\d+\\.\\d+) RUB. OKEY. Доступно (\\d+\\.\\d+) RUB",
                "bank1",
                DateUtils.now());


        RegexMessageParser parser = new RegexMessageParser();

        parser.parse(sms, Sets.newHashSet(pattern));
    }

}