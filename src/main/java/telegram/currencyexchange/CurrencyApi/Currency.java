package telegram.currencyexchange.CurrencyApi;

public class Currency {
    private String currency;
    private String buy;
    private String sell;

    public Currency(String currency, String buy, String sell) {
        this.currency = currency;
        this.buy = buy;
        this.sell = sell;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "currency='" + currency + '\'' +
                ", buy='" + buy + '\'' +
                ", sell='" + sell + '\'' +
                '}';
    }
}
