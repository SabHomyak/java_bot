package telegram.currencyexchange.bot;

public enum Bank {
    PRIVAT24("ПриватБанк","privatbank"),
    OSCHADBANK("Ощад Банк","oschadbank"),
    ALFABANK("Альфа-банк","alfa-bank"),
    OTPBANK("ОТП банк","otp-bank"),
    UKRSIBANK("Укрсиббанк","ukrsibbank"),
    RAYFFAYZENBANK("Райффайзен Банк","aval");

    private String name;
    private String nameForLink;

    Bank(String name,String nameForLink) {
        this.name = name;
        this.nameForLink = nameForLink;
    }

    public String getNameForLink() {
        return nameForLink;
    }

    public String getName() {
        return name;
    }
}
