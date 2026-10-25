# skdb-registry-service

Een full-stack simulatie van het Strafrechtketen Databank (SKDB) persoonsregister. Het systeem combineert een Angular frontend voor ketengebruikers met een losse Java backend die strikte identiteits- en validatieregels (zoals de 11-proef en unieke strafketennummers) afdwingt.

## 1. Context en probleemstelling

Wanneer er een persoon een verdachte wordt bij de politie, krijgt deze een strafrechtketennummer. Deze is per persoon uniek. Het strafrechtketennummer wordt dus gebruikt door de politie, maar ook door de rechtbank en de reclassering en geeft een verdachte een unieke identiteit.

## 2. Architectuur en ontwerpkeuzes

### Hexagonale architectuur (ports & adapters)

Het project bestaat uit verschillende lagen, het domein, de applicatie en de infrastructuur. Deze zijn gescheiden per package, en zijn verdeeld per map in de folderstructuur. Het domein weet niks af van de database, van SQL of van de rest van het project. De buitenwereld (frontend/HTTP) praat door middel van de inkomende (driving) adapter (de REST-controller) met de applicatie (RegistreerPersoonService). De applicatie praat alleen met- en roept methodes aan van de poort (PersoonRepository). De uitgaande (driven) adapter (PostgresPersoonRepository) importeert zowel uit het domein als uit de infrastructuur en implementeert de methodes die bepaald zijn in de poort.

Doordat de applicatielaag alleen van de poort afhangt, kan je het hele project draaien en testen zonder überhaupt een echte database aan te hoeven sluiten. De poort dwingt vooraf bepaalde methodes af, en laat andere methodes niet toe. Het domein bepaalt dan welke methodes/operaties mogen worden gebruikt, in plaats van dat een framework dat doet. Zo wordt het voorkomen dat de applicatielaag moet af weten van database-tabellen en methodes en dat bijvoorbeeld deleteAll(), findAllById(), flush() of findById() (van JpaRepository) door een andere developer aan kan worden geroepen.

### Pure Domain Core (POJO)

Er zitten geen framework- of JPA-annotaties in het domein. Het domein is in pure Java geschreven. Grote aantallen aan tests uitvoeren is op deze manier vele malen sneller, door gebruik te maken van een gewone JVM i.p.v. een Spring ApplicationContext. Annotaties zoals @NotNull of @Pattern zijn slechts metadata en worden genegeerd met runtime (en dwingen niks af bij het instantiëren met "new"). Overigens: stel dat je wil veranderen van het soort database, dan hoeven alleen de infrastructuur en de uitgaande adapter te worden aangepast, de applicatie en het domein blijven onaangeraakt.

Het voorkomt problemen op lange termijn door het feit dat frameworks constant veranderen. Bijvoorbeeld dat er meerdere annotaties opeens verouderd zijn of dat packages moeten worden hernoemd. Dit voorkomt het risico dat de werking van de businesslogica wordt aangetast en kan je er vanuit gaan dat het bij het framework ligt (in de infrastructuur). Ook maakt het de code meer in lijn met de Ubiquitous Language (vaktaal van het domein) door het vermijden van de technische termen die komen met het gebruik van annotaties.

### Decoupled Persistence & Mappers

Er is zowel een NatuurlijkPersoon klasse, als een PersoonJpaEntity, om te vermijden dat NatuurlijkPersoon als domeinmodel moet omgaan met database gerelateerde details als primary keys, en eventuele null waardes van een tabel. De validatieregels in NatuurlijkPersoon blijven zo gehanteerd en de database-details worden afgehandeld door PersoonJpaEntity. De use-case (de service in de applicatielaag) praat alleen met de domeinlaag en is hierdoor net zoals het domein niet beinvloedbaar door veranderingen in het gebruikte framework. En voor NatuurlijkPersoon is de unieke sleutel Strafrechtketennummer en niet die van een database (een Long id).

De mapper (in dit geval PersoonMapper) is er om een entiteit vanuit het domein, zoals NatuurlijkPersoon, en de bijbehorende eigenschappen, Strafrechtketennummer en Burgerservicenummer om te zetten (op te slaan/te mappen) naar de geschikte database-vorm (JpaEntity en String) en andersom door te waardes opnieuw door de validatie heen te halen d.m.v. opnieuw instantiëren als object (te reconstrueren). Deze is geschreven in pure Java, en is het koppelstuk (of de vertaler) tussen het domein en de infrastructuur (waar zich ook het framework bevindt). Hierdoor worden fouten direct afgevangen door de compiler i.p.v. runtime errors doordat het veld stiekem null wordt gelaten bij een typefout.

## 3. Domeinmodel en invarianten

- **Value Objects:**
  Dit zijn objecten met strikte vooraf gedefinieerde validatieregels, die vaak tot de eigenschappen behoren van een Aggregate Root/Entiteit. Ze hebben geen eigen identiteit, en zijn onderling uitwisselbaar. Ze zijn onveranderlijk (immutable), en dus wanneer een entiteit verandert van eigenschap wordt daarmee het hele object vervangen. Ze valideren zichzelf, zodat je in de applicatie- of infrastructuurlaag zeker weet dat je een juiste instantie van het object ontvangt. Dit hoeft daar dus ook niet meer los te gebeuren.

- **Aggregate Roots:**
  In dit project mag NatuurlijkPersoon of Strafzaak niet bestaan zonder hun bijbehorende eigenschappen (Strafrechtketennummer, Burgerservicenummer, Parketnummer). Twee instanties van NatuurlijkPersoon met hetzelfde Strafrechtketennummer, of twee instanties van Strafzaak met hetzelfde Parketnummer zijn hetzelfde. Voor hen is deze eigenschap leidend. Ze bewaken hun eigen toestand, doordat van buitenaf hun eigenschappen niet kunnen worden gewijzigd (geen setters).

- **Statusovergangen:**
  Strafzaak heeft geen setters voor zijn property "status". Status wordt bewaakt door middel van vooraf gedefinieerde waardes en methodes. Zo mag status binnen het type ZaakStatus alleen IN_ONDERZOEK, GESEPONEERD of GEDAGVAARD zijn. De methodes bewaken de overgang van deze statussen. Zo wordt er een IllegalStateException gegooid wanneer een iemand een zaak bijvoorbeeld probeert te seponeren worden wanneer die al gedagvaard of al geseponeerd is of wanneer iemand de zaak probeert te dagvaarden wanneer die al geseponeerd of gedagvaard is.

## 4. Teststrategie (TDD)

## 5. Technologieën en tools

## 6. Hoe draai je en test je het zelf?

### Vereisten (Prerequisites)

Voer uit: "npm install" in /frontend

Je moet voor het draaien van het project Docker, Node v22.22.2 en minimaal Java 21 hebben

### Hoe draai je de backend en frontend?

De backend draai je door middel van "./mvnw spring-boot:test-run" (Testcontainer) uit te voeren in de root en de frontend draai je door middel van "npm start" uit te voeren in /frontend

De backend draait op localhost:8080 en de frontend draait op localhost:4200

### Testen

De backend wordt getest door middel van "./mvnw test" uit te voeren in de root en de frontend wordt getest door middel van "npm run test:ci" uit te voeren in /frontend
