/*-----------------------------------------------------**
 * IWShelpr.c
 *
 * Copyright (c) 2006 Icuiti Corporation.  All rights reserved.
 *
**-----------------------------------------------------*/
#define DRIVER
#define INITGUID
#include <wdm.h>
#include <usbdi.h>
#include <usbdlib.h>
#include <iwshelpr.h>
#include <wdmguid.h>
#include <hidclass.h>
#include "iwrsguid.h"
#include "iwrstreo.h"
#include "sched.h"

/*- globals -------------*/
UCHAR cr[] = "Copyright (c) 2006 Icuiti Corporation. All Rights Reserved.";

KDPC kdpc;
KTIMER myTimer;
PDEVICE_OBJECT sdo;
PFILE_OBJECT sfo;
UCHAR side = 0;

PVOID NotificationEntry = NULL;


/*- end of globals ------*/

/*- externs -------------*/
/*- end of externs ------*/


VOID IWSHELPR_FlipStereo(IN PVOID DeferredContext)
{
	PIWRS_VENDOR_REQUEST req;
	DEVICEREPORT dr;
	KEVENT evt;
	IO_STATUS_BLOCK iostat;
	PIRP Irp;
	PDEVICE_OBJECT sdo = (PDEVICE_OBJECT)DeferredContext;


	DbgPrint(("IWSHELPR.SYS: FlipStereo\n"));
	req = ExAllocatePoolWithTag(NonPagedPool, sizeof(IWRS_VENDOR_REQUEST)+sizeof(DEVICEREPORT), 'IWRS');
	RtlZeroMemory(req, sizeof(IWRS_VENDOR_REQUEST)+sizeof(DEVICEREPORT));

	req->Hdr.bRequest = IWRSTREO_BREQUEST_CODE;
	req->Hdr.wValue = IWRSTREO_WVALUE_CODE;
	req->Hdr.wIndex = IWRSTREO_WINDEX_CODE;
	req->Hdr.wLength = sizeof(DEVICEREPORT) + 1;

	RtlZeroMemory(&dr, sizeof(dr));
	dr.repMode = SET_STEREO_LEFT + side;
	req->Data[0] = IWRSTREO_REPORT_ID;
	RtlCopyMemory(&(req->Data[1]),&dr,sizeof(dr));


	KeInitializeEvent(&evt, SynchronizationEvent, FALSE);
	Irp = IoBuildDeviceIoControlRequest(
		IOCTL_IWRS_VENDOR_REQUEST,
		sdo,
		req,
		sizeof(IWRS_VENDOR_REQUEST)+sizeof(DEVICEREPORT),
		NULL,0,
		TRUE,
		&evt,
		&iostat);

	//IoFreeIrp(Irp); // Until we call IoCallDriver
	if (IoCallDriver(sdo,Irp) == STATUS_PENDING) {
		KeWaitForSingleObject(&evt,Executive,KernelMode,TRUE,NULL);
	}
	side ^= 1;
	dr.repMode = 1;
	ExFreePool(req);
}

VOID  IWSHELPR_TimerDpc(IN PKDPC  pDpc,
			   IN PVOID  DeferredContext,
			   IN PVOID  SystemArgument1,
			   IN PVOID  SystemArgument2
			   )
{
	DbgPrint(("IWSHELPR.SYS: Timer\n"));
	KeWaitForSingleObject(&myTimer,Executive,KernelMode,TRUE,NULL);
	IWSHELPR_Schedule(IWSHELPR_FlipStereo, DeferredContext);
}

VOID IWSHELPR_StartPollTimer(VOID)
{
	NTSTATUS ntStatus;
	OBJECT_ATTRIBUTES ObjAttr;
	UNICODE_STRING keyName;
	PWSTR symList;
	LARGE_INTEGER li;
	GUID IWS_guid = GUID_CLASS_IWEAR_STEREO_DRIVER;

	DbgPrint(("IWSHELPR.SYS: StartPollTimer\n"));
	ntStatus = IoGetDeviceInterfaces(&IWS_guid, NULL, 0, &symList);
	if (ntStatus == STATUS_SUCCESS) {
		RtlInitUnicodeString(&keyName, symList);
		ntStatus = IoGetDeviceObjectPointer(&keyName, FILE_ALL_ACCESS, &sfo, &sdo);
		if (ntStatus == STATUS_SUCCESS) {
			ntStatus = ObReferenceObjectByPointer(sdo,FILE_ALL_ACCESS,NULL,KernelMode);
			li.QuadPart = (__int64)-160000;
			KeInitializeDpc(&kdpc, IWSHELPR_TimerDpc, sdo);
			KeInitializeTimerEx(&myTimer, NotificationTimer);
			KeSetTimerEx(&myTimer, li, 2000, &kdpc);
		}
	}
}

