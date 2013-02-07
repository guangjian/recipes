# A vmWare VM controller module used by other python scripts:

import sys
from pysphere import VIServer


def vm_stop(server, username, password, vmID):
        s_server=str(server)
        s_username=str(username)
        s_password=str(password)
        s_vm=str(vmID)
        server = VIServer()
        server.connect(s_server, s_username, s_password)
        vm1 = server.get_vm_by_name(s_vm)
        vm1.suspend()
        return

def vm_start(server, username, password, vmID):
        s_server=str(server)
        s_username=str(username)
        s_password=str(password)
        s_vm=str(vmID)
        server = VIServer()
        server.connect(s_server, s_username, s_password)
        vm1 = server.get_vm_by_name(s_vm)
        vm1.power_on()
        return

        
def vm_state(server, username, password, vmID):
        s_server=str(server)
        s_username=str(username)
        s_password=str(password)
        s_vm=str(vmID)
        server = VIServer()
        server.connect(s_server, s_username, s_password)
        vm1 = server.get_vm_by_name(s_vm)
        if vm1.is_suspended() or vm1.is_powered_off():
                return "OFF"
        elif vm1.is_powered_on():
                return "ON"
        else:
                return "UNK"