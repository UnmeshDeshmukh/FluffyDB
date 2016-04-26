export CLIENT_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo "** starting server from ${CLIENT_HOME} **"

echo client home = $CLIENT_HOME

JAVA_MAIN='test.TestClientAPI'
JAVA_ARGS_1="$1"
JAVA_ARGS_2="$2"
JAVA_ARGS_3="$3"
JAVA_ARGS_4="$4"
JAVA_ARGS_5="$5"

echo -e "\n** config: ${JAVA_ARGS} **\n"

# superceded by http://www.oracle.com/technetwork/java/tuning-139912.html
JAVA_TUNE='-server -Xms500m -Xmx1000m'


java ${JAVA_TUNE} -cp .:${CLIENT_HOME}/lib/'*':${CLIENT_HOME}/bin ${JAVA_MAIN} ${JAVA_ARGS_1} ${JAVA_ARGS_2} ${JAVA_ARGS_3} ${JAVA_ARGS_4} ${JAVA_ARGS_5} 
