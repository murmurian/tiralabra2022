# Toteutusdokumentti

## Ohjelman yleisrakenne
Ohjelma laskee shunting-yard algoritmiin pohjautuen käyttäjän merkkijonona antaman matemaattisen lausekkeen arvon. Laskin tukee myös muuttujia joihin voi tallettaa lausekkeiden arvoja. Virheellisistä syötteistä ohjelma pyrkii antamaan käyttäjälle mielekästä palautetta.

Laskin tukee tällä hetkellä:
- yhteen-, vähennys-, kerto- ja jakolaskua
- potenssia ja neliöjuurta
- trigonometrisia funktioita sin, cos ja tan
- kymmenkantaista ja luonnollista logaritmia log ja ln

Trigonometristen funktioden osalta laskin tarjoaa vaihtoehdon käyttää joko radiaaneja tai asteita. Sulkujen käytön osalta laskin pyrkii olemaan mahdollisimman käyttäjäystävällinen. Näin ollen esim. lausekkeet `sin 90` ja `sin(90)` tulkitaan samalla tavalla, lauseke `sin cos tan 45` tulkitaan yhdistetyksi funktioksi `sin(cos(tan(45)))` ja negatiivisia arvoja ei ole välttämätöntä merkitä sulkeisiin eli `1--1` vastaa lauseketta `1-(-1)`.

Muuttujien nimeäminen on rajattu käytännön syistä siten, että muuttujan nimen tulee koostua 1-3 kirjaimesta joita seuraa 0-4 numeroa. Muuttujien arvot tallennetaan Javan omaan HashMap-tietorakenteeseen. Näin ollen muuttujiin kohdistuvat operaatiot toimivat käytännössä O(1)-aikaisesti.

Ohjelma koostuu viidestä luokasta: Main, Calculator, Tokenizer, Validator ja TextIO sekä IO-interfacesta. TextIO ja IO luotiin lähinnä testaaminen mielessä, mutta jälkikäteen viisasteltuna projektissa olisi pärjännyt aivan mainiosti ilmankin. Lyhyt katsaus luokkien toiminnasta:

### Main
Main käynnistää itse ohjelman luupin ja sisältää myös mahdollisuuden tulostaa käyttöohjeet.

### Calculator
Calculator sisältää varsinaisen shunting-yard algoritmin sekä HashMap:n muuttujien arvojen tallentamiseen. Luokan calculate-metodi tarkistaa aluksi, onko lauseke sijoitus muuttujaan. Seuraavaksi merkkijono lähetetään Tokenizer-luokkaan pilkottavaksi luvuiksi, operaattoreiksi, suluiksi, muuttujiksi ja funktioiksi Queue-muotoiseksi jonoksi. Käytännössä syötteelle tehdään leksikaalinen analyysi. Saatu jono lähetetään Validator-luokkaan validoitavaksi joka puolestaan tekee lauseelle syntaktisen analyysin. Ennen varsinaista shunting-yard algoritmia, muunnetaan vielä negatiivisten muuttujien ja funktioiden etumerkki `-` merkistä `~` merkiksi, jotta negatiiviset arvot voidaan myöhemmin lausekkeen arvoa laskiessa käsitellä helpommin.

Tämän jälkeen on toteutettu ArrayDeque ja Stack tietorakenteiden avulla itse shunting-yard algoritmi. Algoritmi muuttaa ns. infix-muotoisen lausekkeen postfix-muotoiseksi. Toisin sanoen lauseke muunnetaan muotoon jossa laskujärjestys on yksikäsitteinen ja lausekkeen arvo voidaan laskea käymällä se läpi vasemmalta oikealle. Suluista päästään siis kokonaan eroon. Operandien väleissä olevat operaatiot ikäänkuin siirtyvät niiden perään. Esimerkiksi lause `1+2-3` on postfix muodossa `1 2 + 3 -` ja lauseesta `1+2*3` tulee `1 2 3 * +`. Algoritmi käy syötteen läpi kerran eli toimii lineaarisessa ajassa O(n).

Postfix-muotoiseen lauseeseen sijoitetaan muuttujat jonka jälkeen sen arvo lasketaan evaluate-metodissa. Arvon laskeminen on yksinkertaista. Jonosta lisätään arvoja pinoon, kunnes vastaan tulee laskutoimitus tai funktio. Tämän jälkeen pinon kahdelle päällimmäiselle arvolle toteutetaan laskutoimitus tai funktion ollessa kyseessä pinon päällimmäiselle arvolle. Samaa jatketaan, kunnes jono on käyty läpi. Arvon laskeminen tapahtuu siten myös O(n)-aikaisesti.

