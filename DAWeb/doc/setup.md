	/*
	  DA-NRW Software Suite | ContentBroker
	  Copyright (C) 2013 Historisch-Kulturwissenschaftliche Informationsverarbeitung
	  Universität zu Köln
	
	  This program is free software: you can redistribute it and/or modify
	  it under the terms of the GNU General Public License as published by
	  the Free Software Foundation, either version 3 of the License, or
	  (at your option) any later version.
	
	  This program is distributed in the hope that it will be useful,
	  but WITHOUT ANY WARRANTY; without even the implied warranty of
	  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	  GNU General Public License for more details.
	
	  You should have received a copy of the GNU General Public License
	  along with this program.  If not, see <http://www.gnu.org/licenses/>.
	*/


## Prerequisites

* Oracle Java > 1.6 (1.7 prooved for DA-WEB)
* Tomcat6 or Tomcat7 
* Grails 2.3.8
* phantomJS for functional testing
	
## Configure Runtime Settings of DA-Web

Several runtime settings are needed by DA-Web. All the parameters needed for the app have to reside under 
the Tomcat Server's home in folder .grails, assuming the Tomcat's servers home at /home/tomcat/, there must be a file
called /home/tomcat/.grails/daweb3_properties.groovy. 
Most of the parameters are the same as in config.properties of ContentBroker. 
A documented template can be found here: [daweb3_properties.groovy](daweb3_properties.groovy.dev)
	
## Build DA-Web WAR

In normal build processes this is done automatically by the install processes called in
the maven build process. If you want to build DA-Web as isolated project, you will need 
to have GRAILS installed on your command line, the project itself is mavenized. 

Builds without having a related build of CB are strongly discouraged, while the both 
applications share the same model. 

The command 
<pre>mvn install</pre>
war will build the target file for you. 
Pay attention to config files! After the frist checkout some "magic" Script of maven grails plugin may alter them. Please consider a git reset hard after first checkout!

### Deploy and Running DAWeb locally

The DaWeb interface could be executed locally with command 
<pre>mvn grails:run-app</pre>

## Encode Database Password 

To encode your own DB Password for production, you must have a groovy compiler (and at least a checkout of the class) run

    groovy grails-app/utils/de/uzk/hki/da/utils/DESCodec.groovy <your password>

