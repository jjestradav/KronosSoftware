/**
 * 
 */

importScripts('https://www.gstatic.com/firebasejs/5.9.2/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/5.9.2/firebase-messaging.js');

firebase.initializeApp({
    messagingSenderId: "1012820840434"
});

const messaging = firebase.messaging();
console.log(messaging);
//self.addEventListener('activate', async () => {
//	 const subscription = await self.registration.pushManager.subscribe(options)
//	  const currentToken = await messaging.getToken();
//	  console.log(currentToken);
//	  url='/push/register/'+currentToken;
//	  fetch(url);
//})

self.addEventListener('push', function(event) {
	  if (event.data) {
	    console.log('Push event!! ', event.data.json());
	    var data = event.data.json();
	    console.log(data);
	    showLocalNotification(data.notification.title,data.notification.body,data.notification.icon, self.registration,data);
	  } else {
	    console.log('Push event but no data');
	  }
	});

	function showLocalNotification (title, body,iconS ,swRegistration,data) {
	  const options = {
	    body,
	    //icon:'http://localhost:8080/images/bell.png',
	    // here you can add more properties like icon, image, vibrate, etc.
	  }
	  swRegistration.showNotification(title, options)
	}

	self.addEventListener('notificationclick', function(event) {
		// url='http://localhost:8080/accords/list';
		clients.openWindow(url)
		
		});
