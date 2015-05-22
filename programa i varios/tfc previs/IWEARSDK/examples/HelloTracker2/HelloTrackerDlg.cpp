// HelloTrackerDlg.cpp : implementation file
//

#include "stdafx.h"
#include <dbt.h>
#include "HelloTracker2.h"
#include "HelloTrackerDlg.h"
#define IWEARDRV_EXPLICIT
#include <iweardrv.h>
#include ".\hellotrackerdlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

#define POLL_TIMER 10


// CAboutDlg dialog used for App About

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

// Implementation
protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnTimer(UINT nIDEvent);
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)

END_MESSAGE_MAP()


// CHelloTrackerDlg dialog



CHelloTrackerDlg::CHelloTrackerDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CHelloTrackerDlg::IDD, pParent)
	, m_csYaw(_T(""))
	, m_csPitch(_T(""))
	, m_csRoll(_T(""))
	, m_fTrackerPresent(FALSE)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CHelloTrackerDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_STATIC_YAW, m_csYaw);
	DDX_Text(pDX, IDC_STATIC_PITCH, m_csPitch);
	DDX_Text(pDX, IDC_STATIC_ROLL, m_csRoll);
}

BEGIN_MESSAGE_MAP(CHelloTrackerDlg, CDialog)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	//}}AFX_MSG_MAP
	ON_WM_TIMER()
	ON_BN_CLICKED(IDC_BUTTON_ZERO, OnBnClickedButtonZero)
END_MESSAGE_MAP()


// CHelloTrackerDlg message handlers

BOOL CHelloTrackerDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Add "About..." menu item to system menu.

	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	// TODO: Add extra initialization here

	m_hIwear = LoadLibrary(_T("IWEARDRV.DLL"));
	if (m_hIwear) {
		IWROpenTracker = (PIWROPENTRACKER)GetProcAddress(m_hIwear, _T("IWROpenTracker"));
		IWRCloseTracker = (PIWRCLOSETRACKER)GetProcAddress(m_hIwear, _T("IWRCloseTracker"));
		IWRZeroSet = (PIWRZEROSET)GetProcAddress(m_hIwear, _T("IWRZeroSet"));
		IWRGetTracking = (PIWRGETTRACKING)GetProcAddress(m_hIwear, _T("IWRGetTracking"));
		IWRSetFilterState = (PIWRSETFILTERSTATE)GetProcAddress(m_hIwear, _T("IWRSetFilterState"));
		RestartPoll();
	}
	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CHelloTrackerDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CHelloTrackerDlg::OnPaint() 
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CHelloTrackerDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}


void CHelloTrackerDlg::OnCancel()
{
	// TODO: Add your specialized code here and/or call the base class
	if (m_fTrackerPresent && IWRCloseTracker) {
		m_fTrackerPresent = FALSE;
		IWRCloseTracker();
	}
	CDialog::OnCancel();
}

void CHelloTrackerDlg::RestartPoll(void)
{
	if (IWROpenTracker() == ERROR_SUCCESS) {
		if (IWRSetFilterState) IWRSetFilterState(TRUE);
		m_fTrackerPresent = TRUE;
		SetTimer(POLL_TIMER,POLL_TIMER,NULL);
	}
	else {
		m_fTrackerPresent = FALSE;
		SetTimer(POLL_TIMER,1000,NULL);
	}
}

void CHelloTrackerDlg::OnTimer(UINT nIDEvent)
{
	if (nIDEvent == POLL_TIMER) {
		LONG yaw = 0,pitch = 0,roll = 0;
		if (IWRGetTracking(&yaw, &pitch, &roll) != ERROR_SUCCESS) {
			RestartPoll();
		}
		UpdateData(TRUE);
		m_csYaw.Format(_T("%d"), yaw);
		m_csPitch.Format(_T("%d"), pitch);
		m_csRoll.Format(_T("%d"), roll);
		UpdateData(FALSE);
	}
	CDialog::OnTimer(nIDEvent);
}

void CHelloTrackerDlg::OnBnClickedButtonZero()
{
	// TODO: Add your control notification handler code here
	if (m_fTrackerPresent) {
		IWRZeroSet();
	}
}
