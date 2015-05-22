/*-----------------------------------------------------**
 * sched.c
 * 
 * Copyright (c) 2002 Belkin Components  All rights reserved.
 *
 * $Log: /Belkin/Gaming/Driver/sched.c $
 * 
 * 2     05-08-12 5:42p Robertr
 * moved all Allocations to AloocateWithTag
 * 
 * 1     6/17/02 5:09p Robertr
**-----------------------------------------------------*/
#define DRIVER
#include <wdm.h>
#include "sched.h"

VOID IWSHELPR_SchedWorker(
	PVOID sc 
    )
{
	PSCHED_CONTEXT SchedContext = sc;
	(*(SchedContext->func))(SchedContext->context);
    ExFreePool(sc);
}	 
    
UCHAR IWSHELPR_Schedule(
	PVOID func,
    PVOID context
    )
{
	UCHAR rc = FALSE;
	PSCHED_CONTEXT scontext = ExAllocatePoolWithTag(NonPagedPool, sizeof(SCHED_CONTEXT), 'tsoN');

    if (scontext) {
		scontext->func = func;
	    scontext->context = context;
    	if (scontext != func) {
			ExInitializeWorkItem(&(scontext->Item), IWSHELPR_SchedWorker, scontext);
	        ExQueueWorkItem(&(scontext->Item), DelayedWorkQueue);
			rc = TRUE;
	    }
		else {
		    ExFreePool(scontext);
		}
	}
	return(rc);
}	
