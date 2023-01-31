# Progetto_Naboo
### Progetto universitario, bot telegram sulle notizie di Serie A

#### Membri del gruppo:
<ul>
   <li>Anghinolfi Luca:</li>
  <ul>
     <li>Email: luca.anghinolfi2@studio.unibo.it</li>
     <li>Num. tel: +39 345 957 6969</li>
  </ul>
</ul>

<ul>
  <li> Rossi Tommaso:</li>
  <ul>
      <li>Email: tommaso.rossi23@studio.unibo.it</li>  
      <li>Num. tel: +39 366 328 7560</li>
  </ul>
</ul>

#### Descrizione del progetto:
NABOO è un progetto universitario che consiste nella realizzazione di un Bot Telegram e di un pannello grafico che consentono la raccolta di diverse fonti (Feed RSS): queste vengono aggregate e rese fruibili agli utenti che utilizzeranno il bot.


NABOO permette inoltre di gestire: notizie, utenti, commenti ed altre funzionalità tramite il pannello grafico accessibile soltanto agli amministratori.

Il nostro progetto si basa su notizie sportive, nello specifico calcistiche riguardo alle squadre del campionato di Serie A.


Abbiamo scelto questo tema perché siamo entrambi appassionati di calcio e ci sembrava interessante sviluppare un ambiente di raccolta e condivisione di notizie valutabili dagli utenti.


#### Descrizione del bot telegram:
Serie A Live News Bot è un bot di notizie calcistiche a cui ci si registra in automatico appena l’utente avvia il bot.


Successivamente l’utente potrà scegliere quali notizie visualizzare suddivise per squadra; una volta caricate le notizie potrà commentarle e/o votarle (like/dislike).


#### Descrizione del pannello grafico:
Il pannello grafico è realizzato con JavaFX e permette agli amministratori di NABOO di aggiornare le notizie dai Feed o da file (.txt), gestire gli utenti, i commenti e le fonti.


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
<li>Abbiamo utilizzato Maven per gestire tutte le librerie e tenere il codice pulito.</li>
</ul>
</ul>

#### Funzionamento Telegram bot:
<ol>
<li>Registrazione</li>
Gli utenti vengono registrati in automatico all’invio del comando /start (che avvia il bot), contestualmente viene salvalo lo username o il nome utente Telegram.
<li>Visualizzazione del menù di bottoni</li>
Il bot manda un messaggio contenente un menù di bottoni, ognuno è associato alla corrispondente squadra di Serie A.
<li>Visualizzazione notizie</li>
Premendo i bottoni vengono visualizzate le corrispettive notizie.
<li>Valutazione notizia</li>
E' anche possibile lasciare un commento o una valutazione ad ogni singola notizia.
</ol>

#### Funzionamento Pannello grafico:
<ol>
  <li>Login</li>
  <li>Aggiornamento Notizie</li>
      Da questa sezione è possibile aggiornare le notizie sul database;
  <li>Aggiunta/Rimozione feed</li>
      Da questa sezione è possibile visualizzare i feed da cui si prelevano i dati, rimuoverli o aggiungerne altri;
  <li>Rimozione notizie</li>
      Da questa sezione è possibile visualizzare le notizie e rimuoverle;
  <li>Gestione utenti</li>
      Da questa sezione è possibile visualizzare gli utenti ed eventualmente bloccarli;
  <li>Rimozione commenti</li>
      Da questa sezione è possibile visualizzare e rimuovere i commenti effettuati su Telegram;
  <li>Importa notizie da file</li>
      Da questa sezione è possibile importare notizie da file esterni;
  <li>Profilo amministratore</li>
      Da questa sezione è possibile modificare i dati di accesso dell'amministratore, ed effettuare la disconnessione dal pannello.
</ol>



#### Difficoltà incontrate:
Durante la creazione del progetto abbiamo incontrato diverse difficoltà che poi siamo riusciti a risolvere ; uno dei primi problemi che abbiamo riscontrato è stato comprendere il funzionamento dei Feed RSS, ma una volta capita la logica ci è risultato molto comodo il loro utilizzo.


Abbiamo poi voluto implementare il nostro progetto aggiungendo il database e sostituendolo al salvataggio su file, perchè ci sembrava più realistico e più completo; questo ci ha creato alcuni problemi dovuti alla maggiore complessità del salvataggio dei dati e al collegamento con il pannello grafico.


Utilizzando Javafx ci siamo imbattuti infine nella problematica della creazione e visualizzazione delle varie tabelle per visualizzare i dati contenuti nel database sul pannello grafico.

