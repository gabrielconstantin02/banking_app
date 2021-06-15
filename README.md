# banking_app

## Descrierea proiectului
Proiectul este o aplicatie Android de banking ce permite logare si crearea unui cont, crearea de conturi bancare si depozite, efectuarea de tranzactii, asocierea/crearea de carduri, istoric al tranzactiilor(extras de cont), modificarea/acttualizarea detaliilor contului, personalizari ale profilului. Pentru toate aceste stocari/interogari, aplicatia se foloseste de o baza de date MySQL. Pentru mai multe detalii cu privire la abilitatile aplicatiei, vizitati sectiunea de Backlog & User Stories.

Proiect realizat pentru disciplina "Metode de dezvoltare software" din cadrul Facultatii de Matematica si Informatica, Universitatea Bucuresti.


# Backlog & User Stories
Backlog-ul poate fi consultat in [projects](https://github.com/Kira060200/banking_app/projects/2)

User Stories (si implementarea lor):

1. "Ar trebui sa te poti loga direct cu contul generat de o banca" -> [Login with an existing account](https://github.com/Kira060200/banking_app/issues/3)
2. "Cred ca ar fi utila o functie de creare a contului fara a merge fizic la banca" -> [Create a new account](https://github.com/Kira060200/banking_app/issues/4)
3. "Aplicatia ar avea nevoie de mai multe optiuni de personalizare" -> [Profile picture change](https://github.com/Kira060200/banking_app/issues/25)
4. "Ar fi util sa vad profilul de client in aplicatie" -> [Display profile](https://github.com/Kira060200/banking_app/issues/5)
5. "As vrea sa vad o prezentare sumara a statisticilor contului meu" -> [Display statistics](https://github.com/Kira060200/banking_app/issues/28)
6. "Vreau sa schimb detaliile personale fara a merge fizic la banca" -> [Change personal data](https://github.com/Kira060200/banking_app/issues/25)
7. "Vreau sa schimb parola contului" -> [Change password](https://github.com/Kira060200/banking_app/issues/29)
8. "As vrea sa creez conturi bancare direct din aplicatie" -> [View and create accounts](https://github.com/Kira060200/banking_app/issues/8)
9. "Depozitele ar trebui sa poata fi deschise fara deplasarea la banca" -> [View and create deposits](https://github.com/Kira060200/banking_app/issues/9)
10. "Abilitatea de a face plati directe prin IBAN ar fi foarte utila" -> [Make a direct payment](https://github.com/Kira060200/banking_app/issues/7)
11. "Ar trebui implementata abilitatea de a vedea tranzactiile facute" -> [View transaction history](https://github.com/Kira060200/banking_app/issues/13)
12. "As vrea sa reinnoiesc cardul sau sa il inchid fara a merge la banca" -> [Show cards associated with accounts, renew and close them](https://github.com/Kira060200/banking_app/issues/12)
13. "Vrea sa deschid carduri direct din aplicatie" -> [Generate a new card](https://github.com/Kira060200/banking_app/issues/27)
14. "As vrea un sistem de tipul extras de cont, cu toate detaliile despre contul selectat" -> [View bank account details](https://github.com/Kira060200/banking_app/issues/11) 

# UML 
1. Use case diagram
2. Class diagram

# Database
Diagrama pentru baza de date poate fi consultata [aici](https://imgur.com/Oay0MNT.jpg). 

# Design & Flow
Pentru a intelege mai bine cum functioneaza aplicatia si designul, am realizat [aceasta diagrama](https://whimsical.com/diagrama-aplicatie-banking-GiB6MivjnXyJjdwdnpqPxK). Imaginea este exportata si in proiect [aici](https://github.com/Kira060200/banking_app/blob/master/diagrama_mds.png)



# Source control
1. [branch creation, merge/rebase](https://github.com/Kira060200/banking_app/network)
2. [commits](https://github.com/Kira060200/banking_app/commits/master)

# Teste automate
Testele automate au fost realizate [aici](https://github.com/Kira060200/banking_app/issues/1).

# Bug reporting
1. [Database unavailable after payment](https://github.com/Kira060200/banking_app/issues/33)
2. [Out of range value when inserting into card_number field](https://github.com/Kira060200/banking_app/issues/22)
3. [Crash on twice failed login](https://github.com/Kira060200/banking_app/issues/18)

# Build tool

# Refactoring & Code standards
La partea de refactoring, deoarece aveam nevoie de conexiune cu baza de date, in loc sa realizam aceasta conexiune in fiecare activitate din aplicatie avand mult cod in comun, am creat un [singleton care ne intoarce aceasta instanta de conexiune](https://github.com/Kira060200/banking_app/issues/32). Pentru code standards am urmat [guideline pentru Java de la Google](https://google.github.io/styleguide/javaguide.html)

# Design patterns
Intreaga aplicatie android a fost construita pe modelul MVC:
1. [Model](https://github.com/Kira060200/banking_app/tree/master/app/src/main/java/com/example/banking_app/models) -> aici se gasesc clasele generice pentru User, Account, Deposit, Transaction, Currency
2. [View](https://github.com/Kira060200/banking_app/tree/master/app/src/main/res/layout) -> layout-ul fiecarei activitati din aplicatie
3. [Control](https://github.com/Kira060200/banking_app/tree/master/app/src/main/java/com/example/banking_app/activity) -> activitatile din aplicatie

## Membrii echipei:
- **[Ancuta Ioan](https://github.com/AncutaIoan)**
- **[Constantin Gabriel-Adrian](https://github.com/Kira060200)**
- **[Sociu Daniel](https://github.com/danielsociu)**




