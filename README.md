--- readme ---

As part of my Senior project, I am developing an Android app that utilizes a MySQL database on this server to enable the sharing, group evaluation, improvement and implementation of new ideas and innovation.

This is my ChangeTheWorld android app. I am currently in the earlier beta, in fact if you are reading this it is likely that this is the first Github push ever. What does this mean?

1. The app is in the earliest of alpha. Its performance is not optimized. In fact, I have a list of things I intend on doing, bugs that need to be fixed, and what I intend to do further on in a huge list

2. I would love your feedback, and much later on in the process will need android devices to test on (in addition to whatever I will be testing)

3. If you are interested in helping me setting up the categorys and subcategorys/check out docs.google.com/document/d/1GVSz_fBI7GjAc4JJta39_EX0B0SVpZ5PUdZyDpbF0Jw/edit

4. There are a ton of placeholders for features to be added later or backend that is not yet implemented

5. There is currently an emulator for the app here : https://appetize.io/app/rtkyh4gvg02x56uzeffx1349k4 Please use 100%, Nexus 7, as I have not moved development to any other platforms as of yet

--TO-DO/Tactics for Implementation --

better implement text in initial screen

should we optimize the boolean array that is being bundled to set the bar filter? will it be necessary once dust settles?

how do I better handle resource allocation? when are objects destroyed? can I automate linking of data with frameworks?

fix serialization of drafts

determine if all bar methods necessary, determine if I should make leaderboard filter/access separate object

use better directory for saving files

pushing/pulling favorites upvote/downvote from server

update selected on start based on IdeaPage so tags are selected and values are passed and leaderboard doesn't start on null

for filters, have it so highlighting only occurs when you hit ok (not cancel)

optimize sort algorithms change dataadapter containers new for minimum size and quickest content

pull minimum amount required for adapter, and object number, 

have "check for new" and "get more" buttons at top and bottom once pushing is implemented

description of all functions on welcome screen, potentially on start

for initial screen: not seen friends ideas that are positive  then most thumbs of day positive

categories has no minimum, queries category and subcategory first, most recent selected, this month

search queries optimal search algo, starting at beginning until end, saving when minimum obtained to continue where it left off when user goes down

need advisor help on leaderboard, maybe set rule for if active within. 30 days

friends and idea lists are all optimized algo searches, decide how to determine if user has liked idea or not (will server return truth value, where will who has liked what be stored and how)

decide if its optimal to display subselections by references to positions of ideablocks or create ideablock reference OR BOTH

decide if leaderboard is real-time or at end of each day and if it has time

consult with algorithms expert for current algorithm tactics

crowd-source the category creation

see what prof thinks of leaderboard pushing

figure out how im layering fragments improperly

figure out why I cannot copy-paste

sync favs with database

add leaderboard onclicks to user profiles

-- coming after initial setup --

prompt for deletion of old drafts

view list of friends  onclick pulls you into user or list of user ideas, and you view list of friends not a list of your friends ideas

optimize views used (ie button instead of image instead of text or whatever)

view other users profiles 

add images for profiles to various displays, save profile picture to server

groxxx directed spacing

make icons out of everything possible for prettyness, sexy fonts, and round edges

improve category spacing

ability to back up profile over the internet and save preferences online, not connected to current username and saved in downloads

optimize communication of preferences/mainactivity between fragments

implement comments

scaling based on content, device, and orientation

optimizing mysql transmission

import image for profile picture/make it social/all that jazz

calculate your PERSONAL rank (not leaderboard) by area

verifying/security user accounts

implementing reporting ideas

bug fixing

view comments, notifications from comments

implement recently viewed ideas

attempt to move code towards more modular design

fix drawer expansion style to make it more smooth

--- special thanks to ---

Napalm, g00s, groxx,TacticalJoke, lasserix,cbeust at #android-dev

cmalekpour on github