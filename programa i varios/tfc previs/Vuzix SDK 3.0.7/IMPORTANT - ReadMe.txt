Vuzix Eyewear SDK 3.0.x
=======================

This version of the Vuzix Eyewear SDK is a Windows development kit for enabling Vuzix native mode stereoscopic video and head tracking for compatible Vuzix products.

System Requirements:
- Windows 7, Vista or XP, 32 or 64-bit
- Requires Vuzix VR Manager 3.0 or greater

Supported Vuzix Devices:
- iWear¨ VR920 Video Eyewear
- Wrap(ª) 920 Video Eyewear with Wrap VGA Adapter (firmware release 120 or greater)
- Wrap Tracker 6TC



---------------------------------------------------
Installation
---------------------------------------------------
Note: VR Manager 3.0.x must be installed prior to installing the SDK

- Uninstall any prior versions of the SDK.
- Quit any Vuzix software currently running.
- Run the SDK installer executable.

The SDK will be installed at C:\IWEARSDK.
Documentation is installed in C:\IWEARSDK\docs



---------------------------------------------------
Wrap vs. iWear Models
---------------------------------------------------
Vuzix manufactures two model lines of video eyewear, the "iWear" and "Wrap" models. Each supports different stereoscopic video formats and different tracker systems. These differences are a result of our desire to keep abreast of the latest market trends and advancing technology. We strongly recommend you provide native support for both stereoscopic video formats and tracker systems.

iWear VR920: This is the most prevent model of VR enabled video eyewear on the market today. It utilizes a frame sequential stereoscopy video format and contains an integrated 3 degrees of freedom head tracker.

Wrap 920, w/Wrap VGA Adapter: This is the newest model of VR enabled video eyewear offered by Vuzix. The Wrap 920 is a more universal design that connects to vitally any composite video device and when equipped with an optional Wrap VGA Adapter, it can connect to the VGA port on a PC, drawing power and communicating data through a USB 2.0 connection. An optional Wrap Tracker 6TC (6 degree of freedom tracker with compass) can also be connected to a Wrap 920 to enable it with full virtual reality capabilities. The Wrap 920 supports a side-by-side format of stereoscopic video. This format has become very popular of late. The Wrap Tracker 6TC support provided by this SDK is for 3 degrees of freedom; pitch, yaw and roll. The tracker is capable of 6 degrees of freedom using 3rd party algorithms. Additional information on future SDKs.

Note: The Wrap VGA Adapter must be contain firmware version 120 or greater, available for download from the Vuzix website (www.vuzix.com)
