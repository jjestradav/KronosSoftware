/**
 * 
 */

globalAccord=null;

$(document).ready(()=>{
	let name = $("#accNumber").val();
	(async ()=>{
		let b = await listPdf(name);
		$("#comboTypes").on('change',comboBoxType)
		
		}
	

)();
	
	fetch('/api/notifications/getResponsablesDep/'+name).
	then(response=>{
		console.log(response);
		if(response.status !== 200)
			throw 'Error al cargar los responsables';
		return response.json();
	})
	.then(resp=>{
		console.log(resp);
		if(resp.length>0){
			//document.getElementById('selectDepartment').disabled=true;
			//document.getElementById('okButton').style.visibility='hidden';
			document.getElementById('okButton').style.visibility='show';
			document.getElementById('accordFormPrin').action='/townHall/edit/newResponsables';
		}
		 var parent = $("#usersBody");
         parent.html("");
         resp.forEach(item => {
             listUser(parent, item);
         });
		
	})
	.then(()=>initusersTable())
	.catch(ex=>console.log(ex))
})	


async function listPdf(accNumber){
	
	let url='/api/accords/get/'+accNumber
	fetch(url).then(response=>response.json())
	.then(accord => {
		globalAccord=accord;
		console.log(accord);
		let parent=$("#pdfBody");
		accord.url.forEach(urlObj=>{
			list(parent,urlObj)
		})
	}).finally(()=>{
		initTable()
	})
	
}



function isInRole(){
	let role = document.getElementById('role').value;
	return (role != 'Concejo Municipal') ? "disabled" : "";
}

function list(parent,urlObj){
	console.log(isInRole());
	var tr=$("<tr/>");
	let urlAux=urlObj.url;
	let role = document.getElementById('role').value;
	let names=urlAux.split('/');
	console.log(names);
	let finalName=names[names.length-1];
	let approved=parseInt(urlObj.isApproved,10);
	if(approved===1)
		finalName+='(Pendiente)';
	else
		if(approved===2)
			finalName+='(Aprobado)';
		else
			if(approved===3)
				finalName+='(Rechazado)';
				
			
	tr.html(
	"<td>"+finalName+"</td>"+
	"<td><button type=\"button\" class=\"btn btn-success\" onclick=\"javascript:openPdf('" + urlObj.url + "')\">Abrir</button></td>"+	
	"<td><button type=\"button\" class=\"btn btn-danger\" onclick=\"javascript:deletePdf('" + urlObj.url + "','"+finalName+"')\" "+isInRole()+">Eliminar</button></td>"+
	"<td><input type=\"radio\" name=\"finalResponse\" id=\""+finalName+"checkBox\" value=\""+finalName+"\""+isInRole()+" style=\"text-aling: center; vertical-align:middle \"></td>"+
	"<td><select class=\"form-control\" id=\"approvedPdf_"+finalName+"\""+disableSelect(urlObj)+" >"+
	"<option value='-1'>Seleccionar Opcion</option>" +
	"<option value='3'>Rechazar</option>" +
	"<option value='2'>Aprobar</option>" +
	"</select>" +
	"</td>"
	);
	tr.attr('id',finalName);
	parent.append(tr);
	let checkBox=document.getElementById(finalName+"checkBox");
	if(urlObj.finalResponse)
		checkBox.checked=true;
		
	let comboBox=document.getElementById('approvedPdf_'+finalName)
	setSelectedIndex(comboBox,urlObj);
		comboBox.onchange=(function(){
			updateUrlObj(urlObj, comboBox);
		});
}

function setSelectedIndex(comboBox,urlObj){
	
	let value=parseInt(urlObj.isApproved,10);
	if(value===0 || value===1)
		comboBox.selectedIndex=0;
	else
		if(value===2)
			comboBox.selectedIndex=2;
		else
			if(value===3)
				comboBox.selectedIndex=1;
	
}
function disableSelect(urlObj){
	let value=parseInt(urlObj.isApproved,10);
	
	return (value===1)? "": "disabled";
}
function openPdf(pdf){

	let url='/api/accords/getPdf?path='+pdf;
	fetch(url)
	.then(response=>response.blob())
	.then(data=>{
		console.log(data)
		window.open(URL.createObjectURL(data))
		});
	
}

