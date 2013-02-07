#!/bin/sh
cat << EOF
Content-type: text/html

<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="refresh" content="10;url=/cgi-bin/paintPage.py">
<title>IMS Presence Service</title>
<link rel="stylesheet" href="/cb_style.css" media="screen">
</head>
<body class="claro">
<H3>Processing request</H3>
</body>
</html>
EOF


server="VSPHERESERVER"
username="VSPHEREUSER"
password="VSPHEREPASSWORD"
vm="VSPHEREVM"

./presenceService_stop.py "${server}" "${username}" "${password}" "${vm}"
