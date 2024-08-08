# NickFinder

A mod/bot to automatically find nicknames on hypixel

<img src="https://github.com/user-attachments/assets/bf7f3b18-b3e5-4868-b6bb-05704442ba60" width="200"/>\
When you claim or generate nicks normally by clicking "USE NAME" / "TRY AGAIN" in a book, you're just sending commands: `/nick actuallyset <nickname> respawn` / `/nick help setrandom`.\
\
When you toggle the bot (default keybind is `=`), it will try to generate nicks with `/nick help setrandom` forever until you toggle it again or it finds one you're looking for (matches a target pattern and doesn't match any filter patterns)\
If it finds a nick you're looking for, it will claim it with `/nick actuallyset <nickname> respawn` and then toggle itself off.
> <sub> The bot will also turn off if you disconnect from the server or are sent to limbo while it's running.\
> You can get sent to limbo if you send commands too fast (nick delay) or your wifi / the server lagspikes </sub>
---

### About the config/commands

| Setting    | Command                                                       | Description                                                                        |
| ---------- | ------------------------------------------------------------- | ---------------------------------------------------------------------------------- |
| Targets    | `/nickfinder` (`addTarget` / `removeTarget` / `clearTargets`) | Regex patterns for nicks you want to find                                          |
| Filters    | `/nickfinder` (`addFilter` / `removeFilter` / `clearFilters`) | Regex patterns for nicks you want to avoid                                         |
| Nick delay | `/nickfinder setNickDelay`                                    | Time between 'generating' each nick                                                |
| AFK delay  | `/nickfinder setAFKDelay`                                     | Time between antiAFK (swaps to a different random lobby from lobbyMin to lobbyMax) |
| Lobby min  | `/nickfinder setLobbyMin`                                     | Min lobby number you can swap to                                                   |
| Lobby min  | `/nickfinder setLobbyMax`                                     | Max lobby number you can swap to                                                   |
  
---

### About regex
<ins>The mod uses regular expression (regex) patterns to find nicks, but you don't need to know anything about them to use the mod. You'll just be able to do a lot more with the mod if you do.</ins>\
\
For example if you just want a nick containing `Batman` and not containing `5` in it, you could put `Batman` in your targets and `5` in your filters.
> <sub> Target patterns don't 'stack', but filter patterns do. The mod will claim a nick that does match ONE of the target patterns and doesn't match ANY of the filter patterns.\
This means if you put `Cookie` and `.{12}` (anything 12 characters long) in your targets, the mod will claim any nick that has "Cookie" OR any nick that is 12 char long, not both at once.
But if you put the same in your filters, the mod will avoid anything with "Cookie" AND anything 12 char long. </sub>

\
I chose to use regex as the way to find nicks because there are too many different nicks to try to find a really specific one, regex lets you do a more general search.\
A regex pattern is like a math expression used to differentiate between pieces of text. There are different symbols in one that mean different things.\
When you give a program a regex and some text, it reads through both character by character, checking if each character of the text matches up with the pattern.\
Either every character of the text matches up with the pattern, or it doesnt, there's no in between.

---

### Common symbols in regex

Symbol | Description | Example patterns | Example description
------ | ----------- | ---------------- | -------------------
`[]` | One character from within the list/range in these brackets                                                    | `[abcde]` / `[a-e]` | One character, a, b, c, d, or e. (specifcally lowercase)
`+`  | One or more of whatever the previous 'thing' in the pattern was                                               | `hello+`            | "hello" with one or more o's
`?`  | One or zero of whatever the previous 'thing' in the pattern was (aka optional)                                | `hello!?`           | "hello" with an optional ! after the o
`*`  | Zero or more of whatever the previous 'thing' in the pattern was (aka optional or there could be 100 of them) | `He*llo`            | "Hello" with zero or more e's
`{}` | A number in the brackets: That number of whatever the previous thing in the pattern was                       | `hello!{12}`        | "hello" with 12 !'s after the o
`.`  | Any character at all                                                                                          | `[Hh]el.o`          | "hello" or "Hello" with any character replacing the second l
`\d` | Any digit (0-9)                                                                                               |                     |
`^`  | The start of the text                                                                                         | `^Hello`            | "Hello" which MUST be at the start of the text. 'ABCHello' would not match this pattern, but "HelloABC" would
`$`  | The end of the text                                                                                           | `Hello$`            | Similar to above ^ but the opposite
`()` | Like parenthesis in math, means everything inside is one 'thing'                                              | `(H[e3]llo){3}`     | "Hello"/"H3llo" three times
`\|` | One 'thing' OR the other                                                                                      | `hello\|hey`        | "hello" OR "hey"

\
There's a lot more but most of it doesn't matter. You can make and test regex here: https://regex101.com/

---

### Some examples of target/filter patterns you could use

| Command                                      | Description
| -------------------------------------------- | ----------------------------------------------------------------------------------- 
| `/nickfinder addTarget [Pp]hoenix`           | Claim any nick with "phoenix" or "Phoenix"
| `/nickfinder addTarget [Pp]h[o0][e3]n[i1]x`  | Same as above but some letters can be replaced with numbers
| `/nickfinder addTarget [Pp]hoenix$`          | Claim any nick with "phoenix" or "Phoenix" at the end of the nick
| `/nickfinder addTarget ^[A-Z][a-z]+$`        | Claim any nick with one capital letter at the start followed by one or more lowercase letters to the end (OG/old nick format)
| `/nickfinder addTarget (^_{1,2})\|(_{1,2}$)` | Claim any nick with between one and two underscores at the start or end (another rare nick format)
| -                                            | -
| `/nickfinder addFilter \d`                   | Prevents you from claiming any nicks with numbers
| `/nickfinder addFilter _`                    | Prevents you from claiming any nicks with underscores
| `/nickfinder addFilter xX$`                  | Prevents you from claiming any nicks with "xX" at the end of the nick (rules out a specific nick format)

---

## Other stuff

[All* words that can generate in a nick](https://github.com/jonuuh/NickFinder/blob/main/nick-words-final-length-alphabetical.txt) <sub>*probably close to if not perfect as long as staff don't add/remove any</sub>\
[Log of 1 million nicks](https://github.com/jonuuh/NickFinder/blob/main/nicks-1M.log)

### Is this allowed?
No 100% not, use it at your own risk. It should be obvious to any staff looking at your logs.\
(or total nick generation logs/data across the whole server, if they have those)
> <sub>This mod has probably been used to bot through 3 millon+ nicks at this point, ~2 million on my account alone with no bans to anyone so it seems unlikely</sub>

### Recommended place to afk?
I think main lobbies 1-4 are the best place right now, the main lobby has an afk timer of 5mins (lower in many other lobbies) and lobbies 1-4 usually have stacks of players sitting in the spawnpoint.\
All someone (non-staff) watching should see is a book flashing in the hand of one of the stacked players at spawn.

### Letters in a nick that can be replaced with numbers

Letter | Number
------ | ------ 
`l`    | `1`
`i`    | `1`
`o`    | `0`
`a`    | `4`
`e`    | `3`

### Possible nick formats? <sub>just ideas, not accurate or complete</sub>

| Format                     | Examples                                                             | Notes                                                                        
| -------------------------- | -------------------------------------------------------------------- | --------------------------------------------------------------------- 
| `WordWordWord`             | `TheFuriousQueen` `NiceBillyOwl` `Sma11AndSpeedy` `True_and_Shot`    |
| `WordWord`                 | `ToxicSniper` `Crafty_Aqua` `bluewarrior` `max_mag`                  |
| `XxWordsxX`                | `XxDoubleEpicxX` `XxLilAndColdxX`                                    |
| `FirstnameLastnameYear`    | `tommyross2006` `JakePrice2004`                                      |
| `FirstnameLastinitialYear` | `elsiem2011` `florenceb2004`                                         |
| `FirstnameNumber`          | `rory333` `emilia924`                                                |
| `WordWordNumber`           | `ItsKai449` `theseb564` `TonyChief3`                                 |
| `__Word` / `Word__`        | `F14sh__` `l3g3nd4ry__` `B14d3__` `0LIV3R__`                         | ~1/10k rarity? chance of nums replacing letters is way higher for this format, also this & below format are the only ones that can have fully caps words
| `_Word` / `Word_`          | `_W0R1D` `_3m3rald` `HUNT3R_`                                        | ~1/10k rarity? chance of nums replacing letters is way higher for this format
| `Word`                     | `Brunooo` `Liooon` `Raaaanger` `Siiimon` `Bruuuh` `Snooow` `Maaaaxi` | the OG/old nick format: one nick word with a vowel repeated. don't think it can be all lowercase/uppercase or have nums replacing letters. avg rarity seems to be around 1/200k. part of why they're so rare might be because many are already held by old inactive players?

### Repeating nicks?
If you've noticed that sometimes you'll run into the same nick multiple times, its probably because of [caching](https://www.google.com/search?q=what+is+caching).\
An example to explain what caching is: If you ask the server to generate you 1000 nicks two times, the second time would be a lot eaiser for the server if it stored (cached) some of the nicks from the first time, and reused them in the second 1000 nicks.\
I don't know much specifically about it (like how many nicks can be in your cache, when they get added or removed from the cache, etc) but this is probably why you find repeat nicks.\
It's really noticable with the OG nicks because of how rare they are, after finding and claiming one, if you then quickly try to find a different one you can end up finding the same OG nick you already have 10 times in a row\
<sub>This also assumes that nicks are 'generated' at all, it seems way less likely but its possible they're all already pre-generated and read from a big list. I can't really be certain of much with how nicks 'generate', everything I know is stuff I've figured out by trial and error</sub>
