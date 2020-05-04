/**
 * 
 */

document.addEventListener("DOMContentLoaded", function () {

    initTable();
    //subsButton();
});

function initTable() {
    $('#tableAct').DataTable({
        "language": {
            "lengthMenu": "Mostrar _MENU_ Actas",
            "zeroRecords": "No se encontraron resultados",
            "info": "Mostrando Actas del _START_ al _END_ de un total de _TOTAL_ registros",
            "infoEmpty": "Mostrando Actas del 0 al 0 de un total de 0 registros",
            "infoFiltered": "(filtrado de un total de _MAX_ Actas)",
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
        "iDisplayLength": 5,
        "destroy": true
        
    });
}

function changeSelect(value) {
    let field = document.getElementById('searchText');
    let button = document.getElementById('searchButton');
    let combo = document.getElementById('selectType');
    console.log(field);
    if (value === 'sessionDate') {
        field.type = 'text';
        $("#searchText").datepicker({dateFormat: 'dd-mm-yy'});
        field.value = "";
        button.style.visibility = 'visible';
        combo.value = 'A';
        combo.style.visibility = 'hidden';
        combo.value = 'A';

    } else if (value === 'sessionType') {
        field.value = "";
        field.type = 'hidden';
        button.style.visibility = 'visible';
        combo.value = 'A';
        combo.style.visibility = 'visible';
    } else if (value === 'allActs') {
        field.value = "";
        field.type = 'hidden';
        button.style.visibility = 'visible';
        combo.value = 'A';
        combo.style.visibility = 'hidden';
    }  else {
        field.value = "";
        combo.value = 'A';
        field.type = 'hidden';
        button.style.visibility = 'hidden';
        combo.style.visibility = 'hidden';
    }



}



function searchAct() {
	let titleDiv=document.getElementById('titleh3');
    let parameter = document.getElementById('searchAct').value;
    if (parameter !== 'notSelected') {
        switch (parameter) {
            case 'sessionDate':
            	titleDiv.innerHTML='Acta Por Fecha de Sesión'
                searchBySessionDate();
                break;

            case 'sessionType':
            	titleDiv.innerHTML='Actas Por Tipo de Sesión';
                searchBySessionType();
                break;

            case 'allActs':
            	titleDiv.innerHTML='Todos las Actas';
                searchAllActs();
                break;
                
            case 'deactivatedActs':
            	titleDiv.innerHTML='Actas Desactivadas';
            	searchDeactivatedActs();
            	break;

        }
    }
}


var actCounter=0;
function list(parent, act) {
    var tr = $("<tr/>");
    tr.html(
             "<td>" + formatDate(act.sessionDate) + "</td>"
            + "<td>" + toUpper(act.sessionType) + "</td>"
            + "<td> <a href=\"/act/edit/"+ formatDate(act.sessionDate)+"\">" + "<button type=\"button\" style='text-align: center' class=\"btn btn-primary\">"
            + "Editar</button></a></td>"
            );
    parent.append(tr);
 

    actCounter++;

}

function searchBySessionDate() {
    let sessiondate = document.getElementById('searchText').value;
    let _url = "/api/act/sessionDate/" + sessiondate;
    console.log(_url);

    fetch(_url)
            .then(res =>
                res.json()
            )
            .then(act => {
            	console.log(act);
                $('#tableAct').DataTable().clear().destroy();
                var parent = $("#actList");
                parent.html("");
                act.forEach(item => {
                    list(parent, item);
                });

            })
            .then(() => {
                initTable();
            })
            .catch(error => {
                console.log(error);
            });
}

function searchBySessionType() {
    let type = document.getElementById('selectType').value;
    let _url = "/api/act/sessionType/" + type;

    fetch(_url)
            .then(res =>
                res.json()
            )
            .then(act => {
                $('#tableAct').DataTable().clear().destroy();
                var parent = $("#actList");
                parent.html("");
                act.forEach(item => {
                    list(parent, item);
                });

            })
            .then(() => {
                initTable();
            })
            .catch(error => {
                console.log(error);
            });
}

function searchAllActs() {
    let _url = "/api/act/allActs";
    fetch(_url)
            .then(res => {
                console.log(res);
                return res.json();
            })
            .then(act => {
                $('#tableAct').DataTable().clear().destroy();
                var parent = $("#actList");
                parent.html("");
                act.forEach(item => {
                    list(parent, item);
                });
            }).then(() => {

        initTable();
    })

            .catch(error => {
                console.log(error);
            });
}

function searchDeactivatedActs() {
    let _url = "/api/act/deactivatedActs";
    fetch(_url)
            .then(res => {
                console.log(res);
                return res.json();
            })
            .then(act => {
                $('#tableAct').DataTable().clear().destroy();
                var parent = $("#actList");
                parent.html("");
                act.forEach(item => {
                    list(parent, item);
                });
            }).then(() => {

        initTable();
    })

            .catch(error => {
                console.log(error);
            });
}

function formatDate(date) {
	let d=stringToDate(date);
    var month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [day, month, year].join('-');
}

function stringToDate(str){
	return new Date(str.split('-').join('/'));
	
}

function toUpper(str){
	let first = str.charAt(0)
	let upper = first.toUpperCase()
	return upper+str.slice(1)
}




















