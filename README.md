# OfferUp Android Challenge #

Build a simple application that allows a user to complete following sequential process:

1. Login/Logout with their Facebook account.

2. Select an image through either the Camera or the Gallery(or equivalent).

3. Upload the image to our Facebook test account.

### Android Challenge Instructions ###

* The base Android project is already setup with Gradle.
* Add any libraries as desired
* Any referenced assets are included in the drawable-xhdpi folder
* Ensure that the project is build with the included gradlew executable
* Use Facebook username/password androidteam+challenge@offerupnow.com / AndroidChallenge!@
* Deployment instructions

## Screens ##

#### Main Screen ####

* This screen shows after the app starts. 
* The ‘Upload Image’ button is not enabled until the user has logged in with Facebook. 


![pic1.png](https://bitbucket.org/repo/oyee85/images/1416646019-pic1.png)

#### Image Upload Screen ####

* The “Upload to Facebook” button is disabled until at least one image has been provided for upload.
* Clicking the image placeholder brings up an option to select photo from the camera and gallery
* Optionally use a library for this chooser such as https://github.com/coomar2841/image-chooser-library but be prepared to explain how it works
* The image is then displayed cropped inside the same area that was occupied by the + button. 
* Clicking the image also displays the chooser

image2

image3

#### Upload Confirmation Screen ####

* Show this screen after the upload has completed
* The screen will show one button “Done” which will take the user back to the Main Screen to start the process again.

image4

### Who do I talk to? ###

* For any questions, please email challenge@offerupnow.com
* For immediate assistance, call 206-478-4160