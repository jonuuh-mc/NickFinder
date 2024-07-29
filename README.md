# NickFinder

A mod/bot to automatically find nicknames on hypixel

<img src="https://github.com/user-attachments/assets/bf7f3b18-b3e5-4868-b6bb-05704442ba60" width="250"/>\
When you claim or generate nicks by clicking "USE NAME" / "TRY AGAIN", you're just sending commands:\
`/nick actuallyset <nickname> respawn` / `/nick help setrandom`.\
When you start the bot, it uses these commands to try to generate nicks forever until it finds one you want (matches a target pattern and doesn't match any filter patterns), which it then claims.\
Every time the config's nick delay time passes, the mod gets a new nickname, and every time the config's antiafk delay time passes, it will swap to a different lobby from the config's lobbymin to lobbymax (both inclusive).

The mod uses regular expression (regex) patterns to find nicks.\
There are two types of patterns, targets (types of nicks you want) and filters (types of nicks you dont want).\
For example if you want a nick containing `Batman` and no number `5` in it, you could put `Batman` in your targets and `5` in your filters.\
<sub>Note: Target patterns don't 'stack', but filter patterns do. The mod will claim a nick that matches ONE of the target patterns and doesn't match ALL of the filter patterns.\
This means if you put `Cookie` and `.{12}` (anything 12 characters long) in your targets, the mod will claim any nick that has "Cookie" OR any nick that is 12 char long, not both at once.\
But if you put the same in your filters, the mod will avoid anything with "Cookie" AND anything twelve char long.</sub>

A LOT can be said about the patterns, I chose to use regex as the way to find nicks because there are too many different nicks to try to find a specific one, doing a more general search makes more sense.\
A regex pattern is like a math expression used to differentiate between pieces of text. There are different symbols in it that mean different things.\
When you give a program a regex and some text, it reads through both character by character, checking if each character of the text matches up with the pattern.\
Either every character of the text matches the pattern (match), or it doesnt (no match)

Some useful symbols in regex:\
`[]` means "one character from within these brackets" or "a range of characters" *Examples: `[abcde]` means "one character, a, b, c, d, or e". (specifcally lowercase) `[a-e]` means the same thing.*\
`+` means "one or more of whatever the previous thing in the pattern was" *Examples: `hello+` means "hello with one or more o's at the end"*\
`?` means "one or zero of whatever the previous thing in the pattern was, so its optional" *Examples: `hello!?` means "hello with an optional ! at the end"*\
`*` means "zero or more of whatever the previous thing in the pattern was, so it could be optional or there could be 100 of them" *Examples: `he*llo` means "hello with zero or more e's"*\
`{}` with a number inside means "that number of whatever the previous thing in the pattern was" *Examples: `h+ello!{12}` means "hello with one or more h's and 12 !'s at the end"*\
`.` means "any character at all" *Examples: `[Hh]el.o` means "hello or Hello with any character replacing the second l"*\
`\d` means "any digit (0-9)"\
`^` means "the start of the text" *Examples: `^Hello` means "Hello which must be at the start of the text. the text 'ABCHello' would not match"*\
`$` means "the end of the text"\
`()` is like parenthesis in math, it just means everything inside is one "thing" *Examples: `(H[e3]llo){3}` means "Hello"/"H3llo" three times"*\
`\` means "take the next character literally if its a symbol that means something else like `+`" *Examples: `hello\.?` means "hello with an optional . at the end"*\
Theres a lot more but most of it doesn't matter. You can make and test regex here: https://regex101.com/

Ok, sorry about that. Heres why this actually matters for the mod.\
Some examples of patterns you could use:\
`/nickfinder addfilter \d` - prevents you from claiming any nicks with numbers\
`/nickfinder addfilter _` - same thing as above but with underscores\
`/nickfinder addfilter [xX]{2}$` - prevents you from claiming any nicks with "xx", "xX" "XX", or "Xx" at the end

`/nickfinder addtarget [Pp]hoenix` - claim any nick with "phoenix" or "Phoenix" case insensitive\
`/nickfinder addtarget [Pp]h[o0][e3]n[i1]x` - same as above but lets some letters be replaced with numbers\
`/nickfinder addtarget [Pp]hoenix$` - same thing but phoenix must be at the end of the nick\
`/nickfinder addtarget ^[A-Z][a-z]+$` - claim any nick with one capital letter at the start followed by one or more lowercase letters to the end (extremely rare OG/old nick format)\
`/nickfinder addtarget (^__)|(__$)` - claim any nick with two underscores at the start or end (cool slightly rare nick format)
\
\
\
Some more random stuff about nicks, i might add more later

Letters that can be replaced with numbers:\
i, l -> 1\
o -> 0\
a -> 4\
e -> 3

Some nick formats:\
"word and word" EX: True_and_Shot, Sma11AndSpeedy\
"word word word" EX: TheFuriousQueen, NiceBillyOwl\
"word word"  EX: ToxicSniper. Crafty_Aqua\
"firstname lastname year"   EX: tommyross2006, JakePrice2004\
"firstname lastinitial year"   EX: elsiem2011, florenceb2004\
"firstname num"   EX: rory333, emilia924\
"word word number" EX: ItsKai449, theseb564, TonyChief3\
"Xx words xX" EX: XxDoubleEpicxX, XxLilAndColdxX\
"__ word" / "word \__" (all letters replaced with nums?) EX: F14sh__, 0LIV3R__, l3g3nd4ry__, __STRANG3R\
"Word" (one vowel repeated, dk if it can be all lowercase) EX: Brunooo, Liooon, Siiiimon, Maaaaaxi, Snoow

[All words that can generate in a nick](https://github.com/jonuuh/NickFinder/blob/main/nick-words-final-length-alphabetical.txt)\
[Log of 1 million nicks](https://github.com/jonuuh/NickFinder/blob/main/nicks-1M.log)
