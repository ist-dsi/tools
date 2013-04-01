#!/bin/sh
FENIX_HOME=/home/sfbs/workspace/git/fenix-trunk
BENNU_HOME=/home/sfbs/workspace/git/bennu-core/myorg
ant clean-all jar 
cp deploy/fenix-tools.jar $FENIX_HOME/web/WEB-INF/lib/fenix-tools.jar
cp deploy/fenix-tools.jar $BENNU_HOME/web/WEB-INF/lib/fenix-tools.jar
