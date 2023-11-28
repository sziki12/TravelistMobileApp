# Travelist – Turista Applikáció

A Travelist egy turista applikáció, melyet az új helyet felfedezni vágyók használhatnak új helyek, más emberek véleményének megismerésére. A felhasználók meg tudják osztani a saját tapasztalataikat is egy adott helyről, képek és szöveges leírások formájában is.

Ezen felül az applikációban lehetőség van valós idejű nyomonkövetésre és segítségkérésre is. A hely alapú ajánlások segítik majd a felhasználókat, hogy a hozzájuk közel eső helyszínek közül könnyebben válasszanak úticélt. 


## Mobil applikáció
A mobil applikáció  az elindítása után egyből engedélyt kér arra, hogy hozzáférjen a helyzetünkhöz.

![](assets/location_permission_1.png)
![](assets/location_permission_2.png)

Miután ezt engedélyeztük, lehetőség van bejelentkezni, illetve ha még nincs fiókunk, akkor regisztrálni.

![](assets/login.png)
![](assets/sign_up.png)

Ezután megjelenik az alkalmazás menüje, ahonnan kiválaszthatja a felhasználó, hogy milyen műveletet szeretne végezni. Minden oldalról a jobb felső sarokban lévő menü ikonra kattintva visszatérhetünk a lehetőségek listájához.

![](assets/menu.png)

A jobb alsó sarokban lévő beállítások ikonra kattintva módosíthatjuk, hogy milyen időközönként frissüljön a helyzetünk.

![](assets/settings.png)
![](assets/location_update_interval.png)

Ha a menüből a Profile opciót választjuk, megtekinthetjük a profilunkat, módosíthatjuk a felhasználónevünket és a profilképünket.

![](assets/profile.png)

A menüből a Search opciót választva megjelennek a térképen a már elmentett helyek, amelyekről adatokat tárolunk. Egy legördülő menüből kiválaszthatjuk, hogy mely város helyszíneit jelenítse meg a térkép.

![](assets/search.png)


A Recommended For You opcióra klikkelve a menüben egy RecyclerView-ban megjelennek a számunkra ajánlott helyek.

![](assets/recommended_for_you.png)

Alapértelmezetten 7,5 kilométeres körzetből ajánl az app helyeket, viszont ezt a távolságot könnyen módosíthatjuk a Modify distance gombra kattintva. Ekkor egy DialogFragment-tel változtatni tudjuk ezt az értéket.

![](assets/recommended_modify_distance.png)

Ha valamelyik helyről részletesebb információkat szeretnénk kapni, akkor a helyhez tartozó Details gombra kell kattintanunk. Ezután megjelennek a helyszínről készült képek, nyitvatartási idő, elérhetőség, leírás.

![](assets/place_info.png)

Ha a képeket szeretnénk nagyobb méretben megnézni, akkor bármelyik fotóra kattintva egy részletesebb nézettel találjuk szemben magunkat, ahol görgetni tudunk a képek között.

![](assets/image_view.png)


Ha a menüből a Track Others opciót választjuk, akkor megtekinthetjük a térképen az 5 kilométeres körzetünkben lévő felhasználók helyzetét.

![](assets/track_others.png)

Az Upload New Place opciót választva feltölthetünk egy új helyről értékelést, képeket, leírást.

![](assets/upload_new_place.png)

A Request Help opciót választva pedig segítséget kérhetünk a közelünkben lévőktől, akik ekkor egy értesítést kapnak. A képernyő közepén lévő nagy gombot kell csupán megnyomni ehhez.

![](assets/request_help.png)