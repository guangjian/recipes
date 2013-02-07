#!/usr/bin/python
import sys
import vSphereFunctions

print "Content-type: text/html"
print
print '''<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="refresh" content="5;url=/cgi-bin/paintPage.py">
<title>IMS Presence Service</title>
<link rel="stylesheet" href="/cb_style.css" media="screen">
</head>
<body class="claro">
<table align="center" width="100%">
<tr>
<td>'''

server="VSPHERESERVER"
username="VSPHEREUSER"
password="VSPHEREPASSWORD"
vm="VSPHEREVM"

if vSphereFunctions.vm_state(server,username,password,vm) == "ON":
	print ('<form name="stoppresence" action="/cgi-bin/presenceService_stop.cgi" method="get">')
	print '''<br>
	<input type="submit" value="Stop Presence Service">
	</form>
	</td>
	</tr>'''
elif vSphereFunctions.vm_state(server,username,password,vm) == "OFF":
	print ('<form name="startpresence" action="/cgi-bin/presenceService_start.cgi" method="get">')
	print '''<br>
	<input type="submit" value="Start Presence Service">
	</form>
	</td>
	</tr>'''
else:
	print '''Unknown Presence service state.
	</td>
	</tr>'''


print '''</body>
</html>'''