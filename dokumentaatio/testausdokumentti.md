# Testausdokumentti

## Yleistä testaamisesta
Työn luonteen vuoksi on oleellista pohtia mitä on mielekästä testata. Ohjelman aikavaativuuden ollessa kokonaisuudessaan käytännössä lineaarinen, ei tietokoneilla ole mitään haastetta edes todella pitkän lausekkeen laskemisessa. Näin ollen on suorituskyvyn sijaan keskeistä testata, että ohjelma:

* pilkkoo merkkijonon oikein
* tunnistaa virheellisen syötteen
* antaa virheellisestä syötteestä järkevän virheilmoituksen
* laskee laskut oikein

Työ on testattu Junitilla. Koska suurin osa luokkien metodeista on private-metodeja, on varsinaista yksikkötestausta tehty siltä osin kuin se on ollut mahdollista ja mielekästä. Järkevimmältä on tuntunut testata ohjelmaa hieman end-to-end henkisesti antamalla Calculator-luokan calculate-metodille erilaisia syötteitä ja tarkistamalla, että sen palautteena on oikea laskutulos tai virheilmoitus.

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

## Testauskattavuus
![Jacoco Test Report](https://github.com/murmurian/tiralabra2022/blob/main/dokumentaatio/jacoco.jpg)

Testikattavuus on koko ohjelman osalta hyvä 93% Main-luokan ollessa ainoa jota ei ole lainkaan testattu. Testikattavuusraportista huomaa, että ohjelmasta löytyy joitain yksittäisiä rivejä joita ei olla saavutettu. On hyvin mahdollista, että kyseisiin pätkiin ei koskaan edes voi päätyä, mutta en niitä uskalla poistaa, koska ohjelmaa koodatessa ne tuntuivat sen hetkisessä rakenteessa tarpeellisilta.

## Haasteita ja parannettavaa testauksessa
Ohjelmasta löytyi aivan viime hetkeen asti merkittäviä bugeja. Tästä johtuen iso osa testauksesta painottui oikeanlaisen virheilmoitusten testaamiseen. Laskujen oikeellisuutta testataan siis melko vähän, eivätkä testattavat lausekkeet ole kovin monimutkaisia. Ajan puutteen vuoksi myös monipuolisten laskujen keksiminen on ollut osaltaan haastavaa. Myös liukuluvut asettavat haasteita laskujen oikeellisuuden testaamiselle, koska ne eivät ole tarkkoja. Tämä korostuu erityisesti esim. trigonometristen funtioiden testaamisessa. Toisinaan trigonmetrinen identiteetti (sin(x))^2+(cos(x))^2 saattoi ohjelmaa käsin testatessa antaa arvoksi 0.999... Junit sallii liukulukuja vertaillessa tarkkuuden määrittämisen, mutta koska evaluate-metodi ei ole julkinen, antaa ohjelma laskujen tulon String-muodossa. Tämän olisi voinut ratkaista vaikkapa tallettamalla viimeisimmän laskutuloksen luokan muuttujaan, jolloin laskimeenkin olisi saanut kivan lisäominaisuuden.
