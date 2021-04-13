import countries.Country;
import countries.CountryRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Scratch {

    /**
     * 01.
     * Visszaadja a leghosszabb országnév fordítást.
     */
    public static void main01(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll()
                .stream()
                .flatMap(country -> country.getTranslations().values().stream())
                .max(Comparator.comparing(String::length))
                .get();

        System.out.println(res);
    }

    /**
     * 02.
     * Visszaadja a leghosszabb olasz (azaz "it" nyelvkódú) országnév fordítást.
     */
    public static void main02(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll()
                .stream()
                .flatMap(country -> country.getTranslations().entrySet().stream()
                        .filter(t -> t.getKey().equals("it"))).map(Map.Entry::getValue)
                .max(Comparator.comparing(String::length))
                .get();

        System.out.println(res);
    }

    /**
     * 03.
     * A konzolra írja a leghosszabb országnév fordítást a nyelvkódjával együtt nyelvkód_=_fordítás formában.
     * Tipp: használd a Map.entrySet() metódust.
     */
    public static void main03(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll()
                .stream()
                .flatMap(country -> country.getTranslations().entrySet().stream())
                .max(Comparator.comparing(t -> t.getValue().length())).get();

        System.out.println(res);
    }

    /**
     * 04.
     * A konzolra írja az egyszavas (azaz szóköz karaktert nem tartalmazó) országneveket.
     */
    public static void main04(String[] args)  throws IOException{
        new CountryRepository().getAll()
                .stream()
                .map(Country::getName)
                .filter(name -> !name.contains(" "))
                .forEach(System.out::println);
    }

    /**
     * 05.
     * Visszaadja a legtöbb szóból áll országnevet. Tipp: használd a String.split() metódust.
     */
    public static void main05(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll()
                .stream()
                .map(Country::getName)
                .max(Comparator.comparing(name -> name.split(" ").length))
                .get();

        System.out.println(res);
    }

    /**
     * 06.
     * Visszaadja, hogy van-e legalább egy olyan főváros, melynek neve palindrom.
     * Tipp: használd a StringBuilder.reverse() metódust.
     */
    public static void main06(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll()
                .stream()
                .map(Country::getCapital)
                .anyMatch(name -> name.equals(new StringBuilder(name).reverse().toString()));

        System.out.println(res);
    }

    /**
     * 07.
     * Visszaadja a kisbetű-nagybetű érzéketlenül a legtöbb 'e' karaktert tartalmazó országnevet.
     * Tipp: deklarálj egy int charCount(String s, char c) segédmetódust, mely visszaadja az adott sztringben
     * az adott karakter előfordulásainak számát.
     */
    static int charCount(String s, char c){

        int count = 0;
        for (char character: s.toLowerCase().toCharArray()){
            if (character == c){
                count++;
            }
        }
        return count;
    }

    public static void main07(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll()
                .stream()
                .map(Country::getName)
                .max(Comparator.comparingInt(name -> charCount(name, 'e')))
                .get();

        System.out.println(res);
    }

    /**
     * 08.
     * Visszaadja a legtöbb angol magánhangzót (azaz 'a', 'e', 'i', 'o', 'u' karaktert) tartalmazó fővárost.
     * Tipp: deklarálj egy int vowelCount(String s) segédmetódust, mely visszaadja az adott sztringben
     * az angol magánhangzók számát.
     */
    static int vowelCount(String s){

        int count = 0;
        String vowels = "aeiou";
        for (char character: s.toLowerCase().toCharArray()){

            if (vowels.contains(Character.toString(character))){
                count++;
            }
        }
        return count;
    }

    public static void main08(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll()
                .stream()
                .map(Country::getCapital)
                .max(Comparator.comparingInt(Scratch::vowelCount))
                .get();

        System.out.println(res);
    }

    /**
     * 09.
     * Visszaad egy asszociatív tömböt, mely az országnevekben előforduló minden egyes karakterhez megadja
     * az előfordulások számát, ahol a kisbetű és nagybetű karakterek azonosnak tekintendők.
     * A visszatérési érték tehát Map<Character, Long> típusú kell, hogy legyen.
     */
    public static void main09(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll();

        final var res2 =  Stream.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z')
                .collect(Collectors
                        .toMap(character -> character,
                                character -> (long) charCount(res.stream()
                                                                    .map(Country::getName)
                                                                    .collect(Collectors.joining()), character)));
        System.out.println(res2);
    }

    /**
     * 10.
     * Visszaad egy asszociatív tömböt, mely minden egyes időzónához megadja az országok számát.
     * A visszatérési érték tehát Map<ZoneId, Long> típusú kell, hogy legyen.
     */
    public static void main10(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll();

        final var res2 = res
                .stream()
                .flatMap(country -> country.getTimezones().stream())
                .distinct()
                .collect(Collectors.toMap(timezone -> timezone,
                        timezone -> res.stream()
                                            .flatMap(country -> country.getTimezones().stream())
                                            .filter(timezone::equals)
                                            .count()));

        System.out.println(res2);
    }

    /**
     * 11.
     * Visszaadja földrészenként azoknak az országoknak a számát, melyek neve kisbetű-nagybetű érzéketlen módon
     * az országkódjukkal kezdődik. A visszatérési érték tehát Map<Region, Long> típusú kell, hogy legyen.
     */
    public static void main11(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll()
                .stream()
                .filter(country -> country.getCode().toLowerCase().equals(country.getName().toLowerCase().substring(0, 2)))
                .collect(Collectors.groupingBy(Country::getRegion, Collectors.counting()));

        System.out.println(res);
    }

    /**
     * 12.
     * Visszaad egy, az átlagosnál nagyobb vagy egyenlő népsűrűségű országok és az átlagosnál kisebb népsűrűségű
     * országok számát tartalmazó asszociatív tömböt.
     * A visszatérési érték tehát Map<Boolean, Long> típusú kell, hogy legyen.
     */
    public static void main12(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll();

        final var res2 = res.stream()
                .collect(Collectors.partitioningBy(country -> (double) country.getPopulation() >= res.stream()
                                                                                                        .mapToLong(Country::getPopulation)
                                                                                                        .average()
                                                                                                        .getAsDouble(), Collectors.counting()));

        System.out.println(res2);
    }

    /**
     * 13.
     * Visszaad egy asszociatív tömböt, mely minden egyes országkódhoz megadja az ország portugál (azaz "pt" nyelvkódú)
     * nevét. A visszatérési érték tehát Map<String, String> típusú kell, hogy legyen.
     * Tipp: használd a Collectors.toMap() metódust.
     */
    public static void main13(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll()
                .stream()
                .collect(Collectors.toMap(Country::getCode, country -> country.getTranslations().get("pt")));

        System.out.println(res);
    }

    /**
     * 14.
     * Visszaadja földrészenként azoknak a fővárosoknak a listáját, melyek neve megegyezik az országuk nevével.
     * A visszatérési érték tehát Map<Region, List<String>> típusú kell, hogy legyen.
     * Tipp: használd a Collectors.groupingBy(), Collectors.filtering() és Collectors.mapping() metódusokat.
     */
    public static void main14(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll()
                .stream()
                .filter(country -> country.getName().equals(country.getCapital()))
                .collect(Collectors.groupingBy(Country::getRegion, Collectors.mapping(Country::getCapital, Collectors.toList())));

        System.out.println(res);
    }

    /**
     * 15.
     * Visszaad egy, az országnév-népsűrűség párokból álló asszociatív tömböt.
     * A visszatérési érték tehát Map<Region, Double> típusú kell, hogy legyen.
     * Ha egy ország területe null, akkor a Double.NaN speciális érték legyen a népsűrűsége.
     */
    static double populationDensity(Country country){
        if (country.getArea() == null){
            return Double.NaN;
        }else{
            return BigDecimal.valueOf(country.getPopulation()).divide(country.getArea(), RoundingMode.FLOOR).doubleValue();
        }
    }

    public static void main15(String[] args)  throws IOException{
        final var res = new CountryRepository().getAll()
                .stream()
                .collect(Collectors.toMap(Country::getName, Scratch::populationDensity));

        System.out.println(res);
    }
}
