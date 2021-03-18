package telegram.currencyexchange.CurrencyApi;

import telegram.currencyexchange.bot.Bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankFromMinfin {
    private String parsedSite;
    private Bank bank;

    public BankFromMinfin(Bank bank) {
        this.bank = bank;
        parsedSite = parseSite();
    }

    private String parseSite() {
        String url = "https://minfin.com.ua/ua/company/" + bank.getNameForLink();
        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0"); // add this line to your code


            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("<tbody")) {
                    inputLine = in.readLine();
                    while (!inputLine.contains("</tbody>")) {
                        sb.append(inputLine);
                        inputLine = in.readLine();
                    }
                    return sb.toString();
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "s";
    }

    private Map<String, Currency> getCurrenciesFromSite(String str) {
        Map<String, Currency> currencies = new HashMap<>();
        Pattern pattern = Pattern.compile("<tr>(.*?)</tr>");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String row = matcher.group();
            Pattern p = Pattern.compile("<td>(.*)</td><td>(.*)</td><td>(.*)</td>");
            Matcher m = p.matcher(row);
            while (m.find()) {
                Currency currency = new Currency(m.group(1), m.group(2), m.group(3));
                currencies.put(m.group(1), currency);
            }
        }
        return currencies;
    }

    public Map<String, Currency> getCurrencies() {
        return getCurrenciesFromSite(parsedSite);
    }

    public Currency getCurrency(String currency) {
        return getCurrencies().get(currency);
    }
}


//    Pattern pattern = Pattern.compile("<tr><td>(.*)</td><td>(.*)</td><td>(.*)</td></tr>");
//    Matcher matcher = pattern.matcher(inputLine);
//                        while (matcher.find()) {
//                                System.out.println(matcher.group(1) + " " + matcher.group(2) + " " + matcher.group(3));
//                                }