# Käyttöohje
Ohjelma on kirjoitettu Ubuntu 20.04.3 LTS -käyttöjärjestelmään ja Java versiolla 17. Ohjelma toimii oletettavasti Debian-pohjaisilla Linux-jakeluilla. Ohjelmaa on lisäksi pintapuolisesti testattu Windows 11 käyttöjärjestelmällä johon on asennettu OpenJDK 17.0.5 LTS.

## Ohjelman käynnistäminen
Lataa repositorio koneellesi. Ohjelma käynnistetään komennolla
```
./gradlew run -q --console=plain
```
Vaihtoehtoisesti lataa ohjelman release-versio [tiralabra2022.jar](https://github.com/murmurian/tiralabra2022/releases/download/v1.0/tiralabra2022.jar) ja käynnistä ohjelma komennolla
```
java -jar tiralabra2022.jar
```
Oman .jar-tiedoston voi luoda komennolla
```
./gradlew build
```
Jar-tiedosto löytyy hakemistosta `build/libs`.

## Ohjelman käyttö
Ohjelma antaa käyttöohjeet englanniksi käynnistettäessä. Ohjeet voi tulostaa uudestaan komennolla `help`. Ohjelman voi lopettaa komennolla `quit`.

Ohjelmalle voi syöttää laskutoimituksia tai sijoittaa muuttujaan laskutoimituksen arvon. Hyväksytty muuttuja sisältää 1-3 kirjainta joita seuraa 0-4 numeroa. Sijoituksen voi tehdä yhtäsuuruusmerkillä esim. `x = 1 + 2`.

Ohjelma pyrkii olemaan käyttäjäystävällinen mm. välilyöntien ja sulkujenkäytön osalta. Voit siis käyttää välilyöntejä haluamallasi tavalla siten mikä tuntuu selkeimmältä. Esim. `x=1+2` on sama kuin `x = 1 + 2`. Funktiot ja negatiiviset arvot eivät edellytä sulkeiden käyttöä. Esim. `sin(1)` on sama kuin `sin 1` ja `1 - -x` on sama kuin `1 - (-x)`. Peräkkäiset funktiot ilman sulkeita tulkitaan yhdistetyiksi funktioiksi. Esim. `sin cos tan 1` on sama kuin `sin(cos(tan(1)))`.

Ohjelma tukee seuraavia laskutoimituksia:
- yhteen-, vähennys-, kerto- ja jakolaskua +, -, *, /
- potenssia ja neliöjuurta ^ ja sqrt
- trigonometrisia funktioita sin, cos ja tan
- kymmenkantaista ja luonnollista logaritmia log ja ln

Trigonometriset funktiot tukevat radiaaneja ja asteita. Oletusarvo on radiaanit. Voit vaihtaa asteiksi komennolla `deg` ja takaisin radiaaneiksi komennolla `rad`.

Ohjelman muistiin on valmiiksi tallennettu muuttujat `pi` ja `e`.

Laskin pyrkii antamaan mielekästä palautetta virheellisestä syötteestä. Esim. virhekohdat laskujärjestyksessä pyritään korostamaan punaisella.

## Testien suorittaminen
Testit voi suorittaa komennolla

```
./gradlew test
```
ja testikattavuusraportin saa luotua komennolla

```
./gradlew jacocoTestReport
```
Testikattavuusraportti löytyy tämän jälkeen tiedostosta build/reports/jacoco/test/html/index.html