VOID IWSHELPR_StopPollTimer(VOID)
{
	DbgPrint(("IWSHELPR.SYS: StopPollTimer\n"));
	KeCancelTimer(&myTimer);
	KeFlushQueuedDpcs();
	ObDereferenceObject(sfo);
	ObDereferenceObject(sdo);
}

NTSTATUS IWSHELPR_PnPCallback(
	IN PVOID NotificationStructure,
	IN PVOID Context
	)
{
	DEVICE_INTERFACE_CHANGE_NOTIFICATION *pcn = NotificationStructure;
	GUID IWS_guid = GUID_CLASS_IWEAR_STEREO_DRIVER;

	DbgPrint(("IWSHELPR.SYS: PnPCallback..."));

	if (RtlEqualMemory(&(pcn->InterfaceClassGuid),&(GUID_CLASS_IWEAR_STEREO_DRIVER),sizeof(GUID))) {
		if (RtlEqualMemory(&(pcn->Event),&(GUID_DEVICE_INTERFACE_ARRIVAL),sizeof(GUID))) {
			DbgPrint(("Arrival\n"));
			IWSHELPR_StartPollTimer();
		}
		else if (RtlEqualMemory(&(pcn->Event),&(GUID_DEVICE_INTERFACE_REMOVAL),sizeof(GUID))) {
			DbgPrint(("Removal\n"));
			IWSHELPR_StopPollTimer();
		}
	}

	return(STATUS_SUCCESS);
}

/************************************************************************
**
** Called from system when the driver is about to be unloaded.
** 
** Return Codes:
**		NONE
**
************************************************************************/
void IWSHELPR_Unload(PDRIVER_OBJECT DriverObject) 
{
	DbgPrint(("IWSHELPR.SYS: Unload\n"));
	IoUnregisterPlugPlayNotification(NotificationEntry);
}

/************************************************************************
**
** Main entry point for the driver.
** 
** Return Codes:
**		STATUS_SUCCESS
**
************************************************************************/
NTSTATUS DriverEntry(
	IN PDRIVER_OBJECT DriverObject,
	IN PUNICODE_STRING RegistryPath
	) 
{
	NTSTATUS ntStatus = STATUS_SUCCESS,tmp;
	GUID IWS_guid = GUID_CLASS_IWEAR_STEREO_DRIVER;

	//GUID IWS_guid = GUID_GPIO_INTERFACE;

	DbgPrint(("IWSHELPR.SYS: entering DriverEntry\n"));

	// Create dispatch points for device control, create, close, read, write
	/*
	DriverObject->MajorFunction[IRP_MJ_SYSTEM_CONTROL] = IWSHELPR_System_Control;
	DriverObject->MajorFunction[IRP_MJ_DEVICE_CONTROL] = IWSHELPR_Device_Control;
	DriverObject->MajorFunction[IRP_MJ_INTERNAL_DEVICE_CONTROL] = IWSHELPR_Internal_Device_Control;
	DriverObject->MajorFunction[IRP_MJ_PNP_POWER]	   = IWSHELPR_PNP;
	DriverObject->MajorFunction[IRP_MJ_POWER]	       = IWSHELPR_Power;
	DriverObject->DriverExtension->AddDevice           = IWSHELPR_PnPAddDevice;
	*/
	DriverObject->DriverUnload						   = IWSHELPR_Unload;

	ntStatus = IoRegisterPlugPlayNotification(
				EventCategoryDeviceInterfaceChange,
				PNPNOTIFY_DEVICE_INTERFACE_INCLUDE_EXISTING_INTERFACES,
				&IWS_guid,
				DriverObject,
				IWSHELPR_PnPCallback,
				NULL,
				&NotificationEntry);

	//StartPollTimer();

	ntStatus = STATUS_SUCCESS;
	KdPrint(("IWSHELPR.SYS: exiting DriverEntry (%x)\n", ntStatus));

	return(ntStatus);
}