Viimeiseksi mikäli lauseke on sijoitus muuttujaan, tallennetaan arvo ja lopuksi tulostetaan lausekkeen arvo käyttäjälle.

### Tokenizer
Luokka pilkkoo regex:n avulla merkkijonon operandien ja sulkujen kohdalta paloiksi. Tämän jälkeen se tunnistaa tuetut funktiot ja erottelee ne mahdollisista muuttujista. Luokka tunnistaa myös tunnistaa liukuluvut ja huolehtii, että lukujen, muuttujien ja funktioiden negatiiviset etumerkit sisältyvät paloihin. Funktioiden tunnistaminen ja negatiivisten etumerkkien käsittely käy kumpikin syötteen läpi kertaalleen, eli luokan operaatiot toimivat lineaarisessa O(n)-ajassa.

### Validator
Validator-luokka tarkistaa, että käyttäjän antama syöte noudattaa laskimen laskusääntöjä, muuttujan nimeämiskäytäntöä ja että lausekkeessa käytetyillä muuttujilla on arvo. Virheellisestä syötteestä pyritään antamaan mielekästä palautetta. Syöte käydään läpi useaan kertaan jolloin koodi pysyy selkeämpänä. Taitava algoritmiohjelmoija olisi epäilemättä saanut läpikäyntien määrää vähennettyä, mutta erilaisten tarkastusten eritteleminen tuntui mielekkäältä tavalta tässä projektissa ja teki mahdolliseksi tarkemman palautteen antamisen. Pilkottua syötettä tarkastaessa käydään ensin läpi muuttujien oikea nimeäminen. Seruraavaksi tarkistetaan, että käytettyihin muuttujiin on sijoitettu arvo. Tämän jälkeen tarkistetaan sulkujen oikea järjestys ja määrä. Viimeiseksi käydään läpi, että operandit, operaatiot, sulut, muuttujat ja funktiot ovat oikeassa järjestyksessä. Esim. ettei kahta operandia ole peräkkäin kuten `1*/2`.

## Aika- ja tilavaativuudet
Työn luonteen vuoksi aika- ja tilavaatimuksien analysointi ei ole kovin merkittävää. Vaikka esim. Validator-luokka käykin syötteen läpi useaan kertaan, tapahtuvat kaikki läpikäynnit lineaarisesti, joten aikavaativuus on O(n). Työ käyttää enimmäkseen jonoa ja pinoa, sekä hajautustaulua muuttujien arvojen tallentamiseen. Näin ollen tilavaativuus on O(n).

## Puutteet ja parannusehdotukset
Työ on tehty pahassa kiireessä mm. samanaikaisesti n. 10 vuoden asumisen jälkeisen muuton kanssa joten parannettavaa on epäilemättä runsaasti. Merkittävin parannus olisi ehdottomasti koodin refaktorointi. Nykyisellään esim. Calculator-, Tokenizer- ja Validator-luokat taitavat kaikki sisältää omat tarkastuksensa onko kyseessä funktio ja toteutuksetkin ovat hyvin poikkeavia. Kätevintä olisi kaiketi käyttää vain Validator-luokkaa ja sen Set-ratkaisua kaikissa luokissa. Samoin negatiivisten arvojen käsittelyssä varsinkin lausekkeiden alussa ja lyhyissä lausekkeissa on todella sotkuista koodia. Joiltain osin myös muuttujien nimeäminen on ollut hieman kirjavaa ja voisi olla esim. luokkien välillä yhtenäisempää. Toiminnallisuudesta laskimeen olisi ollut kiva lisätä useamman argumentin funktioita kuten min ja max
-funktiot ja mahdollisesti myös kertoma, jolloin itse shunting-yardin osalta työ olisi ollut ehkäpä hieman vaativampi. Testauksessakin on parannettavaa, mutta tästä tarkemmin testausdokumentissa.

Lopullista dokumentaatiota kirjoittaessa ohjelmasta tuli myös mieleen, että logaritmeja lisätessä ymmärsin, ettei syötettä validoidessa tiedä tuleeko logaritmin argumentiksi positiivinen luku vai negatiivinen. Tämä oli siis huomioitava itse laskun tulosta laskiessa. Sama ongelma pätee myös nollalla jakamiseen (ja tangenttiin...), esim. lauseke `1/(1-1)` ei ole määritelty, vaikka se ohjelman näkökulmasta onkin validi. Tässä kohtaa, kun korjauksia ei enää ehdi tehdä, Java kuitenkin pelastaa ohjelman kaatumiselta laskien arvoksi äärettömän.

## Lähteet
* [Shunting-yard-algoritmi](https://en.wikipedia.org/wiki/Shunting-yard_algorithm)
* Antti Laaksonen, Tietorakenteet ja algoritmit 2021
