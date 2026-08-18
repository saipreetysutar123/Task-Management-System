#!/bin/sh

if [ -f /etc/secrets/isrgrootx1.pem ]; then
    keytool -importcert \
        -noprompt \
        -trustcacerts \
        -alias tidb-ca \
        -file /etc/secrets/isrgrootx1.pem \
        -keystore "$JAVA_HOME/lib/security/cacerts" \
        -storepass changeit
fi

exec java -jar /app/app.jar