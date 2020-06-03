/**
 * 
 */


$(document).ready(()=>{
	initusersTable();
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
)

function changeSelectUser(value){
	
	let state=parseInt(document.getElementById('state').value);
	if(state===2 || state===3){
	let userBtn=document.getElementById('userBtn');
	console.log(userBtn.style)
	if(value ==='nullOpt')
		userBtn.style.visibility='hidden';
	else
		userBtn.style.visibility='';
	}
}

async function JsontoString(){
	let json=JSON.stringify(tableToJson());
	console.log(json);
	let hidden=document.getElementById('hiddenId');
	hidden.value=json;
	
	return hidden;
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



function addUser(){
	let table=document.getElementById('usersBody');
	
	
	let selectUser=document.getElementById('selectUser');
	let auxName=selectUser.options[selectUser.selectedIndex].innerHTML;
	let name=auxName.split('/')[0];
	let username=selectUser.options[selectUser.selectedIndex].value;

	
	$('#usersTable').DataTable().row.add([username,name]).draw();
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
    
    
    
}






async function submitFormEdit(){
    let table=$('#usersTable').DataTable();
    table.column( 0 ).visible( true );
	let a =await JsontoString();
	bootbox.confirm('¿Está seguro que desea continuar?',result=>{
		
		if(result)
			$("#accordFormPrin").submit();
			
	})

}