function deletePdf(pdf,finalName){
	let table=document.getElementById('pdfTable');
	if(table.rows.length-1 === 1){
		bootbox.alert("Por favor agregue otro documento antes de eliminar");
	}
	else
	bootbox.confirm("¿Esta seguro que desea eliminar este archivo ?", result => {
		
		if(result){
			
			let url='/api/accords/deletePdf/'+globalAccord.accNumber+'?path='+pdf;
			fetch(url)
			.then(()=>{document.getElementById(finalName).remove()});
			
			}
		})
		
	}
	
function comboBoxType(){
	let comboType= $("#comboTypes").val();
	let username=$("#username");
	let email=$("#email");
	
	if(parseInt(comboType,10) !== 1){
		username.prop("disabled",false)
		email.prop("disabled",false)
	}
	else{
		username.val('');
		email.val('');
		username.prop("disabled",true)
		email.prop("disabled",true)
	}
		
}


function uploadPdf(){
	console.log(globalAccord);
	let _url='/api/accords/uploadPdf/'+globalAccord.accNumber;
	let form=document.getElementById('accordForm');
	
	let count= $("input:file")[0].files.length;
		
		let _data=new FormData(form);		
		
	 $.ajax({
	        type: "POST",
	        encType: "multipart/form-data",
	        url: _url,
	        cache: false,
	        processData: false,
	        contentType: false,
	        data: _data,
	        success : accord=>{

	        		if(accord.state.id === 0){
	        			document.getElementById('state').value=0;
	        		}
	        		updateTable();

	        },
            
	        error: function (response) {
	            console.log(response);
	           
	        }
	    })
		

}

function updateTable(){
	$('#pdfTable').DataTable().clear().destroy();
	listPdf(globalAccord.accNumber)
}


function initTable() {
    $('#pdfTable').DataTable({
        "language": {
            "lengthMenu": "Mostrar _MENU_ Acuerdos",
            "zeroRecords": "",
            "info": "Mostrando Acuerdos del _START_ al _END_ de un total de _TOTAL_ registros",
            "infoEmpty": "Mostrando Acuerdos del 0 al 0 de un total de 0 registros",
            "infoFiltered": "(filtrado de un total de _MAX_ Acuerdos)",
            "sSearch": "Filtrar:",
            "oPaginate": {
                "sFirst": "Primero",
                "sLast": "Último",
                "sNext": "Siguiente",
                "sPrevious": "Anterior"
            },
            "sProcessing": "Procesando..."
        },
        "lengthChange": false,
        "destroy": true,
        "searching":false,
        "info": false,
        "iDisplayLength": 3
    });
    

}


function initusersTable() {
    $('#usersTable').DataTable({
        "language": {
            "lengthMenu": "Mostrar _MENU_ Acuerdos",
            "zeroRecords": "",
            "info": "Mostrando Acuerdos del _START_ al _END_ de un total de _TOTAL_ registros",
            "infoEmpty": "Mostrando Acuerdos del 0 al 0 de un total de 0 registros",
            "infoFiltered": "(filtrado de un total de _MAX_ Acuerdos)",
            "sSearch": "Filtrar:",
            "oPaginate": {
                "sFirst": "Primero",
                "sLast": "Último",
                "sNext": "Siguiente",
                "sPrevious": "Anterior"
            },
            "sProcessing": "Procesando..."
        },
        "lengthChange": false,
        "destroy": true,
        "searching":false,
        "info": false,
        "iDisplayLength": 3
    });
    
    let table=$('#usersTable').DataTable();
    table.column( 0 ).visible( false );
    $('#usersBody').on('dblclick', 'tr', function () {
    	
    	bootbox.confirm('Está seguro que desea eliminar este usuario',result => {
    		console.log(result)
    		if(result){
    		    table
    	        .row(this)
    	        .remove()
    	        .draw();
    		}
    	});

    	} );
}


