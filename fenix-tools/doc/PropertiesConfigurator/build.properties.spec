# This is an example of a properties specification file...
# The format of the specification is
# #any comment - ignored...
# # @metadata1=value1 for each metadata property - it may even be in multiple lines... 
# # it must start with an @ sign for each property and after specifying the first metadata
# # all subsequent lines will be considered part of the metadata
# # (so up to this line this will be considered the value for metadata1)
# #
# # Next will present the base metadata for all property readers:
# # @message=The message to present to the user on data collection
# # @type=The type of the property (further about types below)
# # @required=[yes/no]/[true/false]/[1/0]/[y/n] if the user must enter this property (default=y)
# # @persist=[yes/no]/[true/false]/[1/0]/[y/n] if this property should be saved to the generated file (default=y)
# # @dependency=other.property.name=* (this property depends on other.property.name being defined... no matter the value)
# # @dependency=other.property.name=value (this property depends on other.property.name having the defined value)
# # 
# 
# Some properties might generate other properties based on the values specified...
# For those types of properties, the additional metadata required is:
# # @generated.message=The message to present to the user on data collection where ${value} is replaced by the base property value/values
# # @generated.key=The property key to be generated where ${value} is replaced by the base property value/values
# # @generated.type=The type of the property (further about types below)
# # @generated.required=[yes/no]/[true/false]/[1/0]/[y/n] if the user must enter this property (default=y)
# # @generated.persist=[yes/no]/[true/false]/[1/0]/[y/n] if this property should be saved to the generated file (default=y)
# # @generated.defaultValue=The default value to be used if no previous run was made...
#
# in addition, you shoud specify any dependent property metadata as by its own type
# prepended by the keyword "generated."
#
# Each property must have a type. To read each type, the class 
# pt.linkare.ant.propreaders.PropertyReaderManager tries to localize a class
# that implements the interface pt.linkare.ant.propreaders.PropertyReader by instrospection 
# from the following class names: 
# 1. From a system property called "property.reader.<type>" 
# 2. From the package  pt.linkare.ant.propreaders.<Type>PropertyReader
# 
# If you want to extend the type system you may implement PropertyReader interface or extends AbstractPropertyReader
#
# The current types available are:
# type=string 
#   additional metadata: minLength=the minimum string length (default=1)
# 
# type=boolean
#   additional metadata: yesOption=the "true" option string (default=y)
#                        yesOptionValue=the value to use for a "true" response (default=1)
#                        noOption= the "false" option string (default=n)
#						 noOptionValue=the value to use for a "false" response (default=0)
#
#   
# type=file
#   additional metadata: validateFile=yes/no (should the file exist?)
#                        createFile=Should the file be created if it does not exist? (validateFile must be set to "no")
#
# type=hostname  
#   additional metadata: validate=yes/no (must this be a valid hostname - resolvable to at least one IP address)
#   
# type=integer
#   additional metadata: min=The integer minimum value allowed (default=Integer.MIN_VALUE)
#                        max=The integer maximum value allowed (default=Integer.MAX_VALUE)
#   
# 
# type=langString  - A list of languages to choose from (menu-like) - defaults to a list of all available locales
#  	additional metadata: langs={"lang_1","lang_2","lang_3"} (Languages should be language ISO Codes : pt, en, es, it, fr
# 
# type=menu - A menu to choose from an option
#   additional metadata: options={"option 1","option 2","option 3"} (required)
#                        optionsValues={"option value 1","option value 2","option value 3"} (default=index of the chosen option - 0 based)
#   
# type=multipleOptions - A menu to choose from several options (comma separated string of values)
#   additional metadata: options={"option 1","option 2","option 3"} (required)
#                        optionsValues={"option value 1","option value 2","option value 3"} (default=index of the chosen option - 0 based)
# 
# type=password 
#   additional metadata: minLength=the minimum password length (default=1)
#
#
# type=path
#   additional metadata: validatePathh=yes/no (should the path exist?)
#                        createPath=Should the path be created if it does not exist? (validatePath must be set to "no")
#
# type=stringReplace - it gets a string from input but then it replaces it in replaceString and writes out the replaceString as the value
#   additional metadata: replaceString=The string to replace in ${0} is replaced by the actual value
#						 minLength=The minimum length required (default=1)
#
# type=url
#   additional metadata: validate=y/n (must the url be validated it is a valid url string spec? -  defaults to no)
#	
# type=default - just uses the default value specified for the property... does not ask user for info
#   additional metadata: none
#
# type=nullableDefault - also uses the default value specified... but it does not complain of nulls
#   additional metadata: none
#
# type=hostnameList - A list of hostnames
#   additional metadata: @validate=Must be a valid hostname resolvable by DNS
#
# The rest of this file is an example to start with						 
#
#						 
#
#						 
#
#------------------------------------------------------------------------------
# General Configuration
#------------------------------------------------------------------------------
#
# @message = The host the application will run on
# @type = hostname
# @required = yes
# @persist = yes
# @validate = yes
http.host=localhost

