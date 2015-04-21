--- README ---

I am developing an Android app that utilizes a MySQL database on this server to enable the sharing, group evaluation, improvement and implementation of new ideas and innovation.

This android application was my first attempt at full stack development and it has been an incredible experience!

This app is best for anyone who thinks of something brilliant, sees a problem in the world they want fixed, or is bored and wants to toss something out there and see what people think

This app connects innovators with good concepts who may never do anything with them otherwise to individuals who will be able to implement them to better us all.

This app allows group evaluation and improvement of ideas plus casual areas for funny/humorous ideas, which reduces riff raff from the serious sections without moderation by administrators.


I am currently in the alpha stages of development. What does this entail?

--------------------------------------------

--- current state of the Project ----

- Open testing, algorithmic additions required, working on stability, performance, changing the "back" function, and adding comments.

- Fully network connected! Your accounts and data are now saved and persistent!

----------------------------------

1. Emulator here: You MUST use Nexus 7/9 - www.appetize.io/app/rtkyh4gvg02x56uzeffx1349k4 - You MUST use Nexus 7/9, and emulator connects to my server but cannot handle Google Apps so reverse geocoding/location services are emulated

2. The app is in the earliest of alpha. Its performance is not optimized. In fact, I have a list of things I intend on doing, bugs that need to be fixed, and what I intend to do further on in a huge list

3. I would love your feedback, and much later on in the process will need android devices to test on (in addition to whatever I will be testing)

4. If you are interested in helping me setting up the category's and subcategory's/check out https://docs.google.com/a/cnu.edu/document/d/1GVSz_fBI7GjAc4JJta39_EX0B0SVpZ5PUdZyDpbF0Jw/edit

5. The first public alpha is here! Please send feedback! I will be working on stability, performance, changing the "back" function, and adding comments.



--------------------------------------------

-- MySQL Abstract (Done in MySQL Workbench) --

http://thekarlbrown.com/mysqleerd.png 

Entity Relationship Diagram I designed for the app

EDIT: Completely redid MySQL abstract 3/7 to meet changing demands/new knowledge

--------------------------------------------

--- PHP --- 

currently: PHP complete for current needs, detailed for future goals

decided on simple username/phone number verification, and following of individuals not friending

incorporated error messages and PDO to aide in prevention of MySQL injection

todo: second version without error messages and push only (maybe not push only as no real security difference), get real UI input  from someone

--------------------------------------------


--TO-DO/Tactics for Implementation --

improve how I change trending in relation to geocoding (the current band-aide due to desire to push to alpha stage is hilarious)

figure out why appetize is crashing (likely because of location data issues) and figure out how to deal with that 

move reverse geocoding to server size depending on performance difference, required if app goes international due to hashmap

improve incorrect queries/returns of how things failed then with new information give more detail client side about what was wrong about login

set custom go/backs for sub-pages

hash password client side instead of post/get plaintext  (doubtful that risk of knowing hashing technique via reverse engineering of apk client side is worse than sending post/get that could be wiresharked but will verify before implementation)

(for example, special characters in password fields vs weaker passwords for users vs certain characters not allowed. will PHP adequately protect Database with PDO?) 

change friends to followers in followers ideas 

php adds: deal with null inputs for insert/update calls

change php inserts so that auto_increment doesn't trigger after app is working and diagnostics are complete IE add id=id+1 not autoincrements (what about holes)

retrieve categories from server instead of setting them manually

better implement text in initial screen

should we optimize the boolean array that is being bundled to set the bar filter? will it be necessary once dust settles?

how do I better handle resource allocation? when are objects destroyed? can I automate linking of data with frameworks?

fix serialization of drafts

determine if all bar methods necessary, determine if I should make leaderboard filter/access separate object

use better directory for saving files

pushing/pulling favourites upvote/downvote from server

update selected on start so leaderboard doesn't start on null

optimize sort algorithms change dataadapter containers new for minimum size and quickest content

pull minimum amount required for adapter, and object number, 

have "check for new" and "get more" buttons at top and bottom once pushing is implemented

description of all functions on welcome screen, potentially on start

search queries optimal search algorithm, starting at beginning until end, saving when minimum obtained to continue where it left off when user goes down

need advisor help on leaderboard, maybe set rule for if active within. 30 days

friends and idea lists are all optimized algorithm searches, decide how to determine if user has liked idea or not (will server return truth value, where will who has liked what be stored and how)

decide if leaderboard is real-time or at end of each day and if it has time

see what prof thinks of leaderboard pushing

figure out how I am layering fragments improperly

figure out why I cannot copy-paste

add leaderboard onclicks to user profiles

--------------------------------------------

-- coming after initial setup --

get good UI input for re-design

due to complexities of php, if ($case==0) will throw an error but stil evaluate as true then change the database in favratd.php. I need to check to see how this affects the program later, but assume we are not worrying about custom attacks atm and may be a non-issue

prompt for deletion of old drafts

view list of followers  onclick pulls you into list of followers not ideas, which then pulls into each individuals ideas (requires new data container)

bring custom picture for each user (or do away with picture in profile)

optimize views used (ie button instead of image instead of text or whatever)

view other users profiles 

add images for profiles to various displays, save profile picture to server

groxxx directed spacing

make icons out of everything possible for aesthetics, sexy fonts, and round edges

improve category spacing

ability to back up profile over the internet and save preferences online, not connected to current username and saved in downloads

optimize communication of preferences/mainactivity between fragments

implement comments

scaling based on content, device, and orientation

import image for profile picture/make it social/all that jazz

calculate your PERSONAL rank (not leaderboard) by area

verifying/security user accounts

implementing reporting ideas

bug fixing

view comments, notifications from comments

implement recently viewed ideas

attempt to move code towards more modular design

fix drawer expansion style to make it more smooth

--------------------------------------------

--- special thanks to ---

Napalm, g00s, groxx,TacticalJoke, lasserix,cbeust at #android-dev

danblack, seekwill, salle at #mysql

alphos, xyphoid. TML at #php

cmalekpour on github

Lisa Slover for excellent icon ideas