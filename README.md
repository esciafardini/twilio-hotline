# hotline

This is a bot that interfaces with Twilio to text my friends. <br />
There are automatic responses written in Twilio for when they respond. <br /> <br />
The file ```hotline.private``` is excluded from this Repo (for obvious reasons). <br />
The symbol p/people references a map<br />
The symbol p/phone-numbers references clojure.set/map-invert of p/people.<br />
<br /> 
 p/people data structure:
 ```clj
{"+14445557777" "Jeff Jeffreys"
 "+14445559999" "Amanda Nicoleson"
 "+13105557700" "Rich Richies"}
 ```

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
