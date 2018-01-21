package core.message.parser;

import model.MessagePattern;
import model.Sms;
import model.TransactionType;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;

/**
 * @author red
 * @since 0.0.1
 */
public interface MessageParser {

    ParseResult parse(Sms sms, Collection<MessagePattern> patterns);

}
