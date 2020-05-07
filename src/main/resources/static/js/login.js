/**
 * 
 */

function forgot(){
	let username=document.getElementById('email').value;
	console.log(username);
	fetch('/api/password/forgot/'+username)
	.then(response=>{
		console.log(response);
		if(response.status===500)
			bootbox.alert("Error Interno. Por favor intente más tarde");
		else
			if(response.status===404)
				bootbox.alert("Error. El correo digitado no se encuentra en el sistema");
				
			else
				bootbox.alert("Operacion exitosa. Por favor revise su correo");
				
		
	})
}