/**
 * 
 */

async function init() {
try{
  const registration = await navigator.serviceWorker.register('/sw.js');

  await navigator.serviceWorker.ready.then(async function(registration){
	  console.log(registration);
	  firebase.initializeApp({
	    messagingSenderId: "1012820840434"
});
const messaging = firebase.messaging();
messaging.usePublicVapidKey('BD0sY_XxKfRVPHYB-dEjcrtcHiPUy-wuLd59jR-kjPNDHqCXqaEoV2wsRyh_84Ls3jsyIKpsA6qhF2384aNO5KQ');
messaging.useServiceWorker(registration);	
console.log(messaging)

	  try {
		  await messaging.requestPermission();
		    const currentToken = await messaging.getToken();
		    console.log(currentToken)
			
			  url='/push/register/'+currentToken;
			  fetch(url);
		  } catch (e) {
		    console.log('Unable to get permission', e);
		    return;
		  }

// navigator.serviceWorker.addEventListener('message', event => {
// if (event.data === 'newData') {
// showData();
// }
// });
		
		  console.log(url)
		  messaging.onTokenRefresh(async () => {
	    console.log('token refreshed');
	    const newToken = await messaging.getToken();
	    fetch(url);
	  });
		  
	  
  });
}

catch(e){
	console.log(e);
}
}



document.getElementById('but').addEventListener('click',e=>{
init();
})

