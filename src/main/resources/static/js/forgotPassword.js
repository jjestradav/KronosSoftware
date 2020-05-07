/**
 * 
 */
document.addEventListener("DOMContentLoaded", function(){
	
	
	const urlParams = new URLSearchParams(window.location.search);
	const username = urlParams.get('username');
	document.getElementById('username').value=username;
	document.getElementById('username').readOnly=true;
	const token = urlParams.get('token');
	//verifyToken(username,token);
	
	
});

function verifyToken(username,token){
	
	let url='/api/password/verify/'+username+'/'+token;
	
	fetch(url)
	.then(response=>{
		if(response.ok){
			return;
		}
		else
		if(response.status===404){
			bootbox.alert("Error! Usuario o token no encontrado");
		}
		else 
			if(response.status===500){
			
				bootbox.alert("Error interno! Por favor intente más tarde");
		}
			else if(response.status===403){
				bootbox.alert("Error interno! El token no coincide");
			}
				
				else if(response.status===410){
					bootbox.alert("Error! El tiempo de la solicitud ha expirado.\n " +
							"Por favor vuelva a solicitar el token al correo")
				}
			
		
		document.getElementById('subBtn').disabled='true';
		document.getElementById('confirm_password').disabled='true';
		document.getElementById('password').disabled='true';
		
		
	})
	
}





function send(){
	

const username=document.getElementById('username').value;
const password= document.getElementById('password').value;
const confirmPass= document.getElementById('confirm_password').value;


if(password !== confirmPass)
	bootbox.alert("Error! Las contraseñas no coinciden");


else {

const csrfToken=document.getElementById('token').value;
const headers = new Headers({
	'Content-Type': 'application/json',
    'X-CSRF-TOKEN': csrfToken
});
const urlParams = new URLSearchParams(window.location.search);
const token = urlParams.get('token');
let userObj={
		
username:username,
password:password,
token:token	
};
fetch('/api/password/reset',{
	method:'post',
	headers,
    credentials: 'include',
	body:JSON.stringify(userObj)
})
.then(response=>{
	if(!response.ok)
		bootbox.alert("Ha ocurrido un error!")
	else
		bootbox.alert("Operacion Exitosa");
})	
	
	}
return false;
}