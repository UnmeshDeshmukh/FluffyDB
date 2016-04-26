export SVR_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo "** starting server from ${SVR_HOME} **"

echo server home = $SVR_HOME

JAVA_MAIN='server.MessageApp'
JAVA_ARGS_1="$1"
JAVA_ARGS_2="$2"
echo -e "\n** config: ${JAVA_ARGS} **\n"

# superceded by http://www.oracle.com/technetwork/java/tuning-139912.html
JAVA_TUNE='-server -Xms500m -Xmx1000m'


java ${JAVA_TUNE} -cp .:${SVR_HOME}/lib/'*':${SVR_HOME}/bin ${JAVA_MAIN} ${JAVA_ARGS_1} ${JAVA_ARGS_2} 
