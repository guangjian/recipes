#!/bin/sh
echo "Content-type: text/html"
echo ""

/var/www/cgi-bin/killLoad.cgi

cat << EOF
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="refresh" content="15;url=/cgi-bin/paintMainPage.cgi">
<title>DNS Application Scaling Demo Load Generator</title>
<link rel="stylesheet" href="/cb_style.css" media="screen">
</head>
<P>
<body>
<H3>
Stopping load against DNS server ....
</H3>
</body>
</html>
EOF
