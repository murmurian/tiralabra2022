# Testausdokumentti

Aikatauluongelmien vuoksi kirjoitan tässä vaiheessa vain joitain ajatuksia koskien sovelluksen testaamista:

* sovelluksen luonteen vuoksi tehokkuuden testaaminen ei ole mielekästä
* sovelluksessa tärkeää on, että se kertoo käyttäjälle virheellisestä syötteestä järkevällä tavalla ja tämä täytyy testata
* toinen merkittävä asia on, että laskin laskee laskut oikein. Laskujärjestyksen yms. täytyy siis olla oikein.
* painopiste tulee olemaan yksikkötestauksessa joka toteutetaan Junit:lla
* osaltaan haasteelliseksi testien laatimisen tekee erityyppisten lausekkeiden keksiminen

Tällä hetkellä testikattavuus on kelvollinen. Testien laatu tosin ei vielä ole mielestäni erityisen hyvällä tasolla, mutta toivottavasti tämä korjaantuu pian.

![Jacoco Test Report](https://github.com/murmurian/tiralabra2022/blob/main/dokumentaatio/jacoco.jpg)