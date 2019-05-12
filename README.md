# 3I107-backend

## Description
Objectifs
1. Ingérer de la donnée :​ Charger de la donnée le plus rapidement possible.
2. Retrouver les lignes :​ Effectuer des requêtes permettant de retrouver rapidement de la
donnée fine. Les fonctions SQL similaires sont le SELECT ... WHERE ... GROUP BY
3. Distribuer le dataset :​ Tournant sur 3 noeuds, développer le meilleur moyen de répartir la donnée

## API
| Method | Route                      | Data(Example)
| ------ | ---------------------------|------
| PUT    | /table/:tableName          |``` { "fields": [{ "name": fieldX },{ "type": Int }]} ```
| PUT    | /table/index/:tableName    |``` { "newIndex": "firstName"} ```
| POST   | /table/insertOne/:tableName|``` { "data": [{ "name": "firstName", "value": "NGUYEN"}]} ```
| GET    | /table/:tableName?query    | /table/:tableName?firstName=NGUYEN&lastName=Jasmine

#TODO: GetQuery
table needs to be sorted, MAP => REDUCE
(Map by the key hashCode)
## Installation
To launch your tests:
```
./mvnw clean test
```

To package your application:
```
./mvnw clean package
```

To run your application:
```
./mvnw clean exec:java
```

## Run configuration
Everything is passed in the redeploy.bat
To change current port and other sibling-ports
```
-Dport=8081 -Dother=8082,8083
```

## Usage pricipal
Grandes étapes
- [ ] 1. Développer l’API REST
Définir une API REST standard permettant d’effectuer les 4 opérations ci-dessus.
- [ ] 2. Ajouter le support des tables / index
La structure interne restera simple (HashMap, ...) mais permettra d’obtenir des premiers résultats probants sur une faible volumétrie.
- [ ] 3. Charger de la données
Réaliser le parsing du CSV et l’ingestion des lignes dans les index d’une table
- [ ] 4. Distribuer le serveur
Être capable de faire communiquer les noeuds. Lorsqu’une action s’effectue sur un noeud, elle est répercutée sur les autres. Chaque noeud connaît les tables, index ... LEs notions de tolérance aux pannes et réplication ne sont pas l’objectif du projet.
- [ ] 5. Rendre le programme performant
- Être capable d’avoir le plus de lignes en mémoire disponible
- Loader / Récupérer de la donnée le plus vite possible
- Découvrir les techniques d’indexation (B-Tree, hash, ... )


## Fonctionnalités supplémentaires
Quelques exemples :
- Support des aggrégats (SUM, AVG, MIN, MAX, COUNT) comme en SQL
- Mise à jour / suppression de lignes dans les index
- Support du HAVING ORDER BY LIMIT
- Administration des tables / index (suppression, ajout de colonnes ... )
- Ajout de fonctions comme CONTAINS, YEAR ... dans les requêtes de sélection