function deleteAccord(){

	bootbox.confirm("¿Está seguro que desea eliminar el acuerdo?",result=>{
		
		if(result){
			let url='/accords/deleteAccord/'+globalAccord.accNumber;
			 fetch(url).then(()=>{
				 window.location.replace('/accords/list');
			 })
		}
		
	})

}

function cleanPdfForm(){
	if(globalAccord != null){
		let finalResponseArray = globalAccord.url.filter(item=>item.finalResponse==true);
		if(!finalResponseArray.length){
			let radioButtons=document.getElementsByName("finalResponse");
			for(let radio of radioButtons){
				radio.checked=false;
			}
		}
		
	}
	document.getElementById('accord').value='';
}


function changeSelectDepartments(select){
	let addDprmntButton=document.getElementById('addDprmntButton');
	
	if(parseInt(select.value,10) !== -1)
		addDprmntButton.style.visibility='';
	
	else
		addDprmntButton.style.visibility='hidden';
}
		


function hideUsers(){
	 document.getElementById('users').style.visibility='hidden';
	 document.getElementById('userLabel').style.visibility='hidden';
	 document.getElementById('addUserButton').style.visibility='hidden';
}

function showUsers(){
	document.getElementById('addUserButton').style.visibility='';
	document.getElementById('users').style.visibility='';
	document.getElementById('userLabel').style.visibility='';
}

function addDprmnt(){
	let table=document.getElementById('usersBody');
	
	
	let selectDeps=document.getElementById('selectDepartment');
	let depName=selectDeps.options[selectDeps.selectedIndex].innerHTML;
	let depID=selectDeps.options[selectDeps.selectedIndex].value;

	
	$('#usersTable').DataTable().row.add([depID,depName]).draw();
}




function tableToJson() {	
var myRows = [];
var $headers = $("th");
var $rows = $("#usersBody tr").each(function(index) {
  $cells = $(this).find("td");
  myRows[index] = {};
  $cells.each(function(cellIndex) {
    myRows[index][$($headers[cellIndex]).html()] = $(this).html();
  });    
});



return myRows;
}

async function JsontoString(){
	let json=JSON.stringify(tableToJson());
	console.log(json);
	let hidden=document.getElementById('hiddenId');
	hidden.value=json;
	
	return hidden;
}

async function submitForm(){
    let table=$('#usersTable').DataTable();
    table.column( 0 ).visible( true );
	let a =await JsontoString();
	$("#accordFormPrin").submit();
}


function listUser(parent, item) {


    var tr = $("<tr/>");
    tr.html(
            "<td>" +item.id + "</td>" +
            "<td>" +item.name + "</td>"

            );
    parent.append(tr);
  
}

async function submitFormEdit(){
	let a =await JsontoString();
	$("#accordFormPrin").submit();
}

function updateUrlObj(urlObj,comboBox){
	
	let value=parseInt(comboBox.value,10);
	
	if(value!==-1){
		
		bootbox.confirm('Esta Accion no puede ser desecha. ¿Desea continuar?',confirm=>{
			if(confirm){
				let names=urlObj.url.split('/');
				let fileName=names[names.length-1];
				let dir='/api/accords/updateUrl/'+globalAccord.accNumber+'/'+fileName+'/'+value;
				console.log(value);
				fetch(dir).then(response=>{
					if(!response.ok)
						throw new Error();
					
					comboBox.disabled=true;
					updateTable();
					toastr.success('Solicitud Exitosa');
					
				}).catch(err=>{
					bootbox.alert("Ha ocurrido un error. Por favor intentar luego");
				})
			}
			else{
				comboBox.selectedIndex=0;
			}
			
		})	
	
	
	}
}

