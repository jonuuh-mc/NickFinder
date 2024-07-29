# NickFinder

A mod/bot to automatically find nicknames on hypixel

<img src="https://github.com/user-attachments/assets/bf7f3b18-b3e5-4868-b6bb-05704442ba60" width="200"/>\
When you claim or generate nicks by clicking "USE NAME" / "TRY AGAIN", you're just sending commands: `/nick actuallyset <nickname> respawn` / `/nick help setrandom`.\
When you toggle the bot (default keybind is `=`), it uses these commands to try to generate nicks forever until it finds one you want (matches a target pattern and doesn't match any filter patterns), which it then claims.

---

#### About the config
- Targets: Regex patterns for nicks you want to find
- Filters: Regex patterns for nicks you want to avoid
- Nick delay: Time between 'generating' each nick
- AFK delay: Time between antiAFK (swaps to a different random lobby from lobbyMin to lobbyMax)
- Lobby min: Min lobby number you can swap to (inclusive)
- Lobby min: Max lobby number you can swap to (inclusive)
  
---

#### About regex
The mod uses regular expression (regex) patterns to find nicks, but you don't need to know anything about them to use the mod. You'll just be able to do more with the mod if you do.\
For example if you just want a nick containing `Batman` and not containing `5` in it, you could put `Batman` in your targets and `5` in your filters.\
<sub>Target patterns don't 'stack', but filter patterns do. The mod will claim a nick that matches ONE of the target patterns and doesn't match ALL of the filter patterns.\
This means if you put `Cookie` and `.{12}` (anything 12 characters long) in your targets, the mod will claim any nick that has "Cookie" OR any nick that is 12 char long, not both at once.\
But if you put the same in your filters, the mod will avoid anything with "Cookie" AND anything 12 char long.</sub>

I chose to use regex as the way to find nicks because there are too many different nicks to try to find a really specific one, regex lets you do a more general search.\
A regex pattern is like a math expression used to differentiate between pieces of text. There are different symbols in one that mean different things.\
When you give a program a regex and some text, it reads through both character by character, checking if each character of the text matches up with the pattern.\
Either every character of the text matches the pattern (match), or it doesnt (no match)

---

#### Useful symbols in regex
* `[]` means "one character from within these brackets" or "a range of characters" *Examples: `[abcde]` means "one character, a, b, c, d, or e". (specifcally lowercase) `[a-e]` means the same thing.*
* `+` means "one or more of whatever the previous thing in the pattern was" *Examples: `hello+` means "hello with one or more o's at the end"*
* `?` means "one or zero of whatever the previous thing in the pattern was, so its optional" *Examples: `hello!?` means "hello with an optional ! at the end"*
* `*` means "zero or more of whatever the previous thing in the pattern was, so it could be optional or there could be 100 of them" *Examples: `he*llo` means "hello with zero or more e's"*
* `{}` with a number inside means "that number of whatever the previous thing in the pattern was" *Examples: `h+ello!{12}` means "hello with one or more h's and 12 !'s at the end"*
* `.` means "any character at all" *Examples: `[Hh]el.o` means "hello or Hello with any character replacing the second l"*
* `\d` means "any digit (0-9)"
* `^` means "the start of the text" *Examples: `^Hello` means "Hello which must be at the start of the text. the text 'ABCHello' would not match"*
* `$` means "the end of the text"
* `()` is like parenthesis in math, it just means everything inside is one "thing" *Examples: `(H[e3]llo){3}` means "Hello"/"H3llo" three times"*
* `\` means "take the next character literally if its a symbol that means something else like `+`" *Examples: `hello\.?` means "hello with an optional . at the end"*\
Theres a lot more but most of it doesn't matter. You can make and test regex here: https://regex101.com/

---

#### Some examples of regex patterns you could use
* `/nickfinder addfilter \d` - prevents you from claiming any nicks with numbers
* `/nickfinder addfilter _` - same thing as above but with underscores
* `/nickfinder addfilter [xX]{2}$` - prevents you from claiming any nicks with "xx", "xX" "XX", or "Xx" at the end of the nick

* `/nickfinder addtarget [Pp]hoenix` - claim any nick with "phoenix" or "Phoenix" case insensitive
* `/nickfinder addtarget [Pp]h[o0][e3]n[i1]x` - same as above but lets some letters be replaced with numbers
* `/nickfinder addtarget [Pp]hoenix$` - same thing but phoenix must be at the end of the nick
* `/nickfinder addtarget ^[A-Z][a-z]+$` - claim any nick with one capital letter at the start followed by one or more lowercase letters to the end (extremely rare OG/old nick format)
* `/nickfinder addtarget (^__)|(__$)` - claim any nick with two underscores at the start or end (another rare nick format, 1 in a few thousand?)

---

## Other stuff

[All* words that can generate in a nick](https://github.com/jonuuh/NickFinder/blob/main/nick-words-final-length-alphabetical.txt) <sub>*maybe</sub>\
[Log of 1 million nicks](https://github.com/jonuuh/NickFinder/blob/main/nicks-1M.log)

### Is this allowed?
No 100% not, use it at your own risk. It would be obvious to any admin looking at your logs.\
<sub>I've probably botted through 2,000,000+ nicks at this point with no ban so it seems unlikely</sub>

### Recommended place to afk?
I think main lobbies 1-4 are the best place right now, the main lobby has an afk timer of 5mins (lower in many other lobbies) and lobbies 1-4 usually have stacks of players sitting in the spawnpoint.\
All anyone would see is a book flashing in the hand of one of the stacked players at spawn.

### Letters in a nick that can be replaced with numbers
* i/l <-> 1
* o <-> 0
* a <-> 4
* e <-> 3

### Possible nick formats? <sub>(just ideas, not accurate or complete)</sub>
* \<WordWordWord> EX: TheFuriousQueen, NiceBillyOwl, Sma11AndSpeedy True_and_Shot
* \<WordWord> EX: ToxicSniper, Crafty_Aqua, bluewarrior
* \<FirstnameLastnameYear> EX: tommyross2006, JakePrice2004
* \<FirstnameLastinitialYear> EX: elsiem2011, florenceb2004
* \<FirstnameNumber> EX: rory333, emilia924
* \<WordWordNumber> EX: ItsKai449, theseb564, TonyChief3
* \<XxWordsxX> EX: XxDoubleEpicxX, XxLilAndColdxX
* \<\_\_Word> / <Word\_\_> (all letters replaced with nums?) EX: F14sh\_\_, 0LIV3R\_\_, l3g3nd4ry\_\_, \_\_STRANG3R
* \<Word> (one vowel repeated, don't think it can be all lowercase?) EX: Brunooo, Liooon, Siiiimon, Maaaaaxi, Snoow
  
<sub>OG/old nick format rarity seems to be about 1 in a few 100k, that might change though if more get claimed,\
part of why they're so rare is probably because most are already claimed by old/inactive players</sub>

### Repeating nicks?
If you've noticed that sometimes you'll run into the same nick multiple times while botting, its probably because of [caching](https://www.google.com/search?q=what+is+caching).\
An example to explain what caching is: If you ask the server to generate you 1000 nicks two times, the second time would be a lot eaiser for the server if it stored (cached) some of the nicks from the first time, and reused them in the second 1000 nicks.\
I don't know much specifically about it (like how many nicks can be in your cache, when they get added or removed from the cache, etc) but this is probably why you find repeat nicks.\
<sub>this also assumes that nicks are 'generated' at all, it seems way less likely but its possible they're all already pre-generated and read from a big list</sub>
