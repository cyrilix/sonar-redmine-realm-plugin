sonar-redmine-realm-plugin
==========================

Sonar plugin to authenticate with redmine REST api

# Installation #

1. Install the plugin through the Update Center or download it into the *SONARQUBE_HOME/extensions/plugins* directory.
2. Restart the SonarQube server.

# Usage #

1. Configure the Redmine plugin by editing the SONARQUBE_HOME/conf/sonar.properties file (see below)
2. Restart the SonarQube server and check the log file for:
    * INFO org.sonar.INFO Security realm: REDMINE
    * Log into SonarQube

# Configuration #

Property	| Description			         | Default value | Mandatory | Example
:---------- | :----------------------------- | :-----------: | :-------: | :---------------------
redmine.url	| URL of the Redmine server.	 | None		     |   Yes	 | http://localhost:3000
redmine.key	| api_key of priviledged account | None	         |   Yes     |                      

