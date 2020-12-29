- App Name: MyRecipe

- Project final presentation slides  
https://docs.google.com/presentation/d/1KVR0hTI3Wg_qbYvfZUW8Rcjv6XK3JwBg_l-_jfQvj7A/edit?usp=sharing

- Project Demo  
https://drive.google.com/file/d/1BTRxnBoCPFLFdOFTlIWhS1t8D2HJ7dsT/view?usp=sharing

- Team Members: Haoyu Wu, Ruoyao Yang, Jialun Chen

- Project Description  
Motivation:
During the special virus quarantine period, most people enjoy cooking and exploring recipes, and keeping track of food intake is an important way to stay healthy.
Therefore, we decide to build an app that can search recipes with our available ingredients at home, and add recipes we're interested in to our own Favorite collections. We can also customize personal meal plan in this app to keep track of nutrients and calories intake.

Features:
[Part 1] Search and Discover
1. User can register an account and login the app
2. User can input his/her height & weight information, and food recording goal(diet/gain muscle/keep), the app will generate a goal daily calory amount for the user
3. In the "Discover" tab shows popular recipes
4. In the "Search" tab, user can search recipes by ingredients
5. In the "Search" tab, user can also search recipes by name
6. When user clicks on one recipe, the detail information will be shown: ingradients, price, cooking instruction and similar recipes

[Part 2] Apply recipes
1. User can create their own Favorite collections, and add recipes in them
2. Recipes can be removed from Favorite collections through dragging
3. Recipes inside Favorite collections can be edited, and add photos taken from camera
4. Recipes can be added to user's own meal plans

[Part 3] Meal plans
1. User can customize their own meal plans to keep record of what they have eaten
2. Meal plan looks like a calendar, showing food eaten and intake statistics such as calories, nutrients, fats, sugar etc.
3. The app will alert the user what nutrients he/she lacks from previous meals, and recommend recipes that are rich in these nutrients to the user


- Additional notes
It can't be said issues but some imperfect performances because of APIs.
The first is that imageUrl returned by API may be null, so we do judgement and using another image when it returns an empty imageUrl.
The second is that smart meal page has no photho shown with recipes because the API used to return the imageUrl but now there is no imageUrl.
And the last thing is that API website has limited number of requests. If you used up the point, please switch to another apiKey in XXRepository.py. We have put 4 apiKey here.