# The required and persist assume value y, so we may omit it
# @message = The http port the application will run on
# @type = integer
# @min = 1
# @max = 65535
http.port=8080

# @message = The name of the application
# @type = string
app.name=fenix

# @message = application virtual context path
# @type = string
app.context=fenix

# @message = Manager Filter Pattern
# @type = menu
# @options = {"Nothing"}
# @optionsValues = {"nothing"}
manager.filter.pattern=nothing


# @message = Local application index page link
# @type = string
index.html.link.localhost=siteMap.do

# @message = Local application domain index page link
# @type = string
index.html.link.localhost.localdomain=siteMap.do

# @message = Salad21 application index page link
# @type = string
index.html.link.salad21=loginPage.jsp

# @message = Salad21 ist.utl.pt application index page link
# @type = string
index.html.link.salad21.ist.utl.pt=loginPage.jsp

# @message = Login page url
# @type = url
# @validate = yes
login.page=https://localhost:8080/ciapl/privado

# @message = Institution main url
# @type = url
# @validate = yes
institution.url=http://www.ist.utl.pt

# Only required now is no, so please define it, as the default is yes
# @message = Institution project directory
# @type = path
# @required = no
# @validatePath = yes
institution.project=/home/gedl/workspace/fenix-head

# @message = Default language for application
# @type = langString
language=pt

#------------------------------------------------------------------------------
# Web Container Configuration
#------------------------------------------------------------------------------
#
# @message = Relative path of the web application
# @type = path
app.path=/${app.name}

# This property should not be persisted to build.properties, as it could be a security hazard
# @message = Tomcat manager's password
# @type = password
# @required = yes
# @persist = no
# @dependency = manager.username=*
manager.password=admin

# Note that there is a dependency on a value defined for manager.url
# although this property is required, it will only be asked for if manager.url 
# has been defined and it is not null
# @message = Tomcat manager's username
# @type = string
# @required = yes
# @persist = no
# @dependency = manager.url=*
manager.username=admin

# Note that manager url is optional, so this makes the other previous two optional if
# this one is optional
# @message = Tomcat manager's url
# @type = url
# @required = no
# @persist = no
# @validate = yes
manager.url=http://localhost:8080/manager

#------------------------------------------------------------------------------
# Data Repository Configuration
#------------------------------------------------------------------------------
#
# @message = Persistence support type
# @type = menu
# @options = {"OJB Persistence support","Versioned Objects Persistence Support","Delegate Persistence Support"}
# @optionsValues = {"net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB","net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsPersistenceSupport","net.sourceforge.fenixedu.persistenceTier.delegatedObjects.DelegatePersistenceSupport"}
default.persistenceSupport=net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB

# @message = Fenix Database name
# @type = string
db.name=fenix

# @message = Database username
# @type = string
db.user=fenix

# @message = Database password
# @type = string
# @persist = no
db.pass=fenix

# @message = Database location
# @type = string
db.alias=//localhost:3306/${db.name}?useUnicode=true&amp;characterEncoding=latin1


