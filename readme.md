# Word Master

The `Word Master` is an app where people can learn English words.It generates words for you to review and new words for you to learn everyday. The review words generated are based on your previous learning. You can adjust your daily learning goal in settings.

## creator

* Shiqing Guo

## Project Demo

#### Login:
![](/login.png)

* input your userid and password and press signup or login(if you have already signed up). 
* if the format is incorrect, you will get prompt."forget password" is not implemented. 
 
#### home:
![](/home.png)

* the card shows the English word, if you forgot the definition of the word you can tap the card to see its definition.
* click one of "familiar", "not sure" or "unfamiliar" according to if you are familiar with this word. after clicking
the button the card will switch to the next word. 
* you can also slide the card left or right to see the last or next word. When you slide right, the word is
automatically noted as "familiar". 
* the progress bar under the card denotes how much word you have learned today.
* this list of word cards is refreshed every day, you can change the date of your device to see the effect
   
#### dictionary:
![](/dictionary.png)
 
 * after pressing the dictionary icon, you can switch to this page. this is a list of all the words which you can click
  to see its definition. 
 * on the top is a search bar you can use to search a word.
  
#### settings:
![](/settings.png)
 
 * here you can check your user id. user page is not implemented so pressing the right arrow does not have any effect
 , but you can press the logout button to logout.
 * "today learning goal" is the section where you can customize how many words you want to review and learn separately.



## Packages and Major Source Codes

The main package in the project is `com.example.wordmaster`.  There are 5 other packages in `com.example.wordmaster
`: `business` , `database` , `exception` , `model` and `ui`


### model:

The `model`  package is for the basic models of this project.

##### Dictionary:

`Dictionary`  class represents a single item in a dictionary, includes the word and its definition.

##### FrequentWord:

`FrequentWord`  class represents a word from the most frequently used word list.

##### LearnedWord:

`LearnedWord`  class represents a word that's already learned by the user.

##### LearningWord:

`LearningWord`  class represents a word that a user is learning today.

##### User:

`User`  class represents a user.

##### UserInfo:

`UserInfo`  class represents the information related to a user.



### Business:

The classes in `Business` package handle the data access to database and logic of a model.

##### FrequentWordBus:

`FrequentWordBus` class is the business object of `FrequentWord`

##### LearnedWordBus:

`LearnedWordBus` class is the business object of `LearnedWord`

##### LearningWordBus:

`LearningWordBus` class is the business object of `LearningWord`

##### UserBus:

`UserBus` class is the business object of `User`

##### UserInfoBus:

`UserInfoBus` class is the business object of `UserInfo`



### database:

The `database` package handles all the data access.

##### Database:

`Database` load the existing sql database, extract or modify data in the database


### ui:

The `ui` package contains all the android ui code.

##### DictionaryDefinition:

`DictionaryDefinition` is the page after you click an item in the dictionary.

##### DictionaryFragment:

`DictionaryFragment` is the page after you click the dictionary icon from the bottom navigation.

##### EndlessRecyclerViewScrollListener:

`EndlessRecyclerViewScrollListener` is used to render the remaining words after user scroll to the bottom.

##### HomeFragment:

`HomeFragment` is the page after you click the home icon from the bottom navigation.

##### LoginActivity:

`LoginActivity` is the page after you logout or first time login.

##### MainActivity:

`MainActivity` is the start up activity, it sets up the bottom navigation, and detect if should start login activity.

##### SettingsFragment:

`SettingsFragment` is the page after you click the settings icon from the bottom navigation.

##### WordCardAdapter:

`WordCardAdapter` is the adapter for the recycler view to render a list of cards in home page.

##### WordListAdapter:

`WordListAdapter` is the adapter for the recycler view to render a list of words in dictionary page.




### exception:

The `exception` package is responsible for handling the custom exceptions

##### InvalidFormatException:

`InvalidFormatException` happens when the format some user input is invalid.

##### InvalidPasswordException:

`InvalidPasswordException` happens when the password is invalid, it's child of `InvalidFormatException`.

##### InvalidUserIDException:

`DuplicateException` happens when the userID is invalid, it's child of `InvalidFormatException`.



## Testing:

I don't have much time, so only tested part of the program, all the test classes are under `androidTest`.

###### Test Classes:

* `business`:
  * LearnedWordBusTest
  * LearningWordBusTest
  * UserBusTest
  * UserInfoBusTest
* `database`:
  * DatabaseTest
* `model`:
  * LearningWordTest



## System Requirement:

#### Android Version: 

* Minimum requirement: Android 6.0 (Marshmallow). 

* API level: 30

#### Tested Device:

* Emulator: 
  * Pixel_3a, API 30, CPU x86, Android 11



## Project URL:

https://github.com/ShiqingGuo/wordMaster.git


## dataset:
1. frequent word from https://www.corpusdata.org/
2. dictionary from https://github.com/matthewreagan/WebstersEnglishDictionary


## dependency:
1. google material design
2. SQLiteAssetHelper to help load existing database from https://github.com/jgilfelt/android-sqlite-asset-helper
3. Endless Scrolling with AdapterViews and RecyclerView
https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView
