--- README ---

I developed an Android app that utilizes a MySQL database and PHP Web API to enable the sharing, group evaluation, improvement and implementation of new ideas and innovation.

This android application was my first attempt at full stack development and it has been an incredible experience!

This app is best for anyone who thinks of something brilliant, sees a problem in the world they want fixed, or is bored and wants to toss something out there and see what people think

This app connects innovators with good concepts who may never do anything with them otherwise to individuals who will be able to implement them to better us all.

This app allows group evaluation and improvement of ideas plus casual areas for funny/humorous ideas, which reduces riff raff from the serious sections without moderation by administrators.


I am currently in the beta stages of development. What does this entail?

--------------------------------------------

--- current state of the Project ----

- Open testing, working on stability, performance, changing the "back" function, and adding comments.

- Fully network connected! Your accounts and data are now saved and persistent!

----------------------------------

1. Emulator here for all device sizes! - www.appetize.io/app/rtkyh4gvg02x56uzeffx1349k4 - Emulator is internet connected but cannot handle GoogleLocation API so reverse geocoding/location services are emulated

2. I have a list of things I will do listed below

3. Get the APK Here and give me Feedback - www.thekarlbrown.com/ChangeTheWorld.apk

4. I am currently accommodating multiple display sizes and doing final polishing in order to get the app on the Google Play store!


--------------------------------------------

-- MySQL Abstract (Done in MySQL Workbench) --

http://thekarlbrown.com/mysqleerd.png

Entity Relationship Diagram I designed for the app

EDIT: Completely redid MySQL abstract 3/7 to meet changing demands/new knowledge

--------------------------------------------

--- PHP ---

Web API Fully Completed, Available Here at: www.github.com/thekarlbrown/ChangeTheWorld/blob/master/server-side%20php/PHP%20API.txt

To-Do: Improve the PHP API for better verification, change from GET to POST

--------------------------------------------


--TO-DO/Tactics for Implementation --

improve incorrect queries/returns of how things failed then with new information give more detail client side about what was wrong about login

set custom go/backs for sub-pages, currently back button exits app

hash password client side instead of post/get plaintext  (doubtful that risk of knowing hashing technique via reverse engineering of apk client side is worse than sending post/get that could be wiresharked but will verify before implementation)

(for example, special characters in password fields vs weaker passwords for users vs certain characters not allowed. will PHP adequately protect Database with PDO?)

change friends to favorites in favorite ideas

php adds: deal with null inputs for insert/update calls

change php inserts so that auto_increment doesn't trigger after app is working and diagnostics are complete IE add id=id+1 not autoincrements (maybe)

retrieve categories from server instead of setting them manually

determine if all bar methods necessary, determine if I should make leaderboard filter/access separate object

have "check for new" and "get more" buttons at top and bottom once pushing is implemented

decide if leaderboard is real-time or at end of each day and if it has time

check status of copy/paste

add leaderboard onclicks to user profiles

due to complexities of php, if ($case==0) will throw an error but stil evaluate as true then change the database in favratd.php. I need to check to see how this affects the program later, but assume we are not worrying about custom attacks atm and may be a non-issue

--------------------------------------------

-- coming after initial setup --

add ability to delete old drafts, and save over existing draft

view list of followers onclick pulls you into list of followers not ideas, which then pulls into each individuals ideas (requires new data container)

bring custom picture for each user (or do away with picture in profile)

add images for profiles to various tabs, save profile picture to server

implement comments, add notifications from comments

calculate your PERSONAL rank (not leaderboard) by area

implementing reporting ideas

fix drawer expansion style to make it more smooth

--------------------------------------------

--- special thanks to ---

Napalm, g00s, groxx,TacticalJoke, lasserix,cbeust at #android-dev

danblack, seekwill, salle at #mysql

alphos, xyphoid. TML at #php

cmalekpour on github

Lisa Slover for excellent icon ideas
