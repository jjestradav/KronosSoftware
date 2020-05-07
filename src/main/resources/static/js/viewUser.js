/**
 * 
 */

$(document).ready(()=>{
	$("#comboDepartments").css("pointer-events","none");
	
})

function changePassword(checkbox){
	let passInput=document.getElementById('password').type;
	if(checkbox.checked)
		passInput='text'
	else
		passInput='password';
}