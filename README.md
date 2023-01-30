# Progetto_Naboo
### Progetto universitario, bot telegram sulle ntozie di serie A

#### Membri del gruppo:
<ul>
<li> Anghinolfi Luca: 
<ul>
<li> email: luca.anghinolfi2@studio.unibo.it</ll>  
<li> num. tel: +39 345 957 6969 </ll>
</ul>
</ul>

<ul>
<li> Rossi Tommaso: 
<ul>
<li> email: tommaso.rossi23@studio.unibo.it</ll>  
<li> num. tel: +39 366 328 7560 </ll>
</ul>
</ul>

#### Descrizione del progetto generale:
NABOO è un progetto universitario che consiste nella realizzazione di un Bot Telegram e di un pannello grafico che consentono la raccolta di diverse fonti (Feed RSS): queste vengono aggregate e rese fruibili agli utenti che utilizzeranno il bot.<br>
NABOO permette inoltre di gestire: notizie, utenti, commenti ed altre funzionalità tramite il pannello grafico accessibile soltanto agli amministratori.

Il nostro progetto si basa su notizie sportive, nello specifico calcistiche riguardo alle squadre del campionato di Serie A.<br>

Abbiamo scelto questo tema perché siamo entrambi appassionati di calcio e ci sembrava interessante sviluppare un ambiente di raccolta e condivisione di notizie valutabili dagli utenti.<br>

#### Descrizione del bot telegram:
Serie A Live News Bot è un bot di notizie calcistiche a cui ci si registra in automatico appena l’utente avvia il bot.<br>
Successivamente l’utente potrà scegliere quali notizie vuole visualizzare suddivise per squadra; una volta caricate le notizie potrà commentarle e/o votarle (like/dislike).<br>


#### Descrizione del pannello grafico:
Il pannello grafico è realizzato con JavaFX e permette agli amministratori di NABOO di aggiornare le notizie dai feed o da file, gestire gli utenti, i commenti e le fonti.<br>
Tutte le modifiche apportate dagli amministratori sul pannello grafico sono salvate sul database.

#### Linguaggi/Software utilizzati:
<ul>
<li>Linguaggi:</li>
<ul>
<li>java</li>
</ul>
<li>Software:</li>
<ul>
<li>SceneBuilder</li>
</ul>
<li>Librerie</li>
<ul>
<li>Abbiamo utilizzato Maven per gestire tutte le librerie e tenere il codice pulito</li>
</ul>
</ul>

#### Funzionamento Telegram bot:
<ol>
<li>Registrazione</li>
Gli utenti vengono registrati all’invio del comando /start (che avvia il bot), vengono prelevati username o nome utenete telegramn
<li>Visualizzazione del menù di bottoni</li>
il bot manda un messaggio contenente un menù di bottoni che sono associati ad una squadra di Serie A.
<li>Visualizzazione notizie</li>
Al premere di un bottone vengono visualizzate le corrispettive notizie
<li>Valutazione notizia</li>
E' anche possibile lasciare un commento o una valutazione ad ogni singola notizia
</ol>

#### Funzionamento Pannello grafico:

<ol>
<li> Login </li>
<li> Aggiornamento Notizie </li>
Da questa sezione è possibile aggionrare le notizie sul database
<li> Aggiunta/Rimozione feed </li>
Da questa sezione è possibile visualizzare i feed da cui si prelevano i dati, rimuoverli o aggiungerne ulteriori
<li> Rimozione notizie </li>
Da questa sezione è possibile visualizzare le notizie e rimuoverle
<li> Gestione utenti </li>
Da questa sezione è possibile visualizzare gli utenti e in caso bloccarli
<li> Rimozione commenti </li>
Da questa sezione è possibile visualizzare e rimuovere i commenti effettuati su telegram 
<li> Importa notizie da file </li>
Da questa sezione è possibile importare ntizie da dile esterni
<li> Profilo amministratore </li>
Da questa sezione è possibile modificare i di accesso dell'amministrartore, e disconnetersi dal pannello
</ol>

#### Difficoltà incontrate:
feed rss
Database --> Hibernate
JavaFx --> tabelle
Query
