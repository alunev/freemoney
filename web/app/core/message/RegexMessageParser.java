package core.message;

import model.MessagePattern;
import model.Sms;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;

/**
 * @author red
 * @since 0.0.1
 */
public class RegexMessageParser implements MessageParser {

    private final Set<MessagePattern> patterns;

    public RegexMessageParser(Set<MessagePattern> patterns) {
        this.patterns = patterns;
    }

    public void doParsing(Sms sms) {

    }

    @Override
    public String getSourceString() {
        return null;
    }

    @Override
    public String getDestString() {
        return null;
    }

    @Override
    public BigDecimal getAmount() {
        return null;
    }

    @Override
    public Currency getCurrency() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }
}
