// HelloTrackerDlg.h : header file
//

#pragma once


// CHelloTrackerDlg dialog
class CHelloTrackerDlg : public CDialog
{
// Construction
public:
	CHelloTrackerDlg(CWnd* pParent = NULL);	// standard constructor

// Dialog Data
	enum { IDD = IDD_HELLOTRACKER2_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support


// Implementation
protected:
	HICON m_hIcon;

	// Generated message map functions
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()
public:
	CString m_csYaw;
	CString m_csPitch;
	CString m_csRoll;
	BOOL m_fTrackerPresent;
protected:
	virtual void OnCancel();
public:
	afx_msg void OnTimer(UINT nIDEvent);
	afx_msg void OnBnClickedButtonZero();
	void RestartPoll(void);
	HMODULE m_hIwear;
};
