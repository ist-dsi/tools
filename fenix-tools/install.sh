#!/bin/sh
FENIX_HOME=/home/sfbs/workspace/fenix-trunk/
FENIX_WEB_FRAMEWORK_HOME=/home/sfbs/workspace/fenix-web-framework/
ant jar 
cp deploy/fenix-tools.jar $FENIX_WEB_FRAMEWORK_HOME/lib/fenix-tools.jar 
cp deploy/fenix-tools.jar $FENIX_HOME/web/WEB-INF/lib/fenix-tools.jar
