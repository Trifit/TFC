/*-----------------------------------------------------**
 * i2chelpr.h
 * 
 * Copyright (c) 2006 Icuiti Corporation.  All rights reserved.
 *
**-----------------------------------------------------*/
#ifndef _I2CHELPR_H
#define _I2CHELPR_H

typedef struct _DEVICE_EXTENSION
{
	UCHAR Polling;
	UCHAR SurpriseRemove;
    UCHAR Status;
    KEVENT CallUSBDEvent;
	UCHAR WaitOnStopEvent;
	KEVENT StopEvent;
    WORK_QUEUE_ITEM ResetItem;
    WORK_QUEUE_ITEM ResetSendItem;
    WORK_QUEUE_ITEM LogicalMapItem;
    WORK_QUEUE_ITEM ResetIntrItem;
    WORK_QUEUE_ITEM RemoveItem;
    WCHAR DeviceLinkNameBuffer[MAXIMUM_FILENAME_LENGTH];
	LIST_ENTRY HidReadIrps;
	KSPIN_LOCK HidIrpLock;
	KSPIN_LOCK MapLock;
	PUCHAR ReportDescriptor;
	PIRP PollIrp;

	PDEVICE_OBJECT ThisDeviceObject;
	PDEVICE_OBJECT PhysicalDeviceObject;
	PDEVICE_OBJECT StackDeviceObject;

	// Power management stuff
	PIRP PowerIrp;
    DEVICE_POWER_STATE CurrentDevicePowerState;
    DEVICE_CAPABILITIES DeviceCapabilities;

} DEVICE_EXTENSION, *PDEVICE_EXTENSION;


typedef struct _IO_CONTEXT  {
	PIRP irp;
    PDEVICE_EXTENSION deviceExtension;
    PULONG irpcount;
    UCHAR Buffer[16];
} IO_CONTEXT, *PIO_CONTEXT;

/*- end of user defines types ------*/

/*- globals -------------*/
/*- end of globals ------*/


/*- macros -------------*/
#define GET_MINIDRIVER_DEVICE_EXTENSION(DO)  \
((PDEVICE_EXTENSION) (((PHID_DEVICE_EXTENSION)(DO)->DeviceExtension)->MiniDeviceExtension))

#define GET_NEXT_DEVICE_OBJECT(DO) \
(((PDEVICE_EXTENSION)(DO)->DeviceExtension)->NextDeviceObject)

#define GET_PHYSICAL_DEVICE_OBJECT(DO) \
(((PDEVICE_EXTENSION)(DO)->DeviceExtension)->PhysicalDeviceObject)
/*- end of macros ------*/



/*- function prototypes -------------*/
/*- end of function prototypes ------*/
#endif // end of #ifndef _I2CHELPR_H 
