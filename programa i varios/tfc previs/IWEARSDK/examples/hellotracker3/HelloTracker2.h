// HelloTracker2.h : main header file for the PROJECT_NAME application
//

#pragma once

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols


// CHelloTracker2App:
// See HelloTracker2.cpp for the implementation of this class
//

class CHelloTracker2App : public CWinApp
{
public:
	CHelloTracker2App();

// Overrides
	public:
	virtual BOOL InitInstance();

// Implementation

	DECLARE_MESSAGE_MAP()
};

extern CHelloTracker2App theApp;