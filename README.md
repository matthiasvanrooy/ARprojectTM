Projectteam: Nicolas Van Dyck, Tom Gijsbers, Matthias Van Rooy

We hebben ervoor gekozen om een app te ontwikkelen waarmee je kaartjes van fruit of groenten kan scannen. Je krijgt dan het product in kwestie te zien en bij ieder product kan je ook extra uitleg laten afspelen. Gebruikers kunnen dus als het ware 'groenten' en 'fruit' verzamelen. 

Microservices:
We bouwden verder op de voorbeeldservices (product-service, inventory-service & order-service) en voegden er een vierde service aan toe, de user-service. 
De user-service beheert alles wat logischerwijs met de users te maken heeft, zoals het aanmaken van users, het aanpassen van je profiel en het toevoegen van gescande producten in een lijst. De user-service werkt dus vooral samen met de product-service om de juiste gegevens op te halen.

De product-service hebben we aangepast aan onze noden en de inventory- en order-service hebben we zodanig aangepast dat we in principe een basis van winkelkarfunctionaliteit aan zouden kunnen toevoegen. We hebben er met name voor gezorgd dat wanneer je een order plaatst, dat de stock in de inventory dan ook naar beneden gaat.

Ons diagram wat betreft samenhang services en gateway vind je als draw.io-bestand terug in deze repo.

Uitbreidingen:
We hebben voor iedere service unittesten toegevoegd aan de hand van mockito en MockWebServers die voor 100% coverage zorgen.

De link naar het flutterproject op github: https://github.com/TomGijsbers/florafocus

Werking van gateway op port 8084 Postman:
GET /users/all (get all users)
![image](https://github.com/user-attachments/assets/6f039054-218a-4dd9-acc2-486fd5b703b3)

GET /users?email (get user through email)
![image](https://github.com/user-attachments/assets/1832cd22-01be-4e5a-8d24-b67333acedeb)

GET /users/{id} (get user through id)
![image](https://github.com/user-attachments/assets/b3220d6e-2adb-4201-8295-e04a75bb8672)

POST /users (create new user)
![image](https://github.com/user-attachments/assets/3b5a5b90-3641-46be-a77b-ef527b03f494)

GET /users/{id}/products (get the products already collected by the user)
![image](https://github.com/user-attachments/assets/e137db8a-498e-4f47-a1c3-97b8ea6895b1)

PUT /users/{id} (update user info)
![image](https://github.com/user-attachments/assets/36e171ac-6cb6-4a41-bbd0-bcb6818ca3df)

POST /users/{id}/products/{skuCode} (add product to a user)
![image](https://github.com/user-attachments/assets/0c94b7df-33d1-4d40-a246-b475217857c9)

GET /products?skuCode= (get a product based on skucode)
![image](https://github.com/user-attachments/assets/2cc98efb-711b-4b5d-bbd0-e8898907b2cc)

GET /products/all (get all products)
![image](https://github.com/user-attachments/assets/c9324122-8801-4a8a-8967-3f06afe0ff85)

GET /orders (get all orders)
![image](https://github.com/user-attachments/assets/3253303f-fe32-45e7-936e-6f05e41dd145)

POST /orders (create a new order)
![image](https://github.com/user-attachments/assets/2ffc25a3-7b72-49ce-bf07-3e43c4af9d92)

Niet via Gateway:

GET /api/inventory?skuCode= (get inventoryitem by skuCode)
![image](https://github.com/user-attachments/assets/6039d283-6ee2-420c-bb69-b4d93e737255)

PUT /api/inventory (subtract ordered amount from stockquantity)
![image](https://github.com/user-attachments/assets/ba73200f-e357-4509-9249-e61c9fcdd0fa)
