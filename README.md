# Weather Planner

### A web application that allows people to interactively look-up and store weather based off of various search criteria.

## To run the app:
- Ensure you have Maven installed.
- Run the command `mvn jetty:run` and go to `https://localhost`
- Alternatively, there are run configurations for Eclipse included in the repo

#### Note:
- ensure no other apps are currently running on port 80 on your computer
- http is not supported. the app only supports https through a self signed cert
	- this may cause problems with chrome/safari/other browsers. ensure that your security features allow for untrusted certs from localhost apps

### Features
- Base weather page
	- allow users to look up current weather by location
- Vacation planning page
	- allow users to look up potential vacations by weather criteria
- Activities page
	- allow users to look up where they may be able to do their favorite recreational activities
- Analysis page
	- allow users to interact with locations they have stored to their history

### Technology Used
- Java
- Maven
- Jetty
- SQLite
- jQuery
- Vanilla JS
- HTML/CSS

### Developers
- Mark Kinol
- Drew Cutchins
- Mike Delucia
- Brighton Balfrey

