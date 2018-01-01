# sbt
## windows:
SBT_OPTS="-Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M"
java $SBT_OPTS -jar "c:\dev\cygwin64\home\admin\bin\sbt-launch.jar" "$@"

## ubuntu:
sud apt install sbt


# bower


# redis
wget http://download.redis.io/redis-stable.tar.gz
tar xvzf redis-stable.tar.gz
cd redis-stable
make
make test
sudo make install

