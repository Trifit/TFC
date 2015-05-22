/*-----------------------------------------------------**
 * sched.h
 * 
 * Copyright (c) 1999-2002 e-TEK Labs  All rights reserved.
 *
 * $Log: /Belkin/Gaming/Driver/sched.h $
 * 
 * 1     6/17/02 5:09p Robertr
**-----------------------------------------------------*/
#ifndef _SCHED_H_
#define _SCHED_H_

struct _SCHED_CONTEXT  {
	WORK_QUEUE_ITEM Item;
    VOID (*func)(PVOID);
    PVOID context;
};

typedef struct _SCHED_CONTEXT SCHED_CONTEXT, *PSCHED_CONTEXT; 
    
UCHAR IWSHELPR_Schedule(
	PVOID func,
    PVOID context
);

#endif // _SCHED_H_