Projectteam: Nicolas Van Dyck, Tom Gijsbers, Matthias Van Rooy

We hebben ervoor gekozen om een app te ontwikkelen waarmee je kaartjes van fruit of groenten kan scannen. Je krijgt dan het product in kwestie te zien en bij ieder product kan je ook extra uitleg laten afspelen. Gebruikers kunnen dus als het ware 'groenten' en 'fruit' verzamelen. 

Microservices:
We bouwden verder op de voorbeeldservices (product-service, inventory-service & order-service) en voegden er een vierde service aan toe, de user-service. 
De user-service beheert alles wat logischerwijs met de users te maken heeft, zoals het aanmaken van users, het aanpassen van je profiel en het toevoegen van gescande producten in een lijst. De user-service werkt dus vooral samen met de product-service om de juiste gegevens op te halen.

De product-service hebben we aangepast aan onze noden en de inventory- en order-service hebben we zodanig aangepast dat we in principe een basis van winkelkarfunctionaliteit aan zouden kunnen toevoegen. We hebben er met name voor gezorgd dat wanneer je een order plaatst, dat de stock in de inventory dan ook naar beneden gaat.

Ons diagram wat betreft samenhang services en gateway vind je als draw.io-bestand terug in deze repo.

Uitbreidingen:
We hebben voor iedere service unittesten toegevoegd aan de hand van mockito en MockWebServers die voor 100% coverage zorgen.
Hosting van de API gebeurt via Nicolas' eigen server.

De link naar het flutterproject op github: https://github.com/TomGijsbers/florafocus

Werking van gateway op port 8084 Postman:
GET /users/all (get all users)
![image](https://github.com/user-attachments/assets/94b5bf33-6f0a-4f36-af20-1c6cd21c99f9)

GET /users?email (get user through email)
![image](https://github.com/user-attachments/assets/87353eea-669c-4326-aef9-6c4d7ab23143)

GET /users/{id} (get user through id)
![image](https://github.com/user-attachments/assets/9fe3603c-ab33-450b-b36e-df2bfaeeae7d)

POST /users (create new user)
![image](https://github.com/user-attachments/assets/3b5a5b90-3641-46be-a77b-ef527b03f494)

GET /users/{id}/products (get the products already collected by the user)
![image](https://github.com/user-attachments/assets/a794f995-3d82-41bc-92ba-834618fe84f6)

PUT /users/{id} (update user info)
![image](https://github.com/user-attachments/assets/36e171ac-6cb6-4a41-bbd0-bcb6818ca3df)

POST /users/{id}/products/{skuCode} (add product to a user)
![image](https://github.com/user-attachments/assets/0c94b7df-33d1-4d40-a246-b475217857c9)

GET /products?skuCode= (get a product based on skucode)
![image](https://github.com/user-attachments/assets/4ea41ba7-9b71-4013-a513-673d98fe4447)

GET /products/all (get all products)
![image](https://github.com/user-attachments/assets/8d846778-a4c0-41ee-b521-e94077ecf37a)

GET /orders (get all orders)
![image](https://github.com/user-attachments/assets/f1cebc04-e83e-40ec-a93e-6cb76e12fdfd)

POST /orders (create a new order)
![image](https://github.com/user-attachments/assets/1f6739cc-975d-4d44-8798-fe5a71027bab)

Niet via Gateway:

GET /api/inventory?skuCode= (get inventoryitem by skuCode)
![image](https://github.com/user-attachments/assets/b29f1e05-5027-4bb8-be3b-3ccce805ee46)

PUT /api/inventory (subtract ordered amount from stockquantity)
![image](https://github.com/user-attachments/assets/7d85b282-b69d-46ae-82b9-8f0324713d12)