#------------------------------------------------------------------------------
# Allowed Roles Configuration - Security area
#------------------------------------------------------------------------------
#
#  filter.hostnames: comma seperated list of hostnames. The specified hostname
#                    can is expected to be anything following the http:// 
#                    string.
#  filter.hostname.<hostname>: comma seperated list of RoleTypes that are to
#                              be provided by the server <hostname>.
#
# @message = The hostnames under which this application will be made available for portal filters availability
# @type = hostnameList
# @validate = true
# @required=true
# @persist=true
# @validate=true
# @generated.message=Please choose the available portals for hostname ${value}
# @generated.type=multipleOptions
# @generated.required=true
# @generated.persist=true
# @generated.key=filter.hostname.${value}
# @generated.options={"Person","Student","Teacher","Timetable Manager","Master Degree Candidate","Master Degree Administrative Office","Treasury","Coordinator","Employee","Assiduousness Management","Management","Degree Administrative Office","Credits Management","Department Credits Management","Erasmus","Degree Administrative Office (Super User)","Scientific Council","Administrator","Operator","Seminaries Coordination","Website Management","Grant Owner","Grant Owner Manager","Department Member","Department Administrative Office","Planning and Studies Administrative Office (GEP)","Directive Council","Delegate","First time Student","Projects Management","Institutional Projects Management","Bologne Process Management","Content Mng. System Manager","Space Manager", "Researcher", "Pedagogical Council", "Alumni"}
# @generated.optionsValues={"PERSON","STUDENT","TEACHER","TIME_TABLE_MANAGER","MASTER_DEGREE_CANDIDATE","MASTER_DEGREE_ADMINISTRATIVE_OFFICE","TREASURY","COORDINATOR","EMPLOYEE","MANAGEMENT_ASSIDUOUSNESS","MANAGER","DEGREE_ADMINISTRATIVE_OFFICE","CREDITS_MANAGER","DEPARTMENT_CREDITS_MANAGER","ERASMUS,DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER","SCIENTIFIC_COUNCIL","ADMINISTRATOR","OPERATOR","SEMINARIES_COORDINATOR","WEBSITE_MANAGER","GRANT_OWNER","GRANT_OWNER_MANAGER","DEPARTMENT_MEMBER","DEPARTMENT_ADMINISTRATIVE_OFFICE","GEP","DIRECTIVE_COUNCIL","DELEGATE","FIRST_TIME_STUDENT","PROJECTS_MANAGER","INSTITUCIONAL_PROJECTS_MANAGER","BOLONHA_MANAGER","CMS_MANAGER,SPACE_MANAGER", "RESEARCHER", "PEDAGOGICAL_COUNCIL", "ALUMNI"}
# @generated.defaultValue={"PERSON","STUDENT","TEACHER","TIME_TABLE_MANAGER","MASTER_DEGREE_CANDIDATE","MASTER_DEGREE_ADMINISTRATIVE_OFFICE","TREASURY","COORDINATOR","EMPLOYEE","MANAGEMENT_ASSIDUOUSNESS","MANAGER","DEGREE_ADMINISTRATIVE_OFFICE","CREDITS_MANAGER","DEPARTMENT_CREDITS_MANAGER","ERASMUS,DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER","SCIENTIFIC_COUNCIL","ADMINISTRATOR","OPERATOR","SEMINARIES_COORDINATOR","WEBSITE_MANAGER","GRANT_OWNER","GRANT_OWNER_MANAGER","DEPARTMENT_MEMBER","DEPARTMENT_ADMINISTRATIVE_OFFICE","GEP","DIRECTIVE_COUNCIL","DELEGATE","FIRST_TIME_STUDENT","PROJECTS_MANAGER","INSTITUCIONAL_PROJECTS_MANAGER","BOLONHA_MANAGER","CMS_MANAGER,SPACE_MANAGER", "RESEARCHER", "PEDAGOGICAL_COUNCIL", "ALUMNI"}
filter.hostnames=localhost,localhost.localdomain


#------------------------------------------------------------------------------
# End of build.properties.spec file
#------------------------------------------------------------------------------
