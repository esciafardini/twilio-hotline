# hotline

This is a bot that interfaces with Twilio to text my friends. <br />
There are automatic responses written in Twilio for when they respond. <br /> <br />
The file ```hotline.private``` is excluded from this Repo (for obvious reasons). <br />
The symbol p/people references a map with people's names (str) as keywords.&nbsp; <br /> <br />
/////////////////////////////////// <br /> <br />
Example p/people data structure: <br />
{"jeff" {:phone "+10001112222" :name "Jeff Hucklebug"} <br />
 "bill" {:phone "+10002223333" :name "Bill Dingus"} <br />
 "jen"  {:phone "+13332221111" :name "Jen Jarvis"}} <br /> <br />

## Usage

I use this to text people with cider-jack-in, <br />
editing the messages and evaluating the -main function from the REPL.

## License

Copyright © 2022


This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
